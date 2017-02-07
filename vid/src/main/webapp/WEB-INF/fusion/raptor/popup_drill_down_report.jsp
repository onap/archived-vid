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
<%@ page import="java.util.*" %>

<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%	ReportDefinition rdef = (ReportDefinition) request.getSession().getAttribute(AppConstants.SI_REPORT_DEFINITION); 
	List reportCols  = rdef.getAllColumns();
	List rFormFields = null;
	if(rdef.getFormFieldList()!=null&&rdef.getFormFieldList().getFormField().size()>0)
		rFormFields = rdef.getFormFieldList().getFormField();
	
	ReportFormFields ddReportFormFields = (ReportFormFields) request.getAttribute(AppConstants.RI_FORM_FIELDS); 
	
	String drillDownSuppress = AppUtils.getRequestNvlValue(request, "drillDownSuppress");
	String drillDownParams   = AppUtils.getRequestNvlValue(request, "drillDownParams");
	String drillDownRequest   = AppUtils.getRequestNvlValue(request, "drillDownRequest");
	
	Hashtable paramDefinitions = new Hashtable();
	StringTokenizer st = new StringTokenizer(drillDownParams, "&");
	//Added for passing request parameters in Drill Down
    String[] reqParameters = Globals.getRequestParams().split(",");	
    int icnt=0;
    //
	while(st.hasMoreTokens()) {
		String param = st.nextToken();
		DrillDownParamDef paramDef = new DrillDownParamDef(param);
		if(paramDef.getFieldName().length()>0)
			paramDefinitions.put(paramDef.getFieldName(), paramDef);
	}	// while
%>

<html>
<head>
	<title>Drill-down Parameters Configuration</title>
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">

<script language="JavaScript">
<!--
	var bCloseWindow = false;

	function setParams(newParams) {
		var suppressValue = document.dataform.v_suppress.value;
		var showInPopup;
		if(document.dataform.showInPopup.checked)
			showInPopup = "true";
		window.opener.document.forma.drillDownPopUp.value = showInPopup;
		window.opener.setDrillDownValuesSuppress(newParams, suppressValue);
		window.close();
	}   // setParams

	function resetDrillDown() {
		window.opener.document.forma.drillDownCtl.selectedIndex = 0;
		window.opener.document.forma.drillDownURL.value         = "";
		window.opener.document.forma.drillDownParams.value      = "";
		window.opener.document.forma.drillDownSuppress.value    = "";
		window.opener.document.forma.drillDownPopUp.value    	= "";
	}   // setParams


//-->
</script>

</head>
<body onLoad="if(bCloseWindow) setParams('');">

<form name="dataform" onSubmit="return false;">
<table class="mTAB" width="94%" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan="2">
			<b class=rtableheader>DRILL-DOWN PARAMETERS CONFIGURATION</b>
		</td>
	</tr>

<%	if(ddReportFormFields!=null)
		for(ddReportFormFields.resetNext(); ddReportFormFields.hasNext(); ) {
			FormField ff = ddReportFormFields.getNext(); 
			if(!ff.getFieldType().equals(FormField.FFT_BLANK)) {
			
			DrillDownParamDef paramDef = (DrillDownParamDef) paramDefinitions.get(ff.getFieldName());
			if(paramDef==null)
				paramDef = new DrillDownParamDef(""); %>
             
            <% if (ff!=null && (ff.getValidationType().equals(FormField.VT_TIMESTAMP_HR) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_MIN) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_SEC)) ) {
            %>
	<tr>
		<td class=rbg1 colspan="2" height="30" align="left"><b class=rtableheader>
			&nbsp;<%= ff.getFieldDisplayName() %>
			<input type="hidden" name="paramName" value="<%= ff.getFieldName() %>"></b></td> 
	</tr>
	<tr>
		<td class=rbg2 align="left" width="33%" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="None"<%= paramDef.getValType().equals("0")?" checked":"" %>>No value </font>
		</td>
		<td class=rbg3 align="left" width="67%" nowrap>
			<font class=rtabletext>Accept default</font>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="Fixed"<%= paramDef.getValType().equals("1")?" checked":"" %>>Fixed value </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<input type="text" size="30" maxlength="100" name="v_<%= ff.getFieldName() %>" value="<%= java.net.URLDecoder.decode(paramDef.getValValue(),"UTF8") %>" onChange="document.dataform.r_<%= ff.getFieldName() %>[1].click();"></font>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="Column"<%= paramDef.getValType().equals("2")?" checked":"" %>>Value of column </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<select name="c_<%= ff.getFieldName() %>" onChange="document.dataform.r_<%= ff.getFieldName() %>[2].click();">
			    <option value="this">--- Current drill-down column ---
<% 			for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
				DataColumnType dct = (DataColumnType) iter.next(); 
				//if(dct.isVisible()) { 
				%>
			    <option value="<%= dct.getColId() %>"<%= paramDef.getValColId().equals(dct.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
<%				//} 
%>
<% 			} %>
			</select></font>
		</td>
	</tr>
<%			if(rFormFields!=null) { %>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="FormField"<%= paramDef.getValType().equals("3")?" checked":"" %>>Value of form field </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<select name="f_<%= ff.getFieldName() %>" onChange="document.dataform.r_<%= ff.getFieldName() %>[3].click();">
<% 				for(Iterator iter=rFormFields.iterator(); iter.hasNext(); ) { 
					FormFieldType fft = (FormFieldType) iter.next(); %>
			    <option value="<%= fft.getFieldId() %>"<%= paramDef.getValFieldId().equals(fft.getFieldId())?" selected":"" %>><%= fft.getFieldName() %>
<% 				} %>
			</select></font>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="ColFormSet"<%= paramDef.getValType().equals("4")?" checked":"" %>>Value set </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			Pass the value of the selected column if not empty,<br>
			otherwise pass the value of the selected form field</font>
		</td>
	</tr>
<%					}	// if
					
%>
<!--  END -->
				<%
				paramDef = (DrillDownParamDef) paramDefinitions.get(ff.getFieldName()+"_Hr");
				if(paramDef==null)
					paramDef = new DrillDownParamDef("");
				%>
            	<tr>
        		<td class=rbg1 colspan="2" height="30" align="left"><b class=rtableheader>
        			&nbsp;<%= ff.getFieldDisplayName() %> (Hour)
        			<input type="hidden" name="paramName" value="<%= ff.getFieldName() %>_Hr"></b></td> 
        		</tr>
				<tr>
					<td class=rbg2 align="left" width="33%" height="30" nowrap>
						<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="r_<%= ff.getFieldName() %>_Hr" value="None"<%= paramDef.getValType().equals("0")?" checked":"" %>>No value </font>
					</td>
					<td class=rbg3 align="left" width="67%" nowrap>
						<font class=rtabletext>Accept default</font>
					</td>
				</tr>
				<tr>
					<td class=rbg2 align="left" height="30" nowrap>
						<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="r_<%= ff.getFieldName() %>_Hr" value="Column" <%= paramDef.getValType().equals("2")?" checked":"" %> >Value of column </font>
					</td>
					<td class=rbg3 align="left" nowrap>
						<font class=rtabletext>
							<select name="c_<%= ff.getFieldName() %>_Hr" onChange="document.dataform.r_<%= ff.getFieldName() %>_Hr[1].click();">
			    				<option value="this">--- Current drill-down column ---
					<% 			for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
									DataColumnType dct = (DataColumnType) iter.next(); 
									//if(dct.isVisible()) { 
					%>
			    					<option value="<%= dct.getColId() %>"<%= paramDef.getValColId().equals(dct.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
		<%				//} 
		%>
<% 								} %>
							</select>
					</font>
					</td>
			</tr>
<%
 				  if (ff.getValidationType().equals(FormField.VT_TIMESTAMP_MIN) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_SEC)) {
%>
				<%
				paramDef = (DrillDownParamDef) paramDefinitions.get(ff.getFieldName()+"_Min");
				if(paramDef==null)
					paramDef = new DrillDownParamDef("");
				%>
            	<tr>
        		<td class=rbg1 colspan="2" height="30" align="left"><b class=rtableheader>
        			&nbsp;<%= ff.getFieldDisplayName() %> (Minutes)
        			<input type="hidden" name="paramName" value="<%= ff.getFieldName() %>_Min"></b></td> 
        		</tr>
				<tr>
					<td class=rbg2 align="left" width="33%" height="30" nowrap>
						<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="r_<%= ff.getFieldName() %>_Min" value="None"<%= paramDef.getValType().equals("0")?" checked":"" %>>No value </font>
					</td>
					<td class=rbg3 align="left" width="67%" nowrap>
						<font class=rtabletext>Accept default</font>
					</td>
				</tr>
				<tr>
					<td class=rbg2 align="left" height="30" nowrap>
						<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="r_<%= ff.getFieldName() %>_Min" value="Column" <%= paramDef.getValType().equals("2")?" checked":"" %>>Value of column </font>
					</td>
					<td class=rbg3 align="left" nowrap>
						<font class=rtabletext>
							<select name="c_<%= ff.getFieldName() %>_Min" onChange="document.dataform.r_<%= ff.getFieldName() %>_Min[1].click();">
			    				<option value="this">--- Current drill-down column ---
					<% 			for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
									DataColumnType dct = (DataColumnType) iter.next(); 
									//if(dct.isVisible()) { 
					%>
			    					<option value="<%= dct.getColId() %>"<%= paramDef.getValColId().equals(dct.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
		<%				//} 
		%>
<% 								} %>
							</select>
					</font>
					</td>
			</tr>
<% 					  
 				  }
				  if(ff.getValidationType().equals(FormField.VT_TIMESTAMP_SEC)) {
%>
				<%
				paramDef = (DrillDownParamDef) paramDefinitions.get(ff.getFieldName()+"_Sec");
				if(paramDef==null)
					paramDef = new DrillDownParamDef("");
				%>
            	<tr>
        		<td class=rbg1 colspan="2" height="30" align="left"><b class=rtableheader>
        			&nbsp;<%= ff.getFieldDisplayName() %> (Seconds)
        			<input type="hidden" name="paramName" value="<%= ff.getFieldName() %>_Sec"></b></td> 
        		</tr>
				<tr>
					<td class=rbg2 align="left" width="33%" height="30" nowrap>
						<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="r_<%= ff.getFieldName() %>_Sec" value="None"<%= paramDef.getValType().equals("0")?" checked":"" %>>No value </font>
					</td>
					<td class=rbg3 align="left" width="67%" nowrap>
						<font class=rtabletext>Accept default</font>
					</td>
				</tr>
				<tr>
					<td class=rbg2 align="left" height="30" nowrap>
						<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="r_<%= ff.getFieldName() %>_Sec" value="Column" <%= paramDef.getValType().equals("2")?" checked":"" %>>Value of column </font>
					</td>
					<td class=rbg3 align="left" nowrap>
						<font class=rtabletext>
							<select name="c_<%= ff.getFieldName() %>_Sec" onChange="document.dataform.r_<%= ff.getFieldName() %>_Sec[1].click();">
			    				<option value="this">--- Current drill-down column ---
					<% 			for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
									DataColumnType dct = (DataColumnType) iter.next(); 
									//if(dct.isVisible()) { 
					%>
			    					<option value="<%= dct.getColId() %>"<%= paramDef.getValColId().equals(dct.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
		<%				//} 
		%>
<% 								} %>
							</select>
					</font>
					</td>
			</tr>
<% 					  
 		
				  }
				  	                  
               } else { 	
%> 
	<tr>
		<td class=rbg1 colspan="2" height="30" align="left"><b class=rtableheader>
			&nbsp;<%= ff.getFieldDisplayName() %>
			<input type="hidden" name="paramName" value="<%= ff.getFieldName() %>"></b></td> 
	</tr>
	<tr>
		<td class=rbg2 align="left" width="33%" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="None"<%= paramDef.getValType().equals("0")?" checked":"" %>>No value </font>
		</td>
		<td class=rbg3 align="left" width="67%" nowrap>
			<font class=rtabletext>Accept default</font>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="Fixed"<%= paramDef.getValType().equals("1")?" checked":"" %>>Fixed value </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<input type="text" size="30" maxlength="100" name="v_<%= ff.getFieldName() %>" value="<%= java.net.URLDecoder.decode(paramDef.getValValue(),"UTF8") %>" onChange="document.dataform.r_<%= ff.getFieldName() %>[1].click();"></font>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="Column"<%= paramDef.getValType().equals("2")?" checked":"" %>>Value of column </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<select name="c_<%= ff.getFieldName() %>" onChange="document.dataform.r_<%= ff.getFieldName() %>[2].click();">
			    <option value="this">--- Current drill-down column ---
<% 			for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
				DataColumnType dct = (DataColumnType) iter.next(); 
				//if(dct.isVisible()) { 
				%>
			    <option value="<%= dct.getColId() %>"<%= paramDef.getValColId().equals(dct.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
<%				//} 
%>
<% 			} %>
			</select></font>
		</td>
	</tr>
<%			if(rFormFields!=null) { %>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="FormField"<%= paramDef.getValType().equals("3")?" checked":"" %>>Value of form field </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<select name="f_<%= ff.getFieldName() %>" onChange="document.dataform.r_<%= ff.getFieldName() %>[3].click();">
<% 				for(Iterator iter=rFormFields.iterator(); iter.hasNext(); ) { 
					FormFieldType fft = (FormFieldType) iter.next(); %>
			    <option value="<%= fft.getFieldId() %>"<%= paramDef.getValFieldId().equals(fft.getFieldId())?" selected":"" %>><%= fft.getFieldName() %>
<% 				} %>
			</select></font>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="r_<%= ff.getFieldName() %>" value="ColFormSet"<%= paramDef.getValType().equals("4")?" checked":"" %>>Value set </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			Pass the value of the selected column if not empty,<br>
			otherwise pass the value of the selected form field</font>
		</td>
	</tr>
<%			}	// if
               } // else
			} // if BLANK 	   
		}   // for 
%>

<script language="JavaScript">
<!--
<% if(ddReportFormFields==null||ddReportFormFields.getFieldCount()==0) { %>
	bCloseWindow = true;
<% } %>
	function replaceSC(strValue) {
		var newValue = "";
		
		for(var i=0; i<strValue.length; i++) {
			var ch = strValue.charAt(i);
			
			if(ch=='@')
				newValue += "%40";
			else if(ch=='+')
				newValue += "%2B";
			else if(ch=='/')
				newValue += "%2F";
			else
				newValue += ch;
		}	// for
		
		return newValue;
	}	// replaceSC

	function collectValues() {
		var newParams = "";
          newParams += setCheckBoxValues();
<%	if(ddReportFormFields!=null) {
		for(ddReportFormFields.resetNext(); ddReportFormFields.hasNext(); ) {
			FormField ff = ddReportFormFields.getNext(); 
			if(!ff.getFieldType().equals(FormField.FFT_BLANK)) {
			String fieldName = ff.getFieldName(); 
			
			if (ff!=null && !(ff.getValidationType().equals(FormField.VT_TIMESTAMP_HR) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_MIN) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_SEC)) ) {
%>				
		if(document.dataform.r_<%= fieldName %>[1].checked||document.dataform.r_<%= fieldName %>[2].checked<%= (rFormFields!=null)?"||document.dataform.r_"+fieldName+"[3].checked||document.dataform.r_"+fieldName+"[4].checked":"" %>) {
			if(newParams!="")
				newParams += "&";
			newParams += "<%= fieldName %>=";
			if(document.dataform.r_<%= fieldName %>[1].checked) {
				newParams += replaceSC(escape(document.dataform.v_<%= fieldName %>.value));
			} else if(document.dataform.r_<%= fieldName %>[2].checked) {
				newParams += "["+escape(document.dataform.c_<%= fieldName %>.options[document.dataform.c_<%= fieldName %>.selectedIndex].value)+"]";
			<% if(rFormFields!=null) { %>
			} else if(document.dataform.r_<%= fieldName %>[3].checked) {
				newParams += "[!"+escape(document.dataform.f_<%= fieldName %>.options[document.dataform.f_<%= fieldName %>.selectedIndex].value)+"]";
			} else if(document.dataform.r_<%= fieldName %>[4].checked) {
				newParams += "["+escape(document.dataform.c_<%= fieldName %>.options[document.dataform.c_<%= fieldName %>.selectedIndex].value)+"!"+escape(document.dataform.f_<%= fieldName %>.options[document.dataform.f_<%= fieldName %>.selectedIndex].value)+"]";
			<% } %>
			}
		}   // if
<%	
 } //if non-timestamp
			if (ff!=null && (ff.getValidationType().equals(FormField.VT_TIMESTAMP_HR) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_MIN) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_SEC)) ) {
 
			if(ff.getValidationType().equals(FormField.VT_TIMESTAMP_HR) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_MIN) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_SEC)) {
%>
		if(document.dataform.r_<%= fieldName %>[1].checked||document.dataform.r_<%= fieldName %>[2].checked<%= (rFormFields!=null)?"||document.dataform.r_"+fieldName+"[3].checked||document.dataform.r_"+fieldName+"[4].checked":"" %>) {
			if(newParams!="")
				newParams += "&";
			newParams += "<%= fieldName %>=";
			if(document.dataform.r_<%= fieldName %>[1].checked) {
				newParams += replaceSC(escape(document.dataform.v_<%= fieldName %>.value));
			} else if(document.dataform.r_<%= fieldName %>[2].checked) {
				newParams += "["+escape(document.dataform.c_<%= fieldName %>.options[document.dataform.c_<%= fieldName %>.selectedIndex].value)+"]";
			<% if(rFormFields!=null) { %>
			} else if(document.dataform.r_<%= fieldName %>[3].checked) {
				newParams += "[!"+escape(document.dataform.f_<%= fieldName %>.options[document.dataform.f_<%= fieldName %>.selectedIndex].value)+"]";
			} else if(document.dataform.r_<%= fieldName %>[4].checked) {
				newParams += "["+escape(document.dataform.c_<%= fieldName %>.options[document.dataform.c_<%= fieldName %>.selectedIndex].value)+"!"+escape(document.dataform.f_<%= fieldName %>.options[document.dataform.f_<%= fieldName %>.selectedIndex].value)+"]";
			<% } %>
			}
		}   // if				

				if(document.dataform.r_<%= fieldName %>_Hr[1].checked) {
					if(newParams!="")
						newParams += "&";
					newParams += "<%= fieldName %>_Hr=";
					if(document.dataform.r_<%= fieldName %>_Hr[1].checked) {
						newParams += "["+escape(document.dataform.c_<%= fieldName %>_Hr.options[document.dataform.c_<%= fieldName %>_Hr.selectedIndex].value)+"]";
					}
				}   // if				
<%
			}//hour
%>
<%				
			if(ff.getValidationType().equals(FormField.VT_TIMESTAMP_MIN) || ff.getValidationType().equals(FormField.VT_TIMESTAMP_SEC)) {
%>
				if(document.dataform.r_<%= fieldName %>_Min[1].checked) {
					if(newParams!="")
						newParams += "&";
					newParams += "<%= fieldName %>_Min=";
					if(document.dataform.r_<%= fieldName %>_Min[1].checked) {
						newParams += "["+escape(document.dataform.c_<%= fieldName %>_Min.options[document.dataform.c_<%= fieldName %>_Min.selectedIndex].value)+"]";
     }
			}   // if
			<%
			} // min
			if(ff.getValidationType().equals(FormField.VT_TIMESTAMP_SEC)) {
%>
				if(document.dataform.r_<%= fieldName %>_Sec[1].checked) {
					if(newParams!="")
						newParams += "&";
					newParams += "<%= fieldName %>_Sec=";
					if(document.dataform.r_<%= fieldName %>_Sec[1].checked) {
						newParams += "["+escape(document.dataform.c_<%= fieldName %>_Sec.options[document.dataform.c_<%= fieldName %>_Sec.selectedIndex].value)+"]";
				}
			}   // if							
<%
		} // sec
		}
	} // BLANK
		}	// for 
     }
%>
        //alert(newParams);
 		setParams(newParams);
	}   // collectValues

	function setCheckBoxValues() {
		var newValues ="";
<%
        icnt=0;
        if(reqParameters.length>0) {
%>
         if(document.dataform.requestParam) {  
          for (i=0;i<document.dataform.requestParam.length;i++) {
            <% icnt++;%>          
            if(document.dataform.requestParam[i].checked) {
              newValues += escape("<%=reqParameters[icnt-1]%>")+"="+"[#"+escape("<%=reqParameters[icnt-1]%>")+"]";
            }
         }
         }
<% } %>
      return newValues;
	} 

//-->
</script>

	<tr>
		<td colspan="2">&nbsp;</td> 
	</tr>
	<tr>
		<td class=rbg1 colspan="2" height="30" align="left"><b class=rtableheader>
			&nbsp;Parameter values not to be passed to the drill-down report</b></td> 
	</tr>
	<tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Suppress values </font>
		</td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<input type="text" size="30" maxlength="100" name="v_suppress" value="<%= drillDownSuppress %>">
			<br>separate by | if multiple values</font>
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td> 
	</tr>
	<% if(!Globals.getPassRequestParamInDrilldown() && (!(reqParameters.length==1 && reqParameters[0].length()<=0))) { 
		%>
	<tr>
		<td class=rbg1 colspan="2" height="30" align="left"><b class=rtableheader>
			&nbsp;Request Parameter values to be passed to the drill-down report</b></td> 
	</tr>
	<!-- <tr>
		<td class=rbg2 align="left" height="30" nowrap>
			<font class=rtabletext>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Request values </font>
		</td>
	 </tr>
	 -->
		<%
        icnt=0;
 
        for (int i = 0; i < reqParameters.length; i++) { 
          icnt++;
                       
        %>
        <tr<%= (icnt%2==0)?" class=rowalt1":" class=rowalt2" %>>
		<!--<td align="center" height="30"><font class=rtabletext><%= icnt %></font></td>-->
		<td><font class=rtabletext><%= reqParameters[i]%></font></td>
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
          <input type="checkbox" name="requestParam" <%= ((drillDownRequest!=null)&&(drillDownRequest.indexOf(reqParameters[i])!=-1))?"checked":""%>>
		</td>
		</tr>
		<%
		} //for
		%>
		 <input type="hidden" name="requestParam">
		<%
		} // if requestParam 
		%>
        <tr> 
			<td class=rbg3 align="left" nowrap><font class=rtabletext>
          		 Show Drilled Down Report In Popup Window: <input type="checkbox" name="showInPopup">
			</td>
		</tr>

	<tr>
		<td colspan="3" align="center"><input type="Button" class=button value="Complete" onClick="collectValues()"></td>
	</tr>
</table>
</form>
	<br><br>
 <script>
    if(window.opener.document.forma.drillDownPopUp.value == 'true')
    	document.dataform.showInPopup.checked = true;
        
 </script>
</body>
</html>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

