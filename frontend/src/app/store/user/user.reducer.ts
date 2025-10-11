import {createReducer, on} from '@ngrx/store';
import * as UserActions from './user.actions';
import {AdoptedPetDTO, UserDTO} from '../../api';

export interface UserState {
  user: UserDTO | null;
  adoptedPets: AdoptedPetDTO[] | [];
  loading: boolean;
  error: any;
}

export const initialState: UserState = {
  user: null,
  adoptedPets: [],
  loading: false,
  error: null,
}

export const userReducer = createReducer(
  initialState,

  on(UserActions.loadUser, (state) => ({...state, loading: true, error: null})),
  on(UserActions.loadUserSuccess, (state, {user}) => ({...state, user, loading: false, error: null})),
  on(UserActions.loadUserFailure, (state, {error}) => ({...state, loading: false, error})),

  on(UserActions.updateUser, (state) => ({...state, loading: true, error: null})),
  on(UserActions.updateUserSuccess, (state, {user}) => ({...state, user, loading: false, error: null})),
  on(UserActions.updateUserFailure, (state, {error}) => ({...state, loading: false, error})),

  on(UserActions.loadAdoptedPetsByUser, (state) => ({...state, loading: true, error: null})),
  on(UserActions.loadAdoptedPetsByUserSuccess, (state, {adoptedPets}) => ({...state, adoptedPets: adoptedPets, loading: false, error: null})),
  on(UserActions.loadAdoptedPetsByUserFailure, (state, {error}) => ({...state, loading: false, error}))
);
