import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import {
  NotificationStore,
  TNotification,
} from '../../../store/notification.store';

import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NotificationComponent implements OnInit {
  notificationStore = inject(NotificationStore);
  destroy = inject(DestroyRef);
  notification$ = toObservable(this.notificationStore.notification);
  message: WritableSignal<string | null> = signal(null);
  notifications: WritableSignal<TNotification[]> = signal([]);
  private nextId = 0;
  ngOnInit() {
    this.notification$
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe((notification) => {
        if (notification.message) {
          const id = this.nextId++;
          const newNotification: TNotification = {
            id,
            message: notification.message,
            type: notification.type,
          };
          this.notifications.set([...this.notifications(), newNotification]);

          setTimeout(() => {
            this.notifications.set(
              this.notifications().filter((n) => n.id !== id)
            );
          }, 5000);
        }
      });
  }
}
