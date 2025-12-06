import { inject } from '@angular/core';
import {
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpStatusCode
} from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const ErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === HttpStatusCode.Unauthorized) {
        alert('Session expired. Please log in again.');
        router.navigate(['/login']);
      } else if (error.status === HttpStatusCode.Forbidden) {
        alert('You are not allowed to perform this action.');
      } else if (error.error?.message) {
        alert(error.error.message);
      } else {
        alert('Something went wrong. Please try again.');
      }

      return throwError(() => error);
    })
  );
};
