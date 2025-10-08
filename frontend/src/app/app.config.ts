import {
  ApplicationConfig, inject, provideAppInitializer,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {providePrimeNG} from 'primeng/config';
import LaraLightBlue from '@primeuix/themes/aura';
import {authReducer} from './store/auth/auth.reducer';
import {AuthEffects} from './store/auth/auth.effects';
import {petReducer} from './store/pet.reducer';
import {PetEffects} from './store/pet.effects';
import {provideStore} from '@ngrx/store';
import {provideEffects} from '@ngrx/effects';
import {provideHttpClient} from '@angular/common/http';
import {MapService} from './component/map-search/map.service';

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
      pet: petReducer
    }),
    provideEffects([
      AuthEffects,
      PetEffects
    ]),

    provideHttpClient(),

    provideAppInitializer(() => {
      const mapsLoader = inject(MapService);
      return mapsLoader.load();
    })
  ]
};
