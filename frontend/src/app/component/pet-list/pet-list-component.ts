import { Component } from '@angular/core';
import { DataViewModule } from 'primeng/dataview';
import {Button} from 'primeng/button';

export interface Pet {
  id: number;
  species: string;
  breed: string;
  name: string;
  location: string;
  age: string;
  description: string;
  imgURL?: string;
}

@Component({
  selector: 'pet-list-component',
  imports: [
    DataViewModule,
    Button,
  ],
  templateUrl: './pet-list-component.html',
  styleUrl: './pet-list-component.css'
})
export class PetListComponent {
  values: Pet[] = [
    {
      id: 1,
      species: "Câine",
      breed: "Golden Retriever",
      name: "Max",
      location: "București",
      age: "2 ani",
      description: "Jucăuș, blând și foarte prietenos cu copiii. Ideal pentru o familie activă."
    },
    {
      id: 2,
      species: "Câine",
      breed: "Labrador",
      name: "Bella",
      location: "Cluj-Napoca",
      age: "3 ani",
      description: "Energică și afectuoasă, adoră plimbările lungi și compania oamenilor."
    },
    {
      id: 3,
      species: "Câine",
      breed: "Ciobănesc German",
      name: "Rex",
      location: "Timișoara",
      age: "4 ani",
      description: "Inteligent și protector, dresat de bază, potrivit pentru curte și pază."
    },
    {
      id: 4,
      species: "Câine",
      breed: "Beagle",
      name: "Luna",
      location: "Brașov",
      age: "1 an",
      description: "Curioasă și foarte sociabilă, se înțelege bine cu alți câini și pisici."
    },
    {
      id: 5,
      species: "Câine",
      breed: "Bulldog Francez",
      name: "Rocky",
      location: "Iași",
      age: "5 ani",
      description: "Calm și loial, preferă liniștea și un stil de viață mai relaxat."
    }
  ];
}
