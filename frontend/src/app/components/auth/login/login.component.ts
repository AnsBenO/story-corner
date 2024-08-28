import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  inject,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { faLock, faUser } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../../services/auth.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router, RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  NotificationStore,
  NotificationType,
} from '../../../store/notification.store';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: '../auth.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent {
  pswIcon = faLock;
  userIcon = faUser;
  fb = inject(FormBuilder);
  destroy = inject(DestroyRef);
  authService = inject(AuthService);
  router = inject(Router);
  notificationStore = inject(NotificationStore);
  loginForm = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  onSubmit(): void {
    this.authService
      .login(this.loginForm.getRawValue())
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe({
        next: () => {
          this.notificationStore.notify(
            "You're logged in successfully",
            NotificationType.SUCCESS
          );
          this.router.navigate(['/']);
        },
      });
  }
  public isInvalidInput(inputName: string): boolean {
    return !!(
      this.loginForm.get(inputName)?.invalid &&
      this.isDirtyAndTouched(inputName)
    );
  }

  public isDirtyAndTouched(inputName: string): boolean {
    return !!(
      this.loginForm.get(inputName)?.dirty &&
      this.loginForm.get(inputName)?.touched
    );
  }
}
