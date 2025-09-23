import {ApexTheme, NgApexchartsModule} from "ng-apexcharts";
import { Component, ViewChild } from "@angular/core";
import {
  ApexAxisChartSeries,
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexPlotOptions,
  ApexYAxis,
  ApexTitleSubtitle,
  ApexXAxis,
  ApexFill
} from "ng-apexcharts";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  tooltip: ApexTooltip;
  title: ApexTitleSubtitle;
  theme: ApexTheme;
  grid: ApexGrid
};

@Component({
  selector: 'app-dashboard',
  imports: [NgApexchartsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {
  @ViewChild("chart") chart!: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  public chartOptions2: Partial<ChartOptions>;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: "Breed numbers",
          data: [2.3, 3.1, 4.0, 10.1, 4.0, 3.6, 3.2, 2.3, 1.4, 0.8, 0.5, 0.2]
        }
      ],
      chart: {
        height: 350,
        type: "bar"
      },
      plotOptions: {
        bar: {
          dataLabels: {
            position: "top" // top, center, bottom
          }
        }
      },
      dataLabels: {
        enabled: true,
        formatter: function(val) {
          return val + "%";
        },
        offsetY: -20,
        style: {
          fontSize: "12px",
          // colors: ["#304758"]
        }
      },

      xaxis: {
        categories: [
          "Jan",
          "Feb",
          "Mar",
          "Apr",
          "May",
          "Jun",
          "Jul",
          "Aug",
          "Sep",
          "Oct",
          "Nov",
          "Dec"
        ],
        position: "top",
        labels: {
          // offsetY: 10
        },
        axisBorder: {
          show: false
        },
        axisTicks: {
          show: false
        },
        crosshairs: {
          fill: {
            type: "gradient",
            gradient: {
              colorFrom: "#D8E3F0",
              colorTo: "#BED1E6",
              stops: [0, 100],
              opacityFrom: 0.4,
              opacityTo: 0.5
            }
          }
        },
        tooltip: {
          enabled: true,
          offsetY: -35
        }
      },
      fill: {
        type: "gradient",
        gradient: {
          shade: "dark",
          type: "horizontal",
          shadeIntensity: 0.25,
          gradientToColors: undefined,
          inverseColors: true,
          opacityFrom: 1,
          opacityTo: 1,
          stops: [50, 0, 100, 100]
        }
      },
      yaxis: {
        axisBorder: {
          show: false
        },
        axisTicks: {
          show: false
        },
        labels: {
          show: false,
          formatter: function(val) {
            return val + "%";
          }
        }
      },
      title: {
        text: "Monthly Inflation in Argentina, 2002",
        floating: true,
        offsetY: 323,
        align: "center",
        // style: {
        //   color: "#444"
        // }
      },
      theme: {
        mode: "dark"
      }
    };

    this.chartOptions2 = {
      series: [
        {
          name: "Breed numbers",
          data: [2.3, 3.1, 4.0, 10.1, 4.0, 3.6, 3.2, 2.3, 1.4, 0.8, 0.5, 0.2]
        }
      ],
      chart: {
        height: 300,
        type: "bar",
        background: "#1e1e2f", // fundal dark
        toolbar: { show: false },
        foreColor: "#fff" // text și valori
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: "50%",
          borderRadius: 4,
          dataLabels: { position: "top" }
        }
      },
      dataLabels: {
        enabled: true,
        formatter: (val) => val + "%",
        style: {
          colors: ["#fff"]
        }
      },
      xaxis: {
        categories: [
          "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"
        ],
        labels: { style: { colors: "#fff" } },
        axisBorder: { show: false },
        axisTicks: { show: false }
      },
      yaxis: {
        show: false // ascunde axa Y
      },
      fill: {
        type: "gradient",
        gradient: {
          shade: "dark",
          type: "vertical",
          shadeIntensity: 0.5,
          opacityFrom: 0.8,
          opacityTo: 0.2,
          stops: [0, 100]
        }
      },
      tooltip: {
        enabled: false,
        theme: "dark",
        y: {
          formatter: (val) => val + "%"
        }
      },
      title: {
        text: undefined // minimalist, fără titlu
      },
      theme: {
        mode: "dark"
      },
      grid: {
        show: false // elimină liniile grid
      }
    };

  }
}
