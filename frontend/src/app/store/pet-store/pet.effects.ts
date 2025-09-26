import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { PetControllerService } from '../api/api/petController.service';
import * as PetActions from './pet.actions';
import { mergeMap, map, catchError, switchMap } from 'rxjs/operators';
import { of, from, Observable } from 'rxjs';
import { PetDTO } from '../api/model/petDTO';

@Injectable()
export class PetEffects {
  private actions$ = inject(Actions);
  private petService = inject(PetControllerService);

  private blobToPets(blob: Blob) {
    return from(blob.text()).pipe(
      map(text => JSON.parse(text) as PetDTO[])
    );
  }

  loadPets$ = createEffect(() =>
    this.actions$.pipe(ofType(PetActions.loadPets),
    mergeMap(() =>
      (this.petService.getPets() as unknown as Observable<Blob>).pipe(
        switchMap(blob => this.blobToPets(blob)),
        map(pets => PetActions.loadPetsSuccess({ pets })),
        catchError(error => of(PetActions.loadPetsFailure({ error })))
      )
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
      mergeMap(({ id }) =>
        (this.petService.getPetById(id) as unknown as Observable<Blob>).pipe(
          switchMap(blob =>
            from(blob.text()).pipe(map(text => JSON.parse(text) as PetDTO))
          ),
          map(pet => PetActions.loadPetSuccess({ pet })),
          catchError(error => of(PetActions.loadPetFailure({ error })))
        )
      )
    )
  );

}
