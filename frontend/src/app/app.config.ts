import {ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {providePrimeNG} from 'primeng/config';
import LaraLightBlue from '@primeuix/themes/aura';
import {petReducer} from './store/pet/pet.reducer';
import {PetEffects} from './store/pet/pet.effects';
import {authReducer} from './store/auth/auth.reducer';
import {AuthEffects} from './store/auth/auth.effects';
import {provideStore} from '@ngrx/store';
import {provideEffects} from '@ngrx/effects';
import {provideHttpClient} from '@angular/common/http';
import {provideAnimations} from '@angular/platform-browser/animations';
import {UserEffects} from './store/user/user.effects';
import {userReducer} from './store/user/user.reducer';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    providePrimeNG({
      theme: {
        preset: LaraLightBlue,
        options: {
          darkModeSelector: null,
        }
      }
    }),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),

    provideStore({
      auth: authReducer,
      pet: petReducer,
      user: userReducer
    }),
    provideEffects([
      AuthEffects,
      PetEffects,
      UserEffects
    ]),

    provideHttpClient(),

    provideAnimations()
  ]
};
