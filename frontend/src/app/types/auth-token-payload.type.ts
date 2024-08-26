export interface AuthTokenPayload {
  role: string;
  name: string;
  jit: string;
  sub: string;
  iat: number;
  exp: number;
}
