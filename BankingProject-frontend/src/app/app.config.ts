import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { JwtInterceptor } from './services/jwt.interceptor';
import { ErrorInterceptor } from './services/error.interceptor';

export const APP_CONFIG = {
  apiBase: 'http://localhost:8080',
  appName: 'Banking Simulation',
  version: '1.0.0',
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([
        JwtInterceptor,   // adds withCredentials: true
        ErrorInterceptor  // global error handling
      ])
    )
  ]
};
