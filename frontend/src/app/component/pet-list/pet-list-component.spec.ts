import {ReactiveFormsModule} from '@angular/forms';
import {PetListComponent} from './pet-list-component';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {loadPets, searchPets} from '../../store/pet-store/pet.actions';
import {By} from '@angular/platform-browser';
import {MockStore, provideMockStore} from '@ngrx/store/testing';
import {of} from 'rxjs';

describe('PetListComponent - Filtering', () => {
  let component: PetListComponent;
  let fixture: ComponentFixture<PetListComponent>;
  let store: MockStore;

  const initialState = {
    pets: {
      pets: [],
      error: null,
      status: 'pending',
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        PetListComponent
      ],
      providers: [
        provideMockStore({ initialState })
      ]
    }).compileComponents();


    fixture = TestBed.createComponent(PetListComponent);
    component = fixture.componentInstance;
    store = TestBed.inject(MockStore);

    fixture.detectChanges();
  });

  it('should dispatch loadPets on init', () => {
    const spy = spyOn(store, 'dispatch');
    component.ngOnInit();
    expect(spy).toHaveBeenCalledWith(loadPets());
  });

  it('should dispatch searchPets when species changes', () => {
    const spy = spyOn(store, 'dispatch');
    component.filterForm.get('species')?.setValue('dog');
    fixture.detectChanges();
    expect(spy).toHaveBeenCalledWith(searchPets({ species: 'dog', breed: '' }));
  });

  it ('should dispatch searchPets when breed changes', () => {
    const spy = spyOn(store, 'dispatch');
    component.filterForm.get('breed')?.setValue('husky');
    fixture.detectChanges();
    expect(spy).toHaveBeenCalledWith(searchPets({ species: '', breed: 'husky' }));
  });

  it('should reset filter form when reset button is clicked', () => {
    component.filterForm.setValue({ species: 'dog', breed: 'husky' });
    fixture.detectChanges()

    const resetButton = fixture.debugElement.query(By.css('.reset-search-button'));
    resetButton.triggerEventHandler('click', new Event('click'));
    fixture.detectChanges();

    expect(component.filterForm.value).toEqual({ species: '', breed: '' });
  });

  it('should show species suggestions when user types', () => {
    component.allSpecies$ = of(['Dog', 'Cat', 'Rabbit']);
    component.filteredSpecies$ = component['createFiltered$'](
      component.allSpecies$,
      'species'
    );

    fixture.detectChanges();

    const input = fixture.debugElement.query(By.css('input[formControlName="species"]')).nativeElement;
    input.value = 'd';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    const suggestions = fixture.debugElement.queryAll(By.css('.suggestions-list li'));
    expect(suggestions.length).toBeGreaterThan(0);
    expect(suggestions[0].nativeElement.textContent.trim()).toBe('Dog');
  });

  it('should not show suggestions when input is empty', ()=> {
    component.allSpecies$ = of(['Dog', 'Cat', 'Rabbit']);
    component.filteredSpecies$ = component['createFiltered$'](
      component.allSpecies$,
      'species'
    );

    fixture.detectChanges();

    component.filterForm.get('species')?.setValue('');
    fixture.detectChanges();

    const suggestions = fixture.debugElement.queryAll(By.css('.suggestions-list li'));
    expect(suggestions.length).toBe(0);
  });
});
