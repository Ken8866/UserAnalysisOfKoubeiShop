import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-city-cost',
  templateUrl: './city-cost.component.html',
  styleUrls: ['./city-cost.component.css'],
})
export class CityCostComponent implements OnInit {

  data = [
    { label: '梧州', value: 4 },
    { label: '廊坊', value: 4 },
    { label: '淮北', value: 5 },
    { label: '邯郸', value: 7 },
    { label: '保定', value: 10 },
    { label: '龙岩', value: 10 },
    { label: '清远', value: 10 },
    { label: '聊城', value: 11 },
    { label: '汕尾', value: 13 },
    { label: '日照', value: 13 },
    { label: '营口', value: 13 },
    { label: '辽阳', value: 13 },
    { label: '绵阳', value: 13 },
    { label: '柳州', value: 13 },
    { label: '濮阳', value: 14 },
    { label: '丽水', value: 14 },
    { label: '肇庆', value: 14 },
    { label: '铜陵', value: 14 },
    { label: '咸阳', value: 14 },
    { label: '威海', value: 14 },
    { label: '东营', value: 14 },
    { label: '葫芦岛', value: 14 },
    { label: '长治', value: 15 },
    { label: '上饶', value: 15 },
    { label: '玉林', value: 15 },
    { label: '汉中', value: 15 },
    { label: '信阳', value: 15 },
    { label: '石河子', value: 16 },
    { label: '三亚', value: 16 },
    { label: '三明', value: 16 },
    { label: '宿迁', value: 16 },
    { label: '抚顺', value: 16 },
    { label: '荆门', value: 17 },
    { label: '自贡', value: 17 },
    { label: '荆州', value: 18 },
    { label: '通辽', value: 18 },
    { label: '洛阳', value: 18 },
    { label: '宝鸡', value: 18 },
    { label: '阳江', value: 18 },
    { label: '锦州', value: 18 },
    { label: '邢台', value: 18 },
    { label: '张家口', value: 19 },
    { label: '六安', value: 19 },
    { label: '天门', value: 19 },
    { label: '思茅', value: 19 },
    { label: '达州', value: 19 },
    { label: '乐山', value: 19 },
    { label: '安康', value: 19 },
    { label: '德阳', value: 20 },
    { label: '烟台', value: 21 },
    { label: '莆田', value: 22 },
    { label: '中山', value: 26 },
    { label: '潍坊', value: 26 },
    { label: '昆明', value: 27 },
    { label: '济宁', value: 28 },
    { label: '湛江', value: 28 },
    { label: '阜阳', value: 28 },
    { label: '黄山', value: 29 },
    { label: '盐城', value: 29 },
    { label: '镇江', value: 31 },
    { label: '宁德', value: 33 },
    { label: '舟山', value: 33 },
    { label: '十堰', value: 34 },
    { label: '黄冈', value: 35 },
    { label: '咸宁', value: 35 },
    { label: '芜湖', value: 35 },
    { label: '株洲', value: 35 },
    { label: '泰州', value: 37 },
    { label: '徐州', value: 40 },
    { label: '太原', value: 41 },
    { label: '宜昌', value: 41 },
    { label: '漳州', value: 41 },
    { label: '蚌埠', value: 42 },
    { label: '黄石', value: 44 },
    { label: '衢州', value: 45 },
    { label: '江门', value: 49 },
    { label: '贵阳', value: 50 },
    { label: '南平', value: 54 },
    { label: '襄阳', value: 54 },
    { label: '孝感', value: 61 },
    { label: '扬州', value: 69 },
    { label: '珠海', value: 72 },
    { label: '淮安', value: 74 },
    { label: '湖州', value: 79 },
    { label: '石家庄', value: 80 },
    { label: '哈尔滨', value: 97 },
    { label: '惠州', value: 100 },
    { label: '台州', value: 107 },
    { label: '泉州', value: 121 },
    { label: '嘉兴', value: 122 },
    { label: '郑州', value: 134 },
    { label: '南通', value: 138 },
    { label: '南昌', value: 145 },
    { label: '长沙', value: 155 },
    { label: '沈阳', value: 158 },
    { label: '济南', value: 174 },
    { label: '大连', value: 178 },
    { label: '佛山', value: 181 },
    { label: '南宁', value: 183 },
    { label: '青岛', value: 187 },
    { label: '东莞', value: 194 },
    { label: '西安', value: 204 },
    { label: '合肥', value: 225 },
    { label: '天津', value: 233 },
    { label: '常州', value: 234 },
    { label: '重庆', value: 238 },
    { label: '厦门', value: 239 },
    { label: '绍兴', value: 279 },
    { label: '金华', value: 279 },
    { label: '无锡', value: 320 },
    { label: '福州', value: 391 },
    { label: '成都', value: 396 },
    { label: '温州', value: 511 },
    { label: '宁波', value: 564 },
    { label: '苏州', value: 760 },
    { label: '深圳', value: 868 },
    { label: '广州', value: 1143 },
    { label: '武汉', value: 1184 },
    { label: '南京', value: 1278 },
    { label: '北京', value: 1775 },
    { label: '杭州', value: 2094 },
    { label: '上海', value: 3142 },

  ];

  constructor() {

  }

  option = {
    title: {
      show: false,
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)',
    },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      x: 'left',
      data: []
    },
    series: [
      {
        name: '整体消费情况',
        type: 'pie',
        radius: '75%',
        center: ['50%', '55%'],
        data: [

        ],
        label: {
          show: false
        },
        itemStyle: {
          emphasis: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          }
        },
      },
    ],
  };
  echartInstance;

  ngOnInit() {
    this.data.map(item => {
      this.option.legend.data.push(item.label);
      this.option.series[0].data.push({
        value: item.value,
        name: item.label
      })
    })
  }
  onChartInit(ev) {
    this.echartInstance = ev;
  }

  onclick(city) {
    console.log(city);
  }

  onSelectedCyc() { }
}
