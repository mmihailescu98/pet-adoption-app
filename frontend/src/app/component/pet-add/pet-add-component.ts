import {Component, Output, EventEmitter, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {Textarea} from 'primeng/textarea';
import {MapSearch} from '../map-search/map-search';
import {LocationDTO} from '../../api'
import {NavBar} from '../nav-bar/nav-bar';
import {Card} from 'primeng/card';
import {FloatLabel} from 'primeng/floatlabel';
import {Step, StepList, StepPanel, StepPanels, Stepper} from 'primeng/stepper';


@Component({
  selector: 'pet-add-component',
  standalone: true,
  imports: [DialogModule, ButtonModule, InputTextModule, ReactiveFormsModule, Textarea, MapSearch, NavBar, Card, FloatLabel, Stepper, StepList, Step, StepPanels, StepPanel, FormsModule],
  templateUrl: './pet-add-component.html',
  styleUrls: ['./pet-add-component.css']
})
export class PetAddComponent {
  @Output() displayChange = new EventEmitter<boolean>();
  @Output() savePet = new EventEmitter<any>();  // emits the form values
  @ViewChild(MapSearch) mapComponent!: MapSearch;

  petForm: FormGroup;
  private _activeStep: number = 1;

  get activeStep(): number {
    return this._activeStep;
  }

  set activeStep(value: number) {
    this._activeStep = value;
    if (value === 3) {
      if(this.mapComponent){
        this.mapComponent.emiteLocation();
      }
    }
  }

  constructor(private fb: FormBuilder) {
    this.petForm = this.fb.group({
      species: ['', Validators.required],
      breed: ['', Validators.required],
      name: ['', Validators.required],
      location: this.fb.group({
        state: ['', Validators.required],
        city: ['', Validators.required],
        street: [''],
        latitude: [null, Validators.required],
        longitude: [null, Validators.required]
      }),
      age: ['', [Validators.required, Validators.min(0)]],
      description: [''],
      imgURL: ['']
    });
  }

  onSave() {
    if (this.petForm.valid) {
      console.log("Pet Saved");
      console.log(this.petForm)
      this.petForm.reset();
    }
  }

  modifyLocation(loc: LocationDTO){
    this.petForm.get('location')?.patchValue({
      state: loc.state,
      city: loc.city,
      street: loc.street,
      latitude: loc.latitude,
      longitude: loc.longitude
    });
    console.log(loc);
  }

  protected readonly name = name;
}
