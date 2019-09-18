import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../shared/store/reducers";
import {DragAndDropModel} from "./dragAndDrop.model";
import {FeatureFlagsService, Features} from "../../../../shared/services/featureFlag/feature-flags.service";
import * as _ from 'lodash';

@Injectable()
export class DragAndDropService {

  constructor(private store: NgRedux<AppState>){}

  isAllow(): boolean {
    return FeatureFlagsService.getFlagState(Features.FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE, this.store);
  }
  /********************************************************************
   * manage drawing-board drag and drop operation
   * @param nodes - array with elements data.
   * @param tree - tree instance
   * @param node - element information
   * @param from - element from information
   * @param to - element to information
   ************************************************************/

  drag(store, instanceId : string , nodes, {from, to}) :void{
    if (!store.getState().global.flags["FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE"]) return;

    let firstLevelNames : DragAndDropModel[] = [
        new DragAndDropModel('VF',true),
        new DragAndDropModel('VL',true),
        new DragAndDropModel('VFmodule',false)
    ];

    const fromObject = _.find(firstLevelNames, ['type', from.data.type]);
    const toObject = _.find(firstLevelNames, ['type', to.parent.data.type]);

    /***********************************************************************************************
     if the type are the same and there in same level + same parent -> then change element position
     ***********************************************************************************************/
    if(fromObject.isFirstLevel === toObject.isFirstLevel){ // moving element in the same level and in the first level
      if(fromObject.isFirstLevel){
        this.array_move(nodes, from.index , to.parent.index, instanceId);
      } else if(fromObject.isFirstLevel === toObject.isFirstLevel){
        /* check if they have the same parent */
        if(from.parent.data.trackById === to.parent.parent.data.trackById){
          let vfModules = nodes.find((parents)=> {
            return parents.trackById === to.parent.parent.data.trackById;
          }).children;
          this.array_move(vfModules, from.index , to.parent.index, instanceId, to.parent.parent.data.vnfStoreKey);
        }
      }
    }
  }


   /********************************************************************
   * move element inside array with elements position
   * @param arr - array with elements data.
   * @param originalPosition - element original position
   * @param destPosition - element dest position
   * @param destPinstanceIdosition - instance id
   ******************************************************************/
  array_move(arr, originalPosition, destPosition, instanceId : string, parentStoreKey?) {
    if (destPosition >= arr.length) {
      let k = destPosition - arr.length + 1;
      while (k--) {
        arr.push(undefined);
      }
    }
    arr.splice(destPosition, 0, arr.splice(originalPosition, 1)[0]);
    arr.forEach((item, index) => {
      if(item.position !== index){
        item.position = index;
        item.updatePoistionFunction(this, item, instanceId, parentStoreKey);
      }
    });
  };
}
