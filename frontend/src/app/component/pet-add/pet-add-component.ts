import {Component, Output, EventEmitter, ViewChild} from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
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
import {addPetForAdoption} from '../../store/pet/pet.actions';


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

  adoptionRequestForm: FormGroup;
  petForm: FormGroup;
  private _activeStep: number = 1;

  get activeStep(): number {
    return this._activeStep;
  }

  // Force map component to emit location for form when the user is on the save panel
  set activeStep(value: number) {
    if (value === 3) {
      if (this.mapComponent) {
        this.mapComponent.emitLocation();
      }
    }
    this._activeStep = value;
  }

  constructor(private fb: FormBuilder, private router: Router, private store: Store) {
    this.petForm = this.fb.group({
      species: ['', [Validators.required, Validators.maxLength(50)]],
      breed: ['', [Validators.required, Validators.maxLength(50)]],
      name: ['', [Validators.required, Validators.maxLength(50)]],
      location: this.fb.group({
        state: ['', [Validators.required]],
        city: ['', [Validators.required]],
        street: [''],
        latitude: [null, [Validators.required]],
        longitude: [null, [Validators.required]]
      }),
      age: ['', [Validators.required, Validators.min(0), Validators.maxLength(50)]],
      description: [''],
      imgURL: ['', [Validators.required, Validators.min(0), Validators.maxLength(255)]]
    });

    this.adoptionRequestForm = this.fb.group({
      pet: this.petForm,
      additionalImages: this.fb.array<FormControl<string>>([]),
      contactNumber: ['', [Validators.required, Validators.min(0), Validators.maxLength(20)]],
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

  get contactNumber() {
    return this.adoptionRequestForm.get('contactNumber');
  }

  get additionalImages(): FormArray<FormControl<string>> {
    return this.adoptionRequestForm.get('additionalImages') as FormArray<FormControl<string>>;
  }


  addImage(url: string = '') {
    if (this.additionalImages.length >= 5) {
      console.warn('Maximum 5 images are allowed.');
      return;
    }

    const control = this.fb.control<string>(
      url,
      {
        validators: [Validators.required, Validators.maxLength(255)],
        nonNullable: true,
      }
    );

    this.additionalImages.push(control);
  }

  removeImage(index: number) {
    this.additionalImages.removeAt(index);
  }

  getErrorMessage(controlName: string, form: FormGroup): string {
    const control = form.get(controlName);
    if (control?.hasError('required')) {
      return 'This field is required.';
    }
    if (control?.hasError('minlength')) {
      const reqLen = control.getError('minlength')?.requiredLength;
      return `Must be at least ${reqLen} characters long.`;
    }
    if (control?.hasError('maxlength')) {
      const reqLen = control.getError('maxlength')?.requiredLength;
      return `Must be at most ${reqLen} characters long.`;
    }
    if (control?.hasError('min')) {
      const minVal = control.getError('min')?.min;
      return `Value must be at least ${minVal}.`;
    }
    return '';
  }


  onSave() {
    if (this.adoptionRequestForm.invalid) {
      this.adoptionRequestForm.markAllAsTouched();
      return;
    }

    const adoptionRequest = this.adoptionRequestForm.value;

    this.store.dispatch(addPetForAdoption({adoptionRequest}));
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
