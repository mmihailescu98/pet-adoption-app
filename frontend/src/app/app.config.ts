import {ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {providePrimeNG} from 'primeng/config';

import LaraLightBlue from '@primeuix/themes/aura';
import {provideStore} from '@ngrx/store';
import {authReducer} from './store/auth/auth.reducer';
import {provideEffects} from '@ngrx/effects';
import {AuthEffects} from './store/auth/auth.effects';
import {provideHttpClient} from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    providePrimeNG({
      theme: {
        preset: LaraLightBlue, // theme can be changed
        options: {
          darkModeSelector: null,
        }
      }
    }),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),

    provideStore({ auth:authReducer }),
    provideEffects([AuthEffects]),

    provideHttpClient(),
  ]
};
