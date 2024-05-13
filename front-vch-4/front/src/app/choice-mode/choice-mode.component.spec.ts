import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChoiceModeComponent } from './choice-mode.component';

describe('ChoiceModeComponent', () => {
  let component: ChoiceModeComponent;
  let fixture: ComponentFixture<ChoiceModeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChoiceModeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ChoiceModeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
