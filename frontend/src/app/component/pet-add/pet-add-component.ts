import {Component, Output, EventEmitter, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {DialogModule} from 'primeng/dialog';
import {ButtonModule} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {Textarea} from 'primeng/textarea';
import {MapSearch} from '../map-search/map-search';
import {LocationDTO} from '../../api'
import {NavBar} from '../nav-bar/nav-bar';
import {Card} from 'primeng/card';
import {FloatLabel} from 'primeng/floatlabel';
import {Step, StepList, StepPanel, StepPanels, Stepper} from 'primeng/stepper';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {addPet} from '../../store/pet/pet.actions';


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

  // Force map component to emit location for form when the user is on the save panel
  set activeStep(value: number) {
    this._activeStep = value;
    if (value === 3) {
      if (this.mapComponent) {
        this.mapComponent.emitLocation();
      }
    }
  }

  constructor(private fb: FormBuilder, private router: Router, private store: Store) {
    this.petForm = this.fb.group({
      species: ['', [Validators.required]],
      breed: ['', [Validators.required]],
      name: ['', [Validators.required]],
      location: this.fb.group({
        state: ['', [Validators.required]],
        city: ['', [Validators.required]],
        street: [''],
        latitude: [null, [Validators.required]],
        longitude: [null, [Validators.required]]
      }),
      age: ['', [Validators.required, Validators.min(0)]],
      description: [''],
      imgURL: ['']
    });
  }

  //Getters
  get name() {
    return this.petForm.get('name');
  }

  get species() {
    return this.petForm.get('species');
  }

  get breed() {
    return this.petForm.get('breed');
  }

  get age() {
    return this.petForm.get('age');
  }

  get description() {
    return this.petForm.get('description');
  }

  get imgURL() {
    return this.petForm.get('imgURL');
  }

  getErrorMessage(controlName: string): string {
    const control = this.petForm.get(controlName);
    if (control?.hasError('required')) {
      return 'This field is required.';
    }
    if (control?.hasError('minlength')) {
      const reqLen = control.getError('minlength')?.requiredLength;
      return `Must be at least ${reqLen} characters long.`;
    }
    if (control?.hasError('min')) {
      const minVal = control.getError('min')?.min;
      return `Value must be at least ${minVal}.`;
    }
    return '';
  }


  onSave() {
    if (this.petForm.invalid) {
      this.petForm.markAllAsTouched();
      return;
    }

    const newPet = this.petForm.value;

    this.store.dispatch(addPet({pet: newPet}));

    this.router.navigate(['/pet-list']);
    alert("Pet saved!");
  }

  //This is used to modify petForm location by the map component
  modifyLocation(loc: LocationDTO) {
    this.petForm.get('location')?.patchValue({
      state: loc.state,
      city: loc.city,
      street: loc.street,
      latitude: loc.latitude,
      longitude: loc.longitude
    });
  }
}
