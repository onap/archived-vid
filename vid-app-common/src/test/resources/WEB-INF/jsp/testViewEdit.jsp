<link rel="stylesheet" type="text/css"
	href="app/vid/test/testViewEdit.css" />

<script src="app/vid/test/testAaiData.js"></script>
<script src="app/vid/test/testViewEdit.js"></script>

<link rel="stylesheet" type="text/css" href="app/vid/styles/dialogs.css" />

<script src="app/vid/scripts/controller/creationDialogController.js"></script>
<script src="app/vid/scripts/controller/deleteResumeDialogController.js"></script>
<script src="app/vid/scripts/controller/detailsDialogController.js"></script>
<script src="app/vid/scripts/controller/statusDialogController.js"></script>
<script src="app/vid/scripts/controller/msoCommitController.js"></script>

<script src="app/vid/scripts/services/aaiService.js"></script>
<script src="app/vid/scripts/services/asdcService.js"></script>
<script src="app/vid/scripts/services/creationService.js"></script>
<script src="app/vid/scripts/services/dataService.js"></script>
<script src="app/vid/scripts/services/deleteResumeService.js"></script>
<script src="app/vid/scripts/services/detailsService.js"></script>
<script src="app/vid/scripts/services/statusService.js"></script>
<script src="app/vid/scripts/services/componentService.js"></script>
<script src="app/vid/scripts/services/msoService.js"></script>
<script src="app/vid/scripts/services/propertyService.js"></script>
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
<%@ page import="org.openecomp.vid.mso.*"%>
<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties"%>
<%
	String properties = "{msoMaxPolls:" + SystemProperties.getProperty(MsoProperties.MSO_MAX_POLLS)
			+ ",msoMaxPollingIntervalMsec:"
			+ SystemProperties.getProperty(MsoProperties.MSO_POLLING_INTERVAL_MSECS) + "}";
%>

<div ng-controller="testViewEditController"
	ng-init="init(<%=properties%>);" ng-cloak>

	<div popup-window class="popupContents" ngx-show="{{popup.isVisible}}"
		ng-cloak>
		<div ng-include="'app/vid/scripts/view-models/creationDialog.htm'"></div>
		<div ng-include="'app/vid/scripts/view-models/deleteResumeDialog.htm'"></div>
		<div ng-include="'app/vid/scripts/view-models/detailsDialog.htm'"
			onload="autoStartTest();"></div>
	</div>

	<h1 class="heading2">
		<center>Test View Edit Page</center>
	</h1>
	<hr />
	These buttons simulate the add, delete and "show details" (called
	"script" in User Stories) icons (or buttons) that are expected on the
	view / edit page.
	<hr />
	<div>
		<input type="checkbox" ng-change="testMsoModeChanged();"
			ng-model="isTestMsoMode"></input> <span>Use test MSO
			controller</span>
	</div>
	<h3 ng-style="callbackStyle">CALLBACK: {{callbackResults}}</h3>
	<table>
		<tr>
			<td>Service</td>
			<td>
				<button type="button" ng-click="createService();" att-button
					btn-type="primary" size="small">Add</button>
			</td>
			<td>
				<button type="button" ng-click="deleteService();" att-button
					btn-type="primary" size="small">Delete</button>
			</td>
			<td>
				<button type="button" ng-click="showServiceDetails();" att-button
					btn-type="primary" size="small">Show Details</button>
			</td>
		</tr>
		<tr>
			<td>VNF</td>
			<td>
				<button type="button" ng-click="createVnf();" att-button
					btn-type="primary" size="small">Add</button>
			</td>
			<td>
				<button type="button" ng-click="deleteVnf();" att-button
					btn-type="primary" size="small">Delete</button>
			</td>
			<td>
				<button type="button" ng-click="showVnfDetails();" att-button
					btn-type="primary" size="small">Show Details</button>
			</td>
		</tr>
		<tr>
			<td>VF Module</td>
			<td>
				<button type="button" ng-click="createVfModule();" att-button
					btn-type="primary" size="small">Add</button>
			</td>
			<td>
				<button type="button" ng-click="deleteVfModule();" att-button
					btn-type="primary" size="small">Delete</button>
			</td>
			<td>
				<button type="button" ng-click="showVfModuleDetails();" att-button
					btn-type="primary" size="small">Show Details</button>
			</td>
		</tr>
		<tr>
			<td>Volume Group</td>
			<td>
				<button type="button" ng-click="createVolumeGroup();" att-button
					btn-type="primary" size="small">Add</button>
			</td>
			<td>
				<button type="button" ng-click="deleteVolumeGroup();" att-button
					btn-type="primary" size="small">Delete</button>
			</td>
			<td>
				<button type="button" ng-click="showVolumeGroupDetails();"
					att-button btn-type="primary" size="small">Show Details</button>
			</td>
		</tr>
		<tr>
			<td>Network</td>
			<td>
				<button type="button" ng-click="createNetwork();" att-button
					btn-type="primary" size="small">Add</button>
			</td>
			<td>
				<button type="button" ng-click="deleteNetwork();" att-button
					btn-type="primary" size="small">Delete</button>
			</td>
			<td>
				<button type="button" ng-click="showNetworkDetails();" att-button
					btn-type="primary" size="small">Show Details</button>
			</td>
		</tr>
	</table>
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

