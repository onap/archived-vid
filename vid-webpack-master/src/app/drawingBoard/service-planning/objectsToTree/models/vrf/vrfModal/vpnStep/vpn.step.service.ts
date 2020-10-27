import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../../../shared/store/reducers";
import {AaiService} from "../../../../../../../shared/services/aaiService/aai.service";
import {
  ISearchField,
  ITableContent,
  ModalInformation
} from "../../../../../../../shared/components/searchMembersModal/members-table/element-table-row.model";
import {ElementsTableService} from "../../../../../../../shared/components/searchMembersModal/members-table/elements-table.service";
import {
  clearAllGenericModalhelper,
  updateGenericModalhelper
} from "../../../../../../../shared/storeUtil/utils/global/global.actions";
import {Observable} from "rxjs";
import {CustomTableColumnDefinition} from "../../../../../../../shared/components/searchMembersModal/members-table/elements-table.component";
import {VPNModalRow} from "./vpn.step.model";
import {NetworkStepService} from "../networkStep/network.step.service";
import * as _ from "lodash";
import {
  associateVRFMember,
  clearAssociateVRFMemberInstance,
  createVrfInstance
} from "../../../../../../../shared/storeUtil/utils/vrf/vrf.actions";
import {calculateNextUniqueModelName} from "../../../../../../../shared/storeUtil/utils/reducersHelper";

@Injectable()
export class VpnStepService {
  constructor(private _store: NgRedux<AppState>, private _aaiService: AaiService, private _networkStepService: NetworkStepService) {
  }

  uniqObjectField: string = "instanceId";
  type: string = "VPN";

  getVpnStep = (serviceInstance, serviceModelId, ...otherSteps): ModalInformation => {
    return {
      type: this.type,
      serviceModelId: serviceModelId,
      title: 'Associate VPN',
      description: 'Select a VPN to associate to the VRF Entry',
      noElementsMsg: 'No VPN instances were found.',
      maxSelectRow: 1,
      uniqObjectField: this.uniqObjectField,
      backAction: (searchElementsModalComponent) => {
        ElementsTableService.changeModalInformationDataTrigger.next({
          modalInformation: otherSteps[1].getNetworkStep(serviceInstance, searchElementsModalComponent.modalInformation.serviceModelId, ...otherSteps),
          selectedRowsIds: [this._store.getState().global.genericModalHelper.selectedNetwork.instanceUUID]
        });
      },
      topButton: {
        text: 'SET VPN',
        action: (searchElementsModalComponent) => {
          let currentVRF = this._store.getState().global.genericModalHelper['currentVRF'];
          const vrfStoreKey: string = _.isNil(currentVRF['vrfStoreKey'])
            ? calculateNextUniqueModelName(currentVRF['model']['name'], searchElementsModalComponent.modalInformation.serviceModelId, this._store.getState().service, 'vrfs')
            : currentVRF['vrfStoreKey'];


          if (_.isNil(currentVRF['vrfStoreKey'])) {
            this._store.dispatch(createVrfInstance(currentVRF['model'], serviceModelId, vrfStoreKey));
          } else {
            this._store.dispatch(clearAssociateVRFMemberInstance(vrfStoreKey, serviceModelId, 'networks'));
            this._store.dispatch(clearAssociateVRFMemberInstance(vrfStoreKey, serviceModelId, 'vpns'));
          }

          this.associateVrfVPNMember(searchElementsModalComponent, vrfStoreKey);
          this.associateVrfNetworkMember(searchElementsModalComponent, vrfStoreKey);

          this._store.dispatch(clearAllGenericModalhelper());
          searchElementsModalComponent.closeDialog();
        }
      },
      getElements: () => this.getVPNs(),
      criteria: [],
      searchFields: this.getsVPNStepSearchFields(),
      tableHeaders: this.getVPNStepHeaders(),
      tableContent: this.getVPNTableContent()
    };
  };

  getVPNs = (): Observable<any[]> => {
    return this._aaiService.retrieveActiveVPNs().map((vpns: VPNModalRow[]) => {
      return this.sortElementsResultByField(vpns, "instanceName");
    });
  };

  sortElementsResultByField = (elements, fieldName): any[] => {
    return _.sortBy(elements, o => o[fieldName]);
  };

  associateVrfVPNMember = (searchElementsModalComponent, vrfStoreKey): void => {
    let tmpMembers = searchElementsModalComponent._membersTableService.allElementsStatusMap;
    for (let key in tmpMembers) {
      if (tmpMembers[key].isSelected) {
        this._store.dispatch(updateGenericModalhelper(`selected${this.type}`, tmpMembers[key], this.uniqObjectField));
        this._store.dispatch(associateVRFMember(vrfStoreKey, searchElementsModalComponent.modalInformation.serviceModelId, tmpMembers[key], 'vpns'));
      }
    }
  };

  associateVrfNetworkMember = (searchElementsModalComponent, vrfStoreKey): void => {
    let selectedNetworks = this._store.getState().global.genericModalHelper['selectedNetwork'];
    for (let key in selectedNetworks) {
      this._store.dispatch(associateVRFMember(vrfStoreKey, searchElementsModalComponent.modalInformation.serviceModelId, selectedNetworks[key], 'networks'));
    }
  };

  getsVPNStepSearchFields = (): ISearchField[] => {
    return [
      {
        title: 'Type ',
        dataTestId: 'Type',
        value: "SERVICE-INFRASTRUCTURE",
        type: "LABEL"
      }
    ]
  };

  getVPNStepHeaders = (): CustomTableColumnDefinition[] => {
    return [
      {displayName: `VPN instance name`, key: ['instanceName']},
      {displayName: `Version`, key: ['modelInfo', 'modelVersionId']},
      {displayName: `Instance ID`, key: ['instanceId']},
      {displayName: `Platform`, key: ['platformName']},
      {displayName: 'Region', key: ['region']},
      {displayName: 'Route target', key: ['routeTargets', 'globalRouteTarget'], type: 'LIST'},
      {displayName: 'Route target role', key: ['routeTargets', 'routeTargetRole'], type: 'LIST'},
      {displayName: 'Customer VPN ID', key: ['customerId']},
    ];
  };

  getVPNTableContent = (): ITableContent[] => {
    return [
      {
        id: 'vpn-name',
        contents: [{
          id: ['vpn-name'],
          value: ['instanceName']
        }]
      },
      {
        id: 'model-version-id',
        contents: [{
          id: ['model-version-id'],
          value: ['modelInfo', 'modelVersionId']
        }]
      },
      {
        id: 'vpn-id',
        contents: [{
          id: ['vpn-id'],
          value: ['instanceId']
        }]
      },
      {
        id: 'vpn-platform',
        contents: [{
          id: ['vpn-platform'],
          value: ['platformName']
        }]
      },
      {
        id: 'vpn-region',
        contents: [{
          id: ['vpn-region'],
          value: ['region']
        }]
      },
      {
        id: 'global-route-target',
        contents: [{
          id: ['global-route-target'],
          value: ['routeTargets', 'globalRouteTarget'],
          type: 'LIST'
        }]
      },
      {
        id: 'route-target-role',
        contents: [{
          id: ['route-target-role'],
          value: ['routeTargets', 'routeTargetRole'],
          type: 'LIST'
        }]
      },
      {
        id: 'customer-vpn-id',
        contents: [{
          id: ['customer-vpn-id'],
          value: ['customerId']
        }]
      }
    ];
  };

}
