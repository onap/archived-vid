import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AvailableModelsTreeService} from './available-models-tree.service';
import {ServiceNodeTypes} from "../../../shared/models/ServiceNodeTypes";
import {DefaultDataGeneratorService} from "../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {MessageBoxService} from "../../../shared/components/messageBox/messageBox.service";
import {MessageBoxData} from "../../../shared/components/messageBox/messageBox.data";
import {SdcUiCommon} from "onap-ui-angular";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {SharedTreeService} from "../objectsToTree/shared.tree.service";

describe('Available Models Tree Service', () => {

  let injector;
  let service: AvailableModelsTreeService;
  let httpMock: HttpTestingController;
  let sharedTreeService: SharedTreeService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [AvailableModelsTreeService,
        DefaultDataGeneratorService,
        SharedTreeService,
        MockNgRedux]
    });
    await TestBed.compileComponents();
      injector = getTestBed();
      service = injector.get(AvailableModelsTreeService);
      httpMock = injector.get(HttpTestingController);
      sharedTreeService = injector.get(SharedTreeService);
  })().then(done).catch(done.fail));


  test('addingAlertAddingNewVfModuleModal should open message modal', () => {
    jest.spyOn(MessageBoxService.openModal, 'next');
    service.addingAlertAddingNewVfModuleModal();

    expect(MessageBoxService.openModal.next).toHaveBeenCalledWith(new MessageBoxData(
      "Select a parent",  // modal title
      "There are multiple instances on the right side that can contain this vf-module Please select the VNF instance, to add this vf-module to, on the right side and then click the + sign",
      SdcUiCommon.ModalType.warning,
      SdcUiCommon.ModalSize.medium,
      [
        {text: "Close", size: "medium", closeModal: true}
      ]));
  });



  describe('#shouldOpenModalDialogOnAddInstance', () => {

    test('should open popup on add instance', () => {
      // add vnf should return true
      let result = service.shouldOpenDialog(ServiceNodeTypes.VF, [], true);
      expect(result).toBeTruthy();

      //  add vfModule with user provided naming should return true
      result = service.shouldOpenDialog(ServiceNodeTypes.VFmodule, [], false);
      expect(result).toBeTruthy();

      //  add vfModule with dynamicInputs without defaultValues should return true
      result = service.shouldOpenDialog(ServiceNodeTypes.VFmodule, [{
        id: '2017488_pasqualevpe0_vnf_config_template_version',
        type: 'string',
        name: '2017488_pasqualevpe0_vnf_config_template_version',
        isRequired: true,
        description: 'VPE Software Version'
      }], true);
      expect(result).toBeTruthy();

      // add vfModule with dynamicInputs with defaultValues should return false
      result = service.shouldOpenDialog(ServiceNodeTypes.VFmodule, [{
        id: '2017488_pasqualevpe0_vnf_config_template_version',
        type: 'string',
        name: '2017488_pasqualevpe0_vnf_config_template_version',
        value: '17.2',
        isRequired: true,
        description: 'VPE Software Version'
      }], true);
      expect(result).toBeFalsy();
    });
  });


  test('shouldOpenVRFModal', () => {
    const nodes = [{
      "id": "dd024d73-9bd1-425d-9db5-476338d53433",
      "modelCustomizationId": "dd024d73-9bd1-425d-9db5-476338d53433",
      "modelVersionId": "9cac02be-2489-4374-888d-2863b4511a59",
      "modelUniqueId": "dd024d73-9bd1-425d-9db5-476338d53433",
      "name": "VRF Entry Configuration 0",
      "tooltip": "VRF",
      "type": "VRF",
      "count": 0,
      "max": 1,
      "children": [],
      "disabled": false,
      "dynamicInputs": [],
      "isEcompGeneratedNaming": false,
      "typeName": "VRF",
      "componentInfoType": "VRF",
      "data": {
      },
      "getModel" : ()=>{
        return  {
          min : 1
        }
      }
    }];


    const serviceStore = {
      "serviceHierarchy": {
        "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc": {}
      },
      "serviceInstance": {
        "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc": {
          "action": "Create",
          "isDirty": false,
          "vnfs": {},
          "vrfs": {
            "VRF Entry Configuration": {
              "action": "Create",
              "uuid": "9cac02be-2489-4374-888d-2863b4511a59",
              "inputs": {},
              "type": "Configuration",
              "trackById": "s0z58emiprq",
              "modelInfo": {
                "modelInvariantId": "b67a289b-1688-496d-86e8-1583c828be0a",
                "modelVersionId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                "modelName": "VRF Entry Configuration",
                "modelVersion": "5.0",
                "modelCustomizationId": "dd024d73-9bd1-425d-9db5-476338d53433",
                "modelUniqueId": "dd024d73-9bd1-425d-9db5-476338d53433",
                "modelCustomizationName": "VRF Entry Configuration",
                "uuid": "9cac02be-2489-4374-888d-2863b4511a59"
              },
              "vpns": {
                "VRF Entry Configuration vpns 1": {
                  "action": "Create",
                  "instanceId": "46fcb25a-e7ba-4d96-99ba-3bb6eae6aba7",
                  "instanceName": "LPPVPN",
                  "platformName": "AVPN",
                  "instanceType": "SERVICE-INFRASTRUCTURE",
                  "region": "USA,EMEA",
                  "customerId": "VPN1271",
                  "modelInfo": {
                    "modelCustomizationId": null,
                    "modelInvariantId": null,
                    "modelVersionId": null
                  },
                  "routeTargets": null,
                  "isSelected": true
                }
              },
              "networks": {
                "VRF Entry Configuration networks 1": {
                  "action ": "Create",
                  "instanceName": "AUK51a_oam_calea_net_0",
                  "instanceType": "SR-IOV-PROVIDER2-0",
                  "role": "role-1",
                  "orchStatus": "Active",
                  "physicalName": "sriovnet0",
                  "instanceId": "46fcb25a-e7ba-4d96-99ba-3bb6eae6aba7",
                  "serviceName": "LPPVPN",
                  "serviceUUID": "VPN1271",
                  "tenantName": "ecomp_ispt",
                  "lcpCloudRegionId": "USA,EMEA",
                  "modelInfo": {
                    "modelCustomizationId": "46fcb25a-e7ba-4d96-99ba-3bb6eae6aba7",
                    "modelInvariantId": "46fcb25a-e7ba-4d96-99ba-3bb6eae6aba7",
                    "modelVersionId": "7010093-df36-4dcb-8428-c3d02bf3f88d",
                    "modelType": "vpn"
                  }
                }
              }
            }
          },
          "instanceParams": [],
          "validationCounter": 0,
          "existingNames": {},
          "existingVNFCounterMap": {},
          "existingVRFCounterMap": {},
          "existingVnfGroupCounterMap": {},
          "existingNetworksCounterMap": {},
          "optionalGroupMembersMap": {},
          "networks": {},
          "vnfGroups": {},
          "bulkSize": 1,
          "service": {
            "vidNotions": {
              "instantiationUI": "serviceWithVRF",
              "modelCategory": "other",
              "viewEditUI": "serviceWithVRF",
              "instantiationType": "ALaCarte"
            },
            "uuid": "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc",
            "invariantUuid": "7ee41ce4-4827-44b0-a48e-2707a59905d2",
            "name": "VRF Service for Test",
            "version": "1.0",
            "toscaModelURL": null,
            "category": "Network L4+",
            "serviceType": "INFRASTRUCTURE",
            "serviceRole": "Configuration",
            "description": "xxx",
            "serviceEcompNaming": "true",
            "instantiationType": "A-La-Carte",
            "inputs": {}
          },
          "collectionResources": {},
          "configurations": {},
          "fabricConfigurations": {},
          "serviceProxies": {},
          "vfModules": {},
          "volumeGroups": {},
          "pnfs": {},
          "isALaCarte": true,
          "testApi": "VNF_API",
          "vidNotions": {
            "instantiationUI": "serviceWithVVRF",
            "modelCategory": "other",
            "viewEditUI": "serviceWithVRF",
            "instantiationType": "ALaCarte"
          }
        }
      }
    };

    spyOn(sharedTreeService, 'modelByIdentifiers').and.returnValue({});

    const serviceModelId :string = '4117a0b6-e234-467d-b5b9-fe2f68c8b0fc';

    let result: boolean = service.shouldOpenVRFModal(nodes,  serviceModelId, serviceStore);
    expect(result).not.toBeNull();

    serviceStore.serviceInstance[serviceModelId].existingVRFCounterMap = {
      "dd024d73-9bd1-425d-9db5-476338d53433" : 1
    };

    result = service.shouldOpenVRFModal(nodes,  serviceModelId, serviceStore);
    expect(result).toBeNull();

  });

});
