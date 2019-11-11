/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

var parameterBlockDirective = function($log, PARAMETER, UtilityService, $compile) {
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

    var IS_SINGLE_OPTION_AUTO_SELECTED = true;

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


    var getParameterHtml = function(parameter, editable) {
        var style = valueStyle;
        var attributeString = "";
        if (parameter.type === PARAMETER.BOOLEAN) {
            style = checkboxValueStyle;
        }
        if (UtilityService.hasContents(parameter.description)) {
            attributeString += " title=' " + parameter.description + " '";
        }
        var rowstyle='';
        if(parameter.type == 'file' && !parameter.isVisiblity){
            rowstyle = ' style="display:none;"';
        }
        var html = "<tr"+rowstyle+"><td style='" + nameStyle + "'" + attributeString + ">"
            + getNameHtml(parameter) + "</td>";
        if (editable === undefined) {
            if (UtilityService.hasContents(parameter.value)) {
                html += "<td data-tests-id='" +  getParameterName(parameter) + "' style='" + style + "'>" + parameter.value;
            } else {
                html += "<td data-tests-id='" +  getParameterName(parameter) + "' style='" + style + "'>";
            }
        } else {
            html += "<td style='" + style + "'>" + getValueHtml(parameter);
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
        var name = getParameterName(parameter);

        var requiredLabel = "";
        if (parameter.isRequired) {
            requiredLabel = "<img src='app/vid/images/asterisk.png' style='"
                + requiredLabelStyle + "'></img>";
        }
        return name + ":" + requiredLabel;
    };

    var getParameterName = function(parameter) {
        var name = "";
        if (UtilityService.hasContents(parameter.name)) {
            name = parameter.name;
        } else {
            name = parameter.id;
        }
        return name;
    }

    var getValueHtml = function(parameter) {

        var textInputPrompt = "Enter data";
        var attributeString = " data-tests-id='" + parameter.id +"' parameter-id='" + parameter.id + "'";
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
        if (UtilityService.hasContents(parameter.isReadOnly) && (parameter.isReadOnly === true)) {
            attributeString += " readonly";
        }
        if ( (UtilityService.hasContents(parameter.maxLength)) && (UtilityService.hasContents(parameter.minLength)) ) {
            attributeString += " pattern='.{" + parameter.minLength + "," + parameter.maxLength + "}' required";
        }
        else if (UtilityService.hasContents(parameter.maxLength)) {
            attributeString += " maxlength='" + parameter.maxLength + "'";
        }
        else if (UtilityService.hasContents(parameter.minLength)) {
            attributeString += " pattern='.{" + parameter.minLength + ",}'"
        }
        if (parameter.isVisible === false) {
            additionalStyle = " visibility: hidden;";
        }

        var name = "";
        if (UtilityService.hasContents(parameter.name)) {
            name = parameter.name;
        } else {
            name = parameter.id;
        }
        attributeString += " parameter-name='" + name + "'";

        if ( parameter.type === PARAMETER.MAP ) {
            textInputPrompt = "{<key1>: <value1>,\.\.\.,<keyN>: <valueN>}";
        }

        if ( parameter.type === PARAMETER.LIST ) {
            textInputPrompt = "[<value1>,\.\.\.,<valueN>]";
        }

        switch (parameter.type) {
            case PARAMETER.BOOLEAN:
                if (parameter.value) {
                    return "<select" + attributeString + " style='" + selectStyle
                        + additionalStyle + "'>" + "<option value=true>true</option>"
                        + "<option value=false>false</option>"
                    + "</select>";
                }else{
                    return "<select" + attributeString + " style='" + selectStyle
                        + additionalStyle + "'>" + "<option value=false>false</option>"
                        + "<option value=true>true</option>"
                        + "</select>";
                }
                break;
            case PARAMETER.CHECKBOX:
                if (parameter.value) {
                    return "<input type='checkbox' "+attributeString+ " checked='checked' style='"+checkboxInputStyle+"'"
                        + " value='true'/>";
                }else{
                    return "<input type='checkbox' "+attributeString+ "' style='"+checkboxInputStyle+"'"
                        + " value='false'/>";
                }
                break;
            case PARAMETER.FILE:
                return "<input type='file' "+attributeString+ " id='"+parameter.id+"' value='"+parameter.value+"'/>";
                break;
            case PARAMETER.NUMBER:
                var value=parameter.value;
                var parameterSpec = "<input type='number'" + attributeString + " style='" + textInputStyle + additionalStyle + "'";

                if ( UtilityService.hasContents(parameter.min) ) {
                    parameterSpec += " min='" + parameter.min + "'";
                }
                if ( UtilityService.hasContents(parameter.max) ) {
                    parameterSpec += " max='" + parameter.max + "'";
                }
                if (UtilityService.hasContents(value)) {
                    parameterSpec += " value='" + value + "'";
                }
                parameterSpec += ">";

                /*if(!isNaN(value) && value.toString().index('.') != -1){
                 //float
                 return "<input type='text'" + attributeString + " style='"
                 + textInputStyle + additionalStyle + "' only-integers" + value
                 + "></input>";
                 } else {
                 //integer
                 return "<input type='text'" + attributeString + " style='"
                 + textInputStyle + additionalStyle + "'  only-float" + value
                 + "></input>";
                 }*/
                return (parameterSpec);
                break;
            case PARAMETER.SELECT:
                if (UtilityService.hasContents(parameter.prompt)) {
                    attributeString += " prompt='" + parameter.prompt + "'";
                }
                return "<select" + attributeString + " style='" + selectStyle
                    + additionalStyle + "'>" + getOptionListHtml(parameter)
                    + "</select>";
                break;
            case PARAMETER.MULTI_SELECT:
                return '<multiselect id="' + parameter.id + '"' + attributeString + ' ng-model="multiselectModel.' + parameter.id + '" options="getOptionsList(\'' + parameter.id + '\')" display-prop="name" id-prop="id"></multiselect>';
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
                var finalString = "<input type='text'" + attributeString + " style='"
                    + textInputStyle + additionalStyle + "'" + value
                    + ">";
                return finalString;
        }
    };


    var getBooleanListHtml = function(parameter){
        var html = "";

    };

    var getOptionListHtml = function(parameter) {

        var html = "";

        if (!angular.isArray(parameter.optionList)
            || parameter.optionList.length === 0) {
            return "";
        }

        if (UtilityService.hasContents(parameter.prompt)) {
                html += "<option value=''>" + parameter.prompt + "</option>";
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
            html += getOptionHtml(option.isPermitted, option.isDataLoading, value, name, parameter);
        }
        return html;
    };

    function getOptionHtml(isPermitted, isDefault, value, name, parameter) {
        var html = "";
        if (isDefault === undefined || isDefault === false )  {
            if(isPermitted)
                html = "<option class='" + parameter.id + "Option' value='" + value + "'>" + name + "</option>";
            else {
                html = "<option class='" + parameter.id + "Option' value='" + value + "' disabled>" + name + "</option>";
            }
        }
        else {
            if(isPermitted)
                html = "<option class='" + parameter.id + "Option' value='" + value + "'>" + "' selected>"  + name + "</option>";
            else {
                html = "<option class='" + parameter.id + "Option' value='" + value + "' disabled>" + "' selected>"  + name + "</option>";
            }
        }
        return html;
    }

    var getParameter = function(element, expectedId) {
        var id = $(element).attr("parameter-id");
        if (!id || (expectedId !== undefined && expectedId !== id)) {
            return undefined;
        }
        var parameter = {
            id : id
        };
        if ($(element).prop("type") === "checkbox") {
            parameter.value = $(element).prop("checked");
        }else if ($(element).prop("type") === "file") {
            parameter.value = $('#'+id).attr("value");

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
        if($(element).is("multiselect")) {
            if(!$(element).find(".active").text().trim()) {
                return '"' + $(element).attr("parameter-name") + '"';
            }
        }
        else {
            if ($(element).prop("type") === "text") {
                $(element).val($(element).val().trim());
            }
            if ($(element).val() === "" || $(element).val() === null) {
                return '"' + $(element).attr("parameter-name") + '"';
            }
        }
        return "";
    };

    var callback = function(element, scope) {
        scope.callback({
            id : $(element).attr("parameter-id")
        });
    };

    return {
        restrict : "EA",
        replace  : true,
        template : "<div><table style='" + tableStyle + "'></table></div>",
        scope : {
            control : "=",
            callback : "&"
        },
        link : function(scope, element, attrs) {

            var control = scope.control || {};
            scope.multiselectModel = {};

            scope.getOptionsList = function (parameterId) {
                return _.find(scope.parameterList, {'id': parameterId})["optionList"];
            };
            control.setList = function(parameterList) {
                scope.parameterList = parameterList;
                scope.multiselectModel = {};
                var html = "";
                for (var i = 0; i < parameterList.length; i++) {
                    html += getParameterHtml(parameterList[i], attrs.editable);
                }
                element.replaceWith($compile(element.html(html))(scope));

                element.find("input, select").unbind("change.namespace1");
                element.find("input, select").bind("change.namespace1", function() {
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
                element.find("input, select").unbind("change.namespace2");
                element.find("input, select").bind("change.namespace2", function() {
                    callback(this, scope);
                });
            };

            control.getList = function(expectedId) {
                var parameterList = new Array();
                element.find("input, select").each(function() {
                    var parameter = getParameter(this, expectedId);
                    if (parameter !== undefined) {
                        parameterList.push(parameter);
                    }
                });
                angular.forEach(scope.multiselectModel, function(value, key) {
                    parameterList.push({id: key, value: value});
                });
                return parameterList;
            };

            control.getRequiredFields = function() {
                var requiredFields = "";
                var count = 0;
                element.find("input, select, multiselect").each(function() {
                    if ($(this).attr("is-required") === "true") {
                        var requiredField = getRequiredField(this);
                        if ((requiredField !== "") && (++count == 1)) {
                                requiredFields = requiredField;
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
            };
        }
    }
};

appDS2.directive('parameterBlock', [ "$log", "PARAMETER", "UtilityService", "$compile",
    parameterBlockDirective ]);


appDS2.directive('onlyIntegers', function () {
    return  {
        restrict: 'A',
        link: function (scope, elm, attrs, ctrl) {
            elm.on('keydown', function (event) {
                if(event.shiftKey){event.preventDefault(); return false;}
                //console.log(event.which);
                if ([8, 13, 27, 37, 38, 39, 40].indexOf(event.which) > -1) {
                    // backspace, enter, escape, arrows
                    return true;
                } else if (event.which >= 49 && event.which <= 57) {
                    // numbers
                    return true;
                } else if (event.which >= 96 && event.which <= 105) {
                    // numpad number
                    return true;
                }
                // else if ([110, 190].indexOf(event.which) > -1) {
                //     // dot and numpad dot
                //     return true;
                // }
                else {
                    event.preventDefault();
                    return false;
                }
            });
        }
    };
});

appDS2.directive('onlyFloat', function () {
    return  {
        restrict: 'A',
        link: function (scope, elm, attrs, ctrl) {
            elm.on('keydown', function (event) {
                if ([110, 190].indexOf(event.which) > -1) {
                    // dot and numpad dot
                    event.preventDefault();
                    return true;
                }
                else{
                    return false;
                }
            });
        }
    };
});
