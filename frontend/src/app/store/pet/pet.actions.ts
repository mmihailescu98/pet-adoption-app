import { createAction, props } from '@ngrx/store';
import { PetDTO } from '../../api';

export const loadPets = createAction('[Pet List] Load Pets');
export const loadPetsSuccess = createAction('[Pet List] Load Pets Success', props<{ pets: PetDTO[] }>());
export const loadPetsFailure = createAction('[Pet List] Load Pets Failure', props<{ error: any }>());
export const addPet = createAction(
  '[Pet] Add Pet',
  props<{ pet: PetDTO }>()
);

export const searchPets = createAction('[Pet List] Search Pets', props<{ breed: string; species: string }>());
export const searchPetsSuccess = createAction('[Pet List] Search Pets Success', props<{ pets: PetDTO[] }>());
export const searchPetsFailure = createAction('[Pet List] Search Pets Failure', props<{ error: any }>());

export const loadPet = createAction(
  '[Pet Profile] Load Pet',
  props<{ id: number }>()
);

export const loadPetSuccess = createAction(
  '[Pet Profile] Load Pet Success',
  props<{ pet: PetDTO }>()
);

export const loadPetFailure = createAction(
  '[Pet Profile] Load Pet Failure',
  props<{ error: any }>()
);

export const adoptPet = createAction(
  '[Pet Profile] Adopt Pet',
  props<{ id: number }>()
);

export const adoptPetSuccess = createAction(
  '[Pet Profile] Adopt Pet Success',
  props<{ pet: PetDTO }>()
);

export const adoptPetFailure = createAction(
  '[Pet Profile] Adopt Pet Failure',
  props<{ error: any }>()
);
