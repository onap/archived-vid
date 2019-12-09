import * as _ from 'lodash'

export class Utils {

  static getMaxFirstLevel(properties, flags: { [key: string]: boolean }) : number | null{
    if (flags && !!flags['FLAG_2002_UNLIMITED_MAX']) {
      return !_.isNil(properties) && !_.isNil(properties.max_instances) ? properties.max_instances : null;
    } else {
      return properties.max_instances || 1;
    }
  }

  public static clampNumber = (number, min, max) => {
    return Math.max(min, Math.min(number, max));
  };

  public static hasContents(object: Object): boolean {
    if (object === undefined || object === null || object === "") {
      return false;
    }
    return true;
  };

  public static convertModel(serviceModel) {

    let isNewFlow:boolean = false;

    for (let networkCustomizationName in serviceModel.networks) {
      let networkModel = serviceModel.networks[networkCustomizationName];
      if ( networkModel.customizationUuid != null ) {
        isNewFlow = true;
        break;
      }
    }
    if ( !isNewFlow ) {
      for (let vnfCustomizationName in serviceModel.vnfs) {
        let vnfModel = serviceModel.vnfs[vnfCustomizationName];
        if ( vnfModel.customizationUuid != null ) {
          isNewFlow = true;
          break;
        }
      }
    }
    if ( isNewFlow ) {
      return (Utils.convertNewModel (serviceModel) );
    }
    else {
      return (Utils.convertOldModel (serviceModel) );
    }
  };

  private static convertNewModel (serviceModel ) {
    let completeResources = new Array();
    let resource = {};
    let convertedAsdcModel = {
      "service": serviceModel.service,
      "networks": {},
      "vnfs": {},
      "pnfs": serviceModel.pnfs,
      "serviceProxies": serviceModel.serviceProxies,
      "completeDisplayInputs": {},
      "isNewFlow": true
    };

    for(let key in serviceModel.service.inputs) {
      if(_.includes(["instance_node_target", "naming_policy", "vf_instance_name"], key)) {
        delete convertedAsdcModel.service.inputs[key];
      }
    }

    for (let networkCustomizationName in serviceModel.networks) {
      let networkModel = serviceModel.networks[networkCustomizationName];

      convertedAsdcModel.networks[networkModel.customizationUuid] = {
        "uuid": networkModel.uuid,
        "invariantUuid": networkModel.invariantUuid,
        "version": networkModel.version,
        "name": networkModel.name,
        "modelCustomizationName": networkModel.modelCustomizationName,
        "customizationUuid": networkModel.customizationUuid,
        "inputs": "",
        "description": networkModel.description,
        "commands": {},
        "displayInputs": {}
      };

      resource = {
        "name": networkModel.modelCustomizationName,
        "description": networkModel.description
      };

      completeResources.push (resource);

    }

    _.forEach(serviceModel.configurations, function(element) {
      element.isConfig = true;
    });
    _.forEach(serviceModel.pnfs, function(element, key) {
      element.isPnf= true;
      element.modelCustomizationName= key;
    });
    let mergedVnfs = Object.assign(serviceModel.vnfs, serviceModel.configurations, serviceModel.pnfs);

    for (let vnfCustomizationName in mergedVnfs) {
      let vnfModel = mergedVnfs[vnfCustomizationName];
      let vnfCustomizationUuid = vnfModel.customizationUuid;
      convertedAsdcModel.vnfs[vnfModel.customizationUuid] = {
        "uuid": vnfModel.uuid,
        "invariantUuid": vnfModel.invariantUuid,
        "version": vnfModel.version,
        "name": vnfModel.name,
        "modelCustomizationName": vnfModel.modelCustomizationName,
        "customizationUuid": vnfModel.customizationUuid,
        "inputs": "",
        "description": vnfModel.description,
        "vfModules": {},
        "volumeGroups": {},
        "commands": {},
        "displayInputs": {},
        "properties": {},
        "nfRole": "",
        "nfType": "",
        "sourceNodes": vnfModel.sourceNodes,
        "collectorNodes": vnfModel.collectorNodes,
        "isConfigurationByPolicy": vnfModel.configurationByPolicy ? vnfModel.configurationByPolicy : false,
        "isConfig": vnfModel.isConfig ? vnfModel.isConfig : false,
        "isPnf": vnfModel.isPnf ? vnfModel.isPnf : false
      };

      resource = {
        "name": vnfModel.modelCustomizationName,
        "description": vnfModel.description
      };
      completeResources.push (resource);

      if (vnfModel.commands != null) {
        /*
                 * commands: {
                 *  internal_net_param_ntu: {
                 * 		command: get_input,
                 * 		displaName: internal_net_param_ntu,
                 * 		inputName: vccfd1_internal_net_param_ntu // pointer to input key
                 * }
                 * If the input name (ptr) is one of instance_node_target,  naming_policy or vf_instance_name
                 * then ignore it
                 *
                 */

        convertedAsdcModel.vnfs[vnfCustomizationUuid].properties=vnfModel.properties;
        //
        let vnf_type = "";
        let vnf_role = "";
        let vnf_function = "";
        let vnf_code = "";
        if ( !( _.isEmpty(vnfModel.properties) ) ) {
          if (this.hasContents (vnfModel.properties.nf_type) ) {
            vnf_type = vnfModel.properties.nf_type;
          }
          if (this.hasContents (vnfModel.properties.nf_role) ) {
            vnf_role = vnfModel.properties.nf_role;
          }
          if (this.hasContents (vnfModel.properties.nf_function) ) {
            vnf_function = vnfModel.properties.nf_function;
          }
          if (this.hasContents (vnfModel.properties.nf_naming_code) ) {
            vnf_code = vnfModel.properties.nf_naming_code;
          }
        }
        convertedAsdcModel.vnfs[vnfCustomizationUuid]["nfType"] = vnf_type;
        convertedAsdcModel.vnfs[vnfCustomizationUuid]["nfRole"] = vnf_role;
        convertedAsdcModel.vnfs[vnfCustomizationUuid]["nfFunction"] = vnf_function;
        convertedAsdcModel.vnfs[vnfCustomizationUuid]["nfCode"] = vnf_code;
        //
        for (let vfModuleCustomizationName in serviceModel.vnfs[vnfCustomizationName].vfModules) {
          let vfModuleModel = serviceModel.vnfs[vnfCustomizationName].vfModules[vfModuleCustomizationName];
          convertedAsdcModel.vnfs[vnfCustomizationUuid].vfModules[vfModuleModel.customizationUuid] = vfModuleModel;
        }

        for (let volumeGroupCustomizationName in serviceModel.vnfs[vnfCustomizationName].volumeGroups) {
          let volumeGroupModel = serviceModel.vnfs[vnfCustomizationName].volumeGroups[volumeGroupCustomizationName];
          convertedAsdcModel.vnfs[vnfCustomizationUuid].volumeGroups[volumeGroupModel.customizationUuid] = volumeGroupModel;
        }
      }
    }

    return (convertedAsdcModel);
  };

  public static isALaCarte(instantiationType) {
    return instantiationType !== 'Macro';
  }

  private static convertOldModel(serviceModel ) {
    let resource = {};
    let convertedAsdcModel = {
      "service": serviceModel.service,
      "networks": {},
      "vnfs": {},
      "pnfs": serviceModel.pnfs,
      "serviceProxies": serviceModel.serviceProxies,
      "completeDisplayInputs": {},
      "isNewFlow": false
    };
    let completeResources = new Array();
    for (let networkCustomizationName in serviceModel.networks) {
      let networkModel = serviceModel.networks[networkCustomizationName];
      convertedAsdcModel.networks[networkModel.invariantUuid] = {};
      convertedAsdcModel.networks[networkModel.uuid] = {
        "uuid": networkModel.uuid,
        "invariantUuid": networkModel.invariantUuid,
        "version": networkModel.version,
        "name": networkModel.name,
        "modelCustomizationName": networkModel.modelCustomizationName,
        "customizationUuid": networkModel.customizationUuid,
        "inputs": "",
        "description": networkModel.description,
        "commands": {},
        "displayInputs": {}
      };
      resource = {
        "name": networkModel.modelCustomizationName,
        "description": networkModel.description
      };
      completeResources.push (resource);
    }

    _.forEach(serviceModel.configurations, function(element) {
      element.isConfig = true;
    });
    _.forEach(serviceModel.pnfs, function(element, key) {
      element.isPnf= true;
      element.modelCustomizationName= key;
    });
    let mergedVnfs = Object.assign(serviceModel.vnfs, serviceModel.configurations, serviceModel.pnfs);

    for (let vnfCustomizationName in mergedVnfs) {
      let vnfModel = mergedVnfs[vnfCustomizationName];
      convertedAsdcModel.vnfs[vnfModel.uuid] = {
        "uuid": vnfModel.uuid,
        "invariantUuid": vnfModel.invariantUuid,
        "version": vnfModel.version,
        "name": vnfModel.name,
        "modelCustomizationName": vnfModel.modelCustomizationName,
        "customizationUuid": vnfModel.customizationUuid,
        "inputs": "",
        "description": vnfModel.description,
        "vfModules": {},
        "volumeGroups": {},
        "commands": {},
        "displayInputs": {},
        "sourceNodes": vnfModel.sourceNodes,
        "collectorNodes": vnfModel.collectorNodes,
        "isConfigurationByPolicy": vnfModel.configurationByPolicy ? vnfModel.configurationByPolicy : false,
        "isConfig": vnfModel.isConfig ? vnfModel.isConfig : false,
        "isPnf": vnfModel.isPnf ? vnfModel.isPnf : false
      };
      resource = {
        "name": vnfModel.modelCustomizationName,
        "description": vnfModel.description
      };
      completeResources.push (resource);

      for (let vfModuleCustomizationName in serviceModel.vnfs[vnfCustomizationName].vfModules) {
        let vfModuleModel = serviceModel.vnfs[vnfCustomizationName].vfModules[vfModuleCustomizationName];
        convertedAsdcModel.vnfs[vnfModel.uuid].vfModules[vfModuleModel.uuid] = vfModuleModel;
      }

      for (let volumeGroupCustomizationName in serviceModel.vnfs[vnfCustomizationName].volumeGroups) {
        let volumeGroupModel = serviceModel.vnfs[vnfCustomizationName].volumeGroups[volumeGroupCustomizationName];
        convertedAsdcModel.vnfs[vnfModel.uuid].volumeGroups[volumeGroupModel.uuid] = volumeGroupModel;
      }
    }

    let completeDisplayInputs = {};

    return (convertedAsdcModel);
  };
}
