import {Routes} from '@angular/router';
import {TestComponent} from './component/test-component/test-component';
import {PetListComponent} from './component/pet-list/pet-list-component';
import {UserAuthComponent} from './component/user-authentication/userAuth-component';
import {PetProfileComponent} from './component/pet-profile/pet-profile';
import {Dashboard} from './component/dashboard/dashboard';
import {MapSearch} from './component/map-search/map-search';
import {UserProfileComponent} from './component/user-profile/user-profile-component';
import {authGuard} from './store/auth/auth.guard';

export const routes: Routes = [
  {
    path: 'authentication',
    component: UserAuthComponent
  },

  {
    path: '',
    canActivateChild: [authGuard],
    children: [
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
      },
      {
        path: 'user-profile/:id',
        component: UserProfileComponent
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'pet-list'
      }
      ]
  },

  {
    path: '**',
    redirectTo: ''
  }
];
