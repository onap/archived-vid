/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

"use strict";

/*
 * "CreationService" isolates the "component-specific" logic required by the
 * "CreationDialog" controller.
 *
 * "Components" are defined as the 5 element types managed by the dialogs: A)
 * Service B) VNF C) VF Module D) Volume Group and E) Network.
 * 
 */

var CreationService = function($log, AaiService, AsdcService, DataService,VIDCONFIGURATION,
                               ComponentService, COMPONENT, FIELD, PARAMETER, UtilityService, OwningEntityService,featureFlags) {

    var _this = this;
    var getAsyncOperationList = function() {
        if (DataService.getLoggedInUserId() == null) {
            getLoggedInUserID();
        } else {
            UtilityService.startNextAsyncOperation();
        }
        switch (_this.componentId) {
            case COMPONENT.SERVICE:
                return [ getSubscribers, getServices, getAicZones, getOwningEntityProperties ];
            case COMPONENT.NETWORK:
                return [ getLcpCloudRegionTenantList, getOwningEntityProperties ];
            case COMPONENT.VNF:
                return [ getLcpCloudRegionTenantList, getOwningEntityProperties ];
            case COMPONENT.VF_MODULE:
                return [ getLcpCloudRegionTenantList ];
            case COMPONENT.VOLUME_GROUP:
                return [ getLcpCloudRegionTenantList ];
        }
    };

    /*
     * "getSummaryList" and "getUserProvidedList" return parameters that should
     * be displayed in the summary and user provided sections, respectively. The
     * functions are expected to return lists that are in the format needed by
     * the parameter-block directive.
     */

    var getSummaryList = function() {

        /*
         * These placeholders should be removed and their usage in
         * "getSummaryList" should be replaced by appropriate code as the
         * requirements and interfaces firm up.
         */

        var PLACEHOLDER_RESOURCE_DESCRIPTION = "Resource Description (PLACEHOLDER)";
        var PLACEHOLDER_SERVICE_CATEGORY = "Service Category (PLACEHOLDER)";
        var PLACEHOLDER_VF_MODULE_DESCRIPTION = "VF Module Description (PLACEHOLDER)";
        var PLACEHOLDER_VF_MODULE_LABEL = "VF Module Label (PLACEHOLDER)";
        var PLACEHOLDER_VF_MODULE_TYPE = "VF Module Type (PLACEHOLDER)";

        _this.parameterList = new Array();

        /*
         * Common fields displayed at the top of all create instance screens.
         */
        if(DataService.getModelInfo(_this.componentId)["serviceTypeName"]==null
            || DataService.getModelInfo(_this.componentId)["serviceTypeName"]==undefined
            || DataService.getModelInfo(_this.componentId)["serviceTypeName"]==''){
            addToList(FIELD.NAME.SERVICE_NAME, DataService.getServiceName());
        }

        switch (_this.componentId) {
            case COMPONENT.SERVICE:
                if ( !DataService.getALaCarte() ) {
                    // for macro instantiation need to add the resource names under the node template list
                    // this field is called modelCustomizationName in the asdc client code
                    var p;
                    var rlist = DataService.getResources();
                    var res;
                    if ( rlist != null ) {
                        for (var i = 0; i < rlist.length; i++) {
                            res = rlist[i];

                            p = FIELD.NAME.RESOURCE_NAME.concat(" " + (i+1));
                            addToList(p, res.name );
                            p = FIELD.NAME.RESOURCE_DESCRIPTION.concat(" " + (i+1));
                            addToList(p, res.description );
                        }
                    }
                }
                if(DataService.getModelInfo(_this.componentId)["createSubscriberName"]!=null && DataService.getModelInfo(_this.componentId)["createSubscriberName"]!=''){
                    addToList(FIELD.NAME.SUBSCRIBER_NAME, DataService
                        .getModelInfo(_this.componentId)["createSubscriberName"]);
                }
                if(DataService.getModelInfo(_this.componentId)["serviceTypeName"]!=null && DataService.getModelInfo(_this.componentId)["serviceTypeName"]!=''){
                    addToList(FIELD.NAME.SERVICE_TYPE, DataService
                        .getModelInfo(_this.componentId)["serviceTypeName"]);
                    addToList(FIELD.NAME.SERVICE_NAME, DataService.getServiceName());
                }
                addToList(FIELD.NAME.SERVICE_INVARIANT_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_INVARIANT_ID]);
                addToList(FIELD.NAME.SERVICE_VERSION, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_VERSION]);
                addToList(FIELD.NAME.SERVICE_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_NAME_VERSION_ID]);
                addToList(FIELD.NAME.SERVICE_DESCRIPTION, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.DESCRIPTION]);
                addToList(FIELD.NAME.SERVICE_CATEGORY, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.CATEGORY]);
                if (DataService.getModelInfo(_this.componentId)[FIELD.ID.SERVICE_TYPE] != "null") {
                    addToList(FIELD.NAME.SERVICE_TYPE, DataService
                        .getModelInfo(_this.componentId)[FIELD.ID.SERVICE_TYPE]);
                    addToList(FIELD.NAME.SERVICE_ROLE, DataService
                        .getModelInfo(_this.componentId)[FIELD.ID.SERVICE_ROLE]);
                }

                break;
            case COMPONENT.VF_MODULE:
                addToList(FIELD.NAME.SUBSCRIBER_NAME, DataService
                    .getSubscriberName());
                addToList(FIELD.NAME.SERVICE_INSTANCE_NAME, DataService
                    .getServiceInstanceName());
                addToList(FIELD.NAME.MODEL_NAME, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_NAME]);
                addToList(FIELD.NAME.MODEL_INVARIANT_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_INVARIANT_ID]);
                addToList(FIELD.NAME.MODEL_VERSION, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_VERSION]);
                addToList(FIELD.NAME.MODEL_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_NAME_VERSION_ID]);
                addToList(FIELD.NAME.MODEL_CUSTOMIZATION_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.CUSTOMIZATION_UUID]);
                break;
            case COMPONENT.VNF:
                addToList(FIELD.NAME.SUBSCRIBER_NAME, DataService
                    .getSubscriberName());
                addToList(FIELD.NAME.SERVICE_INSTANCE_NAME, DataService
                    .getServiceInstanceName());
                addToList(FIELD.NAME.MODEL_NAME, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_NAME]);
                addToList(FIELD.NAME.MODEL_INVARIANT_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_INVARIANT_ID]);
                addToList(FIELD.NAME.MODEL_VERSION, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_VERSION]);
                addToList(FIELD.NAME.MODEL_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_NAME_VERSION_ID]);
                addToList(FIELD.NAME.MODEL_CUSTOMIZATION_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.CUSTOMIZATION_UUID]);
                addToList(FIELD.NAME.MODEL_CUSTOMIZATION_NAME, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_CUSTOMIZATION_NAME]);
                addToList(FIELD.NAME.MODEL_VNF_TYPE, DataService
                    .getModelInfo(_this.componentId)[COMPONENT.VNF_TYPE]);
                addToList(FIELD.NAME.MODEL_VNF_ROLE, DataService
                    .getModelInfo(_this.componentId)[COMPONENT.VNF_ROLE]);
                addToList(FIELD.NAME.MODEL_VNF_FUNCTION, DataService
                    .getModelInfo(_this.componentId)[COMPONENT.VNF_FUNCTION]);
                addToList(FIELD.NAME.MODEL_VNF_CODE, DataService
                    .getModelInfo(_this.componentId)[COMPONENT.VNF_CODE]);
                break;
            case COMPONENT.NETWORK:
            case COMPONENT.VOLUME_GROUP:
                addToList(FIELD.NAME.SUBSCRIBER_NAME, DataService
                    .getSubscriberName());
                addToList(FIELD.NAME.SERVICE_INSTANCE_NAME, DataService
                    .getServiceInstanceName());
                addToList(FIELD.NAME.MODEL_NAME, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_NAME]);
                addToList(FIELD.NAME.MODEL_INVARIANT_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_INVARIANT_ID]);
                addToList(FIELD.NAME.MODEL_VERSION, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_VERSION]);
                addToList(FIELD.NAME.MODEL_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.MODEL_NAME_VERSION_ID]);
                addToList(FIELD.NAME.MODEL_CUSTOMIZATION_UUID, DataService
                    .getModelInfo(_this.componentId)[FIELD.ID.CUSTOMIZATION_UUID]);
                break;
        }

        return _this.parameterList;
    };

    var getUserProvidedList = function() {
        var parameterList = [];
        var isUserProvidedNaming = false;
        if ( ((DataService.getModelInfo(_this.componentId).serviceEcompNaming != null)
            && (DataService.getModelInfo(_this.componentId).serviceEcompNaming === "false")) || DataService.getE2EService() ) {
            isUserProvidedNaming = true;
        }

        var isInTop = DataService.getHideServiceFields() || false;
        if (_this.componentId === COMPONENT.SERVICE) {
            if ( DataService.getALaCarte() ) {
                parameterList = [ FIELD.PARAMETER.INSTANCE_NAME ];
                if(!isInTop){
                    parameterList = parameterList.concat([ getSubscribersParameter(),
                        FIELD.PARAMETER.SERVICE_TYPE_DISABLED ]);
                }
            }
            else {
                // macro

                if(!isInTop){
                    if (isUserProvidedNaming) {
                        parameterList = [ FIELD.PARAMETER.INSTANCE_NAME ];

                    }
                    parameterList = parameterList.concat([ getSubscribersParameter() ]);
                    parameterList = parameterList.concat([ getServiceId(),
                        FIELD.PARAMETER.SERVICE_TYPE,
                        FIELD.PARAMETER.LCP_REGION,
                        FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN,
                        FIELD.PARAMETER.TENANT_DISABLED
                    ]);
                    if(!DataService.getE2EService()) {
                        parameterList = parameterList.concat([getAicZonesParameter()]);
                    }

                }else{
                    parameterList = parameterList.concat([ getServiceId(),
                        FIELD.PARAMETER.LCP_REGION,
                        FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN,
                        FIELD.PARAMETER.TENANT_DISABLED ]);
                }
            }

            if(!DataService.getE2EService()) {
                parameterList = parameterList.concat([getProjectParameter()]);
                parameterList = parameterList.concat([getOwningEntityParameter()]);
            }

            //if service model has a pnf, add a PNF ID parameter
            if (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_PNP_INSTANTIATION) && DataService.getPnf()) {
                parameterList = parameterList.concat([ FIELD.PARAMETER.PNF_ID ]);
            }
        }
        else {
            parameterList = [ FIELD.PARAMETER.INSTANCE_NAME ];
            switch (_this.componentId) {
                case COMPONENT.NETWORK:
                case COMPONENT.VNF:
                    parameterList = parameterList.concat([ getServiceId(),
                        getLcpRegionParameter(), FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN,
                        FIELD.PARAMETER.TENANT_DISABLED ]);
                    parameterList = parameterList.concat([ getLineOfBusinessParameter() ]);


                    if(_this.componentId === COMPONENT.VNF){
                        parameterList[parameterList.length -1].isRequired = true;
                    }

                    parameterList = parameterList.concat([ getPlatformParameter() ]);

                    if(_this.componentId === COMPONENT.NETWORK){
                        parameterList[parameterList.length -1].isRequired = false;
                    }

                    break;
                case COMPONENT.VF_MODULE:
                    parameterList = parameterList.concat([
                        getLcpRegionParameter(),
                        FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN,
                        FIELD.PARAMETER.TENANT_DISABLED
                    ]);

                    var availableVolumeGroupList = DataService.getAvailableVolumeGroupList();

                    if (availableVolumeGroupList && availableVolumeGroupList.length > 0) {
                        var availableVolumeGroupNames = [FIELD.STATUS.NONE];

                        for (var i = 0; i < availableVolumeGroupList.length; i++) {
                            availableVolumeGroupNames.push(availableVolumeGroupList[i].instance.name);
                        }

                        parameterList.push(addOptionList(
                            FIELD.PARAMETER.AVAILABLE_VOLUME_GROUP,
                            availableVolumeGroupNames));
                    }
                    break;
                case COMPONENT.VOLUME_GROUP:
                    parameterList = parameterList.concat([ getLcpRegionParameter(),
                        FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN,
                        FIELD.PARAMETER.TENANT_DISABLED ]);
            }
        }
        parameterList.push(FIELD.PARAMETER.SUPPRESS_ROLLBACK);
        if(_this.componentId === COMPONENT.VF_MODULE ){
            parameterList.push({name: FIELD.NAME.SDN_C_PRELOAD,
                    id: FIELD.ID.SDN_C_PRELOAD,
                    type: "checkbox",
                    isEnabled: true,
                    isRequired: false,
                    hideFieldAndLabel: true
                }
            );
            parameterList.push({name: FIELD.NAME.UPLOAD_SUPPLEMENTORY_DATA_FILE,
                    id: FIELD.ID.UPLOAD_SUPPLEMENTORY_DATA_FILE,
                    type: "checkbox",
                    isEnabled: true,
                    isRequired: false,
                    value:false
                }
            );

            parameterList.push({name: FIELD.NAME.SUPPLEMENTORY_DATA_FILE,
                    id: FIELD.ID.SUPPLEMENTORY_DATA_FILE,
                    type: "file",
                    isRequired: false,
                    isVisiblity: false
                }
            );
        }

        if( VIDCONFIGURATION.UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED  && _this.componentId === COMPONENT.VOLUME_GROUP){
            parameterList.push({name: FIELD.NAME.UPLOAD_SUPPLEMENTORY_DATA_FILE,
                    id: FIELD.ID.UPLOAD_SUPPLEMENTORY_DATA_FILE,
                    type: "checkbox",
                    isEnabled: true,
                    isRequired: false
                }
            );

            parameterList.push({name: FIELD.NAME.SUPPLEMENTORY_DATA_FILE,
                    id: FIELD.ID.SUPPLEMENTORY_DATA_FILE,
                    type: "file",
                    isRequired: false,
                    isVisiblity: false
                }
            );
        }

        addArbitraryParameters(parameterList);

        return parameterList;
    };

    var addArbitraryParameters = function(parameterList) {
        if ( DataService.getModelInfo(_this.componentId).displayInputs != null ) {
            var inputs = DataService.getModelInfo(_this.componentId).displayInputs;
            for ( var key in inputs) {
                var parameter = {
                    id : key,
                    type : PARAMETER.STRING,
                    name : ComponentService.getFieldDisplayName(key),
                    value : inputs[key][PARAMETER.DEFAULT],
                    isRequired : inputs[key][PARAMETER.REQUIRED],
                    description : inputs[key][PARAMETER.DESCRIPTION]
                };
                if ( DataService.getALaCarte() ) {
                    parameter.name = ComponentService.getFieldDisplayName(inputs[key][PARAMETER.DISPLAY_NAME]);
                }
                switch (inputs[key][PARAMETER.TYPE]) {
                    case PARAMETER.INTEGER:
                        parameter.type = PARAMETER.NUMBER;
                        break;
                    case PARAMETER.BOOLEAN:
                        parameter.type = PARAMETER.BOOLEAN;
                        break;
                    case PARAMETER.RANGE:
                        break;
                    case PARAMETER.LIST:
                        parameter.type = PARAMETER.LIST;
                        break;
                    case PARAMETER.MAP:
                        parameter.type = PARAMETER.MAP;
                        break;
                }

                if ( UtilityService.hasContents(inputs[key][PARAMETER.CONSTRAINTS])
                    && ( inputs[key][PARAMETER.CONSTRAINTS].length > 0 ) ) {
                    var constraintsArray = inputs[key][PARAMETER.CONSTRAINTS];
                    //console.log ("Calling addConstraintParameters for input name=" + key);
                    addConstraintParameters (parameterList, constraintsArray, key, inputs, parameter);
                }
                else {

                    parameterList.push(parameter);
                }
            }
            DataService.setArbitraryParameters (parameterList);
        }
    };

	var addConstraintParameters = function(parameterList, constraintsArray, key, inputs, parameter) {
		// If there are constraints and the operator is "valid_values",
		// use a select parameter type. 
		var i = constraintsArray.length;
		var parameterPushed = false;
		if ( i > 0 ) {
			while ( (i--) && (!parameterPushed) ) {
				var keys = Object.keys(constraintsArray[i]);
				//var keys_len = keys.length;
				for ( var operator in keys ) {
					//console.log ("keys[operator]=" + keys[operator]);
					switch (keys[operator]) {
					case PARAMETER.VALID_VALUES:
						var j = constraintsArray[i][PARAMETER.VALID_VALUES].length;
						if ( j > 0 ) {
							var oList = [];
							var option;
							while (j--) {
								option = {
										name: constraintsArray[i][PARAMETER.VALID_VALUES][j],
										isDefault: false
								}; 
								if ( ( UtilityService.hasContents (inputs[key][PARAMETER.DEFAULT]) ) 
										&& (inputs[key][PARAMETER.DEFAULT] === constraintsArray[i][PARAMETER.VALID_VALUES][j] ) ) {
									option = {
											name: constraintsArray[i][PARAMETER.VALID_VALUES][j],
											isDefault: true
									} 
								}
								oList.push(option);
							}
							parameter.type = PARAMETER.SELECT;
							parameter.optionList = oList;
							parameterList.push(parameter);
							parameterPushed = true;
							//console.log ("pushed param for valid values");
						}
					break; 
					
					case PARAMETER.EQUAL: 
						if ( constraintsArray[i][PARAMETER.EQUAL] != null ) {
							//override parameter type
							parameter.type = PARAMETER.STRING;
							parameter.isReadOnly = true;
							parameter.value = constraintsArray[i][PARAMETER.EQUAL];
							parameterList.push(parameter);
							parameterPushed = true;
							//console.log ("pushed param for equal");
						}
					break;
						
					case PARAMETER.LENGTH: 
						if ( constraintsArray[i][PARAMETER.LENGTH] != null ) {
							parameter.minLength = constraintsArray[i][PARAMETER.LENGTH];
							parameter.maxLength = constraintsArray[i][PARAMETER.LENGTH];
							parameterList.push(parameter);
							parameterPushed = true;
							//console.log ("pushed param for length: ");
							//console.log (JSON.stringify (parameter, null, 4));
						}
					break;
					case PARAMETER.MAX_LENGTH: 
						if ( constraintsArray[i][PARAMETER.MAX_LENGTH] != null ) {
							parameter.maxLength = constraintsArray[i][PARAMETER.MAX_LENGTH];
							parameterList.push(parameter);
							parameterPushed = true;
							//console.log ("pushed param for max length: ");
							//console.log (JSON.stringify (parameter, null, 4));
						}
					break;
					case PARAMETER.MIN_LENGTH: 
						if ( constraintsArray[i][PARAMETER.MIN_LENGTH] != null ) {
							parameter.minLength = constraintsArray[i][PARAMETER.MIN_LENGTH];
							parameterList.push(parameter);
							parameterPushed = true;
							//console.log ("pushed param for min length: ");
							//console.log (JSON.stringify (parameter, null, 4));
						}
					break;
					case PARAMETER.IN_RANGE:
						if ( constraintsArray[i][PARAMETER.IN_RANGE] != null ) {
							if (constraintsArray[i][PARAMETER.IN_RANGE].length > 1 ) {
								parameter.min = constraintsArray[i][PARAMETER.IN_RANGE][0];
								parameter.max = constraintsArray[i][PARAMETER.IN_RANGE][1];
								parameter.type = PARAMETER.NUMBER;
								parameter.value = inputs[key][PARAMETER.DEFAULT];
								parameterList.push(parameter);
								parameterPushed = true;
								//console.log ("pushed param for in_range");
							}
						}
					break; 
					case PARAMETER.GREATER_THAN:
						if ( constraintsArray[i][PARAMETER.GREATER_THAN] != null ) {
								parameter.type = PARAMETER.NUMBER;
								parameter.min = constraintsArray[i][PARAMETER.GREATER_THAN];
								parameter.value = inputs[key][PARAMETER.DEFAULT];
								parameterList.push(parameter);
								parameterPushed = true;
								//console.log ("pushed param for greater_than");
							
						}
					break;
					}//switch
				}//for
				
			}//while
		}//if
	};
	var addToList = function(name, value) {
		_this.parameterList.push({
			name : name,
			value : value
		});
	};
	var setInventoryInfo = function(){
        var inventoryItem = DataService.getInventoryItem();
        var inventoryInfo = ComponentService.getInventoryInfo(
            _this.componentId, inventoryItem);
	};

    /*
     * The "*Mso*" functions return URL and request details that can be passed
     * to the MSO controller. The request details defines the info passed as
     * part of the POST body.
     */

    var getMsoUrl = function() {
        switch (_this.componentId) {
            case COMPONENT.NETWORK:
                return "mso_create_nw_instance/"
                    + DataService.getServiceInstanceId();
            case COMPONENT.SERVICE:
                if(DataService.getE2EService() === true)
                    return "mso_create_e2e_svc_instance";
                else
                    return "mso_create_svc_instance";
            case COMPONENT.VNF:
                return "mso_create_vnf_instance/"
                    + DataService.getServiceInstanceId();
            case COMPONENT.VF_MODULE:
                return "mso_create_vfmodule_instance/"
                    + DataService.getServiceInstanceId() + "/vnfs/"
                    + DataService.getVnfInstanceId();
            case COMPONENT.VOLUME_GROUP:
                return "mso_create_volumegroup_instance/"
                    + DataService.getServiceInstanceId() + "/vnfs/"
                    + DataService.getVnfInstanceId();
        }
    };

    var getMsoE2ERequest = function(parameterList) {
        var modelInfo = DataService.getModelInfo(_this.componentId);

        //region id
        let cloudConfiguration = buildCloudConfiguration(parameterList);
        var lcpRegion = cloudConfiguration.lcpCloudRegionId;
        var cloudOwner = cloudConfiguration.cloudOwner;

        var params = [];
        var displayInputs = modelInfo.displayInputs;
        var groupBy = _.groupBy(displayInputs, "templateUUID");

        _.forEach(groupBy, function(nodeTemplateInputs, nodeTemplateUUID) {
            var reqParas = {};
            var vfLocations = [];

            nodeTemplateInputs.forEach(function(parameter){
                if(parameter.type === 'vf_location') {
                    var loc = {
                        vnfProfileId: parameter.displayName,
                        locationConstraints : {
                            vimId: cloudOwner + '_' + lcpRegion
                        }
                    };
                    vfLocations.push(loc);
                } else if(parameter.type === 'sdn_controller') {
                    if(parameter.value === undefined || parameter.value === null) {
                        reqParas[parameter.name] = '';
                    } else {
                        reqParas[parameter.name] = parameter.value.value;
                    }
                } else {
                    var name;
                    _.forEach(displayInputs, function(item, key){
                        if(item === parameter) {
                            name = key;
                        }
                    });
                    var value = _.find(parameterList, function(item){
                        return item.id === name;
                    }).value;
                    reqParas[parameter.displayName] = value;
                }
            });

            params.push({
                resourceName: nodeTemplateInputs[0].templateName,
                resourceInvariantUuid: nodeTemplateInputs[0].templateInvariantUUID,
                resourceUuid: nodeTemplateInputs[0].templateUUID,
                resourceCustomizationUuid: nodeTemplateInputs[0].templateCustomizationUUID,
                parameters: {
                    locationConstraints: vfLocations,
                    //TODO resources: [],
                    requestInputs: reqParas
                }
            });
        });

        var requestBody = {
            service: {
                name: getValueFromList(FIELD.ID.INSTANCE_NAME, parameterList),
                description: modelInfo["description"],
                serviceInvariantUuid: modelInfo["modelInvariantId"],
                serviceUuid: modelInfo["modelNameVersionId"],
                globalSubscriberId: DataService.getGlobalCustomerId(),
                serviceType: getValueFromList(FIELD.ID.SERVICE_TYPE, parameterList) || modelInfo["serviceTypeName"],
                parameters: {
                    locationConstraints: [],
                    resources: params,
                    requestInputs: {} //TODO
                }
            }
        };

        return requestBody;
    };

    var getMsoRequestDetails = function(parameterList) {
        console.log("getMsoRequestDetails invoked, parameterList="); console.log(JSON.stringify(parameterList,null,4));

        //VoLTE logic goes here
        if(DataService.getE2EService() === true) {
            return getMsoE2ERequest(parameterList);
        }

        var modelInfo = DataService.getModelInfo(_this.componentId);
        var requestorloggedInId = DataService.getLoggedInUserId();
        var owningEntityId = getValueFromList(FIELD.ID.OWNING_ENTITY, parameterList);
        if (requestorloggedInId ==  null)
            requestorloggedInId = "";
        var isSupRollback = false;
        if (getValueFromList(FIELD.ID.SUPPRESS_ROLLBACK,parameterList) === "true") {
            isSupRollback = true;
        }
        var requestDetails = {
            requestInfo : {
                instanceName : getValueFromList(FIELD.ID.INSTANCE_NAME,
                    parameterList) || DataService.getVfModuleInstanceName(),
                source : FIELD.ID.VID,
                suppressRollback : isSupRollback,
                requestorId: requestorloggedInId
            },
            modelInfo : {
                modelType : _this.componentId,
                modelInvariantId : modelInfo.modelInvariantId,
                modelVersionId : modelInfo.modelNameVersionId,
                modelName : modelInfo.modelName,
                modelVersion : modelInfo.modelVersion,
                modelCustomizationId: modelInfo.customizationUuid,
                modelCustomizationName : modelInfo.modelCustomizationName
            },
            requestParameters : {
                userParams : getArbitraryParameters(parameterList)
            }
        };
        if (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_ADD_MSO_TESTAPI_FIELD)) {
            if ((_this.componentId != COMPONENT.SERVICE) || ( DataService.getALaCarte() )) {
                requestDetails.requestParameters.testApi = DataService.getMsoRequestParametersTestApi();
            }
        }
        if ( (_this.componentId != COMPONENT.SERVICE) || ( !DataService.getALaCarte() ) ) {
            // include cloud region for everything but service create alacarte
            requestDetails.cloudConfiguration = buildCloudConfiguration(parameterList);
        }
        switch (_this.componentId) {

            case COMPONENT.SERVICE:
                requestDetails.subscriberInfo = {
                    globalSubscriberId : DataService.getGlobalCustomerId(),
                    subscriberName : DataService.getSubscriberName()
                };
                var isInTop = DataService.getHideServiceFields() || false;
                if(isInTop){
                    requestDetails.requestParameters.subscriptionServiceType = DataService.getModelInfo(_this.componentId)["serviceTypeName"];
                }else{
                    requestDetails.requestParameters.subscriptionServiceType = getValueFromList(
                        FIELD.ID.SERVICE_TYPE, parameterList);
                }
                requestDetails.requestParameters.aLaCarte = DataService.getALaCarte();
                if ( !DataService.getALaCarte() ) {
                    requestDetails.requestInfo.productFamilyId = getValueFromList(
                        FIELD.ID.PRODUCT_FAMILY, parameterList);
                }
                var svcModelInfo = {
                    modelType : _this.componentId,
                    modelInvariantId : modelInfo.modelInvariantId,
                    modelVersionId : modelInfo.modelNameVersionId,
                    modelName : modelInfo.modelName,
                    modelVersion : modelInfo.modelVersion
                };
                requestDetails.modelInfo = svcModelInfo;

                var selectedProject = getValueFromList(FIELD.ID.PROJECT, parameterList);

                if (selectedProject) {
                    requestDetails.project = {
                        projectName: getValueFromList(FIELD.ID.PROJECT, parameterList)
                    };
                }

                requestDetails.owningEntity = {
                    owningEntityId: owningEntityId,
                    owningEntityName: getOwningEntityNameById(owningEntityId)
                };

                break;
            case COMPONENT.VNF:

                requestDetails.requestInfo.productFamilyId = getValueFromList(
                    FIELD.ID.PRODUCT_FAMILY, parameterList);

                var lineOfBusiness = getValueFromList(FIELD.ID.LINE_OF_BUSINESS, parameterList);

                if(lineOfBusiness) {
                    requestDetails.lineOfBusiness = {
                        lineOfBusinessName: lineOfBusiness
                    };
                }

                requestDetails.platform = {
                    platformName: getValueFromList(FIELD.ID.PLATFORM, parameterList)
                };

                break;
            case COMPONENT.NETWORK:
                requestDetails.requestInfo.productFamilyId = getValueFromList(
                    FIELD.ID.PRODUCT_FAMILY, parameterList);
                var lineOfBusiness = getValueFromList(FIELD.ID.LINE_OF_BUSINESS, parameterList);

                if(lineOfBusiness) {
                    requestDetails.lineOfBusiness = {
                        lineOfBusinessName: lineOfBusiness
                    };
                }

                var platform = getValueFromList(FIELD.ID.PLATFORM, parameterList);
                if(platform !== null && platform !== ""){
                requestDetails.platform = {
                        platformName: platform
                };
                }



                break;
            case COMPONENT.VF_MODULE:
                requestDetails.requestParameters.usePreload = getValueFromList(
                    FIELD.ID.SDN_C_PRELOAD, parameterList);
                if(_this.componentId == COMPONENT.VF_MODULE &&(requestDetails.requestParameters.usePreload== null || requestDetails.requestParameters.usePreload === '')){
                    requestDetails.requestParameters.usePreload = false;
                }
                break;
            case COMPONENT.VOLUME_GROUP:
                break;
        }

        var relatedInstanceList = getRelatedInstanceList(parameterList);

        if (relatedInstanceList !== undefined) {
            requestDetails.relatedInstanceList = relatedInstanceList;
        }

        return requestDetails;
    };

    var buildCloudConfiguration = function (parameterList) {

        var lcpRegionOptionId = getValueFromList(FIELD.ID.LCP_REGION, parameterList);
        var cloudOwnerAndLcpCloudRegion = DataService.getCloudOwnerAndLcpCloudRegionFromOptionId(lcpRegionOptionId);
        var cloudOwner = cloudOwnerAndLcpCloudRegion.cloudOwner;
        var lcpRegion = cloudOwnerAndLcpCloudRegion.cloudRegionId === FIELD.KEY.LCP_REGION_TEXT ?
            getValueFromList(FIELD.ID.LCP_REGION_TEXT,parameterList) :
            cloudOwnerAndLcpCloudRegion.cloudRegionId;

        return {
            lcpCloudRegionId: lcpRegion,
            cloudOwner: featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST) ? cloudOwner : undefined,
            tenantId: getValueFromList(FIELD.ID.TENANT, parameterList)
        };
    };


    var getRelatedInstanceList = function(parameterList) {
        var relatedInstanceList = new Array();
        switch (_this.componentId) {
            case COMPONENT.SERVICE:
                return undefined;
            case COMPONENT.NETWORK:
            case COMPONENT.VNF:
                addRelatedInstance(relatedInstanceList, COMPONENT.SERVICE,
                    DataService.getServiceInstanceId());
                break;
            case COMPONENT.VF_MODULE:
                addRelatedInstance(relatedInstanceList, COMPONENT.SERVICE,
                    DataService.getServiceInstanceId());
                addRelatedInstance(relatedInstanceList, COMPONENT.VNF, DataService
                    .getVnfInstanceId());

                var availableVolumeGroup = getValueFromList(
                    FIELD.ID.AVAILABLE_VOLUME_GROUP, parameterList);

                if (UtilityService.hasContents(availableVolumeGroup) && availableVolumeGroup !== "None") {
                    var availableVolumeGroups = DataService.getAvailableVolumeGroupList();

                    for (var i = 0; i < availableVolumeGroups.length; i++) {
                        if (availableVolumeGroups[i].instance.name == availableVolumeGroup) {
                            DataService.setModelInfo(COMPONENT.VOLUME_GROUP, DataService.getModelInfo(COMPONENT.VF_MODULE));
                            DataService.setVolumeGroupInstanceId(availableVolumeGroups[i].instance.object["volume-group-id"]);
                            break;
                        }
                    }

                    addRelatedInstance(relatedInstanceList, COMPONENT.VOLUME_GROUP,
                        DataService.getVolumeGroupInstanceId());
                }
                break;
            case COMPONENT.VOLUME_GROUP:
                addRelatedInstance(relatedInstanceList, COMPONENT.SERVICE,
                    DataService.getServiceInstanceId());
                addRelatedInstance(relatedInstanceList, COMPONENT.VNF, DataService
                    .getVnfInstanceId());
                break;
        }

        return relatedInstanceList;
    };

    var addRelatedInstance = function(relatedInstanceList, componentId,
                                      instanceId) {
        var modelInfo = DataService.getModelInfo(componentId);
        var relatedInstance;
        if (modelInfo !== undefined) {
            if (componentId === COMPONENT.SERVICE) {
                relatedInstance = {
                    "instanceId" : instanceId,
                    "modelInfo" : {
                        "modelType" : componentId,
                        "modelName" : modelInfo.modelName,
                        "modelInvariantId" : modelInfo.modelInvariantId,
                        "modelVersion" : modelInfo.modelVersion,
                        "modelVersionId" : modelInfo.modelNameVersionId,

                    }
                };
            }
            else {
                relatedInstance = {
                    "instanceId" : instanceId,
                    "modelInfo" : {
                        "modelType" : componentId,
                        "modelName" : modelInfo.modelName,
                        "modelInvariantId" : modelInfo.modelInvariantId,
                        "modelVersion" : modelInfo.modelVersion,
                        "modelVersionId" : modelInfo.modelNameVersionId,
                        "modelCustomizationId": modelInfo.customizationUuid,
                        "modelCustomizationName": modelInfo.modelCustomizationName
                    }
                };
            }
            relatedInstanceList.push({
                relatedInstance : relatedInstance
            });
        }
    };

    /*
     * var getArbitraryParameters = function(parameterList) { var
     * arbitraryParameters = new Object(); for (var i = 0; i <
     * parameterList.length; i++) { var parameter = parameterList[i]; switch
     * (parameter.id) { case FIELD.ID.INSTANCE_NAME: case
     * FIELD.ID.PRODUCT_FAMILY: case FIELD.ID.LCP_REGION: case
     * FIELD.ID.LCP_REGION_TEXT: case FIELD.ID.SERVICE_TYPE: case
     * FIELD.ID.TENANT: case FIELD.ID.SUPPRESS_ROLLBACK: break; default:
     * arbitraryParameters[parameter.id] = parameter.value; } } return
     * arbitraryParameters; }
     */
    var getArbitraryParameters = function(parameterList) {
        var arbitraryParameters = new Object();
        var arbitraryArray = new Array();
        for (var i = 0; i < parameterList.length; i++) {
            var parameter = parameterList[i];
            switch (parameter.id) {
                case FIELD.ID.AVAILABLE_VOLUME_GROUP:
                case FIELD.ID.INSTANCE_NAME:
                case FIELD.ID.PRODUCT_FAMILY:
                case FIELD.ID.LCP_REGION:
                case FIELD.ID.LCP_REGION_TEXT:
                case FIELD.ID.SERVICE_TYPE:
                case FIELD.ID.TENANT:
                case FIELD.ID.SUPPRESS_ROLLBACK:
                case FIELD.ID.SUBSCRIBER_NAME:
                case FIELD.ID.SDN_C_PRELOAD:
                case FIELD.ID.UPLOAD_SUPPLEMENTORY_DATA_FILE:
                case FIELD.ID.OWNING_ENTITY:
                case FIELD.ID.PLATFORM:
                case FIELD.ID.LINE_OF_BUSINESS:
                case FIELD.ID.PROJECT:
                    break;
                case FIELD.ID.SUPPLEMENTORY_DATA_FILE:
                    arbitraryParameters =  FIELD.PARAMETER.SUPPLEMENTORY_DATA_FILE['value'];
                    arbitraryArray=arbitraryParameters;
                    FIELD.PARAMETER.SUPPLEMENTORY_DATA_FILE['value']=[];
                    break;

                default:
                    if (parameter.value != '') {
                        arbitraryParameters = {
                            name: parameter.id,
                            value: parameter.value
                        };
                        arbitraryArray.push(arbitraryParameters);
                    }
            }
        }
        return (arbitraryArray);
    };

    var getModel = function() {
        AsdcService.getModel(DataService.getModelId(), function(response) {
            DataService.setModelInfo(_this.componentId, {
                modelInvariantId : response.data.invariantUUID,
                modelNameVersionId : response.data.uuid,
                modelName : response.data.name,
                modelVersion : response.data.version,
                inputs : response.data.inputs
            });
            UtilityService.startNextAsyncOperation();
        });
    };

    var getSubscriptionServiceTypeList = function() {
        AaiService.getSubscriptionServiceTypeList(DataService
            .getGlobalCustomerId(), function(response) {
            DataService.setSubscriptionServiceTypeList(response);
            UtilityService.startNextAsyncOperation();
        });
    };

    var getLoggedInUserID = function() {
        AaiService.getLoggedInUserID(function(response) {
            DataService.setLoggedInUserId(response.data);
            UtilityService.startNextAsyncOperation();
        });
    };

    var getSubscribers = function() {
        AaiService.getSubscribers(function(response) {
            DataService.setSubscribers(response);
            UtilityService.startNextAsyncOperation();
        });
    };
    var getServices = function() {
        AaiService.getServices(function(response) {
            var serviceIdList = [];
            angular.forEach(response.data, function(value, key) {
                angular.forEach(value, function(subVal, key) {
                    var newVal = {
                        "id" : subVal[FIELD.ID.SERVICE_ID],
                        "description" : subVal[FIELD.ID.SERVICE_DESCRIPTION],
                        "isPermitted" : subVal[FIELD.ID.IS_PERMITTED],
                    };
                    serviceIdList.push(newVal);
                    DataService.setServiceIdList(serviceIdList);
                });
            });

            UtilityService.startNextAsyncOperation();
        });
    };
    var getAicZones = function() {
        AaiService.getAicZones(function(response) {
            var serviceIdList = [];
            angular.forEach(response.data, function(value, key) {
                angular.forEach(value, function(subVal, key) {
                    var newVal = {
                        "id" : subVal[FIELD.ID.ZONE_ID],
                        "name" : subVal[FIELD.ID.ZONE_NAME],
                    };
                    serviceIdList.push(newVal);
                    DataService.setAicZones(serviceIdList);
                });
            });

            UtilityService.startNextAsyncOperation();
        });
    };

    var getOwningEntityProperties = function() {
        OwningEntityService.getOwningEntityProperties(function(owningEntityProperties) {
            DataService.setOwningEntityProperties(owningEntityProperties);
            UtilityService.startNextAsyncOperation();
        });

    };

    var getLcpCloudRegionTenantList = function() {
        AaiService.getLcpCloudRegionTenantList(DataService
            .getGlobalCustomerId(), DataService.getServiceType(), function(
            response) {
            DataService.setCloudRegionTenantList(response);
            UtilityService.startNextAsyncOperation();
        });
    };

    var internalGetParametersHandler = function() {
        if (angular.isFunction(_this.getParametersHandler)) {
            _this.getParametersHandler({
                summaryList : getSummaryList(),
                userProvidedList : getUserProvidedList()
            });
        }
    };

    var getSubscribersParameter = function() {
        var subscribers = DataService.getSubscribers();
        var parameter = FIELD.PARAMETER.SUBSCRIBER_NAME;
        if ( UtilityService.hasContents(subscribers)) {
            parameter.optionList = [];

            for (var i = 0; i < subscribers.length; i++) {
                parameter.optionList.push({
                    id : subscribers[i][FIELD.ID.GLOBAL_CUSTOMER_ID],
                    name : subscribers[i][FIELD.ID.SUBNAME],
                    isPermitted : subscribers[i][FIELD.ID.IS_PERMITTED]
                })
            }
        }
        return parameter;
    };

    var getServiceId = function() {
        var serviceIdList = DataService.getServiceIdList();
        //var serviceTypeList = DataService.getSubscriptionServiceTypeList();
        var parameter = FIELD.PARAMETER.PRODUCT_FAMILY;
        parameter.optionList = new Array();
        if ( UtilityService.hasContents(serviceIdList) ) {
            // load them all
            for (var i = 0; i < serviceIdList.length; i++) {
                parameter.optionList.push({
                    id : serviceIdList[i].id,
                    name : serviceIdList[i].description,
                    isPermitted : serviceIdList[i].isPermitted
                });
            }
        }

        return parameter;
    };

    var getAicZonesParameter = function() {
        var aicList = DataService.getAicZones();
        var parameter = FIELD.PARAMETER.AIC_ZONES;
        parameter.optionList = new Array();
        if ( UtilityService.hasContents(aicList) ) {
            // load them all
            for (var i = 0; i < aicList.length; i++) {
                parameter.optionList.push({
                    id : aicList[i].id,
                    name : aicList[i].name,
                    isPermitted : true

                });
            }
        }

        return parameter;
    };

    var getProjectParameter = function() {
        return getOwningEntityParameterWithOptions(FIELD.PARAMETER.PROJECT);
    };

    var getOwningEntityParameter = function() {
        return getOwningEntityParameterWithOptions(FIELD.PARAMETER.OWNING_ENTITY);
    };

    var getLineOfBusinessParameter = function() {
        return getOwningEntityParameterWithOptions(FIELD.PARAMETER.LINE_OF_BUSINESS);
    };

    var getPlatformParameter = function() {
        return getOwningEntityParameterWithOptions(FIELD.PARAMETER.PLATFORM);
    };

    var getOwningEntityNameById = function (id) {
        var properties = DataService.getOwningEntityProperties();
        var parameter = _.find(properties[FIELD.ID.OWNING_ENTITY], {"id": id});
        return parameter && parameter.name;
    };

    var getOwningEntityParameterWithOptions = function(parameter) {
        var properties = DataService.getOwningEntityProperties();
        if (properties && properties[parameter.id]) {
            parameter.optionList = _.map(properties[parameter.id], function(parameter) {
                return {
                    "id" : parameter.id,
                    "name" : parameter.name,
                    "isPermitted": true
                };
            });
        }

        return parameter;
    };

    var getLcpRegionParameter = function() {
        var cloudRegionTenantList = DataService.getCloudRegionTenantList();
        console.log ( "cloudRegionTenantList=");
        console.log ( JSON.stringify (cloudRegionTenantList, null, 4 ));

        var parameter = FIELD.PARAMETER.LCP_REGION;
        if ( UtilityService.hasContents (cloudRegionTenantList) ) {
            parameter.optionList = new Array();
            for (var i = 0; i < cloudRegionTenantList.length; i++) {
                for (var j = 0; j < parameter.optionList.length; j++) {
                    if (parameter.optionList[j].id === cloudRegionTenantList[i].cloudRegionOptionId) {
                        parameter.optionList[j].isPermitted =
                            parameter.optionList[j].isPermitted || cloudRegionTenantList[i].isPermitted;
                        break;
                    }
                }
                if (j < parameter.optionList.length) {
                    continue;
                }

                var optionName = featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST) && cloudRegionTenantList[i].cloudOwner ?
                    cloudRegionTenantList[i].cloudRegionId + " (" + AaiService.removeVendorFromCloudOwner(cloudRegionTenantList[i].cloudOwner).toUpperCase() + ")" :
                    cloudRegionTenantList[i].cloudRegionId;

                parameter.optionList.push({
                    id : cloudRegionTenantList[i].cloudRegionOptionId,
                    name: optionName,
                    isPermitted : cloudRegionTenantList[i].isPermitted
                });
            }
        }
        return parameter;
    };

    var getTenantList = function(cloudRegionOptionId) {
        var cloudRegionTenantList = DataService.getCloudRegionTenantList();
        var parameter = "";
        if ( UtilityService.hasContents (cloudRegionTenantList) ) {
            parameter = FIELD.PARAMETER.TENANT_ENABLED;
            parameter.optionList = new Array();
            for (var i = 0; i < cloudRegionTenantList.length; i++) {
                if (cloudRegionTenantList[i].cloudRegionOptionId === cloudRegionOptionId) {
                    parameter.optionList.push({
                        id : cloudRegionTenantList[i].tenantId,
                        name : cloudRegionTenantList[i].tenantName,
                        isPermitted : cloudRegionTenantList[i].isPermitted

                    });
                }
            }
        }
        return parameter;

    };

    var addOptionList = function(parameter, optionSimpleArray) {
        var optionList = new Array();
        if (!angular.isArray(optionSimpleArray)) {
            return optionList;
        }
        for (var i = 0; i < optionSimpleArray.length; i++) {
            optionList.push({
                name : optionSimpleArray[i],
                isPermitted :true,
            });
        }
        parameter.optionList = optionList;
        return parameter;
    };

    var getValueFromList = function(id, parameterList) {
        for (var i = 0; i < parameterList.length; i++) {
            if (parameterList[i].id === id) {
                return parameterList[i].value;
            }
        }
        return null;
    };
    var updateUserParameterList = function(updatedId, parameterListControl) {
        console.log ("updateUserParameterList() updatedId=" + updatedId);
        if (updatedId === FIELD.ID.PRODUCT_FAMILY && DataService.getHideServiceFields()) {
            var cloudRegionTenantList = new Array();
            AaiService.getLcpCloudRegionTenantList(DataService.getGlobalCustomerId(), DataService.getServiceType(), function(cloudRegionTenantList) {
                DataService.setCloudRegionTenantList(cloudRegionTenantList);
                parameterListControl.updateList([ getLcpRegionParameter() ]);
            });
        }else if (updatedId === FIELD.ID.SDN_C_PRELOAD) {
            var list = parameterListControl.getList(updatedId);
            if($('input[parameter-id="'+updatedId+'"]').is(':checked')){
                FIELD.PARAMETER.SDN_C_PRELOAD_CHECKED.value=true;
                parameterListControl
                    .updateList([ FIELD.PARAMETER.SDN_C_PRELOAD_CHECKED ]);
            }else{
                parameterListControl
                    .updateList([ FIELD.PARAMETER.SDN_C_PRELOAD_UNCHECKED ]);
            }
        }else if (updatedId === FIELD.ID.UPLOAD_SUPPLEMENTORY_DATA_FILE) {
            if($('input[parameter-id="'+updatedId+'"]').is(':checked')){
                $('input[parameter-id="'+FIELD.ID.SUPPLEMENTORY_DATA_FILE+'"]').closest('tr').show();
                FIELD.PARAMETER.UPLOAD_SUPPLEMENTORY_DATA_FILE_CHECKED.value=true;
                parameterListControl
                    .updateList([ FIELD.PARAMETER.UPLOAD_SUPPLEMENTORY_DATA_FILE_CHECKED ]);
            }else{
                $('input[parameter-id="'+FIELD.ID.SUPPLEMENTORY_DATA_FILE+'"]').closest('tr').hide();
                FIELD.PARAMETER.UPLOAD_SUPPLEMENTORY_DATA_FILE_CHECKED.value=false;
                parameterListControl
                    .updateList([ FIELD.PARAMETER.UPLOAD_SUPPLEMENTORY_DATA_FILE_UNCHECKED ]);
            }
        } else if (updatedId === FIELD.ID.SUPPLEMENTORY_DATA_FILE) {
            var filePath =  $('input[parameter-id="'+updatedId+'"]').val();
            var arr =filePath.split('.');
            var fileExt  = arr[arr.length-1];
            if(fileExt!='' && fileExt.toLowerCase()!='json'){
                $('input[parameter-id="'+updatedId+'"]').val('');
                alert("Invalid file format. Please select *.json format file.");
                return false;
            }
        } else  if (updatedId === FIELD.ID.LCP_REGION) {
            var list = parameterListControl.getList(updatedId);
            if (list[0].selectedIndex >= 0) {
                parameterListControl
                    .updateList([ getTenantList(list[0].value) ]);
            } else {
                parameterListControl
                    .updateList([ FIELD.PARAMETER.TENANT_DISABLED ]);
            }

            var cloudOwnerAndLcpCloudRegion = DataService.getCloudOwnerAndLcpCloudRegionFromOptionId(list[0].value);
            if (cloudOwnerAndLcpCloudRegion.cloudRegionId === FIELD.KEY.LCP_REGION_TEXT) {
                parameterListControl
                    .updateList([ FIELD.PARAMETER.LCP_REGION_TEXT_VISIBLE ]);
            } else {
                parameterListControl
                    .updateList([ FIELD.PARAMETER.LCP_REGION_TEXT_HIDDEN ]);
            }
        } else if (updatedId === FIELD.ID.SUBSCRIBER_NAME) {
            var list = parameterListControl.getList(updatedId);
            if (list[0].selectedIndex >= 0) {
                DataService.setGlobalCustomerId(list[0].value);

                AaiService.getSubscriptionServiceTypeList(DataService
                    .getGlobalCustomerId(), function(response) {
                    DataService.setSubscriptionServiceTypeList(response);
                    var serviceTypeParameters = FIELD.PARAMETER.SERVICE_TYPE;
                    serviceTypeParameters.optionList = [];

                    for (var i = 0; i < response.length; i++) {
                        serviceTypeParameters.optionList.push({
                            "id" : response[i].name,
                            "name" : response[i].name,
                            "isPermitted" :response[i].isPermitted

                        });
                    }
                    console.log ( "updateUserParameterList: service type parameters " );
                    console.log ( JSON.stringify (serviceTypeParameters, null, 4));
                    parameterListControl.updateList([ serviceTypeParameters ]);
                });

            }
        } else if ( updatedId === FIELD.ID.SERVICE_TYPE ) {
            var list = parameterListControl.getList(updatedId);
            if (list[0].selectedIndex >= 0) {

                DataService.setServiceType(list[0].value);
                var cloudRegionTenantList = new Array();
                AaiService.getLcpCloudRegionTenantList(DataService.getGlobalCustomerId(), DataService.getServiceType(), function(cloudRegionTenantList) {
                    DataService.setCloudRegionTenantList(cloudRegionTenantList);
                    parameterListControl.updateList([ getLcpRegionParameter() ]);
                });
            } else {
                parameterListControl
                    .updateList([ FIELD.PARAMETER.SERVICE_TYPE_DISABLED ]);
            }
        }

    };

    return {
        initializeComponent : function(componentId) {
            _this.componentId = ComponentService.initialize(componentId);
        },
        setHttpErrorHandler : function(httpErrorHandler) {
            _this.httpErrorHandler = httpErrorHandler;
        },
        getComponentDisplayName : ComponentService.getComponentDisplayName,
        getParameters : function(getParametersHandler) {
            _this.getParametersHandler = getParametersHandler;
            UtilityService.setHttpErrorHandler(_this.httpErrorHandler);
            UtilityService.startAsyncOperations(getAsyncOperationList(),
                internalGetParametersHandler);
        },
        updateUserParameterList : updateUserParameterList,
        getMsoRequestDetails : getMsoRequestDetails,
        getMsoUrl : getMsoUrl,
        setInventoryInfo: setInventoryInfo
    };
};

appDS2.factory("CreationService", [ "$log", "AaiService", "AsdcService",
    "DataService","VIDCONFIGURATION", "ComponentService", "COMPONENT", "FIELD", "PARAMETER",
    "UtilityService", "OwningEntityService","featureFlags", CreationService ]);
