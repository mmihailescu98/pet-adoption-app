import { createAction, props } from '@ngrx/store';
import {AdoptionListItemDTO, AdoptionAddRequestDTO, PetDTO} from '../../api';

export const loadPets = createAction('[Pet List] Load Pets');
export const loadPetsSuccess = createAction('[Pet List] Load Pets Success', props<{ pets: PetDTO[] }>());
export const loadPetsFailure = createAction('[Pet List] Load Pets Failure', props<{ error: any }>());
export const addPet = createAction(
  '[Pet] Add Pet',
  props<{ pet: PetDTO }>()
);
export const addPetForAdoption = createAction(
  '[Adoption] Add Pet',
  props<{ adoptionRequest: AdoptionAddRequestDTO }>()
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

// Favorite pets actions -----------------------------------------------------------------------

export const addFavoritePet = createAction(
  '[Pet Favorite] Add Favorite Pet',
  props<{ petId: number,userId: number }>()
);

export const addFavoritePetSuccess = createAction(
  '[Pet Favorite] Add Favorite Pet Success',
);

export const addFavoritePetFailure = createAction(
  '[Pet Favorite] Add Favorite Pet Failure',
  props<{ error: any }>()
);

export const removeFavoritePet = createAction(
  '[Pet Favorite] Remove Favorite Pet',
  props<{ petId: number,userId: number }>()
);

export const removeFavoritePetSuccess = createAction(
  '[Pet Favorite] Remove Favorite Pet Success',
);

export const removeFavoritePetFailure = createAction(
  '[Pet Favorite] Remove Favorite Pet Failure',
  props<{ error: any }>()
);

//----------------------------------------------------------------------------------------------------



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

export const updatePet = createAction(
  '[Pet Profile] Update Pet',
  props<{ updatedPet: PetDTO }>()
)

export const updatePetSuccess = createAction(
  '[Pet Profile] Update Pet Success',
  props<{ updatedPet: PetDTO }>()
)

export const updatePetFailure = createAction(
  '[Pet Profile] Update Pet Failure',
  props<{updateError: string}>()
)

export const resetUpdateStatus = createAction(
  '[Pet Profile] Reset Update Status',
)

export const resetUpdateError = createAction(
  '[Pet Profile] Reset Update Error'
)

export const addPetForAdoptionSuccess = createAction(
  '[Adoption] Added pet for adoption Success',
  props<{ adoptionRequest: AdoptionAddRequestDTO }>()
);

export const addPetForAdoptionFailure = createAction(
  '[Adoption] Added pet for adoption Failure',
  props<{ error: any }>()
);

export const requestAdoptionForPet = createAction(
  '[Pet Profile] Request Adoption For Pet',
  props<{ petId: number, userId: number }>()
);

export const requestAdoptionForPetSuccess = createAction(
  '[Pet Profile] Request Adoption For Pet Success',
  props<{ adoption: AdoptionListItemDTO }>()
);

export const requestAdoptionForPetFailure = createAction(
  '[Pet Profile] Request Adoption For Pet Failure',
  props<{ error: any }>()
);
