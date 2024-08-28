import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  inject,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faEnvelope,
  faLock,
  faPhone,
  faUser,
  faGlobe,
} from '@fortawesome/free-solid-svg-icons';
import { matchPasswords } from './match-passwords.validator';
import { Router, RouterModule } from '@angular/router';
import { TrimInputDirective } from '../trim-input.directive';
import {
  NotificationStore,
  NotificationType,
} from '../../../store/notification.store';
import { RegisterPayload } from '../../../types/register-payload';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TrimInputDirective,
    FontAwesomeModule,
    RouterModule,
  ],
  templateUrl: './signup.component.html',
  styleUrl: '../auth.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SignupComponent {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  destroy = inject(DestroyRef);
  router = inject(Router);
  notificationStore = inject(NotificationStore);
  maxLengths = {
    firstName: 50,
    lastName: 50,
    username: 20,
    password: 8,
    country: 100,
  };

  minLengths = {
    username: 3,
    password: 8,
    phone: 10,
  };

  signupForm = this.fb.nonNullable.group(
    {
      firstName: [
        '',
        [Validators.required, Validators.maxLength(this.maxLengths.firstName)],
      ],
      lastName: [
        '',
        [Validators.required, Validators.maxLength(this.maxLengths.lastName)],
      ],
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(this.minLengths.username),
          Validators.maxLength(this.maxLengths.username),
        ],
      ],
      password: [
        '',
        [Validators.required, Validators.minLength(this.minLengths.password)],
      ],
      confirmPassword: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^0\d{9}$/)]],
      country: [
        '',
        [Validators.required, Validators.maxLength(this.maxLengths.country)],
      ],
    },
    {
      validators: matchPasswords,
    }
  );
  pswIcon = faLock;
  userIcon = faUser;
  emailIcon = faEnvelope;
  phoneIcon = faPhone;
  countryIcon = faGlobe;

  onSubmit() {
    const dataToSubmit = <RegisterPayload>this.signupForm.getRawValue();
    delete dataToSubmit.confirmPassword;
    this.authService
      .register(dataToSubmit)
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe({
        next: () => {
          this.notificationStore.notify(
            'You are now registered',
            NotificationType.SUCCESS
          );
          this.router.navigate(['/']);
        },
      });
  }

  public isInvalidInput(inputName: string): boolean {
    return !!(
      this.signupForm.get(inputName)?.invalid &&
      this.isDirtyAndTouched(inputName)
    );
  }

  public isDirtyAndTouched(inputName: string): boolean {
    return !!(
      this.signupForm.get(inputName)?.dirty &&
      this.signupForm.get(inputName)?.touched
    );
  }
}
