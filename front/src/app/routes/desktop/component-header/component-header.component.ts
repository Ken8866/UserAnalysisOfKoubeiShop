import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  // tslint:disable-next-line: component-selector
  selector: 'app-header',
  templateUrl: './component-header.component.html',
  styleUrls: ['./component-header.component.css'],
})
export class ComponentHeaderComponent implements OnInit {
  @Input() header: string;
  @Input() model: string[];
  @Output() onSet = new EventEmitter();
  @Output() onExpand = new EventEmitter();
  set = false;
  expand = false;
  constructor() {}
  onClickSet(e) {
    this.onSet.emit(e);
  }
  onClickExpand(e) {
    this.onExpand.emit(e);
  }
  ngOnInit() {
    if (this.model && this.model.length > 0) {
      this.model.forEach(m => {
        this.set = m === 'set' ? true : false;
        this.expand = m === 'expand' ? true : false;
      });
    }
  }
}
