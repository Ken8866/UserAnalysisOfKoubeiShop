import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'spark-liucun',
  templateUrl: './liucun.component.html',
  styleUrls: ['./liucun.component.less']
})
export class LiucunComponent implements OnInit {

  cols = [{ header: '日期', field: 'date' }];
  datas;

  constructor() {
    for (let i = 0; i < 30; i++) {
      this.cols.push({ header: '第' + (i + 1) + '天', field: 'label' + i });
    }

  }

  ngOnInit() {
  }

}
