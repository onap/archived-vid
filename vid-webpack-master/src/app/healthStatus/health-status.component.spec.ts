import {TestBed, ComponentFixture, async} from '@angular/core/testing';
import {HealthStatusComponent} from "./health-status.component";
import {HealthStatusService} from "../shared/server/healthStatusService/health-status.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ExternalComponentStatus} from "../shared/models/externalComponentStatus";
import {Observable} from "rxjs";
import {of} from "rxjs";

export class MockHealthStatusService {


  getProbe(): Observable<Array<ExternalComponentStatus>> {
    return of(new Array<ExternalComponentStatus>(
      new ExternalComponentStatus("x", true, {y:"r"})));
  }
}

describe('HealthStatusComponent', () => {
  let component: HealthStatusComponent;
  let fixture: ComponentFixture<HealthStatusComponent>;
  let mockHealthStatusService : MockHealthStatusService;

  mockHealthStatusService =  new MockHealthStatusService();

  beforeAll(done => (async () => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{provide: HealthStatusService, useValue: mockHealthStatusService}],
      declarations: [HealthStatusComponent]
    });
    await TestBed.compileComponents();

    fixture = TestBed.createComponent(HealthStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  })().then(done).catch(done.fail));

  test('isAvailable taken from component status available field', () => {
    expect(component.isAvailable(new ExternalComponentStatus("a", true, null))).toBeTruthy();
    expect(component.isAvailable(new ExternalComponentStatus("a", false, null))).toBeFalsy();
  });

  test('getMetadata filter rawData ', () => {
    let metadata:string = JSON.stringify(component.getMetadata(new ExternalComponentStatus("a", true, {a:1, rawData:2})));
    expect(metadata).toContain("1");
    expect(metadata.indexOf("2")).toEqual(-1);
  });

  test('componentStatus is initialized on startup ', () => {
    expect(JSON.stringify(component.componentStatuses[0].metadata)).toContain("y");
  });

  test('when refresh componentStatus is updated', () => {
    spyOn(mockHealthStatusService, "getProbe" ).and.returnValue(
      of(new Array<ExternalComponentStatus>(
      new ExternalComponentStatus("mySpecialValue", true, {y:"z"}))));
    component.refreshData();
    expect(component.componentStatuses[0].component).toEqual("mySpecialValue");
    expect(mockHealthStatusService.getProbe).toHaveBeenCalledTimes(1);

  });

});
