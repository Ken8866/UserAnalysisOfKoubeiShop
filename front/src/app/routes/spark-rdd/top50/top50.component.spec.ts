import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Top50Component } from './top50.component';

describe('Top50Component', () => {
  let component: Top50Component;
  let fixture: ComponentFixture<Top50Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Top50Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Top50Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
