import { Component, OnInit } from '@angular/core';
import { CateData } from 'src/assets/data/cate-name';
import { topspark } from 'src/assets/data/top-spark';

@Component({
  selector: 'sk-top',
  templateUrl: './top.component.html',
  styleUrls: ['./top.component.less']
})
export class TopComponent implements OnInit {
  rankingListData = [];
  option = {
    title: {
      text: '商店人员消费',
      show: false
    },
    tooltip: {
      trigger: 'axis',
      x: 'center'
    },
    legend: {
      data: ['平均日交易额'],
      x: 'right'
    },
    grid: {
      left: '15%',
      right: '10%',
      bottom: '10%',
      top: '15%'
    },
    xAxis: [
      {
        type: 'value'
      }
    ],
    yAxis: [
      {
        title: '商店ID',
        type: 'category',
        data: []
      }
    ],
    series: [
      {
        name: '平均日交易额',
        type: 'bar',
        barWidth: 10,
        data: [],
      }
    ]
  };
  constructor() {

  }

  ngOnInit() {
    topspark.map(item => {
      this.rankingListData.push({
        title: item.shopId,
        total: item.avgPerDayPayCount
      })
      this.option.yAxis[0].data.push(item.shopId);
      this.option.series[0].data.push(item.avgPerDayPayCount);
    })
  }

}
