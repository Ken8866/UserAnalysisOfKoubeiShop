import { Component, OnInit } from '@angular/core';
import { CateData } from 'src/assets/data/cate-name';

@Component({
  selector: 'app-top-prod',
  templateUrl: './top-prod.component.html',
  styleUrls: ['./top-prod.component.less']
})
export class TopProdComponent implements OnInit {
  option = {
    title: {
      text: '商店人员消费'
    },
    tooltip: {
      trigger: 'axis',
      x: 'center'
    },
    legend: {
      data: ['人均消费', '用户总付费次数'],
      x: 'right'
    },
    grid: {
      left: '5%',
      right: '10%',
      bottom: '10%',
      top: '15%'
    },
    xAxis: [
      {
        type: 'category',
        data: []
      }
    ],
    yAxis: [
      {
        type: 'value'
      },
      {
        type: 'value',
        postion: 'right'
      }
    ],
    series: [
      {
        name: '人均消费',
        type: 'bar',
        barWidth: 30,
        data: [],
      },
      {
        name: '用户总付费次数',
        type: 'line',
        yAxisIndex: 1,
        data: []
      }
    ]
  };
  rankingListData = [];
  constructor() {
    CateData.map(item => {
      this.rankingListData.push({
        title: item.shop_id + "-" + item.cate_name,
        total: item.pay_c
      })
      this.option.xAxis[0].data.push(item.shop_id);
      this.option.series[0].data.push(item.per_pay);
      this.option.series[1].data.push(item.pay_c);
    })
  }

  ngOnInit() {
  }

  onChartInit(ec) {

  }
}
