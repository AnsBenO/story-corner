export interface AuthTokenPayload {
  role: string;
  name: string;
  sub: string;
  iat: number;
  exp: number;
}
