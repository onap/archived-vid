import {ComponentFixture, TestBed } from '@angular/core/testing';
import { TreeNodeHeaderPropertiesComponent } from './tree-node-header-properties.component';

describe('TreeNodeHeaderPropertiesComponent', () => {
  let component: TreeNodeHeaderPropertiesComponent;
  let fixture: ComponentFixture<TreeNodeHeaderPropertiesComponent>;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      declarations: [ TreeNodeHeaderPropertiesComponent ]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(TreeNodeHeaderPropertiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

  })().then(done).catch(done.fail));


  test('should create', () => {
    expect(component).toBeTruthy();
  });
});
