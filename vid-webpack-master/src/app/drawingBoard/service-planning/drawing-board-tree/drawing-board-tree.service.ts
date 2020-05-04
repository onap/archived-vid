import {Injectable} from "@angular/core";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import * as _ from 'lodash';
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {FeatureFlagsService, Features} from "../../../shared/services/featureFlag/feature-flags.service";
import {ServiceInstanceActions} from "../../../shared/models/serviceInstanceActions";
import {Subject} from "rxjs";

@Injectable()
export class  DrawingBoardTreeService {

  static triggerCheckIsDirty : Subject<string> = new Subject<string>();

  constructor(private store: NgRedux<AppState>){}
  isVFModuleMissingData(node: ITreeNode, serviceModelId : string): boolean {
    if(node.data.type === 'VFmodule' &&!_.isNil(this.store.getState().service.serviceInstance[serviceModelId].vnfs) &&  !_.isNil(this.store.getState().service.serviceInstance[serviceModelId].vnfs[node.parent.data.vnfStoreKey])){
      if(!_.isNil(this.store.getState().service.serviceInstance[serviceModelId].vnfs[node.parent.data.vnfStoreKey].vfModules)
        && !_.isNil(this.store.getState().service.serviceInstance[serviceModelId].vnfs[node.parent.data.vnfStoreKey].vfModules[node.data.modelName])
        && !_.isNil(this.store.getState().service.serviceInstance[serviceModelId].vnfs[node.parent.data.vnfStoreKey].vfModules[node.data.modelName][node.data.dynamicModelName])){

        return this.store.getState().service.serviceInstance[serviceModelId].vnfs[node.parent.data.vnfStoreKey].vfModules[node.data.modelName][node.data.dynamicModelName].isMissingData;
      }
    }
    return false;
  }

  isVNFMissingData(node : ITreeNode, serviceModelId : string) : boolean {
    if(node.data.type == 'VF'  && !_.isNil(this.store.getState().service.serviceInstance[serviceModelId].vnfs[node.data.vnfStoreKey])){
      return  this.store.getState().service.serviceInstance[serviceModelId].vnfs[node.data.vnfStoreKey].isMissingData;
    }
  }

  isViewEditFlagTrue():boolean{
    return FeatureFlagsService.getFlagState(Features.FLAG_1902_NEW_VIEW_EDIT, this.store);
  }

  isPauseVFMInstantiationCreationFlagTrue() {
    return FeatureFlagsService.getFlagState(Features.FLAG_2006_PAUSE_VFMODULE_INSTANTIATION_CREATION, this.store);
  }

  /**********************************************
   return all drawing board context menu options
   ***********************************************/
  generateContextMenuOptions() : TreeNodeContextMenuModel[]{
    return [
      new TreeNodeContextMenuModel('edit', 'context-menu-edit', 'Edit', 'edit-file-o'),
      new TreeNodeContextMenuModel('duplicate', 'context-menu-duplicate', 'Duplicate', 'copy-o'),
      new TreeNodeContextMenuModel('showAuditInfo', 'context-menu-showAuditInfo', 'Show audit info', 'eye-o'),
      new TreeNodeContextMenuModel('addGroupMember', 'context-menu-addGroupMember', 'Add group members', 'plus'),
      new TreeNodeContextMenuModel('delete', 'context-menu-delete', 'Delete', 'trash-o'),
      new TreeNodeContextMenuModel('remove', 'context-menu-remove', 'Remove', 'trash-o'),
      new TreeNodeContextMenuModel('upgrade', 'context-menu-upgrade', 'Upgrade', 'upgrade'),
      new TreeNodeContextMenuModel('undoDelete', 'context-menu-undoDelete', 'Undo Delete', 'undo-delete'),
      new TreeNodeContextMenuModel('undoUpgrade', 'context-menu-undoUpgrade', 'Undo Upgrade', 'undo-delete'),
      new TreeNodeContextMenuModel('changeAssociations', 'context-menu-changeAssociations', 'Change Associations', 'edit-file-o'),
      new TreeNodeContextMenuModel('pauseInstantiation', 'context-menu-pause', 'Add pause upon completion', 'upgrade')
    ];
  }


  /*******************************************************************
    delete or remove all service child's on delete existing service
   *******************************************************************/
  deleteActionService(nodes : ITreeNode[], serviceModelId : string){
    if(!_.isNil(nodes)){
      for(let node of nodes){
        node.data = node;
        if(!_.isNil(node.children)){
          node.children.map((child)=>{
            child.data = child;
            child.parent = node;
          });
        }

        let menuActionsName : string = node.data.action === ServiceInstanceActions.Create ? 'remove' : 'delete';
        if(!_.isNil(node.data.menuActions) && !_.isNil(node.data.menuActions[menuActionsName])){
          node.data.menuActions[menuActionsName]['method'](node, serviceModelId)
        }

      }
    }
  }
  /*******************************************************************
   undo delete all service child's on undo delete existing service
   *******************************************************************/
  undoDeleteActionService(nodes : ITreeNode[], serviceModelId : string){
    if(!_.isNil(nodes)){
      for(let node of nodes){
        node.data = node;
        if(!_.isNil(node.children)){
          node.children.map((child)=>{
            child.data = child;
            child.parent = node;
          });
        }

        if(!_.isNil(node.data.menuActions) && !_.isNil(node.data.menuActions['undoDelete'])){
          node.data.menuActions['undoDelete']['method'](node, serviceModelId)
        }
      }
    }
  }

  /***********************************************************
   return true if should add line hover the instance name
   ***********************************************************/
  isTextDecoration(node) : boolean{
    return !_.isNil(node.data) && !_.isNil(node.data.action) && node.data.action.split('_').pop() === 'Delete';
  }


  /******************************************
   should create object of instances action
   ******************************************/
  generateServiceActionObject(nodes){
    let obj = {};
    let index = 0;
    for(let node of nodes){
      obj[index] = {};
      index++;
    }
  }
}

export class TreeNodeContextMenuModel {
  methodName: string;
  dataTestId: string;
  label: string;
  iconClass: string;

  constructor(methodName: string,
              dataTestId: string,
              label: string,
              iconClass: string) {
    this.methodName = methodName;
    this.dataTestId = dataTestId;
    this.label = label;
    this.iconClass = iconClass;
  }
}
