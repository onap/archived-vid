import {Injectable} from '@angular/core';
import {DefaultDataGeneratorService} from "../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {MessageBoxData} from "../../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../../shared/components/messageBox/messageBox.service";
import * as _ from "lodash";
import { SdcUiCommon} from "onap-ui-angular";
import {SharedTreeService} from "../objectsToTree/shared.tree.service";

export class AvailableNodeIcons {
   addIcon: boolean;
   vIcon: boolean;

  constructor(addIcon: boolean, vIcon: boolean) {
    this.addIcon = addIcon;
    this.vIcon = vIcon;
  }
}

@Injectable()
export class AvailableModelsTreeService {
  constructor(private _defaultDataGeneratorService: DefaultDataGeneratorService,
              private store: NgRedux<AppState>,
              public _shareTreeService : SharedTreeService) {
  }



  shouldOpenDialog(type: string, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    if (!isEcompGeneratedNaming || this._defaultDataGeneratorService.requiredFields[type].length > 0) {
      return true;
    }

    if (dynamicInputs) {
      for(let input of dynamicInputs) {
        if (input.isRequired && _.isEmpty(input.value)) {
          return true;
        }
      }
    }
    return false;
  }

  getOptionalVNFs(serviceUUID: string, vnfOriginalModelName : string) : any[] {
    let result = [];
    if(!_.isNil(this.store.getState().service.serviceInstance) && !_.isNil(this.store.getState().service.serviceInstance[serviceUUID])){
      const serviceVNFsInstances = this.store.getState().service.serviceInstance[serviceUUID].vnfs;
      for(let vnfKey in serviceVNFsInstances){
        if(serviceVNFsInstances[vnfKey].originalName === vnfOriginalModelName){
          serviceVNFsInstances[vnfKey].vnfStoreKey = vnfKey;
          result.push(serviceVNFsInstances[vnfKey]);
        }
      }
    }


    return result;
  }



  addingAlertAddingNewVfModuleModal() : void {
    let messageBoxData : MessageBoxData = new MessageBoxData(
      "Select a parent",  // modal title
      "There are multiple instances on the right side that can contain this vf-module Please select the VNF instance, to add this vf-module to, on the right side and then click the + sign",
      SdcUiCommon.ModalType.warning,
      SdcUiCommon.ModalSize.medium,
      [
        {text:"Close", size:"medium", closeModal:true}
      ]);

    MessageBoxService.openModal.next(messageBoxData);
  }

}
