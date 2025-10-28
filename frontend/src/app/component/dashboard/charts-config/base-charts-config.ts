import {
  ApexChart,
  ApexDataLabels,
  ApexFill,
  ApexLegend,
  ApexNonAxisChartSeries,
  ApexPlotOptions,
  ApexResponsive,
  ApexStroke,
  ApexTheme,
  ApexTitleSubtitle,
  ApexTooltip,
  ApexXAxis,
  ApexYAxis,
} from "ng-apexcharts";

export type ChartOptions = {
  series: ApexAxisChartSeries | ApexNonAxisChartSeries;
  chart: ApexChart,
  plotOptions: ApexPlotOptions,
  dataLabels: ApexDataLabels,
  xaxis: ApexXAxis,
  yaxis: ApexYAxis,
  fill: ApexFill,
  tooltip: ApexTooltip,
  title: ApexTitleSubtitle,
  theme: ApexTheme,
  grid: ApexGrid,
  labels: string[],
  legend: ApexLegend,
  stroke: ApexStroke,
  responsive: ApexResponsive[],
  colors: string[]
};

//Base configurations for charts
export const baseBarConfig: Partial<ChartOptions> =
  {
    chart: {
      height: 300,
      type: "bar",
      background: "transparent",
      toolbar: { show: false },
      foreColor: "#fff"
    },
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: "20%",
        borderRadius: 6,
        dataLabels: { position: "top" }
      }
    },
    dataLabels: {
      enabled: true,
      formatter: (val) => val,
      style: {
        colors: ["#fff"]
      },
      offsetY:-20
    },
    xaxis: {
      categories: [],
      labels: {
        show: false,
        style: { colors: "#fff" }
      },
      axisBorder: { show: false },
      axisTicks: { show: false }
    },
    yaxis: {
      show: false,
    },
    fill: {
      type: "gradient",
      gradient: {
        shade: "light",
        type: "vertical",
        shadeIntensity: 0.3,
        gradientToColors: ["#00BFFF"],
        inverseColors: false,
        opacityFrom: 0.8,
        opacityTo: 0.9,
        stops: [0, 100]
      }
    },
    tooltip: {
      enabled: true,
      theme: "dark",
      y: {
        formatter: (val) => val + "%"
      }
    },
    title: {
      text: undefined
    },
    theme: {
      mode: "dark"
    },
    grid: {
      show: false
    },
  }

export const baseDonutConfig: Partial<ChartOptions> =
  {
    chart: {
      type: "donut",
      height: 300,
      background: "transparent",
      foreColor: "#fff"
    },
    labels: ["Team A", "Team B", "Team C", "Team D", "Team E"],
    colors: ["#1E90FF", "#00CED1", "#00BFFF", "#4682B4", "#87CEFA"],
    legend: {
      position: "right",
      labels: { colors: "#fff" },

    },
    stroke: {
      width: 0
    },
    dataLabels: {
      enabled: true,
      formatter: (val, opts) => opts.w.globals.labels[opts.seriesIndex] + ": " + val + "%",
      style: { colors: ["#fff"] }
    },
    responsive: [
      {
        breakpoint: 480,
        options: {
          chart: { width: 200 },
          legend: { position: "bottom" }
        }
      }
    ]
    ,
    tooltip: {
      enabled: true,
      theme: "dark",
      y: { formatter: (val) => val + "%" }
    },
    theme: {
      mode: "dark"
    }
  }
