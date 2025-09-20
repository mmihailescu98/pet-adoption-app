import { Routes } from '@angular/router';
import {TestComponent} from './component/test-component/test-component';
import {PetListComponent} from './component/pet-list/pet-list-component';
import {UserAuthComponent} from './component/user-authentication/userAuth-component';
import { PetProfileComponent } from './component/pet-profile/pet-profile';

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
  },
  {
    path: 'pet-profile/:id',
    component: PetProfileComponent
  }
];
