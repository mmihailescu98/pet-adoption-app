import { createAction, props } from '@ngrx/store';
import {UserLoginModel} from '../../api';

export const login = createAction(
  '[Auth] Login',
  props<{ username: string; password: string }>()
);

export const loginSuccess = createAction(
  '[Auth] Login Success',
  props<{ token: string;
      userModel: UserLoginModel;
   }>()
);

export const loginFailure = createAction(
  '[Auth] Login Failure',
  props<{ error: string }>()
);

export const register = createAction(
  '[Auth] Register',
  props<{ username: string, password: string }>(),
);

export const registerSuccess = createAction(
  '[Auth] Register Success'
);

export const registerFailure = createAction(
  '[Auth] Register Failure',
  props<{ error: string }>()
)


export const resetHasRegisteredState = createAction(
  '[Auth] Register Reset',
)

export const clearLoginError = createAction(
  '[Auth] Clear Login Error',
)

export const clearRegisterError = createAction(
  '[Auth] Clear Register Error',
)
