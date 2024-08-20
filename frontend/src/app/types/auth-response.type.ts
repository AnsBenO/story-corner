import { CurrentUser } from './current-user.type';

export interface AuthResponse {
  jwt: string;
  user: CurrentUser;
}
