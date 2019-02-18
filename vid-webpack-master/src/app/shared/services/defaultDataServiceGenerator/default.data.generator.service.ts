import {Injectable} from '@angular/core';
import * as _ from 'lodash';

import {NgRedux} from '@angular-redux/store';
import {AppState} from '../../store/reducers';
import {VnfTreeNode} from "../../models/vnfTreeNode";
import {VfModuleInstance} from "../../models/vfModuleInstance";
import {VfModule} from "../../models/vfModule";
import {VfModuleTreeNode} from "../../models/vfModuleTreeNode";
import {InputType} from "../../models/inputTypes";
import {ServiceNodeTypes} from "../../models/ServiceNodeTypes";
import {Constants} from "../../utils/constants";
import {Utils} from "../../utils/utils";
import {NetworkTreeNode} from "../../models/networkTreeNode";
import {createVNFInstance} from "../../storeUtil/utils/vnf/vnf.actions";
import {changeInstanceCounter} from "../../storeUtil/utils/general/general.actions";
import {createNetworkInstance} from "../../storeUtil/utils/network/network.actions";
import {createVFModuleInstance} from "../../storeUtil/utils/vfModule/vfModule.actions";
import {createVnfGroupInstance} from "../../storeUtil/utils/vnfGroup/vnfGroup.actions";
import {VnfGroupTreeNode} from "../../models/vnfGroupTreeNode";
import {ModelInfo} from "../../models/modelInfo";
import {ServiceInstanceActions} from "../../models/serviceInstanceActions";
import Parameter = Constants.Parameter;

@Injectable()
export class DefaultDataGeneratorService {
  static controlsFieldsStatus = {};
  public requiredFields = {
    VF: [InputType.LCP_REGION, InputType.TENANT, InputType.PLATFORM],
    Network: [InputType.LCP_REGION, InputType.TENANT, InputType.PLATFORM],
    VL: [InputType.LCP_REGION, InputType.TENANT, InputType.PLATFORM],
    VFmodule: [],
    VnfGroup: []
  };

  constructor(private store: NgRedux<AppState>) {
  }

  public getArbitraryInputs(inputs) {
    let parameter;
    let parameterList = [];
    for (let key in inputs) {
      parameter = {
        id: key,
        type: Parameter.STRING,
        name: key,
        value: inputs[key][Parameter.DEFAULT],
        isRequired: inputs[key][Parameter.REQUIRED],
        description: inputs[key][Parameter.DESCRIPTION]
      };
      switch (inputs[key][Parameter.TYPE]) {
        case Parameter.INTEGER:
          parameter.type = Parameter.NUMBER;
          break;
        case Parameter.BOOLEAN:
          parameter.type = Parameter.BOOLEAN;
          break;
        case Parameter.RANGE:
          break;
        case Parameter.LIST:
          parameter.type = Parameter.LIST;
          break;
        case Parameter.MAP:
          parameter.type = Parameter.MAP;
          break;
      }
      if (Utils.hasContents(inputs[key][Parameter.CONSTRAINTS])
        && ( inputs[key][Parameter.CONSTRAINTS].length > 0 )) {
        let constraintsArray = inputs[key][Parameter.CONSTRAINTS];
        this.addConstraintParameters(parameterList, constraintsArray, key, inputs, parameter);
      }
      else {

        parameterList.push(parameter);
      }
    }
    return parameterList;
  }

  private addConstraintParameters(parameterList, constraintsArray, key, inputs, parameter) {
    // If there are constraints and the operator is "valid_values",
    // use a select parameter type.
    let i: number = constraintsArray.length;
    let parameterPushed: boolean = false;
    if (i > 0) {
      while ((i--) && (!parameterPushed)) {
        let keys = Object.keys(constraintsArray[i]);
        for (let operator in keys) {
          switch (keys[operator]) {
            case Parameter.VALID_VALUES:
              let j: number = constraintsArray[i][Parameter.VALID_VALUES].length;
              if (j > 0) {
                let oList = [];
                let option;
                while (j--) {
                  option = {
                    name: constraintsArray[i][Parameter.VALID_VALUES][j],
                    isDefault: false
                  };
                  if ((Utils.hasContents(inputs[key][Parameter.DEFAULT]) )
                    && (inputs[key][Parameter.DEFAULT] === constraintsArray[i][Parameter.VALID_VALUES][j] )) {
                    option = {
                      name: constraintsArray[i][Parameter.VALID_VALUES][j],
                      isDefault: true
                    }
                  }
                  oList.push(option);
                }
                parameter.type = Parameter.SELECT;
                parameter.optionList = oList;
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;

            case Parameter.EQUAL:
              if (constraintsArray[i][Parameter.EQUAL] != null) {
                parameter.type = Parameter.STRING;
                parameter.isReadOnly = true;
                parameter.value = constraintsArray[i][Parameter.EQUAL];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;

            case Parameter.LENGTH:
              if (constraintsArray[i][Parameter.LENGTH] != null) {
                parameter.minLength = constraintsArray[i][Parameter.LENGTH];
                parameter.maxLength = constraintsArray[i][Parameter.LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.MAX_LENGTH:
              if (constraintsArray[i][Parameter.MAX_LENGTH] != null) {
                parameter.maxLength = constraintsArray[i][Parameter.MAX_LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.MIN_LENGTH:
              if (constraintsArray[i][Parameter.MIN_LENGTH] != null) {
                parameter.minLength = constraintsArray[i][Parameter.MIN_LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.IN_RANGE:
              if (constraintsArray[i][Parameter.IN_RANGE] != null) {
                if (constraintsArray[i][Parameter.IN_RANGE].length > 1) {
                  parameter.min = constraintsArray[i][Parameter.IN_RANGE][0];
                  parameter.max = constraintsArray[i][Parameter.IN_RANGE][1];
                  parameter.type = Parameter.NUMBER;
                  parameter.value = inputs[key][Parameter.DEFAULT];
                  parameterList.push(parameter);
                  parameterPushed = true;
                }
              }
              break;
            case Parameter.GREATER_THAN:
              if (constraintsArray[i][Parameter.GREATER_THAN] != null) {
                parameter.type = Parameter.NUMBER;
                parameter.min = constraintsArray[i][Parameter.GREATER_THAN];
                parameter.value = inputs[key][Parameter.DEFAULT];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
          }
        }
      }
    }
  };

  updateDynamicInputsVnfDataFromModel(modelType: string, model: any): any[] {
    let displayInputs;
    if (modelType === ServiceNodeTypes.VFmodule) {
      displayInputs = model.inputs;
    }
    return _.isEmpty(displayInputs) ? [] : this.getArbitraryInputs(displayInputs);
  }

  updateNetworksOnFirstSet(serviceId: string, formServiceValues: any){
    const serviceHierarchy = this.store.getState().service.serviceHierarchy[serviceId];
    if (serviceHierarchy && !_.isEmpty(serviceHierarchy.networks)) {
      for (let networkUUID in serviceHierarchy.networks) {
        const isEcompGeneratedNaming = this.getIsEcompGeneratedNaming(serviceHierarchy.networks[networkUUID]);
        let min_vnf_instances_greater_than_0 = serviceHierarchy.networks[networkUUID].properties['min_instances'] && serviceHierarchy.networks[networkUUID].properties['min_instances'] > 0;
        if(this.store.getState().global.flags['FLAG_DEFAULT_VNF'] &&  min_vnf_instances_greater_than_0)
        {
          this.createNetworkInstanceReduxIfNotExist(
            serviceId,
            this.generateNetworkData(serviceHierarchy, networkUUID, formServiceValues, isEcompGeneratedNaming)
          );
        }
      }
    }
  }

  updateVnfGroupsOnFirstSet(serviceId: string, formServiceValues: any){
    const serviceHierarchy = this.store.getState().service.serviceHierarchy[serviceId];
    if (serviceHierarchy && !_.isEmpty(serviceHierarchy.vnfGroups)) {
      for (let vnfGroupUUID in serviceHierarchy.vnfGroups) {
        const isEcompGeneratedNaming = this.getIsEcompGeneratedNaming(serviceHierarchy.vnfGroups[vnfGroupUUID]);
        let min_vnf_group_instances_greater_than_0 = serviceHierarchy.vnfGroups[vnfGroupUUID].properties['min_instances'] && serviceHierarchy.vnfGroups[vnfGroupUUID].properties['min_instances'] > 0;
        if(this.store.getState().global.flags['FLAG_DEFAULT_VNF'] &&  min_vnf_group_instances_greater_than_0)
        {
          this.createVnfGroupInstanceReduxIfNotExist(
            serviceId,
            this.generateVnfGroupData(serviceHierarchy, vnfGroupUUID, formServiceValues, isEcompGeneratedNaming)
          );
        }
      }
    }
  }

  updateReduxOnFirstSet(serviceId: string, formServiceValues: any): void {
    this.updateNetworksOnFirstSet(serviceId, formServiceValues);
    this.updateVnfGroupsOnFirstSet(serviceId, formServiceValues);
    const serviceHierarchy = this.store.getState().service.serviceHierarchy[serviceId];
    if (serviceHierarchy && !_.isEmpty(serviceHierarchy.vnfs)) {
      for (let vnfUUID in serviceHierarchy.vnfs) {
        const isEcompGeneratedNaming = this.getIsEcompGeneratedNaming(serviceHierarchy.vnfs[vnfUUID]);
        for (let vnfModuleUUID in serviceHierarchy.vnfs[vnfUUID].vfModules) {
          const vfModuleModel = serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID];
          if (vfModuleModel.properties.minCountInstances > 0) {
            let vfModule = this.generateVFModule(vfModuleModel, this.updateDynamicInputsVnfDataFromModel(ServiceNodeTypes.VFmodule, vfModuleModel), isEcompGeneratedNaming, formServiceValues.isALaCarte);
            if (vfModuleModel.properties.initialCount > 0) {
              this.createVNFInstanceReduxIfNotExist(
                serviceId,
                this.generateVNFData(serviceHierarchy, vnfUUID, formServiceValues, isEcompGeneratedNaming)
              );

              this.addDefaultVfModulesInRedux(
                serviceId,
                vfModuleModel.properties.initialCount,
                vfModule,
                vnfModuleUUID,
                vnfUUID
              )

            }
          }
        }

        let min_vnf_instances_greater_than_0 = serviceHierarchy.vnfs[vnfUUID].properties['min_instances'] && serviceHierarchy.vnfs[vnfUUID].properties['min_instances'] > 0;
        if(this.store.getState().global.flags['FLAG_DEFAULT_VNF'] &&  min_vnf_instances_greater_than_0)
        {
          this.createVNFInstanceReduxIfNotExist(
            serviceId,
            this.generateVNFData(serviceHierarchy, vnfUUID, formServiceValues, isEcompGeneratedNaming)
          );
        }
      }
    }
  }


  private getIsEcompGeneratedNaming(vnfJson) {
    const ecompGeneratedNaming = vnfJson.properties.ecomp_generated_naming;
    return ecompGeneratedNaming === "true";
  };

  createVNFInstanceReduxIfNotExist(serviceId: string, vnfData: any): void {
    if(!this.store.getState().service.serviceInstance[serviceId].vnfs[vnfData.modelInfo.modelCustomizationName]){
      this.store.dispatch(createVNFInstance(vnfData, vnfData.modelInfo.modelCustomizationName, serviceId));
      this.store.dispatch(changeInstanceCounter(vnfData.modelInfo.modelUniqueId, serviceId, 1, <any> {data : {type : 'VF'}}));
    }
  }

  createNetworkInstanceReduxIfNotExist(serviceId: string, networkData: any): void {
    if(!this.store.getState().service.serviceInstance[serviceId].vnfs[networkData.modelInfo.modelCustomizationName]){
      this.store.dispatch(createNetworkInstance(networkData, networkData.modelInfo.modelCustomizationName, serviceId));
      this.store.dispatch(changeInstanceCounter(networkData.modelInfo.modelUniqueId, serviceId, 1, <any> {data : {type : 'VL'}}));
    }
  }

  createVnfGroupInstanceReduxIfNotExist(serviceId: string, vnfGroupData: any): void {
    if(!this.store.getState().service.serviceInstance[serviceId].vnfGroups[vnfGroupData.modelInfo.modelCustomizationName]){
      this.store.dispatch(createVnfGroupInstance(vnfGroupData, vnfGroupData.modelInfo.modelCustomizationName, serviceId));
      this.store.dispatch(changeInstanceCounter(vnfGroupData.modelInfo.modelUniqueId , serviceId, 1, <any> {data : {type : 'VnfGroup'}}));
    }
  }

  addDefaultVfModulesInRedux(serviceId: string, numberOfVfModules: number, vfModuleData: any, vfModuleName: string, vnfUUID : string){
    for (let i = 0; i < numberOfVfModules; i++) {
      this.store.dispatch(createVFModuleInstance(vfModuleData, vfModuleName, serviceId, null, vnfUUID));
    }
  }

  generateVnfGroupInstance(vnfGroupModel: any, isEcompGeneratedNaming : boolean, isALaCarte: boolean, instanceName: string) {
    let modelInfo = new ModelInfo(vnfGroupModel);
    let instanceParams = {};
    return {
      'uuid' : modelInfo.uuid,
      'action': ServiceInstanceActions.Create,
      'instanceName': (!isEcompGeneratedNaming) ? instanceName : null,
      'isMissingData' : false,
      'modelInfo': modelInfo,
      'rollbackOnFailure' : "true",
      'instanceParams': [
        instanceParams
      ],
      'trackById': DefaultDataGeneratorService.createRandomTrackById()
    };
  }


  generateVFModule(vfModule: any, dynamicInputs: any, isEcompGeneratedNaming : boolean, isALaCarte: boolean) {
    let instanceParams = {};
    dynamicInputs.forEach(field => {
      instanceParams[field.id] = field.value;
    });
    return {
      'isMissingData' : this.setIsMissingData(ServiceNodeTypes.VFmodule, dynamicInputs, isEcompGeneratedNaming, isALaCarte),
      'sdncPreReload': null,
      'modelInfo': {
        'modelType': 'VFmodule',
        'modelInvariantId': vfModule.invariantUuid,
        'modelVersionId': vfModule.uuid,
        'modelName': vfModule.name,
        'modelVersion': vfModule.version,
        'modelCustomizationId': vfModule.customizationUuid,
        'modelCustomizationName': vfModule.modelCustomizationName,
        'modelUniqueId' : vfModule.customizationUuid || vfModule.uuid
      },
      'instanceParams': [
        instanceParams
      ],
      'trackById': DefaultDataGeneratorService.createRandomTrackById(),
    };
  }

  setIsMissingData(type: string, dynamicInputs: any, isEcompGeneratedNaming: boolean, isAlaCarte?: boolean): boolean {
    if (isAlaCarte || !isEcompGeneratedNaming || this.requiredFields[type].length > 0) {
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

  generateVNFData(serviceHierarchy: any, vnfName: string, formValues: any, isEcompGeneratedNaming) {
    return {
      'uuid' : serviceHierarchy.vnfs[vnfName].uuid,
      'isMissingData' :this.setIsMissingData(ServiceNodeTypes.VF, [], isEcompGeneratedNaming),
      'productFamilyId': formValues.productFamilyId,
      'lcpCloudRegionId': null,
      'tenantId': null,
      'lineOfBusiness': null,
      'platformName': null,
      'modelInfo': {
        'modelType': 'VF',
        'modelInvariantId': serviceHierarchy.vnfs[vnfName].invariantUuid,
        'modelVersionId': formValues.modelInfo.modelVersionId,
        'modelName': serviceHierarchy.vnfs[vnfName].name,
        'modelVersion': serviceHierarchy.vnfs[vnfName].version,
        'modelCustomizationId': serviceHierarchy.vnfs[vnfName].customizationUuid,
        'modelCustomizationName': serviceHierarchy.vnfs[vnfName].modelCustomizationName,
        'modelUniqueId' : serviceHierarchy.vnfs[vnfName].customizationUuid || serviceHierarchy.vnfs[vnfName].uuid,
      },
      'trackById': DefaultDataGeneratorService.createRandomTrackById(),
    }
  }

  generateNetworkData(serviceHierarchy: any, networkName: string, formValues: any, isEcompGeneratedNaming) {
      return {
        'uuid' : serviceHierarchy.network[networkName].uuid,
        'isMissingData' :this.setIsMissingData(ServiceNodeTypes.VL, [], isEcompGeneratedNaming),
        'productFamilyId': formValues.productFamilyId,
        'lcpCloudRegionId': null,
        'tenantId': null,
        'lineOfBusiness': null,
        'platformName': null,
        'modelInfo': {
          'modelType': 'VF',
          'modelInvariantId': serviceHierarchy.network[networkName].invariantUuid,
          'modelVersionId': formValues.modelInfo.modelVersionId,
          'modelName': serviceHierarchy.network[networkName].name,
          'modelVersion': serviceHierarchy.network[networkName].version,
          'modelCustomizationId': serviceHierarchy.network[networkName].modelCustomizationId,
          'modelCustomizationName': serviceHierarchy.network[networkName].modelCustomizationName,
          'modelUniqueId' : serviceHierarchy.network[networkName].modelCustomizationId || serviceHierarchy.network[networkName].uuid,
        },
        'trackById': DefaultDataGeneratorService.createRandomTrackById(),
      }
    }

  generateVnfGroupData(serviceHierarchy: any, vnfGroupName: string, formValues: any, isEcompGeneratedNaming) {
    return {
      'uuid' : serviceHierarchy.vnfGroups[vnfGroupName].uuid,
      'isMissingData' :this.setIsMissingData(ServiceNodeTypes.VnfGroup, [], isEcompGeneratedNaming),
      'platformName': null,
      'modelInfo': {
        'modelType': 'VnfGroup',
        'modelInvariantId': serviceHierarchy.vnfGroups[vnfGroupName].invariantUuid,
        'modelVersionId': formValues.modelInfo.modelVersionId,
        'modelName': serviceHierarchy.vnfGroups[vnfGroupName].name,
        'modelVersion': serviceHierarchy.vnfGroups[vnfGroupName].version,
        'modelCustomizationId': serviceHierarchy.vnfGroups[vnfGroupName].modelCustomizationId,
        'modelCustomizationName': serviceHierarchy.vnfGroups[vnfGroupName].modelCustomizationName,
        'modelUniqueId' : serviceHierarchy.vnfGroups[vnfGroupName].modelCustomizationId || serviceHierarchy.vnfGroups[vnfGroupName].uuid,

      },
      'trackById': DefaultDataGeneratorService.createRandomTrackById(),
    }
  }


  static createRandomTrackById() {
    return Math.random().toString(36).slice(2);
  }

  private checkMissingData(instance, type: string, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    if (!isEcompGeneratedNaming && _.isEmpty(instance.instanceName)) {
      return true;
    }

    for (let field of this.requiredFields[type]) {
      if (_.isEmpty(instance[field])) {
        return true;
      }
    }

    for (let field of dynamicInputs) {
      if (field.isRequired && !_.isNil(instance.instanceParams) && _.isEmpty(instance.instanceParams[0][field.id])) {
        return true;
      }
    }

    return false;
  }

  createNewTreeNode(instance: any, model: any, storeKey : string, type : string): VnfTreeNode {
    let tmp  = null;
    if(type === 'vnfs') {
      tmp = new VnfTreeNode(instance, model, storeKey);
    }else if (type === 'vnfGroups') {
      tmp = new VnfGroupTreeNode(instance, model, storeKey);
    }else {
      tmp = new NetworkTreeNode(instance, model, storeKey);
    }
    tmp.missingData = this.checkMissingData(instance, ServiceNodeTypes.VF, [], model.isEcompGeneratedNaming);

    return tmp;
  }

  createNewVfModuleTreeNode(instance: VfModuleInstance, vfModuleModel: VfModule, vfModuleModelName: string, isEcompGeneratedNamig: boolean, dynamicInputs, dynamicModelName  :string): VfModuleTreeNode {
    let newVfModule: VfModuleTreeNode = new VfModuleTreeNode(instance, vfModuleModel, vfModuleModelName, dynamicInputs, isEcompGeneratedNamig, dynamicModelName);
    newVfModule.missingData = this.checkMissingData(instance, ServiceNodeTypes.VFmodule, dynamicInputs, isEcompGeneratedNamig);
    return newVfModule;
  }

}
