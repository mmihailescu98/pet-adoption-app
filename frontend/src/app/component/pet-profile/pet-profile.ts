import { Component, Input, OnInit } from '@angular/core';
import { AsyncPipe, NgClass } from '@angular/common';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import {PetDTO, UserLoginModel} from '../../api';
import { loadPet, adoptPet } from '../../store/pet/pet.actions';
import { selectSelectedPet, selectPetStatus, selectPetError } from '../../store/pet/pet.selectors';
import { ActivatedRoute } from '@angular/router';
import {NavBar} from '../nav-bar/nav-bar';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {selectLoggedInUser} from '../../store/auth/auth.selector';
import {ButtonDirective, ButtonIcon} from 'primeng/button';

@Component({
  selector: 'app-pet-profile',
  standalone: true,
  imports: [AsyncPipe, NgClass, NavBar, ReactiveFormsModule, ButtonDirective, ButtonIcon],
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
        console.log("User : \n",user);
        this.userId = user?.id!
        this.userIsAdmin = user?.isAdmin!;
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
      console.log("Form invalid")
      return;
    }
    const updated = { ...pet, ...this.editForm.value } as PetDTO;

    this.editMode = false;
  }
}
