import {Component, OnDestroy, OnInit} from '@angular/core';
import {DataViewModule} from 'primeng/dataview';
import {Button, ButtonDirective} from 'primeng/button';
import {Store} from '@ngrx/store';
import {addFavoritePet, loadPets, removeFavoritePet, searchPets} from '../../store/pet/pet.actions';
import {selectAllPets, selectPetStatus, selectPetError} from '../../store/pet/pet.selectors';
import {PetDTO} from '../../api/model/petDTO';
import {Observable, startWith, Subject, takeUntil, map, switchMap, take} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {ReactiveFormsModule, FormGroup, FormBuilder} from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';
import {NavBar} from '../nav-bar/nav-bar';
import {RouterLink} from '@angular/router';
import {PetAddComponent} from '../pet-add/pet-add-component';
import {selectLoggedInUser} from '../../store/auth/auth.selector';

@Component({
  selector: 'pet-list-component',
  imports: [
    DataViewModule,
    Button,
    AsyncPipe,
    ReactiveFormsModule,
    ButtonDirective,
    NavBar,
    RouterLink,
    PetAddComponent,
    TooltipModule,
  ],
  templateUrl: './pet-list-component.html',
  styleUrl: './pet-list-component.css'
})

export class PetListComponent implements OnInit, OnDestroy {
  pets$: Observable<PetDTO[]>;
  status$: Observable<string>;
  error$: Observable<any>;

  allSpecies$: Observable<string[]>;
  allBreeds$: Observable<string[]>;
  filteredSpecies$: Observable<string[]>;
  filteredBreeds$: Observable<string[]>;

  showAddDialog = false;
  filterForm: FormGroup;

  private destroy$ = new Subject<void>();

  constructor(private store: Store, private formBuilder: FormBuilder) {
    this.pets$ = this.store.select(selectAllPets);
    this.status$ = this.store.select(selectPetStatus);
    this.error$ = this.store.select(selectPetError);

    this.filterForm = this.formBuilder.group({species: '', breed: ''});

    this.allSpecies$ = this.createLookup$(this.pets$, 'species');
    this.allBreeds$ = this.createLookup$(this.pets$, 'breed');

    this.filteredSpecies$ = this.createFiltered$(this.allSpecies$, 'species');
    this.filteredBreeds$ = this.createFiltered$(this.allBreeds$, 'breed');
  }

  ngOnInit() {
    this.store.dispatch(loadPets());
    this.filterForm.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(({species, breed}) => {this.store.dispatch(searchPets({breed, species}));});
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  openDialog() {
    this.showAddDialog = true;
  }

  saveNewPet(pet: PetDTO) {
  }

  onReset(event: Event) {
    this.filterForm.reset({species: '', breed: ''});
  }

  selectSuggestion(controlName: 'species' | 'breed', value: string) {
    this.filterForm.get(controlName)?.setValue(value, { emitEvent: true });
  }

  private createLookup$(source$: Observable<PetDTO[]>, key: 'species' | 'breed'): Observable<string[]> {
    return source$.pipe(map(pets => Array.from(new Set(pets.map(p => p[key]).filter((v): v is string => !!v)))));
  }

  private createFiltered$(list$: Observable<string[]>, controlName: 'species' | 'breed'): Observable<string[]> {
    return list$.pipe(
      switchMap(list =>
        this.filterForm.get(controlName)!.valueChanges.pipe(
          startWith(this.filterForm.get(controlName)!.value || ''),
          map(input => {
            if (!input) return [];
            return list.filter(item =>
              item.toLowerCase().includes(input.toLowerCase()) &&
              item.toLowerCase() !== input.toLowerCase()
            );
          })
        )
      )
    );
  }

  toggleFavorite(pet: PetDTO) {
    this.store.select(selectLoggedInUser).pipe(take(1)).subscribe(user => {
      if (user) {
        if(pet.isUserFavorite) {
          this.store.dispatch(removeFavoritePet({ petId: pet.id!, userId: user.id! }));
        } else {
          this.store.dispatch(addFavoritePet({ petId: pet.id!, userId: user.id! }));
        }
      } else
        alert("Please log in to manage favorites.");
    });
  }
}
