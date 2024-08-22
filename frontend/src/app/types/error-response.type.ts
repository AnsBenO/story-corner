export interface ErrorResponse {
  type: string;
  title: string;
  status: number;
  detail: string;
  instance: string;
  service: string;
  error_category: string;
  timestamp: Date;
  path: string;
}
