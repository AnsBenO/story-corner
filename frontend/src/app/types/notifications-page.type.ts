import { InboxNotification } from './inbox-notification.type';

export interface NotificationsPage {
  data: InboxNotification[];
  totalElements: number;
  pageNumber: number;
  totalPages: number;
  isFirst: boolean;
  isLast: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}
