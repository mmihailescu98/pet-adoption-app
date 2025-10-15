import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapSearch } from './map-search';

describe('MapSearch', () => {
  let component: MapSearch;
  let fixture: ComponentFixture<MapSearch>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MapSearch]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MapSearch);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
