import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { PetProfileComponent } from './pet-profile';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { ActivatedRoute } from '@angular/router';
import { Button } from 'primeng/button';
import { Tooltip } from 'primeng/tooltip';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import {PetDTO} from '../../api';
import { selectPetError, selectPetStatus, selectSelectedPet } from '../../store/pet/pet.selectors';
import { removeFavoritePet } from '../../store/pet/pet.actions';
import { selectLoggedInUser } from '../../store/auth/auth.selector';

describe('PetProfileComponent', () => {
  let fixture: ComponentFixture<PetProfileComponent>;
  let component: PetProfileComponent;
  let store: MockStore;

  const mockPetFavorite : PetDTO = {
    id: 1,
    name: 'Buddy',
    species: 'Dog',
    breed: 'Labrador',
    age: '3',
    location: 'NY',
    status: 'PENDING',
    description: 'Friendly and energetic',
    imgURL: 'buddy.jpg',
    isUserFavorite: true
  };

  const mockPetNotFavorite = { ...mockPetFavorite, isUserFavorite: false };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PetProfileComponent, Button, Tooltip],
      providers: [
        provideMockStore(),
        {
          provide: ActivatedRoute,
          useValue: { params: of({ id: 1 }) } // simulate route /pets/1
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PetProfileComponent);
    component = fixture.componentInstance;
    store = TestBed.inject(MockStore);
  });

  it('renders filled star when pet is favorite', fakeAsync(() => {
    // Arrange selectors BEFORE first change detection
    store.overrideSelector(selectSelectedPet, mockPetFavorite);
    store.overrideSelector(selectPetStatus, 'success');
    store.overrideSelector(selectPetError, null);

    // Act
    fixture.detectChanges();
    tick();
    fixture.detectChanges();

    const pButton = fixture.debugElement.query(By.css('p-button'));
    expect(pButton).toBeTruthy();
    expect(pButton.componentInstance.icon).toBe('pi pi-star-fill');
  }));

  it('renders empty star when pet is favorite', fakeAsync(() => {
    // Arrange selectors BEFORE first change detection
    store.overrideSelector(selectSelectedPet, mockPetNotFavorite);
    store.overrideSelector(selectPetStatus, 'success');
    store.overrideSelector(selectPetError, null);

    // Act
    fixture.detectChanges();
    tick();
    fixture.detectChanges();

    const pButton = fixture.debugElement.query(By.css('p-button'));
    expect(pButton).toBeTruthy();
    expect(pButton.componentInstance.icon).toBe('pi pi-star');
  }));

  it('clicking star on favorite dispatches remove and icon becomes empty', fakeAsync(() => {
    const mockUser = { id: 10 } as any;
    store.overrideSelector(selectSelectedPet, mockPetFavorite);
    store.overrideSelector(selectPetStatus, 'success');
    store.overrideSelector(selectPetError, null);
    store.overrideSelector(selectLoggedInUser, mockUser);

    const dispatchSpy = spyOn(store, 'dispatch');

    // Initial render with favorite pet -> filled star
    fixture.detectChanges();
    tick();
    fixture.detectChanges();

    let pButton = fixture.debugElement.query(By.css('p-button'));
    expect(pButton).toBeTruthy();
    expect(pButton.componentInstance.icon).toBe('pi pi-star-fill');

    // Click star -> should dispatch removeFavoritePet
    pButton.triggerEventHandler('onClick', {});
    expect(dispatchSpy).toHaveBeenCalledWith(removeFavoritePet({ petId: 1, userId: 10 }));

    // Simulate store update after remove -> pet not favorite
    store.overrideSelector(selectSelectedPet, mockPetNotFavorite);
    store.refreshState();
    tick();
    fixture.detectChanges();

    pButton = fixture.debugElement.query(By.css('p-button'));
    expect(pButton).toBeTruthy();
    expect(pButton.componentInstance.icon).toBe('pi pi-star');
  }));

});
