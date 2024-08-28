import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

@Injectable({
  providedIn: 'root',
})
export class InboxService {
  private messageSubject: WebSocketSubject<string> = webSocket<string>('');
  public message$: Observable<string> = this.messageSubject.asObservable();

  updateInterval(interval: number): void {
    this.messageSubject.next(JSON.stringify(interval));
  }
}
