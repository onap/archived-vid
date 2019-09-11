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

var ComponentService = function($log, COMPONENT, UtilityService) {

    var _this = this;

    var componentList = [ {
		id : COMPONENT.NETWORK,
		displayName : "Network"
    }, {
		id : COMPONENT.SERVICE,
		displayName : "Service Instance"
    }, {
    	id : COMPONENT.OLDVERSION,
		displayName : "Previous Version"
	}, {
		id : COMPONENT.VNF,
		displayName : "Virtual Network Function"
    }, {
		id : COMPONENT.VF_MODULE,
		displayName : "VF Module"
    }, {
        id: COMPONENT.VOLUME_GROUP,
        displayName: "Volume Group"
    }, {
        id : COMPONENT.CONFIGURATION,
        displayName : "Port Mirroring Configuration"
    } ];

    var getInventoryInfo = function(suffix, inventoryItem) {
	var pattern = new RegExp(suffix + "-");
	for ( var key in inventoryItem) {
	    if (pattern.exec(key)) {
		return inventoryItem[key];
	    }
	}
    };

    /*
     * Converts 'id' to a user friendly version.
     * 
     * The algorithm used is:
     * 
     * 1) If "id" found in COMPONENT.FULL_NAME_MAP, return the name found in the
     * map.
     * 
     * 2) Otherwise, if camel case, add "-" between camel case words.
     * 
     * 3) Split id into multiple "partial names" assuming "-" is the delimiter.
     * 
     * 4) Map any partial names found in COMPONENT.PARTIAL_NAME_MAP to the name
     * found in the map.
     * 
     * 5) Use partial names whenever not found in map.
     * 
     * 5) Return name by combining all partial names with " " delimiter.
     */
    var getDisplayName = function(id) {
	var tmp = COMPONENT.FULL_NAME_MAP[id.toLowerCase()];
	if (UtilityService.hasContents(tmp)) {
	    return tmp;
	}
	/*
	 * Add "-" if camel case found.
	 */
	var id = id.replace(/([a-z](?=[A-Z]))/g, '$1-');
	var name = "";
	var arg = id.split("-");
	for (var i = 0; i < arg.length; i++) {
	    if (i > 0) {
		name += " ";
	    }
	    var tmp = COMPONENT.PARTIAL_NAME_MAP[arg[i].toLowerCase()];
	    if (UtilityService.hasContents(tmp)) {
		name += tmp;
	    } else {
		name += arg[i].slice(0, 1).toUpperCase() + arg[i].slice(1);
	    }
	}
	return name;
    };

    return {
	initialize : function(componentId) {
	    for (var i = 0; i < componentList.length; i++) {
		if (componentList[i].id === componentId) {
		    _this.componentId = componentList[i].id;
		    return componentId;
		}
	    }
	    throw "ComponentService:initializeComponent: componentId not found: "
		    + componentId;
	},
	getComponentDisplayName : function() {
	    for (var i = 0; i < componentList.length; i++) {
		if (componentList[i].id === _this.componentId) {
		    return componentList[i].displayName;
		}
	    }
	},
	getInventoryInfo : getInventoryInfo,
	getInventoryParameterList : function(suffix, inventoryItem, isPartial) {
		console.log ("getInventoryParameterList" ); console.log ( JSON.stringify ( inventoryItem, null, 4));
	    var parameterList = new Array();
	    var pattern = new RegExp("network-id|network-name");
	    var pattern1 = new RegExp("neutron");
	
	    if ( (suffix === COMPONENT.NETWORK) && (isPartial) ) {
	    	for ( var id in inventoryItem.object) {
				if (pattern.exec(id) && (!(pattern1.exec(id))) ) {
				    parameterList.push({
					id : id,
					value : inventoryItem.object[id]
				    });
				}
			}
	    }
	    else if (suffix === COMPONENT.NETWORK) {
	    	for ( var id in inventoryItem.object) {
	    		parameterList.push({
	    			id : id,
	    			value : inventoryItem.object[id]
	    		});
			}
	    	for ( var index in inventoryItem.subnets) {
	    		for (var fieldId in inventoryItem.subnets[index]) {
	    			parameterList.push({
	    				id : fieldId,
	    				value : inventoryItem.subnets[index][fieldId]
	    			});
	    		}
			}
	    }
	    else {
		    for ( var id in inventoryItem) {
			//if (pattern.exec(id)) {
			    parameterList.push({
				id : id,
				value : inventoryItem[id]
			    });
			//}
		    }
	    }
	    return parameterList;
	},
	getDisplayNames : function(inputList) {
	    var outputList = new Array();
	    for (var i = 0; i < inputList.length; i++) {
		var entry = angular.copy(inputList[i]);
		if (!UtilityService.hasContents(entry.name)) {
		    entry.name = getDisplayName(entry.id);
		}
		outputList.push(entry);
	    }
	    return outputList;
	},
	getFieldDisplayName : function(name) {
	    return getDisplayName(name);
	}
    };
};

appDS2.factory("ComponentService", [ "$log", "COMPONENT", "UtilityService",
	ComponentService ]);
