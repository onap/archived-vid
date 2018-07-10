import {Injectable} from '@angular/core';
import {Constants} from "../shared/utils/constants";
import {Utils} from "../utils/utils";
import * as _ from 'lodash';
import Parameter = Constants.Parameter;
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {ServiceInstance} from "../shared/models/serviceInstance";
import {VNFModel} from "../shared/models/vnfModel";
import {ServiceNodeTypes} from "../shared/models/ServiceNodeTypes";
import {VfModuleMap} from "../shared/models/vfModulesMap";
import {VnfInstance} from "../shared/models/vnfInstance";
import {VfModuleTreeNode} from "../shared/models/vfModuleTreeNode";
import {VfModule} from "../shared/models/vfModule";
import {VnfTreeNode} from "../shared/models/vnfTreeNode";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../store/reducers";
import {InputType} from "../shared/models/inputTypes";


@Injectable()
export class ServicePlanningService {

  modelDataTree: any[] = [];
  drawingDataTree: any[] = [];
  service: any = {name:'My Service'} ;
  public requiredFields = {
    VF: [InputType.LCP_REGION, InputType.TENANT, InputType.PLATFORM],
    VFmodule: []
  };

  constructor(private store: NgRedux<AppState>) {}


  public getServiceName() :string{
    return this.service.name;
  }

  public getServiceInstance(serviceModelId): ServiceInstance {
    return this.store.getState().service.serviceInstance[serviceModelId];
  }

  public getVnfInstance(serviceModelId, vnfModelName): VnfInstance {
    return this.getServiceInstance(serviceModelId).vnfs[vnfModelName];
  }

  public getVfModuleMap(serviceModelId, vnfModelName, vfModuleModelName): VfModuleMap {
    let vnfInstance =  this.getVnfInstance(serviceModelId, vnfModelName);
    return _.get(vnfInstance, ['vfModules', vfModuleModelName]);
  }

  public convertServiceModelToTreeNodes(serviceModel) {
    let _this = this;

    _.forOwn(serviceModel.vnfs, function(item, key) {
      _this.addFirstLevelModel(key, item, item.type, serviceModel);
    });

    _.forOwn(serviceModel.configurations, function(item, key) {
      _this.addFirstLevelModel(key, item, ServiceNodeTypes.Configuration, serviceModel);
    });

    _.forOwn(serviceModel.networks, function(network, key) {
      _this.addFirstLevelModel(key, network, ServiceNodeTypes.Network, serviceModel);
    });

    return this.modelDataTree;
  }

  private addFirstLevelModel(key, value, valueType, serviceModel) {

    let node = this.convertItemToTreeNode(key, value, valueType, null, false);
    let vnfInstance = this.getVnfInstance(serviceModel.service.uuid, key);
    if(value.vfModules) {
      node.children = Object.keys(value.vfModules).map((vmKey) =>
        this.convertItemToTreeNode(vmKey, value.vfModules[vmKey], ServiceNodeTypes.VFmodule, value, !vnfInstance));
    }
    this.modelDataTree.push(node);
  }

  private convertItemToTreeNode(key, value, valueType, parentModel , disabled) {

    return {
      id: value.uuid,
      name: key,
      tooltip: valueType,
      type: valueType,
      count: value.count || 0,
      max: value.max || 1,
      children: [],
      disabled: disabled,
      dynamicInputs: this.updateDynamicInputsVnfDataFromModel(valueType, value),
      userProvidedNaming: this.isUserProvidedNaming(valueType, value, parentModel)
    }
  }

  public convertServiceInstanceToTreeData(serviceInstance: ServiceInstance, modelId: string): any {
    let drawingBoardData = [];
    let _this = this;
    _.forOwn(serviceInstance.vnfs, (vnfInstance, vnfModelName) => {
      let vnfModel: VNFModel = _this.store.getState().service.serviceHierarchy[modelId].vnfs[vnfModelName];
      let vnfNode = new VnfTreeNode(vnfInstance, vnfModel);

      let vfModuleNodes = [];
      _.forOwn(vnfInstance.vfModules, (vfModuleMap, vfModuleModelName) => {
        _.forOwn(vfModuleMap, (vfModuleInstance, vfModuleInstsanceName) => {
          let vfModule: VfModule = _this.store.getState().service.serviceHierarchy[modelId].vnfs[vnfModelName].vfModules[vfModuleModelName];
          let vfModuleNode: VfModuleTreeNode = new VfModuleTreeNode(vfModuleInstance, vfModule, vfModuleModelName);
          vfModuleNodes.push(vfModuleNode);
        });
      });
      vnfNode.children = vfModuleNodes;
      drawingBoardData.push(vnfNode);
    });

    return drawingBoardData;
  }

  public getArbitraryInputs(inputs) {
    let parameter;
    let parameterList = [];
    for (let key in inputs) {
      parameter = {
        id : key,
        type : Parameter.STRING,
        name : key,
        value : inputs[key][Parameter.DEFAULT],
        isRequired : inputs[key][Parameter.REQUIRED],
        description : inputs[key][Parameter.DESCRIPTION]
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
      if ( Utils.hasContents(inputs[key][Parameter.CONSTRAINTS])
        && ( inputs[key][Parameter.CONSTRAINTS].length > 0 ) ) {
        let constraintsArray = inputs[key][Parameter.CONSTRAINTS];
        this.addConstraintParameters (parameterList, constraintsArray, key, inputs, parameter);
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
    let i:number = constraintsArray.length;
    let parameterPushed: boolean = false;
    if ( i > 0 ) {
      while ( (i--) && (!parameterPushed) ) {
        let keys = Object.keys(constraintsArray[i]);
        for ( let operator in keys ) {
          switch (keys[operator]) {
            case Parameter.VALID_VALUES:
              let j: number = constraintsArray[i][Parameter.VALID_VALUES].length;
              if ( j > 0 ) {
                let oList = [];
                let option;
                while (j--) {
                  option = {
                    name: constraintsArray[i][Parameter.VALID_VALUES][j],
                    isDefault: false
                  };
                  if ( (Utils.hasContents (inputs[key][Parameter.DEFAULT]) )
                    && (inputs[key][Parameter.DEFAULT] === constraintsArray[i][Parameter.VALID_VALUES][j] ) ) {
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
              if ( constraintsArray[i][Parameter.EQUAL] != null ) {
                //override parameter type
                parameter.type = Parameter.STRING;
                parameter.isReadOnly = true;
                parameter.value = constraintsArray[i][Parameter.EQUAL];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;

            case Parameter.LENGTH:
              if ( constraintsArray[i][Parameter.LENGTH] != null ) {
                parameter.minLength = constraintsArray[i][Parameter.LENGTH];
                parameter.maxLength = constraintsArray[i][Parameter.LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.MAX_LENGTH:
              if ( constraintsArray[i][Parameter.MAX_LENGTH] != null ) {
                parameter.maxLength = constraintsArray[i][Parameter.MAX_LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.MIN_LENGTH:
              if ( constraintsArray[i][Parameter.MIN_LENGTH] != null ) {
                parameter.minLength = constraintsArray[i][Parameter.MIN_LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.IN_RANGE:
              if ( constraintsArray[i][Parameter.IN_RANGE] != null ) {
                if (constraintsArray[i][Parameter.IN_RANGE].length > 1 ) {
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
              if ( constraintsArray[i][Parameter.GREATER_THAN] != null ) {
                parameter.type = Parameter.NUMBER;
                parameter.min = constraintsArray[i][Parameter.GREATER_THAN];
                parameter.value = inputs[key][Parameter.DEFAULT];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
          }//switch
        }//for
      }//while
    }//if
  };

  public static isVfModule(node:ITreeNode): boolean {
    return node.data.type=='VFmodule';
  }

  public static isVnf(node:ITreeNode): boolean {
    return node.data.type == ServiceNodeTypes.VF;
  }

  updateDynamicInputsVnfDataFromModel(modelType: string, model: any): Array<any> {
    let displayInputs;
    if (modelType === ServiceNodeTypes.VFmodule) {
      displayInputs = model.inputs;
    }
    return _.isEmpty(displayInputs) ? [] : this.getArbitraryInputs(displayInputs);
  }

  isUserProvidedNaming(type: string, nodeModel: any, parentModel: any) : boolean {
    let model;
    if (type === ServiceNodeTypes.VFmodule) {
      model = parentModel;
    }
    else {
      model = nodeModel;
    }
    const ecompGeneratedNaming = model.properties.ecomp_generated_naming;
    return ecompGeneratedNaming !== undefined && ecompGeneratedNaming === "false";
  }
}
