import { inject, Injectable } from '@angular/core';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { BehaviorSubject, filter, Observable } from 'rxjs';
import { InboxNotification } from '../types/inbox-notification.type';
import SockJS from 'sockjs-client';
import { MarkAsReadPayload } from '../types/mark-as-read-payload.type';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { NotificationsPage } from '../types/notifications-page.type';

@Injectable({
  providedIn: 'root',
})
export class InboxService {
  private stompClient!: CompatClient;

  private inboxNewNotificationSubject: BehaviorSubject<InboxNotification | null> =
    new BehaviorSubject<InboxNotification | null>(null);

  private http = inject(HttpClient);

  initSocketConnection(username: string) {
    const url = '//localhost:8081/inbox-socket';
    const socket = new SockJS(url);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, () => {
      this.stompClient.subscribe(
        `/user/${username}/topic/notifications`,
        (message) => {
          this.inboxNewNotificationSubject.next(JSON.parse(message.body));
        }
      );
    });
  }

  getInboxNotification(): Observable<InboxNotification> {
    return this.inboxNewNotificationSubject.asObservable().pipe(
      filter((n): n is InboxNotification => {
        return n !== null && n !== undefined;
      })
    );
  }

  markAsRead(ids: MarkAsReadPayload) {
    return this.http.put(`${environment.API_URL}/notifications/read`, ids);
  }

  getAllNotifications(page?: number) {
    return page
      ? this.http.get<NotificationsPage>(
          `${environment.API_URL}/notifications?page=${page}`
        )
      : this.http.get<NotificationsPage>(
          `${environment.API_URL}/notifications`
        );
  }
}
