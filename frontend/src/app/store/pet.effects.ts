import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AdoptionControllerService, PetControllerService} from '../api';
import * as PetActions from './pet.actions';
import { mergeMap, map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable()
export class PetEffects {
  private actions$ = inject(Actions);
  private petService = inject(PetControllerService);
  private adoptionService = inject(AdoptionControllerService);

  loadPets$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PetActions.loadPets),
      mergeMap(() =>
        {

          return this.adoptionService.getAdoptions().pipe(
            map(adoptions => {
              const adoptablePets = adoptions.map(adoption => adoption.pet!);
              return PetActions.loadPetsSuccess({ pets: adoptablePets });
            }),
            catchError(error => of(PetActions.loadPetsFailure({ error })))
          );
        }
      )
  ));

  loadPet$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PetActions.loadPet),
      mergeMap((value) =>
        { return this.petService.getPetById(value.id).pipe(
            map(pet => PetActions.loadPetSuccess({ pet })),
            catchError(error => of(PetActions.loadPetFailure({ error })))
          )
        }
    )
  ));

}
