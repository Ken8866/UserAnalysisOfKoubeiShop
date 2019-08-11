import { Component, OnInit } from '@angular/core';
import { interval } from 'rxjs';

@Component({
  selector: 'ss-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.less']
})
export class ShopComponent implements OnInit {

  option = {
    title: { show: false },
    lengend: { show: false },
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
    // dataZoom: [
    //   {
    //     show: true,
    //     realtime: true,
    //     start: 65,
    //     end: 85
    //   },
    //   {
    //     type: 'inside',
    //     realtime: true,
    //     start: 65,
    //     end: 85
    //   }
    // ],
    legend: {
      show: false
    },
    grid: {
      top: '5%',
      left: '2%',
      right: '2%',
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
      type: 'value',
      title: '浏览次数'
    },
    series: [
      {
        name: '浏览次数',
        type: 'bar',
        // barWidth: 5,
        datasetIndex: 0,
        encode: { x: 'x', y: 'y' }
      }
    ]
  }
  echartIntance;

  constructor() {
    const cc = interval(2000);
    cc.subscribe(res => {
      for (let i = 0; i < 2000; i++) {
        this.option.dataset[0].source[i].y += Math.ceil(Math.random() * 10)
      }
      this.echartIntance.setOption(this.option);
    })
  }

  ngOnInit() {
    for (let i = 0; i < 2000; i++) {
      this.option.dataset[0].source.push({
        x: i + 1, y: Math.ceil(Math.random() * 100)
      })
    }
  }

  onChartInit(ec) {
    this.echartIntance = ec;
  }

}
