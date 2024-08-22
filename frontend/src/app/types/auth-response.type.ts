import { CurrentUser } from './current-user.type';

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: CurrentUser;
}
