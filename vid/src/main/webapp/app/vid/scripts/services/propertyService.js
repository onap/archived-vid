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

var PropertyService = function($location, $http) {

    var RE = /.*?:\/\/.*?:.*?\/(.*?)\//g;
    var BASE_PATH = RE.exec($location.absUrl())[1];
    var DEFAULT_AAI_BASE_URL = "/" + BASE_PATH;
    var DEFAULT_ASDC_BASE_URL = "asdc";
    var DEFAULT_MSO_MAX_POLLING_INTERVAL_MSEC = 60000;
    var DEFAULT_MSO_MAX_POLLS = 10;
    var DEFAULT_MSO_BASE_URL = "/" + BASE_PATH + "/mso";
    var DEFAULT_SERVER_RESPONSE_TIMEOUT_MSEC = 60000;
    var MSO_POLLING_INTERVAL_MSECS = "mso_polling_interval_msecs";
    var MSO_MAX_POLLS = "mso_max_polls";

    var _this = this;

    _this.asdcBaseUrl = DEFAULT_ASDC_BASE_URL;
    _this.aaiBaseUrl = DEFAULT_AAI_BASE_URL;
    _this.msoMaxPollingIntervalMsec = DEFAULT_MSO_MAX_POLLING_INTERVAL_MSEC;
    _this.msoMaxPolls = DEFAULT_MSO_MAX_POLLS;
    _this.msoBaseUrl = DEFAULT_MSO_BASE_URL;
    _this.serverResponseTimeoutMsec = DEFAULT_SERVER_RESPONSE_TIMEOUT_MSEC;

    return {
	getAaiBaseUrl : function() {
	    return _this.aaiBaseUrl;
	},
	setAaiBaseUrl : function(aaiBaseUrl) {
	    _this.aaiBaseUrl = aaiBaseUrl;
	},
	getAsdcBaseUrl : function() {
	    return _this.asdcBaseUrl;
	},
	setAsdcBaseUrl : function(asdcBaseUrl) {
	    _this.asdcBaseUrl = asdcBaseUrl;
	},
	retrieveMsoMaxPollingIntervalMsec : function(defaultvalue) {
		_this.msoMaxPollingIntervalMsec = defaultvalue;
		$http.get( _this.aaiBaseUrl + "/get_property/" + MSO_POLLING_INTERVAL_MSECS + "/" + defaultvalue, {

		},{
			timeout: _this.serverResponseTimeoutMsec
		}).then(function(response) {
			//console.log ( "retrieveMsoMaxPollingIntervalMsec: " ); console.log ( JSON.stringify(response) );
			//console.log ( "retrieveMsoMaxPollingIntervalMsec: " + response.data );
			_this.msoMaxPollingIntervalMsec = response.data;
	        console.log ("retrieveMsoMaxPollingIntervalMsec: " + _this.msoMaxPollingIntervalMsec);
	    },function errorCallback(response) {
	    	console.log ( "retrieveMsoMaxPollingIntervalMsec: " + response.status );
	    	//console.log ( "retrieveMsoMaxPollingIntervalMsec: " ); console.log ( JSON.stringify(response) );
	      });
	    return _this.msoMaxPollingIntervalMsec;
	},
	getMsoMaxPollingIntervalMsec : function() {
	    return _this.msoMaxPollingIntervalMsec;
	},
	setMsoMaxPollingIntervalMsec : function(msoMaxPollingIntervalMsec) {
	    _this.msoMaxPollingIntervalMsec = msoMaxPollingIntervalMsec;
	},
	retrieveMsoMaxPolls : function(defaultvalue) {
		_this.msoMaxPolls = defaultvalue;
		$http.get( _this.aaiBaseUrl + "/get_property/" + MSO_MAX_POLLS + "/" + defaultvalue, {

		},{
			timeout: _this.serverResponseTimeoutMsec
		}).then(function(response) {
			//console.log ( "retrieveMsoMaxPolls: " ); console.log ( JSON.stringify(response) );
			//console.log ( "retrieveMsoMaxPolls: " + response.data.entity );
			_this.msoMaxPolls = response.data;
	        console.log ("retrieveMsoMaxPolls: " + _this.msoMaxPolls);
	    });
	    return _this.msoMaxPolls;
	},
	getMsoMaxPolls : function() {
	    return _this.msoMaxPolls;
	},
	setMsoMaxPolls : function(msoMaxPolls) {
	    _this.msoMaxPolls = msoMaxPolls;
	},
	getMsoBaseUrl : function() {
	    return _this.msoBaseUrl;
	},
	setMsoBaseUrl : function(msoBaseUrl) {
	    _this.msoBaseUrl = msoBaseUrl;
	},
	getServerResponseTimeoutMsec : function() {
	    return _this.serverResponseTimeoutMsec;
	},
	setServerResponseTimeoutMsec : function(serverResponseTimeoutMsec) {
	    _this.serverResponseTimeoutMsec = serverResponseTimeoutMsec;
	}
    };
}

app.factory("PropertyService", PropertyService);
