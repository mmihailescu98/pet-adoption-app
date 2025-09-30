import { createSelector, createFeatureSelector } from '@ngrx/store';
import { AuthState } from './auth.state';

export const selectAuthState = createFeatureSelector<AuthState>('auth');

export const selectLoading = createSelector(
  selectAuthState,
  (state) => state.loading
);

export const selectLoginError = createSelector(
  selectAuthState,
  (state) => state.loginError
);

export const selectIsLoggedIn = createSelector(
  selectAuthState,
  (state) => state.token != null
);

export const selectToken = createSelector(
  selectAuthState,
  (state) => state.token
)

export const selectHasRegistered = createSelector(
  selectAuthState,
  (state) => state.hasRegistered
)

export const selectRegistrationError = createSelector(
  selectAuthState,
  (state) => state.registrationError
)
