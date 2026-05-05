import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import {
  provideHttpClient,
  withInterceptorsFromDi,
  withFetch,
  HTTP_INTERCEPTORS
} from '@angular/common/http';

import { routes } from './app.routes';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { ErrorInterceptor } from './interceptors/error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimations(),

    // HttpClient con:
    //  - withFetch(): usa la API fetch nativa (recomendado para SSR)
    //  - withInterceptorsFromDi(): registra los interceptores definidos abajo con HTTP_INTERCEPTORS
    provideHttpClient(
      withFetch(),
      withInterceptorsFromDi()
    ),

    // Interceptores de clase (orden importante: primero JWT, después manejo de errores)
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ]
};
