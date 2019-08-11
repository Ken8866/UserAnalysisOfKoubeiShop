import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { FormsModule, FormControl, FormGroup } from '@angular/forms';

import { ProgressSpinnerModule } from 'primeng/progressspinner';

import { ComponentLoaderComponent } from './component-load/component-loader.component';
import { ComponentHeaderComponent } from './component-header/component-header.component';

const COMPONENT_NOROUNT = [
  ComponentLoaderComponent,
  ComponentHeaderComponent
];

@NgModule({
  imports: [
    FormsModule,
    SharedModule,
    ProgressSpinnerModule
  ],
  declarations: [
    ...COMPONENT_NOROUNT
  ],
  exports: COMPONENT_NOROUNT,
  entryComponents: COMPONENT_NOROUNT
})
export class CommonComponentModule { }