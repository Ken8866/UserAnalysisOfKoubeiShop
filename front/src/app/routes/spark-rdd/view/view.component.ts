import { Component, OnInit } from '@angular/core';
import { viewday } from 'src/assets/data/view-day';
import { viewweek } from 'src/assets/data/view-weak';
import { viewmonth } from 'src/assets/data/view-mont';

@Component({
  selector: 'spark-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.less']
})
export class ViewComponent implements OnInit {

  width = document.body.clientWidth / 2 - 150;
  option_day = {
    title: {
      show: false
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        animation: false,
        label: {
          backgroundColor: '#505765'
        }
      }
    },
    legend: {
      show: false
    },
    grid: {
      top: '5%',
      left: '5%',
      right: '5%',
      bottom: '8%',
      containLabel: true
    },
    dataset: [
      {
        dimensions: ['x', 'y'],
        source: []
      }
    ],
    xAxis: {
      type: 'time',
      boundaryGap: false
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '浏览次数',
        type: 'line',
        datasetIndex: 0,
        encode: { x: 'x', y: 'y' }
      }
    ]
  };

  option_week = {
    title: {
      show: false
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        animation: false,
        label: {
          backgroundColor: '#505765'
        }
      }
    },
    legend: {
      show: false
    },
    grid: {
      top: '5%',
      left: '5%',
      right: '5%',
      bottom: '8%',
      containLabel: true
    },
    dataset: [
      {
        dimensions: ['x', 'y'],
        source: []
      }
    ],
    xAxis: {
      type: 'category',
      boundaryGap: false
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '浏览次数',
        type: 'bar',
        datasetIndex: 0,
        barWidth: 10,
        encode: { x: 'x', y: 'y' }
      }
    ]
  };

  option_month = {
    title: {
      show: false
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        animation: false,
        label: {
          backgroundColor: '#505765'
        }
      }
    },
    legend: {
      show: false
    },
    grid: {
      top: '5%',
      left: '5%',
      right: '5%',
      bottom: '8%',
      containLabel: true
    },
    dataset: [
      {
        dimensions: ['x', 'y'],
        source: []
      }
    ],
    xAxis: {
      type: 'category',
      boundaryGap: false
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '浏览次数',
        type: 'bar',
        barWidth: 20,
        datasetIndex: 0,
        encode: { x: 'x', y: 'y' }
      }
    ]
  };
  constructor() {
    for (let i = 0; i < 2000; i++)
      this.shops.push({ label: i + 1, value: i + 1 });

    this.selectedValue = this.shops[1196].value;

    this.dateRange = [new Date(2016, 5, 22), new Date(2016, 9, 31)];

    viewday.map(item => {
      this.option_day.dataset[0].source.push({ x: item.timeStr, y: item.viewCount });
    })

    viewweek.map(item => {
      this.option_week.dataset[0].source.push({ x: item.weekOfYear, y: item.viewCount });
    })

    viewmonth.map(item => {
      this.option_month.dataset[0].source.push({ x: item.timeStr, y: item.viewCount });
    })
  }

  dateRange;
  selectedValue

  shops = [];
  selectedItem = 'day';

  ngOnInit() {
  }

  onChange(e) {
    console.log(e);
    console.log(this.dateRange)
  }
  show(tt) {
    this.selectedItem = tt;
  }

  onChartInit(e) {

  }
}
