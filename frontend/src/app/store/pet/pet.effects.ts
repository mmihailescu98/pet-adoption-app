import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { AdoptionControllerService, PetControllerService} from '../../api';
import * as PetActions from './pet.actions';
import { mergeMap, map, catchError, switchMap } from 'rxjs/operators';
import { of, from, Observable } from 'rxjs';
import { PetDTO } from '../../api/model/petDTO';

@Injectable()
export class PetEffects {
  private actions$ = inject(Actions);
  private petService = inject(PetControllerService);
  private adoptionService = inject(AdoptionControllerService);

  private blobToPets(blob: Blob) {
    return from(blob.text()).pipe(
      map(text => JSON.parse(text) as PetDTO[])
    );
  }

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

  searchPets$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PetActions.searchPets),
      mergeMap(action =>
        (this.petService.getPets(action.species, action.breed) as unknown as Observable<Blob>).pipe(
          switchMap(blob => this.blobToPets(blob)),
          map(pets => PetActions.searchPetsSuccess({ pets })),
          catchError(error => of(PetActions.searchPetsFailure({ error })))
        )
      )
    )
  );



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

  adoptPet$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PetActions.adoptPet),
      mergeMap(({ id }) =>
        this.petService.adoptPet(id).pipe(
          map((pet: any) => PetActions.adoptPetSuccess({ pet })),
          catchError(error => of(PetActions.adoptPetFailure({ error })))
        )
      )
    )
  );

  updatePet$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PetActions.updatePet),
      mergeMap(({ updatedPet }) =>
        this.petService.updatePet(updatedPet.id!,updatedPet).pipe(
          map((pet : PetDTO) => PetActions.updatePetSuccess({ updatedPet: pet })),
          catchError(error => of(PetActions.updatePetFailure({updateError: error})))
        )
      )
    )
  )

}
