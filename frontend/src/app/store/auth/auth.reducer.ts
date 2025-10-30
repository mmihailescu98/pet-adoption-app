import { createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';
import { AuthState } from './auth.state';

export const initialState: AuthState = {
  token: null,
  loading: false,
  hasRegistered: false,
  loginError: null,
  registrationError: null,
  userModel: null,
};

export const authReducer = createReducer(
  initialState,

  on(AuthActions.login, (state) => ({
    ...state,
    loading: true,
    loginError: null
  })),

  on(AuthActions.loginSuccess, (state, { token, userModel }) => {
    return {
      ...state,
      loading: false,
      token,
      userModel,
    };
  }),

  on(AuthActions.loginFailure, (state, { error }) => ({
    ...state,
    loading: false,
    loginError: error,
  })),

  on(AuthActions.register, (state) => ({
    ...state,
    loading: true,
    registrationError: null,
  })),

  //on register token is not given
  on(AuthActions.registerSuccess, (state) => ({
    ...state,
    loading: false,
    hasRegistered: true,
  })),

  on(AuthActions.registerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    registrationError: error,
  })),

  on(AuthActions.resetHasRegisteredState, (state) => ({
    ...state,
    hasRegistered: false,
  })),

  on(AuthActions.clearRegisterError, (state) => ({
    ...state,
    registrationError: null,
  })),

  on(AuthActions.clearLoginError, (state) => ({
    ...state,
    loginError: null,
  })),

  on(AuthActions.logout, () => ({
    ...initialState
  }))
);
