import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';

type TNotificationState = {
  notification: TNotification;
};
export type TNotification = {
  id?: number;
  message: string | null;
  type: NotificationType | null;
};
export enum NotificationType {
  ERROR = 'ERROR',
  INFO = 'INFO',
  SUCCESS = 'SUCCESS',
}
const initialState: TNotificationState = {
  notification: { message: null, type: null },
};

export const NotificationStore = signalStore(
  { providedIn: 'root' },
  withState<TNotificationState>(initialState),
  withMethods((store) => ({
    notify(message: string, type: NotificationType) {
      const notification = { message, type };
      patchState(store, { notification });
      console.log('notifications: ', store.notification());
    },
  }))
);
