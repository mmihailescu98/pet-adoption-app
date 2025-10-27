import {Component, Input, Output, EventEmitter} from '@angular/core';
import {FormGroup, ReactiveFormsModule} from '@angular/forms';
import {DialogModule} from 'primeng/dialog';
import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {SelectModule} from 'primeng/select';
import {Textarea} from 'primeng/textarea';
import {UserDTO} from '../../api';

@Component({
  selector: 'edit-profile-dialog',
  imports: [
    ReactiveFormsModule,
    DialogModule,
    InputTextModule,
    ButtonModule,
    SelectModule,
    Textarea,
  ],
  templateUrl: './edit-profile-dialog-component.html',
  styleUrl: './edit-profile-dialog-component.css',
})
export class EditProfileDialogComponent {
  @Input() editForm: FormGroup = new FormGroup({});
  @Input() locationOptions: string[] = [];
  @Input() showEditDialog: boolean = false;
  @Output() cancelEdit = new EventEmitter<void>();
  @Output() saveEdit = new EventEmitter<UserDTO>();
  @Output() showEditDialogChange = new EventEmitter<boolean>();

  onCancelEdit() {
    this.showEditDialogChange.emit(false);
    this.cancelEdit.emit();
  }

  onSaveEdit() {
    if(this.editForm.valid){
      this.showEditDialogChange.emit(false);
      this.saveEdit.emit(this.editForm.value);
    }
  }

  onDialogHide() {
    this.showEditDialogChange.emit(false);
    this.cancelEdit.emit();
  }
}
