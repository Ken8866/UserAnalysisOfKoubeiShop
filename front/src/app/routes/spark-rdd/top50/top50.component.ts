import { Component, OnInit } from '@angular/core';
import { viewtop } from 'src/assets/data/view-top';
import { interval } from 'rxjs';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'spark-top50',
  templateUrl: './top50.component.html',
  styleUrls: ['./top50.component.less']
})
export class Top50Component implements OnInit {

  columns = [
    { header: '序号', field: 'xh' },
    { header: '商店', field: 'shopId' },
    { header: '浏览次数', field: 'viewCount' },
    { header: '城市', field: 'cityName' },
    { header: '人均消费', field: 'perPay' }
  ]
  datas = [];

  constructor() {
    this.datas = viewtop;
    this.datas.map((item, index) => {
      item.xh = index + 1;
    });

    // const cc = interval(2000)
    // cc.subscribe(res => {
    //   console.log("---------------------")
    // })


  }

  ngOnInit() {
  }

}
