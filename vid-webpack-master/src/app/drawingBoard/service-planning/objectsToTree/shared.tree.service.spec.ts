import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {NgReduxTestingModule} from "@angular-redux/store/testing";
import {SharedTreeService} from "./shared.tree.service";
import {ObjectToInstanceTreeService} from "./objectToInstanceTree/objectToInstanceTree.service";
import {ObjectToTreeService} from "./objectToTree.service";
import {DefaultDataGeneratorService} from "../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {DynamicInputsService} from "./dynamicInputs.service";
import {DialogService} from "ng2-bootstrap-modal";
import {VnfPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {ControlGeneratorUtil} from "../../../shared/components/genericForm/formControlsServices/control.generator.util.service";
import {AaiService} from "../../../shared/services/aaiService/aai.service";
import {NetworkPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/network/network.popup.service";
import {NetworkControlGenerator} from "../../../shared/components/genericForm/formControlsServices/networkGenerator/network.control.generator";
import {VfModulePopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {VfModuleControlGenerator} from "../../../shared/components/genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {VnfGroupControlGenerator} from "../../../shared/components/genericForm/formControlsServices/vnfGroupGenerator/vnfGroup.control.generator";
import {FeatureFlagsService} from "../../../shared/services/featureFlag/feature-flags.service";
import {VnfControlGenerator} from "../../../shared/components/genericForm/formControlsServices/vnfGenerator/vnf.control.generator";
import {NgRedux} from "@angular-redux/store";
import {GenericFormService} from "../../../shared/components/genericForm/generic-form.service";
import {FormBuilder} from "@angular/forms";
import {SdcUiComponentsModule} from "onap-ui-angular";
import {LogService} from "../../../shared/utils/log/log.service";
import {IframeService} from "../../../shared/utils/iframe.service";
import {BasicPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/basic.popup.service";
import {VnfGroupPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vnfGroup/vnfGroup.popup.service";
import {DuplicateService} from "../duplicate/duplicate.service";
import {AppState} from "../../../shared/store/reducers";
import {MessageBoxService} from "../../../shared/components/messageBox/messageBox.service";
import {ErrorMsgService} from "../../../shared/components/error-msg/error-msg.service";
import {AuditInfoModalComponent} from "../../../shared/components/auditInfoModal/auditInfoModal.component";
import {ILevelNodeInfo} from "./models/basic.model.info";
import {VnfModelInfo} from "./models/vnf/vnf.model.info";
import {ServiceInstanceActions} from "../../../shared/models/serviceInstanceActions";
import each from "jest-each";
import {DrawingBoardModes} from "../drawing-board.modes";
import {ComponentInfoService} from "../component-info/component-info.service";
import {ComponentInfoModel, ComponentInfoType} from "../component-info/component-info-model";
import {ModelInformationItem} from "../../../shared/components/model-information/model-information.component";
import {VpnStepService} from "./models/vrf/vrfModal/vpnStep/vpn.step.service";
import {NetworkStepService} from "./models/vrf/vrfModal/networkStep/network.step.service";
import {VfModuleUpgradePopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";
import {SharedControllersService} from "../../../shared/components/genericForm/formControlsServices/sharedControlles/shared.controllers.service";
import {ModalService} from "../../../shared/components/customModal/services/modal.service";
import {CreateDynamicComponentService} from "../../../shared/components/customModal/services/create-dynamic-component.service";
import { PnfPopupService } from "../../../shared/components/genericFormPopup/genericFormServices/pnf/pnf.popup.service";
import { PnfControlGenerator } from "../../../shared/components/genericForm/formControlsServices/pnfGenerator/pnf.control.generator";

class MockAppStore<T> {
  getState() {
    return getStore()
  }

  dispatch() {
  }
}




function getNodeWithData(menuAction:string){
  const nodeData = {
    menuActions: {}
  };
  nodeData['menuActions'][menuAction] =  {
    method: (node, serviceModelId) => {}
  };
  const node = {
    parent: {
      data: nodeData,
      parent: {
        data: nodeData
      }
    }
  };
  return node
}

describe('Shared Tree Service', () => {
  let injector;
  let service: SharedTreeService;
  let _objectToInstanceTreeService: ObjectToInstanceTreeService;
  let store: NgRedux<AppState>;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule, SdcUiComponentsModule],
      providers: [
        SharedTreeService,
        SharedControllersService,
        ObjectToTreeService,
        DefaultDataGeneratorService,
        DialogService,
        VnfPopupService,
        PnfPopupService,
        ControlGeneratorUtil,
        AaiService,
        LogService,
        BasicPopupService,
        VnfGroupPopupService,
        DuplicateService,
        IframeService,
        DynamicInputsService,
        NetworkPopupService,
        NetworkControlGenerator,
        VfModulePopupService,
        VfModuleUpgradePopupService,
        VfModuleControlGenerator,
        VnfGroupControlGenerator,
        DialogService,
        FeatureFlagsService,
        VnfControlGenerator,
        PnfControlGenerator,
        AaiService,
        DialogService,
        GenericFormService,
        FormBuilder,
        ErrorMsgService,
        ObjectToInstanceTreeService,
        ComponentInfoService,
        NetworkStepService,
        VpnStepService,
        ModalService,
        CreateDynamicComponentService,
        {provide: NgRedux, useClass: MockAppStore}
      ]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    service = injector.get(SharedTreeService);
    _objectToInstanceTreeService = injector.get(ObjectToInstanceTreeService);
    store = injector.get(NgRedux);
  })().then(done).catch(done.fail));

  test('SharedTreeService should be defined', () => {
    expect(service).toBeDefined();
  });

  test('SharedTreeService upgradeBottomUp should call redux actions', () => {
    const serviceModelId = "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd";
    const node = getNodeWithData("upgrade");
    spyOn(node.parent.data.menuActions['upgrade'], 'method');
    service.upgradeBottomUp(node, serviceModelId);
    expect(node.parent.data.menuActions['upgrade'].method).toBeCalledWith(node.parent, serviceModelId);
    expect(node.parent.data.menuActions['upgrade'].method).toBeCalledTimes(2);

  });

  test('SharedTreeService undoUpgradeBottomUp should call redux actions', () => {
    const serviceModelId = "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd";
    const node = getNodeWithData("undoUpgrade");
    spyOn(node.parent.data.menuActions['undoUpgrade'], 'method');
    service.undoUpgradeBottomUp(node, serviceModelId);
    expect(node.parent.data.menuActions['undoUpgrade'].method).toBeCalledWith(node.parent, serviceModelId);
    expect(node.parent.data.menuActions['undoUpgrade'].method).toBeCalledTimes(2);
  });

  test('shouldShowDeleteInstanceWithChildrenModal should open modal if child exist with action create', () => {
    jest.spyOn(MessageBoxService.openModal, 'next');
    let foo = () => {

    };
    let node = <any>{
      children: [{action: "Create"}, {action: "None"}],
      data: {
        typeName: 'VNF'
      }
    };
    service.shouldShowDeleteInstanceWithChildrenModal(node, "serviceModelId", foo);
    expect(MessageBoxService.openModal.next).toHaveBeenCalled();
  });

  each([
    ['volumeGroups by entry name', "volumeGroups",
      "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1", "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1"],
    ['vfmodule by customizationUuid', "vfModules",
      "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401", "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1"],
    ['vnf by customizationUuid', "vnfs",
      "1da7b585-5e61-4993-b95e-8e6606c81e45", "2017-488_PASQUALE-vPE 0"],
    ['vnfGroups by invariantUuid because no customizationUuid', "vnfGroups",
      "4bb2e27e-ddab-4790-9c6d-1f731bc14a45", "groupingservicefortest..ResourceInstanceGroup..0"],
  ]).test('modelByIdentifier should success: %s', (description, modelTypeName, modelUniqueIdOrName, expectedModelCustomizationName) => {
    let serviceModelFromHierarchy =
      getStore().service.serviceHierarchy["1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"];

    expect(service.modelByIdentifiers(serviceModelFromHierarchy, modelTypeName, modelUniqueIdOrName))
      .toHaveProperty("modelCustomizationName", expectedModelCustomizationName);
  });

  each([
    ['vfmodule by invariantUuid when there is customizationUuid', "vfModules", "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1"],
    ['network by non-existing modelUniqueIdOrName', "networks", "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1"],
    ['collectionResource has no resource', "collectionResources", "whatever"],
    ['non-existing model-type', "fooBar", "whatever"],
  ]).test('modelByIdentifier should fail: %s', (description, modelTypeName, modelUniqueIdOrName) => {
    let serviceModelFromHierarchy =
      getStore().service.serviceHierarchy["1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"];

    expect(service.modelByIdentifiers(serviceModelFromHierarchy, modelTypeName, modelUniqueIdOrName))
      .toBeUndefined();
  });

  test('openAuditInfoModalInsideIframe should open modal for failed instance', () => {
    jest.spyOn(AuditInfoModalComponent.openInstanceAuditInfoModal, 'next');

    let modelInfoServiceMock: ILevelNodeInfo = new VnfModelInfo(null, null,
      null, null, null, null, null,
      null, null, null, null,null, null);
    const modelMock = {"a": "a"};
    const instanceMock = {"instance": "instance", "trackById": "123456789"};
    const instanceTypeMock = "instanceTypeMock";
    jest.spyOn(modelInfoServiceMock, 'getModel').mockReturnValue(modelMock);
    let node = <any>{
      data: {
        modelId: '6b528779-44a3-4472-bdff-9cd15ec93450',
        trackById: '1245df21',
        isFailed: true
      }
    };
    service.openAuditInfoModal(node, "serviceModelId", instanceMock, instanceTypeMock, <any>modelInfoServiceMock);
    expect(AuditInfoModalComponent.openInstanceAuditInfoModal.next).toHaveBeenCalledWith(
      {
        "instance": instanceMock,
        "instanceId": "serviceModelId",
        "model": modelMock,
        "type": instanceTypeMock
      });
  });
  each([
    ['undefined', 'Unlimited (default)', {}],
    ['null', 'Unlimited (default)', {max:null}],
    ['3', '3', {max:3}],
    ]).
  test("when there is %s max instances in model , shell return %s text", (desc, expected, model) =>{
    expect(service.createMaximumToInstantiateModelInformationItem(model).values[0]).toBe(expected);
  });

  test('shouldShowDeleteInstanceWithChildrfenModal should not open modal if all childs with action None', () => {
    let foo = () => {
    };
    spyOn(MessageBoxService.openModal, 'next');

    let node = <any>{
      children: [{action: "None"}, {action: "None"}],
      data: {
        typeName: 'VNF'
      }
    };
    service.shouldShowDeleteInstanceWithChildrenModal(node, "serviceModelId", foo);
    expect(MessageBoxService.openModal.next).not.toHaveBeenCalled();
  });

  test ('addGeneralInfoItems should return correct info - ordered',()=>{
    let specificNetworkInfo = [
      ModelInformationItem.createInstance('Network role', "network role 1, network role 2")
    ];
    const actualInfoModel: ComponentInfoModel = service.addGeneralInfoItems(specificNetworkInfo,ComponentInfoType.NETWORK, getNetworkModelInfoFromHierarchy(),getNetworkInstance());

    let expectedGeneralInfo = [
      ModelInformationItem.createInstance('Model version', '2.0'),
      ModelInformationItem.createInstance('Model customization ID', 'customization-id-from-hierarchy'),
      ModelInformationItem.createInstance('Instance ID', 'NETWORK4_INSTANCE_ID'),
      ModelInformationItem.createInstance('Instance type', 'CONTRAIL30_HIMELGUARD'),
      ModelInformationItem.createInstance('In maintenance', false),
      ModelInformationItem.createInstance('Network role', 'network role 1, network role 2')
    ];
    expect(actualInfoModel.modelInfoItems).toEqual(expectedGeneralInfo);
  });

  each([
    ['model version from hierarchy', null, getNetworkModelInfoFromHierarchy(), '2.0'],
    ['undefined model', null, null, undefined],
    ['model version from instance', getSelectedModelInfo(), null, '5.0'],
    ['model version from instance', getSelectedModelInfo(), getNetworkModelInfoFromHierarchy(), '5.0'],
  ]).
  test ('getModelVersionEitherFromInstanceOrFromHierarchy should return %s ' ,
    (description, instance, model, expectedResult) =>{
    let actualResult = service.getModelVersionEitherFromInstanceOrFromHierarchy(instance, model);
    expect(actualResult).toEqual(expectedResult);
  });

  each([
    ['model CustomizationId from hierarchy', null, getNetworkModelInfoFromHierarchy(), 'customization-id-from-hierarchy'],
    ['undefined model', null, null, undefined],
    ['model CustomizationId from instance', getSelectedModelInfo(), null, 'model-customization-id-from-instance'],
    ['model CustomizationId from instance', getSelectedModelInfo(), getNetworkModelInfoFromHierarchy(), 'model-customization-id-from-instance'],
  ]).
  test ('getCustomizationIdEitherFromInstanceOrFromHierarchy should return %s ' ,
    (description, instance, model, expectedResult) =>{
      let actualResult = service.getModelCustomizationIdEitherFromInstanceOrFromHierarchy(instance, model);
      expect(actualResult).toEqual(expectedResult);
    });

  each([
    ['UUID from instance', getSelectedModelInfo(), getNetworkModelInfoFromHierarchy(),"UUID-from-instance" ],
    ['UUID from instance', getSelectedModelInfo(), null,"UUID-from-instance" ],
    ['UUID from hierarchy', null, getNetworkModelInfoFromHierarchy(),"UUID-from-hierarchy" ],
    ['UUID undefined', null, null, undefined],

  ]).
  test('getModelVersionIdEitherFromInstanceOrFromHierarchy should %s', (description, instance, model, expectedResult) => {
    let actualUuid = service.getModelVersionIdEitherFromInstanceOrFromHierarchy(instance, model);
    expect(actualUuid).toEqual(expectedResult);
  });

  each([
    ['from instance', getSelectedModelInfo(), getNetworkModelInfoFromHierarchy(), 'invariantId-from-instance'],
    ['from instance', getSelectedModelInfo(), null, 'invariantId-from-instance'],
    ['from hierarchy', null, getNetworkModelInfoFromHierarchy(), 'invariantId-from-hierarchy'],
    ['undefined', null, null, undefined],
  ]).
  test('getModelInvariantIdEitherFromInstanceOrFromHierarchy should return invariantId %s', (description, instance, model, expectedInvariantId) =>{
    let actualInvariantId = service.getModelInvariantIdEitherFromInstanceOrFromHierarchy(instance, model);
    expect(actualInvariantId).toEqual(expectedInvariantId);
  });

  test('statusProperties should be prop on node according to node properties', () => {

    let node = service.addingStatusProperty({orchStatus: 'completed', provStatus: 'inProgress', type: 'VFmodule', instanceModelInfo:{modelVersion: '1'}, inMaint: false});
    expect(node.statusProperties).toBeDefined();
    expect(node.statusProperties).toEqual([Object({
      key: 'Prov Status: ',
      value: 'inProgress',
      testId: 'provStatus'
    }), Object({
      key: 'Orch Status: ',
      value: 'completed',
      testId: 'orchStatus'
    }),
    Object({
      key: 'Model Version: ',
      value: '1',
      testId: 'modelVersion'
    })]);
    node = service.addingStatusProperty({orchStatus: 'completed', provStatus: 'inProgress',type: 'VFmodule',  instanceModelInfo:{}, inMaint: true});
    expect(node.statusProperties).toEqual([Object({
      key: 'Prov Status: ',
      value: 'inProgress',
      testId: 'provStatus'
    }), Object({
      key: 'Orch Status: ',
      value: 'completed',
      testId: 'orchStatus'
    }), Object({
      key: 'Model Version: ',
      value: undefined,
      testId: 'modelVersion'
    }), Object({
      key: 'In-maintenance',
      value: '',
      testId: 'inMaint'
    })
]);
  });
  each([
    ['version 2', '2', '2'],
    ['undefined', null, undefined]
  ]).
  test('getNodeModelVersion should return %s',  (description, nodeVersion, expectedVersion) => {
    let node = <any>{
      instanceModelInfo:{
        modelVersion: nodeVersion
      }
    };
    let actualVersion = service.getNodeModelVersion(node);
    expect(actualVersion).toEqual(expectedVersion);
  });

  each([
    [false, 'method is not in menu actions', ServiceInstanceActions.None, DrawingBoardModes.EDIT, {}, true],
    [false, 'there is no action in node', null, DrawingBoardModes.EDIT, {someMethod: "someValue"}, true],
    [true, 'edit mode, action is none, method in menu action', ServiceInstanceActions.None, DrawingBoardModes.EDIT, {someMethod: "someValue"}, true],
    [false, 'edit mode, action is none, method in menu action, macro service', ServiceInstanceActions.None, DrawingBoardModes.EDIT, {someMethod: "someValue"}, false],
    [false, 'edit mode, action is not none, method in menu action', ServiceInstanceActions.Resume, DrawingBoardModes.EDIT, {someMethod: "someValue"}, true],
    [false, 'edit mode, action is CREATE, method in menu action', ServiceInstanceActions.Resume, DrawingBoardModes.EDIT, {someMethod: "someValue"}, true]
  ]).test('shouldShowButtonGeneric return %s if %s ', (expected, description, action, mode, menuActions, isALaCarte) => {
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
      global: {
        drawingBoardStatus: mode
      },
      service : {
        serviceInstance: {
          someModelId : {
            isALaCarte
          }
        }

      }
    });
    let node = <any>{
      data:{
        action: action,
        menuActions: menuActions
      },
    };

    let res = service.shouldShowButtonGeneric(node, "someMethod", "someModelId");
    expect(res).toBe(expected);
  });

  const enableRemoveAndEditItemsDataProvider = [
    ['Create action CREATE mode', DrawingBoardModes.CREATE ,ServiceInstanceActions.Create, true],
    ['Create action VIEW mode',DrawingBoardModes.VIEW , ServiceInstanceActions.Create,false],
    ['Create action RETRY_EDIT mode',DrawingBoardModes.RETRY_EDIT,  ServiceInstanceActions.Create,  true],
    ['Create action EDIT mode',DrawingBoardModes.EDIT, ServiceInstanceActions.Create,  true],
    ['Create action RETRY mode',DrawingBoardModes.RETRY, ServiceInstanceActions.Create,  false],
    ['None action EDIT mode',DrawingBoardModes.EDIT,  ServiceInstanceActions.None, false],
    ['None action RETRY_EDIT mode', DrawingBoardModes.RETRY_EDIT, ServiceInstanceActions.None, false]];
  each(enableRemoveAndEditItemsDataProvider).test('shouldShowEditAndDelete if child exist with %s', (description, mode, action, enabled) => {
    jest.spyOn(store, 'getState').mockReturnValue(<any>{
        global: {
          drawingBoardStatus: mode
        }
      });
      let node = <any>{
        data:{
          action: action
        },
      };
      let res = service.shouldShowRemoveAndEdit(node);
      expect(res).toBe(enabled);
    });


  const isVfmoduleAlmostPartOfModelOnlyCustomizationUuidDifferProvider = [
    ['node is part of model, but vfmodule diff by customization',
      true, 'mDNS 01222020 0', '9fdc68e9-9f53-431c-b8a2-7e337b9a0d0a', '82160e6e-d9c4-45ef-bd19-01573ab11b61'],

    ['vnf model-name not found',
      false, 'mDNS 01222020 1', '9fdc68e9-9f53-431c-b8a2-7e337b9a0d0a', '82160e6e-d9c4-45ef-bd19-01573ab11b61'],

    ['vfmodule invariant-id not found',
      false, 'mDNS 01222020 0', 'wrong invariant-id', '82160e6e-d9c4-45ef-bd19-01573ab11b61'],

    ['vfmodule customization-id match',
      false, 'mDNS 01222020 0', '9fdc68e9-9f53-431c-b8a2-7e337b9a0d0a', 'c9b32003-febc-44e0-a97f-7630fa7fa4a0'],
  ];

  each(isVfmoduleAlmostPartOfModelOnlyCustomizationUuidDifferProvider).test('isVfmoduleAlmostPartOfModelOnlyCustomizationUuidDiffer: when  %s should return %s', (description, expected, vnfModelName, invariantUuid, customizationUuid) => {
    const serviceModelId : string = 'a243da28-c11e-45a8-9f26-0284a9a789bc';
    spyOn(store, 'getState').and.returnValue({
      service : {
        serviceHierarchy : {
          [serviceModelId] : {
            vnfs : {
              [vnfModelName] : {
                vfModules : {
                  vfModuleModelName : {
                    invariantUuid : invariantUuid,
                    customizationUuid : customizationUuid
                  }
                }
              }
            }
          }
        }
      }
    });

    const node = <any>{
      data:{
        modelInvariantId : '9fdc68e9-9f53-431c-b8a2-7e337b9a0d0a',
        modelCustomizationId : 'c9b32003-febc-44e0-a97f-7630fa7fa4a0',
        modelName : 'vfModuleModelName'
      },
      parent : {
        data : {
          modelName : "mDNS 01222020 0"
        }
      }
    };

    const isDiffCustomizationUuidResponse : boolean = service.isVfmoduleAlmostPartOfModelOnlyCustomizationUuidDiffer(node, serviceModelId);
    expect(isDiffCustomizationUuidResponse).toEqual(expected);
  });

  each([
    [false, true, true, false],
    [true, true, true, true],
    [true, true, false, true],
    [true, false, true, true],
    [true, false, false, false],
  ]).
  test('when flag is %s the UpdatedLatestVersion is %s and Vfmodule not exists on hierarchy is %s isShouldShowButtonGenericMustToBeCalled should return %s', (
     flag: boolean,
     isThereAnUpdatedLatestVersion: boolean,
     isVfModuleCustomizationIdNotExistsOnModel: boolean,
     isShouldShowButtonGenericMustToBeCalled: boolean
     ) => {
      let node = <any>  {};
      let serviceModelId : string = '08c5fa17-769a-4231-bd92-aed4b0ed086d';
      jest.spyOn(store, 'getState').mockReturnValue(<any>{
        global: {
          "flags": {
            "FLAG_FLASH_REPLACE_VF_MODULE": flag,
          },
        }
      });
      spyOn(service, 'isThereAnUpdatedLatestVersion').and.returnValue(isThereAnUpdatedLatestVersion);
      spyOn(service, 'isVfModuleCustomizationIdNotExistsOnModel').and.returnValue(isVfModuleCustomizationIdNotExistsOnModel);

      expect(service.isVfMoudleCouldBeUpgraded(node, serviceModelId)).toEqual(isShouldShowButtonGenericMustToBeCalled);
    });

  each([
    ['Vfm customization uuid not exists in model', 'not-existing-customization-uuid', 'service-model-id', true],
    ['Vfm customization uuid exists in model', 'existing-customization-uuid', 'service-model-id', false]
  ]).
  test('%s when vfModuleNode is  %s and serviceModelId is %s ', (
    description,
    modelCustomizationId,
    serviceModelId: string,
    isExistsOnHierarchy: boolean,
    ) => {

    const vfModuleNode = { data: {
        modelCustomizationId : modelCustomizationId
      }};

    jest.spyOn(store, 'getState').mockReturnValue(<any>{
      service : {
        "serviceHierarchy": {
          [serviceModelId]: {
            "vfModules": {
              "module-1": {
                "customizationUuid": "3d7f41c8-333b-4fee-b50d-5687e9c2170f",
              },
              "module-2": {
                "customizationUuid": "existing-customization-uuid",
              }
            }
          }
        }
      }
    });
    expect(service.isVfModuleCustomizationIdNotExistsOnModel(vfModuleNode, serviceModelId)).toEqual(isExistsOnHierarchy);


  });
  
  test("showPauseWithOrchStatus test", () => {
    const node = {
      "orchStatus":"Active"
    };
    expect(service.showPauseWithOrchStatus(node)).toEqual(false);
  });

});

function getStore() {
  return {
    "global": {
      "name": null,
      "flags": {
        "EMPTY_DRAWING_BOARD_TEST": false,
        "FLAG_NETWORK_TO_ASYNC_INSTANTIATION": false,
        "FLAG_ADD_MSO_TESTAPI_FIELD": true,
        "FLAG_SERVICE_MODEL_CACHE": true,
        "FLAG_SHOW_ASSIGNMENTS": true,
        "FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS": true,
        "FLAG_A_LA_CARTE_AUDIT_INFO": true,
        "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": true,
        "FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS": true,
        "FLAG_1902_NEW_VIEW_EDIT": true,
        "FLAG_1810_IDENTIFY_SERVICE_FOR_NEW_UI": false,
        "FLAG_1902_VNF_GROUPING": true,
        "FLAG_SHOW_VERIFY_SERVICE": true,
        "FLAG_ASYNC_ALACARTE_VFMODULE": true,
        "FLAG_ASYNC_ALACARTE_VNF": true,
        "FLAG_1810_AAI_LOCAL_CACHE": true,
        "FLAG_EXP_USE_DEFAULT_HOST_NAME_VERIFIER": false,
        "FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI": false,
        "FLAG_SUPPLEMENTARY_FILE": true,
        "FLAG_5G_IN_NEW_INSTANTIATION_UI": true,
        "FLAG_RESTRICTED_SELECT": false,
        "FLAG_1810_CR_LET_SELECTING_COLLECTOR_TYPE_UNCONDITIONALLY": true
      },
      "drawingBoardStatus": "VIEW",
      "type": "UPDATE_DRAWING_BOARD_STATUS"
    },
    "service": {
      "serviceHierarchy": {
        "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd": {
          "service": {
            "uuid": "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
            "invariantUuid": "cdb90b57-ed78-4d44-a5b4-7f43a02ec632",
            "name": "action-data",
            "version": "1.0",
            "toscaModelURL": null,
            "category": "Network L1-3",
            "serviceType": "pnf",
            "serviceRole": "Testing",
            "description": "PASQUALE vMX vPE based on Juniper 17.2 release. Updated with updated VF for v8.0 of VLM",
            "serviceEcompNaming": "false",
            "instantiationType": "Macro",
            "inputs": {},
            "vidNotions": {"instantiationUI": "legacy", "modelCategory": "other", "viewEditUI": "legacy"}
          },
          "vnfs": {
            "2017-388_PASQUALE-vPE 1": {
              "uuid": "0903e1c0-8e03-4936-b5c2-260653b96413",
              "invariantUuid": "00beb8f9-6d39-452f-816d-c709b9cbb87d",
              "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
              "name": "2017-388_PASQUALE-vPE",
              "version": "1.0",
              "customizationUuid": "280dec31-f16d-488b-9668-4aae55d6648a",
              "inputs": {},
              "commands": {},
              "properties": {
                "vmxvre_retype": "RE-VMX",
                "vnf_config_template_version": "get_input:2017488_pasqualevpe0_vnf_config_template_version",
                "sriov44_net_id": "48d399b3-11ee-48a8-94d2-f0ea94d6be8d",
                "int_ctl_net_id": "2f323477-6936-4d01-ac53-d849430281d9",
                "vmxvpfe_sriov41_0_port_mac": "00:11:22:EF:AC:DF",
                "int_ctl_net_name": "VMX-INTXI",
                "vmx_int_ctl_prefix": "10.0.0.10",
                "sriov43_net_id": "da349ca1-6de9-4548-be88-2d88e99bfef5",
                "sriov42_net_id": "760669ba-013d-4d9b-b0e7-4151fe2e6279",
                "sriov41_net_id": "25ad52d5-c165-40f8-b3b0-ddfc2373280a",
                "nf_type": "vPE",
                "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                "is_AVPN_service": "false",
                "vmx_RSG_name": "vREXI-affinity",
                "vmx_int_ctl_forwarding": "l2",
                "vmxvre_oam_ip_0": "10.0.0.10",
                "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                "vmxvre_image_name_0": "VRE-ENGINE_17.2-S2.1.qcow2",
                "vmxvre_instance": "0",
                "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvre_flavor_name": "ns.c1r16d32.v5",
                "vmxvpfe_volume_size_0": "40.0",
                "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                "nf_naming": "{ecomp_generated_naming=true}",
                "multi_stage_design": "true",
                "nf_naming_code": "Navneet",
                "vmxvre_name_0": "vREXI",
                "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                "vmxvpfe_image_name_0": "VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2",
                "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                "vmxvre_console": "vidconsole",
                "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                "vf_module_id": "123",
                "nf_function": "JAI",
                "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                "vmxvre_int_ctl_ip_0": "10.0.0.10",
                "ecomp_generated_naming": "true",
                "AIC_CLLI": "get_input:2017488_pasqualevpe0_AIC_CLLI",
                "vnf_name": "mtnj309me6vre",
                "vmxvpfe_sriov41_0_port_unknownunicastallow": "true",
                "vmxvre_volume_type_1": "HITACHI",
                "vmxvpfe_sriov44_0_port_broadcastallow": "true",
                "vmxvre_volume_type_0": "HITACHI",
                "vmxvpfe_volume_type_0": "HITACHI",
                "vmxvpfe_sriov43_0_port_broadcastallow": "true",
                "bandwidth_units": "get_input:2017488_pasqualevpe0_bandwidth_units",
                "vnf_id": "123",
                "vmxvre_oam_prefix": "24",
                "availability_zone_0": "mtpocfo-kvm-az01",
                "ASN": "get_input:2017488_pasqualevpe0_ASN",
                "vmxvre_chassis_i2cid": "161",
                "vmxvpfe_name_0": "vPFEXI",
                "bandwidth": "get_input:2017488_pasqualevpe0_bandwidth",
                "availability_zone_max_count": "1",
                "vmxvre_volume_size_0": "45.0",
                "vmxvre_volume_size_1": "50.0",
                "vmxvpfe_sriov42_0_port_broadcastallow": "true",
                "vmxvre_oam_gateway": "10.0.0.10",
                "vmxvre_volume_name_1": "vREXI_FAVolume",
                "vmxvre_ore_present": "0",
                "vmxvre_volume_name_0": "vREXI_FBVolume",
                "vmxvre_type": "0",
                "vnf_instance_name": "get_input:2017488_pasqualevpe0_vnf_instance_name",
                "vmxvpfe_sriov41_0_port_unknownmulticastallow": "true",
                "oam_net_id": "b95eeb1d-d55d-4827-abb4-8ebb94941429",
                "vmx_int_ctl_len": "24",
                "vmxvpfe_sriov43_0_port_vlanstrip": "false",
                "vmxvpfe_sriov41_0_port_broadcastallow": "true",
                "vmxvre_volume_id_1": "6e86797e-03cd-4fdc-ba72-2957119c746d",
                "vmxvpfe_sriov41_0_port_vlanfilter": "4001",
                "nf_role": "Testing",
                "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
              },
              "type": "VF",
              "modelCustomizationName": "2017-388_PASQUALE-vPE 1",
              "vfModules": {},
              "volumeGroups": {},
              "vfcInstanceGroups": {}
            },
            "2017-388_PASQUALE-vPE 0": {
              "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168",
              "invariantUuid": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
              "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
              "name": "2017-388_PASQUALE-vPE",
              "version": "4.0",
              "customizationUuid": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
              "inputs": {},
              "commands": {},
              "properties": {
                "vmxvre_retype": "RE-VMX",
                "vnf_config_template_version": "get_input:2017488_pasqualevpe0_vnf_config_template_version",
                "sriov44_net_id": "48d399b3-11ee-48a8-94d2-f0ea94d6be8d",
                "int_ctl_net_id": "2f323477-6936-4d01-ac53-d849430281d9",
                "vmxvpfe_sriov41_0_port_mac": "00:11:22:EF:AC:DF",
                "int_ctl_net_name": "VMX-INTXI",
                "vmx_int_ctl_prefix": "10.0.0.10",
                "sriov43_net_id": "da349ca1-6de9-4548-be88-2d88e99bfef5",
                "sriov42_net_id": "760669ba-013d-4d9b-b0e7-4151fe2e6279",
                "sriov41_net_id": "25ad52d5-c165-40f8-b3b0-ddfc2373280a",
                "nf_type": "vPE",
                "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                "is_AVPN_service": "false",
                "vmx_RSG_name": "vREXI-affinity",
                "vmx_int_ctl_forwarding": "l2",
                "vmxvre_oam_ip_0": "10.0.0.10",
                "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                "vmxvre_image_name_0": "VRE-ENGINE_17.2-S2.1.qcow2",
                "vmxvre_instance": "0",
                "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvre_flavor_name": "ns.c1r16d32.v5",
                "vmxvpfe_volume_size_0": "40.0",
                "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                "nf_naming": "{ecomp_generated_naming=true}",
                "multi_stage_design": "true",
                "nf_naming_code": "Navneet",
                "vmxvre_name_0": "vREXI",
                "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                "vmxvpfe_image_name_0": "VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2",
                "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                "vmxvre_console": "vidconsole",
                "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                "vf_module_id": "123",
                "nf_function": "JAI",
                "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                "vmxvre_int_ctl_ip_0": "10.0.0.10",
                "ecomp_generated_naming": "true",
                "AIC_CLLI": "get_input:2017488_pasqualevpe0_AIC_CLLI",
                "vnf_name": "mtnj309me6vre",
                "vmxvpfe_sriov41_0_port_unknownunicastallow": "true",
                "vmxvre_volume_type_1": "HITACHI",
                "vmxvpfe_sriov44_0_port_broadcastallow": "true",
                "vmxvre_volume_type_0": "HITACHI",
                "vmxvpfe_volume_type_0": "HITACHI",
                "vmxvpfe_sriov43_0_port_broadcastallow": "true",
                "bandwidth_units": "get_input:2017488_pasqualevpe0_bandwidth_units",
                "vnf_id": "123",
                "vmxvre_oam_prefix": "24",
                "availability_zone_0": "mtpocfo-kvm-az01",
                "ASN": "get_input:2017488_pasqualevpe0_ASN",
                "vmxvre_chassis_i2cid": "161",
                "vmxvpfe_name_0": "vPFEXI",
                "bandwidth": "get_input:2017488_pasqualevpe0_bandwidth",
                "availability_zone_max_count": "1",
                "vmxvre_volume_size_0": "45.0",
                "vmxvre_volume_size_1": "50.0",
                "vmxvpfe_sriov42_0_port_broadcastallow": "true",
                "vmxvre_oam_gateway": "10.0.0.10",
                "vmxvre_volume_name_1": "vREXI_FAVolume",
                "vmxvre_ore_present": "0",
                "vmxvre_volume_name_0": "vREXI_FBVolume",
                "vmxvre_type": "0",
                "vnf_instance_name": "get_input:2017488_pasqualevpe0_vnf_instance_name",
                "vmxvpfe_sriov41_0_port_unknownmulticastallow": "true",
                "oam_net_id": "b95eeb1d-d55d-4827-abb4-8ebb94941429",
                "vmx_int_ctl_len": "24",
                "vmxvpfe_sriov43_0_port_vlanstrip": "false",
                "vmxvpfe_sriov41_0_port_broadcastallow": "true",
                "vmxvre_volume_id_1": "6e86797e-03cd-4fdc-ba72-2957119c746d",
                "vmxvpfe_sriov41_0_port_vlanfilter": "4001",
                "nf_role": "Testing",
                "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
              },
              "type": "VF",
              "modelCustomizationName": "2017-388_PASQUALE-vPE 0",
              "vfModules": {},
              "volumeGroups": {},
              "vfcInstanceGroups": {}
            },
            "2017-488_PASQUALE-vPE 0": {
              "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
              "invariantUuid": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
              "description": "Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM",
              "name": "2017-488_PASQUALE-vPE",
              "version": "5.0",
              "customizationUuid": "1da7b585-5e61-4993-b95e-8e6606c81e45",
              "inputs": {},
              "commands": {},
              "properties": {
                "max_instances": 1,
                "vmxvre_retype": "RE-VMX",
                "vnf_config_template_version": "get_input:2017488_pasqualevpe0_vnf_config_template_version",
                "sriov44_net_id": "48d399b3-11ee-48a8-94d2-f0ea94d6be8d",
                "int_ctl_net_id": "2f323477-6936-4d01-ac53-d849430281d9",
                "vmxvpfe_sriov41_0_port_mac": "00:11:22:EF:AC:DF",
                "int_ctl_net_name": "VMX-INTXI",
                "vmx_int_ctl_prefix": "10.0.0.10",
                "sriov43_net_id": "da349ca1-6de9-4548-be88-2d88e99bfef5",
                "sriov42_net_id": "760669ba-013d-4d9b-b0e7-4151fe2e6279",
                "sriov41_net_id": "25ad52d5-c165-40f8-b3b0-ddfc2373280a",
                "nf_type": "vPE",
                "vmxvpfe_int_ctl_ip_1": "10.0.0.10",
                "is_AVPN_service": "false",
                "vmx_RSG_name": "vREXI-affinity",
                "vmx_int_ctl_forwarding": "l2",
                "vmxvre_oam_ip_0": "10.0.0.10",
                "vmxvpfe_sriov44_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvpfe_sriov41_0_port_vlanstrip": "false",
                "vmxvpfe_sriov42_0_port_vlanfilter": "4001",
                "vmxvpfe_sriov44_0_port_unknownunicastallow": "true",
                "vmxvre_image_name_0": "VRE-ENGINE_17.2-S2.1.qcow2",
                "vmxvre_instance": "0",
                "vmxvpfe_sriov43_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvre_flavor_name": "ns.c1r16d32.v5",
                "vmxvpfe_volume_size_0": "40.0",
                "vmxvpfe_sriov43_0_port_vlanfilter": "4001",
                "nf_naming": "{ecomp_generated_naming=true}",
                "multi_stage_design": "true",
                "nf_naming_code": "Navneet",
                "vmxvre_name_0": "vREXI",
                "vmxvpfe_sriov42_0_port_vlanstrip": "false",
                "vmxvpfe_volume_name_0": "vPFEXI_FBVolume",
                "vmx_RSG_id": "bd89a33c-13c3-4a04-8fde-1a57eb123141",
                "vmxvpfe_image_name_0": "VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2",
                "vmxvpfe_sriov43_0_port_unknownunicastallow": "true",
                "vmxvpfe_sriov44_0_port_unknownmulticastallow": "true",
                "vmxvre_console": "vidconsole",
                "vmxvpfe_sriov44_0_port_vlanfilter": "4001",
                "vmxvpfe_sriov42_0_port_mac": "00:11:22:EF:AC:DF",
                "vmxvpfe_volume_id_0": "47cede15-da2f-4397-a101-aa683220aff3",
                "vmxvpfe_sriov42_0_port_unknownmulticastallow": "true",
                "vmxvpfe_sriov44_0_port_vlanstrip": "false",
                "vf_module_id": "123",
                "nf_function": "JAI",
                "vmxvpfe_sriov43_0_port_unknownmulticastallow": "true",
                "vmxvre_int_ctl_ip_0": "10.0.0.10",
                "ecomp_generated_naming": "true",
                "AIC_CLLI": "get_input:2017488_pasqualevpe0_AIC_CLLI",
                "vnf_name": "mtnj309me6vre",
                "vmxvpfe_sriov41_0_port_unknownunicastallow": "true",
                "vmxvre_volume_type_1": "HITACHI",
                "vmxvpfe_sriov44_0_port_broadcastallow": "true",
                "vmxvre_volume_type_0": "HITACHI",
                "vmxvpfe_volume_type_0": "HITACHI",
                "vmxvpfe_sriov43_0_port_broadcastallow": "true",
                "bandwidth_units": "get_input:2017488_pasqualevpe0_bandwidth_units",
                "vnf_id": "123",
                "vmxvre_oam_prefix": "24",
                "availability_zone_0": "mtpocfo-kvm-az01",
                "ASN": "get_input:2017488_pasqualevpe0_ASN",
                "vmxvre_chassis_i2cid": "161",
                "vmxvpfe_name_0": "vPFEXI",
                "bandwidth": "get_input:2017488_pasqualevpe0_bandwidth",
                "availability_zone_max_count": "1",
                "vmxvre_volume_size_0": "45.0",
                "vmxvre_volume_size_1": "50.0",
                "vmxvpfe_sriov42_0_port_broadcastallow": "true",
                "vmxvre_oam_gateway": "10.0.0.10",
                "vmxvre_volume_name_1": "vREXI_FAVolume",
                "vmxvre_ore_present": "0",
                "vmxvre_volume_name_0": "vREXI_FBVolume",
                "vmxvre_type": "0",
                "vnf_instance_name": "get_input:2017488_pasqualevpe0_vnf_instance_name",
                "vmxvpfe_sriov41_0_port_unknownmulticastallow": "true",
                "oam_net_id": "b95eeb1d-d55d-4827-abb4-8ebb94941429",
                "vmx_int_ctl_len": "24",
                "vmxvpfe_sriov43_0_port_vlanstrip": "false",
                "vmxvpfe_sriov41_0_port_broadcastallow": "true",
                "vmxvre_volume_id_1": "6e86797e-03cd-4fdc-ba72-2957119c746d",
                "vmxvpfe_sriov41_0_port_vlanfilter": "4001",
                "nf_role": "Testing",
                "vmxvre_volume_id_0": "f4eacb79-f687-4e9d-b760-21847c8bb15a",
                "vmxvpfe_sriov42_0_port_unknownunicastallow": "true",
                "vmxvpfe_flavor_name": "ns.c20r16d25.v5"
              },
              "type": "VF",
              "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
              "vfModules": {
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                  "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                  "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                  "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "version": "6",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "properties": {
                    "minCountInstances": 0,
                    "maxCountInstances": null,
                    "initialCount": 0,
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "baseModule": false
                  },
                  "inputs": {},
                  "volumeGroupAllowed": true
                },
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                  "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                  "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                  "customizationUuid": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "version": "5",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                  "properties": {
                    "minCountInstances": 1,
                    "maxCountInstances": 1,
                    "initialCount": 1,
                    "vfModuleLabel": "PASQUALE_base_vPE_BV",
                    "baseModule": true
                  },
                  "inputs": {},
                  "volumeGroupAllowed": false
                },
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                  "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                  "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                  "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                  "version": "6",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                  "properties": {
                    "minCountInstances": 0,
                    "maxCountInstances": null,
                    "initialCount": 0,
                    "vfModuleLabel": "PASQUALE_vPFE_BV",
                    "baseModule": false
                  },
                  "inputs": {},
                  "volumeGroupAllowed": true
                }
              },
              "volumeGroups": {
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
                  "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
                  "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
                  "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "version": "6",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
                  "properties": {
                    "minCountInstances": 0,
                    "maxCountInstances": null,
                    "initialCount": 0,
                    "vfModuleLabel": "PASQUALE_vRE_BV",
                    "baseModule": false
                  },
                  "inputs": {}
                },
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
                  "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
                  "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
                  "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
                  "description": null,
                  "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                  "version": "6",
                  "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
                  "properties": {
                    "minCountInstances": 0,
                    "maxCountInstances": null,
                    "initialCount": 0,
                    "vfModuleLabel": "PASQUALE_vPFE_BV",
                    "baseModule": false
                  },
                  "inputs": {}
                }
              },
              "vfcInstanceGroups": {}
            }
          },
          "networks": {
            "ExtVL 0": {
              "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
              "invariantUuid": "379f816b-a7aa-422f-be30-17114ff50b7c",
              "description": "ECOMP generic virtual link (network) base type for all other service-level and global networks",
              "name": "ExtVL",
              "version": "37.0",
              "customizationUuid": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
              "inputs": {},
              "commands": {},
              "properties": {
                "network_assignments": "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
                "exVL_naming": "{ecomp_generated_naming=true}",
                "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                "network_homing": "{ecomp_selected_instance_node_target=false}"
              },
              "type": "VL",
              "modelCustomizationName": "ExtVL 0"
            }
          },
          "collectionResources": {},
          "configurations": {},
          "fabricConfigurations": {},
          "serviceProxies": {},
          "vfModules": {
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
              "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
              "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
              "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
              "description": null,
              "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "version": "6",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "properties": {
                "minCountInstances": 0,
                "maxCountInstances": null,
                "initialCount": 0,
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "baseModule": false
              },
              "inputs": {},
              "volumeGroupAllowed": true
            },
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
              "uuid": "f8360508-3f17-4414-a2ed-6bc71161e8db",
              "invariantUuid": "b34833bb-6aa9-4ad6-a831-70b06367a091",
              "customizationUuid": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
              "description": null,
              "name": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
              "version": "5",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
              "properties": {
                "minCountInstances": 1,
                "maxCountInstances": 1,
                "initialCount": 1,
                "vfModuleLabel": "PASQUALE_base_vPE_BV",
                "baseModule": true
              },
              "inputs": {},
              "volumeGroupAllowed": false
            },
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
              "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
              "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
              "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
              "description": null,
              "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
              "version": "6",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
              "properties": {
                "minCountInstances": 0,
                "maxCountInstances": null,
                "initialCount": 0,
                "vfModuleLabel": "PASQUALE_vPFE_BV",
                "baseModule": false
              },
              "inputs": {},
              "volumeGroupAllowed": true
            }
          },
          "volumeGroups": {
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1": {
              "uuid": "25284168-24bb-4698-8cb4-3f509146eca5",
              "invariantUuid": "7253ff5c-97f0-4b8b-937c-77aeb4d79aa1",
              "customizationUuid": "f7e7c365-60cf-49a9-9ebf-a1aa11b9d401",
              "description": null,
              "name": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "version": "6",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
              "properties": {
                "minCountInstances": 0,
                "maxCountInstances": null,
                "initialCount": 0,
                "vfModuleLabel": "PASQUALE_vRE_BV",
                "baseModule": false
              },
              "inputs": {}
            },
            "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2": {
              "uuid": "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a",
              "invariantUuid": "eff8cc59-53a1-4101-aed7-8cf24ecf8339",
              "customizationUuid": "3cd946bb-50e0-40d8-96d3-c9023520b557",
              "description": null,
              "name": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
              "version": "6",
              "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2",
              "properties": {
                "minCountInstances": 0,
                "maxCountInstances": null,
                "initialCount": 0,
                "vfModuleLabel": "PASQUALE_vPFE_BV",
                "baseModule": false
              },
              "inputs": {}
            }
          },
          "pnfs": {},
          "vnfGroups": {
            "groupingservicefortest..ResourceInstanceGroup..0": {
              "type": "VnfGroup",
              "invariantUuid": "4bb2e27e-ddab-4790-9c6d-1f731bc14a45",
              "uuid": "daeb6568-cef8-417f-9075-ed259ce59f48",
              "version": "1",
              "name": "groupingservicefortest..ResourceInstanceGroup..0",
              "modelCustomizationName": "groupingservicefortest..ResourceInstanceGroup..0",
              "properties": {
                "contained_resource_type": "VF",
                "role": "SERVICE-ACCESS",
                "function": "DATA",
                "description": "DDD0",
                "type": "LOAD-GROUP",
                "ecomp_generated_naming": "true"
              },
              "members": {
                "vdorothea_svc_vprs_proxy 0": {
                  "uuid": "65fadfa8-a0d9-443f-95ad-836cd044e26c",
                  "invariantUuid": "f4baae0c-b3a5-4ca1-a777-afbffe7010bc",
                  "description": "A Proxy for Service vDOROTHEA_Svc_vPRS",
                  "name": "vDOROTHEA_Svc_vPRS Service Proxy",
                  "version": "1.0",
                  "customizationUuid": "bdb63d23-e132-4ce7-af2c-a493b4cafac9",
                  "inputs": {},
                  "commands": {},
                  "properties": {},
                  "type": "Service Proxy",
                  "sourceModelUuid": "da7827a2-366d-4be6-8c68-a69153c61274",
                  "sourceModelInvariant": "24632e6b-584b-4f45-80d4-fefd75fd9f14",
                  "sourceModelName": "vDOROTHEA_Svc_vPRS"
                }
              }
            },
            "groupingservicefortest..ResourceInstanceGroup..1": {
              "type": "VnfGroup",
              "invariantUuid": "a704112d-dbc6-4e56-8d4e-aec57e95ef9a",
              "uuid": "c2b300e6-45de-4e5e-abda-3032bee2de56",
              "version": "1",
              "name": "groupingservicefortest..ResourceInstanceGroup..1",
              "modelCustomizationName": "groupingservicefortest..ResourceInstanceGroup..1",
              "properties": {
                "contained_resource_type": "VF",
                "role": "SERVICE-ACCESS",
                "function": "SIGNALING",
                "description": "DDD1",
                "type": "LOAD-GROUP",
                "ecomp_generated_naming": "true"
              },
              "members": {
                "tsbc0001vm001_svc_proxy 0": {
                  "uuid": "65fadfa8-a0d9-443f-95ad-836cd044e26c",
                  "invariantUuid": "f4baae0c-b3a5-4ca1-a777-afbffe7010bc",
                  "description": "A Proxy for Service tsbc0001vm001_Svc",
                  "name": "tsbc0001vm001_Svc Service Proxy",
                  "version": "1.0",
                  "customizationUuid": "3d814462-30fb-4c62-b997-9aa360d27ead",
                  "inputs": {},
                  "commands": {},
                  "properties": {},
                  "type": "Service Proxy",
                  "sourceModelUuid": "28aeb8f6-5620-4148-8bfb-a5fb406f0309",
                  "sourceModelInvariant": "c989ab9a-33c7-46ec-b521-1b2daef5f047",
                  "sourceModelName": "tsbc0001vm001_Svc"
                }
              }
            }
          }
        },
        "b75e0d22-05ff-4448-9266-5f0d4e1dbbd6": {
          "service": {
            "uuid": "b75e0d22-05ff-4448-9266-5f0d4e1dbbd6",
            "invariantUuid": "5b9c0f33-eec1-484a-bf77-736a6644d7a8",
            "name": "Using VID for VoIP Network Instantiations Shani",
            "version": "1.0",
            "toscaModelURL": null,
            "category": "VoIP Call Control",
            "serviceType": "",
            "serviceRole": "",
            "description": "Using VID for VoIP Network Instantiations Shani",
            "serviceEcompNaming": "true",
            "instantiationType": "ClientConfig",
            "inputs": {},
            "vidNotions": {"instantiationUI": "legacy", "modelCategory": "other", "viewEditUI": "legacy"}
          },
          "vnfs": {},
          "networks": {
            "AIC30_CONTRAIL_BASIC 0": {
              "uuid": "ac815c68-35b7-4ea4-9d04-92d2f844b27c",
              "invariantUuid": "de01afb5-532b-451d-aac4-ff9ff0644060",
              "description": "Basic contrail 3.0.x L3 network for AIC 3.x sites. ",
              "name": "AIC30_CONTRAIL_BASIC",
              "version": "3.0",
              "customizationUuid": "e94d61f7-b4b2-489a-a4a7-30b1a1a80daf",
              "inputs": {},
              "commands": {},
              "properties": {
                "network_assignments": "{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}",
                "exVL_naming": "{ecomp_generated_naming=true}",
                "network_flows": "{is_network_policy=false, is_bound_to_vpn=false}",
                "network_scope": "Service",
                "network_type": "AIC30_CONTRAIL_BASIC",
                "network_technology": "Contrail",
                "network_homing": "{ecomp_selected_instance_node_target=false}"
              },
              "type": "VL",
              "modelCustomizationName": "AIC30_CONTRAIL_BASIC 0"
            }
          },
          "collectionResources": {},
          "configurations": {},
          "fabricConfigurations": {},
          "serviceProxies": {},
          "vfModules": {},
          "volumeGroups": {},
          "pnfs": {},
          "vnfGroups": {}
        }
      },
      "serviceInstance": {
        "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd": {
          "vnfs": {
            "2017-488_PASQUALE-vPE 0": {
              "action": "None",
              "inMaint": false,
              "rollbackOnFailure": "true",
              "originalName": "2017-488_PASQUALE-vPE 0",
              "isMissingData": false,
              "trackById": "stigekyxrqi",
              "vfModules": {
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0": {
                  "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0gytfi": {
                    "isMissingData": false,
                    "sdncPreReload": null,
                    "modelInfo": {
                      "modelType": "VFmodule",
                      "modelInvariantId": "b34833bb-6aa9-4ad6-a831-70b06367a091",
                      "modelVersionId": "f8360508-3f17-4414-a2ed-6bc71161e8db",
                      "modelName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0",
                      "modelVersion": "5",
                      "modelCustomizationId": "a55961b2-2065-4ab0-a5b7-2fcee1c227e3",
                      "modelCustomizationName": "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"
                    },
                    "instanceParams": [{}],
                    "trackById": "3oj23o7nupo"
                  }
                }
              },
              "vnfStoreKey": "2017-488_PASQUALE-vPE 0",
              "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
              "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
              "lcpCloudRegionId": "AAIAIC25",
              "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
              "lineOfBusiness": "ONAP",
              "platformName": "xxx1",
              "modelInfo": {
                "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                "modelVersionId": "69e09f68-8b63-4cc9-b9ff-860960b5db09",
                "modelName": "2017-488_PASQUALE-vPE",
                "modelVersion": "5.0",
                "modelCustomizationName": "2017-488_PASQUALE-vPE 0",
                "modelCustomizationId": "1da7b585-5e61-4993-b95e-8e6606c81e45",
                "uuid": "69e09f68-8b63-4cc9-b9ff-860960b5db09"
              },
              "legacyRegion": "11111111",
              "instanceParams": [{}]
            },
            "2017-388_PASQUALE-vPE 0": {
              "action": "Create",
              "inMaint": false,
              "rollbackOnFailure": "true",
              "originalName": "2017-388_PASQUALE-vPE 0",
              "isMissingData": false,
              "trackById": "nib719t5vca",
              "vfModules": {},
              "vnfStoreKey": "2017-388_PASQUALE-vPE 0",
              "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
              "lcpCloudRegionId": "AAIAIC25",
              "legacyRegion": "11111",
              "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
              "platformName": "platform",
              "lineOfBusiness": "zzz1",
              "instanceParams": [{}],
              "modelInfo": {
                "modelInvariantId": "72e465fe-71b1-4e7b-b5ed-9496118ff7a8",
                "modelVersionId": "afacccf6-397d-45d6-b5ae-94c39734b168",
                "modelName": "2017-388_PASQUALE-vPE",
                "modelVersion": "4.0",
                "modelCustomizationId": "b3c76f73-eeb5-4fb6-9d31-72a889f1811c",
                "modelCustomizationName": "2017-388_PASQUALE-vPE 0",
                "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168"
              },
              "uuid": "afacccf6-397d-45d6-b5ae-94c39734b168"
            },
            "2017-388_PASQUALE-vPE 1": {
              "action": "None",
              "inMaint": false,
              "rollbackOnFailure": "true",
              "originalName": "2017-388_PASQUALE-vPE 1",
              "isMissingData": false,
              "trackById": "cv7l1ak8vpe",
              "vfModules": {},
              "vnfStoreKey": "2017-388_PASQUALE-vPE 1",
              "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
              "lcpCloudRegionId": "AAIAIC25",
              "legacyRegion": "123",
              "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
              "platformName": "platform",
              "lineOfBusiness": "ONAP",
              "instanceParams": [{}],
              "modelInfo": {
                "modelInvariantId": "00beb8f9-6d39-452f-816d-c709b9cbb87d",
                "modelVersionId": "0903e1c0-8e03-4936-b5c2-260653b96413",
                "modelName": "2017-388_PASQUALE-vPE",
                "modelVersion": "1.0",
                "modelCustomizationId": "280dec31-f16d-488b-9668-4aae55d6648a",
                "modelCustomizationName": "2017-388_PASQUALE-vPE 1",
                "uuid": "0903e1c0-8e03-4936-b5c2-260653b96413"
              },
              "uuid": "0903e1c0-8e03-4936-b5c2-260653b96413"
            }
          },
          "instanceParams": [{}],
          "validationCounter": 0,
          "existingNames": {"yoav": ""},
          "existingVNFCounterMap": {
            "69e09f68-8b63-4cc9-b9ff-860960b5db09": 1,
            "afacccf6-397d-45d6-b5ae-94c39734b168": 1,
            "0903e1c0-8e03-4936-b5c2-260653b96413": 1
          },
          "existingVnfGroupCounterMap": {
            "daeb6568-cef8-417f-9075-ed259ce59f48": 0,
            "c2b300e6-45de-4e5e-abda-3032bee2de56": -1
          },
          "existingNetworksCounterMap": {"ddc3f20c-08b5-40fd-af72-c6d14636b986": 1},
          "networks": {
            "ExtVL 0": {
              "inMaint": false,
              "rollbackOnFailure": "true",
              "originalName": "ExtVL 0",
              "isMissingData": false,
              "trackById": "s6okajvv2n8",
              "networkStoreKey": "ExtVL 0",
              "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
              "lcpCloudRegionId": "AAIAIC25",
              "legacyRegion": "12355555",
              "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
              "platformName": "platform",
              "lineOfBusiness": null,
              "instanceParams": [{}],
              "modelInfo": {
                "modelInvariantId": "379f816b-a7aa-422f-be30-17114ff50b7c",
                "modelVersionId": "ddc3f20c-08b5-40fd-af72-c6d14636b986",
                "modelName": "ExtVL",
                "modelVersion": "37.0",
                "modelCustomizationId": "94fdd893-4a36-4d70-b16a-ec29c54c184f",
                "modelCustomizationName": "ExtVL 0",
                "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
              },
              "uuid": "ddc3f20c-08b5-40fd-af72-c6d14636b986"
            }
          },
          "vnfGroups": {
            "groupingservicefortest..ResourceInstanceGroup..0": {
              "inMaint": false,
              "rollbackOnFailure": "true",
              "originalName": "groupingservicefortest..ResourceInstanceGroup..0",
              "isMissingData": false,
              "trackById": "se0obn93qq",
              "vnfGroupStoreKey": "groupingservicefortest..ResourceInstanceGroup..0",
              "instanceName": "groupingservicefortestResourceInstanceGroup0",
              "instanceParams": [{}],
              "modelInfo": {
                "modelInvariantId": "4bb2e27e-ddab-4790-9c6d-1f731bc14a45",
                "modelVersionId": "daeb6568-cef8-417f-9075-ed259ce59f48",
                "modelName": "groupingservicefortest..ResourceInstanceGroup..0",
                "modelVersion": "1",
                "modelCustomizationName": "groupingservicefortest..ResourceInstanceGroup..0",
                "uuid": "daeb6568-cef8-417f-9075-ed259ce59f48"
              },
              "uuid": "daeb6568-cef8-417f-9075-ed259ce59f48"
            }
          },
          "instanceName": "yoav",
          "globalSubscriberId": "e433710f-9217-458d-a79d-1c7aff376d89",
          "subscriptionServiceType": "TYLER SILVIA",
          "owningEntityId": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc",
          "productFamilyId": "d8a6ed93-251c-47ca-adc9-86671fd19f4c",
          "lcpCloudRegionId": "AAIAIC25",
          "tenantId": "092eb9e8e4b7412e8787dd091bc58e86",
          "aicZoneId": "ATL53",
          "pause": null,
          "projectName": "WATKINS",
          "rollbackOnFailure": "true",
          "bulkSize": 1,
          "aicZoneName": "AAIATLTE-ATL53",
          "owningEntityName": "WayneHolland",
          "testApi": "VNF_API",
          "isEcompGeneratedNaming": false,
          "tenantName": "USP-SIP-IC-24335-T-01",
          "modelInfo": {
            "modelInvariantId": "cdb90b57-ed78-4d44-a5b4-7f43a02ec632",
            "modelVersionId": "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
            "modelName": "action-data",
            "modelVersion": "1.0",
            "uuid": "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd"
          },
          "isALaCarte": false,
          "name": "action-data",
          "version": "1.0",
          "description": "PASQUALE vMX vPE based on Juniper 17.2 release. Updated with updated VF for v8.0 of VLM",
          "category": "Network L1-3",
          "uuid": "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
          "invariantUuid": "cdb90b57-ed78-4d44-a5b4-7f43a02ec632",
          "serviceType": "pnf",
          "serviceRole": "Testing",
          "vidNotions": {"instantiationUI": "legacy", "modelCategory": "other", "viewEditUI": "legacy"},
          "isMultiStepDesign": true
        }
      },
      "lcpRegionsAndTenants": {
        "lcpRegionList": [{
          "id": "AAIAIC25",
          "name": "AAIAIC25 (AIC)",
          "isPermitted": true,
          "cloudOwner": "irma-aic"
        }, {"id": "hvf6", "name": "hvf6 (AIC)", "isPermitted": true, "cloudOwner": "irma-aic"}],
        "lcpRegionsTenantsMap": {
          "AAIAIC25": [{
            "id": "092eb9e8e4b7412e8787dd091bc58e86",
            "name": "USP-SIP-IC-24335-T-01",
            "isPermitted": true,
            "cloudOwner": "irma-aic"
          }],
          "hvf6": [{
            "id": "bae71557c5bb4d5aac6743a4e5f1d054",
            "name": "AIN Web Tool-15-D-testalexandria",
            "isPermitted": true,
            "cloudOwner": "irma-aic"
          }, {
            "id": "d0a3e3f2964542259d155a81c41aadc3",
            "name": "test-hvf6-09",
            "isPermitted": true,
            "cloudOwner": "irma-aic"
          }, {
            "id": "fa45ca53c80b492fa8be5477cd84fc2b",
            "name": "ro-T112",
            "isPermitted": true,
            "cloudOwner": "irma-aic"
          }, {
            "id": "cbb99fe4ada84631b7baf046b6fd2044",
            "name": "DN5242-Nov16-T3",
            "isPermitted": true,
            "cloudOwner": "irma-aic"
          }]
        }
      },
      "subscribers": [{
        "id": "ERICA5779-Subscriber-2",
        "name": "ERICA5779-Subscriber-2",
        "isPermitted": false
      }, {
        "id": "ERICA5779-Subscriber-3",
        "name": "ERICA5779-Subscriber-3",
        "isPermitted": false
      }, {
        "id": "ERICA5779-Subscriber-4",
        "name": "ERICA5779-Subscriber-5",
        "isPermitted": false
      }, {
        "id": "ERICA5779-TestSub-PWT-101",
        "name": "ERICA5779-TestSub-PWT-101",
        "isPermitted": false
      }, {
        "id": "ERICA5779-TestSub-PWT-102",
        "name": "ERICA5779-TestSub-PWT-102",
        "isPermitted": false
      }, {
        "id": "ERICA5779-TestSub-PWT-103",
        "name": "ERICA5779-TestSub-PWT-103",
        "isPermitted": false
      }, {
        "id": "31739f3e-526b-11e6-beb8-9e71128cae77",
        "name": "CRAIG/ROBERTS",
        "isPermitted": false
      }, {"id": "DHV1707-TestSubscriber-2", "name": "DALE BRIDGES", "isPermitted": false}, {
        "id": "jimmy-example",
        "name": "JimmyExampleCust-20161102",
        "isPermitted": false
      }, {"id": "jimmy-example2", "name": "JimmyExampleCust-20161103", "isPermitted": false}, {
        "id": "CAR_2020_ER",
        "name": "CAR_2020_ER",
        "isPermitted": true
      }, {
        "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
        "name": "Emanuel",
        "isPermitted": false
      }, {
        "id": "21014aa2-526b-11e6-beb8-9e71128cae77",
        "name": "JULIO ERICKSON",
        "isPermitted": false
      }, {
        "id": "DHV1707-TestSubscriber-1",
        "name": "LLOYD BRIDGES",
        "isPermitted": false
      }, {"id": "e433710f-9217-458d-a79d-1c7aff376d89", "name": "SILVIA ROBBINS", "isPermitted": true}],
      "productFamilies": null,
      "serviceTypes": {
        "e433710f-9217-458d-a79d-1c7aff376d89": [{
          "id": "17",
          "name": "JOHANNA_SANTOS",
          "isPermitted": false
        }, {"id": "16", "name": "LINDSEY", "isPermitted": false}, {
          "id": "2",
          "name": "Emanuel",
          "isPermitted": false
        }, {"id": "5", "name": "Kennedy", "isPermitted": false}, {
          "id": "14",
          "name": "SSD",
          "isPermitted": false
        }, {"id": "1", "name": "TYLER SILVIA", "isPermitted": true}, {
          "id": "12",
          "name": "VPMS",
          "isPermitted": false
        }, {"id": "3", "name": "vJamie", "isPermitted": false}, {
          "id": "0",
          "name": "vRichardson",
          "isPermitted": false
        }, {"id": "18", "name": "vCarroll", "isPermitted": false}, {
          "id": "9",
          "name": "vFLORENCE",
          "isPermitted": false
        }, {"id": "13", "name": "vWINIFRED", "isPermitted": false}, {
          "id": "10",
          "name": "vMNS",
          "isPermitted": false
        }, {"id": "15", "name": "vMOG", "isPermitted": false}, {
          "id": "8",
          "name": "vOTA",
          "isPermitted": false
        }, {"id": "11", "name": "vEsmeralda", "isPermitted": false}, {
          "id": "6",
          "name": "vPorfirio",
          "isPermitted": false
        }, {"id": "7", "name": "vVM", "isPermitted": false}, {"id": "4", "name": "vVoiceMail", "isPermitted": false}]
      },
      "aicZones": [
        {
          "id": "NFT1",
          "name": "NFTJSSSS-NFT1"
        },
        {
          "id": "JAG1",
          "name": "YUDFJULP-JAG1"
        },
        {
          "id": "YYY1",
          "name": "UUUAIAAI-YYY1"
        },
        {
          "id": "AVT1",
          "name": "AVTRFLHD-AVT1"
        },
        {
          "id": "ATL34",
          "name": "ATLSANAI-ATL34"
        }
      ],
      "categoryParameters": {
        "owningEntityList": [{
          "id": "aaa1",
          "name": "aaa1"
        }, {"id": "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "name": "WayneHolland"}, {
          "id": "Melissa",
          "name": "Melissa"
        }],
        "projectList": [{"id": "WATKINS", "name": "WATKINS"}, {"id": "x1", "name": "x1"}, {"id": "yyy1", "name": "yyy1"}],
        "lineOfBusinessList": [{"id": "ONAP", "name": "ONAP"}, {"id": "zzz1", "name": "zzz1"}],
        "platformList": [{"id": "platform", "name": "platform"}, {"id": "xxx1", "name": "xxx1"}]
      },
      "type": "UPDATE_LCP_REGIONS_AND_TENANTS"
    }
  }
}


function getNetworkModelInfoFromHierarchy(){
  return {
    "version": "2.0",
    "customizationUuid":"customization-id-from-hierarchy",
    "uuid": "UUID-from-hierarchy",
    "invariantUuid": "invariantId-from-hierarchy"
  }
}

function getSelectedModelInfo() {
  return {
    "instanceModelInfo": {
      "modelVersion": "5.0",
      "modelCustomizationId": "model-customization-id-from-instance",
      "modelVersionId": "UUID-from-instance",
      "modelInvariantId": "invariantId-from-instance"
    }
  }
}
function getNetworkInstance() {
  return {
    "inMaint": false,
    "instanceId": "NETWORK4_INSTANCE_ID",
    "instanceType": "CONTRAIL30_HIMELGUARD",
  };
}
