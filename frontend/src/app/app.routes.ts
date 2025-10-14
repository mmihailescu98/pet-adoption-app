import { Routes } from '@angular/router';
import {TestComponent} from './component/test-component/test-component';
import {PetListComponent} from './component/pet-list/pet-list-component';
import {UserAuthComponent} from './component/user-authentication/userAuth-component';
import { PetProfileComponent } from './component/pet-profile/pet-profile';
import {Dashboard} from './component/dashboard/dashboard';
import {MapSearch} from './component/map-search/map-search';

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
    path: 'maps-test',
    component: MapSearch
  }
];
