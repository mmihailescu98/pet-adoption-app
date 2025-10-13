import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { PetControllerService } from '../../api/api/petController.service';
import * as PetActions from './pet.actions';
import {mergeMap, map, catchError, switchMap, debounceTime, groupBy} from 'rxjs/operators';
import {of, from, Observable, EMPTY} from 'rxjs';
import { PetDTO } from '../../api/model/petDTO';
import {FavoritePetsControllerService} from '../../api';

@Injectable()
export class PetEffects {
  private actions$ = inject(Actions);
  private petService = inject(PetControllerService);
  private favoritePetService = inject(FavoritePetsControllerService);

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


  private initialFavoritesStateMap = new Map<number, boolean>();

  toggleFavoritePet$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PetActions.addFavoritePet, PetActions.removeFavoritePet),
      groupBy(action => {
        // store the initial (pre-toggle) favorite state if not yet tracked
        if (!this.initialFavoritesStateMap.has(action.petId)) {
          this.initialFavoritesStateMap.set(
            action.petId,
            action.type !== PetActions.addFavoritePet.type
          );
        }
        return action.petId;
      }),
      mergeMap(group$ =>
        group$.pipe(
          debounceTime(2000),
          switchMap(action => {
            console.log("action effect triggered with", this.initialFavoritesStateMap.get(action.petId));

            let request$: Observable<any> = EMPTY;

            if (
              action.type === PetActions.addFavoritePet.type &&
              this.initialFavoritesStateMap.get(action.petId) === false
            ) {
              request$ = this.favoritePetService.addFavoritePet({
                petId: action.petId,
                userId: action.userId,
              });
            } else if (
              action.type === PetActions.removeFavoritePet.type &&
              this.initialFavoritesStateMap.get(action.petId) === true
            ) {
              request$ = this.favoritePetService.removeFavoritePet({
                petId: action.petId,
                userId: action.userId,
              });
            }

            return request$.pipe(
              map(() => {
                this.initialFavoritesStateMap.delete(action.petId); // clean up
                return action.type === PetActions.addFavoritePet.type
                  ? PetActions.addFavoritePetSuccess()
                  : PetActions.removeFavoritePetSuccess();
              }),
              catchError(error => {
                this.initialFavoritesStateMap.delete(action.petId); // clean up even on error
                return of(
                  action.type === PetActions.addFavoritePet.type
                    ? PetActions.addFavoritePetFailure({ error })
                    : PetActions.removeFavoritePetFailure({ error })
                );
              })
            );
          })
        )
      )
    )
  );


}
