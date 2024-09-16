import {
  patchState,
  signalStore,
  withComputed,
  withMethods,
  withState,
} from '@ngrx/signals';
import { NotificationsPage } from '../types/notifications-page.type';
import { computed, inject } from '@angular/core';
import { InboxService } from '../services/inbox.service';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { tapResponse } from '@ngrx/operators';
import { of, pipe, switchMap, throwError } from 'rxjs';

const initialState: NotificationsPage = {
  data: [],
  totalElements: 0,
  pageNumber: 0,
  totalPages: 0,
  isFirst: false,
  isLast: false,
  hasNext: false,
  hasPrevious: false,
};

export const InboxStore = signalStore(
  { providedIn: 'root' },
  withState<NotificationsPage>(initialState),
  withComputed((store) => ({
    notReadCount: computed(() => store.data().filter((n) => !n.read).length),
  })),
  withMethods((store, inboxService = inject(InboxService)) => ({
    getNotifications: rxMethod<{ page?: number }>(
      pipe(
        switchMap((payload) =>
          inboxService.getAllNotifications(payload.page).pipe(
            tapResponse({
              next: (pageData) => {
                const updatedNotifications = [
                  ...store.data(),
                  ...pageData.data,
                ];
                patchState(store, {
                  ...pageData,
                  data: updatedNotifications,
                });
              },
              error: (err) => {
                console.error('Error loading notifications:', err);
                throwError(() => err);
              },
            })
          )
        )
      )
    ),

    getNewNotifications: rxMethod<void>(
      pipe(
        switchMap(() =>
          inboxService.getInboxNotification().pipe(
            tapResponse({
              next: (newNotification) => {
                patchState(store, { data: [newNotification, ...store.data()] });
              },
              error: (err) => {
                console.error(err);
                throwError(() => err);
              },
            })
          )
        )
      )
    ),

    markAsRead: rxMethod<void>(
      pipe(
        switchMap(() => {
          const ids: number[] = store
            .data()
            .filter((n) => !n.read)
            .map((n) => n.id);
          if (ids.length > 0) {
            return inboxService.markAsRead(ids).pipe(
              tapResponse({
                next: () => {
                  patchState(store, {
                    data: store.data().map((n) => ({
                      ...n,
                      read: true,
                    })),
                  });
                },
                error: (err) => {
                  console.error(err);
                  throwError(() => err);
                },
              })
            );
          } else {
            return of();
          }
        })
      )
    ),
  }))
);
