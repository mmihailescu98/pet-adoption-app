import { createAction, props } from '@ngrx/store';
import { PetDTO } from '../api';

export const loadPets = createAction('[Pet List] Load Pets');
export const loadPetsSuccess = createAction('[Pet List] Load Pets Success', props<{ pets: PetDTO[] }>());
export const loadPetsFailure = createAction('[Pet List] Load Pets Failure', props<{ error: any }>());

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
