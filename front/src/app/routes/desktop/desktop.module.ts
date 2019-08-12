import { NgModule } from '@angular/core';
import { SharedModule } from '@shared';
import { NgxEchartsModule } from 'ngx-echarts';

import { DesktopRoutingModule } from './desktop-routing.module';

import { MainComponent } from './main/main.component';
import { ComponentHeaderComponent } from './component-header/component-header.component';
import { CityCostComponent } from './city-cost/city-cost.component';
import { ShopViewComponent } from './shop-view/shop-view.component';
import { TopProdComponent } from './top-prod/top-prod.component';

import {
  TableModule
} from 'primeng/table';

const COMPONENTS = [MainComponent, ComponentHeaderComponent];
const COMPONENTS_NOROUNT = [];

@NgModule({
  imports: [SharedModule, DesktopRoutingModule, NgxEchartsModule, TableModule],
  declarations: [...COMPONENTS, ...COMPONENTS_NOROUNT, CityCostComponent, ShopViewComponent, TopProdComponent],
  entryComponents: COMPONENTS_NOROUNT,
})
export class DesktopModule { }
