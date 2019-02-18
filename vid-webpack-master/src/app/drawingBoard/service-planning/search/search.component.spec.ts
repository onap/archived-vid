import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {SearchComponent} from "./search.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

describe('Spinner component', () => {

  let component: SearchComponent;
  let fixture: ComponentFixture<SearchComponent>;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule, HttpClientTestingModule],
      providers: [],
      declarations: [SearchComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(SearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

  })().then(done).catch(done.fail));


  test('component should be defined', () => {
    expect(component).toBeDefined();
  });

  test('searchTree should return all nodes that include some text: with text', () => {
    component.nodes = [
      {
        name: 'name_1'
      },
      {
        name: 'name_2'
      },
      {
        name: 'name_3'
      },
      {
        name: 'name_3'
      }];
    jest.spyOn(component.updateNodes, 'emit');
    spyOn(component, 'expandParentByNodeId').and.stub();
    component.searchTree('name_1');

    expect(component.updateNodes.emit).toHaveBeenCalledWith({
      nodes: [
        {
          name: 'name_1'
        },
        {
          name: 'name_2'
        },
        {
          name: 'name_3'
        },
        {
          name: 'name_3'
        }],
      filterValue: 'name_1'
    });
  });

  test('searchTree should return all nodes that include some text: without text', () => {
    component.nodes = [
      {
        name: 'name_1',
        children: [
          {
            name: 'name_child_1'
          }
        ]
      },
      {
        name: 'name_2'
      },
      {
        name: 'name_3'
      },
      {
        name: 'name_4'
      }];
    jest.spyOn(component.updateNodes, 'emit');
    spyOn(component, 'expandParentByNodeId').and.stub();
    component.searchTree('');

    expect(component.updateNodes.emit).toHaveBeenCalled();
  });
});
