import {ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {providePrimeNG} from 'primeng/config';
import LaraLightBlue from '@primeuix/themes/aura';
import {petReducer} from './store/pet-store/pet.reducer';
import {PetEffects} from './store/pet-store/pet.effects';
import {provideStore} from '@ngrx/store';
import {provideEffects} from '@ngrx/effects';
import {provideHttpClient} from '@angular/common/http';

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

    provideStore({ pet: petReducer }),
    provideEffects([PetEffects]),

    provideHttpClient()
  ]
};
