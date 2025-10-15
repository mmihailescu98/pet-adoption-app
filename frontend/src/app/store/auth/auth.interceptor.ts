import { HttpRequest, HttpEvent } from '@angular/common/http';
import { HttpInterceptorFn, HttpHandlerFn } from '@angular/common/http';
import { Observable } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
  const authState = localStorage.getItem('auth');
  let token: string | null = null;

  if (authState) {
    try {
      token = JSON.parse(authState).token;
    } catch {
      token = null;
    }
  }

  const clonedReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(clonedReq);
};
