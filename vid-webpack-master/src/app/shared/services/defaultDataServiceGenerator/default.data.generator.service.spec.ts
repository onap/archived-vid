import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NgRedux} from '@angular-redux/store';
import {DefaultDataGeneratorService} from './default.data.generator.service';
import {ServiceNodeTypes} from "../../models/ServiceNodeTypes";
import {VNFModel} from "../../models/vnfModel";

class MockAppStore<T> {}

describe('Default Data Generator Service', () => {
  let injector;
  let service: DefaultDataGeneratorService;
  let httpMock: HttpTestingController;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
          providers: [DefaultDataGeneratorService,
            {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(DefaultDataGeneratorService);
    httpMock = injector.get(HttpTestingController);
  })().then(done).catch(done.fail));

  test('generateVFModule aLaCarte vf module object should missed data', () => {
    const serviceHierarchy = generateServiceHierarchy();
    const vnfUUID: string = 'VF_vGeraldine 0';
    const vnfModuleUUID: string = 'vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0';

    let result = service.generateVFModule(serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID], [], false, true);
    expect(result.isMissingData).toBeTruthy();
    expect(result.rollbackOnFailure).toBeTruthy();
  });

  test('generateVFModule should create vf module object', () => {
    const serviceHierarchy = generateServiceHierarchy();
    const vnfUUID: string = 'VF_vGeraldine 0';
    const vnfModuleUUID: string = 'vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0';

    let result = service.generateVFModule(serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID], [], false, false);

    expect(result.modelInfo.modelType).toEqual('VFmodule');
    expect(result.modelInfo.modelInvariantId).toEqual(serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].invariantUuid);
    expect(result.modelInfo.modelVersionId).toEqual(serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].uuid);
    expect(result.modelInfo.modelName).toEqual(serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].name);
    expect(result.modelInfo.modelVersion).toEqual(serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].version);
    expect(result.modelInfo.modelCustomizationId).toEqual(serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].customizationUuid);
    expect(result.modelInfo.modelCustomizationName).toEqual(serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].modelCustomizationName);
    expect(result.sdncPreReload).toBeNull();
    expect(result.isMissingData).toBeTruthy();
    expect(result.instanceParams).toEqual([{}]);
    expect(result.rollbackOnFailure).toBeNull();
  });

  test('generateVNFData should create vnf object', () => {
    const serviceHierarchy = generateServiceHierarchy();
    const vnfName: string = 'VF_vGeraldine 0';
    const formValues = generateVNFFormValues();

    let result = service.generateVNFData(serviceHierarchy, vnfName, formValues, false);

    expect(result.productFamilyId).toEqual(formValues.productFamilyId);
    expect(result.lcpCloudRegionId).toBeNull();
    expect(result.tenantId).toBeNull();
    expect(result.lineOfBusiness).toBeNull();
    expect(result.platformName).toBeNull();
    expect(result.modelInfo.modelType).toEqual('VF');
    expect(result.modelInfo.modelInvariantId).toEqual(serviceHierarchy.vnfs[vnfName].invariantUuid);
    expect(result.modelInfo.modelVersionId).toEqual(serviceHierarchy.vnfs[vnfName].uuid);
    expect(result.modelInfo.modelName).toEqual(serviceHierarchy.vnfs[vnfName].name);
    expect(result.modelInfo.modelVersion).toEqual(serviceHierarchy.vnfs[vnfName].version);
    expect(result.modelInfo.modelCustomizationId).toEqual(serviceHierarchy.vnfs[vnfName].customizationUuid);
    expect(result.modelInfo.modelUniqueId).toEqual(serviceHierarchy.vnfs[vnfName].customizationUuid);
    expect(result.modelInfo.modelCustomizationName).toEqual(serviceHierarchy.vnfs[vnfName].modelCustomizationName);
    expect(result.isMissingData).toBeTruthy();
  });

  describe('#updateDynamicInputsVnfDataFromModel', () => {
    test('get vfModule instance params', () => {
      let dynamicInputs = service.updateDynamicInputsVnfDataFromModel(ServiceNodeTypes.VFmodule, generateVFModule());
      expect(dynamicInputs).toEqual([{
        id: '2017488_pasqualevpe0_vnf_config_template_version',
        type: 'string',
        name: '2017488_pasqualevpe0_vnf_config_template_version',
        value: '17.2',
        isRequired: true,
        description: 'VPE Software Version'
      }, {
        id: '2017488_pasqualevpe0_AIC_CLLI',
        type: 'string',
        name: '2017488_pasqualevpe0_AIC_CLLI',
        value: 'ATLMY8GA',
        isRequired: true,
        description: 'AIC Site CLLI'
      }]);

      /*get vfModule with no instance params should return empty array*/
      dynamicInputs = service.updateDynamicInputsVnfDataFromModel(ServiceNodeTypes.VFmodule, generateVFModule2);
      expect(dynamicInputs).toEqual([]);

      /*get vf instance params should be undefined*/
      dynamicInputs = service.updateDynamicInputsVnfDataFromModel(ServiceNodeTypes.VF, generateVNF());
      expect(dynamicInputs).toEqual([]);
    });
  });

  describe('#createNewVfModuleTreeNode', () => {
    test('createNewVfModuleTreeNode with isEcompGeneratedNaming instance name not fill - missing data true', () => {
      const vnfModuleUUID: string = 'vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0';
      const vfModuleModel = generateServiceHierarchy().vnfs['VF_vGeraldine 0'].vfModules['vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0'];
      const newVfModule = service.createNewVfModuleTreeNode(<any>{
        instanceName: "",
        instanceParams: {},
        modelInfo: {
          "modelCustomizationName": "VF_vGeraldine 0",
          "modelName": "VF_vGeraldine 0",
          "modelCustomizationId": "91415b44-753d-494c-926a-456a9172bbb9",
          "modelInvariantId": "4160458e-f648-4b30-a176-43881ffffe9e",
          "modelVersionId": "d6557200-ecf2-4641-8094-5393ae3aae60",
          "modelType": "vnf",
          "modelVersion": "1"
        },
        volumeGroupName: "",
        isMissingData : false,
        trackById: Math.random().toString()
      }, vfModuleModel, vnfModuleUUID, false, [], "");
      expect(newVfModule.name).toEqual('&lt;Automatically Assigned&gt;');
      expect(newVfModule.missingData).toEqual(true);
    });

    test('createNewVfModuleTreeNode without isEcompGeneratedNaming missing data false', () => {
      const vnfModuleUUID: string = 'vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0';
      const vfModuleModel = generateServiceHierarchy().vnfs['VF_vGeraldine 0'].vfModules['vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0'];
      const newVfModule = service.createNewVfModuleTreeNode(<any>{
        instanceName: "",
        instanceParams: {},
        modelInfo: {
          "modelCustomizationName": "VF_vGeraldine 0",
          "modelName": "VF_vGeraldine 0",
          "modelCustomizationId": "91415b44-753d-494c-926a-456a9172bbb9",
          "modelInvariantId": "4160458e-f648-4b30-a176-43881ffffe9e",
          "modelVersionId": "d6557200-ecf2-4641-8094-5393ae3aae60",
          "modelType": "vnf",
          "modelVersion": "1"
        },
        volumeGroupName: "",
        isMissingData : false,
        trackById: Math.random().toString()
      }, vfModuleModel, vnfModuleUUID, true, [], "");
      expect(newVfModule.name).toEqual('&lt;Automatically Assigned&gt;');
      expect(newVfModule.missingData).toEqual(false);
    });
  });

  describe('#createNewVnfTreeNode', () => {
    test('createNewVnfTreeNode with isEcompGeneratedNaming instance name not filled - missing data true', () => {
      const vnfModel = generateServiceHierarchy().vnfs['VF_vGeraldine 0'];
      const newVnf = service.createNewTreeNode({
        uuid : '',
        instanceName: "",
        productFamilyId: "productFamilyId",
        lcpCloudRegionId: "lcpCloudRegionId",
        legacyRegion: "legacyRegion",
        tenantId: "tenantId",
        platformName: "platformName",
        lineOfBusiness: "lineOfBusiness",
        rollbackOnFailure: "rollbackOnFailure",
        originalName : null,
        vfModules: {},
        modelInfo: {
          "modelCustomizationName": "VF_vGeraldine 0",
          "modelName": "VF_vGeraldine 0",
          "modelCustomizationId": "91415b44-753d-494c-926a-456a9172bbb9",
          "modelInvariantId": "4160458e-f648-4b30-a176-43881ffffe9e",
          "modelVersionId": "d6557200-ecf2-4641-8094-5393ae3aae60",
          "modelType": "vnf",
          "modelVersion": "1"
        },
        isMissingData: false,
        trackById: Math.random().toString(),
        vnfStoreKey: "abc"
      }, new VNFModel(vnfModel),'VF_vGeraldine 0', 'vnfs');
      expect(newVnf.name).toEqual('VF_vGeraldine 0');
      expect(newVnf.missingData).toEqual(true);
    });

    test('createNewVnfTreeNode with isEcompGeneratedNaming instance name filled - missing data false', () => {
      const vnfModel = generateServiceHierarchy().vnfs['VF_vGeraldine 0'];
      const newVnf = service.createNewTreeNode({
        uuid : '',
        instanceName: "instanceName",
        productFamilyId: "productFamilyId",
        lcpCloudRegionId: "lcpCloudRegionId",
        legacyRegion: "legacyRegion",
        tenantId: "tenantId",
        platformName: "platformName",
        lineOfBusiness: "lineOfBusiness",
        rollbackOnFailure: "rollbackOnFailure",
        originalName : null,
        vfModules: {},
        modelInfo: {
          "modelCustomizationName": "VF_vGeraldine 0",
          "modelName": "VF_vGeraldine 0",
          "modelCustomizationId": "91415b44-753d-494c-926a-456a9172bbb9",
          "modelInvariantId": "4160458e-f648-4b30-a176-43881ffffe9e",
          "modelVersionId": "d6557200-ecf2-4641-8094-5393ae3aae60",
          "modelType": "vnf",
          "modelVersion": "1"
        },
        isMissingData: false,
        trackById: Math.random().toString(),
        vnfStoreKey: "abc"
      }, vnfModel,'VF_vGeraldine 0', 'vnfs');
      expect(newVnf.name).toEqual("instanceName");
      expect(newVnf.missingData).toEqual(false);
    });
  });

});


function generateServiceHierarchy() {
  return JSON.parse('{"service":{"uuid":"6e59c5de-f052-46fa-aa7e-2fca9d674c44","invariantUuid":"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0","name":"ComplexService","version":"1.0","toscaModelURL":null,"category":"Emanuel","serviceType":"","serviceRole":"","description":"ComplexService","serviceEcompNaming":"true","instantiationType":"Macro","inputs":{}},"vnfs":{"VF_vGeraldine 0":{"uuid":"d6557200-ecf2-4641-8094-5393ae3aae60","invariantUuid":"4160458e-f648-4b30-a176-43881ffffe9e","description":"VSP_vGeraldine","name":"VF_vGeraldine","version":"2.0","customizationUuid":"91415b44-753d-494c-926a-456a9172bbb9","inputs":{},"commands":{},"properties":{"gpb2_Internal2_mac":"00:11:22:EF:AC:DF","sctp-b-ipv6-egress_src_start_port":"0","sctp-a-ipv6-egress_rule_application":"any","Internal2_allow_transit":"true","sctp-b-IPv6_ethertype":"IPv6","sctp-a-egress_rule_application":"any","sctp-b-ingress_action":"pass","sctp-b-ingress_rule_protocol":"icmp","ncb2_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-ipv6-ingress-src_start_port":"0.0","ncb1_Internal2_mac":"00:11:22:EF:AC:DF","fsb_volume_size_0":"320.0","sctp-b-egress_src_addresses":"local","sctp-a-ipv6-ingress_ethertype":"IPv4","sctp-a-ipv6-ingress-dst_start_port":"0","sctp-b-ipv6-ingress_rule_application":"any","domain_name":"default-domain","sctp-a-ingress_rule_protocol":"icmp","sctp-b-egress-src_start_port":"0.0","sctp-a-egress_src_addresses":"local","sctp-b-display_name":"epc-sctp-b-ipv4v6-sec-group","sctp-a-egress-src_start_port":"0.0","sctp-a-ingress_ethertype":"IPv4","sctp-b-ipv6-ingress-dst_end_port":"65535","sctp-b-dst_subnet_prefix_v6":"::","nf_naming":"{ecomp_generated_naming=true}","sctp-a-ipv6-ingress_src_subnet_prefix":"0.0.0.0","sctp-b-egress-dst_start_port":"0.0","ncb_flavor_name":"nv.c20r64d1","gpb1_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-egress_dst_subnet_prefix_len":"0.0","Internal2_net_cidr":"10.0.0.10","sctp-a-ingress-dst_start_port":"0.0","sctp-a-egress-dst_start_port":"0.0","fsb1_Internal2_mac":"00:11:22:EF:AC:DF","sctp-a-egress_ethertype":"IPv4","vlc_st_service_mode":"in-network-nat","sctp-a-ipv6-egress_ethertype":"IPv4","sctp-a-egress-src_end_port":"65535.0","sctp-b-ipv6-egress_rule_application":"any","sctp-b-egress_action":"pass","sctp-a-ingress-src_subnet_prefix_len":"0.0","sctp-b-ipv6-ingress-src_end_port":"65535.0","sctp-b-name":"epc-sctp-b-ipv4v6-sec-group","fsb2_Internal1_mac":"00:11:22:EF:AC:DF","sctp-a-ipv6-ingress-src_start_port":"0.0","sctp-b-ipv6-egress_ethertype":"IPv4","Internal1_net_cidr":"10.0.0.10","sctp-a-egress_dst_subnet_prefix":"0.0.0.0","fsb_flavor_name":"nv.c20r64d1","sctp_rule_protocol":"132","sctp-b-ipv6-ingress_src_subnet_prefix_len":"0","sctp-a-ipv6-ingress_rule_application":"any","ecomp_generated_naming":"false","sctp-a-IPv6_ethertype":"IPv6","vlc2_Internal1_mac":"00:11:22:EF:AC:DF","vlc_st_virtualization_type":"virtual-machine","sctp-b-ingress-dst_start_port":"0.0","sctp-b-ingress-dst_end_port":"65535.0","sctp-a-ipv6-ingress-src_end_port":"65535.0","sctp-a-display_name":"epc-sctp-a-ipv4v6-sec-group","sctp-b-ingress_rule_application":"any","int2_sec_group_name":"int2-sec-group","vlc_flavor_name":"nd.c16r64d1","sctp-b-ipv6-egress_src_addresses":"local","vlc_st_interface_type_int1":"other1","sctp-b-egress-src_end_port":"65535.0","sctp-a-ipv6-egress-dst_start_port":"0","vlc_st_interface_type_int2":"other2","sctp-a-ipv6-egress_rule_protocol":"any","Internal2_shared":"false","sctp-a-ipv6-egress_dst_subnet_prefix_len":"0","Internal2_rpf":"disable","vlc1_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-ipv6-egress_src_end_port":"65535","sctp-a-ipv6-egress_src_addresses":"local","sctp-a-ingress-dst_end_port":"65535.0","sctp-a-ipv6-egress_src_end_port":"65535","Internal1_forwarding_mode":"l2","Internal2_dhcp":"false","sctp-a-dst_subnet_prefix_v6":"::","pxe_image_name":"MME_PXE-Boot_16ACP04_GA.qcow2","vlc_st_interface_type_gtp":"other0","ncb1_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-src_subnet_prefix_v6":"::","sctp-a-egress_dst_subnet_prefix_len":"0.0","int1_sec_group_name":"int1-sec-group","Internal1_dhcp":"false","sctp-a-ipv6-egress_dst_end_port":"65535","Internal2_forwarding_mode":"l2","fsb2_Internal2_mac":"00:11:22:EF:AC:DF","sctp-b-egress_dst_subnet_prefix":"0.0.0.0","Internal1_net_cidr_len":"17","gpb2_Internal1_mac":"00:11:22:EF:AC:DF","sctp-b-ingress-src_subnet_prefix_len":"0.0","sctp-a-ingress_dst_addresses":"local","sctp-a-egress_action":"pass","fsb_volume_type_0":"SF-Default-SSD","ncb2_Internal2_mac":"00:11:22:EF:AC:DF","vlc_st_interface_type_sctp_a":"left","vlc_st_interface_type_sctp_b":"right","sctp-a-src_subnet_prefix_v6":"::","vlc_st_version":"2","sctp-b-egress_ethertype":"IPv4","sctp-a-ingress_rule_application":"any","gpb1_Internal2_mac":"00:11:22:EF:AC:DF","instance_ip_family_v6":"v6","sctp-a-ipv6-egress_src_start_port":"0","sctp-b-ingress-src_start_port":"0.0","sctp-b-ingress_dst_addresses":"local","fsb1_Internal1_mac":"00:11:22:EF:AC:DF","vlc_st_interface_type_oam":"management","multi_stage_design":"false","oam_sec_group_name":"oam-sec-group","Internal2_net_gateway":"10.0.0.10","sctp-a-ipv6-ingress-dst_end_port":"65535","sctp-b-ipv6-egress-dst_start_port":"0","Internal1_net_gateway":"10.0.0.10","sctp-b-ipv6-egress_rule_protocol":"any","gtp_sec_group_name":"gtp-sec-group","sctp-a-ipv6-egress_dst_subnet_prefix":"0.0.0.0","sctp-b-ipv6-egress_dst_subnet_prefix_len":"0","sctp-a-ipv6-ingress_dst_addresses":"local","sctp-a-egress_rule_protocol":"icmp","sctp-b-ipv6-egress_action":"pass","sctp-a-ipv6-egress_action":"pass","Internal1_shared":"false","sctp-b-ipv6-ingress_rule_protocol":"any","Internal2_net_cidr_len":"17","sctp-a-name":"epc-sctp-a-ipv4v6-sec-group","sctp-a-ingress-src_end_port":"65535.0","sctp-b-ipv6-ingress_src_subnet_prefix":"0.0.0.0","sctp-a-egress-dst_end_port":"65535.0","sctp-a-ingress_action":"pass","sctp-b-egress_rule_protocol":"icmp","sctp-b-ipv6-ingress_action":"pass","vlc_st_service_type":"firewall","sctp-b-ipv6-egress_dst_end_port":"65535","sctp-b-ipv6-ingress-dst_start_port":"0","vlc2_Internal2_mac":"00:11:22:EF:AC:DF","vlc_st_availability_zone":"true","fsb_volume_image_name_1":"MME_FSB2_16ACP04_GA.qcow2","sctp-b-ingress-src_subnet_prefix":"0.0.0.0","sctp-a-ipv6-ingress_src_subnet_prefix_len":"0","Internal1_allow_transit":"true","gpb_flavor_name":"nv.c20r64d1","availability_zone_max_count":"1","fsb_volume_image_name_0":"MME_FSB1_16ACP04_GA.qcow2","sctp-b-ipv6-ingress_dst_addresses":"local","sctp-b-ipv6-egress_dst_subnet_prefix":"0.0.0.0","sctp-b-ipv6-ingress_ethertype":"IPv4","vlc1_Internal2_mac":"00:11:22:EF:AC:DF","sctp-a-ingress-src_subnet_prefix":"0.0.0.0","sctp-a-ipv6-ingress_action":"pass","Internal1_rpf":"disable","sctp-b-ingress_ethertype":"IPv4","sctp-b-egress_rule_application":"any","sctp-b-ingress-src_end_port":"65535.0","sctp-a-ipv6-ingress_rule_protocol":"any","sctp-a-ingress-src_start_port":"0.0","sctp-b-egress-dst_end_port":"65535.0"},"type":"VF","modelCustomizationName":"VF_vGeraldine 0","vfModules":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1":{"uuid":"522159d5-d6e0-4c2a-aa44-5a542a12a830","invariantUuid":"98a7c88b-b577-476a-90e4-e25a5871e02b","customizationUuid":"55b1be94-671a-403e-a26c-667e9c47d091","description":null,"name":"VfVgeraldine..vflorence_vlc..module-1","version":"2","modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1","properties":{"minCountInstances":0,"maxCountInstances":null,"initialCount":0,"vfModuleLabel":"vflorence_vlc"},"inputs":{},"volumeGroupAllowed":false},"vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2":{"uuid":"41708296-e443-4c71-953f-d9a010f059e1","invariantUuid":"1cca90b8-3490-495e-87da-3f3e4c57d5b9","customizationUuid":"6add59e0-7fe1-4bc4-af48-f8812422ae7c","description":null,"name":"VfVgeraldine..vflorence_gpb..module-2","version":"2","modelCustomizationName":"VfVgeraldine..vflorence_gpb..module-2","properties":{"minCountInstances":0,"maxCountInstances":null,"initialCount":0,"vfModuleLabel":"vflorence_gpb"},"inputs":{},"volumeGroupAllowed":false},"vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0":{"uuid":"a27f5cfc-7f12-4f99-af08-0af9c3885c87","invariantUuid":"a6f9e51a-2b35-416a-ae15-15e58d61f36d","customizationUuid":"f8c040f1-7e51-4a11-aca8-acf256cfd861","description":null,"name":"VfVgeraldine..base_vflorence..module-0","version":"2","modelCustomizationName":"VfVgeraldine..base_vflorence..module-0","properties":{"minCountInstances":1,"maxCountInstances":1,"initialCount":1,"vfModuleLabel":"base_vflorence"},"inputs":{},"volumeGroupAllowed":true}},"volumeGroups":{"vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0":{"uuid":"a27f5cfc-7f12-4f99-af08-0af9c3885c87","invariantUuid":"a6f9e51a-2b35-416a-ae15-15e58d61f36d","customizationUuid":"f8c040f1-7e51-4a11-aca8-acf256cfd861","description":null,"name":"VfVgeraldine..base_vflorence..module-0","version":"2","modelCustomizationName":"VfVgeraldine..base_vflorence..module-0","properties":{"minCountInstances":1,"maxCountInstances":1,"initialCount":1,"vfModuleLabel":"base_vflorence"},"inputs":{}}},"vfcInstanceGroups":{}}},"networks":{"ExtVL 0":{"uuid":"ddc3f20c-08b5-40fd-af72-c6d14636b986","invariantUuid":"379f816b-a7aa-422f-be30-17114ff50b7c","description":"ECOMP generic virtual link (network) base type for all other service-level and global networks","name":"ExtVL","version":"37.0","customizationUuid":"94fdd893-4a36-4d70-b16a-ec29c54c184f","inputs":{},"commands":{},"properties":{"network_assignments":"{is_external_network=false, ipv4_subnet_default_assignment={min_subnets_count=1}, ecomp_generated_network_assignment=false, ipv6_subnet_default_assignment={min_subnets_count=1}}","exVL_naming":"{ecomp_generated_naming=true}","network_flows":"{is_network_policy=false, is_bound_to_vpn=false}","network_homing":"{ecomp_selected_instance_node_target=false}"},"type":"VL","modelCustomizationName":"ExtVL 0"}},"collectionResources":{},"configurations":{"Port Mirroring Configuration By Policy 0":{"uuid":"b4398538-e89d-4f13-b33d-ca323434ba50","invariantUuid":"6ef0ca40-f366-4897-951f-abd65d25f6f7","description":"A port mirroring configuration by policy object","name":"Port Mirroring Configuration By Policy","version":"27.0","customizationUuid":"3c3b7b8d-8669-4b3b-8664-61970041fad2","inputs":{},"commands":{},"properties":{},"type":"Configuration","modelCustomizationName":"Port Mirroring Configuration By Policy 0","sourceNodes":[],"collectorNodes":null,"configurationByPolicy":false}},"serviceProxies":{},"vfModules":{"vf_vgeraldine0..VfVgeraldine..vflorence_vlc..module-1":{"uuid":"522159d5-d6e0-4c2a-aa44-5a542a12a830","invariantUuid":"98a7c88b-b577-476a-90e4-e25a5871e02b","customizationUuid":"55b1be94-671a-403e-a26c-667e9c47d091","description":null,"name":"VfVgeraldine..vflorence_vlc..module-1","version":"2","modelCustomizationName":"VfVgeraldine..vflorence_vlc..module-1","properties":{"minCountInstances":0,"maxCountInstances":null,"initialCount":0,"vfModuleLabel":"vflorence_vlc"},"inputs":{},"volumeGroupAllowed":false},"vf_vgeraldine0..VfVgeraldine..vflorence_gpb..module-2":{"uuid":"41708296-e443-4c71-953f-d9a010f059e1","invariantUuid":"1cca90b8-3490-495e-87da-3f3e4c57d5b9","customizationUuid":"6add59e0-7fe1-4bc4-af48-f8812422ae7c","description":null,"name":"VfVgeraldine..vflorence_gpb..module-2","version":"2","modelCustomizationName":"VfVgeraldine..vflorence_gpb..module-2","properties":{"minCountInstances":0,"maxCountInstances":null,"initialCount":0,"vfModuleLabel":"vflorence_gpb"},"inputs":{},"volumeGroupAllowed":false},"vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0":{"uuid":"a27f5cfc-7f12-4f99-af08-0af9c3885c87","invariantUuid":"a6f9e51a-2b35-416a-ae15-15e58d61f36d","customizationUuid":"f8c040f1-7e51-4a11-aca8-acf256cfd861","description":null,"name":"VfVgeraldine..base_vflorence..module-0","version":"2","modelCustomizationName":"VfVgeraldine..base_vflorence..module-0","properties":{"minCountInstances":1,"maxCountInstances":1,"initialCount":1,"vfModuleLabel":"base_vflorence"},"inputs":{},"volumeGroupAllowed":true}},"volumeGroups":{"vf_vgeraldine0..VfVgeraldine..base_vflorence..module-0":{"uuid":"a27f5cfc-7f12-4f99-af08-0af9c3885c87","invariantUuid":"a6f9e51a-2b35-416a-ae15-15e58d61f36d","customizationUuid":"f8c040f1-7e51-4a11-aca8-acf256cfd861","description":null,"name":"VfVgeraldine..base_vflorence..module-0","version":"2","modelCustomizationName":"VfVgeraldine..base_vflorence..module-0","properties":{"minCountInstances":1,"maxCountInstances":1,"initialCount":1,"vfModuleLabel":"base_vflorence"},"inputs":{}}},"pnfs":{}}');
}


function generateVNFFormValues() {
  return JSON.parse('{"globalSubscriberId":"e433710f-9217-458d-a79d-1c7aff376d89","productFamilyId":"vTerrance","subscriptionServiceType":"TYLER SILVIA","lcpCloudRegionId":"hvf6","tenantId":"1178612d2b394be4834ad77f567c0af2","aicZoneId":"JAG1","projectName":"WATKINS","owningEntityId":"d61e6f2d-12fa-4cc2-91df-7c244011d6fc","rollbackOnFailure":"true","bulkSize":1,"instanceParams":[{}],"modelInfo":{"modelInvariantId":"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0","modelVersionId":"6e59c5de-f052-46fa-aa7e-2fca9d674c44","modelName":"ComplexService","modelVersion":"1.0"},"tenantName":"AIN Web Tool-15-D-SSPtestcustome","aicZoneName":"YUDFJULP-JAG1"}');
}

function generateVFModule() {
  return {
    'uuid': '25284168-24bb-4698-8cb4-3f509146eca5',
    'invariantUuid': '7253ff5c-97f0-4b8b-937c-77aeb4d79aa1',
    'customizationUuid': 'f7e7c365-60cf-49a9-9ebf-a1aa11b9d401',
    'description': null,
    'name': '2017488PasqualeVpe..PASQUALE_vRE_BV..module-1',
    'version': '6',
    'modelCustomizationName': '2017488PasqualeVpe..PASQUALE_vRE_BV..module-1',
    'properties': {'minCountInstances': 0, 'maxCountInstances': null, 'initialCount': 0},
    'commands': {},
    'volumeGroupAllowed': true,
    'inputs': {
      '2017488_pasqualevpe0_vnf_config_template_version': {
        'type': 'string',
        'description': 'VPE Software Version',
        'entry_schema': null,
        'constraints': [],
        'required': true,
        'default': '17.2'
      },
      '2017488_pasqualevpe0_AIC_CLLI': {
        'type': 'string',
        'description': 'AIC Site CLLI',
        'entry_schema': null,
        'constraints': [],
        'required': true,
        'default': 'ATLMY8GA'
      }
    }
  };
}

function generateVFModule2() {
  return {
    'uuid': '0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a',
    'invariantUuid': 'eff8cc59-53a1-4101-aed7-8cf24ecf8339',
    'customizationUuid': '3cd946bb-50e0-40d8-96d3-c9023520b557',
    'description': null,
    'name': '2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2',
    'version': '6',
    'modelCustomizationName': '2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2',
    'properties': {'minCountInstances': 0, 'maxCountInstances': null, 'initialCount': 0},
    'commands': {},
    'volumeGroupAllowed': true,
    'inputs': {}
  };
}

function generateVNF() {
  return {
    'uuid': '0903e1c0-8e03-4936-b5c2-260653b96413',
    'invariantUuid': '00beb8f9-6d39-452f-816d-c709b9cbb87d',
    'description': 'Name PASQUALE vPE Description The provider edge function for the PASQUALE service supported by the Junipers VMX product Category Router Vendor Juniper Vendor Release Code 17.2 Owners Mary Fragale. Updated 9-25 to use v8.0 of the Juniper Valid 2 VLM',
    'name': '2017-388_PASQUALE-vPE',
    'version': '1.0',
    'customizationUuid': '280dec31-f16d-488b-9668-4aae55d6648a',
    'inputs': {
      'vnf_config_template_version': {
        'type': 'string',
        'description': 'VPE Software Version',
        'entry_schema': null,
        'constraints': [],
        'required': true,
        'default': '17.2'
      },
      'bandwidth_units': {
        'type': 'string',
        'description': 'Units of bandwidth',
        'entry_schema': null,
        'constraints': [],
        'required': true,
        'default': 'Gbps'
      },
      'bandwidth': {
        'type': 'string',
        'description': 'Requested VPE bandwidth',
        'entry_schema': null,
        'constraints': [],
        'required': true,
        'default': '10'
      },
      'AIC_CLLI': {
        'type': 'string',
        'description': 'AIC Site CLLI',
        'entry_schema': null,
        'constraints': [],
        'required': true,
        'default': 'ATLMY8GA'
      },
      'ASN': {
        'type': 'string',
        'description': 'AV/PE',
        'entry_schema': null,
        'constraints': [],
        'required': true,
        'default': 'AV_vPE'
      },
      'vnf_instance_name': {
        'type': 'string',
        'description': 'The hostname assigned to the vpe.',
        'entry_schema': null,
        'constraints': [],
        'required': true,
        'default': 'mtnj309me6'
      }
    },
    'commands': {
      'vnf_config_template_version': {
        'displayName': 'vnf_config_template_version',
        'command': 'get_input',
        'inputName': '2017488_pasqualevpe0_vnf_config_template_version'
      },
      'bandwidth_units': {
        'displayName': 'bandwidth_units',
        'command': 'get_input',
        'inputName': 'pasqualevpe0_bandwidth_units'
      },
      'bandwidth': {'displayName': 'bandwidth', 'command': 'get_input', 'inputName': 'pasqualevpe0_bandwidth'},
      'AIC_CLLI': {'displayName': 'AIC_CLLI', 'command': 'get_input', 'inputName': '2017488_pasqualevpe0_AIC_CLLI'},
      'ASN': {'displayName': 'ASN', 'command': 'get_input', 'inputName': '2017488_pasqualevpe0_ASN'},
      'vnf_instance_name': {
        'displayName': 'vnf_instance_name',
        'command': 'get_input',
        'inputName': '2017488_pasqualevpe0_vnf_instance_name'
      }
    },
    'properties': {
      'vmxvre_retype': 'RE-VMX',
      'vnf_config_template_version': 'get_input:2017488_pasqualevpe0_vnf_config_template_version',
      'sriov44_net_id': '48d399b3-11ee-48a8-94d2-f0ea94d6be8d',
      'int_ctl_net_id': '2f323477-6936-4d01-ac53-d849430281d9',
      'vmxvpfe_sriov41_0_port_mac': '00:11:22:EF:AC:DF',
      'int_ctl_net_name': 'VMX-INTXI',
      'vmx_int_ctl_prefix': '10.0.0.10',
      'sriov43_net_id': 'da349ca1-6de9-4548-be88-2d88e99bfef5',
      'sriov42_net_id': '760669ba-013d-4d9b-b0e7-4151fe2e6279',
      'sriov41_net_id': '25ad52d5-c165-40f8-b3b0-ddfc2373280a',
      'nf_type': 'vPE',
      'vmxvpfe_int_ctl_ip_1': '10.0.0.10',
      'is_AVPN_service': 'false',
      'vmx_RSG_name': 'vREXI-affinity',
      'vmx_int_ctl_forwarding': 'l2',
      'vmxvre_oam_ip_0': '10.0.0.10',
      'vmxvpfe_sriov44_0_port_mac': '00:11:22:EF:AC:DF',
      'vmxvpfe_sriov41_0_port_vlanstrip': 'false',
      'vmxvpfe_sriov42_0_port_vlanfilter': '4001',
      'vmxvpfe_sriov44_0_port_unknownunicastallow': 'true',
      'vmxvre_image_name_0': 'VRE-ENGINE_17.2-S2.1.qcow2',
      'vmxvre_instance': '0',
      'vmxvpfe_sriov43_0_port_mac': '00:11:22:EF:AC:DF',
      'vmxvre_flavor_name': 'ns.c1r16d32.v5',
      'vmxvpfe_volume_size_0': '40.0',
      'vmxvpfe_sriov43_0_port_vlanfilter': '4001',
      'nf_naming': '{ecomp_generated_naming=true}',
      'nf_naming_code': 'Navneet',
      'vmxvre_name_0': 'vREXI',
      'vmxvpfe_sriov42_0_port_vlanstrip': 'false',
      'vmxvpfe_volume_name_0': 'vPFEXI_FBVolume',
      'vmx_RSG_id': 'bd89a33c-13c3-4a04-8fde-1a57eb123141',
      'vmxvpfe_image_name_0': 'VPE_ROUTING-ENGINE_17.2R1-S2.1.qcow2',
      'vmxvpfe_sriov43_0_port_unknownunicastallow': 'true',
      'vmxvpfe_sriov44_0_port_unknownmulticastallow': 'true',
      'vmxvre_console': 'vidconsole',
      'vmxvpfe_sriov44_0_port_vlanfilter': '4001',
      'vmxvpfe_sriov42_0_port_mac': '00:11:22:EF:AC:DF',
      'vmxvpfe_volume_id_0': '47cede15-da2f-4397-a101-aa683220aff3',
      'vmxvpfe_sriov42_0_port_unknownmulticastallow': 'true',
      'vmxvpfe_sriov44_0_port_vlanstrip': 'false',
      'vf_module_id': '123',
      'nf_function': 'JAI',
      'vmxvpfe_sriov43_0_port_unknownmulticastallow': 'true',
      'vmxvre_int_ctl_ip_0': '10.0.0.10',
      'AIC_CLLI': 'get_input:2017488_pasqualevpe0_AIC_CLLI',
      'vnf_name': 'mtnj309me6vre',
      'vmxvpfe_sriov41_0_port_unknownunicastallow': 'true',
      'vmxvre_volume_type_1': 'HITACHI',
      'vmxvpfe_sriov44_0_port_broadcastallow': 'true',
      'vmxvre_volume_type_0': 'HITACHI',
      'vmxvpfe_volume_type_0': 'HITACHI',
      'vmxvpfe_sriov43_0_port_broadcastallow': 'true',
      'bandwidth_units': 'get_input:pasqualevpe0_bandwidth_units',
      'vnf_id': '123',
      'vmxvre_oam_prefix': '24',
      'availability_zone_0': 'mtpocfo-kvm-az01',
      'ASN': 'get_input:2017488_pasqualevpe0_ASN',
      'vmxvre_chassis_i2cid': '161',
      'vmxvpfe_name_0': 'vPFEXI',
      'bandwidth': 'get_input:pasqualevpe0_bandwidth',
      'availability_zone_max_count': '1',
      'vmxvre_volume_size_0': '45.0',
      'vmxvre_volume_size_1': '50.0',
      'vmxvpfe_sriov42_0_port_broadcastallow': 'true',
      'vmxvre_oam_gateway': '10.0.0.10',
      'vmxvre_volume_name_1': 'vREXI_FAVolume',
      'vmxvre_ore_present': '0',
      'vmxvre_volume_name_0': 'vREXI_FBVolume',
      'vmxvre_type': '0',
      'vnf_instance_name': 'get_input:2017488_pasqualevpe0_vnf_instance_name',
      'vmxvpfe_sriov41_0_port_unknownmulticastallow': 'true',
      'oam_net_id': 'b95eeb1d-d55d-4827-abb4-8ebb94941429',
      'vmx_int_ctl_len': '24',
      'vmxvpfe_sriov43_0_port_vlanstrip': 'false',
      'vmxvpfe_sriov41_0_port_broadcastallow': 'true',
      'vmxvre_volume_id_1': '6e86797e-03cd-4fdc-ba72-2957119c746d',
      'vmxvpfe_sriov41_0_port_vlanfilter': '4001',
      'nf_role': 'Testing',
      'vmxvre_volume_id_0': 'f4eacb79-f687-4e9d-b760-21847c8bb15a',
      'vmxvpfe_sriov42_0_port_unknownunicastallow': 'true',
      'vmxvpfe_flavor_name': 'ns.c20r16d25.v5'
    },
    'type': 'VF',
    'modelCustomizationName': '2017-388_PASQUALE-vPE 1',
    'vfModules': {},
    'volumeGroups': {}
  };
}
