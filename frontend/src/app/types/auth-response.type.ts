import { CurrentUser } from './current-user.type';

export interface AuthResponse {
  accessToken: string;
  user: CurrentUser;
}
