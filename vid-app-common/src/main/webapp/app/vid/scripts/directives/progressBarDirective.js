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
 * "progressBarDirective.js" provides a progress bar directive.
 * 
 * SYNTAX:
 * 
 * <div progress-bar value="percentProgress"></div>
 * 
 * "percentProgress" is the numeric percent progress to be displayed (0 to 100)
 * expressed as a number only (i.e. no trailing "%"). An "scoped" Angular value
 * can be used (e.g. "{{percentProgress}}").
 * 
 * Two additional attributes can also be included:
 * 
 * increases-only="true | false"
 * 
 * Normally, the progress bar always updates the display with whatever value is
 * set. Alternatively, if "increases-only" is set to "true", the display will
 * only be updated if "percentProgress" is >= the previous value.
 * 
 * control="control"
 * 
 * Provides a method ... $scope.control.reset()" ... that a controller can call
 * to reset the progress to it's initial zero state. This would only expected to
 * be needed if A) increases-only is set to true and B) there is a need to reset
 * the controller to 0. If increases-only is set to false or not present, an
 * alternative method of resetting the progress is to just tset percentProgress
 * to 0.
 * 
 * CAVEATS:
 * 
 * 1) The extended attribute "ngx-show" should be used instead of "ng-show" if
 * the control needs to be conditionally visible. Example:
 * ngx-show="{{isProgressVisible}}"
 * 
 * 2) $scope.control.reset() should be conditionally called as follows IF it is
 * called immediately after HTML is rendered. This is due to a timing-related
 * behavior.
 * 
 * 3) The progress bar displays values of "0" and "100" if percentProgress is,
 * respectively, less than 0 or greater than 100.
 * 
 * CUSTOM STYLING:
 * 
 * The ECOMP portal styling uses the class named "progress". The class
 * attributes can be overridden in CSS. This example was tested:
 * 
 * .progress { margin: 0px 10px; height: 40px }
 * 
 * Additional styling can be applied to the progress-bar element. Example:
 * 
 * div[progress-bar=""] { padding-top: 10px; }
 * 
 * if (angular.isFunction($scope.control.reset)) { $scope.control.reset(); }
 * 
 * DEPENDENCIES:
 * 
 * This code assumes dependency files provided by the ECOMP Portal framework are
 * included. For example, ".../app/fusion/external/ebz/sandbox/styles/base.css"
 * is one required dependency. There may also be others.
 */

var progressBarDirective = function() {

    var style = "font-weight: bold;";
    /*
     * The 3 "aria-*" properties were added as per an Internet reference
     * example. These appear to have no impact on current behavior but are
     * retained for future reference.
     */
    var properties = "class='progress-bar' role='progressbar' "
	    + "aria-valuenow='' aria-valuemin='0' aria-valuemax='100'";
    var previousValue = 0;

    var updateProgress = function(element, attrs, valueAsString) {
	if (valueAsString === undefined || valueAsString === null
		|| valueAsString === "") {
	    valueAsString = "0";
	}
	var valueAsInteger = parseInt(valueAsString);
	if (valueAsInteger > 100) {
	    valueAsInteger = 100;
	    valueAsString = "100";
	}
	if (attrs.increasesOnly === "true") {
	    if (valueAsInteger >= previousValue) {
		previousValue = valueAsInteger;
	    } else {
		return;
	    }
	}
	element.css("width", valueAsString + "%");
	if (valueAsInteger >= 100) {
	    element.removeClass("progress-bar-info").addClass(
		    "progress-bar-success");
	} else {
	    element.removeClass("progress-bar-success").addClass(
		    "progress-bar-info");
	}
	if (valueAsInteger >= 5) {
	    element.html(valueAsString + " %");
	} else {
	    /*
	     * Hide text since color combination is barely visible when progress
	     * portion is narrow.
	     */
	    element.html("");
	}
    };

    return {
	restrict : "EA",
	transclude : true,
	replace : true,
	template : "<div ng-transclude " + properties + " style='" + style
		+ "'></div>",
	scope : {
	    control : "=",
	    progressBar : "@"
	},
	link : function(scope, element, attrs) {

	    /*
	     * It should be possible to alternatively add this wrapper in the
	     * template instead of using "element.wrap". Some techniques were
	     * attempted but were unsuccessful.
	     */
	    element.wrap("<div class='progress'></div");

	    var control = scope.control || {};

	    control.reset = function() {
		previousValue = 0;
		updateProgress(element, attrs, 0);
	    };

	    attrs.$observe("value", function(valueString) {
		updateProgress(element, attrs, valueString);
	    });

	    attrs.$observe("ngxShow", function(valueString) {
		if (valueString === "false") {
		    element.parent().hide();
		} else {
		    element.parent().show();
		}
	    });
	}
    };
};

appDS2.directive("progressBar", progressBarDirective);
