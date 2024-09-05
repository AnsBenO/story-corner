import { CommonModule } from '@angular/common';
import {
  Component,
  DestroyRef,
  inject,
  OnInit,
  signal,
  WritableSignal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router,
  RouterModule,
} from '@angular/router';

@Component({
  selector: 'app-loader',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './loader.component.html',
  styleUrl: './loader.component.scss',
})
export class LoaderComponent implements OnInit {
  router = inject(Router);

  destroy = inject(DestroyRef);

  loading: WritableSignal<boolean> = signal(false);

  ngOnInit(): void {
    this.router.events
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe((event) => {
        if (event instanceof NavigationStart) {
          this.loading.set(true);
        } else if (
          event instanceof NavigationEnd ||
          event instanceof NavigationCancel ||
          event instanceof NavigationError
        ) {
          this.loading.set(false);
        }
      });
  }
}
