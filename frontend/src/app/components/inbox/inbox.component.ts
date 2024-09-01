import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  inject,
  OnDestroy,
  OnInit,
  Output,
  signal,
  WritableSignal,
} from '@angular/core';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { InboxStore } from '../../store/inbox.store';
import { AuthService } from '../../services/auth.service';
@Component({
  selector: 'app-inbox',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './inbox.component.html',
  styleUrl: './inbox.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InboxComponent implements OnDestroy {
  @Output() onClose = new EventEmitter<void>();
  inboxStore = inject(InboxStore);
  authService = inject(AuthService);
  closingInbox: WritableSignal<boolean> = signal(false);
  xMarkIcon = faXmark;

  ngOnDestroy(): void {
    this.inboxStore.markAsRead();
  }

  toggleInboxOutsideClick($event: MouseEvent) {
    const inboxDiv = document.querySelector('#inboxDiv');
    const targetElement = $event.target as HTMLElement;
    if (!inboxDiv?.contains(targetElement)) {
      this.closeInbox();
    }
  }
  loadMore() {
    if (
      this.inboxStore.hasNext() &&
      this.authService.currentUser() !== undefined &&
      this.authService !== null
    ) {
      const nextPage = this.inboxStore.pageNumber() + 1;
      this.inboxStore.getNotifications({ page: nextPage });
    }
  }
  closeInbox() {
    this.closingInbox.set(true);
    setTimeout(() => {
      this.closingInbox.set(false);
      this.onClose.emit();
    }, 500);
  }
}
