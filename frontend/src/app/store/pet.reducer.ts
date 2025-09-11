import { createReducer, on } from '@ngrx/store';
import * as PetActions from './pet.actions';
import { PetDTO } from '../api/model/petDTO';

export interface PetState {
  pets: PetDTO[];
  error: any;
  status: 'pending' | 'loading' | 'error' | 'success';
}

const initialState: PetState = {
  pets: [],
  error: null,
  status: 'pending',
};

export const petReducer = createReducer(
  initialState,

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
    error: error,
    status: 'error',
  }))
);
