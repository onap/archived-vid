/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

var parameterBlockDirective = function($log, PARAMETER, UtilityService) {
    /*
     * If "IS_SINGLE_OPTION_AUTO_SELECTED" is set to "true" ...
     * 
     * IF these 3 conditions all exist:
     * 
     * 1) The parameter type is PARAMETER.SELECT
     * 
     * 2) AND the "prompt" attribute is set to a string.
     * 
     * 3) AND the optionList" only contains a single entry
     * 
     * THEN the "prompt" will not be displayed as an initial select option.
     */

    var IS_SINGLE_OPTION_AUTO_SELECTED = false;

    /*
     * Optionally remove "nameStyle" and "valueStyle" "width" entries to set
     * dynamic sizing.
     */
    var tableStyle = "width: auto; margin: 0 auto; border-collapse: collapse; border: none;";
    var nameStyle = "width: 220px; text-align: left; vertical-align: middle; font-weight: bold; padding: 3px 5px; border: none;";
    var valueStyle = "width: 400px; text-align: left; vertical-align: middle; padding: 3px 5px; border: none;";
    var checkboxValueStyle = "width: 400px; text-align: center; vertical-align: middle; padding: 3px 5px; border: none;"
    var textInputStyle = "height: 25px; padding: 2px 5px;";
    var checkboxInputStyle = "height: 18px; width: 18px; padding: 2px 5px;";
    var selectStyle = "height: 25px; padding: 2px; text-align: center;";
    var requiredLabelStyle = "width: 25px; padding: 5px 10px 10px 5px;";
    var textInputPrompt = "Enter data";

    var getParameterHtml = function(parameter, editable) {
	var style = valueStyle;
	var attributeString = "";
	if (parameter.type === PARAMETER.BOOLEAN) {
	    style = checkboxValueStyle;
	}
	if (UtilityService.hasContents(parameter.description)) {
	    attributeString += " title=' " + parameter.description + " '";
	}
	var html = "<tr><td style='" + nameStyle + "'" + attributeString + ">"
		+ getNameHtml(parameter) + "</td><td style='" + style + "'>";
	if (editable === undefined) {
	    if (UtilityService.hasContents(parameter.value)) {
		html += parameter.value;
	    }
	} else {
	    html += getValueHtml(parameter);
	}
	html += "</td></tr>";
	return html;
    };

    var updateParameter = function(parameter, element, editable) {
	$(element).parent().parent().children("td").first().html(
		getNameHtml(parameter));
	if (editable === undefined) {
	    $(element).html(parameter.value);
	} else {
	    $(element).parent().html(getValueHtml(parameter));
	}
    };

    var getNameHtml = function(parameter) {
	if (parameter.isVisible === false) {
	    return "";
	}
	var name = "";
	if (UtilityService.hasContents(parameter.name)) {
	    name = parameter.name;
	} else {
	    name = parameter.id;
	}
	var requiredLabel = "";
	if (parameter.isRequired) {
	    requiredLabel = "<img src='app/vid/images/asterisk.png' style='"
		    + requiredLabelStyle + "'></img>";
	}
	return name + ":" + requiredLabel;
    };

    var getValueHtml = function(parameter) {
	var attributeString = " parameter-id='" + parameter.id + "'";
	var additionalStyle = "";
	if (parameter.isEnabled === false) {
	    attributeString += " disabled='disabled'";
	}
	if (parameter.isRequired) {
	    attributeString += " is-required='true'";
	}
	if (UtilityService.hasContents(parameter.description)) {
	    attributeString += " title=' " + parameter.description + " '";
	}
	if (parameter.isVisible === false) {
	    additionalStyle = "visibility: hidden;";
	}
	var name = "";
	if (UtilityService.hasContents(parameter.name)) {
	    name = parameter.name;
	} else {
	    name = parameter.id;
	}
	attributeString += " parameter-name='" + name + "'";

	switch (parameter.type) {
	case PARAMETER.BOOLEAN:
	    if (parameter.value) {
		attributeString += " checked='checked'";
	    }
	    return "<input type='checkbox'" + attributeString + " style='"
		    + checkboxInputStyle + additionalStyle + "'></input>";
	    break;
	case PARAMETER.SELECT:
	    if (UtilityService.hasContents(parameter.prompt)) {
		attributeString += " prompt='" + parameter.prompt + "'";
	    }
	    return "<select" + attributeString + " style='" + selectStyle
		    + additionalStyle + "'>" + getOptionListHtml(parameter)
		    + "</select>";
	    break;
	case PARAMETER.STRING:
	default:
	    var value = "";
	    if (UtilityService.hasContents(parameter.value)) {
		value = " value='" + parameter.value + "'";
	    }
	    if (UtilityService.hasContents(parameter.prompt)) {
		attributeString += " placeholder='" + parameter.prompt + "'";
	    } else if (textInputPrompt !== "") {
		attributeString += " placeholder='" + textInputPrompt + "'";
	    }
	    return "<input type='text'" + attributeString + " style='"
		    + textInputStyle + additionalStyle + "'" + value
		    + "></input>";
	}
    };

    var getOptionListHtml = function(parameter) {

	var html = "";

	if (!angular.isArray(parameter.optionList)
		|| parameter.optionList.length === 0) {
	    return "";
	}

	if (UtilityService.hasContents(parameter.prompt)) {
	    if (!(IS_SINGLE_OPTION_AUTO_SELECTED && parameter.optionList.length === 1)) {
		html += "<option value=''>" + parameter.prompt + "</option>";
	    }
	}

	for (var i = 0; i < parameter.optionList.length; i++) {
	    var option = parameter.optionList[i];
	    var name = option.name;
	    var value = "";
	    if (option.id === undefined) {
		value = option.name;
	    } else {
		if (name === undefined) {
		    name = option.id;
		}
		value = option.id;
	    }
	    html += "<option value='" + value + "'>" + name + "</option>";
	}
	return html;
    };

    var getParameter = function(element, expectedId) {
	var id = $(element).attr("parameter-id");
	if (expectedId !== undefined && expectedId !== id) {
	    return undefined;
	}
	var parameter = {
	    id : id
	};
	if ($(element).prop("type") === "checkbox") {
	    parameter.value = $(element).prop("checked");
	} else {
	    if ($(element).prop("type") === "text") {
		$(element).val($(element).val().trim());
	    }
	    parameter.value = $(element).val();
	}
	if ($(element).prop("selectedIndex") === undefined) {
	    parameter.selectedIndex = -1;
	} else {
	    parameter.selectedIndex = $(element).prop("selectedIndex");
	    if (UtilityService.hasContents($(element).attr("prompt"))) {
		parameter.selectedIndex--;
	    }
	}
	return parameter;
    };

    var getRequiredField = function(element) {
	if ($(element).prop("type") === "text") {
	    $(element).val($(element).val().trim());
	}
	if ($(element).val() === "" || $(element).val() === null) {
	    return '"' + $(element).attr("parameter-name") + '"';
	} else {
	    return "";
	}
    };

    var callback = function(element, scope) {
	scope.callback({
	    id : $(element).attr("parameter-id")
	});
    };

    return {
	restrict : "EA",
	replace : true,
	template : "<div><table style='" + tableStyle + "'></table></div>",
	scope : {
	    control : "=",
	    callback : "&"
	},
	link : function(scope, element, attrs) {

	    var control = scope.control || {};

	    control.setList = function(parameterList) {
		var html = "";
		for (var i = 0; i < parameterList.length; i++) {
		    html += getParameterHtml(parameterList[i], attrs.editable);
		}
		element.html(html);
		element.find("input, select").bind("change", function() {
		    callback(this, scope);
		});
	    }

	    control.updateList = function(parameterList) {
		element.find("input, select").each(
			function() {
			    for (var i = 0; i < parameterList.length; i++) {
				if (parameterList[i].id === $(this).attr(
					"parameter-id")) {
				    updateParameter(parameterList[i], this,
					    attrs.editable);
				}
			    }
			});
	    }

	    control.getList = function(expectedId) {
		var parameterList = new Array();
		element.find("input, select").each(function() {
		    var parameter = getParameter(this, expectedId);
		    if (parameter !== undefined) {
			parameterList.push(parameter);
		    }
		});
		return parameterList;
	    }

	    control.getRequiredFields = function() {
		var requiredFields = "";
		var count = 0;
		element.find("input, select").each(function() {
		    if ($(this).attr("is-required") === "true") {
			var requiredField = getRequiredField(this);
			if (requiredField !== "") {
			    if (++count == 1) {
				requiredFields = requiredField;
			    }
			}
		    }
		});
		if (--count <= 0) {
		    return requiredFields;
		} else if (count == 1) {
		    return requiredFields + " and 1 other field";
		} else {
		    return requiredFields + " and " + count + " other fields";
		}
	    }
	}
    }
}

app.directive('parameterBlock', [ "$log", "PARAMETER", "UtilityService",
	parameterBlockDirective ]);
