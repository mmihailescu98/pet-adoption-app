import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {ButtonDirective} from 'primeng/button';
import {FloatLabel} from 'primeng/floatlabel';
import {IconField} from 'primeng/iconfield';
import {InputIcon} from 'primeng/inputicon';
import {InputText} from 'primeng/inputtext';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import {Store} from '@ngrx/store';
import {resetRegister, register} from '../../../store/auth/auth.actions';
import {selectRegistrationError, selectHasRegistered} from '../../../store/auth/auth.selector';
import {Observable, Subscription} from 'rxjs';

@Component({
  selector: 'register-form',
  imports: [
    FloatLabel,
    IconField,
    InputIcon,
    InputText,
    FormsModule,
    ButtonDirective,
    ReactiveFormsModule
  ],
  templateUrl: './register-form.html',
  styleUrl: './register-form.css',
})
export class RegisterForm implements OnInit, OnDestroy {
  @Output() signedUp = new EventEmitter<void>();

  hasRegistered$: Observable<boolean>;
  registrationError$ : Observable<string | null>;

  subscriptionArray : Array<Subscription> = [];

  registrationForm!: FormGroup;

  constructor(private store: Store, private formBuilder: FormBuilder) {
    this.registrationError$ = this.store.select(selectRegistrationError);
    this.hasRegistered$ = this.store.select(selectHasRegistered);
  }

  ngOnInit() {
    this.initRegistrationForm();
    this.initSubscriptions();
  }

  ngOnDestroy(): void {
    this.endSubscriptions();
  }

  initRegistrationForm() {
    this.registrationForm = this.formBuilder.group({
      first_name: ['', [Validators.required]],
      last_name: ['', [Validators.required]],
      username: ['', [Validators.required]],
      email: ['', [Validators.required,Validators.email]],
      password: ['', [Validators.required]],
      password_check: ['', [Validators.required]],
    })
  }

  initSubscriptions() {
    this.subscriptionArray = Array.of
    (
      this.hasRegistered$.subscribe((value) =>
      {
        if (value) {
          alert('Registration successful!');
          this.store.dispatch(resetRegister());

          //in order to change to log-in tab
          this.signedUp.emit();
        }
      }),

      this.registrationError$.subscribe((error) =>
      {
        if (error) {
          alert(`Registration failed: \n${error}`);
        }
      }),
    )
  }

  endSubscriptions(){
    this.subscriptionArray.forEach((sub)=>sub.unsubscribe());
  }

  handleRegistration() {
    this.registrationForm.markAllAsTouched();

    let username: string = this.registrationForm.get('username')?.value;
    let password: string = this.registrationForm.get('password')?.value;
    let password_check: string = this.registrationForm.get('password_check')?.value;

    if(!password || !password_check || !username)
      return;

    if(password !== password_check) {
      alert("Passwords do not match!");
      return;
    }
    console.log(username, password);
    this.store.dispatch(register({ username:username, password:password }));
  }
}
