import { Routes } from '@angular/router';
import {TestComponent} from './component/test-component/test-component';
import {PetListComponent} from './component/pet-list/pet-list-component';
import {UserAuthComponent} from './component/user-authentication/userAuth-component';
import { PetProfileComponent } from './component/pet-profile/pet-profile';
import {Dashboard} from './component/dashboard/dashboard';
import {MapSearch} from './component/map-search/map-search';
import {UserProfileComponent} from './component/user-profile/user-profile-component';
import {authGuard} from './store/auth/auth.guard';

export const routes: Routes = [
  {
    path: '',
    component: UserAuthComponent
  },
  {
    path: 'pet-list',
    component: PetListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'pet-profile/:id',
    component: PetProfileComponent,
    canActivate: [authGuard]
  },
  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuard]
  },
  {
    path: 'maps-test',
    component: MapSearch,
    canActivate: [authGuard]
  },
  {
    path: 'user-profile/:id',
    component: UserProfileComponent,
    canActivate: [authGuard]
  }
];
