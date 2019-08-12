import { Component, OnInit } from '@angular/core';
import { ViewData } from 'src/assets/data/shop-view';
import { PayData } from 'src/assets/data/shop-pay';
@Component({
  selector: 'app-shop-view',
  templateUrl: './shop-view.component.html',
  styleUrls: ['./shop-view.component.less']
})
export class ShopViewComponent implements OnInit {

  selected_date;

  option = {
    title: {
      text: '商家交易',
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
    dataZoom: [
      {
        show: true,
        realtime: true,
        start: 65,
        end: 85
      },
      {
        type: 'inside',
        realtime: true,
        start: 65,
        end: 85
      }
    ],
    legend: {
      data: ['交易次数', '浏览次数']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    dataset: [
      {
        dimensions: ['x', 'y'],
        source: []
      }, {
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
        name: '交易次数',
        type: 'line',
        datasetIndex: 0,
        encode: { x: 'x', y: 'y' }
      },
      {
        name: '浏览次数',
        type: 'line',
        datasetIndex: 1,
        encode: { x: 'x', y: 'y' }
      }
    ]
  };

  constructor() {
    PayData.map(item => {
      this.option.dataset[1].source.push({
        x: item.time, y: item.count
      })
    })
    ViewData.map(item => {
      this.option.dataset[0].source.push({
        x: item.time, y: item.count
      })
    })

  }

  ngOnInit() {
  }

  onChartInit(ec) {

  }

}
