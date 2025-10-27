import { createReducer, on } from '@ngrx/store';
import * as PetActions from './pet.actions';
import { PetDTO } from '../../api';

export interface PetState {
  pets: PetDTO[];
  selectedPet: PetDTO | null;   // add this
  error: any;
  status: 'pending' | 'loading' | 'error' | 'success';
}

const initialState: PetState = {
  pets: [],
  selectedPet: null,
  error: null,
  status: 'pending',
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
  // addPet
  on(PetActions.addPet, state => ({
    ...state,
    error: null,
    status: 'loading',
  })),
  on(PetActions.addPetSuccess, (state, { pet }) => ({
    ...state,
    pets: [...state.pets, pet],
    error: null,
    status: 'success',
  })),
  on(PetActions.addPetFailure, (state, { error }) => ({
    ...state,
    error,
    status: 'error',
  })),

);

