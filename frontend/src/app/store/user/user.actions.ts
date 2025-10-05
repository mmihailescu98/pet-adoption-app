import {createAction, props} from '@ngrx/store';
import {PetDTO, UserDTO} from '../../api';


export const loadUser = createAction('[User] Load User', props<{ userID: number }>());
export const loadUserSuccess = createAction('[User] Load User Success', props<{ user: UserDTO }>());
export const loadUserFailure = createAction('[User] Load User Failure', props<{ error: any }>());

export const updateUser = createAction('[User] Update User', props<{ user: UserDTO }>());
export const updateUserSuccess = createAction('[User] Update User Success', props<{ user: UserDTO }>());
export const updateUserFailure = createAction('[User] Update User Failure', props<{ error: any }>());

export const loadAdoptedPetsByUser = createAction('[User] Load Adopted Pets By User', props<{ userID: number }>());
export const loadAdoptedPetsByUserSuccess = createAction('[User] Load Adopted Pets By User Success', props<{ adoptedPets: PetDTO[] }>());
export const loadAdoptedPetsByUserFailure = createAction('[User] Load Adopted Pets By User Failure', props<{ error: any }>());
