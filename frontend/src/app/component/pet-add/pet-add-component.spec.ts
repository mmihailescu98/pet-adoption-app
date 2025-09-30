import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PetAddComponent } from './pet-add-component';
import { ReactiveFormsModule } from '@angular/forms';

describe('PetAddComponent', () => {
  let component: PetAddComponent;
  let fixture: ComponentFixture<PetAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        PetAddComponent
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PetAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the form with required controls', () => {
    expect(component.petForm.contains('species')).toBeTrue();
    expect(component.petForm.contains('breed')).toBeTrue();
    expect(component.petForm.contains('name')).toBeTrue();
    expect(component.petForm.contains('location')).toBeTrue();
    expect(component.petForm.contains('age')).toBeTrue();
    expect(component.petForm.contains('description')).toBeTrue();
    expect(component.petForm.contains('imgURL')).toBeTrue();
  });

  it('should emit savePet when onSave called with valid form', () => {
    spyOn(component.savePet, 'emit');

    const expectedPet = {
      species: 'Dog',
      breed: 'Husky',
      name: 'Snow',
      location: 'NY',
      age: 3,
      description: 'Friendly husky',
      imgURL: 'http://example.com/husky.jpg'
    };

    component.petForm.setValue(expectedPet);
    component.onSave();

    expect(component.savePet.emit).toHaveBeenCalledWith(expectedPet);
    expect(component.display).toBeFalse();
  });

  it('should not emit savePet if form is invalid', () => {
    spyOn(component.savePet, 'emit');

    component.petForm.setValue({
      species: '',
      breed: '',
      name: '',
      location: '',
      age: null,
      description: '',
      imgURL: ''
    });

    component.onSave();

    expect(component.savePet.emit).not.toHaveBeenCalled();
  });

  it('should close the dialog when hideDialog is called', () => {
    component.display = true;
    component.hideDialog();
    expect(component.display).toBeFalse();
  });
});
