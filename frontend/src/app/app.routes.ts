import { Routes } from '@angular/router';
import {TestComponent} from './component/test-component/test-component';
import {PetProfileComponent} from './component/pet-profile/pet-profile';

export const routes: Routes = [
  {
    path: '',
    component: TestComponent
  },
  {
    path: 'pet-profile',
    component: PetProfileComponent
  }
];
