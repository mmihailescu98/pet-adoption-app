  import {Component, OnInit} from '@angular/core';
  import {DataViewModule} from 'primeng/dataview';
  import {Button} from 'primeng/button';
  import {Store} from '@ngrx/store';
  import {loadPets} from '../../store/pet.actions';
  import {selectAllPets, selectPetError, selectPetStatus} from '../../store/pet.selectors';
  import {PetDTO} from '../../api/model/petDTO';
  import {Observable} from 'rxjs';
  import {AsyncPipe} from '@angular/common';

  @Component({
    selector: 'pet-list-component',
    imports: [
      DataViewModule,
      Button,
      AsyncPipe
    ],
    templateUrl: './pet-list-component.html',
    styleUrl: './pet-list-component.css'
  })

  export class PetListComponent implements OnInit {
    public pets$: Observable<PetDTO[]>;
    public status$: Observable<string>;
    public error$: Observable<any>;


    constructor(private store: Store) {
      this.pets$ = this.store.select(selectAllPets);
      this.status$ = this.store.select(selectPetStatus);
      this.error$ = this.store.select(selectPetError);
    }

    ngOnInit() {
      this.store.dispatch(loadPets());
    }

  }
