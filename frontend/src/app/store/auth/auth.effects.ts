import {inject, Injectable} from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {AuthControllerService, LoginRequest, RegisterRequest} from '../../api';
import * as AuthActions from './auth.actions';
import { catchError, map, mergeMap } from 'rxjs';
import { of } from 'rxjs';

@Injectable()
export class AuthEffects {
  private actions$ = inject(Actions);
  private authService = inject(AuthControllerService)

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.login),
      mergeMap((action) => {
          const request: LoginRequest = {
            username: action.username,
            password: action.password
          };
          return this.authService.login(request).pipe(
            map(response => AuthActions.loginSuccess({ userModel: response.userModel ? response.userModel : 'success, but no token provided' })), // map here the user details
            catchError(error => of(AuthActions.loginFailure({ error: error.message })))
          );
        }
      )
    )
  );

  register$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.register),
      mergeMap((action) => {
          const request : RegisterRequest = {
            username: action.username,
            password: action.password,
            // roles are ignored in the backend for now
            //roles:
          }

          return this.authService.register(request).pipe(
            map(() => AuthActions.registerSuccess()),
            catchError((error) => of(AuthActions.registerFailure({error: error.message})))
          );
        }
      )
    )
  );
}
