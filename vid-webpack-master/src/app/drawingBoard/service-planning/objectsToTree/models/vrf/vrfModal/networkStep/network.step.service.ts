import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../../../shared/store/reducers";
import {AaiService} from "../../../../../../../shared/services/aaiService/aai.service";
import {
  updateGenericModalCriteria,
  updateGenericModalhelper
} from "../../../../../../../shared/storeUtil/utils/global/global.actions";
import {ElementsTableService} from "../../../../../../../shared/components/searchMembersModal/members-table/elements-table.service";
import {
  ICriteria,
  ISearchField,
  ITableContent,
  ModalInformation
} from "../../../../../../../shared/components/searchMembersModal/members-table/element-table-row.model";
import {Observable, of} from "rxjs";
import * as _ from "lodash";
import {CustomTableColumnDefinition} from "../../../../../../../shared/components/searchMembersModal/members-table/elements-table.component";
import {NetworkModalRow} from "./network.step.model";

@Injectable()
export class NetworkStepService {
  constructor(private _store: NgRedux<AppState>, private _aaiService: AaiService) {}

  uniqObjectField: string = "instanceId";
  type: string = "Network";

  getNetworkStep = (serviceInstance, serviceModelId, ...otherSteps): ModalInformation => {
    return {
      type: this.type,
      serviceModelId: serviceModelId,
      title: 'Associate network to VRF Entry',
      description: 'Select a network to associate to the VRF Entry',
      noElementsMsg: 'No network were found.',
      maxSelectRow: 1,
      uniqObjectField: this.uniqObjectField,
      backAction: (searchElementsModalComponent) => {
        searchElementsModalComponent.closeDialog();
      },
      topButton: {
        text: 'Next',
        action: (searchElementsModalComponent) => {
          let tmpMembers = searchElementsModalComponent._membersTableService.allElementsStatusMap;
          let vpnStep = otherSteps[0];
          for (let key in tmpMembers) {
            if (tmpMembers[key].isSelected) {
              this._store.dispatch(updateGenericModalhelper(`selected${this.type}`, tmpMembers[key], this.uniqObjectField));
            }
          }
          ElementsTableService.changeModalInformationDataTrigger.next({
            modalInformation: vpnStep.getVpnStep(serviceInstance, searchElementsModalComponent.modalInformation.serviceModelId, ...otherSteps),
            selectedRowsIds: []
          })
        }
      },
      searchButton: {
        text: 'Search',
        action: (searchElementsModalComponent) => {
          this.getNetworks(serviceInstance).subscribe((networks) => {
            const currentRole = searchElementsModalComponent.modalInformation.criteria.find((criteria: ICriteria) => {
              criteria.label === 'Roles';
            });
            ElementsTableService.changeFnTableDataTrigger.next(
              networks.filter((network) => {
                if (_.isNil(currentRole)) return network;
                return network.role === currentRole.currentValue;
              }));
          });
        }
      },
      getElements: (...args) => this.getNetworks(serviceInstance),
      criteria: this.getNetworkStepCriteria(serviceInstance),
      searchFields: this.getsNetworkStepSearchFields(serviceInstance),
      tableHeaders: this.getNetworkStepHeaders(),
      tableContent: this.getNetworkTableContent()
    };
  };

  getNetworks = (serviceInstance): Observable<any[]> => {
    let cloudRegion = serviceInstance.lcpCloudRegionId;
    let tenantId = serviceInstance.tenantId;
    return this._aaiService.retrieveActiveNetwork(cloudRegion, tenantId).map((networks: NetworkModalRow[]) => {
      this.generateRolesOptions(networks);
      return this.sortElementsResultByField(networks, this.uniqObjectField);
    });
  };

  getNetworkStepCriteria(serviceInstance): ICriteria[] {
    return [{
      label: "Roles",
      defaultValue: "",
      type: 'DROPDOWN',
      onInit: (): Observable<any> => of(this._store.getState().global.genericModalCriteria.roles),
      onChange: (that, selectedOption): void => {
        that.currentValue = selectedOption;
      },
      dataTestId: 'roles-criteria',
      isRequired: false
    }]
  }

  getsNetworkStepSearchFields = (serviceInstance): ISearchField[] => {
    return [
      {
        title: 'Region',
        dataTestId: 'Region',
        value: serviceInstance["lcpCloudRegionId"],
        type: "LABEL"
      }, {
        title: 'Tenant',
        dataTestId: 'Tenant',
        value: serviceInstance["tenantName"],
        type: "LABEL"
      },
      {
        title: 'Orch. status',
        dataTestId: 'Orch_status',
        value: "Active",
        type: "LABEL"
      },
      {
        title: 'Related to',
        dataTestId: 'Related_to',
        value: "VPN binding",
        type: "LABEL"
      }
    ]
  };

  generateRolesOptions = (networks): void => {
    let roles: string[] = (_.uniq(_.flatten(_.map(networks, 'role').filter((role) => !_.isNil(role))))).sort();
    const SELECTED_AN_OPTION: string = "-- select an option --";
    const NOT_ASSIGNED: string = "Not assigned";

    roles = _.remove([SELECTED_AN_OPTION, this.shouldAddNotAssigned(networks) ? NOT_ASSIGNED : null, ...roles], null);
    this._store.dispatch(updateGenericModalCriteria("roles", roles));
  };

  getNetworkStepHeaders = (): CustomTableColumnDefinition[] => {
    return [
      {displayName: `Name`, key: ['instanceName']},
      {displayName: `Type`, key: ['instanceType']},
      {displayName: `Role`, key: ['role']},
      {displayName: 'Orch. Status', key: ['orchStatus']},
      {displayName: 'Physical name', key: ['physicalName']},
      {displayName: 'Instance ID', key: ['instanceId']},
      {displayName: 'Model UUID', key: ['modelInfo', 'modelVersionId']},
      {displayName: 'Service name', key: ['serviceName']},
      {displayName: 'Service UUID', key: ['serviceUUID']},
      {displayName: 'Tenant', key: ['tenantName']},
      {displayName: 'Region', key: ['lcpCloudRegionId']}
    ];
  };

  getNetworkTableContent = (): ITableContent[] => {
    return [
      {
        id: 'instanceName',
        contents: [{
          id: ['name'],
          value: ["instanceName"]
        }]
      },
      {
        id: 'instanceType',
        contents: [{
          id: ['type'],
          value: ['instanceType']
        }]
      },
      {
        id: 'role',
        contents: [{
          id: ['role'],
          value: ['role']
        }]
      },
      {
        id: 'network-orch-status',
        contents: [{
          id: ['orchStatus'],
          value: ['orchStatus']
        }]
      },
      {
        id: 'network-physical-name',
        contents: [{
          id: ['physicalName'],
          value: ['physicalName']
        }]
      },
      {
        id: 'network-instance-id',
        contents: [{
          id: ['instanceID'],
          value: ['instanceId']
        }]
      },
      {
        id: 'network-model-uuid',
        contents: [{
          id: ['modelUUID'],
          value: ['modelInfo', 'modelVersionId']
        }]
      },
      {
        id: 'network-serviceName',
        contents: [{
          id: ['serviceName'],
          value: ['serviceName']
        }]
      },
      {
        id: 'network-service-id',
        contents: [{
          id: ['serviceUUID'],
          value: ['serviceUUID']
        }]
      },
      {
        id: 'network-tenantName',
        contents: [{
          id: ['tenant'],
          value: ['tenantName']
        }]
      },
      {
        id: 'network-region',
        contents: [{
          id: ['region'],
          value: ['lcpCloudRegionId']
        }]
      }
    ];
  };

  sortElementsResultByField = (elements, fieldName): any[] => {
    return _.sortBy(elements, o => o[fieldName]);
  };

  shouldAddNotAssigned = (networks): boolean => {
    return _.values(networks).some(network => _.isNil(network.role) || network.role === "");
  };
}
