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
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.ColFilterType" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.FormFieldType" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.FormField" %>
<%@ page errorPage="error_page.jsp" %>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();

	boolean isEdit  = curSubStep.equals(AppConstants.WSS_EDIT);
	
	String filterId = AppUtils.getRequestNvlValue(request, AppConstants.RI_DETAIL_ID);
	String colId    = isEdit?filterId.substring(0, filterId.indexOf('|')):null;
	int filterPos = -1;
	if(isEdit)
		try {
			filterPos = Integer.parseInt(filterId.substring(colId.length()+1));
		} catch(NumberFormatException e) {}
	
	DataColumnType currColumn = isEdit?rdef.getColumnById(colId):null;
	ColFilterType  currFilter = isEdit?rdef.getFilterById(colId, filterPos):null;
	
	String arg = null; %>

<script language="JavaScript">
<!--
function showFormFieldPopup() {
	var w = window.open("", "filterPopup", "width=400,height=400,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
	
	w.document.writeln("<html><head>");
	w.document.writeln("<title>Form Fields</title>");
	w.document.writeln("<link rel=stylesheet type=text/css href='<%= AppUtils.getBaseFolderURL() %>css/raptor.css'>");
	w.document.writeln("<script language=JavaScript>");
	w.document.writeln("function setValue(newValue, fieldType) {");
	w.document.writeln("    window.opener.document.forma.argValue.value      = newValue;");
	w.document.writeln("    window.opener.document.forma.formFieldType.value = fieldType;");
	w.document.writeln("    window.close();");
	w.document.writeln("}   // setValue");
	w.document.writeln("</"+"script>");
	w.document.writeln("</head><body>");
	
	w.document.writeln("<table width=94% border=0 cellspacing=1 align=center>");
	w.document.writeln("	<tr class=rbg1>");
	w.document.writeln("		<td height=30>&nbsp;</td>");
	w.document.writeln("		<td><b class=rtableheader>Report Form Fields</b></td>");
	w.document.writeln("	</tr>");
<%	int iCnt = 0;
	if(rdef.getFormFieldList()!=null)
		for(Iterator iter=rdef.getFormFieldList().getFormField().iterator(); iter.hasNext(); iCnt++) { 
			FormFieldType fft = (FormFieldType) iter.next(); %>
	w.document.writeln("	<tr<%= (iCnt%2==0)?" class=rbg8":"" %>>");
	w.document.writeln("		<td align=center valign=middle height=30><font class=rtabletext><%= (iCnt+1) %></font></td>");
	w.document.writeln("		<td valign=middle>");
	w.document.writeln("			<b class=rtabletext><a href=\"javascript:setValue('[<%= fft.getFieldName() %>]', '<%= fft.getFieldType() %>')\"><%= fft.getFieldName() %></a></b>");
	w.document.writeln("		</td>");
	w.document.writeln("	</tr>");
<%			}	// for
	if(iCnt==0) { %>
	w.document.writeln("	<tr class=rbg3 height=30>");
	w.document.writeln("		<td colspan=2 align=center valign=middle><b class=rtabletext>There are no form fields defined</b></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr>");
	w.document.writeln("		<td colspan=2 align=center><br><input type=Submit class=Button value=Close onClick=\"window.close();\"></td>");
	w.document.writeln("	</tr>");
<%	} else { %>
	w.document.writeln("	<tr class=rbg1 height=30>");
	w.document.writeln("		<td>&nbsp;</td>");
	w.document.writeln("		<td>&nbsp;</td>");
	w.document.writeln("	</tr>");
<%	}	// if 
%>
	w.document.writeln("</table>");
	
	w.document.writeln("</body></html>");
	w.document.close();
}   // showFormFieldPopup

function showArgPopup() {
	var argType = document.forma.argType.options[document.forma.argType.selectedIndex].value;
	if(argType=="<%= AppConstants.AT_FORMULA %>") {
		alert("Please select Argument Type to be one of the following:\n - Simple Value\n - Another Column\n - List of Values\n - Run-time Form Field");
		document.forma.argType.focus();
		return;
	}
	
	if(argType=="<%= AppConstants.AT_FORM %>") {
		showFormFieldPopup();
		return;
	}
	
<% if(isEdit) { %>
	var colId = "<%= colId %>";
<% } else { %>
	var colId = document.forma.filterColId.options[document.forma.filterColId.selectedIndex].value;
<% } %>
	
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.filter."+((argType=="<%= AppConstants.AT_COLUMN %>")?"col":"data&<%= AppConstants.RI_ARG_TYPE %>="+argType+"&<%= AppConstants.RI_COLUMN_ID %>="+colId)+"&<%= AppConstants.RI_JS_TARGET_FIELD %>=document.forma.argValue&<%= AppConstants.RI_RESET_PARAMS %>=Y", "filterPopup", "width=440,height=400,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showArgPopup
//-->
</script>


<table width="100%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %> - <%= curSubStep %></b></td>
	</tr>
<% if((isEdit?filterPos:rdef.getAllFilters().size())==0) { %>
	<input type="hidden" name="filterJoin" value="AND">
<% } else { %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Join Condition: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<%  arg = (request.getParameter("filterJoin")!=null)?request.getParameter("filterJoin"):(isEdit?currFilter.getJoinCondition():""); %>
			<select name="filterJoin">
				<option value="AND"<%= arg.equals("AND")?" selected":"" %>>AND
				<option value="OR"<%=  arg.equals("OR") ?" selected":"" %>>OR
			</select></font></td>
	</tr>
<% } %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Opening Brackets: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<%  arg = (request.getParameter("openBrackets")!=null)?request.getParameter("openBrackets"):(isEdit?nvl(currFilter.getOpenBrackets()):""); %>
			<select name="openBrackets">
				<option value=""<%=      arg.equals("")     ?" selected":"" %>>
				<option value="("<%=     arg.equals("(")    ?" selected":"" %>>(
				<option value="(("<%=    arg.equals("((")   ?" selected":"" %>>((
				<option value="((("<%=   arg.equals("(((")  ?" selected":"" %>>(((
				<option value="(((("<%=  arg.equals("((((") ?" selected":"" %>>((((
				<option value="((((("<%= arg.equals("(((((")?" selected":"" %>>(((((
			</select></font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Filter By Column: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
		<% if(isEdit) { %>
			<%= currColumn.getDisplayName() %>
		    <input type="hidden" name="filterColId" value="<%= colId %>">
		    <input type="hidden" name="filterPos" value="<%= filterPos %>">
		<% } else { %>
			<select name="filterColId">
<%				int iCount = 0;
				List reportCols = rdef.getAllColumns();
				for(Iterator iter=reportCols.iterator(); iter.hasNext(); iCount++) { 
					DataColumnType dct = (DataColumnType) iter.next(); %>
				    <option value="<%= dct.getColId() %>"<%= ((request.getParameter("filterColId")==null)?(iCount==0):dct.getColId().equals(request.getParameter("filterColId")))?" selected":"" %>><%= dct.getDisplayName() %>
<%				}   // for 
%>
			</select>
		<% } %>
		</font></td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Expression: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<%  arg = (request.getParameter("filterExpr")!=null)?request.getParameter("filterExpr"):(isEdit?currFilter.getExpression():""); %>
			<select name="filterExpr">
				<option value="="<%=           arg.equals("=")          ?" selected":"" %>>=
				<option value="&lt;&gt;"<%=    arg.equals("<>")         ?" selected":"" %>>&lt;&gt;
				<option value="&gt;"<%=        arg.equals(">")          ?" selected":"" %>>&gt;
				<option value="&gt;="<%=       arg.equals(">=")         ?" selected":"" %>>&gt;=
				<option value="&lt;"<%=        arg.equals("<")          ?" selected":"" %>>&lt;
				<option value="&lt;="<%=       arg.equals("<=")         ?" selected":"" %>>&lt;=
				<option value="LIKE"<%=        arg.equals("LIKE")       ?" selected":"" %>>LIKE
				<option value="IS NULL"<%=     arg.equals("IS NULL")    ?" selected":"" %>>IS NULL
				<option value="IS NOT NULL"<%= arg.equals("IS NOT NULL")?" selected":"" %>>IS NOT NULL
				<option value="IN"<%=          arg.equals("IN")         ?" selected":"" %>>IN (list)
				<option value="NOT IN"<%=      arg.equals("NOT IN")     ?" selected":"" %>>NOT IN (list)
			</select></font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Argument Type: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<%  arg = (request.getParameter("argType")!=null)?request.getParameter("argType"):(isEdit?nvl(currFilter.getArgType()):""); %>
			<select name="argType" onChange="document.forma.formFieldType.value=''; if(options[selectedIndex].value=='<%= AppConstants.AT_COLUMN %>') document.forma.argValue.value='';">
				<option value="<%= AppConstants.AT_VALUE %>"<%=   arg.equals(AppConstants.AT_VALUE)  ?" selected":"" %>>Simple Value
				<option value="<%= AppConstants.AT_COLUMN %>"<%=  arg.equals(AppConstants.AT_COLUMN) ?" selected":"" %>>Another Column
				<option value="<%= AppConstants.AT_FORMULA %>"<%= arg.equals(AppConstants.AT_FORMULA)?" selected":"" %>>Expression
				<option value="<%= AppConstants.AT_LIST %>"<%=    arg.equals(AppConstants.AT_LIST)   ?" selected":"" %>>List of Values
				<option value="<%= AppConstants.AT_FORM %>"<%=    arg.equals(AppConstants.AT_FORM)   ?" selected":"" %>>Run-time Form Field
			</select></font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Argument Value: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<%	String argValue = nvl(request.getParameter("argValue"), isEdit?(nvl(currFilter.getArgType()).equals(AppConstants.AT_COLUMN)?"["+rdef.getColumnById(currFilter.getArgValue()).getDisplayName()+"]":nvl(currFilter.getArgValue())):"");
				String formFieldType = "";
				if(arg.equals(AppConstants.AT_FORM)&&rdef.getFormFieldList()!=null)
					for(Iterator iter=rdef.getFormFieldList().getFormField().iterator(); iter.hasNext(); iCnt++) { 
						FormFieldType fft = (FormFieldType) iter.next();
						if(argValue.equals("["+fft.getFieldName()+"]")) {
							formFieldType = fft.getFieldType();
							break;
						}	// if
					}	// for 
			%>
		    <input type="hidden" name="formFieldType" value="<%= formFieldType %>">
			<input type="text" size="30" name="argValue" class=rtabletext value="<%= argValue %>" 
				onFocus="if(document.forma.argType.options[document.forma.argType.selectedIndex].value=='<%= AppConstants.AT_COLUMN %>') blur();" onChange="document.forma.formFieldType.value='';"></font>
		    <a href="javascript:showArgPopup()"><img border="0" src="<%= AppUtils.getImgFolderURL() %>shareicon.gif" alt="Select from list" width="12" height="12"></a>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Closing Brackets: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<%  arg = (request.getParameter("closeBrackets")!=null)?request.getParameter("closeBrackets"):(isEdit?nvl(currFilter.getCloseBrackets()):""); %>
			<select name="closeBrackets">
				<option value=""<%=      arg.equals("")     ?" selected":"" %>>
				<option value=")"<%=     arg.equals(")")    ?" selected":"" %>>)
				<option value="))"<%=    arg.equals("))")   ?" selected":"" %>>))
				<option value=")))"<%=   arg.equals(")))")  ?" selected":"" %>>)))
				<option value="))))"<%=  arg.equals("))))") ?" selected":"" %>>))))
				<option value=")))))"<%= arg.equals(")))))")?" selected":"" %>>)))))
			</select></font></td>
	</tr>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
	var selExpr = "";
	selExpr = document.forma.filterExpr.options[document.forma.filterExpr.selectedIndex].value;
	var selArgType = "";
	selArgType = document.forma.argType.options[document.forma.argType.selectedIndex].value;
	
	var isListArgument = false;
	if(selArgType=="<%= AppConstants.AT_LIST %>")
		isListArgument = true;
	
	var isFormSingleValueArgument = false;	// text with popup, drop-down, list-box and radio-buttons are always single value
	var isFormMultiValueArgument  = false;	// check boxes and multi-select list box are always multi-value; text box and text area can be either
	var isFormArgument            = false;	// text box and text area can be used for both single-value and multi-value
	if(selArgType=="<%= AppConstants.AT_FORM %>") {
		isFormArgument = true;
		
		if( document.forma.formFieldType.value=="<%= FormField.FFT_TEXT_W_POPUP %>"||
			document.forma.formFieldType.value=="<%= FormField.FFT_COMBO_BOX %>"||
			document.forma.formFieldType.value=="<%= FormField.FFT_LIST_BOX %>"||
			document.forma.formFieldType.value=="<%= FormField.FFT_RADIO_BTN %>")
			isFormSingleValueArgument = true;
		
		if( document.forma.formFieldType.value=="<%= FormField.FFT_CHECK_BOX %>"||
			document.forma.formFieldType.value=="<%= FormField.FFT_LIST_MULTI %>")
			isFormMultiValueArgument = true;
	}	// if
	
	if((selExpr!="IS NULL")&&(selExpr!="IS NOT NULL")) {
		if((selExpr=="IN")||(selExpr=="NOT IN")) {
			if(! (isListArgument||(isFormArgument&&(! isFormSingleValueArgument)))) {
				alert("This expression requires argument which is List of Values.\nPlease either select the argument type List of Values or\nselect argument type Run-time Form Field and then select Form Field which is Text Box, Text Area, Check Boxes or Multi-select List Box.");
				document.forma.argValue.focus();
				document.forma.argValue.select();
				
				return false;
			}
		} else
			if(isListArgument||(isFormArgument&&isFormMultiValueArgument)) {
				alert("This expression cannot have argument type List of Values.\nPlease select another argument type.\n\nNote: Form Field which is Check Boxes or Multi-select List Box is also considered to be List of Values.");
				document.forma.argValue.focus();
				document.forma.argValue.select();
				
				return false;
			}
		
		if(selArgType!="<%= AppConstants.AT_FORM %>")
			if(document.forma.argValue.value=="") {
				alert("Please provide argument value");
				document.forma.argValue.focus();
				document.forma.argValue.select();
				
				return false;
			}
	}	// if
	 
	return true;
}   // dataValidate
//-->
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
