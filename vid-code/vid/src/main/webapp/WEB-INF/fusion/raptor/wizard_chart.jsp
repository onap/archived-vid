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
<%--
 Name: wizard_chart.jsp
 Use : This JSP is invoked when chart tab is selected. This is used for creating chart configuration for the report. 
 
 Change Log
 ==========
 
 12-Aug-2009 : Version 8.5 (Sundar);
 				<UL> 
 				<LI> For Line Chart Category can be configured. For this UI is added.</LI>
 				<LI> Line Chart can be displayed as 3D or 2D.</LI>
 				</UL>
 29-Jun-2009 : Version 8.4 (Sundar);
 				<UL> 
 				<LI> For Bar Chart Last Series/Category can be configured as Line Chart or Bar Chart. For this UI is added.</LI>
 				<LI> UI options for compare to prev year chart has been added. </LI>
 				</UL>

 23-Jun-2009 : Version 8.4 (Sundar);
 				<UL> 
 				<LI> Hiding/ Unhiding parameters based on chart type is checked throughly and missing elements were added.</LI>
 				<LI> Table width is made 100% for special input parameters </LI>
 				</UL>
 
 22-Jun-2009 : Version 8.4 (Sundar); 
 				
 				<UL> 
 				<LI> Calendar JS and CSS were added to this page as it is used in customizable input parameters for Time Difference Chart. </LI>
 				<LI> JS method and configurable input parameters were added for Multiple Pie Chart, Bar Chart 3D, Pareto, Time Difference Chart and Multiple
 				     Time Series Chart. </LI>
 				</UL>
--%>
<%@page import="org.openecomp.portalsdk.analytics.model.runtime.FormField"%>
<%@page import="org.openecomp.portalsdk.analytics.model.runtime.ReportFormFields"%>
<%@page import="org.openecomp.portalsdk.analytics.model.runtime.ReportRuntime"%>
<%@page import="org.openecomp.portalsdk.analytics.model.base.IdNameValue"%>
<%@page import="org.openecomp.portalsdk.analytics.model.DataCache"%>
<%@page import="org.openecomp.portalsdk.analytics.model.ReportHandler"%>
<%@page import="java.util.Vector"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.DataColumnType" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportDefinition" %>
<%@ page import="org.openecomp.portalsdk.analytics.controller.WizardSequence" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.AppConstants" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.Globals" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.AppUtils" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.Reports"%>

<% 	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();    
   	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED) || rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED_DATAMIN);

 	String legendColId = null;
	String valueColId = null;
	
	//String firstColId = null;
	//String firstNumColId = null;
	
	List reportCols     = rdef.getAllColumns();
	List chartValueCols = rdef.getChartValueColumnsList(AppConstants.CHART_ALL_COLUMNS, null);
	
	ArrayList unusedNumCols = new ArrayList(reportCols.size());
	int  numColsCount = 0;
	for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
		DataColumnType dct = (DataColumnType) iter.next();
		
		if(nvl(dct.getColOnChart()).equals(AppConstants.GC_LEGEND))
			legendColId = dct.getColId();
		
		if(isSQLBased||nvl(dct.getColType()).equals(AppConstants.CT_NUMBER)) {
			numColsCount++;
			if(nvl(dct.getColOnChart()).length()==0)	//dct.getChartSeq()<0)
				unusedNumCols.add(dct);
		}	// if
		
/*		if(dct.getChartSeq()>0)
			valueColId = dct.getColId();
		
		if(firstColId==null)
			firstColId = dct.getColId();
		if(firstNumColId==null)
			if(isSQLBased)
				firstNumColId = dct.getColId();
			else
				if(nvl(dct.getColType()).equals(AppConstants.CT_NUMBER))
					firstNumColId = dct.getColId();*/
	}   // for
	
	String chartType = nvl(rdef.getChartType()); %>

<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/CalendarPopup.js"></script> 
<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/calendar.css">

<script language="javascript">



function refreshWizard() {
   var selectBox = document.forma.chartType;
   var selectedString = selectBox.options[selectBox.selectedIndex].value;
   
   if(selectedString == '<%=AppConstants.GT_TIME_SERIES%>'){
   	document.getElementById("seriesDiv").style.display='block';
   	var selObj = document.getElementById('chartSeries');
   	if(selObj) {
	   	var selIndex = selObj.selectedIndex;
	   	var value = selObj.options[selIndex].value;
		var value = <%=chartValueCols.size()%>;
		if(value > 1) {
	     	document.getElementById("multiseriesDiv").style.display='block';
	     	
	    } else {
		 	document.getElementById("multiseriesDiv").style.display='none';    
			document.getElementById("multiSeries").value='N';    
		} 	
   	}
    <% 	int col1Idx = 1; %>
	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
   	if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="block";
   	if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="block";
	if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display="block";
	if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
   	if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
	if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
   	if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
	if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
   	if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
   	if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="block";
   	if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   	if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
   	if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='block';	
	if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='none';
	if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='block';
	if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='none';
	if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';
	    
		

   } else {
   <%
   	col1Idx = 1;
    for(int i=1; i<chartValueCols.size(); i++) {
		col1Idx++;
	%>
     document.getElementById("newChartDiv<%=col1Idx%>").style.visibility='hidden';
     //document.getElementById("chartGroupDiv<%=col1Idx%>").style.visibility='hidden';
     document.getElementById("seriesDiv").style.display='none';
     document.getElementById("multiseriesDiv").style.display='none';   
   	 if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
 	 if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
	if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
	if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='none';
  	 
	 
     <%
     }
    %>
 
   if(selectedString == "<%=AppConstants.GT_PIE_MULTIPLE%>") {
   //var contentIframe = window.parent.document.getElementById("content_Iframe");
   //contentIframe.style.height = contentIframe.clientHeight + 120; 
  		if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="block";
   		if (document.getElementById("multiplePieChartOptions"))  document.getElementById("multiplePieChartOptions").style.display="block";
   		if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
   	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
   		if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
   		if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
   		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="block";
   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='block';
   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='none';
   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='block';
   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='none';
   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

   }  else if(selectedString == "<%=AppConstants.GT_SCATTER%>") {
	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
			if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
	   		if (document.getElementById("multiplePieChartOptions"))  document.getElementById("multiplePieChartOptions").style.display="none";
	   		if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
	   	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
	   		if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='block';
	   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
	   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
	   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
	   		if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
	   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
	   		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="block";
	   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
	   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
	   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='block';
	   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='none';
	   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='block';
	   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='none';
	   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';


   }  else if(selectedString == "<%=AppConstants.GT_REGRESSION%>") {
	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
			if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
	   		if (document.getElementById("multiplePieChartOptions"))  document.getElementById("multiplePieChartOptions").style.display="none";
	   		if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
	   	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
	   		if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='block';
		   	if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
	   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='block';
	   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='block';
	   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
	   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
	   		if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
	   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
	   		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="none";
	   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
	   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
	   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='block';
	   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='none';
	   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='block';
	   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

	   		
   }  else if(selectedString == "<%=AppConstants.GT_STACKED_VERT_BAR%>" || selectedString == "<%=AppConstants.GT_STACKED_HORIZ_BAR%>" || selectedString == "<%=AppConstants.GT_STACKED_VERT_BAR_LINES%>"
	    || selectedString == "<%=AppConstants.GT_STACKED_HORIZ_BAR_LINES%>") {
	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
			if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
	   		if (document.getElementById("multiplePieChartOptions"))  document.getElementById("multiplePieChartOptions").style.display="none";
	   		if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
	   	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
	   		if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='block';
		   	if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
	   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
	   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='block';
	   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="block";
	   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
	   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
	   		if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
	   		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="block";
	   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
	   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
	   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='block';
	   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='none';
	   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='block';
	   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='block';
	   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

    }  else if(selectedString == "<%=AppConstants.GT_BAR_3D%>") { 
   		//var contentIframe = window.parent.document.getElementById("content_Iframe");
   		//contentIframe.style.height = contentIframe.clientHeight + 120; 
	   	if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="block";
   		if (document.getElementById("BarChartOptions"))  document.getElementById("BarChartOptions").style.display='block';
   	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display='block';
   		if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='block';
   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
	   	if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='block';
   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='block';
   		if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
		if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="block";
   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='block';
   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='block';
   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='none';
   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='block';
   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

    }  else if(selectedString == "<%=AppConstants.GT_LINE%>") { 
   		//var contentIframe = window.parent.document.getElementById("content_Iframe");
   		//contentIframe.style.height = contentIframe.clientHeight + 120; 
		if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
   		if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
   	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
   		if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='block';
   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
	   	if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='block';
   		if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
		if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="block";
   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='block';
   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='block';
   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='none';
   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='block';
   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='block';
   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='block';

    } else if (selectedString == "<%=AppConstants.GT_PARETO_CHART%>") {
 	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
		if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
	   	if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
	   	if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
	   	if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
  	 	if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
  		if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
  		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="none";
   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='block';
   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='none';
   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='block';
   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='none';
   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

    } else if (selectedString == "<%=AppConstants.GT_TIME_DIFFERENCE_CHART%>") {
  	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
 	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
		if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
 	  	if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
 	 	if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='block';
   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
 		if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
   		if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
   		if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
   		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="none";
   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='block';
   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='block';
   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='none';
   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='none';
   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='none';
   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

    } else if (selectedString == "<%=AppConstants.GT_COMPARE_PREVYEAR_CHART%>") {
   	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
  	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
		if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
  	  	if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
  	 	if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='block';
   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
  		if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
   		if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
   		if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
   		if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="none";
   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='block';
   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='none';
       	if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='none';
   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='none';
   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

    }else if (selectedString != "<%=AppConstants.GT_TIME_SERIES%>") { 
	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
	   	if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="block";
	   	if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="block";
	   	if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
	   	//if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
	   	if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
	   	if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
	   	if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="block";
   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='block';
   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

    } else 	if (selectedString == "<%=AppConstants.GT_MULTIPLE_TIMESERIES_CHART%>") {
 	   //var contentIframe = window.parent.document.getElementById("content_Iframe");
	   //contentIframe.style.height = contentIframe.clientHeight + 120; 
		if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
   			if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='none';
	   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
		   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
   			if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='block';
   	   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
   		   	if (document.getElementById("chartOptions"))  document.getElementById("chartOptions").style.display="block";
   		   	if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='none';
   	   		if (document.getElementById("lastSeriesAsLineChartDiv"))  document.getElementById("lastSeriesAsLineChartDiv").style.display='none';
   	   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
   		   	if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
   		   	if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
   		   	if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
   	   		if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
   		   	if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="block";
   	   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   	   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
   	 		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';

	 } else {
		   //var contentIframe = window.parent.document.getElementById("content_Iframe");
		   //contentIframe.style.height = contentIframe.clientHeight + 120; 
		if (document.getElementById("animatedDiv"))  document.getElementById("animatedDiv").style.display="none";
	   		if (document.getElementById("regressionChartDiv"))  document.getElementById("regressionChartDiv").style.display='none';
   		if (document.getElementById("LabelAngleForOtherCharts")) document.getElementById("LabelAngleForOtherCharts").style.display='block';
   		if (document.getElementById("LabelAngleForTimeSeriesCharts")) document.getElementById("LabelAngleForTimeSeriesCharts").style.display='none';
   		if (document.getElementById("maxLabelsInDomainAxisForOtherCharts")) document.getElementById("maxLabelsInDomainAxisForOtherCharts").style.display='none';
   		if (document.getElementById("CustomizingYAxisDiv"))  document.getElementById("CustomizingYAxisDiv").style.display='none';
	   	if (document.getElementById("drillDownOptions"))  document.getElementById("drillDownOptions").style.display="none";
   		if (document.getElementById("lastSeriesAsBarChartDiv"))  document.getElementById("lastSeriesAsBarChartDiv").style.display='none';
	   	if (document.getElementById("seriesDiv"))  document.getElementById("seriesDiv").style.display='none';
	   	if (document.getElementById("multiseriesDiv"))  document.getElementById("multiseriesDiv").style.display='none';
	   	if (document.getElementById("multiplePieChartOptions")) document.getElementById("multiplePieChartOptions").style.display="none";
	   	if (document.getElementById("barChartOptions"))  document.getElementById("barChartOptions").style.display="none";
	   	if (document.getElementById("stackBarChartOptions"))  document.getElementById("stackBarChartOptions").style.display="none";
	   	if (document.getElementById("AddRangeAxisDiv"))  document.getElementById("AddRangeAxisDiv").style.display="none";
   		if (document.getElementById("TimeDifferenceChartOptions")) document.getElementById("TimeDifferenceChartOptions").style.display='none';
   		if (document.getElementById("BarLineChartOptions")) document.getElementById("BarLineChartOptions").style.display='none';
   		if (document.getElementById("keepAsString")) document.getElementById("keepAsString").style.display='none';
	   	}
    }
 }


</script>

<iframe id="calendarFrame" class="nav" z-index:199; scrolling="no"  frameborder="0"  width=165px height=165px src="" style="position:absolute; display:none;">
</iframe>

 <div id="calendarDiv" name="calendarDiv" style="position:absolute; z-index:200; visibility:none; background-color:white;layer-background-color:white;"></div>
 

<table width="100%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=5 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
<%	if(numColsCount==0) { %>
	<tr>
		<td class=rbg2 colspan=5 align="center" height="30"><font class=rtabletext>No numeric columns found - chart not available</font></td>
	</tr>
	<input type="hidden" name="chartType" value="">
	<input type="hidden" name="chartWidth" value="">
	<input type="hidden" name="chartHeight" value="">
	
<%	} else { %>

	<tr>
	  <td colspan="4">
	    <table><tr>
		<td class=rbg2 align="right" width="50%" height="30"><font class=rtabletext>Chart Type: </font></td>
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="chartType" onChange="showAlertForTimeSeries()">
				<option value=""                                <%= chartType.equals(""                       )?" selected":"" %>>--- Do Not Include Chart ---
				<option value="<%= AppConstants.GT_BAR_3D    %>"<%= chartType.equals(AppConstants.GT_BAR_3D   ) || chartType.equals(AppConstants.GT_HORIZ_BAR   ) || chartType.equals(AppConstants.GT_VERT_BAR   ) || chartType.equals(AppConstants.GT_BAR_LINES)?" selected":"" %>>Bar Chart
				<option value="<%= AppConstants.GT_TOTAL_BAR %>"<%= chartType.equals(AppConstants.GT_TOTAL_BAR)?" selected":"" %>>Bar Chart With Running Total
                                <% if(!Globals.getJFreeVersion().equals(AppConstants.JFV_0911)) { %> 
				    <option value="<%= AppConstants.GT_STACKED_VERT_BAR %>"<%= chartType.equals(AppConstants.GT_STACKED_VERT_BAR)?" selected":"" %>>Vertical Stacked Bar				
				    <option value="<%= AppConstants.GT_STACKED_HORIZ_BAR %>"<%= chartType.equals(AppConstants.GT_STACKED_HORIZ_BAR)?" selected":"" %>>Horizontal Stacked Bar								
				    <option value="<%= AppConstants.GT_STACKED_VERT_BAR_LINES %>"<%= chartType.equals(AppConstants.GT_STACKED_VERT_BAR_LINES)?" selected":"" %>>Vertical Stacked Bar With Lines Chart				
				    <option value="<%= AppConstants.GT_STACKED_HORIZ_BAR_LINES %>"<%= chartType.equals(AppConstants.GT_STACKED_HORIZ_BAR_LINES)?" selected":"" %>>Horizontal Stacked Bar With Lines Chart								
                                <% } %> 
				<option value="<%= AppConstants.GT_LINE      %>"<%= chartType.equals(AppConstants.GT_LINE     )?" selected":"" %>>Line Chart
				<option value="<%= AppConstants.GT_PIE_3D    %>"<%= chartType.equals(AppConstants.GT_PIE_3D   )?" selected":"" %>>Pie Chart 3D
				<option value="<%= AppConstants.GT_PIE       %>"<%= chartType.equals(AppConstants.GT_PIE      )?" selected":"" %>>Pie Chart
				<option value="<%= AppConstants.GT_PIE_MULTIPLE %>"<%= chartType.equals(AppConstants.GT_PIE_MULTIPLE      )?" selected":"" %>>Multiple Pie Chart
				<option value="<%= AppConstants.GT_PARETO_CHART %>"<%= chartType.equals(AppConstants.GT_PARETO_CHART      )?" selected":"" %>>Pareto Chart
				<option value="<%= AppConstants.GT_MULTIPLE_TIMESERIES_CHART %>"<%= chartType.equals(AppConstants.GT_MULTIPLE_TIMESERIES_CHART      )?" selected":"" %>>Multiple TimeSeries Chart
				<option value="<%= AppConstants.GT_TIME_DIFFERENCE_CHART %>"<%= chartType.equals(AppConstants.GT_TIME_DIFFERENCE_CHART      )?" selected":"" %>>Time Difference Chart
				<option value="<%= AppConstants.GT_COMPARE_PREVYEAR_CHART %>"<%= chartType.equals(AppConstants.GT_COMPARE_PREVYEAR_CHART      )?" selected":"" %>>Compare Previous Year Chart
				<option value="<%= AppConstants.GT_SCATTER %>"<%= chartType.equals(AppConstants.GT_SCATTER      )?" selected":"" %>>Scatter Plot Chart
				<option value="<%= AppConstants.GT_REGRESSION %>"<%= chartType.equals(AppConstants.GT_REGRESSION      )?" selected":"" %>>Regression Chart
				<option value="<%= AppConstants.GT_HIERARCHICAL %>"<%= chartType.equals(AppConstants.GT_HIERARCHICAL      )?" selected":"" %>>Hierarchical (HTML5) Chart
				<option value="<%= AppConstants.GT_HIERARCHICAL_SUNBURST %>"<%= chartType.equals(AppConstants.GT_HIERARCHICAL_SUNBURST      )?" selected":"" %>>Hierarchical (HTML5) Sun Burst Chart
				
<%		if(!Globals.getJFreeVersion().equals(AppConstants.JFV_0911)) { %>				
				<option value="<%= AppConstants.GT_TIME_SERIES%>"<%= chartType.equals(AppConstants.GT_TIME_SERIES )?" selected":"" %>>Time Series Chart				
<%      } //It is supported only when chart library is above 0911 %>
<%		if(Globals.getShowNonstandardCharts()) { %>
				<option value="<%= AppConstants.GT_MTD_BAR   %>"<%= chartType.equals(AppConstants.GT_MTD_BAR  )?" selected":"" %>>Month To Date Bar Chart
<%		}	// if(Globals.getShowNonstandardCharts 
%>
			</select></font></td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<input type="checkbox" name="chartTypeFixed" value="Y"<%= rdef.isChartTypeFixed()?" checked":"" %>>
			&nbsp;Do NOT allow user to change chart type at runtime
		</td>	
		<td id="animatedDiv" style ="display:none;">
           <select name="animatedOption">
				<option value="static" <%=!rdef.isChartAnimate()?" selected":""%>> 	Static 	</option>
				<option value="animate" <%=rdef.isChartAnimate()?" selected":""%>> 	Animate </option>
		   </select>		   
		</td>
       </tr>
       </table>
       </td>	   
	</tr>
	<tr>
		<td class=rbg2 align="right"  height="30"><font class=rtabletext>Chart Width (px): </font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type="text" name="chartWidth" value="<%= ""+rdef.getChartWidthAsInt() %>" size="10" maxlength="4"></font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right"  height="30"><font class=rtabletext>Chart Height (px): </font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type="text" name="chartHeight" value="<%= ""+rdef.getChartHeightAsInt() %>" size="10" maxlength="4"></font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Domain Axis: </font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<select name="legendCol">
			<%	for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
					DataColumnType dct = (DataColumnType) iter.next(); %>
				<option value="<%= dct.getColId() %>"<%= nvl(legendColId /*, firstColId*/).equals(dct.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
			<%	} %>
			</select></font></td>
	</tr>
	<tr id="seriesDiv">
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Category:</font></td>
		<td class=rbg3 colspan=4 align="left" ><font class=rtabletext>
			<select name="chartSeries" onChange="refreshWizard()">
               <%	int isThereChartSeries = 0;
                    if(rdef.hasSeriesColumn()) isThereChartSeries = 1; 
                    if(isThereChartSeries == 0) {
               %>
  		             <option value="-1" selected> -->select series <-- </option>
               <%                      	
                    } else {
               %>     	
  		             <option value="-1"> -->select series <-- </option>  		         
               <%    
                    } // else
		               for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
					DataColumnType dct = (DataColumnType) iter.next(); 
							if(dct.isChartSeries()!=null && dct.isChartSeries().booleanValue()) {
               %>
               					<option value="<%= dct.getColId() %>" selected><%= dct.getDisplayName() %>
               <%
                    		} else {
							
               %>  							
				<option value="<%= dct.getColId() %>"><%= dct.getDisplayName() %>
               <%
                    		} // else Chart Series
				}	// for 
						
			%>
			</select></font></td>		
    </tr>		 
	<tr id = "lastSeriesAsLineChartDiv">
		<td> last Category display As Line Chart
		</td>
		<td class=rbg3 colspan=3 align="left" nowrap><font class=rtabletext>
			<input type="checkbox" name="lastSeriesALineChart" value="Y"<%= rdef.isLastSeriesALineChart()?" checked":"" %>>
		</td>	
	</tr>

	<tr id = "lastSeriesAsBarChartDiv">
		<td> last Category display As Bar Chart
		</td>
		<td class=rbg3 colspan=3 align="left" nowrap><font class=rtabletext>
			<input type="checkbox" name="lastSeriesABarChart" value="Y"<%= rdef.isLastSeriesABarChart()?" checked":"" %>>
		</td>	
	</tr>

	<tr id = "multiseriesDiv">
		<td class=rbg2 align="right"  height="30"><font class=rtabletext>Multi Series</font></td>
		<td class=rbg3 colspan=4 align="left" width="50%">
			<input type=radio name="multiSeries" value="Y" <%= (AppUtils.getRequestNvlValue(request, "multiSeries").trim().length()>0? (AppUtils.getRequestNvlValue(request, "multiSeries").equals("Y")? " checked ":""): (rdef.isMultiSeries() ? " checked ":" checked ")) %>><font class=rtabletext>Yes</font>
			<input type=radio name="multiSeries" value="N" <%= (AppUtils.getRequestNvlValue(request, "multiSeries").trim().length()>0? (AppUtils.getRequestNvlValue(request, "multiSeries").equals("N")? " checked ":""): (!rdef.isMultiSeries() ? " checked":"")) %>><font class=rtabletext>No</font>
    </tr>
    
<%		if(chartValueCols.size()==0) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Range Axis:</font></td> 
		<td class=rbg3 colspan=3 align="left" width="50%"><font class=rtabletext>
			<select name="valueCol1">
			<%	for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
					DataColumnType dct = (DataColumnType) iter.next(); 
					if(isSQLBased||nvl(dct.getColType()).equals(AppConstants.CT_NUMBER)) {%>
				<option value="<%= dct.getColId() %>"><%= dct.getDisplayName() %>
			<%		}	// if
				}	// for 
			%>
			</select></font></td>
		<td class=rbg3 align="center" nowrap><font class=rtabletext>
			<%	String sValue = ""; %>
			<select name="valueCol1Color">
				<option value=""<%=        sValue.equals("")       ?" selected":"" %>>Color: Default
				<option value="#00FFFF"<%= sValue.equals("#00FFFF")?" selected":"" %>>Aqua
				<option value="#000000"<%= sValue.equals("#000000")?" selected":"" %>>Black
				<option value="#0000FF"<%= sValue.equals("#0000FF")?" selected":"" %>>Blue
				<option value="#FF00FF"<%= sValue.equals("#FF00FF")?" selected":"" %>>Fuchsia
				<option value="#808080"<%= sValue.equals("#808080")?" selected":"" %>>Gray
				<option value="#008000"<%= sValue.equals("#008000")?" selected":"" %>>Green
				<option value="#00FF00"<%= sValue.equals("#00FF00")?" selected":"" %>>Lime
				<option value="#800000"<%= sValue.equals("#800000")?" selected":"" %>>Maroon
				<option value="#000080"<%= sValue.equals("#000080")?" selected":"" %>>Navy
				<option value="#808000"<%= sValue.equals("#808000")?" selected":"" %>>Olive
				<option value="#FF9900"<%= sValue.equals("#FF9900")?" selected":"" %>>Orange
				<option value="#800080"<%= sValue.equals("#800080")?" selected":"" %>>Purple
				<option value="#FF0000"<%= sValue.equals("#FF0000")?" selected":"" %>>Red
				<option value="#C0C0C0"<%= sValue.equals("#C0C0C0")?" selected":"" %>>Silver
				<option value="#008080"<%= sValue.equals("#008080")?" selected":"" %>>Teal
				<option value="#FFFFFF"<%= sValue.equals("#FFFFFF")?" selected":"" %>>White
				<option value="#FFFF00"<%= sValue.equals("#FFFF00")?" selected":"" %>>Yellow
			</select>
			</font></td>
	</tr>
<%		} else { 
			int count = 1;
			String colIdx = "";
			for(Iterator iterV=chartValueCols.iterator(); iterV.hasNext(); count++) { 
				colIdx = "";
				DataColumnType dctV = (DataColumnType) iterV.next();
				colIdx = dctV.getColId();
				int colAxisIdx = 0;
				boolean newChart = false;
				try {
					colAxisIdx = Integer.parseInt(dctV.getColOnChart());
				} catch(Exception e) {} 
				newChart = (dctV.isCreateInNewChart()!=null)?dctV.isCreateInNewChart().booleanValue():false;
				%>

	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Range Axis <%= count %>:</font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<select name="valueCol<%= count %>">
			<%	for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
					DataColumnType dct = (DataColumnType) iter.next(); 
					if(isSQLBased||nvl(dct.getColType()).equals(AppConstants.CT_NUMBER)) {%>
				<option value="<%= dct.getColId() %>"<%= dct.getColId().equals(dctV.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
			<%		}	// if
				}	// for 
			%>
			</select>
			<%	if(count>1) { %>
			&nbsp;
			<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_USER %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dctV.getColId() %>';">
			<%	} %>
			&nbsp;
			</font></td>
		<td class=rbg3 align="center" nowrap>
		<%
		  String chartGroupOrg = dctV.getChartGroup();
		  String yAxisGroup = dctV.getYAxis();	
		  String chartGroup = (chartGroupOrg!=null && chartGroupOrg.length()>0 && chartGroupOrg.indexOf("|")!= -1)?chartGroupOrg.substring(0,chartGroupOrg.lastIndexOf("|")):""; 
		  String yAxis 		= (yAxisGroup!=null && yAxisGroup.length()>0 && yAxisGroup.indexOf("|")!= -1)?yAxisGroup.substring(0,yAxisGroup.lastIndexOf("|")):""; 
		%>
             <div id="chartGroupDiv<%=colIdx%>">
		     <font class=rtabletext>Chart Title:</font><input type="text" name="chartGroup<%= colIdx%>Axis" value="<%=nvl(chartGroup).length()>0?chartGroup:""%>"/>
		     <font class=rtabletext>YAxis:</font><input type="text" name="YAxisLabel<%= colIdx%>" value="<%=nvl(yAxis).length()>0?yAxis:""%>"/>
            </div>
              
                </td>
		<td class=rbg3 align="center" nowrap><font class=rtabletext>
			<%	String sValue = nvl(dctV.getChartColor()); %>
			<select name="valueCol<%= count %>Color">
				<option value=""<%=        sValue.equals("")       ?" selected":"" %>>Color: Default
				<option value="#00FFFF"<%= sValue.equals("#00FFFF")?" selected":"" %>>Aqua
				<option value="#000000"<%= sValue.equals("#000000")?" selected":"" %>>Black
				<option value="#0000FF"<%= sValue.equals("#0000FF")?" selected":"" %>>Blue
				<option value="#FF00FF"<%= sValue.equals("#FF00FF")?" selected":"" %>>Fuchsia
				<option value="#808080"<%= sValue.equals("#808080")?" selected":"" %>>Gray
				<option value="#008000"<%= sValue.equals("#008000")?" selected":"" %>>Green
				<option value="#00FF00"<%= sValue.equals("#00FF00")?" selected":"" %>>Lime
				<option value="#800000"<%= sValue.equals("#800000")?" selected":"" %>>Maroon
				<option value="#000080"<%= sValue.equals("#000080")?" selected":"" %>>Navy
				<option value="#808000"<%= sValue.equals("#808000")?" selected":"" %>>Olive
				<option value="#FF9900"<%= sValue.equals("#FF9900")?" selected":"" %>>Orange
				<option value="#800080"<%= sValue.equals("#800080")?" selected":"" %>>Purple
				<option value="#FF0000"<%= sValue.equals("#FF0000")?" selected":"" %>>Red
				<option value="#C0C0C0"<%= sValue.equals("#C0C0C0")?" selected":"" %>>Silver
				<option value="#008080"<%= sValue.equals("#008080")?" selected":"" %>>Teal
				<option value="#FFFFFF"<%= sValue.equals("#FFFFFF")?" selected":"" %>>White
				<option value="#FFFF00"<%= sValue.equals("#FFFF00")?" selected":"" %>>Yellow
			</select>
			</font></td>
		<td class=rbg3 align="left" nowrap valign="middle"><font class=rtabletext>
			<%	if(count>1) { %>
                   <div id="newChartDiv<%=count%>">
		    <input type="checkbox" name="newChart<%= count %>Axis" value="Y"<%= (newChart)?" checked":"" %>>
			&nbsp;Create in New Chart			
                    </div>
			<input type="checkbox" name="valueCol<%= count %>Axis" value="Y"<%= (colAxisIdx>0)?" checked":"" %>>
			&nbsp;Use secondary axis (Line chart only)
			<%	} %>
			&nbsp;</font></td>
	</tr>
<%			}	// for 
		}	// else (chartValueCols.size()==0)
		if(unusedNumCols.size()>0) { %>
	<tr id="AddRangeAxisDiv">
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Add Values Column:</font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="valueColNew" onChange="if(! checkValueColNew()) return; document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_USER %>'; document.forma.submit();">
				<option value="" selected>--- Select Column ---
			<%	for(Iterator iter=unusedNumCols.iterator(); iter.hasNext(); ) { 
					DataColumnType dct = (DataColumnType) iter.next(); 
					if(isSQLBased||nvl(dct.getColType()).equals(AppConstants.CT_NUMBER)) {%>
				<option value="<%= dct.getColId() %>"><%= dct.getDisplayName() %>
			<%		}	// if
				}	// for 
			%>
			</select>
			</font></td>
		<td class=rbg3 align="center" nowrap><font class=rtabletext>
			<%	String sValue = ""; %>
			<select name="valueColNewColor">
				<option value=""<%=        sValue.equals("")       ?" selected":"" %>>Color: Default
				<option value="#00FFFF"<%= sValue.equals("#00FFFF")?" selected":"" %>>Aqua
				<option value="#000000"<%= sValue.equals("#000000")?" selected":"" %>>Black
				<option value="#0000FF"<%= sValue.equals("#0000FF")?" selected":"" %>>Blue
				<option value="#FF00FF"<%= sValue.equals("#FF00FF")?" selected":"" %>>Fuchsia
				<option value="#808080"<%= sValue.equals("#808080")?" selected":"" %>>Gray
				<option value="#008000"<%= sValue.equals("#008000")?" selected":"" %>>Green
				<option value="#00FF00"<%= sValue.equals("#00FF00")?" selected":"" %>>Lime
				<option value="#800000"<%= sValue.equals("#800000")?" selected":"" %>>Maroon
				<option value="#000080"<%= sValue.equals("#000080")?" selected":"" %>>Navy
				<option value="#808000"<%= sValue.equals("#808000")?" selected":"" %>>Olive
				<option value="#FF9900"<%= sValue.equals("#FF9900")?" selected":"" %>>Orange
				<option value="#800080"<%= sValue.equals("#800080")?" selected":"" %>>Purple
				<option value="#FF0000"<%= sValue.equals("#FF0000")?" selected":"" %>>Red
				<option value="#C0C0C0"<%= sValue.equals("#C0C0C0")?" selected":"" %>>Silver
				<option value="#008080"<%= sValue.equals("#008080")?" selected":"" %>>Teal
				<option value="#FFFFFF"<%= sValue.equals("#FFFFFF")?" selected":"" %>>White
				<option value="#FFFF00"<%= sValue.equals("#FFFF00")?" selected":"" %>>Yellow
			</select>
			</font></td>
		<td colspan="2" class=rbg3 align="left" nowrap><font class=rtabletext>
			<input type="checkbox" name="valueColNewAxis" value="Y">
			&nbsp;Use secondary axis (Line chart only)
			</font></td>
	</tr>
<%		}	// if(unusedNumCols.size()==0)
		if(chartValueCols.size()>1 || rdef.hasSeriesColumn()) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Primary Axis Label:</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type="text" name="leftAxisLabel" class=rtabletext value="<%= nvl(rdef.getChartLeftAxisLabel()) %>" size="40">
			&nbsp;(Multi-series Chart Only)</font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Secondary Axis Label:</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type="text" name="rightAxisLabel" class=rtabletext value="<%= nvl(rdef.getChartRightAxisLabel()) %>" size="40">
			&nbsp;(Multi-series Chart Only)</font></td>
	</tr>
<%		}	// if(chartValueCols.size()>1)
%>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>&nbsp;</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			Note: Some chart types (like Pie) will only display Series 1 Values.</font></td>
	</tr>
	<tr>
	  <td colspan=5>
	   
	<div id="multiplePieChartOptions" style="display:none;">
	<table width="100%">
	<tr>
		<td colspan="3"><font class=rtabletext>Additional Multiple Pie Charts options:</font></td> 
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Order by </font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="multiplePieOrder" value="row" <%= AppUtils.getRequestNvlValue(request, "multiplePieOrder").equals("row")?" checked": ((AppUtils.getRequestNvlValue(request, "multiplePieOrder").length()<=0)? (rdef.isMultiplePieOrderByRow() ?" checked": (rdef.isMultiplePieOrderByColumn()?"":" checked")):"") %>>Table By Row
			<input type=radio name="multiplePieOrder" value="column" <%= AppUtils.getRequestNvlValue(request, "multiplePieOrder").equals("column") ? " checked":((AppUtils.getRequestNvlValue(request, "multiplePieOrder").length()<=0)? (rdef.isMultiplePieOrderByColumn() ?" checked": (rdef.isMultiplePieOrderByRow()?"":" checked")):"") %>>Table By Column
		</font></td>
		<td>
		  <input type="checkbox" name="multiplePieOrderInRunPage" value="Y"<%= AppUtils.getRequestNvlValue(request, "multiplePieOrderInRunPage").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "multiplePieOrderInRunPage").length()<=0)?(rdef.displayPieOrderinRunPage()?" checked":""):"") %>> Display in run page
		</td>  
		
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Label Options</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="multiplePieLabelDisplay" value="percentage" 
			   <%= AppUtils.getRequestNvlValue(request, "multiplePieLabelDisplay").equals("percentage")?" checked": ((AppUtils.getRequestNvlValue(request, "multiplePieLabelDisplay").length()<=0)? (nvl(rdef.getMultiplePieLabelDisplay()).length() > 0 ? (rdef.getMultiplePieLabelDisplay().equals("percentage")?" checked":""):" checked"):"") %>>Display Percentage
			<input type=radio name="multiplePieLabelDisplay" value="value" 
			 <%= AppUtils.getRequestNvlValue(request, "multiplePieLabelDisplay").equals("value")?" checked": ((AppUtils.getRequestNvlValue(request, "multiplePieLabelDisplay").length()<=0)? (nvl(rdef.getMultiplePieLabelDisplay()).length() > 0 ? (rdef.getMultiplePieLabelDisplay().equals("value")?" checked":""):""):"") %>>Display Value
			<input type=radio name="multiplePieLabelDisplay" value="novalue" 
			 <%= AppUtils.getRequestNvlValue(request, "multiplePieLabelDisplay").equals("novalue")?" checked": ((AppUtils.getRequestNvlValue(request, "multiplePieLabelDisplay").length()<=0)? (nvl(rdef.getMultiplePieLabelDisplay()).length() > 0 ? (rdef.getMultiplePieLabelDisplay().equals("novalue")?" checked":""):" checked"):"") %>>Blank
		</font>
		</td>	 
		<td>
		  <input type="checkbox" name="multiplePieLabelDisplayInRunPage" value="Y"<%= AppUtils.getRequestNvlValue(request, "multiplePieLabelDisplayInRunPage").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "multiplePieLabelDisplayInRunPage").length()<=0)?(rdef.displayPieLabelDisplayinRunPage()?" checked":""):"") %>> Display in run page
		</td>  

	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Display Options</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="chartDisplay" value="3D" <%= AppUtils.getRequestNvlValue(request, "chartDisplay").equals("3D")?" checked": ((AppUtils.getRequestNvlValue(request, "chartDisplay").length()<=0)? (rdef.isChartDisplayIn3D()?" checked":""):"") %>>Display in 3D
			<input type=radio name="chartDisplay" value="normal" <%= AppUtils.getRequestNvlValue(request, "chartDisplay").equals("normal")?" checked": ((AppUtils.getRequestNvlValue(request, "chartDisplay").length()<=0)? (!rdef.isChartDisplayIn3D()?" checked":""):"") %>>Display in 2D
			</font></td>
		<td>
		  <input type="checkbox" name="chartDisplayInRunPage" value="Y"<%= AppUtils.getRequestNvlValue(request, "chartDisplayInRunPage").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "chartDisplayInRunPage").length()<=0)?(rdef.displayChartDisplayinRunPage()?" checked":""):"") %>> Display in run page
		</td>  

	</tr>
	</table>
	</div>
	
	<div id="regressionChartDiv" style="display:none;">
		<table width="100%" id = "regressionDiv">
			<tr>
		<td class=rbg2 align="right" width="20%" height="30"><font class=rtabletext>Regression Type </font></td> 
		<td class=rbg3 colspan=4 align="left" width="80%"><font class=rtabletext>
					<input type=checkbox name="LinearRegressionType" value="Y" <%= (AppUtils.getRequestNvlValue(request, "regressionType").trim().length()>0? (AppUtils.getRequestNvlValue(request, "regressionType").charAt(0) == 'Y'? " checked ":""): (nvl(rdef.getLinearRegression()).length()>0 && rdef.getLinearRegression().charAt(0) == 'Y' ? " checked ":" checked ")) %>><font class=rtabletext>Linear</font>
					<input type=checkbox name="ExpRegressionType" value="Y" <%= (AppUtils.getRequestNvlValue(request, "regressionType").trim().length()>1? (AppUtils.getRequestNvlValue(request, "regressionType").charAt(1) == 'Y'? " checked ":""): (nvl(rdef.getLinearRegression()).length()>1 && rdef.getLinearRegression().charAt(1) == 'Y' ? " checked ":" ")) %>><font class=rtabletext>Exponential</font>
					<input type="hidden" name="regressionType" value=""/>
		</font>  </td>
    		</tr>
    		<tr>
    		 <td class=rbg2 align="right" width="20%" height="30"><font class=rtabletext>Linear Regression Color </font></td>
				<td class=rbg3 align="left" colspan=4 width=80% nowrap><font class=rtabletext>
					<%	String linearColor = nvl(rdef.getLinearRegressionColor()); %>
					<select name="valueLinearRegressionColor">
						<option value=""<%=        linearColor.equals("")       ?" selected":"" %>>Color: Default
						<option value="#00FFFF"<%= linearColor.equals("#00FFFF")?" selected":"" %>>Aqua
						<option value="#000000"<%= linearColor.equals("#000000")?" selected":"" %>>Black
						<option value="#0000FF"<%= linearColor.equals("#0000FF")?" selected":"" %>>Blue
						<option value="#FF00FF"<%= linearColor.equals("#FF00FF")?" selected":"" %>>Fuchsia
						<option value="#808080"<%= linearColor.equals("#808080")?" selected":"" %>>Gray
						<option value="#008000"<%= linearColor.equals("#008000")?" selected":"" %>>Green
						<option value="#00FF00"<%= linearColor.equals("#00FF00")?" selected":"" %>>Lime
						<option value="#800000"<%= linearColor.equals("#800000")?" selected":"" %>>Maroon
						<option value="#000080"<%= linearColor.equals("#000080")?" selected":"" %>>Navy
						<option value="#808000"<%= linearColor.equals("#808000")?" selected":"" %>>Olive
						<option value="#FF9900"<%= linearColor.equals("#FF9900")?" selected":"" %>>Orange
						<option value="#800080"<%= linearColor.equals("#800080")?" selected":"" %>>Purple
						<option value="#FF0000"<%= linearColor.equals("#FF0000")?" selected":"" %>>Red
						<option value="#C0C0C0"<%= linearColor.equals("#C0C0C0")?" selected":"" %>>Silver
						<option value="#008080"<%= linearColor.equals("#008080")?" selected":"" %>>Teal
						<option value="#FFFFFF"<%= linearColor.equals("#FFFFFF")?" selected":"" %>>White
						<option value="#FFFF00"<%= linearColor.equals("#FFFF00")?" selected":"" %>>Yellow
					</select>
				</font></td>
    		  
    		</tr>
    		<tr>
    		 <td class=rbg2 align="right" width="20%" height="30"><font class=rtabletext>Exponential Regression Color </font></td>
				<td class=rbg3 align="left" colspan=4 width=80% nowrap><font class=rtabletext>
					<%	String expColor = nvl(rdef.getExponentialRegressionColor()); %>
					<select name="valueExponentialRegressionColor">
						<option value=""<%=        expColor.equals("")       ?" selected":"" %>>Color: Default
						<option value="#00FFFF"<%= expColor.equals("#00FFFF")?" selected":"" %>>Aqua
						<option value="#000000"<%= expColor.equals("#000000")?" selected":"" %>>Black
						<option value="#0000FF"<%= expColor.equals("#0000FF")?" selected":"" %>>Blue
						<option value="#FF00FF"<%= expColor.equals("#FF00FF")?" selected":"" %>>Fuchsia
						<option value="#808080"<%= expColor.equals("#808080")?" selected":"" %>>Gray
						<option value="#008000"<%= expColor.equals("#008000")?" selected":"" %>>Green
						<option value="#00FF00"<%= expColor.equals("#00FF00")?" selected":"" %>>Lime
						<option value="#800000"<%= expColor.equals("#800000")?" selected":"" %>>Maroon
						<option value="#000080"<%= expColor.equals("#000080")?" selected":"" %>>Navy
						<option value="#808000"<%= expColor.equals("#808000")?" selected":"" %>>Olive
						<option value="#FF9900"<%= expColor.equals("#FF9900")?" selected":"" %>>Orange
						<option value="#800080"<%= expColor.equals("#800080")?" selected":"" %>>Purple
						<option value="#FF0000"<%= expColor.equals("#FF0000")?" selected":"" %>>Red
						<option value="#C0C0C0"<%= expColor.equals("#C0C0C0")?" selected":"" %>>Silver
						<option value="#008080"<%= expColor.equals("#008080")?" selected":"" %>>Teal
						<option value="#FFFFFF"<%= expColor.equals("#FFFFFF")?" selected":"" %>>White
						<option value="#FFFF00"<%= expColor.equals("#FFFF00")?" selected":"" %>>Yellow
					</select>
				</font></td>
    		  
    		</tr>
			<tr>
				<td class=rbg2 align="right" width="20%" height="30"><font class=rtabletext>Regression Point customization (in Numbers)</font></td> 
 				<td class=rbg3 colspan=4 align="left" width="80%"><font class=rtabletext>
 				  <input type=text name="regressionPointCustomization" value="<%=nvl(rdef.getCustomizedRegressionPoint(),"")%>">
 				</font></td>   		
    		
    	</table>
    	
    </div>	

	<div id="CustomizingYAxisDiv" style="display:none;">
		<table width="100%">
		  	<tr>
				<td class=rbg2 align="right" width="20%" height="30"><font class=rtabletext>Range Axis (Minimum Range)</font></td> 
 				<td class=rbg3 colspan=4 align="left" width="80%"><font class=rtabletext>
 				  <input type=text name="yAxisLowerLimit" value="<%=nvl(rdef.getRangeAxisLowerLimit(),"")%>">
 				</font></td>
 				</tr>
 				<tr>
				<td class=rbg2 align="right" width="20%" height="30"><font class=rtabletext>Range Axis (Maximum Range)</font></td> 
 				<td class=rbg3 colspan=4 align="left" width="80%"><font class=rtabletext>
 				  <input type=text name="yAxisUpperLimit" value="<%=nvl(rdef.getRangeAxisUpperLimit(),"")%>">
 				</font></td>
			</tr> 				   		
		</table>
	</div>

	
	<div id="BarLineChartOptions" style="display:none;">
	<table width="100%">
	<tr>
		<td colspan="3"><font class=rtabletext>Additional Bar/Line Chart options:</font></td> 
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Orientation </font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="chartOrientation" value="vertical" <%= AppUtils.getRequestNvlValue(request, "chartOrientation").equals("vertical")?" checked": ((AppUtils.getRequestNvlValue(request, "chartOrientation").length()<=0)? (rdef.isVerticalOrientation() ?" checked": (rdef.isVerticalOrientation()?" checked":"")):"") %>>Vertical
			<input type=radio name="chartOrientation" value="horizontal" <%= AppUtils.getRequestNvlValue(request, "chartOrientation").equals("horizontal") ? " checked":((AppUtils.getRequestNvlValue(request, "chartOrientation").length()<=0)? (rdef.isHorizontalOrientation() ?" checked": (rdef.isHorizontalOrientation()?" checked":"")):"") %>>Horizontal
		</font></td>
		<td>
		  <input type="checkbox" name="chartOrientationInRunPage" value="Y"<%= AppUtils.getRequestNvlValue(request, "chartOrientationInRunPage").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "chartOrientationInRunPage").length()<=0)?(rdef.displayChartOrientationInRunPage()?" checked":""):"") %>> Display in run page
		</td>  
		
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Display Options</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="chartDisplay" value="3D" <%= AppUtils.getRequestNvlValue(request, "chartDisplay").equals("3D")?" checked": ((AppUtils.getRequestNvlValue(request, "chartDisplay").length()<=0)? (rdef.isChartDisplayIn3D()?" checked":""):"") %>>Display in 3D
			<input type=radio name="chartDisplay" value="normal" <%= AppUtils.getRequestNvlValue(request, "chartDisplay").equals("normal")?" checked": ((AppUtils.getRequestNvlValue(request, "chartDisplay").length()<=0)? (!rdef.isChartDisplayIn3D()?" checked":""):"") %>>Display in 2D
			</font></td>
		<td>
		  <input type="checkbox" name="chartDisplayInRunPage" value="Y"<%= AppUtils.getRequestNvlValue(request, "chartDisplayInRunPage").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "chartDisplayInRunPage").length()<=0)?(rdef.displayChartDisplayinRunPage()?" checked":""):"") %>> Display in run page
		</td>  
	</tr>
	</table>
	</div>
	<div id="BarChartOptions" style="display:none;">
	<table width="100%">
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Renderer for Secondary Axis</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="secondaryChartRenderer" value="line" 
			   <%= AppUtils.getRequestNvlValue(request, "secondaryChartRenderer").equals("line")?" checked": ((AppUtils.getRequestNvlValue(request, "secondaryChartRenderer").length()<=0)? ((nvl(rdef.getSecondaryChartRenderer()).length() > 0) ? (rdef.getSecondaryChartRenderer().equals("line")?" checked":""):" checked"):"") %>>Line chart Renderer
			<input type=radio name="secondaryChartRenderer" value="level" 
			 <%= AppUtils.getRequestNvlValue(request, "secondaryChartRenderer").equals("level")?" checked": ((AppUtils.getRequestNvlValue(request, "secondaryChartRenderer").length()<=0)? ((nvl(rdef.getSecondaryChartRenderer()).length() > 0) ? (rdef.getSecondaryChartRenderer().equals("level")?" checked":""):""):"") %>>Level chart Renderer
		</font>
		</td>	 
		<td>
		  <input type="checkbox" name="secondaryChartRendererInRunPage" value="Y"<%= AppUtils.getRequestNvlValue(request, "secondaryChartRendererInRunPage").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "secondaryChartRendererInRunPage").length()<=0)?(rdef.displaySecondaryChartRendererInRunPage()?" checked":""):"") %>> Display in run page
		</td>  

	</tr>
	</table>
	</div>
	
	<div id="stackBarChartOptions" style="display:none;">
	<table width="100%">
	<tr>
		<td colspan="3"><font class=rtabletext>Additional Bar Chart options:</font></td> 
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Overlay Item Value:</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="overlayItemValue" value="Y" 
			   <%= AppUtils.getRequestNvlValue(request, "overlayItemValue").equals("Y")?" checked": ((AppUtils.getRequestNvlValue(request, "overlayItemValue").length()<=0)? ((nvl(rdef.getOverlayItemValueOnStackBar()).length() > 0) ? (rdef.getOverlayItemValueOnStackBar().equals("Y")?" checked":""):" checked"):"") %>>Yes
			<input type=radio name="overlayItemValue" value="N" 
			 <%= AppUtils.getRequestNvlValue(request, "overlayItemValue").equals("N")?" checked": ((AppUtils.getRequestNvlValue(request, "overlayItemValue").length()<=0)? ((nvl(rdef.getOverlayItemValueOnStackBar()).length() > 0) ? (rdef.getOverlayItemValueOnStackBar().equals("N")?" checked":""):""):"") %>>No
		</font>
		</td>	 
	</tr>
	</table>
	</div>

	
	<div id="TimeDifferenceChartOptions" style="display:none;">
	<table width="100%">
	<tr>
		<td colspan="3"><font class=rtabletext>Additional Time Difference Chart options:</font></td> 
	</tr>
	<tr>
		<td class=rbg2 align="right" width="15%" height="30"><font class=rtabletext>Interval Marker </font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type="text" size="10" maxlength="10" name="intervalFromDate" value="<%=rdef.getIntervalFromdate()%>" toolTipText="Enter the starting interval date">
			<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20" onClick="oCalendar = new CalendarPopup('calendarDiv', 'calendarFrame');oCalendar.setCssPrefix('raptor');oCalendar.select(document.getElementById('intervalFromDate'),event,'MM/dd/yyyy'); return false;"  style="cursor:hand">
			<input type="text" size="10" maxlength="10" name="intervalToDate" value="<%=rdef.getIntervalTodate()%>" toolTipText="Enter the Ending interval date">
			<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20" onClick="oCalendar = new CalendarPopup('calendarDiv', 'calendarFrame');oCalendar.setCssPrefix('raptor');oCalendar.select(document.getElementById('intervalToDate'),event,'MM/dd/yyyy'); return false;"  style="cursor:hand">
			<input type="text" size="50" maxlength="100" name="intervalLabel" value="<%=rdef.getIntervalLabel()%>" toolTipText="Enter the interval Label">			
		</font></td>
		<td>
		  <input type="checkbox" name="intervalInputInRunPage" value="Y"<%= AppUtils.getRequestNvlValue(request, "intervalInputInRunPage").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "intervalInputInRunPage").length()<=0)?(rdef.displayIntervalInputInRunPage()?" checked":""):"") %>> Display in run page
		</td>  
		
	</tr>
	</table>
	</div>

	<div id="chartOptions" style="display:none;">
	<table width="100%">
	<tr>
		<td colspan="3"><font class=rtabletext>Additional chart options:</font></td> 
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Legend Position</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="legendPosition" value="bottom" <%= AppUtils.getRequestNvlValue(request, "legendPosition").equals("bottom")?" checked": ((AppUtils.getRequestNvlValue(request, "legendPosition").length()<=0)? (nvl(rdef.getLegendPosition()).length()>0 ? (rdef.getLegendPosition().equals("bottom")?" checked":""):" checked"):"") %>>Bottom
			<input type=radio name="legendPosition" value="top" <%= AppUtils.getRequestNvlValue(request, "legendPosition").equals("top")?" checked": ((AppUtils.getRequestNvlValue(request, "legendPosition").length()<=0)? (nvl(rdef.getLegendPosition()).length()>0 ? (rdef.getLegendPosition().equals("top")?" checked":""):""):"") %>>Top
			<input type=radio name="legendPosition" value="left" <%= AppUtils.getRequestNvlValue(request, "legendPosition").equals("left")?" checked": ((AppUtils.getRequestNvlValue(request, "legendPosition").length()<=0)? (nvl(rdef.getLegendPosition()).length()>0 ? (rdef.getLegendPosition().equals("left")?" checked":""):""):"") %>>Left
			<input type=radio name="legendPosition" value="right" <%= AppUtils.getRequestNvlValue(request, "legendPosition").equals("right")?" checked": ((AppUtils.getRequestNvlValue(request, "legendPosition").length()<=0)? (nvl(rdef.getLegendPosition()).length()>0 ? (rdef.getLegendPosition().equals("right")?" checked":""):""):"") %>>Right
			</font></td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Hide Tooltips</font></td>
		<td> 
		  <input type="checkbox" name="hideTooltips" value="Y"<%= AppUtils.getRequestNvlValue(request, "hideToolTips").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "hideToolTips").length()<=0)?(rdef.hideChartToolTips()?" checked":""):"") %>> &nbsp;
		</td>
		<td> &nbsp;</td>  
	</tr>
	<tr id="keepAsString">
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Keep Domain Axis Value As String</font></td>
		<td> 
		  <input type="checkbox" name="keepAsString" value="Y"<%= AppUtils.getRequestNvlValue(request, "keepAsString").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "keepAsString").length()<=0)?(rdef.keepDomainAxisValueInChartAsString()?" checked":""):"") %>> &nbsp;
		</td>
		<td> &nbsp;</td>  
	</tr>
	
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Hide Legend</font></td>
		<td> 
		  <input type="checkbox" name="hideLegend" value="Y"<%= AppUtils.getRequestNvlValue(request, "hideLegend").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "hideLegend").length()<=0)?(rdef.hideChartLegend()?" checked":""):"") %>> &nbsp;
		</td>
		<td>
		  <input type="checkbox" name="showLegendDisplayOptionsInRunPage" value="Y"<%= AppUtils.getRequestNvlValue(request, "showLegendDisplayOptionsInRunPage").equals("Y")? " checked":((AppUtils.getRequestNvlValue(request, "showLegendDisplayOptionsInRunPage").length()<=0)?(rdef.showLegendDisplayOptionsInRunPage()?" checked":""):"") %>> Display in run page
		</td>  
	</tr>
	<tr id ="maxLabelsInDomainAxisForOtherCharts">
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Maximum Number of Labels in Domain Axis:</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=text name="maxLabelsInDomainAxis" class=rtabletext value="<%= AppUtils.getRequestNvlValue(request, "maxLabelsInDomainAxis").trim().length()>0?
					  AppUtils.getRequestNvlValue(request, "maxLabelsInDomainAxis"): 
						  ((AppUtils.getRequestNvlValue(request, "maxLabelsInDomainAxis").length()<=0)?
								   (nvl(rdef.getMaxLabelsInDomainAxis()).length()>0 ? rdef.getMaxLabelsInDomainAxis():"1"):"1")%>" maxlength="2" size="2">
			</font>
		</td>
	</tr>
	<tr id ="LabelAngleForTimeSeriesCharts">
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Legend Label Angle</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="labelAngle" value="UP45" <%= AppUtils.getRequestNvlValue(request, "labelAngle").equals("UP45")?" checked": ((AppUtils.getRequestNvlValue(request, "labelAngle").length()<=0)? (nvl(rdef.getLegendLabelAngle()).length()>0 ? (rdef.getLegendLabelAngle().equals("UP45")?" checked":""):" checked"):"") %>>Standard
			<input type=radio name="labelAngle" value="UP90" <%= AppUtils.getRequestNvlValue(request, "labelAngle").equals("UP90")?" checked": ((AppUtils.getRequestNvlValue(request, "labelAngle").length()<=0)? (nvl(rdef.getLegendLabelAngle()).length()>0 ? (rdef.getLegendLabelAngle().equals("UP90")?" checked":""):" checked"):"") %>>90 &deg;
			</font></td>
		<td>
		   &nbsp;
		</td>  
	</tr>
	<tr id ="LabelAngleForOtherCharts">
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Legend Label Angle</font></td> 
		<td class=rbg3 colspan=4 align="left" width="50%"><font class=rtabletext>
			<input type=radio name="labelAngle" value="UP45" <%= AppUtils.getRequestNvlValue(request, "labelAngle").equals("UP45")?" checked": ((AppUtils.getRequestNvlValue(request, "labelAngle").length()<=0)? (nvl(rdef.getLegendLabelAngle()).length()>0 ? (rdef.getLegendLabelAngle().equals("UP45")?" checked":""):" checked"):"") %>>Up 45 &deg;
			<input type=radio name="labelAngle" value="UP90" <%= AppUtils.getRequestNvlValue(request, "labelAngle").equals("UP90")?" checked": 
				   ((AppUtils.getRequestNvlValue(request, "labelAngle").length()<=0)?
						   (nvl(rdef.getLegendLabelAngle()).length()>0 ? 
								   (rdef.getLegendLabelAngle().equals("UP90")?" checked":""):""):"") %>>Up 90 &deg;
			<input type=radio name="labelAngle" value="DOWN45" <%= AppUtils.getRequestNvlValue(request, "labelAngle").equals("DOWN45")?" checked": ((AppUtils.getRequestNvlValue(request, "labelAngle").length()<=0)? (nvl(rdef.getLegendLabelAngle()).length()>0 ? (rdef.getLegendLabelAngle().equals("DOWN45")?" checked":""):" checked"):"") %>>Down 45 &deg;
			<input type=radio name="labelAngle" value="DOWN90" <%= AppUtils.getRequestNvlValue(request, "labelAngle").equals("DOWN90")?" checked": ((AppUtils.getRequestNvlValue(request, "labelAngle").length()<=0)? (nvl(rdef.getLegendLabelAngle()).length()>0 ? (rdef.getLegendLabelAngle().equals("DOWN90")?" checked":""):" checked"):"") %>>Down 90 &deg;
			<input type=radio name="labelAngle" value="STANDARD" <%= AppUtils.getRequestNvlValue(request, "labelAngle").equals("STANDARD")?" checked": ((AppUtils.getRequestNvlValue(request, "labelAngle").length()<=0)? (nvl(rdef.getLegendLabelAngle()).length()>0 ? (rdef.getLegendLabelAngle().equals("STANDARD")?" checked":""):" checked"):"") %>>Standard
			</font></td>
		<td>
		   &nbsp;
		</td>  
	</tr>	
	</table>
	</div>
	
	<div id="drillDownOptions" style="display:none;">
		<table width="100%">
			<tr>
				<td colspan="2"><font class=rtabletext>Drilldown options:</font></td> 
			</tr>
			<tr>
			<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Drilldown report:</font></td>
			<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			   <select name="drillDownReport" onChange="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='GET_DRILLDOWN_FORMFIELDS'; document.forma.submit();">
			   <option value="-1">Please Select</option>
			   <%
				Vector publicReportIdNames = DataCache.getPublicReportIdNames();
				for(int i=0; i<publicReportIdNames.size(); i++) { 
					IdNameValue reportIdName = (IdNameValue) publicReportIdNames.get(i); %>
					<option value="<%= reportIdName.getId() %>"<%= (nvl(rdef.getDrillReportIdForChart()).equals(reportIdName.getId()))?" selected":"" %>>Public Report: <%= reportIdName.getName() %></option>
			<%	} %>
			   <%
				Vector groupReportIdNames = DataCache.getGroupAccessibleReportIdNames(AppUtils.getUserID(request),AppUtils.getUserRoles(request));
				for(int j=0; j<groupReportIdNames.size(); j++) { 
					IdNameValue reportIdName = (IdNameValue) groupReportIdNames.get(j); %>
						<option value="<%= reportIdName.getId() %>"<%= (nvl(rdef.getDrillReportIdForChart()).equals(reportIdName.getId()))?" selected":"" %>>Group Report: <%= reportIdName.getName() %></option>
			    <% } %>	
			    <%
			    Vector privateReportIdNames = DataCache.getPrivateAccessibleReportIdNames(AppUtils.getUserID(request),AppUtils.getUserRoles(request));
				for(int j=0; j<privateReportIdNames.size(); j++) { 
					IdNameValue reportIdName = (IdNameValue) privateReportIdNames.get(j); %>
						<option value="<%= reportIdName.getId() %>"<%= (nvl(rdef.getDrillReportIdForChart()).equals(reportIdName.getId()))?" selected":"" %>>Private Report: <%= reportIdName.getName() %></option>
	    <% } %>			
			    		
			   </select>
			 </font></td></tr>
			 <%
			 if(nvl(rdef.getDrillReportIdForChart()).length()>0) {
			 	ReportRuntime ddRr = (new ReportHandler()).loadReportRuntime(request, rdef.getDrillReportIdForChart(),
						false);
		        	if (ddRr != null)
		        		request.setAttribute("CHART_FORMFIELDS", ddRr.getReportFormFields());
			 }
			 %> 
			 
			 
           <% if(request.getAttribute("CHART_FORMFIELDS")!=null) { %>
				 <% ReportFormFields ddReportFormFields = (ReportFormFields) request.getAttribute("CHART_FORMFIELDS");  
				  if(ddReportFormFields!=null) {            
					   	for(ddReportFormFields.resetNext(); ddReportFormFields.hasNext(); ) { 
					   		FormField ff = ddReportFormFields.getNext();
					   		if(!ff.getFieldType().equals(FormField.FFT_BLANK)) { 
					   			
					   	%>
					   		<tr>
					   		  <td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext><%=ff.getFieldDisplayName()%>:</font></td>
					   		  <td class=rbg3 align="left" width="50%">
					   		  <font class=rtabletext>
					   				<select name="drillDown_<%=ff.getFieldName()%>">
					   					<option value="<%= ff.getFieldName() %>_xaxis" <%= rdef.isChartDrillDownContainsName(ff.getFieldName()+"_xaxis")?" selected":""%>> X Axis </option>
					   					<option value="<%= ff.getFieldName() %>_yaxis" <%= rdef.isChartDrillDownContainsName(ff.getFieldName()+"_yaxis")?" selected":"" %>> Y Axis </option>
					   					<option value="<%= ff.getFieldName() %>_series" <%= rdef.isChartDrillDownContainsName(ff.getFieldName()+"_series")?" selected":"" %>> Series </option>
					   				</select>
					   		  </font>
					   		  </td>
					   		  </tr>		  
								<% }
					   	   }
				  }
           }
				  %>
						
								
	</div>	

	</td>
	</tr>

	
<%	}	// else (numColsCount==0)
%>
</table>
<br>

    <script language="javascript">
      refreshWizard();
    </script>
    
<script language="JavaScript">
<!--
<%	if(numColsCount>0) { %>
function checkValueColNew() {
	if(document.forma.valueColNew.selectedIndex==0)
		return false;
	
	var newColId = "";
	newColId = document.forma.valueColNew.options[document.forma.valueColNew.selectedIndex].value;
<%		for(int idx=1; idx<Math.max(chartValueCols.size(), 1)+1; idx++) { %>
	if(newColId==document.forma.valueCol<%= idx %>.options[document.forma.valueCol<%= idx %>.selectedIndex].value) {
		alert("A column cannot be used as Chart Values Column more than once.");
		document.forma.valueColNew.focus();
		return false;
	}	// if
<%		}	// for
%>
	
	return true;
}   // checkValueColNew
<%	} %>


function showAlertForTimeSeries() {
   var selectBox = document.forma.chartType;
   var selectedString = selectBox.options[selectBox.selectedIndex].value;
   refreshWizard();
   if(selectedString == "<%=AppConstants.GT_PIE_MULTIPLE%>") {
		//var contentIframe = window.parent.document.getElementById("content_Iframe");
		//contentIframe.style.height = contentIframe.clientHeight + 120; 
	    document.getElementById("multiplePieChartOptions").style.display="block";
   }
   else {
	  document.getElementById("multiplePieChartOptions").style.display="none";
   } 
   if(selectedString == "<%=AppConstants.GT_TIME_SERIES%>"){
	   document.getElementById("animatedDiv").style.display="block";
     return confirm("WARNING: Please make sure that the Domain Axis contains only\nDATE type of values, otherwise it will throw error.\nPress Ok to continue, or Cancel to re-examine the selection.");
   } else if (selectedString == "<%=AppConstants.GT_BAR_3D%>"){
	   document.getElementById("animatedDiv").style.display="block";
   } else if (selectedString == "<%=AppConstants.GT_PIE%>"){
	   document.getElementById("animatedDiv").style.display="block";
   } else if (selectedString == "<%=AppConstants.GT_PIE_3D%>"){
	   document.getElementById("animatedDiv").style.display="block";
   } else if (selectedString == "<%=AppConstants.GT_SCATTER%>"){
	   document.getElementById("animatedDiv").style.display="block";
   } else 
	   document.getElementById("animatedDiv").style.display="none";
   return true;
}
function dataValidate() {
	    if(document.forma.maxLabelsInDomainAxis!="") {
			var iVal = 0;
			iVal = parseInt(document.forma.maxLabelsInDomainAxis.value);
			if(isNaN(iVal)||iVal>100||iVal<=0) {
				alert("Maximum Labels in Domain Axis must be a valid integer between 1 and 100 (or blank).\nPlease correct.");
				document.forma.maxLabelsInDomainAxis.select();
				document.forma.maxLabelsInDomainAxis.focus();
				return;
			}	// if
			document.forma.maxLabelsInDomainAxis.value = ""+iVal;
	    }
	    
		if(document.forma.chartWidth.value!="") {
			var iVal = 0;
			iVal = parseInt(document.forma.chartWidth.value);
			if(isNaN(iVal)||iVal<100||iVal>1600) {
				alert("Chart Width must be a valid integer between 100 and 1600 (or blank).\nPlease correct the Chart Width.");
				document.forma.chartWidth.select();
				document.forma.chartWidth.focus();
				return;
			}	// if
			document.forma.chartWidth.value = ""+iVal;
		}	// if
	if(document.forma.chartHeight.value!="") {
		var iVal = 0;
		iVal = parseInt(document.forma.chartHeight.value);
		if(isNaN(iVal)||iVal<100||iVal>1600) {
			alert("Chart Height must be a valid integer between 100 and 1600 (or blank).\nPlease correct the Chart Height.");
			document.forma.chartHeight.select();
			document.forma.chartHeight.focus();
			return;
		}	// if
		document.forma.chartHeight.value = ""+iVal;
	}	// if
<%	if(chartValueCols.size()>1) 
		for(int ci=1; ci<chartValueCols.size(); ci++) { 
			for(int idx=ci+1; idx<chartValueCols.size()+1; idx++) { %>
	if(document.forma.valueCol<%= ci %>.options[document.forma.valueCol<%= ci %>.selectedIndex].value==document.forma.valueCol<%= idx %>.options[document.forma.valueCol<%= idx %>.selectedIndex].value) {
		alert("A column cannot be used as Chart Values Column more than once.");
		document.forma.valueCol<%= idx %>.focus();
		return false;
	}	// if
<%		}	// for
	}
	if(isSQLBased) { %>
	   var selectBox = document.forma.chartType;
	   var selectedString = selectBox.options[selectBox.selectedIndex].value;
		if(document.forma.chartType.selectedIndex>0) {
			   if(selectedString == "<%=AppConstants.GT_SCATTER%>" || selectedString == "<%=AppConstants.GT_REGRESSION%>") {
					if(document.forma.chartSeries.selectedIndex<=0) {
						alert (" Please choose category for this Chart Type." );
						return false;
					}
					if (selectedString == "<%=AppConstants.GT_REGRESSION%>") {
			             if(document.forma && document.forma.LinearRegressionType) {
			                 document.forma.regressionType.value = (document.forma.LinearRegressionType.checked?"Y":"N")+(document.forma.ExpRegressionType.checked?"Y":"N");
			                 if (!checkNonNegativeFloat(document.forma.regressionPointCustomization.value)) {
				                 alert("Please enter positive number for Customizing regression Point");
				                 return false;
			                 }
			                 
			             }
					}
			   }

				/*	if (selectedString == "<%=AppConstants.GT_STACKED_VERT_BAR%>" || selectedString == "<%=AppConstants.GT_STACKED_HORIZ_BAR%>" || selectedString == "<%=AppConstants.GT_STACKED_VERT_BAR_LINES%>"
					    || selectedString == "<%=AppConstants.GT_STACKED_HORIZ_BAR_LINES%>") {
						   var selectSeries = document.forma.chartSeries;
						   var selectedSeriesString = selectSeries.options[selectSeries.selectedIndex].value;
						   if(selectedSeriesString == -1) {
							   alert("Please select category for Stacked Chart. This is Mandatory.");
							   return false;
						   }
					    
					} */
					
		}

		if (!checkNonNegativeFloat(document.forma.yAxisLowerLimit.value)) {
	        alert("Please enter positive number for Range Axis (Minimum Range) ");
	        return false;
		}

		if (!checkNonNegativeFloat(document.forma.yAxisUpperLimit.value)) {
	        alert("Please enter positive number for Range Axis (Maximum Range) ");
	        return false;
		}
		
		if(document.forma.chartType.selectedIndex>0)
			return confirm("WARNING: Please make sure that the Values column contains only\nnumeric values, otherwise your chart will not be properly displayed.\nPress Ok to continue, or Cancel to re-examine the selection.");
	<%  } %>

	
	
	return true;
}   // dataValidate
//-->
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
