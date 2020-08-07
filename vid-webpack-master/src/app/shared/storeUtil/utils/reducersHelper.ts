import * as _ from "lodash";
import {ActionOnFirstLevel} from "./firstLevel/firstLevel.actions";
import {ServiceInstanceActions} from "../../models/serviceInstanceActions";
import {ServiceState} from "./main.reducer";

export function deleteFirstLevel(state: ServiceState, action: ActionOnFirstLevel,shouldUpdateServiceValidationCounter: boolean){
  let newState = _.cloneDeep(state);
  let firstLevel = newState.serviceInstance[action.serviceId][action.firstLevelName][action.storeKey];
  let oldAction = firstLevel.action;
  if(oldAction === ServiceInstanceActions.None_Delete || oldAction === ServiceInstanceActions.Update_Delete) return newState;
  if (shouldUpdateServiceValidationCounter){
    updateServiceValidationCounter(newState, firstLevel['isMissingData'], false, action.serviceId);
  }
  newState.serviceInstance[action.serviceId][action.firstLevelName][action.storeKey].action = (oldAction + '_Delete') as ServiceInstanceActions;
  return newState;
}

export function updateServiceValidationCounter(newState: any, oldValidationState: boolean, newValidationState: boolean, serviceUuid: string) {
  if (oldValidationState && !newValidationState) {
    newState.serviceInstance[serviceUuid].validationCounter--;
  } else if (!oldValidationState && newValidationState) {
    newState.serviceInstance[serviceUuid].validationCounter++;
  }
  resetUpgradeStatus(newState, serviceUuid);
};

function resetUpgradeStatus(newState: any, serviceUuid: string){
  newState.serviceInstance[serviceUuid].upgradedVFMSonsCounter = 0;
  newState.serviceInstance[serviceUuid].isUpgraded = false;
}
