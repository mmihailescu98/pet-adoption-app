import { Injectable } from '@angular/core';

/*This service is used to search for locations from an address using Google Maps APIs */
@Injectable({ providedIn: 'root' })
export class MapService {

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
