import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';

const excludedUrls = [
//  '/api/v1/report',
//  '/api/v1/news'
];

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const token = tokenService.getToken();

  // const isExcluded = excludedUrls.some(url => req.url.includes(url));
  // if (isExcluded) {
  //   return next(req);
  // }

  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  return next(req);
};