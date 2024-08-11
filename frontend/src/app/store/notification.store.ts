import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';

type TNotificationState = {
  notification: TNotification;
};
export type TNotification = {
  id?: number;
  message: string | null;
  type: NotificationType | null;
};
const initialState: TNotificationState = {
  notification: { message: null, type: null },
};

export enum NotificationType {
  ERROR,
  INFO,
}

export const NotificationStore = signalStore(
  { providedIn: 'root' },
  withState<TNotificationState>(initialState),
  withMethods((store) => ({
    notify(message: string, type: NotificationType) {
      const notification = { message, type };
      patchState(store, { notification });
    },
  }))
);
