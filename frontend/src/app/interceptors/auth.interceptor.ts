import type { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authToken = localStorage.getItem('authToken') ?? '';
  const authReq = req.clone({
    setHeaders: {
      Authorization: authToken ? `Token ${authToken}` : '',
    },
  });
  return next(authReq);
};
