import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {PetDTO} from '../../api';
import {Textarea} from 'primeng/textarea';


@Component({
  selector: 'pet-add-component',
  standalone: true,
  imports: [DialogModule, ButtonModule, InputTextModule, ReactiveFormsModule, Textarea],
  templateUrl: './pet-add-component.html',
  styleUrls: ['./pet-add-component.css']
})
export class PetAddComponent {
  @Input() display: boolean = false;
  @Output() displayChange = new EventEmitter<boolean>();
  @Output() savePet = new EventEmitter<any>();  // emits the form values

  petForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.petForm = this.fb.group({
      species: ['', Validators.required],
      breed: ['', Validators.required],
      name: ['', Validators.required],
      location: ['', Validators.required],
      age: ['', [Validators.required, Validators.min(0)]],
      description: [''],
      imgURL: ['']
    });
  }

  hideDialog() {
    this.display = false;
    this.displayChange.emit(this.display);
  }

  onSave() {
    if (this.petForm.valid) {
      this.savePet.emit(this.petForm.value);
      this.hideDialog();
      this.petForm.reset();
    }
  }

  onCancel() {
    this.display = false;
  }

}
