import {MembersTableService} from "./members-table.service";
import {TestBed, getTestBed} from "@angular/core/testing";
import {NgRedux} from "@angular-redux/store";
import {CustomTableColumnDefinition} from "./members-table.component";
import {AppState} from "../../../store/reducers";
import {createRelatedVnfMemberInstance} from "../../../storeUtil/utils/relatedVnfMember/relatedVnfMember.actions";
import {DataFilterPipe} from "../../../pipes/dataFilter/data-filter.pipe";
import {VnfMember} from "../../../models/VnfMember";



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
                      "lcpCloudRegionId": "mtn23b",
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

describe('MembersTableService view member count', () => {
  let injector;
  let service: MembersTableService;
  let store: NgRedux<AppState>;
  let data = loadMockMembers();

  beforeAll(done => (async () => {

    TestBed.configureTestingModule(
      {
        providers: [
          MembersTableService,
          {provide: NgRedux, useClass: MockAppStore},
          DataFilterPipe

        ],
        declarations: [DataFilterPipe]
      });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(MembersTableService);
    store = injector.get(NgRedux)

  })().then(done).catch(done.fail));


  test('should return number of displayed members', () => {
    service.allMemberStatusMap = MembersTableService.generateAllMembersStatus(<any>data);
    service.filteredMembers = <any>data;
    expect(service.calculateNotHideVnfMembers()).toEqual(2);
  });

  test('should return number of selected members', () => {
    service.allMemberStatusMap = MembersTableService.generateAllMembersStatus(<any>data);
    service.allMemberStatusMap['VNF1_INSTANCE_ID'].isSelected = true;
    service.allMemberStatusMap['VNF2_INSTANCE_ID'].isSelected = true;
    expect(service.calculateSelectedVnfMembers()).toEqual(2);
  });

  test('should return number of selected members', () => {
    service.allMemberStatusMap = MembersTableService.generateAllMembersStatus(<any>data);
    service.filteredMembers = <any>data;
    service.allMemberStatusMap['VNF1_INSTANCE_ID'].isSelected = true;
    service.filterMembers('VNF2');
    service.allMemberStatusMap['VNF2_INSTANCE_ID'].isSelected = true;
    expect(service.calculateNotHideVnfMembers()).toEqual(1);
  });

  test('getHeader should return labels with array of keys', () => {
    const headers: CustomTableColumnDefinition[] = MembersTableService.getHeaders();
    expect(headers).toEqual([
      {displayName: 'VNF instance name', key: ['instanceName']},
      {displayName: 'VNF version', key: ['modelInfo', 'modelVersion']},
      {displayName: 'VNF model name', key: ['modelInfo', 'modelName']},
      {displayName: 'Prov Status', key: ['provStatus']},
      {displayName: 'Service instance name', key: ['serviceInstanceName']},
      {displayName: 'Cloud Region', key: ['lcpCloudRegionId']},
      {displayName: 'Tenant Name', key: ['tenantName']}
    ]);
  });


  test('setMembers should dispatch action only on selected members', () => {
    const vnfGroupStoreKey: string = 'vnfGroupStoreKey';
    const serviceId: string = 'serviceId';

    jest.spyOn(store, 'dispatch');
    service.allMemberStatusMap = MembersTableService.generateAllMembersStatus(<any>data);
    service.allMemberStatusMap['VNF1_INSTANCE_ID'].isSelected = true;
    service.setMembers({serviceId: serviceId, vnfGroupStoreKey: vnfGroupStoreKey});
    expect(store.dispatch).toHaveBeenCalledTimes(1);
    expect(store.dispatch).toHaveBeenCalledWith(createRelatedVnfMemberInstance(vnfGroupStoreKey, serviceId, service.allMemberStatusMap['VNF1_INSTANCE_ID']));
  });

  test('generateAllMembersStatus should add to each instance isHide and isSelected and convert to map', () => {

    let allMemberStatusMapMock = MembersTableService.generateAllMembersStatus(<any>data);
    for (const key in allMemberStatusMapMock) {
      expect(allMemberStatusMapMock[key].isSelected).toBeFalsy();
    }
  });

  test('changeAllCheckboxStatus', () => {
    let data = loadMockMembers();
    service.allMemberStatusMap = MembersTableService.generateAllMembersStatus(<any>data);
    service.filteredMembers = <any>data;
    service.changeAllCheckboxStatus(true);
    for (let key in service.allMemberStatusMap) {
      expect(service.allMemberStatusMap[key].isSelected).toEqual(true);
    }
  });

  test('should reset all numbers and lists', () => {
    let data = loadMockMembers();
    service.allMemberStatusMap = MembersTableService.generateAllMembersStatus(<any>data);
    service.filteredMembers = <any>data;
    service.changeAllCheckboxStatus(true);
    service.resetAll();
    expect(service.numberOfNotHideVnfMembers).toEqual(0);
    expect(service.numberOfSelectedAndNotHideVnfMembers).toEqual(0);
    expect(service.numberOfSelectedVnfMembers).toEqual(0);
    expect(service.allMemberStatusMap).toEqual({});
    expect(service.filteredMembers.length).toEqual(0);
  });

  test('checkAllCheckboxStatus should be false if not all are selected', () => {
    service.allMemberStatusMap = MembersTableService.generateAllMembersStatus(<any>loadMockMembers());
    service.updateAmountsAndCheckAll();

    expect(service.allCheckboxAreSelected).toEqual(false);
  });


  test('sortVnfMembersByName should sort list by vnf name', () => {
    let data = <any>loadMockMembers();
    let sortedList = MembersTableService.sortVnfMembersByName(data, "instanceName");

    expect(sortedList[0].instanceName).toEqual("VNF1_INSTANCE_NAME");
    expect(sortedList[1].instanceName).toEqual("VNF2_INSTANCE_NAME");

    let tmp = data[0];
    data[0] = data[1];
    data[1] = tmp;

    sortedList = MembersTableService.sortVnfMembersByName(data, "instanceName");

    expect(sortedList[1].instanceName).toEqual("VNF1_INSTANCE_NAME");
    expect(sortedList[0].instanceName).toEqual("VNF2_INSTANCE_NAME");
    sortedList = MembersTableService.sortVnfMembersByName(null, "instanceName");
    expect(sortedList).toEqual([]);
    sortedList = MembersTableService.sortVnfMembersByName(data, undefined);
    expect(sortedList).toEqual([]);
  });

  test('should return only vnf members not associated to any vnf group', ()=>{
    const result: VnfMember[] = service.filterUsedVnfMembers("serviceModelId",loadMockMembers());
    expect(result.length).toEqual(1);
    expect(result[0].instanceId).toEqual("VNF2_INSTANCE_ID");
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
      "lcpCloudRegionId": "mtn23b",
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
      "lcpCloudRegionId": "mtn23b",
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


