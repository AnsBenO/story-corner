export interface ErrorResponse {
  type: string;
  title: string;
  status: number;
  detail: ErrorDetails;
  instance: string;
  service: string;
  error_category: string;
  timestamp: Date;
  path: string;
}

export interface ErrorDetails {
  [field: string]: string;
}
