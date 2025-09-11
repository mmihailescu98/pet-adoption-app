import { createAction, props } from '@ngrx/store';
import { PetDTO } from '../api/model/petDTO';

export const loadPets = createAction('[Pet List] Load Pets');
export const loadPetsSuccess = createAction('[Pet List] Load Pets Success', props<{ pets: PetDTO[] }>());
export const loadPetsFailure = createAction('[Pet List] Load Pets Failure', props<{ error: any }>());
