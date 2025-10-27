import { Component, Input, OnInit } from '@angular/core';
import { AsyncPipe, NgClass } from '@angular/common';
import { Store } from '@ngrx/store';
import {Observable} from 'rxjs';
import {PetDTO, UserLoginModel} from '../../api';
import {loadPet, adoptPet, updatePet, resetUpdateStatus, resetUpdateError} from '../../store/pet/pet.actions';
import {
  selectSelectedPet,
  selectPetStatus,
  selectPetError,
  selectUpdateStatus,
  selectUpdateError
} from '../../store/pet/pet.selectors';
import { ActivatedRoute } from '@angular/router';
import {NavBar} from '../nav-bar/nav-bar';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {selectLoggedInUser} from '../../store/auth/auth.selector';
import {ButtonDirective, ButtonIcon} from 'primeng/button';
import {InputText} from 'primeng/inputtext';

@Component({
  selector: 'app-pet-profile',
  standalone: true,
  imports: [AsyncPipe, NgClass, NavBar, ReactiveFormsModule, ButtonDirective, ButtonIcon, InputText],
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

  constructor(
    private store: Store,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.pet$ = this.store.select(selectSelectedPet);
    this.status$ = this.store.select(selectPetStatus);
    this.error$ = this.store.select(selectPetError);
    this.user$ = this.store.select(selectLoggedInUser);
    this.updateStatus$ = this.store.select(selectUpdateStatus);
    this.updateError$ = this.store.select(selectUpdateError);


    // First try to get ID from route parameters
    this.route.params.subscribe(params => {
      if (params['id']) {
        const id = parseInt(params['id'], 10);
        this.store.dispatch(loadPet({ id }));
      } else if (this.petId) {
        // Fallback to input property if no route param
        this.store.dispatch(loadPet({ id: this.petId }));
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

  adoptPet(id: number): void {
  this.store.dispatch(adoptPet({ id }));
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
