import {DuplicateService} from './duplicate.service';
import {LogService} from '../../../shared/utils/log/log.service';
import {NgRedux} from '@angular-redux/store';
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {SdcUiServices} from "onap-ui-angular";
import {IModalConfig} from 'onap-ui-angular/dist/components/common';
import {AppState} from "../../../shared/store/reducers";
import {getTestBed, TestBed} from "@angular/core/testing";
import {FeatureFlagsService} from "../../../shared/services/featureFlag/feature-flags.service";
import {SharedTreeService} from "../objectsToTree/shared.tree.service";

class MockAppStore<T> {
  getState(){
    return {
      getState() {
        return {
          service : {
            serviceHierarchy: {
              "serviceId" : {
                vnfs : {
                  "vnfModelName" : {
                    properties : {
                      max_instances : 2
                    }
                  },
                  "vnfModelName2" : {
                    properties : {
                      max_instances  : 2
                    }
                  },
                  "vnfModelName3" : {
                    properties : {
                    }
                  }
                }
              }
            },
            serviceInstance : {
              "serviceId" : {
                existingVNFCounterMap : {
                  "vnfModelId" : 1,
                  "vnfModelId2" : 2,
                  "vnfModelId3" : 0
                }
              }

            }
          }
        }
      }
    }
  }
}

class MockModalService<T> {}

class MockFeatureFlagsService extends  FeatureFlagsService{
  getAllFlags(): { [p: string]: boolean } {
    return {};
  }
}

describe('Drawing board tree service', () => {
  let injector;
  let service: DuplicateService;
  let store : NgRedux<AppState>;
  let featureFlagsService : FeatureFlagsService;
  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers : [
        DuplicateService,
        SharedTreeService,
        LogService,
        {provide: FeatureFlagsService, useClass: MockFeatureFlagsService},
        {provide: NgRedux, useClass: MockAppStore},
        {provide: SdcUiServices.ModalService, useClass: MockModalService}
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(DuplicateService);
    store = injector.get(NgRedux);
    featureFlagsService = injector.get(FeatureFlagsService);

  })().then(done).catch(done.fail));


  test('setNumberOfDuplicates should set number of duplicates', ()=>{
    service.setNumberOfDuplicates(10);
    expect(service.numberOfDuplicates).toEqual(10);
  });

  test('isEnabled should return false if type is VNF and has missing data', ()=>{
    let node  = {
      data : {
        type : 'VF',
        menuActions : {
          duplicate : () => {

          }
        }
      }
    };
    spyOn(node.data.menuActions, 'duplicate').and.returnValue(true);
    spyOn(service, 'hasMissingData').and.returnValue(true);
    let result : boolean = service.isEnabled(<any>node, null, null);
    expect(result).toBeFalsy();
  });

  test('openDuplicateModal', ()=>{
    spyOn(service, 'getRemainsInstance').and.returnValue(1);
    let result : IModalConfig = service.openDuplicateModal(
      'currentServiceId',
      'currentServiceId',
      'currentId',
      'storeKey',
      2,
      null,null);
    expect(result.title).toEqual('Duplicate Node');
  });

  test('openDuplicateModal should call getRemainsInstance with correct parameters', ()=>{
    spyOn(service, 'getRemainsInstance');
    service.openDuplicateModal(
      'currentServiceId',
      'currentServiceId',
      'currentId',
      'storeKey',
      2,
      null,null);
    expect(service.getRemainsInstance).toHaveBeenCalledWith('currentServiceId', 'currentId', 'currentServiceId', null, null);
  });

  test('canDuplicate VNF should return true', () => {
    let node : ITreeNode = <any> {data : {type : 'VF'}};

    let result = service.canDuplicate(node);
    expect(result).toBeTruthy();
  });

  test('canDuplicate Network should return true', () => {
    let node : ITreeNode = <any> {data : {type : 'VL'}};

    let result = service.canDuplicate(node);
    expect(result).toBeTruthy();
  });

  test('canDuplicate VFModule should return false', () => {
    let node : ITreeNode = <any> {data : {type : 'VFModule'}};

    let result = service.canDuplicate(node);
    expect(result).toBeFalsy();
  });

  test('Drawing board tree service should be defined', () => {
    expect(service).toBeDefined();
  });

  test('DrawingBoardDuplicateService should be defined', () => {
    expect(service).toBeDefined();
  });

  test('Duplicate multi vnfs should save multi vnfs in redux', () => {
    service.existingNames = {
      "2017488_pasqualevpe": "",
      "rrr": "",
      "ttt": ""
    };
    let newVnfs = service.cloneVnf(<any>{
      "vfModules": {
        "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2mtlfi": {
            "instanceName": "rrr",
            "volumeGroupName": "ttt"
          }
        }
      },
      "originalName": null,
      "trackById": "pfs1f0len3",
      "instanceName": "2017488_PASQUALEvPE"
    }, "2017-488_PASQUALE-vPE 0");

    expect(newVnfs.instanceName).toBe("2017488_PASQUALEvPE_001");
    expect(newVnfs.vfModules['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2']['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2mtlfi'].instanceName).toBe("rrr_001");
    expect(newVnfs.vfModules['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2']['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2mtlfi'].volumeGroupName).toBe("ttt_001");

    newVnfs = service.cloneVnf(<any>{
      "vfModules": {
        "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
          "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2mtlfi": {
            "instanceName": "rrr",
            "volumeGroupName": "ttt"
          }
        }
      },
      "originalName": null,
      "trackById": "pfs1f0len3",
      "instanceName": "2017488_PASQUALEvPE"
    }, "2017-488_PASQUALE-vPE 0");

    expect(newVnfs.instanceName).toBe("2017488_PASQUALEvPE_002");
    expect(newVnfs.vfModules['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2']['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2mtlfi'].instanceName).toBe("rrr_002");
    expect(newVnfs.vfModules['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2']['2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2mtlfi'].volumeGroupName).toBe("ttt_002");
  });

  test('ensure name is unique - send new name - shouldn\'t change name', () => {
    service.existingNames = {
      "2017488_pasqualevpe": "",
      "uniqueinstancename": ""
    };
    const name = "uniqueInstanceName-1";
    let uniqueName: string = service.ensureUniqueNameOrGenerateOne(name);
    expect(uniqueName).toEqual(name);
  });

  test('ensure name is unique send existing name should change name', () => {
    service.existingNames = {
      "2017488_pasqualevpe": "",
      "uniqueinstancename-1": ""
    };
    const name = "uniqueInstanceName-1";
    let uniqueName: string = service.ensureUniqueNameOrGenerateOne(name);
    expect(uniqueName).toEqual(name + "_001");
  });

  test('isAlreadyExist - send new name should return false', () => {
    service.existingNames = {
      "2017488_pasqualevpe": "",
      "uniqueinstancename": ""
    };
    const name = "uniqueinstancename-1";
    let isExist: boolean = service.isAlreadyExists(name, service.existingNames);
    expect(isExist).toBeFalsy();
  });

  test('isAlreadyExist - send existing name should return true', () => {
    service.existingNames = {
      "2017488_pasqualevpe": "",
      "uniqueinstancename-1": ""
    };
    const name = "uniqueinstancename-1";
    let isExist: boolean = service.isAlreadyExists(name, service.existingNames);
    expect(isExist).toBeTruthy();
  });

  test('isAlreadyExist - send existing name case insensitive should return true', () => {
    service.existingNames = {
      "2017488_pasqualevpe": "",
      "uniqueinstancename-1": ""
    };
    const name = "uniqueInstanceName-1";
    let isExist: boolean = service.isAlreadyExists(name, service.existingNames);
    expect(isExist).toBeTruthy();
  });

  test('getNumberAsPaddingString should return 001 if padding is 000 and val is 1 string', () => {
    let paddingNumber: string = service.getNumberAsPaddingString(1, '0000');
    expect(paddingNumber).toEqual('0001');
  });

  test('getNumberAsPaddingString should return 010 if padding is 000 and val is 10 string', () => {
    let paddingNumber: string = service.getNumberAsPaddingString(10, '0000');
    expect(paddingNumber).toEqual('0010');
  });

  test('generateVNFUniqueName should return the next free number', () => {
    const vnfName: string = "VF_vGeraldine 0";
    let result: string = service.generateUniqueStoreKey(
      "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
      vnfName,
      getExistingVNFs(),
      {}
    );

    expect(result).toEqual(vnfName + ":0003");
  });

  test('generateVNFUniqueName on duplicate multi vnfs should return the next free number', () => {
      spyOn(service, 'openDuplicateModal');
      service.openDuplicateModal(null, null, null,null, 2, null, null);

      expect(service.openDuplicateModal).toHaveBeenCalledWith(null, null, null,null, 2, null, null);
  });


  test('isEnabled should return false if node has  missing data', () => {
    let node  = {
      data : {
        missingData: true,
        menuActions : {
          duplicate : () => {

          }
        }
      }
    };
    let result : boolean = service.isEnabled(<any>node, null, null);
    expect(result).toBeFalsy();
  });

  test('isEnabled should return true if not reach to max', () => {
    let node  = {
      data : {
        missingData: false, modelName : "vnfModelName" , modelId : "vnfModelId", type : "VF", children: [], modelCustomizationId : "vnfModelId",modelUniqueId: "vnfModelId",
        menuActions : {
          duplicate : () => {

          }
        }
      }
    };
    let result : boolean = service.isEnabled(<any>node, <any>getStoreState(), "serviceId");
    expect(result).toBeTruthy();
  });

  test('isEnabled should return false if reach to max', () => {
    let node  = {
      data : {
        missingData: false, modelName : "vnfModelName2" , modelId : "vnfModelId2", type : "VF" ,children: [],
        menuActions : {
          duplicate : () => {

          }
        }
      }
    };
    let result : boolean = service.isEnabled(<any>node, <any>getStoreState(), "serviceId");
    expect(result).toBeFalsy();
  });

  test('isEnabled should return true if max is null', () => {
    let node  = {
      data : {
        missingData: false, modelName : "vnfModelName3" , modelId : "vnfModelId3",type : "VF",  children: [], modelUniqueId: "vnfModelId3",
        menuActions : {
          duplicate : () => {

          }
        }
      }
    };
    let result : boolean = service.isEnabled(<any>node, <any>getStoreState(), "serviceId");
    expect(result).toBeTruthy();
  });

  test('isEnabled should return false if type is vf module', () => {
    let node  = {
      data : {
        missingData: false, modelName : "vnfModelName3" , modelId : "vnfModelId3",type : "VFModule",  children: [],
        menuActions : {
        }
      }
    };
    let result : boolean = service.isEnabled(<any>node, <any>getStoreState(), "serviceId");
    expect(result).toBeFalsy();
  });

  test('getRemainsVNFinstance should return the remains 1 VNF', () => {
    let result : number = service.getRemainsInstance("vnfModelId", "vnfModelName" , "serviceId" , <any>getStoreState(), <any>{data : {type : 'VF'}});
    expect(result).toBe(1);
  });

  test('getRemainsVNFinstance should return the remains  0 VNF', () => {
    let result : number = service.getRemainsInstance("vnfModelId2", "vnfModelName2" , "serviceId" , <any>getStoreState(),<any>{data : {type : 'VF'}});
    expect(result).toBe(0);
  });

  test('isVNFChildrensHasMissingData should return true if vnf missing data = true', () => {
    let result : boolean = service.hasMissingData(<any>getTreeNode(true,false));
    expect(result).toBeTruthy();
  });

  test('isVNFChildrensHasMissingData return should false if vnf missing data = false', () => {
    let result : boolean = service.hasMissingData(<any>getTreeNode(false,false));
    expect(result).toBeFalsy();
  });

  test('isVNFChildrensHasMissingData should return true if vfModule missing data = true', () => {
    let result : boolean = service.hasMissingData(<any>getTreeNode(false,true));
    expect(result).toBeTruthy();
  });

  test('isVNFChildrensHasMissingData should return false if no children and  vfModule missing data = false', () => {
    let node = <any>getTreeNode(false,false);
    node.data.children = [];
    let result : boolean = service.hasMissingData(node);
    expect(result).toBeFalsy();
  });

  function getExistingVNFs(){
    return {"VF_vGeraldine 0":{"rollbackOnFailure":"true","vfModules":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1dgbxq":{"modelInfo":{"modelInvariantId":"98a7c88b-b577-476a-90e4-e25a5871e02b","modelVersionId":"522159d5-d6e0-4c2a-aa44-5a542a12a830","modelName":"VfVgeraldine..vflorence_vlc..module-1","modelVersion":"2","modelCustomizationId":"55b1be94-671a-403e-a26c-667e9c47d091","modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1"},"instanceParams":[{}]}}},"productFamilyId":"17cc1042-527b-11e6-beb8-9e71128cae77","lcpCloudRegionId":"hvf6","tenantId":"bae71557c5bb4d5aac6743a4e5f1d054","lineOfBusiness":"ONAP","platformName":"platform","modelInfo":{"modelInvariantId":"4160458e-f648-4b30-a176-43881ffffe9e","modelVersionId":"d6557200-ecf2-4641-8094-5393ae3aae60","modelName":"VF_vGeraldine","modelVersion":"2.0","modelCustomizationId":"91415b44-753d-494c-926a-456a9172bbb9","modelCustomizationName":"VF_vGeraldine 0"}},"VF_vGeraldine 0:0001":{"rollbackOnFailure":"true","vfModules":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1dgbxq":{"modelInfo":{"modelInvariantId":"98a7c88b-b577-476a-90e4-e25a5871e02b","modelVersionId":"522159d5-d6e0-4c2a-aa44-5a542a12a830","modelName":"VfVgeraldine..vflorence_vlc..module-1","modelVersion":"2","modelCustomizationId":"55b1be94-671a-403e-a26c-667e9c47d091","modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1"},"instanceParams":[{}]}}},"productFamilyId":"17cc1042-527b-11e6-beb8-9e71128cae77","lcpCloudRegionId":"hvf6","tenantId":"bae71557c5bb4d5aac6743a4e5f1d054","lineOfBusiness":"ONAP","platformName":"platform","modelInfo":{"modelInvariantId":"4160458e-f648-4b30-a176-43881ffffe9e","modelVersionId":"d6557200-ecf2-4641-8094-5393ae3aae60","modelName":"VF_vGeraldine","modelVersion":"2.0","modelCustomizationId":"91415b44-753d-494c-926a-456a9172bbb9","modelCustomizationName":"VF_vGeraldine 0"},"originalName":"VF_vGeraldine 0"},"VF_vGeraldine 0:0002":{"rollbackOnFailure":"true","vfModules":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1dgbxq":{"modelInfo":{"modelInvariantId":"98a7c88b-b577-476a-90e4-e25a5871e02b","modelVersionId":"522159d5-d6e0-4c2a-aa44-5a542a12a830","modelName":"VfVgeraldine..vflorence_vlc..module-1","modelVersion":"2","modelCustomizationId":"55b1be94-671a-403e-a26c-667e9c47d091","modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1"},"instanceParams":[{}]}}},"productFamilyId":"17cc1042-527b-11e6-beb8-9e71128cae77","lcpCloudRegionId":"hvf6","tenantId":"bae71557c5bb4d5aac6743a4e5f1d054","lineOfBusiness":"ONAP","platformName":"platform","modelInfo":{"modelInvariantId":"4160458e-f648-4b30-a176-43881ffffe9e","modelVersionId":"d6557200-ecf2-4641-8094-5393ae3aae60","modelName":"VF_vGeraldine","modelVersion":"2.0","modelCustomizationId":"91415b44-753d-494c-926a-456a9172bbb9","modelCustomizationName":"VF_vGeraldine 0"},"originalName":"VF_vGeraldine 0"}}
  }

  function getStoreState(){
    return {
      getState() {
        return {
          service : {
            serviceHierarchy: {
              "serviceId" : {
                vnfs : {
                  "vnfModelName" : {
                    properties : {
                      ecomp_generated_naming: "false",
                      max_instances : 2
                    }
                  },
                  "vnfModelName2" : {
                    properties : {
                      ecomp_generated_naming: "false",
                      max_instances  : 2
                    }
                  },
                  "vnfModelName3" : {
                    properties : {
                      ecomp_generated_naming: "false",
                    }
                  }
                }
              }
            },
            serviceInstance : {
              "serviceId" : {
                existingVNFCounterMap : {
                  "vnfModelId" : 1,
                  "vnfModelId2" : 2,
                  "vnfModelId3" : 0
                }
              }

            }
          },
          global : {
            flags : {

            }
          }
        }
      }
    }
  }


  function getTreeNode(VNfMissingData : boolean, vfModuleMissingData : boolean){
    return {
      data : {
        children : vfModuleMissingData ?
          [{
            missingData : true
          }] :
          [{
            missingData : false
          }
        ],
        missingData : VNfMissingData
      }
    }
  }
});



