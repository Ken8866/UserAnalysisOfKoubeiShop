import { Component, Input, } from '@angular/core';

@Component({
  selector: 'common-component-loader',
  templateUrl: './component-loader.component.html',
  styleUrls: ['./component-loader.component.css']
})
export class ComponentLoaderComponent {
  @Input() load: boolean = true
  @Input() top: string = '50%'
  @Input() left: string = '45%'
}
