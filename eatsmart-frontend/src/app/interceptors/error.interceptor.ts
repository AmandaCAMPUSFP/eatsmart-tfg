import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor() { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'Error desconocido';

        if (error.error instanceof ErrorEvent) {
          // Error de cliente
          errorMessage = error.error.message;
        } else {
          // Error de servidor
          errorMessage = error.error?.message || `Error ${error.status}: ${error.statusText}`;
        }

        console.error('HTTP Error:', errorMessage);
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}
