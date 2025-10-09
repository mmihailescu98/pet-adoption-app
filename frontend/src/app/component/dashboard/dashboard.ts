import {
  ChartComponent,
  NgApexchartsModule
} from "ng-apexcharts";
import {Component, effect, inject, ViewChild} from "@angular/core";
import {Card} from 'primeng/card';
import {NavBar} from '../nav-bar/nav-bar';
import {DashboardStore} from '../../store/dashboard/dashboard.store';
import {AdoptionStatsDTO, PercentageStatsDTO} from '../../api';
import {baseBarConfig, baseDonutConfig, ChartOptions} from './charts-config/base-charts-config';

@Component({
  selector: 'app-dashboard',
  imports: [NgApexchartsModule, Card, NavBar],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
  providers: [DashboardStore],
})
export class Dashboard {

  public breedPercentagesData: Partial<ChartOptions>;
  public speciesPercentagesData: Partial<ChartOptions>;

  public breedMAdoptedData: Partial<ChartOptions>;
  public speciesMAdoptedData: Partial<ChartOptions>;

  //Store
  readonly dashboardStore = inject(DashboardStore);

  constructor() {
    this.breedPercentagesData = {
      ...baseDonutConfig,
    };
    this.speciesPercentagesData = {
      ...baseDonutConfig
    };

    this.breedMAdoptedData = {
      ...baseBarConfig
    }

    this.speciesMAdoptedData = {
      ...baseBarConfig
    }

    //Set up effects for charts data

    //Breeds Percentages donut chart
    effect(() => {
      const data = this.dashboardStore.breedsPercentages();

      this.breedPercentagesData.labels = data
        .filter((item): item is Required<Pick<PercentageStatsDTO, "name">> => item.name !== undefined)
        .map(item => item.name);
      this.breedPercentagesData.series = data
        .filter((item): item is Required<Pick<PercentageStatsDTO, "percentage">> => item.percentage !== undefined)
        .map(item => item.percentage);
    });

    //Species Percentages donut chart
    effect(() => {
      const data = this.dashboardStore.speciesPercentages();

      this.speciesPercentagesData.labels = data
        .filter((item): item is Required<Pick<PercentageStatsDTO, "name">> => item.name !== undefined)
        .map(item => item.name);
      this.speciesPercentagesData.series = data
        .filter((item): item is Required<Pick<PercentageStatsDTO, "percentage">> => item.percentage !== undefined)
        .map(item => item.percentage);
    });

    //Most adopted breeds bar chart
    effect(() => {
      const data = this.dashboardStore.mostAdoptedBreeds();

      this.breedMAdoptedData.series = [{
        name: "Most adopted breeds",
        data: data
          .filter((item): item is Required<Pick<AdoptionStatsDTO, "adoptedCount">> => item.adoptedCount !== undefined)
          .map(item => item.adoptedCount)
      }];
      if(this.breedMAdoptedData.xaxis) {
        this.breedMAdoptedData.xaxis.categories = data
          .filter((item): item is Required<Pick<AdoptionStatsDTO, "name">> => item.name !== undefined)
          .map(item => item.name)
      }
    });

    //Most species breeds bar chart
    effect(() => {
      const data = this.dashboardStore.mostAdoptedSpecies();

      this.speciesMAdoptedData.series = [{
        name: "Most adopted breeds",
        data: data
          .filter((item): item is Required<Pick<AdoptionStatsDTO, "adoptedCount">> => item.adoptedCount !== undefined)
          .map(item => item.adoptedCount)
      }];
      if(this.speciesMAdoptedData.xaxis) {
        this.speciesMAdoptedData.xaxis.categories = data
          .filter((item): item is Required<Pick<AdoptionStatsDTO, "name">> => item.name !== undefined)
          .map(item => item.name)
      }
    });
  }

  ngOnInit() {
    this.dashboardStore.loadAll();
  }
}
