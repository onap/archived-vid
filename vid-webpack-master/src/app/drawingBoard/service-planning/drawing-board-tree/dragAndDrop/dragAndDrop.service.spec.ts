import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NgRedux} from "@angular-redux/store";
import {DragAndDropService} from "./dragAndDrop.service";
import {AppState} from "../../../../shared/store/reducers";

class MockAppStore<T> {
  dispatch(){

  }
  getState() {
    return {
      global: {
        flags: {
          "FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE" : true
        }
      },
      service: {
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


  test('drag should move element position', () => {
    let nodes = [{
      "modelCustomizationId": "91415b44-753d-494c-926a-456a9172bbb9",
      "modelId": "d6557200-ecf2-4641-8094-5393ae3aae60",
      "modelUniqueId": "91415b44-753d-494c-926a-456a9172bbb9",
      "missingData": false,
      "id": "tjjongy92jn",
      "action": "Create",
      "inMaint": false,
      "name": "yoav2_001",
      "modelName": "VF_vGeraldine 0",
      "type": "VF",
      "isEcompGeneratedNaming": true,
      "networkStoreKey": "VF_vGeraldine 0:0001",
      "vnfStoreKey": "VF_vGeraldine 0:0001",
      "typeName": "VNF",
      "menuActions": {"edit": {}, "showAuditInfo": {}, "duplicate": {}, "remove": {}, "delete": {}, "undoDelete": {}},
      "isFailed": false,
      "statusProperties": [{"key": "Prov Status:", "testId": "provStatus"}, {
        "key": "Orch Status:",
        "testId": "orchStatus"
      }],
      "trackById": "di9khuolht",
      "parentType": "",
      "position": 0,
      "children": [{
        "modelCustomizationId": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
        "modelId": "a27f5cfc-7f12-4f99-af08-0af9c3885c87",
        "modelUniqueId": "f8c040f1-7e51-4a11-aca8-acf256cfd861",
        "missingData": false,
        "id": 6654971919519,
        "action": "Create",
        "name": "VFModule1",
        "modelName": "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0",
        "type": "VFmodule",
        "isEcompGeneratedNaming": true,
        "dynamicInputs": [],
        "dynamicModelName": "vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0bykqx",
        "typeName": "M",
        "menuActions": {"edit": {}, "showAuditInfo": {}, "remove": {}, "delete": {}, "undoDelete": {}},
        "isFailed": false,
        "statusProperties": [{"key": "Prov Status:", "testId": "provStatus"}, {
          "key": "Orch Status:",
          "testId": "orchStatus"
        }],
        "trackById": "5pfyfah820h",
        "parentType": "VNF",
        "position": 0,
        "errors": {}
      }, {
        "modelCustomizationId": "6add59e0-7fe1-4bc4-af48-f8812422ae7c",
        "modelId": "41708296-e443-4c71-953f-d9a010f059e1",
        "modelUniqueId": "6add59e0-7fe1-4bc4-af48-f8812422ae7c",
        "missingData": false,
        "id": 987761655742,
        "action": "Create",
        "name": "VNFModule3",
        "modelName": "vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2",
        "type": "VFmodule",
        "isEcompGeneratedNaming": true,
        "dynamicInputs": [],
        "dynamicModelName": "vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2fjrrc",
        "typeName": "M",
        "menuActions": {"edit": {}, "showAuditInfo": {}, "remove": {}, "delete": {}, "undoDelete": {}},
        "isFailed": false,
        "statusProperties": [{"key": "Prov Status:", "testId": "provStatus"}, {
          "key": "Orch Status:",
          "testId": "orchStatus"
        }],
        "trackById": "i3dllio31bb",
        "parentType": "VNF",
        "position": 1,
        "errors": {}
      }, {
        "modelCustomizationId": "55b1be94-671a-403e-a26c-667e9c47d091",
        "modelId": "522159d5-d6e0-4c2a-aa44-5a542a12a830",
        "modelUniqueId": "55b1be94-671a-403e-a26c-667e9c47d091",
        "missingData": false,
        "id": 873798901625,
        "action": "Create",
        "name": "VFModule2",
        "modelName": "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1",
        "type": "VFmodule",
        "isEcompGeneratedNaming": true,
        "dynamicInputs": [],
        "dynamicModelName": "vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1djjni",
        "typeName": "M",
        "menuActions": {"edit": {}, "showAuditInfo": {}, "remove": {}, "delete": {}, "undoDelete": {}},
        "isFailed": false,
        "statusProperties": [{"key": "Prov Status:", "testId": "provStatus"}, {
          "key": "Orch Status:",
          "testId": "orchStatus"
        }],
        "trackById": "w7bvw1nh47s",
        "parentType": "VNF",
        "position": 2,
        "errors": {}
      }],
      "errors": {}
    }, {
      "modelCustomizationId": "91415b44-753d-494c-926a-456a9172bbb9",
      "modelId": "d6557200-ecf2-4641-8094-5393ae3aae60",
      "modelUniqueId": "91415b44-753d-494c-926a-456a9172bbb9",
      "missingData": false,
      "id": "dywch8hkomi",
      "action": "Create",
      "inMaint": false,
      "name": "yoav2",
      "modelName": "VF_vGeraldine 0",
      "type": "VF",
      "isEcompGeneratedNaming": true,
      "networkStoreKey": "VF_vGeraldine 0",
      "vnfStoreKey": "VF_vGeraldine 0",
      "typeName": "VNF",
      "menuActions": {"edit": {}, "showAuditInfo": {}, "duplicate": {}, "remove": {}, "delete": {}, "undoDelete": {}},
      "isFailed": false,
      "statusProperties": [{"key": "Prov Status:", "testId": "provStatus"}, {
        "key": "Orch Status:",
        "testId": "orchStatus"
      }],
      "trackById": "fjczf1urdqo",
      "parentType": "",
      "position": 1,
      "children": [],
      "errors": {}
    }];
    let from = {
      data: {
        type: 'VF',
        index: 1
      }
    };

    let to = {
      parent: {
        data: {
          type: 'VF',
          index: 0
        }
      }
    };
    jest.spyOn(service, 'array_move');

    service.drag(store, "serviceInstanceId", nodes, {from, to});


    expect(service.array_move).toHaveBeenCalled();

  });


});
