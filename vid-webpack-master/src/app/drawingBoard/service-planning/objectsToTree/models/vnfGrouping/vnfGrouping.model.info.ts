import {ILevelNodeInfo} from "../basic.model.info";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {VnfGroupModel} from "../../../../../shared/models/vnfGroupModel";
import {VnfGroupTreeNode} from "../../../../../shared/models/vnfGroupTreeNode";
import {SharedTreeService} from "../../shared.tree.service";
import {VnfGroupPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vnfGroup/vnfGroup.popup.service";
import {
  GenericFormPopupComponent,
  PopupType
} from "../../../../../shared/components/genericFormPopup/generic-form-popup.component";
import {DialogService} from 'ng2-bootstrap-modal';
import {AppState} from "../../../../../shared/store/reducers";
import {NgRedux} from "@angular-redux/store";
import {changeInstanceCounter, removeInstance} from "../../../../../shared/storeUtil/utils/general/general.actions";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {
  deleteActionVnfGroupInstance,
  undoDeleteActionVnfGroupInstance
} from "../../../../../shared/storeUtil/utils/vnfGroup/vnfGroup.actions";
import {RelatedVnfMemberInfoModel} from "../relatedVnfMember/relatedVnfMember.info.model";
import {SearchElementsModalComponent} from "../../../../../shared/components/searchMembersModal/search-elements-modal.component";
import * as _ from "lodash";
import {MessageBoxData} from "../../../../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../../../../shared/components/messageBox/messageBox.service";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {Level1Instance} from "../../../../../shared/models/level1Instance";
import {AaiService} from "../../../../../shared/services/aaiService/aai.service";
import {Observable} from "rxjs";
import {createRelatedVnfMemberInstance} from "../../../../../shared/storeUtil/utils/relatedVnfMember/relatedVnfMember.actions";
import {
  ITableContent,
  SearchFieldItemType
} from "../../../../../shared/components/searchMembersModal/members-table/element-table-row.model";
import {CustomTableColumnDefinition} from "../../../../../shared/components/searchMembersModal/members-table/elements-table.component";

export class VnfGroupingModelInfo implements ILevelNodeInfo {
  constructor(private _dynamicInputsService: DynamicInputsService,
              private _sharedTreeService: SharedTreeService,
              private _dialogService: DialogService,
              private _vnfGroupPopupService: VnfGroupPopupService,
              private _iframeService: IframeService,
              private _aaiService: AaiService,
              private _store: NgRedux<AppState>) {
  }

  name: string = 'vnfGroups';
  type: string = 'VnfGroup';
  typeName: string = 'G';
  childNames: string[] = ['vnfs'];
  componentInfoType = ComponentInfoType.VNFGROUP;
  limitMembers: number;

  /***********************************************************
   * return if user should provide instance name or not.
   *        get info from parent (VNF)
   * @param currentModel - current Model object
   ************************************************************/
  isEcompGeneratedNaming(currentModel): boolean {
    const ecompGeneratedNaming = currentModel.properties.ecomp_generated_naming;
    return ecompGeneratedNaming === "true";
  }

  /***********************************************************
   * return model dynamic inputs
   * @param currentModel - current Model object
   ************************************************************/
  updateDynamicInputsDataFromModel = (currentModel): any => {
    let displayInputs = currentModel.inputs;
    return _.isEmpty(displayInputs) ? [] : this._dynamicInputsService.getArbitraryInputs(displayInputs);
  };

  /***********************************************************
   * return next level object (null because is last level)
   ************************************************************/
  getNextLevelObject = (): RelatedVnfMemberInfoModel => {
    return new RelatedVnfMemberInfoModel(this._sharedTreeService, this._dynamicInputsService, this._store);
  };

  getTooltip = (): string => 'VnfGroup';

  getType = (): string => 'VnfGroup';

  createInstanceTreeNode(instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any {
    let node = new VnfGroupTreeNode(instance, model, storeKey);
    node.missingData = this.hasMissingData(instance, node, model.isEcompGeneratedNaming);
    node = this._sharedTreeService.addingStatusProperty(node);
    node.typeName = this.typeName;
    node.menuActions = this.getMenuAction(<any>node, serviceModelId);
    node.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    node.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";
    node.limitMembers = (!_.isNil(model.properties.quantity)) ? model.properties.quantity : null;
    return node;
  }

  getModel(instanceModel: any): any {
    return new VnfGroupModel(instanceModel);
  }

  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return this._sharedTreeService.hasMissingData(instance, dynamicInputs, isEcompGeneratedNaming, []);
  }

  onClickAdd(node: ITreeNode, serviceModelId: string): void {
    this._dialogService.addDialog(GenericFormPopupComponent, {
      type: PopupType.VNF_GROUP,
      uuidData: <any>{
        serviceId: serviceModelId,
        modelName: node.data.name,
        vnfGroupStoreKey: null,
        modelId: node.data.modelVersionId,
        type: node.data.type,
        popupService: this._vnfGroupPopupService
      },
      node: node,
      isUpdateMode: false
    });
  }

  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    let map = null;
    if (!_.isNil(this._store.getState().service.serviceInstance[serviceModelId])) {
      map = this._store.getState().service.serviceInstance[serviceModelId].existingVnfGroupCounterMap || 0;
      if (!_.isNil(map)) {
        let count = map[node.data.modelUniqueId] || 0;
        count -= this._sharedTreeService.getExistingInstancesWithDeleteMode(node, serviceModelId, 'vnfGroups');
        return count;
      }
    }
    return 0;
  }

  /***********************************************************
   * should show node icon
   * @param node - current ITrees node
   * @param serviceModelId - service id
   ************************************************************/
  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    const showAddIcon = this._sharedTreeService.shouldShowAddIcon();
    return new AvailableNodeIcons(showAddIcon, false);
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    return {
      edit: {
        method: (node, serviceModelId) => {
          this._iframeService.addClassOpenModal('content');
          this._dialogService.addDialog(GenericFormPopupComponent, {
            type: PopupType.VNF_GROUP,
            uuidData: <any>{
              serviceId: serviceModelId,
              modelName: node.data.modelName,
              vnfGroupStoreKey: node.data.vnfGroupStoreKey,
              modelId: node.data.modelId,
              type: node.data.type,
              popupService: this._vnfGroupPopupService
            },
            node: node,
            isUpdateMode: true
          });
        },
        visible: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node)
      },
      remove: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            let storeKey: string = node.data.vnfGroupStoreKey;
            this._store.dispatch(removeInstance(node.data.vnfGroupStoreKey, serviceModelId, storeKey, node));
            this._store.dispatch(changeInstanceCounter(node.data.modelUniqueId, serviceModelId, -1, node));
            this._sharedTreeService.selectedVNF = null;
          } else {
            let messageBoxData: MessageBoxData = new MessageBoxData(
              "Remove VNFGroup",  // modal title
              "You are about to remove this group and all its children from this service. Are you sure you want to remove it?",
              <any>"warning",
              <any>"md",
              [
                {
                  text: "Remove Group",
                  size: "large",
                  callback: this.removeGroup.bind(this, node, serviceModelId),
                  closeModal: true
                },
                {text: "Don’t Remove", size: "medium", closeModal: true}
              ]);

            MessageBoxService.openModal.next(messageBoxData);
          }
        },
        visible: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node)
      },

      addGroupMember: {
        method: (node, serviceModelId) => {
          let serviceHierarchy = this._store.getState().service.serviceHierarchy[serviceModelId];
          let serviceInstance = this._store.getState().service.serviceInstance[serviceModelId];
          let vnfGroupModel = new VnfGroupModel(serviceHierarchy['vnfGroups'][node.data.modelName]);
          this._dialogService.addDialog(SearchElementsModalComponent, {
              modalInformation: {
                type: 'VNF',
                serviceModelId : serviceModelId,
                title: 'Add members to group',
                description: 'Select VNF instances to associate',
                noElementsMsg: 'No VNFs were found that can belong to this group.',
                uniqObjectField: 'instanceId',
                topButton: {
                  text: 'SET MEMBERS',
                  /********************************************************************************************************************************
                   iterate over all current elements:

                   1) if element is selected then update REDUX store
                   2) if element is not selected then delete member

                   @searchElementsModalComponent - all modal information (allElementsStatusMap, vnfGroupStoreKey, serviceId)
                   ********************************************************************************************************************************/

                  action: (searchElementsModalComponent) => {
                    let tmpMembers = searchElementsModalComponent._membersTableService.allElementsStatusMap;
                    for (let key in tmpMembers) {
                      if (tmpMembers[key].isSelected) {
                        this._store.dispatch(createRelatedVnfMemberInstance(node.data.vnfGroupStoreKey, serviceModelId, tmpMembers[key]));
                      }
                    }
                    searchElementsModalComponent.closeDialog();
                  }
                },
                getElements: (): Observable<Level1Instance[]> => {
                  return this._aaiService.getOptionalGroupMembers(serviceModelId, serviceInstance.globalSubscriberId, serviceInstance.subscriptionServiceType, (Object.values(vnfGroupModel.members))[0].sourceModelInvariant, vnfGroupModel.properties.type, vnfGroupModel.properties.role).map((result) => {
                    return this.filterUsedVnfMembers(serviceModelId, result);
                  });
                },
                tableHeaders : this.getTableHeaders(),
                tableContent: this.generateRelatedMemberTableContent(),
                searchFields: [{
                  title: 'Service model name',
                  dataTestId: 'sourceModelName',
                  value: (Object.values(vnfGroupModel.members))[0].sourceModelName,
                  type: SearchFieldItemType.LABEL
                },
                  {
                    title: 'Service invariant UUID',
                    dataTestId: 'sourceModelInvariant',
                    value: (Object.values(vnfGroupModel.members))[0].sourceModelInvariant,
                    type: SearchFieldItemType.LABEL
                  }]
              }
            }
          );
        },
        visible: (node) => !_.isNil(node.data.action) ? node.data.action.split('_').pop() !== 'Delete' : true,
        enable: (node) => !_.isNil(node.data.action) ? node.data.action.split('_').pop() !== 'Delete' : true
      },
      showAuditInfo: {
        method: (node, serviceModelId) => {
          let instance = this._store.getState().service.serviceInstance[serviceModelId].vnfGroups[node.data.vnfGroupStoreKey];
          this._sharedTreeService.openAuditInfoModal(node, serviceModelId, instance, 'VNFGROUP', this);
        },
        visible: (node) => this._sharedTreeService.shouldShowAuditInfo(node),
        enable: (node) => this._sharedTreeService.shouldShowAuditInfo(node)
      },
      delete: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            this._store.dispatch(deleteActionVnfGroupInstance(node.data.vnfGroupStoreKey, serviceModelId));
          } else {
            this._sharedTreeService.shouldShowDeleteInstanceWithChildrenModal(node, serviceModelId, (node, serviceModelId) => {
              this._sharedTreeService.removeDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
                this._store.dispatch(deleteActionVnfGroupInstance(node.data.vnfGroupStoreKey, serviceModelId));
              });
            });
          }
        },
        visible: (node) => this._sharedTreeService.shouldShowDelete(node, serviceModelId),
        enable: (node) => this._sharedTreeService.shouldShowDelete(node, serviceModelId)
      },
      undoDelete: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            this._store.dispatch(undoDeleteActionVnfGroupInstance(node.data.vnfGroupStoreKey, serviceModelId));
          } else {
            this._sharedTreeService.undoDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
              this._store.dispatch(undoDeleteActionVnfGroupInstance(node.data.vnfGroupStoreKey, serviceModelId));
            });
          }
        },
        visible: (node) => this._sharedTreeService.shouldShowUndoDelete(node),
        enable: (node, serviceModelId) => this._sharedTreeService.shouldShowUndoDelete(node) && !this._sharedTreeService.isServiceOnDeleteMode(serviceModelId)
      }
    };


  }


  generateRelatedMemberTableContent(): ITableContent[] {
    return [
      {
        id: 'vnfName',
        contents: [{
          id: ['instanceName'],
          value: ['instanceName']
        }, {
          id: ['instanceId'],
          value: ["instanceId"],
          prefix: 'UUID: '
        }]
      },
      {
        id: 'version',
        contents: [{
          id: ['modelInfo', 'modelVersion'],
          value: ['modelInfo', 'modelVersion']
        }]
      },
      {
        id: 'modelName',
        contents: [{
          id: ['modelInfo', 'modelName'],
          value: ['modelInfo', 'modelName']
        }]
      },
      {
        id: 'provStatus',
        contents: [{
          id: ['provStatus'],
          value: ['provStatus']
        }]
      },
      {
        id: 'serviceInstance',
        contents: [{
          id: ['serviceInstanceName'],
          value: ['serviceInstanceName']
        }, {
          id: ['serviceInstanceId'],
          value: ["serviceInstanceId"],
          prefix: 'UUID: '
        }]
      },
      {
        id: 'cloudRegion',
        contents: [{
          id: ['lcpCloudRegionId'],
          value: ['lcpCloudRegionId']
        }]
      },
      {
        id: 'tenantName',
        contents: [{
          id: ['tenantName'],
          value: ['tenantName']
        }]
      }
    ];
  }

  getTableHeaders() : CustomTableColumnDefinition[]{
    const type : string = 'VNF';
    return  [
      {displayName: `${type} instance name`, key: ['instanceName']},
      {displayName: `${type} version`, key: ['modelInfo', 'modelVersion']},
      {displayName: `${type} model name`, key: ['modelInfo', 'modelName']},
      {displayName: 'Prov Status', key: ['provStatus']},
      {displayName: 'Service instance name', key: ['serviceInstanceName']},
      {displayName: 'Cloud Region', key: ['lcpCloudRegionId']},
      {displayName: 'Tenant Name', key: ['tenantName']}
    ];
  }

  filterUsedVnfMembers = (serviceModelId: string, result: Level1Instance[]): Level1Instance[] => {
    const allMembersMap = _.keyBy(result as Level1Instance[], 'instanceId');
    const vnfGroupsData = this._store.getState().service.serviceInstance[serviceModelId].vnfGroups;
    const vnfMembersArr = _.flatMap(vnfGroupsData).map((vnfGroup) => vnfGroup.vnfs);
    for (let vnf of vnfMembersArr) {
      for (let member in vnf) {
        delete allMembersMap[member];
      }
    }
    return _.flatMap(allMembersMap);
  };

  removeGroup(this, node, serviceModelId) {
    this._store.dispatch(removeInstance(node.data.modelName, serviceModelId, node.data.vnfGroupStoreKey, node));
    this._store.dispatch(changeInstanceCounter(node.data.modelUniqueId, serviceModelId, -1, node));
    this._sharedTreeService.selectedVNF = null;
  }

  updatePosition(that, node, instanceId): void {
    // TODO
  }

  getNodePosition(instance): number {
    return !_.isNil(instance) ? instance.position : null;
  }

  getInfo(model, instance): ModelInformationItem[] {
    return [];
  }

}
