import * as _ from "lodash";
import {ActionOnFirstLevel} from "./firstLevel/firstLevel.actions";
import {ServiceInstanceActions} from "../../models/serviceInstanceActions";
import {ServiceState} from "./main.reducer";
import {ServiceInstance} from "../../models/serviceInstance";

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

export const updateUniqueNames = (oldName: string, newName: string, serviceInstance: ServiceInstance): void => {
  let existingNames = serviceInstance.existingNames;
  if (!_.isNil(oldName) && oldName.toLowerCase() in existingNames) {
    delete existingNames[oldName.toLowerCase()];
  }
  if (!_.isNil(newName)) {
    existingNames[newName.toLowerCase()] = "";
  }
};


export const calculateNextUniqueModelName = (nfModelName: string, serviceId: string, state: any, levelName: string): string => {
  let counter: number = null;
  while (true) {
    let pattern = !_.isNil(counter) ? ("_" + counter) : "";
    if (!_.isNil(state.serviceInstance[serviceId][levelName][nfModelName + pattern])) {
      counter = counter ? (counter + 1) : 1;
    } else {
      return nfModelName + pattern;
    }
  }
};
