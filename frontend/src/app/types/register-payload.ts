export interface RegisterPayload {
  firstName: string; // Required, max length 50
  lastName: string; // Required, max length 50
  username: string; // Required, min length 3, max length 20
  password: string; // Required, min length 8
  confirmPassword?: string;
  email: string; // Required, must be a valid email
  phone: string; // Required, length between 10 and 15, must match the pattern "^0\\d{9}$"
  country: string; // Required, max length 100
}
