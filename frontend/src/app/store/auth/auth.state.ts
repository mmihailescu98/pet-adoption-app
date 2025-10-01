export interface AuthState {
  token: string | null;
  loading: boolean;
  hasRegistered: boolean;
  loginError: string | null ;
  registrationError: string | null ;
  // add user model
  userModel: UserLoginModel
}
