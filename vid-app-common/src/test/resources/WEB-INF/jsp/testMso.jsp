<link rel="stylesheet" type="text/css" href="app/vid/test/testMso.css" />
<script src="app/vid/test/testMso.js"></script>

<link rel="stylesheet" type="text/css" href="app/vid/styles/dialogs.css" />

<script src="app/vid/scripts/controller/msoCommitController.js"></script>
<script src="app/vid/scripts/controller/detailsDialogController.js"></script>
<script src="app/vid/scripts/controller/statusDialogController.js"></script>

<script src="app/vid/scripts/services/dataService.js"></script>
<script src="app/vid/scripts/services/detailsService.js"></script>
<script src="app/vid/scripts/services/statusService.js"></script>
<script src="app/vid/scripts/services/componentService.js"></script>
<script src="app/vid/scripts/services/propertyService.js"></script>
<script src="app/vid/scripts/services/msoService.js"></script>
<script src="app/vid/scripts/services/utilityService.js"></script>

<script src="app/vid/scripts/directives/extensionsDirective.js"></script>
<script src="app/vid/scripts/directives/parameterBlockDirective.js"></script>
<script src="app/vid/scripts/directives/popupWindowDirective.js"></script>
<script src="app/vid/scripts/directives/progressBarDirective.js"></script>

<script src="app/vid/scripts/constants/componentConstants.js"></script>
<script src="app/vid/scripts/constants/fieldConstants.js"></script>
<script src="app/vid/scripts/constants/parameterConstants.js"></script>

<!--
	Read configuration properties from server-side properties settings and
	pass to the test controller via the ng-include onload event.

	Consider / investigate replacing this approach with an alternative
	REST / Angular mechanism.
-->
<%@ page import="org.onap.vid.mso.*"%>
<%@ page import="org.onap.portalsdk.core.util.SystemProperties"%>
<%
	String properties = "{msoMaxPolls:" + SystemProperties.getProperty(MsoProperties.MSO_MAX_POLLS)
			+ ",msoMaxPollingIntervalMsec:"
			+ SystemProperties.getProperty(MsoProperties.MSO_POLLING_INTERVAL_MSECS) + "}";
%>

<div ng-controller="testController" ng-init="init(<%=properties%>);"
	ng-cloak>
	<div popup-window class="popupContents" ngx-show="{{popup.isVisible}}"
		ng-cloak>
		<div ng-include="'app/vid/scripts/view-models/msoCommit.htm'"
			onload="autoStartCommitTest();"></div>
		<div ng-include="'app/vid/scripts/view-models/detailsDialog.htm'"
			onload="autoStartQueryTest();"></div>
	</div>

	<h1 class="heading2">
		<center>Various MSO Tests</center>
	</h1>
	<div>
		<input type="checkbox" ng-change="testMsoModeChanged();"
			ng-model="isTestMsoMode"></input> <span>Use test MSO
			controller</span>
	</div>
	<div>
		<h3>These actions are expected to return successfully.</h3>
		<button type="button" ng-click="queryServiceInstance();" att-button
			btn-type="primary" size="small">Query Service Instance</button>
		<button type="button" ng-click="createServiceInstance();" att-button
			btn-type="primary" size="small">Create Service Instance</button>
		<button type="button" ng-click="deleteServiceInstance();" att-button
			btn-type="primary" size="small">Delete Service Instance</button>
		<button type="button" ng-click="createNetworkInstance();" att-button
			btn-type="primary" size="small">Create Network Instance</button>
		<button type="button" ng-click="deleteNetworkInstance();" att-button
			btn-type="primary" size="small">Delete Network Instance</button>
		<button type="button" ng-click="createVNFInstance();" att-button
			btn-type="primary" size="small">Create VNF Instance</button>
		<button type="button" ng-click="deleteVNFInstance();" att-button
			btn-type="primary" size="small">Delete VNF Instance</button>
		<button type="button" ng-click="createVolumeGroupInstance();"
			att-button btn-type="primary" size="small">Create Volume
			Group</button>
		<button type="button" ng-click="deleteVolumeGroupInstance();"
			att-button btn-type="primary" size="small">Delete Volume
			Group</button>
		<button type="button" ng-click="createVFModuleInstance();" att-button
			btn-type="primary" size="small">Create VF Module</button>
		<button type="button" ng-click="deleteVFModuleInstance();" att-button
			btn-type="primary" size="small">Delete VF Module</button>
	</div>
	<hr />
	<h3>These actions are expected to generate errors. These tests assume the above
		"Use test MSO controller" checkbox is checked. All tests are base on the Create Service
		Instance transaction.</h3>
	<ol>
		<li>
			<button type="button"
				ng-click="generateError('ERROR_POLICY_EXCEPTION');" att-button
				btn-type="primary" size="small">Policy Exception</button> <span>Initial
				response contains policy exception</span>
		</li>
		<li>
			<button type="button"
				ng-click="generateError('ERROR_SERVICE_EXCEPTION');" att-button
				btn-type="primary" size="small">Service Exception</button> <span>Initial
				response contains service exception</span>
		</li>
		<li>
			<button type="button" ng-click="generateError('ERROR_POLL_FAILURE');"
				att-button btn-type="primary" size="small">Poll Failure</button> <span>Subsequent
				getOrchestrationRequest poll response contains MSO failure condition</span>
		</li>
		<li>
			<button type="button"
				ng-click="generateError('ERROR_INVALID_FIELD_INITIAL');" att-button
				btn-type="primary" size="small">Initial Invalid</button> <span>Initial
				response contains invalid data field</span>
		</li>
		<li>
			<button type="button"
				ng-click="generateError('ERROR_INVALID_FIELD_POLL');" att-button
				btn-type="primary" size="small">Poll Invalid</button> <span>Subsequent
				getOrchestrationRequest poll response contains invalid data field</span>
		</li>
		<li>
			<button type="button"
				ng-click="generateError('ERROR_GENERAL_SERVER_EXCEPTION');"
				att-button btn-type="primary" size="small">Server Exception</button>
			<span>VID controller code generates general exception</span>
		</li>
		<li>
			<button type="button" ng-click="generateError('ERROR_MAX_POLLS');"
				att-button btn-type="primary" size="small">Maximum Polls</button> <span>Maximum
				poll attempts exceeded</span>
		</li>
		<li>
			<button type="button"
				ng-click="generateError('ERROR_SERVER_TIMEOUT_INITIAL');" att-button
				btn-type="primary" size="small">Initial Timeout</button> <span>Timeout
				on initial response</span>
		</li>
		<li>
			<button type="button"
				ng-click="generateError('ERROR_SERVER_TIMEOUT_POLL');" att-button
				btn-type="primary" size="small">Poll Timeout</button> <span>Timeout
				on subsequent getOrchestrationRequest poll response</span>
		</li>
		<li>
			<button type="button" ng-click="generateInvalidUrl404();" att-button
				btn-type="primary" size="small">Invalid URL (404)</button> <span>GUI
				front-end specifies invalid URL - HTTP 404 response expected</span>
		</li>
		<li>
			<button type="button" ng-click="generateInvalidUrl405();" att-button
				btn-type="primary" size="small">Invalid URL (405)</button> <span>GUI
				front-end specifies invalid URL - HTTP 405 response expected</span>
		</li>
	</ol>

</div>
<!--  Temporary solution for footer overlapping the men after talking to EComp SDK developer on 06/16/2016 -->

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>

