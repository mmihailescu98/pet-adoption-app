import {Component, Input} from '@angular/core';
import {AdoptedPetDTO} from '../../api';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'adopted-pets-history',
  templateUrl: './adopted-pets-history-component.html',
  imports: [
    AsyncPipe,
    DatePipe
  ],
  styleUrl: './adopted-pets-history-component.css'
})
export class AdoptedPetsHistoryComponent {
  @Input() adoptedPets$?: Observable<AdoptedPetDTO[]>;
}
