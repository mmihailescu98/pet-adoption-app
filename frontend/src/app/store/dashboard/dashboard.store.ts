import { signalStore, withState, withMethods, patchState } from '@ngrx/signals';
import {PercentageStatsDTO} from '../../api'
import {AdoptionStatsDTO} from '../../api'
import { inject } from '@angular/core';
import { DashboardControllerService } from '../../api';
import {lastValueFrom} from 'rxjs';

export interface DashboardState {
  mostAdoptedSpecies: AdoptionStatsDTO[];
  mostAdoptedBreeds: AdoptionStatsDTO[];
  speciesPercentages: PercentageStatsDTO[];
  breedsPercentages: PercentageStatsDTO[];
  totalPets: number;
  totalAdoptedPets: number;
  adoptionRate: number;
  mostPopularPetLocation: string;
  error: any;
  status: 'pending' | 'loading' | 'error' | 'success';
}

const initialState: DashboardState = {
  mostAdoptedSpecies: [],
  mostAdoptedBreeds: [],
  speciesPercentages: [],
  breedsPercentages: [],
  totalPets: -1,
  totalAdoptedPets: -1,
  adoptionRate: -1,
  mostPopularPetLocation: "No location",
  error: null,
  status: 'pending'
};

export const DashboardStore = signalStore(
    withState(initialState),

    withMethods((store, dashboardService = inject(DashboardControllerService)) => ({

        async loadMostAdoptedSpecies() {
          patchState(store, { status: 'loading', error: null });
          try {
            const result = await lastValueFrom(dashboardService.getMostAdoptedSpecies());
            patchState(store, { mostAdoptedSpecies: result, status: 'success' });
          } catch (err: any) {
            patchState(store, { error: err, status: 'error' });
          }
        },

        async loadMostAdoptedBreeds() {
          patchState(store, { status: 'loading', error: null });
          try {
            const result = await lastValueFrom(dashboardService.getMostAdoptedBreeds());
            patchState(store, { mostAdoptedBreeds: result, status: 'success' });
          } catch (err: any) {
            patchState(store, { error: err, status: 'error' });
          }
        },

        async loadSpeciesPercentages() {
          patchState(store, { status: 'loading', error: null });
          try {
            const result = await lastValueFrom(dashboardService.getSpeciesPercentages());
            patchState(store, { speciesPercentages: result, status: 'success' });
          } catch (err: any) {
            patchState(store, { error: err, status: 'error' });
          }
        },

        async loadBreedsPercentages() {
          patchState(store, { status: 'loading', error: null });
          try {
            const result = await lastValueFrom(dashboardService.getBreedPercentages());
            patchState(store, { breedsPercentages: result, status: 'success' });
          } catch (err: any) {
            patchState(store, { error: err, status: 'error' });
          }
        },

      // totalPets: number;
      async loadTotalPets() {
        patchState(store, { status: 'loading', error: null });
        try {
          const result = await lastValueFrom(dashboardService.getTotalPets());
          patchState(store, { totalPets: result, status: 'success' });
        } catch (err: any) {
          patchState(store, { error: err, status: 'error' });
        }
      },
      // totalAdoptedPets: number;
      async loadTotalAdoptedPets() {
        patchState(store, { status: 'loading', error: null });
        try {
          const result = await lastValueFrom(dashboardService.getTotalAdoptedPets());
          patchState(store, { totalAdoptedPets: result, status: 'success' });
        } catch (err: any) {
          patchState(store, { error: err, status: 'error' });
        }
      },
      // adoptionRate: number;
      async loadAdoptionRate() {
        patchState(store, { status: 'loading', error: null });
        try {
          const result = await lastValueFrom(dashboardService.getAdoptionRate());
          patchState(store, { adoptionRate: result, status: 'success' });
        } catch (err: any) {
          patchState(store, { error: err, status: 'error' });
        }
      },
      // mostPopularPetLocation: string;
      async loadMostPopularLocation() {
        patchState(store, { status: 'loading', error: null });
        try {
          const result = await lastValueFrom(dashboardService.mostPopularPetLocation());
          patchState(store, { mostPopularPetLocation: result, status: 'success' });
        } catch (err: any) {
          patchState(store, { error: err, status: 'error' });
        }
      },

        async loadAll() {
          await Promise.all([
            this.loadMostAdoptedSpecies(),
            this.loadMostAdoptedBreeds(),
            this.loadSpeciesPercentages(),
            this.loadBreedsPercentages(),
            this.loadTotalPets(),
            this.loadTotalAdoptedPets(),
            this.loadAdoptionRate(),
            this.loadMostPopularLocation(),
          ]);
        }
      })
    )
  );
