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
          map(response => {
            console.log('Full response object:', JSON.stringify(response));
            console.log('Response type:', typeof response);
            console.log('loggedUser property:', response.loggedUser);
            console.log('token property:', response.token);
            
            if (!response.loggedUser) {
              console.error('loggedUser property is missing or undefined');
              return AuthActions.loginFailure({ error: 'No user data received' });
            }
            return AuthActions.loginSuccess({
              token: response.token || '',
              userModel: {
                id: response.loggedUser.id ?? 0,
                username: response.loggedUser.username ?? ''
              }
            });
          }),
          catchError(error => {
            console.error('Login error:', error);
            return of(AuthActions.loginFailure({ 
              error: `${error.status}: ${error.statusText || 'Unknown'} - ${error.message || 'No message'}`
            }));
          })
        );
      })
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
