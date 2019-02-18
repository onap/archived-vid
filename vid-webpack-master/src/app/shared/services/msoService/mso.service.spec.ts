import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {MsoService} from './mso.service';
import {Constants} from "../../utils/constants";


describe('Mso Service', () => {
  let injector;
  let service: MsoService;
  let httpMock: HttpTestingController;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MsoService]
    });
    await TestBed.compileComponents();


    injector = getTestBed();
    service = injector.get(MsoService);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));

  describe('#instantiation status tests ', ()=> {
    test('retry should send the right request', ()=>{
      const jobId: string = '111';

      service.retryMsoTask(jobId).subscribe();
      const req = httpMock.expectOne(Constants.Path.SERVICES_JOB_INFO_PATH  + '/retry/' + jobId);

      expect(req.request.method).toBe('POST');
    });
  });

});
