import type { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (
    !['/login', '/register', '/refresh-token'].some((url) =>
      req.url?.includes(url)
    )
  ) {
    const accessToken = localStorage.getItem('accessToken') ?? '';
    req = req.clone({
      setHeaders: {
        Authorization: accessToken ? `Bearer ${accessToken}` : '',
      },
    });
  }

  req = req.clone({
    withCredentials: true,
  });
  return next(req);
};
