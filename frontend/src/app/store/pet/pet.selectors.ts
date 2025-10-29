import { createFeatureSelector, createSelector } from '@ngrx/store';
import { PetState } from './pet.reducer';

export const selectPetState = createFeatureSelector<PetState>('pet');

export const selectAllPets = createSelector(
  selectPetState,
  (state: PetState) => state.pets
);

export const selectPetStatus = createSelector(
  selectPetState,
  (state: PetState) => state.status
);

export const selectPetError = createSelector(
  selectPetState,
  (state: PetState) => state.error
);

export const selectSelectedPet = createSelector(
  selectPetState,
  (state: PetState) => state.selectedPet
);

export const selectUpdateStatus = createSelector(
  selectPetState,
  (state: PetState) => state.updateStatus
)

export const selectUpdateError = createSelector(
  selectPetState,
  (state: PetState) => state.updateError
)

export const selectFavoriteError = createSelector(
  selectPetState,
  (state: PetState) => state.favoriteError
)


