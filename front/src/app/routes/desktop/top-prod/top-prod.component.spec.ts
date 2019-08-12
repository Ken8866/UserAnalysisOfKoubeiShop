import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TopProdComponent } from './top-prod.component';

describe('TopProdComponent', () => {
  let component: TopProdComponent;
  let fixture: ComponentFixture<TopProdComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TopProdComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TopProdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
