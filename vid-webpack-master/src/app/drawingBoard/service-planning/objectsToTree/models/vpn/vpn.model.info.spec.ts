import {ComponentInfoService} from "../../../component-info/component-info.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {getTestBed, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {SharedTreeService} from "../../shared.tree.service";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {VpnModelInfo} from "./vpn.model.info";
import {VrfModel} from "../../../../../shared/models/vrfModel";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {Level1Instance} from "../../../../../shared/models/level1Instance";
import * as _ from "lodash";
import {VpnTreeNode} from "../../../../../shared/models/vpnTreeNode";
import {Level1Model} from "../../../../../shared/models/nodeModel";

describe('VPN Model Info', () => {

  let injector;
  let _store : NgRedux<AppState>;
  let _sharedTreeService: SharedTreeService;
  let vpnModel: VpnModelInfo;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        SharedTreeService,
        MockNgRedux
       ]
    }).compileComponents();
    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
    _store = injector.get(NgRedux);
    vpnModel = new VpnModelInfo(_store,_sharedTreeService);
  });


  test('vpnModel should be defined', () => {
    expect(vpnModel).toBeDefined();
  });

  test('vpnModel should defined extra details', () => {
    expect(vpnModel.name).toEqual('vpns');
    expect(vpnModel.type).toEqual('VPN');
    expect(vpnModel.childNames).toBeUndefined;
    expect(vpnModel.componentInfoType).toEqual(ComponentInfoType.VPN);
  });

  test('Info for vpn should be correct', () => {
    const model = null;
    const instance = new VpnTreeNode(new Level1Instance(), new Level1Model(), "");
    instance.region = "USA,EMEA";
    instance.routeTargetId = "globalRouteTarget_1";
    instance.routeTargetRole = "routeTargetRole_1";
    instance.customerVPNId =  "VPN1260";
    let actualVpnInfo = vpnModel.getInfo(model,instance);
    const instanceInfo = [
      ModelInformationItem.createInstance("Region", "USA,EMEA"),
      ModelInformationItem.createInstance("Route target id", "globalRouteTarget_1"),
      ModelInformationItem.createInstance("Route target role", "routeTargetRole_1"),
      ModelInformationItem.createInstance("Customet VPN ID", "VPN1260")];
    expect(actualVpnInfo).toEqual(instanceInfo);
  });

})
