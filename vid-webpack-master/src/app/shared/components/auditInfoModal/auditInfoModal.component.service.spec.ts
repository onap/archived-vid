
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {NgRedux} from "@angular-redux/store";
import {AuditInfoModalComponentService} from "./auditInfoModal.component.service";
import {ModelInformationItem} from "../model-information/model-information.component";
import {ServiceInfoService} from "../../server/serviceInfo/serviceInfo.service";
import {FeatureFlagsService} from "../../services/featureFlag/feature-flags.service";
import {AuditInformationItem} from "../../models/auditInfoControlModels/auditInformationItems.model";

class MockAppStore<T> {
  getState() {
    return {
      "service": {
        "serviceInstance": {
          "6e59c5de-f052-46fa-aa7e-2fca9d674c44": {
            "instanceParams": [],
            "bulkSize": 1,
            "action": "None",
            "instanceName": "PQijmEmzhVma4zujr",
            "instanceId": "service-instance-id",
            "orchStatus": "GARBAGE DATA",
            "productFamilyId": null,
            "lcpCloudRegionId": null,
            "tenantId": null,
            "modelInfo": {
              "modelInvariantId": "d27e42cf-087e-4d31-88ac-6c4b7585f800",
              "modelVersionId": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
              "modelName": "vf_vEPDG",
              "modelType": "service",
              "modelVersion": "5.0"
            },
            "globalSubscriberId": "global-customer-id",
            "subscriptionServiceType": "service-instance-type",
            "owningEntityId": null,
            "owningEntityName": null,
            "tenantName": null,
            "aicZoneId": null,
            "aicZoneName": null,
            "projectName": null,
            "rollbackOnFailure": null,
            "isALaCarte": false
          }
        },
        "subscribers": [
          {
            "id": "CAR_2020_ER",
            "name": "CAR_2020_ER",
            "isPermitted": true
          },
          {
            "id": "21014aa2-526b-11e6-beb8-9e71128cae77",
            "name": "JULIO ERICKSON",
            "isPermitted": false
          },
          {
            "id": "global-customer-id",
            "name": "DALE BRIDGES",
            "isPermitted": false
          },
          {
            "id": "DHV1707-TestSubscriber-1",
            "name": "LLOYD BRIDGES",
            "isPermitted": false
          },
          {
            "id": "jimmy-example",
            "name": "JimmyExampleCust-20161102",
            "isPermitted": false
          },
          {
            "id": "jimmy-example2",
            "name": "JimmyExampleCust-20161103",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-102",
            "name": "ERICA5779-TestSub-PWT-102",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-101",
            "name": "ERICA5779-TestSub-PWT-101",
            "isPermitted": false
          },
          {
            "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
            "name": "Emanuel",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-Subscriber-4",
            "name": "ERICA5779-Subscriber-5",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-TestSub-PWT-103",
            "name": "ERICA5779-TestSub-PWT-103",
            "isPermitted": false
          },
          {
            "id": "ERICA5779-Subscriber-2",
            "name": "ERICA5779-Subscriber-2",
            "isPermitted": false
          },
          {
            "id": "e433710f-9217-458d-a79d-1c7aff376d89",
            "name": "SILVIA ROBBINS",
            "isPermitted": true
          },
          {
            "id": "ERICA5779-Subscriber-3",
            "name": "ERICA5779-Subscriber-3",
            "isPermitted": false
          },
          {
            "id": "31739f3e-526b-11e6-beb8-9e71128cae77",
            "name": "CRAIG/ROBERTS",
            "isPermitted": false
          }
        ]
      }
    }
  }
}

describe("Audit info modal component service", () => {
  let injector;
  let httpMock: HttpTestingController;
  let auditInfoModalComponentService: AuditInfoModalComponentService;
  let serviceInfoService: ServiceInfoService;
  let featureFlagsService: FeatureFlagsService;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuditInfoModalComponentService,
        ServiceInfoService,
        FeatureFlagsService,
        {provide: NgRedux, useClass: MockAppStore}
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    httpMock = injector.get(HttpTestingController);
    auditInfoModalComponentService = injector.get(AuditInfoModalComponentService);

  })().then(done).catch(done.fail));


  test('auditInfoModalComponentService service should be defined', () => {
    expect(auditInfoModalComponentService).toBeDefined();
  });

  test('getModelInfo: VNF information from existing instance with model', () => {
    const model = getVNFModel();
    const instance = getVNFInstance();
    const results: ModelInformationItem[] = auditInfoModalComponentService.getModelInfo(model, instance, "6e59c5de-f052-46fa-aa7e-2fca9d674c44");
    expect(results.find((item) => item.label === 'Model customization ID').values[0]).toEqual("91415b44-753d-494c-926a-456a9172bbb9");
    expect(results.find((item) => item.label === 'Model version').values[0]).toEqual("2.0");
    expect(results.find((item) => item.label === 'Model name').values[0]).toEqual("VF_vGeraldine");
    expect(results.find((item) => item.label === 'Instance name').values[0]).toEqual("4O61SmpFAdCm1oVEs");
    expect(results.find((item) => item.label === 'Instance ID').values[0]).toEqual("66cbb3b5-c823-470c-9520-4e0b85112250");
    expect(results.find((item) => item.label === 'Subscriber name').values[0]).toEqual("DALE BRIDGES"); // TODO should call subscriber
    expect(results.find((item) => item.label === 'Service type').values[0]).toEqual("service-instance-type");
  });

  test('getModelInfo: Network information from existing instance without model', () => {
    const model = getNetworkModel();
    const instance = getNetworkInstance();
    const results: ModelInformationItem[] = auditInfoModalComponentService.getModelInfo(model, instance, "6e59c5de-f052-46fa-aa7e-2fca9d674c44");
    expect(results.find((item) => item.label === 'Instance name').values[0]).toEqual("KADmyK6e3sVWNfGbA");
    expect(results.find((item) => item.label === 'Instance ID').values[0]).toEqual("712cad2d-fc1c-40c7-aaff-d2d6e17ac2b6");
    expect(results.find((item) => item.label === 'Subscriber name').values[0]).toEqual("DALE BRIDGES"); // TODO should call subscriber
    expect(results.find((item) => item.label === 'Service type').values[0]).toEqual("service-instance-type");
  });

  test('getModelInfo: VFModule information from existing instance with model', () => {
    const model = getVFModule();
    const instance = getVFModuleInstance();
    const results: ModelInformationItem[] = auditInfoModalComponentService.getModelInfo(model, instance, "6e59c5de-f052-46fa-aa7e-2fca9d674c44");
    expect(results.find((item) => item.label === 'Model customization ID').values[0]).toEqual("55b1be94-671a-403e-a26c-667e9c47d091");
    expect(results.find((item) => item.label === 'Model version').values[0]).toEqual("2");
    expect(results.find((item) => item.label === 'Model name').values[0]).toEqual("VfVgeraldine..vflorence_vlc..module-1");
    expect(results.find((item) => item.label === 'Instance name').values[0]).toEqual("ss820f_0918_db");
    expect(results.find((item) => item.label === 'Instance ID').values[0]).toEqual("2c1ca484-cbc2-408b-ab86-25a2c15ce280");
    expect(results.find((item) => item.label === 'Subscriber name').values[0]).toEqual("DALE BRIDGES"); // TODO should call subscriber
    expect(results.find((item) => item.label === 'Service type').values[0]).toEqual("service-instance-type");
  });

  test('getModelInfo: VNFGroup information from existing instance with model', () => {
    const model = getVNFGroupModel();
    const instance = getVNFGroupInstance();
    const results: ModelInformationItem[] = auditInfoModalComponentService.getModelInfo(model, instance, "6e59c5de-f052-46fa-aa7e-2fca9d674c44");
    expect(results.find((item) => item.label === 'Model version').values[0]).toEqual("1");
    expect(results.find((item) => item.label === 'Model name').values[0]).toEqual("groupingservicefortest..ResourceInstanceGroup..0");
    expect(results.find((item) => item.label === 'Instance name').values[0]).toEqual("VNF_GROUP1_INSTANCE_NAME");
    expect(results.find((item) => item.label === 'Instance ID').values[0]).toEqual("VNF_GROUP1_INSTANCE_ID");
    expect(results.find((item) => item.label === 'Subscriber name').values[0]).toEqual("DALE BRIDGES"); // TODO should call subscriber
    expect(results.find((item) => item.label === 'Service type').values[0]).toEqual("service-instance-type");
  });


  test('setModalTitlesType:  should return modal title', () => {
    expect(AuditInfoModalComponentService.setModalTitle('VNF')).toEqual('VNF Instantiation Information');
    expect(AuditInfoModalComponentService.setModalTitle('VFMODULE')).toEqual('VfModule Instantiation Information');
    expect(AuditInfoModalComponentService.setModalTitle('NETWORK')).toEqual('Network Instantiation Information');
    expect(AuditInfoModalComponentService.setModalTitle('VNFGROUP')).toEqual('Vnf Group Instantiation Information');
  });

  test('setModalTitlesType:  should return modal title type', () => {
    expect(AuditInfoModalComponentService.setModalTitlesType('VNF')).toEqual('VNF');
    expect(AuditInfoModalComponentService.setModalTitlesType('VFMODULE')).toEqual('VfModule');
    expect(AuditInfoModalComponentService.setModalTitlesType('NETWORK')).toEqual('Network');
    expect(AuditInfoModalComponentService.setModalTitlesType('VNFGROUP')).toEqual('Vnf Group');
  });

  test('initAuditInfoItems:  should return AuditInformationItem initialization', () => {
    const auditInfoItems = getAuditInitItems();
    expect(AuditInfoModalComponentService.initAuditInfoItems()).toEqual(auditInfoItems);
  });

  test('createModelInformationItemsJob:  should return job information', () => {
    let service = {
      "uuid": "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
      "invariantUuid": "cfef8302-d90f-475f-87cc-3f49a62ef14c",
      "name": "ComplexService",
      "version": "1.0",
      "toscaModelURL": null,
      "category": "Emanuel",
      "serviceType": "",
      "serviceRole": "",
      "description": "ComplexService",
      "serviceEcompNaming": "true",
      "instantiationType": "Macro",
      "inputs": {},
      "vidNotions": {
        "instantiationUI": "legacy",
        "modelCategory": "other",
        "viewEditUI": "legacy"
      }
    };
    const results = AuditInfoModalComponentService.createModelInformationItemsJob(<any>service);
  });

  test('getInstanceModelName:  should return model name if exist', () => {
    expect(AuditInfoModalComponentService.getInstanceModelName({name : 'some name'})).toEqual('some name');
    expect(AuditInfoModalComponentService.getInstanceModelName(null)).toEqual('');
  });

  function getVNFModel(){
    return {"name":"VF_vGeraldine","version":"2.0","description":"VSP_vGeraldine","uuid":"d6557200-ecf2-4641-8094-5393ae3aae60","invariantUuid":"4160458e-f648-4b30-a176-43881ffffe9e","max":1,"min":0,"customizationUuid":"91415b44-753d-494c-926a-456a9172bbb9","isEcompGeneratedNaming":true,"type":"VF","modelCustomizationName":"VF_vGeraldine 0","vfcInstanceGroups":{},"properties":{"gpb2_Internal2_mac":"00:11:22:EF:AC:DF","sctp-a-ipv6-egress_rule_application":"any","sctp-b-ipv6-egress_src_start_port":"0","Internal2_allow_transit":"true","sctp-b-IPv6_ethertype":"IPv6","ncb2_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-ingress_rule_protocol":"icmp","sctp-b-ingress_action":"pass","sctp-a-egress_rule_application":"any","sctp-b-ipv6-ingress-src_start_port":"0.0","ncb1_Internal2_mac":"00:11:22:EF:AC:DF","sctp-b-egress_src_addresses":"local","fsb_volume_size_0":"320.0","sctp-a-ipv6-ingress-dst_start_port":"0","sctp-a-ipv6-ingress_ethertype":"IPv4","sctp-b-ipv6-ingress_rule_application":"any","domain_name":"default-domain","sctp-a-egress_src_addresses":"local","sctp-b-egress-src_start_port":"0.0","sctp-a-ingress_rule_protocol":"icmp","sctp-b-display_name":"epc-sctp-b-ipv4v6-sec-group","sctp-b-ipv6-ingress-dst_end_port":"65535","sctp-a-ingress_ethertype":"IPv4","sctp-a-egress-src_start_port":"0.0","sctp-b-dst_subnet_prefix_v6":"::","nf_naming":"{ecomp_generated_naming=true}","sctp-a-ipv6-ingress_src_subnet_prefix":"0.0.0.0","sctp-b-egress-dst_start_port":"0.0","ncb_flavor_name":"nv.c20r64d1","sctp-b-egress_dst_subnet_prefix_len":"0.0","gpb1_Internal1_mac":"00:11:22:EF:AC:DF","Internal2_net_cidr":"10.0.0.10","sctp-a-ingress-dst_start_port":"0.0","fsb1_Internal2_mac":"00:11:22:EF:AC:DF","sctp-a-egress-dst_start_port":"0.0","sctp-a-egress_ethertype":"IPv4","vlc_st_service_mode":"in-network-nat","sctp-a-ipv6-egress_ethertype":"IPv4","sctp-a-egress-src_end_port":"65535.0","sctp-b-egress_action":"pass","sctp-b-ipv6-egress_rule_application":"any","sctp-a-ingress-src_subnet_prefix_len":"0.0","sctp-b-ipv6-ingress-src_end_port":"65535.0","sctp-a-ipv6-ingress-src_start_port":"0.0","fsb2_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-name":"epc-sctp-b-ipv4v6-sec-group","sctp-b-ipv6-egress_ethertype":"IPv4","Internal1_net_cidr":"10.0.0.10","sctp-a-egress_dst_subnet_prefix":"0.0.0.0","fsb_flavor_name":"nv.c20r64d1","sctp_rule_protocol":"132","sctp-a-ipv6-ingress_rule_application":"any","sctp-b-ipv6-ingress_src_subnet_prefix_len":"0","ecomp_generated_naming":"true","sctp-a-IPv6_ethertype":"IPv6","vlc_st_virtualization_type":"virtual-machine","vlc2_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-ingress-dst_end_port":"65535.0","sctp-b-ingress-dst_start_port":"0.0","sctp-a-ipv6-ingress-src_end_port":"65535.0","sctp-a-display_name":"epc-sctp-a-ipv4v6-sec-group","sctp-b-ingress_rule_application":"any","vlc_flavor_name":"nd.c16r64d1","int2_sec_group_name":"int2-sec-group","sctp-b-ipv6-egress_src_addresses":"local","vlc_st_interface_type_int1":"other1","vlc_st_interface_type_int2":"other2","sctp-a-ipv6-egress-dst_start_port":"0","sctp-b-egress-src_end_port":"65535.0","sctp-a-ipv6-egress_dst_subnet_prefix_len":"0","Internal2_shared":"false","sctp-a-ipv6-egress_rule_protocol":"any","Internal2_rpf":"disable","vlc1_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-ipv6-egress_src_end_port":"65535","sctp-a-ipv6-egress_src_addresses":"local","sctp-a-ingress-dst_end_port":"65535.0","sctp-a-ipv6-egress_src_end_port":"65535","Internal1_forwarding_mode":"l2","Internal2_dhcp":"false","sctp-a-dst_subnet_prefix_v6":"::","pxe_image_name":"MME_PXE-Boot_16ACP04_GA.qcow2","vlc_st_interface_type_gtp":"other0","ncb1_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-src_subnet_prefix_v6":"::","sctp-a-egress_dst_subnet_prefix_len":"0.0","int1_sec_group_name":"int1-sec-group","Internal1_dhcp":"false","fsb2_Internal2_mac":"00:11:22:EF:AC:DF","Internal2_forwarding_mode":"l2","sctp-a-ipv6-egress_dst_end_port":"65535","sctp-b-egress_dst_subnet_prefix":"0.0.0.0","Internal1_net_cidr_len":"17","gpb2_Internal1_mac":"00:11:22:EF:AC:DF","sctp-a-ingress_dst_addresses":"local","sctp-b-ingress-src_subnet_prefix_len":"0.0","sctp-a-egress_action":"pass","fsb_volume_type_0":"SF-Default-SSD","ncb2_Internal2_mac":"00:11:22:EF:AC:DF","vlc_st_interface_type_sctp_a":"left","vlc_st_version":"2","sctp-a-src_subnet_prefix_v6":"::","vlc_st_interface_type_sctp_b":"right","sctp-a-ingress_rule_application":"any","sctp-b-egress_ethertype":"IPv4","sctp-a-ipv6-egress_src_start_port":"0","instance_ip_family_v6":"v6","gpb1_Internal2_mac":"00:11:22:EF:AC:DF","sctp-b-ingress-src_start_port":"0.0","fsb1_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-ingress_dst_addresses":"local","vlc_st_interface_type_oam":"management","multi_stage_design":"false","oam_sec_group_name":"oam-sec-group","Internal2_net_gateway":"10.0.0.10","sctp-a-ipv6-ingress-dst_end_port":"65535","Internal1_net_gateway":"10.0.0.10","sctp-b-ipv6-egress-dst_start_port":"0","sctp-b-ipv6-egress_rule_protocol":"any","gtp_sec_group_name":"gtp-sec-group","sctp-a-ipv6-egress_dst_subnet_prefix":"0.0.0.0","sctp-a-ipv6-ingress_dst_addresses":"local","sctp-b-ipv6-egress_dst_subnet_prefix_len":"0","sctp-b-ipv6-egress_action":"pass","sctp-a-egress_rule_protocol":"icmp","sctp-a-ipv6-egress_action":"pass","Internal1_shared":"false","sctp-b-ipv6-ingress_rule_protocol":"any","Internal2_net_cidr_len":"17","sctp-a-name":"epc-sctp-a-ipv4v6-sec-group","sctp-a-ingress-src_end_port":"65535.0","sctp-b-ipv6-ingress_src_subnet_prefix":"0.0.0.0","sctp-a-egress-dst_end_port":"65535.0","sctp-b-egress_rule_protocol":"icmp","sctp-a-ingress_action":"pass","sctp-b-ipv6-ingress_action":"pass","vlc_st_service_type":"firewall","sctp-b-ipv6-egress_dst_end_port":"65535","vlc2_Internal2_mac":"00:11:22:EF:AC:DF","sctp-b-ipv6-ingress-dst_start_port":"0","vlc_st_availability_zone":"true","sctp-b-ingress-src_subnet_prefix":"0.0.0.0","fsb_volume_image_name_1":"MME_FSB2_16ACP04_GA.qcow2","sctp-a-ipv6-ingress_src_subnet_prefix_len":"0","gpb_flavor_name":"nv.c20r64d1","Internal1_allow_transit":"true","availability_zone_max_count":"1","fsb_volume_image_name_0":"MME_FSB1_16ACP04_GA.qcow2","sctp-b-ipv6-ingress_dst_addresses":"local","sctp-b-ipv6-ingress_ethertype":"IPv4","sctp-b-ipv6-egress_dst_subnet_prefix":"0.0.0.0","sctp-a-ingress-src_subnet_prefix":"0.0.0.0","vlc1_Internal2_mac":"00:11:22:EF:AC:DF","sctp-a-ipv6-ingress_action":"pass","Internal1_rpf":"disable","sctp-b-ingress_ethertype":"IPv4","sctp-b-ingress-src_end_port":"65535.0","sctp-b-egress_rule_application":"any","sctp-a-ipv6-ingress_rule_protocol":"any","sctp-a-ingress-src_start_port":"0.0","sctp-b-egress-dst_end_port":"65535.0"}};
  }

  function getVNFInstance(){
    return {"action":"None","instanceName":"4O61SmpFAdCm1oVEs","instanceId":"66cbb3b5-c823-470c-9520-4e0b85112250","orchStatus":null,"productFamilyId":null,"lcpCloudRegionId":null,"tenantId":null,"modelInfo":{"modelCustomizationName":"VF_vGeraldine 0","modelInvariantId":"vnf-instance-model-invariant-id","modelVersionId":"d6557200-ecf2-4641-8094-5393ae3aae60","modelType":"vnf"},"instanceType":"7538ifdSoTccmbEkr","provStatus":null,"inMaint":true,"uuid":"d6557200-ecf2-4641-8094-5393ae3aae60","originalName":"VF_vGeraldine 0","legacyRegion":null,"lineOfBusiness":null,"platformName":null,"trackById":"VF_vGeraldine 0:004","vfModules":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1:008":{"action":"None","instanceName":"ss820f_0918_db","instanceId":"2c1ca484-cbc2-408b-ab86-25a2c15ce280","orchStatus":"deleted","productFamilyId":null,"lcpCloudRegionId":null,"tenantId":null,"modelInfo":{"modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1","modelCustomizationId":"b200727a-1bf9-4e7c-bd06-b5f4c9d920b9","modelInvariantId":"09edc9ef-85d0-4b26-80de-1f569d49e750","modelVersionId":"522159d5-d6e0-4c2a-aa44-5a542a12a830","modelType":"vfModule"},"instanceType":null,"provStatus":null,"inMaint":true,"uuid":"522159d5-d6e0-4c2a-aa44-5a542a12a830","originalName":"VfVgeraldine..vflorence_vlc..module-1","legacyRegion":null,"lineOfBusiness":null,"platformName":null,"trackById":"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1:008","isBase":false,"volumeGroupName":null}},"dc229cd8-c132-4455-8517-5c1787c18b14":{"dc229cd8-c132-4455-8517-5c1787c18b14:009":{"action":"None","instanceName":"ss820f_0918_base","instanceId":"3ef042c4-259f-45e0-9aba-0989bd8d1cc5","orchStatus":"Assigned","productFamilyId":null,"lcpCloudRegionId":null,"tenantId":null,"modelInfo":{"modelCustomizationId":"8ad8670b-0541-4499-8101-275bbd0e8b6a","modelInvariantId":"1e463c9c-404d-4056-ba56-28fd102608de","modelVersionId":"dc229cd8-c132-4455-8517-5c1787c18b14","modelType":"vfModule"},"instanceType":null,"provStatus":null,"inMaint":false,"uuid":"dc229cd8-c132-4455-8517-5c1787c18b14","originalName":null,"legacyRegion":null,"lineOfBusiness":null,"platformName":null,"trackById":"dc229cd8-c132-4455-8517-5c1787c18b14:009","isBase":true,"volumeGroupName":null}}},"networks":{}};
  }

  function getVFModule(){
    return {"uuid":"522159d5-d6e0-4c2a-aa44-5a542a12a830","invariantUuid":"98a7c88b-b577-476a-90e4-e25a5871e02b","customizationUuid":"55b1be94-671a-403e-a26c-667e9c47d091","description":null,"name":"VfVgeraldine..vflorence_vlc..module-1","version":"2","modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1","properties":{"minCountInstances":0,"maxCountInstances":null,"initialCount":0,"vfModuleLabel":"vflorence_vlc","baseModule":false},"inputs":{},"volumeGroupAllowed":false};
  }

  function getVFModuleInstance() {
    return{"action":"None","instanceName":"ss820f_0918_db","instanceId":"2c1ca484-cbc2-408b-ab86-25a2c15ce280","orchStatus":"deleted","productFamilyId":null,"lcpCloudRegionId":null,"tenantId":null,"modelInfo":{"modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1","modelCustomizationId":"b200727a-1bf9-4e7c-bd06-b5f4c9d920b9","modelInvariantId":"09edc9ef-85d0-4b26-80de-1f569d49e750","modelVersionId":"522159d5-d6e0-4c2a-aa44-5a542a12a830","modelType":"vfModule"},"instanceType":null,"provStatus":null,"inMaint":true,"uuid":"522159d5-d6e0-4c2a-aa44-5a542a12a830","originalName":"VfVgeraldine..vflorence_vlc..module-1","legacyRegion":null,"lineOfBusiness":null,"platformName":null,"trackById":"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1:008","isBase":false,"volumeGroupName":null};
  }

  function getNetworkModel(){
    return null;
  }

  function getNetworkInstance(){
      return {"action":"None","instanceName":"KADmyK6e3sVWNfGbA","instanceId":"712cad2d-fc1c-40c7-aaff-d2d6e17ac2b6","orchStatus":"Assigned","productFamilyId":null,"lcpCloudRegionId":null,"tenantId":null,"modelInfo":{"modelInvariantId":"network-instance-model-invariant-id","modelVersionId":"ddc3f20c-08b5-40fd-af72-c6d14636b986","modelType":"network"},"instanceType":"CONTRAIL30_BASIC","provStatus":"nvtprov","inMaint":false,"uuid":"ddc3f20c-08b5-40fd-af72-c6d14636b986","originalName":null,"legacyRegion":null,"lineOfBusiness":null,"platformName":null,"trackById":"ddc3f20c-08b5-40fd-af72-c6d14636b986:001"};
  }

  function getVNFGroupModel(){
    return {"name":"groupingservicefortest..ResourceInstanceGroup..0","version":"1","uuid":"daeb6568-cef8-417f-9075-ed259ce59f48","invariantUuid":"4bb2e27e-ddab-4790-9c6d-1f731bc14a45","max":1,"min":0,"isEcompGeneratedNaming":true,"type":"VnfGroup","modelCustomizationName":"groupingservicefortest..ResourceInstanceGroup..0","properties":{"contained_resource_type":"VF","role":"SERVICE-ACCESS","function":"DATA","description":"DDD0","type":"LOAD-GROUP","ecomp_generated_naming":"true"},"members":{"vdorothea_svc_vprs_proxy 0":{"uuid":"65fadfa8-a0d9-443f-95ad-836cd044e26c","invariantUuid":"f4baae0c-b3a5-4ca1-a777-afbffe7010bc","description":"A Proxy for Service vDOROTHEA_Svc_vPRS","name":"vDOROTHEA_Svc_vPRS Service Proxy","version":"1.0","customizationUuid":"bdb63d23-e132-4ce7-af2c-a493b4cafac9","inputs":{},"commands":{},"properties":{"ecomp_generated_naming":"false"},"type":"Service Proxy","sourceModelUuid":"da7827a2-366d-4be6-8c68-a69153c61274","sourceModelInvariant":"24632e6b-584b-4f45-80d4-fefd75fd9f14","sourceModelName":"vDOROTHEA_Svc_vPRS"}}};
  }

  function getVNFGroupInstance(){
    return {"originalName":"groupingservicefortest..ResourceInstanceGroup..0","trackById":"groupingservicefortest..ResourceInstanceGroup..0:001","instanceName":"VNF_GROUP1_INSTANCE_NAME","action":"None","instanceId":"VNF_GROUP1_INSTANCE_ID","instanceType":"VNF_GROUP1_INSTANCE_TYPE","orchStatus":"Active","provStatus":null,"inMaint":false,"modelInfo":{"modelType":"instanceGroup","modelCustomizationName":"groupingservicefortest..ResourceInstanceGroup..0","modelInvariantId":"4bb2e27e-ddab-4790-9c6d-1f731bc14a45","modelVersionId":"daeb6568-cef8-417f-9075-ed259ce59f48"},"uuid":"daeb6568-cef8-417f-9075-ed259ce59f48","vnfs":{}};
  }

  function getAuditInitItems() {
    return new AuditInformationItem(true, false, [], [], true, false, false, null , null)
  }
});
