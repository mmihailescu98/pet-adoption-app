import {Component, EventEmitter, Output} from '@angular/core';
import {MapService} from './map.service';
import {GoogleMap, MapAdvancedMarker} from '@angular/google-maps';
import {FloatLabel} from 'primeng/floatlabel';
import {Button} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule} from '@angular/forms';
import {LocationDTO} from '../../api'

@Component({
  selector: 'app-map-search',
  imports: [
    GoogleMap,
    MapAdvancedMarker,
    FormsModule,
    InputTextModule,
    FloatLabel,
    Button
  ],
  templateUrl: './map-search.html',
  styleUrl: './map-search.css'
})
export class MapSearch {
  @Output() locationModified = new EventEmitter<LocationDTO>();

  position: google.maps.LatLngLiteral | null = null;

  location: LocationDTO = {
    state: "",
    city: "",
    street: "",
    latitude: this.position?.lat,
    longitude: this.position?.lng
  };

  //Map Options
  center: google.maps.LatLngLiteral = {lat: 46, lng: 25};
  zoom = 6;
  options: google.maps.MapOptions = {
    fullscreenControl: false,
    keyboardShortcuts: false,
    streetViewControl: false,
  }

  //Marker Options
  markerOptions: google.maps.marker.AdvancedMarkerElementOptions = {
    gmpDraggable: true,
  };

  //Events
  moveMap(event: google.maps.MapMouseEvent) {
    if(event.latLng) {
      this.position = (event.latLng.toJSON());
      this.location.latitude = this.position.lat;
      this.location.longitude = this.position.lng;
    }
  }

  //Location search
  constructor(private mapService: MapService) {}

  markerPos(event: google.maps.MapMouseEvent) {
    if(event.latLng){
      this.position = event.latLng.toJSON();
      this.location.latitude = this.position.lat;
      this.location.longitude = this.position.lng;
    }
  }

  async searchLocation() {
    if (!this.location.state || !this.location.city) return;

    try {
      const results = await this.mapService.geocodeAddress(this.location.state + " " + this.location.city + " " + this.location.street);
      const loc = results[0].geometry.location;

      this.center = { lat: loc.lat(), lng: loc.lng() };
      this.position = this.center;
      this.location.latitude = this.position.lat;
      this.location.longitude = this.position.lng;
      this.zoom = 14;

      let nr = "";

      for (const comp of results[0].address_components) {
        if (comp.types.includes('country')) {
          this.location.state = comp.long_name;
        } else if (comp.types.includes('locality')) {
          this.location.city = comp.long_name;
        } else if (comp.types.includes('administrative_area_level_1') && !this.location.city) {
          this.location.city = comp.long_name;
        } else if (comp.types.includes('route')) {
          this.location.street = comp.long_name;
        } else if (comp.types.includes('street_number')) {
          nr = comp.long_name;
        }
      }
      this.location.street = nr ? `${this.location.street} ${nr}` : this.location.street;

    } catch (err) {
      alert('No location found');
    }
  }

  //Emit location for parent component
  emitLocation(){
    this.locationModified.emit(this.location);
  }
}
