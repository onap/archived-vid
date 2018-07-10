import {Injectable} from '@angular/core';
import * as _ from "lodash";
import {ServicePlanningService} from "../../services/service-planning.service";

@Injectable()
export class AvailableModelsTreeService {
  constructor(private _servicePlanningService: ServicePlanningService) {
  }

  shouldShowAddIcon(node: any, serviceHierarchy: any, serviceModelId: string, currentNodeCount: number): boolean {
    let maxNodes: number = 1;
    if (node.data.children !== null && node.data.children.length == 0) {
      let vnfModules = serviceHierarchy[serviceModelId].vfModules;
      if (vnfModules[node.data.name]) {
        maxNodes = vnfModules[node.data.name].properties.maxCountInstances || 1;
      }
    }
    return !node.data.disabled && currentNodeCount < maxNodes
  }

  shouldOpenDialog(type: string, dynamicInputs: any, userProvidedNaming: boolean): boolean {
    if (userProvidedNaming || this._servicePlanningService.requiredFields[type].length > 0) {
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

}
