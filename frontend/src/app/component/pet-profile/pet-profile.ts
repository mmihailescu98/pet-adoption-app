import { Component, Input, OnInit } from '@angular/core';
import { AsyncPipe, NgClass } from '@angular/common';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';import { PetDTO } from '../../api';
import { loadPet, adoptPet } from '../../store/pet.actions';
import { selectSelectedPet, selectPetStatus, selectPetError } from '../../store/pet.selectors';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-pet-profile',
  standalone: true,
  imports: [AsyncPipe, NgClass],
  templateUrl: './pet-profile.html',
  styleUrls: ['./pet-profile.css']
})
export class PetProfileComponent implements OnInit {
  @Input() petId?: number;

  pet$!: Observable<PetDTO | null>;
  status$!: Observable<string>;
  error$!: Observable<any>;

  constructor(
    private store: Store,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.pet$ = this.store.select(selectSelectedPet);
    this.status$ = this.store.select(selectPetStatus);
    this.error$ = this.store.select(selectPetError);

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
  }

  adoptPet(id: number): void {
  this.store.dispatch(adoptPet({ id }));
}
}