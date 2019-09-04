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

var popupWindowDirective = function($log, $window) {

    function getZIndex(element) {
	var maxZIndex = 0;
	$(window.document).find("*").each(function() {
	    var zIndex = parseInt($(this).css("z-index"));
	    if (zIndex > maxZIndex) {
		maxZIndex = zIndex;
	    }
	});

	return maxZIndex;
    }

    var scrollPosition = {
	x : 0,
	y : 0
    };

    var link = function(scope, element, attrs, controller, transcludeFn) {

	var zIndex = getZIndex(element.parent()) + 1;

	element.css("z-index", zIndex + 1);

	var backgroundStyle = "display: none; position: fixed; z-index:"
		+ zIndex + ";" + "top: 0; left: 0; width: 100%; height: 100%;"
		+ "background-color: #000000; opacity: 0.5";

	var className = attrs["class"];
	var classString = "";
	if (className !== undefined && className !== null && className !== "") {
	    element.children().children().children().children().addClass(
		    className);
	    element.removeClass(className);
	}

	element.before("<div style='" + backgroundStyle + "'></div>");

	attrs.$observe("ngxShow", function(value) {
	    if (value === "true") {
		scrollPosition = {
		    x : $window.pageXOffset,
		    y : $window.pageYOffset
		};
		$window.scrollTo(0, 0);
		element.css("display", "table");
		element.prev().css("display", "block");
	    } else if (value === "false") {
		element.css("display", "none");
		element.prev().css("display", "none");
		$window.scrollTo(scrollPosition.x, scrollPosition.y);
	    }
	});
    };

    return {
	restrict : "EA",
	transclude : true,
	replace : true,
	link : link,
	template : '<table style="display: none; position: absolute; left: 0; top: 0; width: 100%; height: 100%; border-collapse: collapse; margin: 0; padding: 0"> <tr><td align="center" style="vertical-align: top; padding: 10px"><div style="display: inline-block; padding: 5px; background-color: white" ng-transclude></div></td></tr></table>'
    };
};

appDS2.directive("popupWindow", [ "$log", "$window", popupWindowDirective ]);
