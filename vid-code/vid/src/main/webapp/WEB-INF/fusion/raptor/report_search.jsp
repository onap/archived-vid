<%--
  ================================================================================
  eCOMP Portal SDK
  ================================================================================
  Copyright (C) 2017 AT&T Intellectual Property
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ================================================================================
  --%>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-touch.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-animate.js"></script>
<script src="http://ui-grid.info/release/ui-grid.js"></script>
<link rel="stylesheet" href="http://ui-grid.info/release/ui-grid.css" type="text/css">

<style></style>

<div ng-controller="reportSearchController">
		<div id="grid1" ui-grid="gridOptions" ui-grid-pagination ui-grid-selection ui-grid-exporter class="grid"></div>
</div>
	
<script>
angular.module('abs').requires.push('ngAnimate', 'ngTouch', 'ui.grid','ui.grid.pagination','ui.grid.selection', 'ui.grid.exporter');
app.controller("reportSearchController", function ($scope,$http,$parse,uiGridConstants,Grid) { 
    $scope.searchdData = {
    		  "metaReport" : {
    			    "pagination" : true,
    			    "pageSize" : 20,
    			    "totalSize" : 9,
    			    "pageNo" : -1
    			  },
    			  "columns" : [ [ {
    			    "columnId" : "no",
    			    "columnTitle" : "No",
    			    "columnWidth" : "5%",
    			    "alignment" : "Center",
    			    "linkURL" : null,
    			    "linkTitle" : null,
    			    "linkForm" : null,
    			    "linkConfirmMsg" : null,
    			    "linkImg" : null,
    			    "linkImgWidth" : null,
    			    "linkImgHeight" : null,
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "No",
    			    "columnWidthHtml" : " width=5%",
    			    "linkImgSizeHtml" : ""
    			  }, {
    			    "columnId" : "rep_id",
    			    "columnTitle" : "Report ID",
    			    "columnWidth" : "5%",
    			    "alignment" : "Center",
    			    "linkURL" : null,
    			    "linkTitle" : null,
    			    "linkForm" : null,
    			    "linkConfirmMsg" : null,
    			    "linkImg" : null,
    			    "linkImgWidth" : null,
    			    "linkImgHeight" : null,
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "Report ID",
    			    "columnWidthHtml" : " width=5%",
    			    "linkImgSizeHtml" : ""
    			  }, {
    			    "columnId" : "rep_name",
    			    "columnTitle" : "Report Name",
    			    "columnWidth" : "25%",
    			    "alignment" : "Left",
    			    "linkURL" : null,
    			    "linkTitle" : null,
    			    "linkForm" : null,
    			    "linkConfirmMsg" : null,
    			    "linkImg" : null,
    			    "linkImgWidth" : null,
    			    "linkImgHeight" : null,
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "Report Name",
    			    "columnWidthHtml" : " width=25%",
    			    "linkImgSizeHtml" : ""
    			  }, {
    			    "columnId" : "descr",
    			    "columnTitle" : "Description",
    			    "columnWidth" : "30%",
    			    "alignment" : "Left",
    			    "linkURL" : null,
    			    "linkTitle" : null,
    			    "linkForm" : null,
    			    "linkConfirmMsg" : null,
    			    "linkImg" : null,
    			    "linkImgWidth" : null,
    			    "linkImgHeight" : null,
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "Description",
    			    "columnWidthHtml" : " width=30%",
    			    "linkImgSizeHtml" : ""
    			  }, {
    			    "columnId" : "owner",
    			    "columnTitle" : "Report Owner",
    			    "columnWidth" : "10%",
    			    "alignment" : "Center",
    			    "linkURL" : null,
    			    "linkTitle" : null,
    			    "linkForm" : null,
    			    "linkConfirmMsg" : null,
    			    "linkImg" : null,
    			    "linkImgWidth" : null,
    			    "linkImgHeight" : null,
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "Report Owner",
    			    "columnWidthHtml" : " width=10%",
    			    "linkImgSizeHtml" : ""
    			  }, {
    			    "columnId" : "create_date",
    			    "columnTitle" : "Create Date",
    			    "columnWidth" : "10%",
    			    "alignment" : "Center",
    			    "linkURL" : null,
    			    "linkTitle" : null,
    			    "linkForm" : null,
    			    "linkConfirmMsg" : null,
    			    "linkImg" : null,
    			    "linkImgWidth" : null,
    			    "linkImgHeight" : null,
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "Create Date",
    			    "columnWidthHtml" : " width=10%",
    			    "linkImgSizeHtml" : ""
    			  }, {
    			    "columnId" : "copy",
    			    "columnTitle" : "&nbsp;&nbsp;Copy&nbsp;&nbsp;",
    			    "columnWidth" : "5%",
    			    "alignment" : "Center",
    			    "linkURL" : "document.forma.r_action.value='report.copy';",
    			    "linkTitle" : "Copy report",
    			    "linkForm" : "forma",
    			    "linkConfirmMsg" : "Are you sure you want to create a copy of this report?",
    			    "linkImg" : "static/fusion/raptor/images/modify_icon.gif",
    			    "linkImgWidth" : "13",
    			    "linkImgHeight" : "12",
    			    "copyLink" : true,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "&nbsp;&nbsp;Copy&nbsp;&nbsp;",
    			    "columnWidthHtml" : " width=5%",
    			    "linkImgSizeHtml" : " width=\"13\" height=\"12\""
    			  }, {
    			    "columnId" : "edit",
    			    "columnTitle" : "&nbsp;&nbsp;Edit&nbsp;&nbsp;",
    			    "columnWidth" : "5%",
    			    "alignment" : "Center",
    			    "linkURL" : "document.forma.r_action.value='report.edit';",
    			    "linkTitle" : "Edit report",
    			    "linkForm" : "forma",
    			    "linkConfirmMsg" : null,
    			    "linkImg" : "static/fusion/raptor/images/pen_paper.gif",
    			    "linkImgWidth" : "12",
    			    "linkImgHeight" : "12",
    			    "copyLink" : false,
    			    "editLink" : true,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "&nbsp;&nbsp;Edit&nbsp;&nbsp;",
    			    "columnWidthHtml" : " width=5%",
    			    "linkImgSizeHtml" : " width=\"12\" height=\"12\""
    			  }, {
    			    "columnId" : "delete",
    			    "columnTitle" : "Delete",
    			    "columnWidth" : "5%",
    			    "alignment" : "Center",
    			    "linkURL" : "document.forma.r_action.value='report.delete';",
    			    "linkTitle" : "Delete report",
    			    "linkForm" : "forma",
    			    "linkConfirmMsg" : "Are you sure you want to delete this report?",
    			    "linkImg" : "static/fusion/raptor/images/deleteicon.gif",
    			    "linkImgWidth" : "12",
    			    "linkImgHeight" : "12",
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : true,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "Delete",
    			    "columnWidthHtml" : " width=5%",
    			    "linkImgSizeHtml" : " width=\"12\" height=\"12\""
    			  }, {
    			    "columnId" : "schedule",
    			    "columnTitle" : "Schedule",
    			    "columnWidth" : "5%",
    			    "alignment" : "Center",
    			    "linkURL" : "document.forma.r_action.value='report.schedule.report.submit_wmenu';",
    			    "linkTitle" : "Schedule report",
    			    "linkForm" : "forma",
    			    "linkConfirmMsg" : null,
    			    "linkImg" : "static/fusion/raptor/images/calendar_icon.gif",
    			    "linkImgWidth" : "20",
    			    "linkImgHeight" : "20",
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : true,
    			    "columnTitleHtml" : "Schedule",
    			    "columnWidthHtml" : " width=5%",
    			    "linkImgSizeHtml" : " width=\"20\" height=\"20\""
    			  }, {
    			    "columnId" : "run",
    			    "columnTitle" : "&nbsp;&nbsp;Run&nbsp;&nbsp;",
    			    "columnWidth" : "5%",
    			    "alignment" : "Center",
    			    "linkURL" : "document.forma.r_action.value='report.run';",
    			    "linkTitle" : "Run report",
    			    "linkForm" : "forma",
    			    "linkConfirmMsg" : null,
    			    "linkImg" : "static/fusion/raptor/images/test_run.gif",
    			    "linkImgWidth" : "12",
    			    "linkImgHeight" : "12",
    			    "copyLink" : false,
    			    "editLink" : false,
    			    "deleteLink" : false,
    			    "scheduleLink" : false,
    			    "columnTitleHtml" : "&nbsp;&nbsp;Run&nbsp;&nbsp;",
    			    "columnWidthHtml" : " width=5%",
    			    "linkImgSizeHtml" : " width=\"12\" height=\"12\""
    			  } ] ],
    			  "rows" : [ [ [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "1",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "1",
    			      "displayValueHtml" : "1",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "610",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "610",
    			      "displayValueHtml" : "610",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "610",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "610",
    			      "displayValueHtml" : "610",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"610\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 1</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"610\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 1</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"610\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 1</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"610\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 1</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"610\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 1</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"610\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 1</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/02/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/02/2009",
    			      "displayValueHtml" : "03/02/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/02/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/02/2009",
    			      "displayValueHtml" : "03/02/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Schedule report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Schedule report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Schedule report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Schedule report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='610'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ], [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "2",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "2",
    			      "displayValueHtml" : "2",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "630",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "630",
    			      "displayValueHtml" : "630",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "630",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "630",
    			      "displayValueHtml" : "630",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"630\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 2</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"630\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 2</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"630\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 2</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"630\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 2</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"630\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 2</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"630\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 2</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/03/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/03/2009",
    			      "displayValueHtml" : "03/03/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/03/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/03/2009",
    			      "displayValueHtml" : "03/03/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='630'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='630'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='630'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='630'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='630'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='630'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='630'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='630'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ], [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "3",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "3",
    			      "displayValueHtml" : "3",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "637",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "637",
    			      "displayValueHtml" : "637",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "637",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "637",
    			      "displayValueHtml" : "637",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"637\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 3 </a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"637\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 3 </a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"637\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 3 </a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"637\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 3 </a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"637\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 3 </a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"637\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Dashboard Type: Report 3 </a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/04/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/04/2009",
    			      "displayValueHtml" : "03/04/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/04/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/04/2009",
    			      "displayValueHtml" : "03/04/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Schedule report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Schedule report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Schedule report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Schedule report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='637'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ], [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "4",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "4",
    			      "displayValueHtml" : "4",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "3321",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "3321",
    			      "displayValueHtml" : "3321",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "3321",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "3321",
    			      "displayValueHtml" : "3321",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3321\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_BAR_CHART_INTERACTIVE</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3321\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_BAR_CHART_INTERACTIVE</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3321\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_BAR_CHART_INTERACTIVE</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3321\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_BAR_CHART_INTERACTIVE</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3321\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_BAR_CHART_INTERACTIVE</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3321\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_BAR_CHART_INTERACTIVE</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/06/2013",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/06/2013",
    			      "displayValueHtml" : "03/06/2013",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/06/2013",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/06/2013",
    			      "displayValueHtml" : "03/06/2013",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3321'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3321'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3321'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3321'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3321'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3321'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3321'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3321'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ], [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "5",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "5",
    			      "displayValueHtml" : "5",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "1012",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "1012",
    			      "displayValueHtml" : "1012",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "1012",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "1012",
    			      "displayValueHtml" : "1012",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"1012\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Demo: Dashboard - 2</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"1012\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Demo: Dashboard - 2</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"1012\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Demo: Dashboard - 2</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"1012\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Demo: Dashboard - 2</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"1012\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Demo: Dashboard - 2</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"1012\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Demo: Dashboard - 2</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "05/06/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "05/06/2009",
    			      "displayValueHtml" : "05/06/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "05/06/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "05/06/2009",
    			      "displayValueHtml" : "05/06/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Schedule report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Schedule report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Schedule report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/calendar_icon.gif\" width=\"20\" height=\"20\" border=\"0\" alt=\"Schedule report\" onClick=\"document.forma.r_action.value='report.schedule.report.submit_wmenu'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Schedule report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='1012'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ], [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "6",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "6",
    			      "displayValueHtml" : "6",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "3322",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "3322",
    			      "displayValueHtml" : "3322",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "3322",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "3322",
    			      "displayValueHtml" : "3322",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3322\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_LINE_CHART_INTERACTIVE</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3322\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_LINE_CHART_INTERACTIVE</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3322\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_LINE_CHART_INTERACTIVE</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3322\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_LINE_CHART_INTERACTIVE</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3322\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_LINE_CHART_INTERACTIVE</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3322\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_LINE_CHART_INTERACTIVE</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/06/2013",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/06/2013",
    			      "displayValueHtml" : "03/06/2013",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "03/06/2013",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "03/06/2013",
    			      "displayValueHtml" : "03/06/2013",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3322'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3322'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3322'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3322'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3322'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3322'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3322'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3322'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ], [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "7",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "7",
    			      "displayValueHtml" : "7",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "3304",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "3304",
    			      "displayValueHtml" : "3304",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "3304",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "3304",
    			      "displayValueHtml" : "3304",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3304\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_PIE_CHART</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3304\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_PIE_CHART</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3304\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_PIE_CHART</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3304\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_PIE_CHART</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3304\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_PIE_CHART</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"3304\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>DEMO_PIE_CHART</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "12/11/2012",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "12/11/2012",
    			      "displayValueHtml" : "12/11/2012",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "12/11/2012",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "12/11/2012",
    			      "displayValueHtml" : "12/11/2012",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3304'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3304'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3304'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='3304'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3304'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3304'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3304'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='3304'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ], [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "8",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "8",
    			      "displayValueHtml" : "8",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "526",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "526",
    			      "displayValueHtml" : "526",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "526",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "526",
    			      "displayValueHtml" : "526",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"526\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>RS Report CrossTab 3</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"526\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>RS Report CrossTab 3</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"526\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>RS Report CrossTab 3</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"526\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>RS Report CrossTab 3</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"526\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>RS Report CrossTab 3</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"526\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>RS Report CrossTab 3</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "CrossTab ",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "CrossTab ",
    			      "displayValueHtml" : "CrossTab ",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "CrossTab ",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "CrossTab ",
    			      "displayValueHtml" : "CrossTab ",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "01/29/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "01/29/2009",
    			      "displayValueHtml" : "01/29/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "01/29/2009",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "01/29/2009",
    			      "displayValueHtml" : "01/29/2009",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='526'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='526'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='526'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='526'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='526'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='526'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='526'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='526'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ], [ {
    			    "columnId" : "no",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "9",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "9",
    			      "displayValueHtml" : "9",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "2671",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "2671",
    			      "displayValueHtml" : "2671",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_id",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "2671",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "2671",
    			      "displayValueHtml" : "2671",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"2671\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Test: CROSSTAB</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"2671\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Test: CROSSTAB</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"2671\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Test: CROSSTAB</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "rep_name",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"2671\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Test: CROSSTAB</a>",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"2671\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Test: CROSSTAB</a>",
    			      "displayValueHtml" : "<a class=\"hyperref1\" href='#' onClick='document.forma.r_action.value=\"report.run\";document.forma.c_master.value=\"2671\";document.forma.refresh.value=\"Y\";document.forma.submit();return false;'>Test: CROSSTAB</a>",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "descr",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "owner",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Super User",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "Super User",
    			      "displayValueHtml" : "Super User",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "09/22/2010",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "09/22/2010",
    			      "displayValueHtml" : "09/22/2010",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "create_date",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "09/22/2010",
    			      "alignment" : "Center",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "09/22/2010",
    			      "displayValueHtml" : "09/22/2010",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='2671'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='2671'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "copy",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Copy report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='2671'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/modify_icon.gif\" width=\"13\" height=\"12\" border=\"0\" alt=\"Copy report\" onClick=\"if(! confirm('Are you sure you want to create a copy of this report?')) return false; document.forma.r_action.value='report.copy'; document.forma.c_master.value='2671'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Copy report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "edit",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "delete",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "schedule",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "",
    			      "alignment" : "Left",
    			      "drillDownLink" : null,
    			      "displayValueLinkHtml" : "&nbsp;",
    			      "displayValueHtml" : "&nbsp;",
    			      "alignmentHtml" : " align=Left"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='2671'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='2671'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  }, {
    			    "columnId" : "run",
    			    "searchresultField" : {
    			      "columnId" : null,
    			      "displayValue" : "Run report",
    			      "alignment" : "Center",
    			      "drillDownLink" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='2671'; document.forma.refresh.value='Y';\">",
    			      "displayValueLinkHtml" : "<input type=\"image\" src=\"static/fusion/raptor/images/test_run.gif\" width=\"12\" height=\"12\" border=\"0\" alt=\"Run report\" onClick=\"document.forma.r_action.value='report.run'; document.forma.c_master.value='2671'; document.forma.refresh.value='Y';\">",
    			      "displayValueHtml" : "Run report",
    			      "alignmentHtml" : " align=Center"
    			    }
    			  } ] ] ]
    			};
    var getByColumnId = function(input, id) {
  		    var i=0, len=input.length;
  		    for (; i<len; i++) {
  		      if (+input[i].columnId == +id) {
  		        return input[i];
  		      }
  		    }
  		    return null;
  		  };


	var paginationOptions = {
		    pageNumber: 1,
		    pageSize: 25,
		    sort: null
		  };
	  Grid.prototype.getCellValue = function getCellValue(row, col){
			 if(col.field.indexOf('==')>-1){
				 var obj = row.entity.filter(function(d){if(d.columnId==col.field.substring(2)) return true; });
				 if(obj.length>0){
				   return obj[0].searchresultField.displayValue;
				 }
			 }
	    if ( typeof(row.entity[ '$$' + col.uid ]) !== 'undefined' ) {
	      return row.entity[ '$$' + col.uid].rendered;
	    } else if (this.options.flatEntityAccess && typeof(col.field) !== 'undefined' ){
	      return row.entity[col.field];
	    } else {

	      if (!col.cellValueGetterCache) {
	        col.cellValueGetterCache = $parse(row.getEntityQualifiedColField(col));
	      }
	      return col.cellValueGetterCache(row);
	    }
	  };
	  
		  $scope.gridOptions = {
		    paginationPageSizes: [25, 50, 75],
		    paginationPageSize: 25,
		    enableGridMenu: true,
		    enableSelectAll: true,
		    exporterCsvFilename: 'myFile.csv',
		    exporterPdfDefaultStyle: {fontSize: 9},
		    exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
		    exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
		    exporterPdfHeader: { text: "My Header", style: 'headerStyle' },
		    exporterPdfFooter: function ( currentPage, pageCount ) {
		      return { text: currentPage.toString() + ' of ' + pageCount.toString(), style: 'footerStyle' };
		    },
		    exporterPdfCustomFormatter: function ( docDefinition ) {
		      docDefinition.styles.headerStyle = { fontSize: 22, bold: true };
		      docDefinition.styles.footerStyle = { fontSize: 10, bold: true };
		      return docDefinition;
		    },
		    exporterPdfOrientation: 'portrait',
		    exporterPdfPageSize: 'LETTER',
		    exporterPdfMaxGridWidth: 500,
		    exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")),
		    onRegisterApi: function(gridApi){
		      $scope.gridApi = gridApi;
		    },
		    useExternalPagination: true,
		    useExternalSorting: true,
		    columnDefs: [  ],
		    onRegisterApi: function(gridApi) {
		      $scope.gridApi = gridApi;
		      $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
		        if (sortColumns.length == 0) {
		          paginationOptions.sort = null;
		        } else {
		          paginationOptions.sort = sortColumns[0].sort.direction;
		        }
		        getPage();
		      });
		      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		        paginationOptions.pageNumber = newPage;
		        paginationOptions.pageSize = pageSize;
		        getPage();
		      });
		    }
		  };
		  
		  //$scope.getCellValue = function() {
		//	  $scope.gridOptions.data = $scope.searchdData.rows[0];
		//	  console.log($scope.gridOptions.data[1][0].displayValue);
		 // };
		 


		  var getPage = function() {
		  };
		  $scope.searchdData.columns[0].forEach(function(entry) {
			  $scope.gridOptions.columnDefs.push({ displayName: entry.columnTitle, field: '=='+entry.columnId});
		  }); 
		  $scope.gridOptions.data = $scope.searchdData.rows[0].splice(1,8);

		  getPage();

});

</script>
