import { Component } from '@angular/core';
import {MapService} from './map.service';
import {GoogleMap, MapAdvancedMarker} from '@angular/google-maps';
import {FloatLabel} from 'primeng/floatlabel';
import {Button} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-map-search',
  imports: [
    GoogleMap,
    MapAdvancedMarker,
    FloatLabel,
    FormsModule,
    Button,
    InputTextModule
  ],
  templateUrl: './map-search.html',
  styleUrl: './map-search.css'
})
export class MapSearch {
//Map Options
  center: google.maps.LatLngLiteral = {lat: 46, lng: 25};
  zoom = 6;
  display: google.maps.LatLngLiteral | undefined;
  options: google.maps.MapOptions = {
    fullscreenControl: false,
    keyboardShortcuts: false,
    streetViewControl: false,
  }

  //Marker Options
  value: string = "";
  position: google.maps.LatLngLiteral | null = null;
  markerOptions: google.maps.marker.AdvancedMarkerElementOptions = {};

  //Events
  moveMap(event: google.maps.MapMouseEvent) {
    if(event.latLng)
      this.center = (event.latLng.toJSON());
  }

  move(event: google.maps.MapMouseEvent) {
    if(event.latLng)
      this.display = event.latLng.toJSON();
  }

  //Location search
  constructor(private mapService: MapService) {}

  async searchLocation() {
    if (!this.value) return;

    try {
      const results = await this.mapService.geocodeAddress(this.value);
      const loc = results[0].geometry.location;

      this.center = { lat: loc.lat(), lng: loc.lng() };
      this.position = this.center;
      this.zoom = 14;
      console.log(results[0].address_components);
    } catch (err) {
      alert('No location found');
    }
  }
}
