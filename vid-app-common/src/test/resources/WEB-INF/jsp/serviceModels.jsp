<link rel="stylesheet" type="text/css" href="app/vid/styles/aaiGetSubs.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/aaiSubDetails.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/aaiSubViewEdit.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/angular-ui-tree.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/dialogs.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/instantiate.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/vidTree.css" />
<link rel="stylesheet" type="text/css" href="app/vid/styles/dialogs.css" />

	
	<script>
	
	var appDS2 = app;
</script>
	<script src="app/vid/scripts/controller/VidApp.js"></script>
	
<!-- Latest compiled and minified JavaScript -->

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>


<script src="app/vid/scripts/angular-ui-tree.js"></script>

<script src="app/vid/scripts/constants/componentConstants.js"></script>
<script src="app/vid/scripts/constants/fieldConstants.js"></script>
<script src="app/vid/scripts/constants/vidConfiguration.js"></script>
<script src="app/vid/scripts/constants/parameterConstants.js"></script>
<script src="app/vid/scripts/controller/aaiSubscriberController.js"></script>
<script src="app/vid/scripts/controller/creationDialogController.js"></script>
<script src="app/vid/scripts/controller/deletionDialogController.js"></script>
<script src="app/vid/scripts/controller/detailsDialogController.js"></script>
<script src="app/vid/scripts/controller/statusDialogController.js"></script>
<script src="app/vid/scripts/controller/InstantiationController.js"></script>
<script src="app/vid/scripts/controller/msoCommitController.js"></script>
<script src="app/vid/scripts/controller/ServiceModelController.js"></script>
<script src="app/vid/scripts/controller/previousVersionDialogController.js"></script>
<script src="app/vid/scripts/controller/previousVersionContoller.js"></script>
<script src="app/vid/scripts/directives/extensionsDirective.js"></script>
<script src="app/vid/scripts/directives/parameterBlockDirective.js"></script>
<script src="app/vid/scripts/directives/popupWindowDirective.js"></script>
<script src="app/vid/scripts/directives/progressBarDirective.js"></script>
<script src="app/vid/scripts/services/aaiService.js"></script>
<script src="app/vid/scripts/services/asdcService.js"></script>
<script src="app/vid/scripts/services/componentService.js"></script>
<script src="app/vid/scripts/services/creationService.js"></script>
<script src="app/vid/scripts/services/dataService.js"></script>
<script src="app/vid/scripts/services/deletionService.js"></script>
<script src="app/vid/scripts/services/detailsService.js"></script>
<script src="app/vid/scripts/services/statusService.js"></script>
<script src="app/vid/scripts/services/msoService.js"></script>
<script src="app/vid/scripts/services/propertyService.js"></script>
<script src="app/vid/scripts/services/utilityService.js"></script>
<script src="app/vid/scripts/services/vnfService.js"></script>

<%@ page import="org.openecomp.vid.mso.*"%>
<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties"%>
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
