import {TestBed, getTestBed} from '@angular/core/testing';
import {
  HttpClientTestingModule,
} from '@angular/common/http/testing';
import {TypeNodeInformation} from "./typeNodeInformation.model";

describe('Available Models Tree Service', () => {
  let injector;
  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: []
    }).compileComponents();

    injector = getTestBed();
  });


  test('TypeNodeInformation VNF', () => {
    let type: TypeNodeInformation = new TypeNodeInformation(<any>{
      data: {
        type: 'VF'
      }
    });
    expect(type.hierarchyName).toEqual('vnfs');
    expect(type.existingMappingCounterName).toEqual('existingVNFCounterMap');
  });

  test('TypeNodeInformation VNF group', () => {
    let type: TypeNodeInformation = new TypeNodeInformation(<any>{
      data: {
        type: 'VnfGroup'
      }
    });
    expect(type.hierarchyName).toEqual('vnfGroups');
    expect(type.existingMappingCounterName).toEqual('existingVnfGroupCounterMap');
  });

  test('TypeNodeInformation VL', () => {
    let type: TypeNodeInformation = new TypeNodeInformation(<any>{
      data: {
        type: 'VL'
      }
    });
    expect(type.hierarchyName).toEqual('networks');
    expect(type.existingMappingCounterName).toEqual('existingNetworksCounterMap');
  });

  test('TypeNodeInformation Network', () => {
    let type: TypeNodeInformation = new TypeNodeInformation(<any>{
      data: {
        type: 'Network'
      }
    });
    expect(type.hierarchyName).toEqual('networks');
    expect(type.existingMappingCounterName).toEqual('existingNetworksCounterMap');
  });
});
