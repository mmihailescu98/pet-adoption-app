import {Component, EventEmitter, OnInit, Output} from '@angular/core';
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
import {resetHasRegisteredState, register, clearRegisterError} from '../../../store/auth/auth.actions';
import {selectRegistrationError, selectHasRegistered} from '../../../store/auth/auth.selector';
import {Observable} from 'rxjs';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy'


@UntilDestroy()
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
export class RegisterForm implements OnInit {
  @Output() onRegister = new EventEmitter<void>();

  hasRegistered$: Observable<boolean>;
  registrationError$ : Observable<string | null>;

  registrationForm!: FormGroup;

  constructor(private store: Store, private formBuilder: FormBuilder) {
    this.registrationError$ = this.store.select(selectRegistrationError);
    this.hasRegistered$ = this.store.select(selectHasRegistered);
  }

  ngOnInit() {
    this.initRegistrationForm();
    this.initSubscriptions();
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
    this.hasRegistered$
      .pipe(untilDestroyed(this))
      .subscribe((value) =>
    {
      if (value) {
        alert('Registration successful!');
        this.store.dispatch(resetHasRegisteredState());

        //in order to change to log-in tab
        this.onRegister.emit();
      }
    })

    this.registrationError$
      .pipe(untilDestroyed(this))
      .subscribe((error) =>
    {
      if (error) {
        alert(`Registration failed: \n${error}`);

        this.store.dispatch(clearRegisterError());
      }
    })
  }


  handleRegistration() {
    let username: string = this.registrationForm.get('username')?.value;
    let password: string = this.registrationForm.get('password')?.value;
    let password_check: string = this.registrationForm.get('password_check')?.value;

    if(!password || !password_check || !username)
      return;

    if(password !== password_check) {
      alert("Passwords do not match!");
      return;
    }
    this.store.dispatch(register({ username:username, password:password }));
  }
}
