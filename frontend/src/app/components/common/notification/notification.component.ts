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
  NotificationType,
  TNotification,
} from '../../../store/notification.store';

import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import {
  faCircleCheck,
  faCircleXmark,
  faInfoCircle,
  faXmark,
  IconDefinition,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NotificationComponent implements OnInit {
  notificationStore = inject(NotificationStore);
  destroy = inject(DestroyRef);
  notification$ = toObservable(this.notificationStore.notification);
  router = inject(Router);
  message: WritableSignal<string | null> = signal(null);
  notifications: WritableSignal<TNotification[]> = signal([]);
  infoIcon = faInfoCircle;
  xMarkIcon = faXmark;
  circleXmarkIcon = faCircleXmark;
  circleCheckIcon = faCircleCheck;
  private nextId = 0;

  notificationClassMap = new Map<string, string>([
    ['ERROR', 'bg-red-300 text-red-800'],
    ['INFO', 'bg-celeste-light text-night-dark'],
    ['SUCCESS', 'bg-green-300 text-green-800'],
  ]);

  getNotificationClass(type: NotificationType | null): string {
    return this.notificationClassMap.get(type ?? '') ?? '';
  }

  getIcon(type: NotificationType): IconDefinition {
    switch (type) {
      case NotificationType.ERROR:
        return this.circleXmarkIcon;
      case NotificationType.INFO:
        return this.infoIcon;
      case NotificationType.SUCCESS:
        return this.circleCheckIcon;
    }
  }
  ngOnInit() {
    this.notification$
      .pipe(
        takeUntilDestroyed(this.destroy),
        tap((notification) => {
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
        })
      )
      .subscribe();
  }
  destroyNotification(id: number): void {
    this.notifications.set(this.notifications().filter((n) => n.id !== id));
  }
}
