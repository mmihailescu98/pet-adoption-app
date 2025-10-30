import {Injectable, inject} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {AdoptionAddRequestDTO, AdoptionControllerService, PetControllerService} from '../../api';
import * as PetActions from './pet.actions';
import {mergeMap, map, catchError, switchMap, debounceTime, groupBy, tap} from 'rxjs/operators';
import {of, from, Observable, EMPTY} from 'rxjs';
import {FavoritePetsControllerService} from '../../api';
import {PetDTO} from '../../api';
import {Router} from '@angular/router';

@Injectable()
export class PetEffects {
  private actions$ = inject(Actions);
  private petService = inject(PetControllerService);
  private favoritePetService = inject(FavoritePetsControllerService);
  private adoptionService = inject(AdoptionControllerService);
  private router = inject(Router);

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

  addPet$ = createEffect(() =>
    this.actions$.pipe(
      ofType(PetActions.addPetForAdoption),
      mergeMap(action =>
        this.adoptionService.createAdoptionListing(action.adoptionRequest).pipe(
          map((adoptionRequest: AdoptionAddRequestDTO) => PetActions.addPetForAdoptionSuccess({adoptionRequest})),
          catchError(error => of(PetActions.addPetForAdoptionFailure({error})))
        )
      )
    )
  );

  addPetForAdoptionSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(PetActions.addPetForAdoptionSuccess),
        tap(() => {
          alert('Pet saved successfully!');
          setTimeout(() => {
            this.router.navigate(['/pet-list']);
          }, 0);
        })
      ),
    {dispatch: false}
  );

  addPetForAdoptionFailure$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(PetActions.addPetForAdoptionFailure),
        tap(({error}) => {
          alert('Failed to save pet. Please try again.');

          console.error('Add pet failed:', error);
        })
      ),
    {dispatch: false}
  );
}
