import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxEchartsModule } from 'ngx-echarts';
import { MainComponent } from './main/main.component';
import { SparkStreamRoutingModule } from './spark-stream-routing.module';
import { ShopComponent } from './shop/shop.component';

import { CityComponent } from './city/city.component';
import { CommonComponentModule } from '../common-component/common.module';



@NgModule({
  declarations: [MainComponent, ShopComponent, CityComponent],
  imports: [
    CommonModule, NgxEchartsModule, SparkStreamRoutingModule, CommonComponentModule
  ]
})
export class SparkStreamModule { }
