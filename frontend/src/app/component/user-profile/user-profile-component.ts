import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {OnDestroy, OnInit} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {ButtonModule} from 'primeng/button';
import {DataViewModule} from 'primeng/dataview';
import {DialogModule} from 'primeng/dialog';
import {InputTextModule} from 'primeng/inputtext';
import {SelectModule} from 'primeng/select';
import {Store} from '@ngrx/store';
import {
  selectAdoptedPetsByUser,
  selectCurrentUser,
  selectUserError,
  selectUserLoading
} from '../../store/user/user.selectors';
import {takeUntil} from 'rxjs/operators';
import {PetDTO, UserDTO} from '../../api';
import {ActivatedRoute} from '@angular/router';
import {EditProfileDialogComponent} from '../edit-profile-dialog/edit-profile-dialog-component';
import {AdoptedPetsHistoryComponent} from '../adopted-pets-history/adopted-pets-history-component';
import {loadAdoptedPetsByUser, loadUser, updateUser} from '../../store/user/user.actions';
import {LOCATION_OPTIONS} from '../../resources/constants/location.constants';
import {PetAddComponent} from '../pet-add/pet-add-component';
import {NavBar} from '../nav-bar/nav-bar';

@Component({
  selector: 'user-profile-component',
  imports: [
    DataViewModule,
    ReactiveFormsModule,
    DialogModule,
    InputTextModule,
    ButtonModule,
    SelectModule,
    EditProfileDialogComponent,
    AdoptedPetsHistoryComponent,
    PetAddComponent,
    NavBar
  ],
  templateUrl: './user-profile-component.html',
  styleUrl: './user-profile-component.css'
})

export class UserProfileComponent implements OnInit, OnDestroy {
  @Input() userID?: number;
  private currentUserID?: number;

  editForm!: FormGroup;
  showEditDialog = false;

  user$: Observable<UserDTO | null>;
  loading$: Observable<boolean>;
  error$: Observable<any>;
  adoptedPets$: Observable<PetDTO[]>;

  locationOptions = [...LOCATION_OPTIONS];

  showAddDialog = false;

  private destroy$ = new Subject<void>();

  constructor(private store: Store, private formBuilder: FormBuilder, private route: ActivatedRoute) {
    this.user$ = this.store.select(selectCurrentUser);
    this.loading$ = this.store.select(selectUserLoading);
    this.error$ = this.store.select(selectUserError);
    this.adoptedPets$ = this.store.select(selectAdoptedPetsByUser);

    this.initializeEditForm();
  }

  ngOnInit() {
    this.loadUserFromRoute()
    this.subscribeToUserChanges();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onEdit() {
    this.showEditDialog = true;
  }

  onSaveEdit(updatedUser: UserDTO) {
    if (this.currentUserID) {
      this.store.dispatch(updateUser({user: {...updatedUser, id: this.currentUserID}}));
      this.showEditDialog = false;
    }
  }

  onCancelEdit() {
    this.showEditDialog = false;

    this.user$.pipe(takeUntil(this.destroy$)).subscribe(user => {
      if (user) {
        this.editForm.patchValue(user);
      }
    });
  }

  onAddPet() {
    this.showAddDialog = true;
  }

  onOpenFavorites() {
    // TODO: Link logic to open favorites
  }

  private initializeEditForm() {
    this.editForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.maxLength(50)]],
      name: ['', [Validators.minLength(2), Validators.maxLength(50)]],
      phone: ['', [Validators.pattern(/^\d{9,15}$/) ,Validators.maxLength(15)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(50)]],
      location: [''],
      imgURL: ['', [Validators.maxLength(500)]],
      bio: ['', [Validators.maxLength(1000)]]
    })
  }

  private loadUserFromRoute() {
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe(params => {
      const id = parseInt(params['id']);
      if (id) {
        this.currentUserID = id;
        this.store.dispatch(loadUser({userID: id}));
        this.store.dispatch(loadAdoptedPetsByUser({userID: id}));
      } else if (this.userID) {
        this.currentUserID = this.userID;
        this.store.dispatch(loadUser({userID: this.userID}));
        this.store.dispatch(loadAdoptedPetsByUser({userID: this.userID}));
      }
    });
  }

  private subscribeToUserChanges() {
    this.user$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        if (user) {
          if (user.id) {
            this.currentUserID = user.id;
          }
          this.editForm.patchValue(user);
        }
      });
  }

  saveNewPet(pet: PetDTO) {}
}
