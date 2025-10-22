import { Routes } from '@angular/router';
import {PetListComponent} from './component/pet-list/pet-list-component';
import {UserAuthComponent} from './component/user-authentication/userAuth-component';
import { PetProfileComponent } from './component/pet-profile/pet-profile';
import {Dashboard} from './component/dashboard/dashboard';
import {PetAddComponent} from './component/pet-add/pet-add-component';

export const routes: Routes = [
  {
    path: '',
    component: UserAuthComponent
  },
  {
    path: 'pet-list',
    component: PetListComponent
  },
  {
    path: 'pet-profile/:id',
    component: PetProfileComponent
  },
  {
    path: 'dashboard',
    component: Dashboard
  },
  {
    path: 'add-pet',
    component: PetAddComponent
  }
];
