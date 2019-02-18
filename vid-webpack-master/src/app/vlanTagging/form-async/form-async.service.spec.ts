import {async, getTestBed, TestBed} from '@angular/core/testing';
import {FormAsyncService} from "./form-async.service";

describe('FormAsyncService', () => {

  let injector;
  let service: FormAsyncService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers: [FormAsyncService]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(FormAsyncService);

  })().then(done).catch(done.fail));

  test('should add cloudOwner according to new tenant value', () => {
    const tenants = [{'id': '1', 'name': 'firstTenant', 'isPermitted': true, cloudOwner: 'irma-aic'},
      {'id': '2', 'name': 'secondTenant', 'isPermitted': true, cloudOwner: 'irma-aic2'}];
    let cloudOwner: string;
    cloudOwner = service.onTenantSelect(tenants,'1');
    expect(cloudOwner).toEqual('irma-aic');

    cloudOwner = service.onTenantSelect(tenants, '2');
    expect(cloudOwner).toEqual('irma-aic2');
  })

});
