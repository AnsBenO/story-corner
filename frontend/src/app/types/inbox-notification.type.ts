// public record NotificationDTO(
//       boolean read,
//       String message) {

// }

export interface InboxNotification {
  id: number;
  read: boolean;
  message: string;
}
