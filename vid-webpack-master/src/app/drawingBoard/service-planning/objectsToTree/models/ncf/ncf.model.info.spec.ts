import {ComponentInfoType} from "../../../component-info/component-info-model";
import {NcfModelInfo} from "./ncf.model.info";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {NcfTreeNode} from "../../../../../shared/models/ncfTreeNode";

describe('NCF Model Info', () => {

  let ncfModel: NcfModelInfo;

  beforeEach(() => {
    ncfModel = new NcfModelInfo(null);
  });

  test('ncfModel should be defined', () => {
    expect(ncfModel).toBeDefined();
  });

  test('ncfModel should defined extra details', () => {
    expect(ncfModel.name).toEqual('ncfs');
    expect(ncfModel.type).toEqual('NCF');
    expect(ncfModel.childNames).toBeUndefined;
    expect(ncfModel.componentInfoType).toEqual(ComponentInfoType.NCF);
  });

  test('createInstanceTreeNode shall create the right ncfTreeNode', () => {
    const instance = {
      action: "None",
      instanceName: "NcmVlanSvcYm161f_77_vTSBC Customer Landing Network Collection",
      instanceId: "6b3536cf-3a12-457f-abb5-fa2203e0d923",
      instanceType: "L3-NETWORK",
      inMaint: false,
      uuid: "dd182d7d-6949-4b90-b3cc-5befe400742e",
      originalName: "ncm_vlan_ym161f..NetworkCollection..0",
      trackById: "6b3536cf-3a12-457f-abb5-fa2203e0d923",
      instanceGroupRole: "SUB_INTERFACE",
      instanceGroupFunction: "vTSBC Customer Landing Network Collection",
      numberOfNetworks: 1
    };

    const parentModel = {
      networksCollection: {
        "ncm_vlan_ym161f..NetworkCollection..0": {
          uuid: "dd182d7d-6949-4b90-b3cc-5befe400742e",
          invariantUuid: "868b109c-9481-4a18-891b-af974db7705a",
          name: "ncm_vlan_ym161f..NetworkCollection..0",
          version: "1",
          networkCollectionProperties: {
            networkCollectionFunction: "vTSBC Customer Landing Network Collection",
            networkCollectionDescription: "vTSBC Customer Landing Network Collection Desc"
          }
        }
      }
    };

    const expected = {
      action: "None",
      id: "6b3536cf-3a12-457f-abb5-fa2203e0d923",
      inMaint: false,
      instanceGroupFunction: "vTSBC Customer Landing Network Collection",
      instanceGroupRole: "SUB_INTERFACE",
      instanceId: "6b3536cf-3a12-457f-abb5-fa2203e0d923",
      instanceName: "NcmVlanSvcYm161f_77_vTSBC Customer Landing Network Collection",
      instanceType: "L3-NETWORK",
      missingData: false,
      modelVersion: "1",
      name: "NcmVlanSvcYm161f_77_vTSBC Customer Landing Network Collection",
      numberOfNetworks: 1,
      storeKey: "6b3536cf-3a12-457f-abb5-fa2203e0d923",
      typeName: "NCF"
    };

    const ncfTreeNode: NcfTreeNode = ncfModel.createInstanceTreeNode(instance, {}, parentModel, "6b3536cf-3a12-457f-abb5-fa2203e0d923", "dd182d7d-6949-4b90-b3cc-5befe400742e");
    expect(ncfTreeNode).toMatchObject(expected);
  });

  test('get modelInformation shall return Role, Collection function, Number of networks', () => {
    const ncf = {
      trackById: "6b3536cf-3a12-457f-abb5-fa2203e0d923",
      instanceGroupRole: "SUB_INTERFACE",
      instanceGroupFunction: "vTSBC Customer Landing Network Collection",
      numberOfNetworks: 1,
      modelVersion: "35"
    };

    const actualModelInformationItems = ncfModel.getInfo(null, ncf);
    const expected = [
      ModelInformationItem.createInstance('Role', 'SUB_INTERFACE'),
      ModelInformationItem.createInstance('Collection function', 'vTSBC Customer Landing Network Collection'),
      ModelInformationItem.createInstance('Number of networks', 1),
    ];

    expect(actualModelInformationItems).toEqual(expected);
  });

  test('get modelInformation shall return empty array when ncf instance is null', () => {
    expect(ncfModel.getInfo(null, null)).toEqual([]);
  });

  test('ncf getMenuAction: delete', ()=>{
    let node = {};
    let serviceModelId = 'serviceModelId';
    let result = ncfModel.getMenuAction(<any>node, serviceModelId);
    spyOn(result['delete'], 'method');
    expect(result['delete']).toBeDefined();
    expect(result['delete'].visible()).toBeFalsy();
    expect(result['delete'].enable()).toBeFalsy();
    result['delete']['method'](node, serviceModelId);
    expect(result['delete']['method']).toHaveBeenCalledWith(node, serviceModelId);
  });

});
