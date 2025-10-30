import { Component, Input, OnInit } from '@angular/core';
import { AsyncPipe, NgClass } from '@angular/common';
import { Store } from '@ngrx/store';
import {Observable, take} from 'rxjs';
import {AdoptionControllerService, Pet, PetDTO} from '../../api';
import { selectSelectedPet, selectPetStatus, selectPetError } from '../../store/pet/pet.selectors';
import {loadPet, adoptPet, removeFavoritePet, addFavoritePet, requestAdoptionForPet} from '../../store/pet/pet.actions';
import { ActivatedRoute } from '@angular/router';
import {NavBar} from '../nav-bar/nav-bar';
import {Button} from 'primeng/button';
import {Tooltip} from 'primeng/tooltip';
import {selectLoggedInUser} from '../../store/auth/auth.selector';

@Component({
  selector: 'app-pet-profile',
  standalone: true,
  imports: [AsyncPipe, NgClass, NavBar, Button, Tooltip],
  templateUrl: './pet-profile.html',
  styleUrls: ['./pet-profile.css']
})
export class PetProfileComponent implements OnInit {
  @Input() petId?: number;

  pet$!: Observable<PetDTO | null>;
  status$!: Observable<string>;
  error$!: Observable<any>;
  currentUser$!: Observable<any>;
  hasRequestedAdoption = false;

  PetStatus = Pet.StatusEnum;

  constructor(
    private store: Store,
    private route: ActivatedRoute,
    private adoptionService: AdoptionControllerService
  ) {}

  ngOnInit(): void {
    this.pet$ = this.store.select(selectSelectedPet);
    this.status$ = this.store.select(selectPetStatus);
    this.error$ = this.store.select(selectPetError);
    this.currentUser$ = this.store.select(selectLoggedInUser);

    this.route.params.subscribe(params => {
      if (params['id']) {
        const id = parseInt(params['id'], 10);
        this.store.dispatch(loadPet({ id }));
        this.checkIfUserRequestedAdoption(id);
      } else if (this.petId) {
        this.store.dispatch(loadPet({ id: this.petId }));
        this.checkIfUserRequestedAdoption(this.petId);
      }
    });
  }

  checkIfUserRequestedAdoption(petId: number): void {
    this.currentUser$.pipe(take(1)).subscribe(user => {
      if (user && user.id) {
        this.adoptionService.hasUserRequestedAdoption(petId, user.id).subscribe(
          hasRequested => {
            this.hasRequestedAdoption = hasRequested;
          },
          error => console.error('Error checking adoption request:', error)
        );
      }
    });
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

  adoptPet(id: number): void {
    this.store.select(selectLoggedInUser).pipe(take(1)).subscribe(user => {
      if (user && user.id) {
        this.store.dispatch(requestAdoptionForPet({ petId: id, userId: user.id }));
        setTimeout(() => {
          this.checkIfUserRequestedAdoption(id);
        }, 500);
      }
    });
  }
}
