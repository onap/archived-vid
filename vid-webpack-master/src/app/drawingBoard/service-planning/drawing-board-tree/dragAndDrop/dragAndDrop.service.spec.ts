import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NgRedux} from "@angular-redux/store";
import {DragAndDropService} from "./dragAndDrop.service";
import {AppState} from "../../../../shared/store/reducers";

class MockAppStore<T> {
  dispatch() {

  }

  getState() {
    return {
      global: {
        flags: {
          "FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE": true,
          "FLAG_2008_DISABLE_DRAG_FOR_BASE_MODULE":true
        }
      },
      service: {
        serviceHierarchy:{
          "serviceInstanceId":{
            vfModules: {
              "vfModulesName":{
                properties:{
                  baseModule:true
                }
              }
            }
          }
        },
        serviceInstance: {
          "serviceInstanceId": {
            vnfs: {
              "vnfStoreKey": {
                isMissingData: true,
                vfModules: {
                  "vfModulesName": {
                    "vfModulesName": {
                      isMissingData: true
                    }
                  }
                }
              },

              "vnfStoreKey1": {
                isMissingData: false,
                vfModules: {
                  "vfModulesName": {
                    "vfModulesName": {
                      isMissingData: false
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

describe('Drag and drop service', () => {
  let injector;
  let service: DragAndDropService;
  let httpMock: HttpTestingController;
  let store: NgRedux<AppState>;
  let nodes;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        DragAndDropService,
        {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(DragAndDropService);
    httpMock = injector.get(HttpTestingController);
    store = injector.get(NgRedux);


  })().then(done).catch(done.fail));

  beforeEach(() => {
    nodes = [
      {
        "trackById": "ckfqe3sb3y8",
        "componentInfoType": "VNF",
        "parentType": "",
        "type": "VF",
        "typeName": "VNF",
        "instanceName": "2017-488_PASQUALE-vPE",
        "id": "04686zg11ur2",
        "children": [
          {
            "id": "1150884479608",
            "action": "Create",
            "instanceName": "puwesovabe",
            "name": "puwesovabe",
            "type": "VFmodule",
            "trackById": "d5if1906rqa",
            "parentType": "VNF",
            "position": 1,
            "componentInfoType": "VFModule",
            "errors": {},
            "updatePoistionFunction": () => {
            },
          },
          {
            "id": "4637423092446",
            "action": "Create",
            "instanceName": "bnmgtrx",
            "name": "bnmgtrx",
            "type": "VFmodule",
            "trackById": "9ei9adlh27e",
            "parentType": "VNF",
            "position": 2,
            "componentInfoType": "VFModule",
            "updatePoistionFunction": () => {
            }
          }
        ],
        "errors": {},
      }
    ];
  })
  test('drag should execute array_move when the nodes parent are same', () => {

    let from = {
      id: "04686zg11ur2",
      index: 0,
      data: {
        instanceName: 'puwesovabe',
        modelName: 'vocggrapitesting0..VocgGrapiTesting..ocgmgr..module-1'
      },
      parent: {
        data: {
          type: 'VF',
          index: 0,
          trackById: 'ckfqe3sb3y8',
          vnfStoreKey: '2017-488_PASQUALE-vPE 0',
        }
      }
    };

    let to = {
      parent: {
        id: "4637423092446",
        index: 1,
        data: {
          instanceName: 'bnmgtrx',
        },
        parent: {
          data: {
            type: 'VF',
            trackById: 'ckfqe3sb3y8',
            vnfStoreKey: '2017-488_PASQUALE-vPE 0',
          }
        }
      }
    };

    jest.spyOn(service, 'array_move');

    service.drop(store, "serviceInstanceId", nodes, {from, to});

    expect(service.array_move).toHaveBeenCalled();

  });


  test('drag shouldnt execute array_move when the nodes parent are different', () => {

    let from = {
      id: 1150884479608,
      index: 0,
      data: {
        instanceName: '2017-488_PASQUALE-vPE',
        modelName: 'vocggrapitesting0..VocgGrapiTesting..ocgmgr..module-1'
      },
      parent: {}
    };

    let to = {
      parent: {
        id: 4637423092446,
        index: 1,
        data: {
          instanceName: 'bnmgtrx',
        },
        parent: {
          data: {
            type: 'VF',
            trackById: '1111',
            vnfStoreKey: '2017-488_PASQUALE-vPE 0',
          }
        }
      }
    };


    jest.spyOn(service, 'array_move');

    service.drop(store, "serviceInstanceId", nodes, {from, to});

    jest.clearAllMocks();

    expect(service.array_move).not.toHaveBeenCalled();

  });

  test('drop should change nodes index and position', () => {

    let arr: Array<any> = service.array_move(nodes[0].children, 0, 1, "serviceInstanceId", '')

    expect(arr[0]).toMatchObject({instanceName: "bnmgtrx", position: 1});
    expect(arr[1]).toMatchObject({instanceName: "puwesovabe", position: 2});

  });

  test('drag shouldnt execute isAllowDrop when the to index is 0 & the drop node is a base module', () => {

    let from = {
      id: "04686zg11ur2",
      index: 1,
      data: {
        instanceName: 'puwesovabe',
        modelName: 'vocggrapitesting0..VocgGrapiTesting..ocgmgr..module-1'
      },
      parent: {
        data: {
          type: 'VF',
          index: 0,
          trackById: 'ckfqe3sb3y8',
          vnfStoreKey: '2017-488_PASQUALE-vPE 0',
        }
      }
    };

    let to = {
      parent: {
        id: "4637423092446",
        index: 0,
        data: {
          instanceName: 'bnmgtrx',
        },
        parent: {
          data: {
            type: 'VF',
            trackById: 'ckfqe3sb3y8',
            vnfStoreKey: '2017-488_PASQUALE-vPE 0',
          }
        }
      }
    };


    jest.spyOn(service, 'isAllowDrop');

    service.drop(store, "serviceInstanceId", nodes, {from, to});

    jest.clearAllMocks();

    expect(service.isAllowDrop).not.toHaveBeenCalled();

  });

  test('drag shouldnt execute isAllowDrop when the from node is base module', () => {

    let from = {
      id: "04686zg11ur2",
      index: 0,
      data: {
        instanceName: 'puwesovabe',
        modelName: 'vocggrapitesting0..VocgGrapiTesting..ocgmgr..module-1'
      },
      parent: {
        data: {
          type: 'VF',
          index: 0,
          trackById: 'ckfqe3sb3y8',
          vnfStoreKey: '2017-488_PASQUALE-vPE 0',
        }
      }
    };

    let to = {
      parent: {
        id: "4637423092446",
        index: 1,
        data: {
          instanceName: 'bnmgtrx',
        },
        parent: {
          data: {
            type: 'VF',
            trackById: 'ckfqe3sb3y8',
            vnfStoreKey: '2017-488_PASQUALE-vPE 0',
          }
        }
      }
    };


    jest.spyOn(service, 'isAllowDrop');

    service.drop(store, "serviceInstanceId", nodes, {from, to});

    jest.clearAllMocks();

    expect(service.isAllowDrop).not.toHaveBeenCalled();

  });


});
