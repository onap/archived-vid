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

/*
 * Defines "extensions" to standard Angular directives. Provides attributes that
 * can be included in HTML tags.
 * 
 * SYNTAX: ngx-enabled="true | false"
 * 
 * Enables / disables an element. Currently only supports button elements that
 * include the "button" element. This extension was added since the similar
 * standard Angular "ng-disabled" attribute does not handle buttons that use the
 * ECOMP styling.
 * 
 * SYNTAX: ngx-visible="true | false"
 * 
 * Sets an element to visible / hidden. Different from ng-show / ng-hide as
 * follows:
 * 
 * ng-show=false or ng-hide=true - Element is completely hidden.
 * 
 * ngx-visible=false - Element is not displayed. However, a blank area is
 * displayed where the element would display if ngx-visible is set to true.
 */

appDS2.directive('ngxEnabled', function() {
    return {
	restrict : "A",
	link : function(scope, element, attrs) {
	    attrs.$observe("ngxEnabled", function(value) {
		if (attrs.attButton === "") {
		    if (value === "true") {
			element.attr("btn-type", "primary").removeClass(
				"button--inactive").addClass("button--primary")
				.prop('disabled', false);
		    } else {
			element.attr("btn-type", "disabled").removeClass(
				"button--primary").addClass("button--inactive")
				.prop('disabled', true);
		    }
		}
	    });
	}
    }
});

appDS2.directive('ngxVisible', function() {
    return {
	restrict : "A",
	link : function(scope, element, attrs) {
	    attrs.$observe("ngxVisible", function(value) {
		if (value === "true") {
		    element.css("visibility", "visible");
		} else {
		    element.css("visibility", "hidden");
		}
	    });
	}
    }
});
