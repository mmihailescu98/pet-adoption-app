import { Routes } from '@angular/router';
import {TestComponent} from './component/test-component/test-component';
import {PetListComponent} from './component/pet-list/pet-list-component';

export const routes: Routes = [
  {
    path: '',
    component: TestComponent
  },
  {
    path: 'pet-list',
    component: PetListComponent
  }
];
