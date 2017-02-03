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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
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
<%@ page import="java.text.SimpleDateFormat"%>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();
	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	//boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED); 
	String dependsOnHelp = "Custom SQL can be defined";
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED_DATAMIN); 
	
	String classifiers = nvl(rdef.getClassifier()); 
	System.out.println(classifiers);
	List reportCols     = rdef.getAllColumns();
	
 	String dateAttrColId = null;
 	DataColumnType dct = null;
 	
	HashMap<String, DataColumnType> unusedNumColsMap = new HashMap<String,DataColumnType>();
	int  numColsCount = 0;
	Iterator iter = null;
	
	for(iter=reportCols.iterator(); iter.hasNext(); ) { 
		dct = (DataColumnType) iter.next();
		
		if(nvl(dct.getDataMiningCol()).equals(AppConstants.DM_DATE_ATTR))
			dateAttrColId = dct.getColId();
		
		if(isSQLBased||nvl(dct.getColType()).equals(AppConstants.CT_NUMBER)) {
			numColsCount++;
			if(nvl(dct.getDataMiningCol()).length()==0)
				unusedNumColsMap.put(dct.getColId(), dct);
		}	// if
	}
	
    String[] fmt   = { "Default", "MM/DD/YYYY", "MM/YYYY", "DD-MON-YYYY", "Month DD, YYYY", "Month, YYYY" };
%>

<table width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	
	<tr>
		<td class=rbg2 align="right" width="10%" height="30"><font class=rtabletext>Classifiers: </font></td>
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="classifiers">
				<option value="" <%= classifiers.equals("")?" selected":"" %>>--- Choose Classifiers ---
				<option value="<%= AppConstants.DM_SVM_CLASSIFIER%>"   <%= classifiers.equals(AppConstants.DM_SVM_CLASSIFIER)?" selected":"" %>>SMOreg
				<option value="<%= AppConstants.DM_GAUSSIAN_CLASSIFIER%>"   <%= classifiers.equals(AppConstants.DM_GAUSSIAN_CLASSIFIER)?" selected":"" %>>Gaussian Process
			</select></font>
	    </td>
	</tr>
	
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Date Attribute: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<select name="timeAttribute">
			<%	for(iter=reportCols.iterator(); iter.hasNext(); ) { 
					dct = (DataColumnType) iter.next(); %>
				<option value="<%= dct.getColId() %>"<%= nvl(dateAttrColId /*, firstColId*/).equals(dct.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
			<%	} %>
			</select></font></td>
	</tr>
	
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Date Format: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<select name="timeFormat">
			<%	for(int i=0; i<fmt.length; i++) { %>
				<option value="<%= fmt[i].equals("N/A")?"":fmt[i] %>"<%= (nvl(rdef.getForecastingTimeFormat()).equals(fmt[i].toUpperCase()))?" selected":"" %>><%= fmt[i] %>
			<%  } %>
			</select>
         </font></td>
	</tr>
	
	
		<tr>
		<td class=rbg2 align="center" colspan="2" height="30"><font class=rtabletext>Forecasting Column:</font></td> 
		</tr>
		
		<%	for(iter=reportCols.iterator(); iter.hasNext(); ) { 
				dct = (DataColumnType) iter.next();
				if(!nvl(dct.getDataMiningCol()).equals(AppConstants.DM_DATE_ATTR) ) {
				
		%>
		<tr>
		<td class=rbg3 align="right" width="50%"><font class=rtabletext>
		     
		          <% if (! unusedNumColsMap.containsKey(dct.getColId()) ) { %>
		              <% if (!dct.getDataMiningCol().equals(AppConstants.DM_DATE_ATTR)) %>
					<input name="forecastCol" type="checkbox" value="<%=dct.getColId() %>" checked></font></td><td class=rbg3 align="left"> <%=dct.getColName() %> </td>
				  <% } else if(isSQLBased||nvl(dct.getColType()).equals(AppConstants.CT_NUMBER)) { %>
				  	<input name="forecastCol" type="checkbox" value="<%=dct.getColId() %>"></font></td> <td class=rbg3 align="left"> <%=dct.getColName() %> </td>
				  <% } %>	
					 	   
			 		 
			
			</tr>
			 <% } %>
			<% }  %>

	 <tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Forecasting Period: </font>
		</td> 
		<td class=rbg3 align="left">
			<select name="forecastingPeriod" onKeyDown="fnKeyDownHandler(this, event);" onKeyUp="fnKeyUpHandler_A(this, event); return false;" onKeyPress = "return fnKeyPressHandler_A(this, event);"  onChange="fnChangeHandler_A(this, event);">
				<option value="-1" style="COLOR:#ff0000;BACKGROUND-COLOR:#ffff00;">Custom</option> <!-- This is the Editable Option -->
				<option value="2"<%=  (rdef.getForecastingPeriod()==2 )?" selected":"" %>>2
				<option value="4"<%=  (rdef.getForecastingPeriod()==4 )?" selected":"" %>>4
				<option value="6"<%=  (rdef.getForecastingPeriod()==6 )?" selected":"" %>>6
				<option value="8"<%=  (rdef.getForecastingPeriod()==8)?" selected":"" %>>8
				<option value="10"<%= (rdef.getForecastingPeriod()==10)?" selected":"" %>>10
				<option value="20"<%= (rdef.getForecastingPeriod()==20)?" selected":"" %>>20
				<option value="25"<%= (rdef.getForecastingPeriod()==25)?" selected":"" %>>25
				<option value="30"<%= (rdef.getForecastingPeriod()==30)?" selected":"" %>>30
			</select>
		</td>
	</tr>						

</table>

<script language="JavaScript">
	function dataValidate() {
		return true;
	}   // dataValidate
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
    private boolean isNumber(String value) { // As per Raptor def, like

        // -$3,270.56
        value = value.trim();
    	if(value.length()>2) return false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!(Character.isDigit(c) || c == '.' || c == '-' || c == '+' || c == ','
                    || c == '$' || c == '%'))
                return false;
        } // for

        return true;
    } // isNumber
%>
