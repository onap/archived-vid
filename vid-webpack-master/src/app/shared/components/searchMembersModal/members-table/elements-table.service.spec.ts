import {ElementsTableService} from "./elements-table.service";
import {TestBed, getTestBed} from "@angular/core/testing";
import {NgRedux} from "@angular-redux/store";
import {CustomTableColumnDefinition} from "./elements-table.component";
import {AppState} from "../../../store/reducers";
import {DataFilterPipe} from "../../../pipes/dataFilter/data-filter.pipe";



class MockAppStore<T> {
  dispatch() {
  }
  getState() {
    return {
      service : {
        serviceHierarchy: {
        },
        serviceInstance : {
          "serviceModelId" : {
            vnfGroups:{
              "aa1":{
                vnfs:{
                   "VNF1_INSTANCE_ID":{
                      "action": "None",
                      "instanceName": "VNF1_INSTANCE_NAME",
                      "instanceId": "VNF1_INSTANCE_ID",
                      "orchStatus": null,
                      "lcpCloudRegionId": "hvf23b",
                      "tenantId": "3e9a20a3e89e45f884e09df0cc2d2d2a",
                      "tenantName": "APPC-24595-T-IST-02C",
                      "modelInfo": {
                      "modelInvariantId": "vnf-instance-model-invariant-id",
                        "modelVersionId": "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
                        "modelVersion": "2.0",
                        "modelName": "vf_vEPDG",
                        "modelType": "vnf"
                    },
                      "instanceType": "VNF1_INSTANCE_TYPE",
                      "provStatus": null,
                      "inMaint": false,
                      "uuid": "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
                      "trackById": "7a6ee536-f052-46fa-aa7e-2fca9d674c44:002",
                      "serviceInstanceId": "service-instance-id1",
                      "serviceInstanceName": "service-instance-name"

                  },
                  "aa1-vnf1":{
                    vnfName: "",
                    instanceId:"",
                    serviceInstanceId:""
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

describe('ElementsTableService view member count', () => {
  let injector;
  let service: ElementsTableService;
  let store: NgRedux<AppState>;
  let data = loadMockMembers();

  beforeAll(done => (async () => {

    TestBed.configureTestingModule(
      {
        providers: [
          ElementsTableService,
          {provide: NgRedux, useClass: MockAppStore},
          DataFilterPipe

        ],
        declarations: [DataFilterPipe]
      });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(ElementsTableService);
    store = injector.get(NgRedux)

  })().then(done).catch(done.fail));


  test('should return number of displayed members', () => {
    service.modalInformation = <any>{
      uniqObjectField : "instanceId"
    };
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>data);
    service.filteredMembers = <any>data;
    expect(service.calculateNotHideRows()).toEqual(2);
  });

  test('should return number of selected members', () => {
    ElementsTableService.uniqObjectField = "instanceId";
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>data);
    service.allElementsStatusMap['VNF1_INSTANCE_ID'].isSelected = true;
    service.allElementsStatusMap['VNF2_INSTANCE_ID'].isSelected = true;
    expect(service.calculateSelectedRows()).toEqual(2);
  });

  test('should return number of selected members', () => {
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>data);
    service.filteredMembers = <any>data;
    service.allElementsStatusMap['VNF1_INSTANCE_ID'].isSelected = true;
    service.filterMembers('VNF2', "VNF");
    service.allElementsStatusMap['VNF2_INSTANCE_ID'].isSelected = true;
    expect(service.calculateNotHideRows()).toEqual(1);
  });

  test('generateAllMembersStatus should add to each instance isHide and isSelected and convert to map', () => {

    let allMemberStatusMapMock = service.generateAllMembersStatus(<any>data);
    for (const key in allMemberStatusMapMock) {
      expect(allMemberStatusMapMock[key].isSelected).toBeFalsy();
    }
  });

  test('changeAllCheckboxStatus', () => {
    service.modalInformation = <any>{
      type : 'SomeType',
      uniqObjectField : 'instanceId'
    };
    let data = loadMockMembers();
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>data);
    service.filteredMembers = <any>data;
    service.changeAllCheckboxStatus(true);
    for (let key in service.allElementsStatusMap) {
      expect(service.allElementsStatusMap[key].isSelected).toEqual(true);
    }
  });

  test('should reset all numbers and lists', () => {
    service.modalInformation = <any>{
      type : 'SomeType',
      uniqObjectField : 'instanceId'
    };
    let data = loadMockMembers();
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>data);
    service.filteredMembers = <any>data;
    service.changeAllCheckboxStatus(true);
    service.resetAll("instanceId");
    expect(service.numberOfNotHideRows).toEqual(0);
    expect(service.numberOfSelectedAndNotHideRows).toEqual(0);
    expect(service.numberOfSelectedRows).toEqual(0);
    expect(service.allElementsStatusMap).toEqual({});
    expect(service.filteredMembers.length).toEqual(0);
  });

  test('checkAllCheckboxStatus should be false if not all are selected', () => {
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>loadMockMembers());
    service.updateAmountsAndCheckAll("instanceId", <any>{});

    expect(service.allCheckboxAreSelected).toEqual(false);
  });


  test('sortVnfMembersByName should sort list by vnf name', () => {
    let data = <any>loadMockMembers();
    let sortedList = service.sortElementsByName(data, "instanceName");

    expect(sortedList[0].instanceName).toEqual("VNF1_INSTANCE_NAME");
    expect(sortedList[1].instanceName).toEqual("VNF2_INSTANCE_NAME");

    let tmp = data[0];
    data[0] = data[1];
    data[1] = tmp;

    sortedList = service.sortElementsByName(data, "instanceName");

    expect(sortedList[1].instanceName).toEqual("VNF1_INSTANCE_NAME");
    expect(sortedList[0].instanceName).toEqual("VNF2_INSTANCE_NAME");
    sortedList = service.sortElementsByName(null, "instanceName");
    expect(sortedList).toEqual([]);
    sortedList = service.sortElementsByName(data, undefined);
    expect(sortedList).toEqual([]);
  });

  test('isRowDisabled should return false current row is selected', ()=> {
    let isDisabled = service.isRowDisabled(true, null);
    expect(isDisabled).toBeFalsy();
  });


  test('isRowDisabled should return false if there is no limit', ()=> {
    let isDisabled = service.isRowDisabled(false, null);
    expect(isDisabled).toBeFalsy();
  });

  test('isRowDisabled should return false if number of rows are less then limit ', ()=> {
    service.modalInformation = <any>{
      uniqObjectField : "instanceId"
    };
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>data);
    service.allElementsStatusMap['VNF1_INSTANCE_ID'].isSelected = true;
    service.allElementsStatusMap['VNF2_INSTANCE_ID'].isSelected = true;

    let isDisabled = service.isRowDisabled(false, 3);
    expect(isDisabled).toBeFalsy();
  });

  test('isRowDisabled should return true if number of rows are equal or more then limit ', ()=> {
    ElementsTableService.uniqObjectField = "instanceId";
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>data);
    service.allElementsStatusMap['VNF1_INSTANCE_ID'].isSelected = true;
    service.allElementsStatusMap['VNF2_INSTANCE_ID'].isSelected = true;

    let isDisabled = service.isRowDisabled(false, 2);
    expect(isDisabled).toBeTruthy();
  });


  test('isCheckAllDisabled should false true if number of rows are equal or more then limit ', ()=> {
    service.modalInformation = <any>{
      uniqObjectField : "instanceId"
    };
    service.allElementsStatusMap = service.generateAllMembersStatus(<any>data);
    service.allElementsStatusMap['VNF1_INSTANCE_ID'].isSelected = true;
    service.allElementsStatusMap['VNF2_INSTANCE_ID'].isSelected = true;

    let isDisabled = service.isCheckAllDisabled( 2);
    expect(isDisabled).toBeFalsy();
  });


});


function loadMockMembers(): any[] {
  return [
    {
      "action": "None",
      "instanceName": "VNF1_INSTANCE_NAME",
      "instanceId": "VNF1_INSTANCE_ID",
      "orchStatus": null,
      "productFamilyId": null,
      "lcpCloudRegionId": "hvf23b",
      "tenantId": "3e9a20a3e89e45f884e09df0cc2d2d2a",
      "tenantName": "APPC-24595-T-IST-02C",
      "modelInfo": {
        "modelInvariantId": "vnf-instance-model-invariant-id",
        "modelVersionId": "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
        "modelVersion": "2.0",
        "modelName": "vf_vEPDG",
        "modelType": "vnf"
      },
      "instanceType": "VNF1_INSTANCE_TYPE",
      "provStatus": null,
      "inMaint": false,
      "uuid": "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
      "originalName": null,
      "legacyRegion": null,
      "lineOfBusiness": null,
      "platformName": null,
      "trackById": "7a6ee536-f052-46fa-aa7e-2fca9d674c44:002",
      "serviceInstanceId": "service-instance-id1",
      "serviceInstanceName": "service-instance-name"
    },
    {
      "action": "None",
      "instanceName": "VNF2_INSTANCE_NAME",
      "instanceId": "VNF2_INSTANCE_ID",
      "orchStatus": null,
      "productFamilyId": null,
      "lcpCloudRegionId": "hvf23b",
      "tenantId": "3e9a20a3e89e45f884e09df0cc2d2d2a",
      "tenantName": "APPC-24595-T-IST-02C",
      "modelInfo": {
        "modelInvariantId": "vnf-instance-model-invariant-id",
        "modelVersionId": "eb5f56bf-5855-4e61-bd00-3e19a953bf02",
        "modelVersion": "1.0",
        "modelName": "vf_vEPDG",
        "modelType": "vnf"
      },
      "instanceType": "VNF2_INSTANCE_TYPE",
      "provStatus": null,
      "inMaint": true,
      "uuid": "eb5f56bf-5855-4e61-bd00-3e19a953bf02",
      "originalName": null,
      "legacyRegion": null,
      "lineOfBusiness": null,
      "platformName": null,
      "trackById": "eb5f56bf-5855-4e61-bd00-3e19a953bf02:003",
      "serviceInstanceId": "service-instance-id2",
      "serviceInstanceName": "service-instance-name"
    }
  ];
}


