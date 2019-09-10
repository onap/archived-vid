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

appDS2.constant("PARAMETER", (function() {
    return {
		BOOLEAN : "boolean",
		SELECT : "select",
		MULTI_SELECT : "multi_select",
		STRING : "string",
		NUMBER : "number",
		VALID_VALUES : "valid_values",
		EQUAL : "equal",
		LENGTH : "length",
		MAX_LENGTH : "max_length",
		MIN_LENGTH : "min_length",
		IN_RANGE : "in_range",
		CONSTRAINTS : "constraints",
		OPERATOR : "operator",
		CONSTRAINT_VALUES : "constraintValues",
		DEFAULT : "default",
		DESCRIPTION : "description",
		TYPE: "type",
		INTEGER: "integer",
		RANGE: "range",
		LIST: "list",
		MAP: "map",
		REQUIRED: "required",
		GREATER_THAN: "greater_than",
		LESS_THAN: "less_than",
		GREATER_OR_EQUAL: "greater_or_equal",
		LESS_OR_EQUAL: "less_or_equal",
		DISPLAY_NAME: "displayName",
		CHECKBOX:'checkbox',
		FILE:'file'		
    };
})());
