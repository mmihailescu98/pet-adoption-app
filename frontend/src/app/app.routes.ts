import { Routes } from '@angular/router';
import {TestComponent} from './component/test-component/test-component';
import {PetListComponent} from './component/pet-list/pet-list-component';
import {UserAuthComponent} from './component/user-authentication/userAuth-component';

export const routes: Routes = [
  {
    path: '',
    component: TestComponent
  },
  {
    path: 'pet-list',
    component: PetListComponent
  },
  {
    path: 'user-registration',
    component: UserAuthComponent
  }
];
