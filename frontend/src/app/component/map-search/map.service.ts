import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment.development';

/*This service is used to search for locations from an address using Google Maps APIs */
@Injectable({ providedIn: 'root' })
export class MapService {
  private loading: Promise<void> | null = null;

  load(): Promise<void> {
    if (typeof window !== 'undefined' && (window as any).google?.maps) {
      return Promise.resolve();
    }
    if (this.loading) return this.loading;

    this.loading = new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${environment.googleMapsApiKey}&libraries=places`;
      script.async = true;
      script.defer = true;
      script.onload = () => resolve();
      script.onerror = (err) => reject(err);
      document.head.appendChild(script);
    });

    return this.loading;
  }
  /*Find locations*/
  geocodeAddress(address: string): Promise<google.maps.GeocoderResult[]> {
    const geocoder = new google.maps.Geocoder();

    return new Promise((resolve, reject) => {
      geocoder.geocode({ address }, (results, status) => {
        if (status === 'OK' && results) resolve(results);
        else reject(status);
      });
    });
  }
}
