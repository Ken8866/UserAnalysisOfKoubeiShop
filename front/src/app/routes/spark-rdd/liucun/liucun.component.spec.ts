import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LiucunComponent } from './liucun.component';

describe('LiucunComponent', () => {
  let component: LiucunComponent;
  let fixture: ComponentFixture<LiucunComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LiucunComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LiucunComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
