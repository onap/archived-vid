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
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.DataColumnType" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.AppConstants" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportDefinition" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.AppUtils" %>
<%@ page import="org.openecomp.portalsdk.analytics.controller.WizardSequence" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.Globals" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.Utils" %>
<%@ page import="java.util.Vector" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.ReportLoader" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.IdNameValue" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.FormFieldType" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.ReportWrapper" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.DataCache" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.FormField" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.Log" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportLogEntry" %>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();
	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED); 
   	
%>

<%	String errorMsg = null;
	Vector reportLogEntries = null; 
	try {
		reportLogEntries = ReportLoader.loadReportLogEntries(reportID);
	} catch(Exception e) { 
		Log.write("ERROR [wizard_log.jsp] Unable to load report log entries. Exception: "+e.getMessage());
		errorMsg = "<b>ERROR: </b>Unable to load report log entries from the database <!--Exception: "+e.getMessage()+"-->";
	} %>

<table class="mTAB" width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=6 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="31%"><b class=rtableheader>Date/Time</b></td>
		<td align="center" valign="Middle" width="31%"><b class=rtableheader>User Name</b></td>
		<td align="center" valign="Middle" width="31%"><b class=rtableheader>Action</b></td>
	    <td align="center" valign="Middle" width="31%"><b class=rtableheader>Execution Time</b></td>				
	    <td align="center" valign="Middle" width="31%"><b class=rtableheader>Run</b></td>	    
	</tr>
<%	int iCount = 0;
	if(reportLogEntries!=null)
		for(Iterator iter=reportLogEntries.iterator(); iter.hasNext(); iCount++) { 
			ReportLogEntry logEntry = (ReportLogEntry) iter.next(); %>
	<tr <%= (iCount%2==0)?" class=rowalt1":" class=rowalt2" %>>
		<td align="center" height="30" class="tdborder"><font class=rtabletext><%= iCount+1 %></font></td>
		<td align="center" class="tdborder"><font class=rtabletext><%= logEntry.getLogTime() %></font></td>
		<td class="tdborder"><font class=rtabletext><%= logEntry.getUserName() %></font></td>
		<td class="tdborder"><font class=rtabletext><%= logEntry.getAction() %></font></td>
        <td class="tdborder"><font class=rtabletext><%= logEntry.getTimeTaken() %></font></td>				
        <td class="tdborder"><font class=rtabletext><%= logEntry.getRunIcon() %></font></td>        
        		
	</tr>
<%	}   // for
	if(errorMsg!=null) { %>
	<tr class=rbg6>
		<td colspan=6 align="center" height="30"><font class=rerrortextsm><%= errorMsg %></font></td>
	</tr>
<%	} else if(iCount==0) { %>
	<tr class=rbg2>
		<td colspan=6 align="center" height="30"><font class=rtabletext>No log entries found</font></td>
	</tr>
<%	} else { %>
	<tr class=rbg1>
		<td colspan=6 align="left" valign="Middle" height="30">
			<input type="Submit" class=button value="Clear Log" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_USER %>';">
		</td>
	</tr>
<%	}	// if 
%>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
	return true;
}   // dataValidate
//-->
</script>

