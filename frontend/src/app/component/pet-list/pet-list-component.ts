  import {Component, OnInit} from '@angular/core';
  import {DataViewModule} from 'primeng/dataview';
  import {Button, ButtonDirective, ButtonLabel} from 'primeng/button';
  import {Store} from '@ngrx/store';
  import {loadPets, searchPets, searchPetsSuccess} from '../../store/pet.actions';
  import {selectAllPets, selectPetError, selectPetStatus} from '../../store/pet.selectors';
  import {PetDTO} from '../../api/model/petDTO';
  import {Observable} from 'rxjs';
  import {AsyncPipe} from '@angular/common';
  import {FormsModule} from '@angular/forms';
  import {InputText} from 'primeng/inputtext';

  @Component({
    selector: 'pet-list-component',
    imports: [
      DataViewModule,
      Button,
      AsyncPipe,
      FormsModule,
      InputText,
      ButtonDirective,
      ButtonLabel
    ],
    templateUrl: './pet-list-component.html',
    styleUrl: './pet-list-component.css'
  })

  export class PetListComponent implements OnInit {
    public pets$: Observable<PetDTO[]>;
    public status$: Observable<string>;
    public error$: Observable<any>;

    searchTerm: string = '';

    constructor(private store: Store) {
      this.pets$ = this.store.select(selectAllPets);
      this.status$ = this.store.select(selectPetStatus);
      this.error$ = this.store.select(selectPetError);
    }

    ngOnInit() {
      this.store.dispatch(loadPets());
    }

    onFilter(event: Event) {
      this.store.dispatch(searchPets({ breed: this.searchTerm, species: this.searchTerm }));
    }

  }
