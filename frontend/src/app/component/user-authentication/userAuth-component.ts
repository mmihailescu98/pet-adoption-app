import {Component} from '@angular/core';
import {DataViewModule} from 'primeng/dataview';
import {RegisterForm} from './register-form/register-form';
import {LoginForm} from './login-form/login-form';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'userReg-component',
  imports: [
    DataViewModule,
    RegisterForm,
    LoginForm,
    FormsModule,
  ],
  templateUrl: './userAuth-component.html',
  styleUrl: './userAuth-component.css'
})

export class UserAuthComponent {
  tab: string = 'login';

  toRegisterForm()
  {
    this.tab = 'registration';
  }

  toLoginForm() {
    this.tab = 'login';
  }

}
