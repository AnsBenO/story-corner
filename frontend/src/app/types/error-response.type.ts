export interface ErrorResponse {
  status: number;
  title: string;
  detail: string;
  type: string; // URI string
  service: string;
  error_category: string;
  timestamp: string; // ISO 8601 date string
}
