import {Component, OnDestroy, OnInit} from '@angular/core';
import {FloatLabel} from 'primeng/floatlabel';
import {IconField} from 'primeng/iconfield';
import {InputIcon} from 'primeng/inputicon';
import {InputText} from 'primeng/inputtext';
import { ButtonDirective } from 'primeng/button';
import {Observable, Subscription} from 'rxjs';
import {Store} from '@ngrx/store';
import {selectLoginError, selectIsLoggedIn, selectLoading} from '../../../store/auth/auth.selector';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {clearLoginError, login} from '../../../store/auth/auth.actions';


@Component({
  selector: 'login-form',
  imports: [
    FloatLabel,
    IconField,
    InputIcon,
    InputText,
    FormsModule,
    ButtonDirective,
    ReactiveFormsModule,
  ],
  templateUrl: './login-form.html',
  styleUrl: './login-form.css',
})
export class LoginForm implements OnInit, OnDestroy {
  isLoading$: Observable<boolean>;
  hasLoggedIn$: Observable<boolean>;
  loginError$: Observable<string | null>;

  subscriptionArray: Array<Subscription> = [];

  loginForm!: FormGroup;

  constructor(private store: Store,private formBuilder: FormBuilder) {
    this.isLoading$ = this.store.select(selectLoading);
    this.loginError$ = this.store.select(selectLoginError);
    this.hasLoggedIn$ = this.store.select(selectIsLoggedIn);
  }

  ngOnInit() {

    this.initSubscriptions();
    this.initLoginForm();
  }

  ngOnDestroy() {
    this.endSubscriptions();
  }

  initSubscriptions(){
    this.subscriptionArray = Array.of(
      this.hasLoggedIn$.subscribe((value) => {
        if (value) {
          alert('Login successful!');
          //should also route to another page, until then after a login is succesful the alert will be triggered each time we come to the login form
        }
      }),

      this.loginError$.subscribe((error) => {
        if (error) {
          alert(`Login failed: \n${error}`);
          this.clearFields();

          this.store.dispatch(clearLoginError());
        }
      }),
    );
  }

  endSubscriptions(){
    this.subscriptionArray.forEach((sub) => sub.unsubscribe());
  }

  initLoginForm() {
    this.loginForm = this.formBuilder.group({
      username : ['', [Validators.required]],
      password: ['', [Validators.required]],
    })
  }

  handleLogin() {
    let username : string = this.loginForm.get('username')?.value;
    let password : string = this.loginForm.get('password')?.value;

    if (!username || !password ) {
      return
    }

    this.store.dispatch(login({username:username, password:password}));
  }

  clearFields() {
    this.loginForm.get('username')?.setValue('');
    this.loginForm.get('password')?.setValue('');
  }
}
