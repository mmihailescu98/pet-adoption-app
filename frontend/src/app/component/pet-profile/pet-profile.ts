import { Component, Input, OnInit } from '@angular/core';
import { AsyncPipe, NgClass } from '@angular/common';
import { Store } from '@ngrx/store';
import {Observable , take} from 'rxjs';
import {AdoptionControllerService, Pet, PetDTO, UserLoginModel} from '../../api';
import {loadPet, adoptPet, updatePet, resetUpdateStatus, resetUpdateError, removeFavoritePet, addFavoritePet, requestAdoptionForPet} from '../../store/pet/pet.actions';
import {
  selectSelectedPet,
  selectPetStatus,
  selectPetError,
  selectUpdateStatus,
  selectUpdateError
} from '../../store/pet/pet.selectors';
import { ActivatedRoute } from '@angular/router';
import {NavBar} from '../nav-bar/nav-bar';
import {GoogleMap, MapAdvancedMarker} from '@angular/google-maps';
import {Button, ButtonDirective, ButtonIcon} from 'primeng/button';
import {Tooltip} from 'primeng/tooltip';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {selectLoggedInUser} from '../../store/auth/auth.selector';
import {InputText} from 'primeng/inputtext';

@Component({
  selector: 'app-pet-profile',
  standalone: true,
  imports: [AsyncPipe, NgClass, NavBar, GoogleMap, MapAdvancedMarker,
    Button, Tooltip, ReactiveFormsModule, InputText, ButtonDirective, ButtonIcon],
  templateUrl: './pet-profile.html',
  styleUrls: ['./pet-profile.css']
})
export class PetProfileComponent implements OnInit {
  @Input() petId?: number;

  pet$!: Observable<PetDTO | null>;
  status$!: Observable<string>;
  error$!: Observable<any>;
  user$!: Observable<UserLoginModel | null>;

  userId: number = -1;
  userIsAdmin: boolean = false;
  editMode = false;
  editForm!: FormGroup;
  previewPet : PetDTO | null = null;
  updateError$!: Observable<any>;
  updateStatus$!: Observable<any>;
  currentUser$!: Observable<any>;
  hasRequestedAdoption = false;

  PetStatus = Pet.StatusEnum;

  constructor(
    private store: Store,
    private route: ActivatedRoute,
    private adoptionService: AdoptionControllerService,
    private formBuilder: FormBuilder
  ) {}

  //Map Options
  zoom = 14;
  display: google.maps.LatLngLiteral | undefined;
  options: google.maps.MapOptions = {
    fullscreenControl: false,
    keyboardShortcuts: false,
    streetViewControl: false,
  }
  markerOptions: google.maps.marker.AdvancedMarkerElementOptions = {};

  //Events
  move(event: google.maps.MapMouseEvent) {
    if(event.latLng)
      this.display = event.latLng.toJSON();
  }

  ngOnInit(): void {
    this.pet$ = this.store.select(selectSelectedPet);
    this.status$ = this.store.select(selectPetStatus);
    this.error$ = this.store.select(selectPetError);
    this.currentUser$ = this.store.select(selectLoggedInUser);
    this.user$ = this.store.select(selectLoggedInUser);
    this.updateStatus$ = this.store.select(selectUpdateStatus);
    this.updateError$ = this.store.select(selectUpdateError);


    // First try to get ID from route parameters
    this.route.params.subscribe(params => {
      if (params['id']) {
        const id = parseInt(params['id'], 10);
        this.store.dispatch(loadPet({ id }));
        this.checkIfUserRequestedAdoption(id);
      } else if (this.petId) {
        // Fallback to input property if no route param
        this.store.dispatch(loadPet({ id: this.petId }));
        this.checkIfUserRequestedAdoption(this.petId);
      }
    });
    this.user$.subscribe((user) => {
      if(user)
        this.userId = user?.id!
      this.userIsAdmin = user?.isAdmin!;
    });

    this.updateStatus$.pipe().subscribe((value) => {
      if(value == "success") {
        alert("Updated successfully.");
        this.store.dispatch(resetUpdateStatus());
      }
    })

    this.updateError$.subscribe((error) => {
      if(error)
      {
        alert(error);
        this.store.dispatch(resetUpdateError());
      }
    })
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

  startEditing(pet: PetDTO): void {

    this.editForm = this.formBuilder.group({
      name: [pet.name || '', [Validators.required]],
      species: [pet.species || '',[Validators.required]],
      breed: [pet.breed || '',[Validators.required]],
      age: [pet.age || '',[Validators.min(0),Validators.max(150)]],
      location: pet.location || '',
      description: pet.description || '',
      imgURL: pet.imgURL || ''
    });

    this.previewPet = {...pet};

    this.editForm.valueChanges.subscribe(values => {
      this.previewPet = { ...this.previewPet, ...values };
    });

    this.editMode = true;
  }

  cancelEditing(): void {
    this.editMode = false;
  }

  saveEditing(pet: PetDTO): void {
    if(this.editForm.invalid)
    {
      alert("All required fields must be added!")
      return;
    }
    const updatedPet = { ...pet, ...this.editForm.value } as PetDTO;

    this.store.dispatch(updatePet({updatedPet}));

    this.editMode = false;
  }

  limitDescriptionLines(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    const lines = textarea.value.split('\n');

    if (lines.length > 5) {
      const trimmedValue = lines.slice(0, 5).join('\n');
      // Update both the textarea and the form control
      this.editForm.get('description')?.setValue(trimmedValue, { emitEvent: false });
    }
  }
}
