import { createReducer, on } from '@ngrx/store';
import * as PetActions from './pet.actions';
import { PetDTO } from '../../api';

export interface PetState {
  pets: PetDTO[];
  selectedPet: PetDTO | null;   // add this
  error: any;
  status: 'pending' | 'loading' | 'error' | 'success';
  updateStatus: 'pending' | 'loading' | 'error' | 'success';
  updateError: any;

  favoriteError: any;
}

export const initialState: PetState = {
  pets: [],
  selectedPet: null,
  error: null,
  status: 'pending',
  updateStatus: 'pending',
  updateError: null,

  favoriteError: null,
};

export const petReducer = createReducer(
  initialState,

  // list
  on(PetActions.loadPets, state => ({
    ...state,
    error: null,
    status: 'loading',
  })),
  on(PetActions.loadPetsSuccess, (state, { pets }) => ({
    ...state,
    pets,
    error: null,
    status: 'success',
  })),
  on(PetActions.loadPetsFailure, (state, { error }) => ({
    ...state,
    error,
    status: 'error',
  })),

  // single pet
  on(PetActions.loadPet, state => ({
    ...state,
    selectedPet: null,
    error: null,
    status: 'loading',
  })),
  on(PetActions.loadPetSuccess, (state, { pet }) => ({
    ...state,
    selectedPet: pet,
    error: null,
    status: 'success',
  })),
  on(PetActions.loadPetFailure, (state, { error }) => ({
    ...state,
    selectedPet: null,
    error,
    status: 'error',
  })),

  on(PetActions.searchPets, state => ({
    ...state,
    error: null,
    status: 'loading',
  })),

  on(PetActions.searchPetsSuccess, (state, { pets }) => ({
    ...state,
    pets,
    error: null,
    status: 'success',
  })),

  on(PetActions.searchPetsFailure, (state, { error }) => ({
    ...state,
    error: error,
    status: 'error',
  })),

  on(PetActions.adoptPet, state => ({
  ...state,
  error: null,
  status: 'loading',
  })),
  on(PetActions.adoptPetSuccess, (state, { pet }) => ({
    ...state,
    selectedPet: pet,
    error: null,
    status: 'success',
    // Update the pet in the pets array too
    pets: state.pets.map(p => p.id === pet.id ? pet : p)
  })),

  on(PetActions.adoptPetFailure, (state, { error }) => ({
    ...state,
    error,
    status: 'error',
  })),

  on(PetActions.updatePet,(state) => ({
    ...state,
    updateStatus: 'loading',
  })),

  on(PetActions.updatePetSuccess, (state, {updatedPet}) => ({
    ...state,
    updateStatus: 'success',
    selectedPet: updatedPet.id === state.selectedPet!.id ? updatedPet : state.selectedPet,

    pets: state.pets.map(p => p.id === updatedPet.id ? updatedPet : p),
  })),

  on(PetActions.updatePetFailure, (state,{ updateError }) => ({
    ...state,
    updateStatus: 'error',
    updateError,
  })),

  on(PetActions.resetUpdateStatus, (state) => ({
    ...state,
    updateStatus: 'pending',
  })),

  on(PetActions.resetUpdateError, (state) => ({
    ...state,
    updateError: null,
  })),

  // Favorite Pet reducers ---------------------------------------------------------------
  on(PetActions.addFavoritePet, ( state , { petId }) => ({
    ...state,
    pets: state.pets.map(p => p.id === petId ? { ...p, isUserFavorite: true } : p),
    selectedPet: state.selectedPet && state.selectedPet.id === petId ? { ...state.selectedPet, isUserFavorite: true } : state.selectedPet
  })),

  on(PetActions.addFavoritePetSuccess, state => ({
    ...state,
  })),

  on(PetActions.addFavoritePetFailure, (state,{ error }) => ({
    ...state,
    favoriteError: error,
  })),

  on(PetActions.removeFavoritePet, (state,{ petId }) => ({
    ...state,
    pets: state.pets.map(p => p.id == petId ? { ...p, isUserFavorite: false } : p),
    selectedPet: state.selectedPet && state.selectedPet.id == petId ? { ...state.selectedPet, isUserFavorite: false } : state.selectedPet
  })),

  on(PetActions.removeFavoritePetSuccess, state => ({
    ...state,
  })),

  on(PetActions.removeFavoritePetFailure, (state,{ error }) => ({
    ...state,
    favoriteError: error,
  }))
);

