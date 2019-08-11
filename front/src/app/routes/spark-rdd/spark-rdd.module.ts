import { NgModule } from '@angular/core';
import { SharedModule } from '@shared';
import { NgxEchartsModule } from 'ngx-echarts';


import {
    TableModule
} from 'primeng/table';
import { ToolbarModule } from 'primeng/toolbar';

import { SparkRddRoutingModule } from './spark-rdd-routing.module';
import { MainComponent } from './main/main.component';
import { TopComponent } from './top/top.component';
import { CommonComponentModule } from '../common-component/common.module';
import { LikeComponent } from './like/like/like.component';
import { LiucunComponent } from './liucun/liucun.component';
import { ViewComponent } from './view/view.component';
import { Top50Component } from './top50/top50.component';

const COMPONENTS = [MainComponent, TopComponent, LiucunComponent, ViewComponent, LikeComponent, Top50Component];


@NgModule({
    imports: [SharedModule, SparkRddRoutingModule, NgxEchartsModule, ToolbarModule,
        TableModule, CommonComponentModule],
    declarations: [...COMPONENTS],
    entryComponents: COMPONENTS,
})
export class SparkRddModule { }
