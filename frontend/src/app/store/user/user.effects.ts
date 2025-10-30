import {Injectable, inject} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import * as UserActions from './user.actions';
import {UserControllerService} from '../../api';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {of, switchMap} from 'rxjs';


@Injectable()
export class UserEffects {
  private actions$ = inject(Actions);
  private userService = inject(UserControllerService);

  loadUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.loadUser),
      mergeMap(action =>
        this.userService.getUser(action.userID).pipe(
          map(user => UserActions.loadUserSuccess({user})),
          catchError(error => of(UserActions.loadUserFailure({error})))
        )
      )
    )
  );

  updateUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.updateUser),
      mergeMap(action => {
        const userID = action.user.id;
        if (!userID) {
          return of(UserActions.updateUserFailure({error: 'ID is required for updating user'}));
        }
        return this.userService.updateUserProfile(userID, action.user).pipe(
          map(user => UserActions.updateUserSuccess({user})),
          catchError(error => of(UserActions.updateUserFailure({error})))
        );
      })
    )
  );

  loadAdoptedPetsByUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.loadAdoptedPetsByUser),
      mergeMap(action =>
        this.userService.getAdoptedPetsByUser(action.userID).pipe(
          map(adoptedPets => UserActions.loadAdoptedPetsByUserSuccess({adoptedPets})),
          catchError(error => of(UserActions.loadAdoptedPetsByUserFailure({error})))
        )
      )
    )
  );

}
