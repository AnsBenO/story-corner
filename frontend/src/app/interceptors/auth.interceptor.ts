import type { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const accessToken = localStorage.getItem('accessToken') ?? '';
  const authReq = req.clone({
    setHeaders: {
      Authorization: accessToken ? `Bearer ${accessToken}` : '',
    },
  });
  return next(authReq);
};
