import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'sk-like',
  templateUrl: './like.component.html',
  styleUrls: ['./like.component.less']
})
export class LikeComponent implements OnInit {

  datas = [];
  datas1 = [];

  naicha = [{ "shopId": "135", "popularScore": "2.66", "cityName": "上海", "rank": 1 },
  { "shopId": "1193", "popularScore": "2.66", "cityName": "上海", "rank": 2 },
  { "shopId": "800", "popularScore": "2.36", "cityName": "上海", "rank": 3 },
  { "shopId": "910", "popularScore": "2.36", "cityName": "上海", "rank": 4 },
  { "shopId": "1241", "popularScore": "2.36", "cityName": "上海", "rank": 5 },
  { "shopId": "215", "popularScore": "1.62", "cityName": "北京", "rank": 1 },
  { "shopId": "390", "popularScore": "1.62", "cityName": "北京", "rank": 2 },
  { "shopId": "796", "popularScore": "3.12", "cityName": "广州", "rank": 1 },
  { "shopId": "913", "popularScore": "2.82", "cityName": "广州", "rank": 2 },
  { "shopId": "1828", "popularScore": "1.92", "cityName": "广州", "rank": 3 },
  { "shopId": "244", "popularScore": "1.02", "cityName": "广州", "rank": 4 },
  { "shopId": "752", "popularScore": "5.06", "cityName": "深圳", "rank": 1 },
  { "shopId": "1182", "popularScore": "2.82", "cityName": "深圳", "rank": 2 }]

  zhongcan =
    [{ "shopId": "761", "popularScore": "4.20", "cityName": "深圳", "rank": 1 },
    { "shopId": "1781", "popularScore": "4.18", "cityName": "深圳", "rank": 2 },
    { "shopId": "1795", "popularScore": "3.28", "cityName": "深圳", "rank": 3 },
    { "shopId": "289", "popularScore": "3.12", "cityName": "深圳", "rank": 4 },
    { "shopId": "1701", "popularScore": "3.12", "cityName": "深圳", "rank": 5 },
    { "shopId": "824", "popularScore": "5.82", "cityName": "上海", "rank": 1 },
    { "shopId": "689", "popularScore": "5.68", "cityName": "上海", "rank": 2 },
    { "shopId": "982", "popularScore": "5.38", "cityName": "上海", "rank": 3 },
    { "shopId": "1683", "popularScore": "5.38", "cityName": "上海", "rank": 4 },
    { "shopId": "895", "popularScore": "5.22", "cityName": "上海", "rank": 5 },
    { "shopId": "238", "popularScore": "6.28", "cityName": "北京", "rank": 1 },
    { "shopId": "1533", "popularScore": "5.82", "cityName": "北京", "rank": 2 },
    { "shopId": "1371", "popularScore": "5.66", "cityName": "北京", "rank": 3 },
    { "shopId": "623", "popularScore": "5.22", "cityName": "北京", "rank": 4 },
    { "shopId": "1727", "popularScore": "4.02", "cityName": "北京", "rank": 5 },
    { "shopId": "495", "popularScore": "5.10", "cityName": "广州", "rank": 1 },
    { "shopId": "1600", "popularScore": "4.94", "cityName": "广州", "rank": 2 },
    { "shopId": "1655", "popularScore": "4.48", "cityName": "广州", "rank": 3 },
    { "shopId": "98", "popularScore": "3.74", "cityName": "广州", "rank": 4 },
    { "shopId": "133", "popularScore": "3.28", "cityName": "广州", "rank": 5 }]

  types = [{ label: '奶茶店', value: 'nc' }, { label: '中式快餐', value: 'kc' }]

  selectedType
  constructor() {
    this.selectedType = this.types[0];
  }

  ngOnInit() {
    this.datas = [
      { title: "北京", color: "#1890ff", items: [] },
      { title: "上海", color: "#52c41a", items: [] },
      { title: "广州", color: "#fa8c16", items: [] },
      { title: "深圳", color: "#eb2f96", items: [] }
    ]
    this.getData(this.naicha, this.datas);
    this.datas1 = [
      { title: "北京", color: "#1890ff", items: [] },
      { title: "上海", color: "#52c41a", items: [] },
      { title: "广州", color: "#fa8c16", items: [] },
      { title: "深圳", color: "#eb2f96", items: [] }
    ]
    this.getData(this.zhongcan, this.datas1);
  }

  getData(ary, data) {
    data.map(item => {
      item.items = [];
      ary.map(res => {
        if (item.title === res.cityName) {
          item.items.push({
            shop_id: res.shopId,
            score: res.popularScore
          })
        }
      })
    })
  }

  onChangeYcType() {

  }

}
