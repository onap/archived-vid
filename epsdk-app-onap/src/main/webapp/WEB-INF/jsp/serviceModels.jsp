<link rel="stylesheet" type="text/css" href="app/vid/external/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/aaiGetSubs.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/aaiSubDetails.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/aaiSubViewEdit.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/angular-ui-tree.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/dialogs.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/instantiate.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/vidTree.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/dialogs.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/angularjs-datetime-picker.css"></link>
<link rel="stylesheet" type="text/css" href="app/vid/styles/common.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/messageViewer.css" />

	
	<script>
	
	var appDS2 = app;
</script>

	<script src="app/vid/scripts/controller/VidApp.js"></script>
	
<!-- Latest compiled and minified JavaScript -->

<script src="app/vid/external/bootstrap/js/bootstrap.min.js"></script>
<script src="app/vid/external/multiselect/angular-bootstrap-multiselect.min.js"></script>
<script src="app/vid/external/upload-file/ng-file-upload.min.js"></script>

<script src="app/vid/scripts/angular-ui-tree.js"></script>

<script src="app/vid/external/lodash/lodash.min.js"></script>
<script src="app/vid/scripts/constants/componentConstants.js"></script>
<script src="app/vid/scripts/constants/fieldConstants.js"></script>
<script src="app/vid/scripts/constants/vidConfiguration.js"></script>
<script src="app/vid/scripts/constants/parameterConstants.js"></script>
<script src="app/vid/scripts/filters/change-managements-by-statuses.filter.js"></script>
<script src="app/vid/scripts/filters/date.filter.js"></script>
<script src="app/vid/scripts/modals/change-management-manual-tasks-controller/change-management-manual-tasks.controller.js"></script>
<script src="app/vid/scripts/modals/new-change-management/new-change-management.controller.js"></script>

<script src="app/vid/scripts/modals/new-test-environment/new-test-environment.controller.js"></script>
<script src="app/vid/scripts/modals/alert-modal/alert-modal.controller.js"></script>
<script src="app/vid/scripts/controller/aaiSubscriberController.js"></script>
<script src="app/vid/scripts/controller/creationDialogController.js"></script>
<script src="app/vid/scripts/controller/deleteResumeDialogController.js"></script>
<script src="app/vid/scripts/controller/detailsDialogController.js"></script>
<script src="app/vid/scripts/controller/statusDialogController.js"></script>
<script src="app/vid/scripts/controller/InstantiationController.js"></script>
<script src="app/vid/scripts/controller/msoCommitController.js"></script>
<script src="app/vid/scripts/controller/pnfSearchAssociationController.js"></script>
<script src="app/vid/scripts/controller/ServiceModelController.js"></script>
<script src="app/vid/scripts/controller/previousVersionDialogController.js"></script>
<script src="app/vid/scripts/controller/previousVersionContoller.js"></script>
<script src="app/vid/scripts/controller/AddNetworkNodeController.js"></script>
<script src="app/vid/scripts/controller/change-management.controller.js"></script>
<script src="app/vid/scripts/controller/testEnvironmentsController.js"></script>
<script src="app/vid/scripts/modals/attach-test-env-manifest/attach-test-env-manifest.controller.js"></script>
<script src="app/vid/scripts/controller/ServiceProxyConfigController.js"></script>

<script src="app/vid/scripts/directives/angularjs-datetime-picker.js"></script>
<script src="app/vid/scripts/controller/msoCommitModalController.js"></script>

<script src="app/vid/scripts/directives/messageViewer.js"></script>
<script src="app/vid/scripts/directives/extensionsDirective.js"></script>
<script src="app/vid/scripts/directives/parameterBlockDirective.js"></script>
<script src="app/vid/scripts/directives/popupWindowDirective.js"></script>
<script src="app/vid/scripts/directives/progressBarDirective.js"></script>
<script src="app/vid/scripts/directives/serviceMetadata.js"></script>
<script src="app/vid/scripts/directives/search.js"></script>
<script src="app/vid/scripts/services/aaiService.js"></script>
<script src="app/vid/scripts/services/schedulerService.js"></script>
<script src="app/vid/scripts/services/asdcService.js"></script>
<script src="app/vid/scripts/services/componentService.js"></script>
<script src="app/vid/scripts/services/creationService.js"></script>
<script src="app/vid/scripts/services/dataService.js"></script>
<script src="app/vid/scripts/services/deleteResumeService.js"></script>
<script src="app/vid/scripts/services/detailsService.js"></script>
<script src="app/vid/scripts/services/statusService.js"></script>
<script src="app/vid/scripts/services/msoService.js"></script>
<script src="app/vid/scripts/services/propertyService.js"></script>
<script src="app/vid/scripts/services/utilityService.js"></script>
<script src="app/vid/scripts/services/vnfService.js"></script>
<script src="app/vid/scripts/services/pnfService.js"></script>
<script src="app/vid/scripts/services/change-management.service.js"></script>
<script src="app/vid/scripts/modals/cancel-pending-workflow/cancel-pending-workflow.controller.js"></script>
<script src="app/vid/scripts/services/OwningEntityService.js"></script>
<script src="app/vid/scripts/services/testEnvironmentsService.js"></script>



<%@ page import="org.onap.vid.mso.*"%>
<%@ page import="org.onap.portalsdk.core.util.SystemProperties"%>
<%
	String properties = "{msoMaxPolls:" + SystemProperties.getProperty(MsoProperties.MSO_MAX_POLLS)
			+ ",msoMaxPollingIntervalMsec:"
			+ SystemProperties.getProperty(MsoProperties.MSO_POLLING_INTERVAL_MSECS) + "}";
%>

<div ng-controller="ServiceModelController"
	ng-init="init(<%=properties%>);" ng-cloak>
	<div ng-view></div>
</div>
<!--  Temporary solution for footer overlapping the men after talking to EComp SDK developer on 06/16/2016 -->
<div class="temporary-solution">
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
</div>
