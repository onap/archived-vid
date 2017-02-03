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
<%@ page import="java.text.SimpleDateFormat"%>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();
	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED); 
	String dependsOnHelp = "Custom SQL can be defined";
%>

<%	String[] dateFormats   = { "MM/DD/YYYY", "MM/YYYY", "DD-MON-YYYY", "Month DD, YYYY", "Month, YYYY" };
	String[] charFormats   = { "N/A" }; 
	
	boolean isEdit = curSubStep.equals(AppConstants.WSS_EDIT);
	FormFieldType currField  = null;
	if(isEdit)
		currField  = rdef.getFormFieldById(AppUtils.getRequestNvlValue(request, AppConstants.RI_DETAIL_ID)); 
	
	String currColId = (currField!=null)?nvl(currField.getColId()):""; 
	String remoteDbPrefix = (String) session.getAttribute("remoteDB");

	String colTableName     = null;
	String colColumnName    = null;
	String colDisplayFormat = null;
	String colDisplayName   = null;
	if(isSQLBased)
		if(currColId.length()>0) {
			colTableName     = ReportWrapper.getSQLBasedFFTColTableName(currColId);
			colColumnName    = ReportWrapper.getSQLBasedFFTColColumnName(currColId);
			colDisplayFormat = ReportWrapper.getSQLBasedFFTColDisplayFormat(currColId);
			
			if(currColId.indexOf("|")>=0)
				currColId = currColId.substring(0, currColId.indexOf('|'));
			colDisplayName   = colColumnName;
			//colDisplayName = currColId.substring(currColId.indexOf('.')+1);
		} %>

<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/editabledropdown.js"></script>
<script language="JavaScript">
 
<!--
var fieldColName = "";

	function toggleDiv(isChecked) {
		if(isChecked) {
			if(document.getElementById('showDefaultSql'))
			document.getElementById('showDefaultSql').style.display="";
			if(document.getElementById('showDefaultValue'))
			document.getElementById('showDefaultValue').style.display="none";
			if(document.forma.defaultValue)
			   document.forma.defaultValue.value="";
		} else {
			if(document.getElementById('showDefaultSql'))
			document.getElementById('showDefaultSql').style.display="none";
			if(document.getElementById('showDefaultValue'))
			document.getElementById('showDefaultValue').style.display="";	
			if(document.getElementById('showDefaultSql') && document.forma.fieldDefaultSQL)
			   document.forma.fieldDefaultSQL.value="";		
		}
					
	}
	
	function toggleFieldTypeSelection() {
		   var selectBox = document.forma.fieldType;
		   var selectedString = "";
		   if(selectBox)
			selectedString = selectBox.options[selectBox.selectedIndex].value;
		   else {
			 <%	if(currField!=null) { %>  
             		selectString = '<%=currField.getFieldType()%>';
             <% } %>
		   }
		   
		   if(selectedString == '<%= FormField.FFT_LIST_MULTI %>'){
			   document.getElementById('multiSelectListSizeDiv').style.display="block";	
		   } else {
			   document.getElementById('multiSelectListSizeDiv').style.display="none";
			   document.getElementById('multiSelectListSize').value="4";
		   }
	}
	
<% if(isSQLBased) { %>
function showTableColsPopup() {
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.table.cols&single_value=Y&return_table_name=Y&return_col_type=Y&remoteDbPrefix=<%=remoteDbPrefix%>", "tableColsPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showTableColsPopup

function setDisplayTypeValues(dbType) {
	if(document.forma.colType.value!=dbType) {
		document.forma.colType.value = dbType;
		
		document.forma.displayFormat.options.length = 0;
		
		if(document.forma.colType.value=="<%= AppConstants.CT_DATE %>") {
		<% for(int i=0; i<dateFormats.length; i++) { %>
			document.forma.displayFormat.options[<%= i %>] = new Option("<%= dateFormats[i] %>", "<%= dateFormats[i] %>");
		<%  } %>
		} else {
		<% for(int i=0; i<charFormats.length; i++) { %>
			document.forma.displayFormat.options[<%= i %>] = new Option("<%= charFormats[i] %>", "<%= charFormats[i].equals("N/A")?"":charFormats[i] %>");
		<%  } %>
		}	// else
		
		document.forma.displayFormat.selectedIndex = 0;
		
		if(document.layers)
			history.go(0);
	}	// if
}   // setDisplayTypeValues

function addText(newValue) {
	var newText = "";
	if(newValue.indexOf("|")<0)
		newText = newValue;
	else {
		setDisplayTypeValues(newValue.substring(newValue.lastIndexOf("|")+1));
		newText = newValue.substring(0, newValue.lastIndexOf("|"));
	}	// else
	
	document.getElementById('fieldColId').value = newText;
	
	var newFieldColName = "";
	newFieldColName = newText.substring(newText.lastIndexOf(".")+1);
	
	if(document.getElementById('fieldName').value==""||document.getElementById('fieldName').value==fieldColName)
		document.getElementById('fieldName').value = newFieldColName;
	fieldColName = newFieldColName;
}   // addText

function clearText() {
	document.getElementById('fieldColId').value = "";
}   // clearText

<% } else { %>
function changeColId() {
	var newFieldColName = document.getElementById('fieldColId').options[document.getElementById('fieldColId').selectedIndex].text;
	if(document.getElementById('fieldColId').selectedIndex>0&&(document.getElementById('fieldName').value==""||document.getElementById('fieldName').value==fieldColName))
		document.getElementById('fieldName').value = newFieldColName;
	fieldColName = newFieldColName;
}   // changeColId

function showDefaultValuePopup() {
	if(document.getElementById('fieldColId').selectedIndex==0) {
		alert("The assistance is available only if this field is based on a column.\nYou can select a column from the list or just type a default value.");
		document.getElementById('fieldColId').focus();
		return;
	}	// if
	
	var colId = document.getElementById('fieldColId').options[document.getElementById('fieldColId').selectedIndex].value;
	
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.filter.data&<%= AppConstants.RI_ARG_TYPE %>=<%= AppConstants.AT_VALUE %>&<%= AppConstants.RI_COLUMN_ID %>="+colId+"&<%= AppConstants.RI_JS_TARGET_FIELD %>=document.forma.defaultValue&<%= AppConstants.RI_RESET_PARAMS %>=Y", "defaultValuePopup", "width=440,height=400,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showDefaultValuePopup
<% }	//  else if(isSQLBased) 
%>

function showTestRunSQLPopup() {
	//var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.testrun.sql&<%= AppConstants.RI_FORMATTED_SQL %>="+escape(""+document.getElementById('fieldSQL').value)+"&<%= AppConstants.RI_CHK_FIELD_SQL %>=Y", "testRunSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.field.testrun.jsp", "testRunSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showTestRunSQLPopup

function showTestRunDefaultSQLPopup() {
	//var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.testrun.sql&<%= AppConstants.RI_FORMATTED_SQL %>="+escape(""+document.getElementById('fieldSQL').value)+"&<%= AppConstants.RI_CHK_FIELD_SQL %>=Y", "testRunSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.field.default.testrun.jsp", "testRunSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showTestRunDefaultSQLPopup

function showStartDatetRunSQLPopup( ) {
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.field.date.start.testrun.jsp", "testRunSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showTestRunRangeSQLPopup

function showEndDateRunSQLPopup( ) {
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.field.date.end.testrun.jsp", "testRunSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
} 
function setDefaultVerifyFlag() {
	document.forma.fieldDefaultSQLOrig.value = document.forma.fieldDefaultSQL.value;
}   // setDefaultVerifyFlag

function setVerifyFlag() {
	document.forma.fieldSQLOrig.value = document.getElementById('fieldSQL').value;
}   // setVerifyFlag

function showSQLInstructions() {
	var w = window.open("", "instrPopup", "width=400,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
	
	w.document.writeln("<html><head>");
	w.document.writeln("<title>Form Field SQL Instructions</title>");
	w.document.writeln("<link rel=stylesheet type=text/css href='<%= AppUtils.getBaseFolderURL() %>css/raptor.css'>");
	w.document.writeln("</head><body>");
	
	w.document.writeln("<table width=94% border=0 cellspacing=1 cellpadding=3 align=center>");
	w.document.writeln("	<tr class=rbg1 height=30>");
	w.document.writeln("		<td><b class=rtableheader>Form Field SQL Instructions</b></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr class=rbg3 align=center valign=middle>");
	w.document.writeln("		<td align=left valign=middle><font class=rtabletext>");
	w.document.writeln("			The SQL result set must have a column <b>id</b> and a column <b>name</b>. ");
	w.document.writeln("			If selecting date values in visual report, the <b>id</b> must be formatted <b>MM/DD/YYYY</b>; the <b>name</b> can use any display format. ");
	w.document.writeln("			Example:<br><br><b>SELECT DISTINCT</b><br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TO_CHAR(t.start_date, 'MM/DD/YYYY') <b>id</b>,<br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TO_CHAR(t.start_date, 'Month YYYY') <b>name</b><br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;<b>FROM</b><br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;my_table t<br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;<b>WHERE</b><br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;t.start_date IS NOT NULL<br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;<b>ORDER BY</b><br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;t.start_date DESC");
	w.document.writeln("		</font></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr class=rbg1 height=30>");
	w.document.writeln("		<td>&nbsp;</td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr>");
	w.document.writeln("		<td align=center><br><input type=Submit class=Button value=Close onClick=\"window.close();\"></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("</table>");
	
	w.document.writeln("</body></html>");
	w.document.close();
}	// showSQLInstructions

function showRangeSQLInstructions() {
	var w = window.open("", "instrPopup", "width=400,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
	
	w.document.writeln("<html><head>");
	w.document.writeln("<title>Form Field Range SQL Instructions</title>");
	w.document.writeln("<link rel=stylesheet type=text/css href='<%= AppUtils.getBaseFolderURL() %>css/raptor.css'>");
	w.document.writeln("</head><body>");
	
	w.document.writeln("<table width=94% border=0 cellspacing=1 cellpadding=3 align=center>");
	w.document.writeln("	<tr class=rbg1 height=30>");
	w.document.writeln("		<td><b class=rtableheader>Form Field Range SQL Instructions</b></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr class=rbg3 align=center valign=middle>");
	w.document.writeln("		<td align=left valign=middle><font class=rtabletext>");
	w.document.writeln("			The SQL should ALWAYS return a valid Oralce date. If the return is not a valid date, the range will not be set.<br>");
	w.document.writeln("			The SQL will always get precedence over the static values.<br>");
	w.document.writeln("			");
	w.document.writeln("			Example:<br><br><b>SELECT</b> SYSDATE<br> ");
	w.document.writeln("			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>FROM</b> DUAL");
	w.document.writeln("		</font></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr class=rbg1 height=30>");
	w.document.writeln("		<td>&nbsp;</td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr>");
	w.document.writeln("		<td align=center><br><input type=Submit class=Button value=Close onClick=\"window.close();\"></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("</table>");
	
	w.document.writeln("</body></html>");
	w.document.close();
}	// showRangeSQLInstructions
//-->
</script>

	
<table width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %> - <%= curSubStep %></b></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Field Name: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<input type="text" style="width: 100px;" class="rtabletext" size="30" maxlength="30" id="fieldName" name="fieldName" value="<%= isEdit?currField.getFieldName():"" %>"></font></td>
	</tr>
 	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Based On Column: </font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
<%			if(isSQLBased) { %>
			<input type=text  style="width: 100px;" class="rtabletext" size=50 id="fieldColId" name="fieldColId" value="<%= currColId %>" onFocus="blur()">
			<a href="javascript:showTableColsPopup()"><img border=0 src="<%= AppUtils.getImgFolderURL() %>shareicon.gif" alt="Select database column" width=12 height=12></a>
<%			} else { %>
			<select name="fieldColId" class="rtabletext" onChange="changeColId()" style="width: 100px;" >
				    <option value=""<%= currColId.equals("")?" selected":"" %>>--- None ---
<%				int iCount = 0;
				List reportCols = rdef.getAllColumns();
				for(Iterator iter=reportCols.iterator(); iter.hasNext(); iCount++) { 
					DataColumnType dct = (DataColumnType) iter.next(); %>
				    <option value="<%= dct.getColId() %>"<%= currColId.equals(dct.getColId())?" selected":"" %>><%= dct.getDisplayName() %>
<%					if(currColId.equals(dct.getColId()))
						colDisplayName = dct.getDisplayName();
				}   // for 
%>
			</select>
<%			}   // else if(isSQLBased)
			if(colDisplayName!=null) { %>
<script language="JavaScript">
<!--
fieldColName = "<%= colDisplayName %>";
//-->
</script>
<%			}   // if 
%>
			</font>
		</td>
	</tr>  
<%	if(isSQLBased) {
		String colType = AppConstants.CT_CHAR;
		if(colTableName!=null&&colColumnName!=null)
			try {
				colType  = nvl(DataCache.getReportTableDbColumnType(colTableName, colColumnName,((String) session.getAttribute("remoteDB"))), AppConstants.CT_CHAR);
			} catch(Exception e) {}
			
		String[] fmt;
		if(colType.equals(AppConstants.CT_DATE)) 
			fmt = dateFormats;
		else 
			fmt = charFormats; %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Display Format: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="displayFormat" style="width: 100px;" >
			<%	for(int i=0; i<fmt.length; i++) { %>
				<option value="<%= fmt[i].equals("N/A")?"":fmt[i] %>"<%= (nvl(colDisplayFormat).toUpperCase().equals(fmt[i].toUpperCase()))?" selected":"" %>><%= fmt[i] %>
			<%  } %>
			</select>
			<input type="hidden" name="colType" value="<%= colType %>">
		</font></td>
	</tr> 
<%	}	// if 
%>

	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Visible? </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="visible" style="width: 100px;" >
				<option value="Y"<%= isEdit?(nvl(currField.getVisible(),"Y").startsWith("Y")?" selected":""):" selected" %>>Yes
				<option value="N"<%= (isEdit&&(! nvl(currField.getVisible(),"Y").startsWith("Y")))?" selected":""        %>>No
			</select></font></td>
	</tr>
   <% if(Globals.getAllowSQLBasedReports() || AppUtils.isAdminUser(request)) { %>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Is used in Group By Clause?</font>
		</td>
		<td class=rbg3 align="left" nowrap>
			<input type="checkbox" name="isGroupFormField" value="Y" <%=(currField!=null && (currField.isGroupFormField()!=null && currField.isGroupFormField().booleanValue()))?" checked":"" %>>
		</td>
	</tr>
	<% } %>
	
	
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Field Type: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<% String curValue = nvl((currField!=null)?currField.getFieldType():"", FormField.FFT_TEXT_W_POPUP); %>
			<select name="fieldType" onChange="toggleFieldTypeSelection()" style="width: 100px;" >
				<%--<option value="<%= FormField.FFT_TEXT_W_POPUP %>"<%= curValue.equals(FormField.FFT_TEXT_W_POPUP)?" selected":"" %>>Text Box with Popup --%>
				<option value="<%= FormField.FFT_TEXT         %>"<%= curValue.equals(FormField.FFT_TEXT        )?" selected":"" %>>Text Box
				<%--<option value="<%= FormField.FFT_TEXTAREA     %>"<%= curValue.equals(FormField.FFT_TEXTAREA    )?" selected":"" %>>Text Area--%>
				<%--<option value="<%= FormField.FFT_COMBO_BOX    %>"<%= curValue.equals(FormField.FFT_COMBO_BOX   )?" selected":"" %>>Drop-Down List--%>
				<option value="<%= FormField.FFT_LIST_BOX     %>"<%= curValue.equals(FormField.FFT_LIST_BOX    )?" selected":"" %>>List Box
				<%--<option value="<%= FormField.FFT_RADIO_BTN    %>"<%= curValue.equals(FormField.FFT_RADIO_BTN   )?" selected":"" %>>Radio Buttons
				<option value="<%= FormField.FFT_CHECK_BOX    %>"<%= curValue.equals(FormField.FFT_CHECK_BOX   )?" selected":"" %>>Check Boxes --%>
				<option value="<%= FormField.FFT_LIST_MULTI   %>"<%= curValue.equals(FormField.FFT_LIST_MULTI  )?" selected":"" %>>Multi-select List Box
				<option value="<%= FormField.FFT_HIDDEN       %>"<%= curValue.equals(FormField.FFT_HIDDEN      )?" selected":"" %>>Hidden
			</select>
		</font></td>
	</tr> 

	<tr id="multiSelectListSizeDiv">
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Visible Size: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select id="multiSelectListSize" name="multiSelectListSize" onKeyDown="fnKeyDownHandler(this, event);" onKeyUp="fnKeyUpHandler_A(this, event); return false;" onKeyPress = "return fnKeyPressHandler_A(this, event);"  onChange="fnChangeHandler_A(this, event);">
				<option value="1"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("1")?" selected":"" )%>>1
				<option value="2"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("2")?" selected":"" )%>>2
				<option value="3"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("3")?" selected":"" )%>>3
				<option value="4"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("4")?" selected":"" )%>>4
				<option value="5"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("5")?" selected":"" )%>>5
				<option value="6"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("6")?" selected":"" )%>>6
				<option value="7"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("7")?" selected":"" )%>>7
				<option value="8"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("8")?" selected":"" )%>>8
				<option value="9"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("9")?" selected":"" )%>>9
				<option value="10"<%= (currField!=null && nvl(currField.getMultiSelectListSize(), "4").equals("10")?" selected":"" )%>>10
				<option value="" style="COLOR:#ff0000;BACKGROUND-COLOR:#ffff00;">Custom</option> <!-- This is the Editable Option -->
				<% if(currField!=null && isNumber(nvl(currField.getMultiSelectListSize())) && new Integer(nvl(currField.getMultiSelectListSize(), "0")).intValue() <= 20) {
				%>
					<option value="<%=nvl(currField.getMultiSelectListSize())%>" selected><%=nvl(currField.getMultiSelectListSize())%></option> <!-- This is the Editable Option -->
                <%				
					 } 
                %>						
			</select>			
		</font></td>
	</tr> 
	
		<%if(! isSQLBased) { %>	
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Default Value: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<input type="text" class="rtabletext" size="20" maxlength="50" name="defaultValue" value="<%= isEdit?nvl(currField.getDefaultValue()):"" %>"></font>
		    <a href="javascript:showDefaultValuePopup()"><img border="0" src="<%= AppUtils.getImgFolderURL() %>shareicon.gif" alt="Select from list" width="12" height="12"></a>
        </td></tr>
		<% } else { %>
            <% if(Globals.getAllowSQLBasedReports() || AppUtils.isAdminUser(request)) { %>

			<tr>
				<td class=rbg2 height="30" align="right">
					<font class=rtabletext>Is Default Value should be SQL</font>
				</td>
 				<td class=rbg3 align="left" nowrap>
					<input type="checkbox" id="isDefaultSQL" name="isDefaultSQL" value="N" <%=currField!=null && currField.getFieldDefaultSQL()!=null && currField.getFieldDefaultSQL().length()>0?" checked":"" %> onClick="toggleDiv(this.checked)">
				</td>
			</tr>
            

            <tbody id="showDefaultSql" style="display:none;">
               <tr>
                <td class=rbg2 height="30" align="right">Default SQL: </td>
				<td class=rbg3>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td valign="top" rowspan="2">
			<input type="hidden" name="fieldDefaultSQLOrig" value="<%= (currField!=null)?nvl(currField.getFieldDefaultSQL()):"" %>">
			<textarea class="buttonLabelField" name="fieldDefaultSQL" cols="40" rows="3"><%= (currField!=null)?nvl(currField.getFieldDefaultSQL()):"" %></textarea>
			</td>
			<td align="center" valign="top">&nbsp;<input type="Button" att-button btn-type="primary" size="small" class=button value="Verify SQL" onClick="if(document.forma.fieldDefaultSQL.value=='') {alert('Please provide SQL.'); return false; } showTestRunDefaultSQLPopup();">&nbsp;</td>
			<td rowspan="2" width="60%">&nbsp;</td></tr><tr>
			<td align="center" valign="middle">&nbsp;<a href="javascript:showSQLInstructions()" class=rtabletext>Instructions</a>&nbsp;</td>
			</tr></table>
                </td> 
              </tr>
			</tbody>
            <% if(Globals.getAllowSQLBasedReports() || AppUtils.isAdminUser(request)) { %>
             <script language="Javascript">
             toggleDiv(document.getElementById('isDefaultSQL').checked);
             </script>
           <% } %>
		    <% } // if admin user  %>  
            <tbody id="showDefaultValue">
               <tr> 
 				<td class=rbg2 height="30" align="right"> Default Value: </td>
				<td class=rbg3>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td valign="top" rowspan="2">
					<input type="text" style="width:200px" class="rtabletext" size="20" maxlength="50" name="defaultValue" value="<%= isEdit?nvl(currField.getDefaultValue()):"" %>"></font>
                 </td>
				</tr></table>
		</td>
	</tr>
			</tbody>
            <% if(Globals.getAllowSQLBasedReports() || AppUtils.isAdminUser(request)) { %>
             <script language="Javascript">
             toggleDiv(document.getElementById('isDefaultSQL').checked);
             </script>
            <% } %>
		<% } //else %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Verify Field Value As: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<% curValue = nvl((currField!=null)?currField.getValidationType():"", FormField.VT_NONE); %>
			<select name="validation" onChange="if (this.value =='<%= FormField.VT_DATE%>') ; else dateRangeRow.style.display='none'" style="width: 200px;" >
				<option value="<%= FormField.VT_NONE               %>"<%= curValue.equals(FormField.VT_NONE              )?" selected":"" %>>--- Do Not Perform Validation ---
				<option value="<%= FormField.VT_DATE               %>"<%= curValue.equals(FormField.VT_DATE              )?" selected":"" %>>Date
				<option value="<%= FormField.VT_TIMESTAMP_HR       %>"<%= curValue.equals(FormField.VT_TIMESTAMP_HR      )?" selected":"" %>>TimeStamp (Hour)
				<option value="<%= FormField.VT_TIMESTAMP_MIN      %>"<%= curValue.equals(FormField.VT_TIMESTAMP_MIN     )?" selected":"" %>>TimeStamp (Hour, Min)
				<option value="<%= FormField.VT_TIMESTAMP_SEC      %>"<%= curValue.equals(FormField.VT_TIMESTAMP_SEC     )?" selected":"" %>>TimeStamp (Hour, Min, Sec)
				<option value="<%= FormField.VT_INT                %>"<%= curValue.equals(FormField.VT_INT               )?" selected":"" %>>Integer
				<option value="<%= FormField.VT_INT_NON_NEGATIVE   %>"<%= curValue.equals(FormField.VT_INT_NON_NEGATIVE  )?" selected":"" %>>Positive Integer
				<option value="<%= FormField.VT_INT_POSITIVE       %>"<%= curValue.equals(FormField.VT_INT_POSITIVE      )?" selected":"" %>>Positive Integer, Cannot Be Zero
				<option value="<%= FormField.VT_FLOAT              %>"<%= curValue.equals(FormField.VT_FLOAT             )?" selected":"" %>>Any Number
				<option value="<%= FormField.VT_FLOAT_NON_NEGATIVE %>"<%= curValue.equals(FormField.VT_FLOAT_NON_NEGATIVE)?" selected":"" %>>Positive Number
				<option value="<%= FormField.VT_FLOAT_POSITIVE     %>"<%= curValue.equals(FormField.VT_FLOAT_POSITIVE    )?" selected":"" %>>Positive Number, Cannot Be Zero
			</select></font>
		</td>
	</tr> 	
	<tr id="dateRangeRow" <%if(curValue.equals(FormField.VT_DATE) || curValue.equals(FormField.VT_TIMESTAMP_HR) || curValue.equals(FormField.VT_TIMESTAMP_MIN) || curValue.equals(FormField.VT_TIMESTAMP_SEC)){%><%}else{%>style="display:none"<%}%>>
		<td colspan="2" class=rbg2>
		<table width="100%">
			<tr>
				<td class=rbg2 align="right" width="33%" height="30"><font class=rtabletext>Valid Date Range: </font></td> 
				<td width="15%" align="left">
					<font class=rtabletext >From </font>
				</td>
				<td align="left">
					<font class=rtabletext >To </font>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:showRangeSQLInstructions()" class=rtabletext>Instructions</a>&nbsp;
				</td>
				
			</tr>				
			<tr>
				<td class=rbg2 align="right"></td> 
				</td>
				<td width="15%" align="left">
					<input type="hidden" name="rangeStartDateSQLOrig" value="<%= (currField!=null)?nvl(currField.getRangeStartDateSQL()):"" %>">
					<textarea name="rangeStartDateSQL" class="rtabletext" style="width: 200px;height: 100px" cols="20" rows="3"><%= (currField!=null)?nvl(currField.getRangeStartDateSQL()):"" %></textarea>
					
				</td>
				<td align="left">
					<input type="hidden" name="rangeEndDateSQLOrig" value="<%= (currField!=null)?nvl(currField.getRangeEndDateSQL()):"" %>">
					<textarea name="rangeEndDateSQL" class="rtabletext" style="width: 200px;height: 100px" cols="20" rows="3"><%= (currField!=null)?nvl(currField.getRangeEndDateSQL()):"" %></textarea>
					
				</td>
			</tr>
			<tr>
				<td class=rbg2 align="right"></td> 
				</td>
				<td width="15%" align="left">
					<input type="Button" class=button att-button btn-type="primary" size="small" value="Verify SQL" onClick="if(document.forma.rangeStartDateSQL.value=='') {alert('Please provide SQL.'); return false; } showStartDatetRunSQLPopup();">&nbsp;</td>
				</td>
				<td align="left">
					<input type="Button" class=button att-button btn-type="primary" size="small" value="Verify SQL" onClick="if(document.forma.rangeEndDateSQL.value=='') {alert('Please provide SQL.'); return false; } showEndDateRunSQLPopup();">&nbsp;</td>
				</td>
			</tr>
			<tr>
				<td class=rbg2 align="right"></td> 
				<td class=rbg3 align="left">
					<%	String stDate = "";
						String endDate = "";
						if (currField!= null 
						  && currField.getRangeStartDate() != null && currField.getRangeStartDate().equals("") == false
						  && currField.getRangeEndDate() != null && currField.getRangeEndDate().equals("") == false){
							SimpleDateFormat dtf = new SimpleDateFormat("MM/dd/yyyy");
							stDate = dtf.format(currField.getRangeStartDate().toGregorianCalendar().getTime());
							endDate = dtf.format(currField.getRangeEndDate().toGregorianCalendar().getTime());
						}
					%>
					<input type="text" class="rtabletext" size="10" maxlength="10" style="width: 100px;" name="rangeStartDate" id="rangeStartDate" value="<%=stDate%>">
					<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20" onClick="oCalendar.select(document.getElementById('rangeStartDate'),event,'MM/dd/yyyy'); return false;"  style="cursor:hand">
					

				</td>
				<td>
					<input type="text" class="rtabletext" size="10" maxlength="10" style="width: 100px;" name="rangeEndDate" id="rangeEndDate" value="<%=endDate%>">
					<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20" onClick="oCalendar.select(document.getElementById('rangeEndDate'),event,'MM/dd/yyyy'); return false;"  style="cursor:hand">								
				</td>
			</tr>
			
		</table>
		</td>
		
	</tr> 
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>&nbsp; </font></td> 
		<td class=rbg3 align="left">
			<% curValue = nvl((currField!=null)?currField.getMandatory():"", "N"); %>
			<input type="Checkbox" class="checkbox" name="mandatory" value="Y"<%= curValue.equals("Y")?" checked":"" %>>
			<font class=rtabletext>User must provide value for this field</font>
		</td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Field Help Text: </font></td> 
		<td class=rbg3 align="left">
			<textarea name="fieldHelp" style="width: 100px;" class="rtabletext" cols="40" rows="3"><%= (currField!=null)?nvl(currField.getComment()):"" %></textarea>
		</td>
	</tr>
<% if(Globals.getAllowSQLBasedReports()||AppUtils.isAdminUser(request)) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>SQL Generating Custom List of Values: <br>(overrides default list) </font></td> 
		<td class=rbg3 align="left">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td valign="top" rowspan="2">
			<input type="hidden" name="fieldSQLOrig" value="<%= (currField!=null)?nvl(currField.getFieldSQL()):"" %>">
			<textarea id="fieldSQL" name="fieldSQL" class="rtabletext" cols="40" rows="3"><%= (currField!=null)?nvl(currField.getFieldSQL()):"" %></textarea>
			</td>
			<td align="center" valign="top">&nbsp;<input type="Button" class=button att-button btn-type="primary" size="small" value="Verify SQL" onClick="if(document.getElementById('fieldSQL').value=='') {alert('Please provide SQL.'); return false; } showTestRunSQLPopup();">&nbsp;</td>
			<td rowspan="2" width="60%">&nbsp;</td></tr><tr>
			<td align="center" valign="middle">&nbsp;<a href="javascript:showSQLInstructions()" class=rtabletext>Instructions</a>&nbsp;</td>
			</tr></table>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="right" height="30"></td> 
		<td class=rbg3 align="left">
		<% curValue = nvl((currField!=null)?currField.getDependsOn():"", "N"); %>
		<font class=rtabletext><input id='dependsOn' type="checkbox" name="dependsOn" value="Y"<%= curValue.equals("Y") ? "checked":"" %>>
		<label title="<%=dependsOnHelp%>" style="cursor: pointer;" for="dependsOn">Depends on another Form Field.</label>
		</td>
	</tr>
<% } else { %>
			<input type="hidden" id="fieldSQL" name="fieldSQL" value="<%= (currField!=null)?nvl(currField.getFieldSQL()):"" %>">
<% } %>
<% 	List predefinedValues = (currField!=null&&currField.getPredefinedValueList()!=null)?currField.getPredefinedValueList().getPredefinedValue():null; %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Provide Predefined List of Values:</font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<%	if(predefinedValues==null||predefinedValues.size()==0) { %>
				Do not use Predefined list - Generate list from database
			<%	} else { 
					String value = (String) predefinedValues.get(0); %>
				<%= value %>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! dataValidate()) {return false;} else if(! confirm('Are you sure?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_USER %>'; document.forma.delPredefinedValue.value='<%= value %>'; }">
			<%	} %>
			</font></td>
	</tr>
<%	if(predefinedValues!=null&&predefinedValues.size()>1)
		for(int i=1; i<predefinedValues.size(); i++) { 
			String value = (String) predefinedValues.get(i); %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>&nbsp;</font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<%= value %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! dataValidate()) {return false;} else if(! confirm('Are you sure?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_USER %>'; document.forma.delPredefinedValue.value='<%= value %>'; }">
			</font></td>
	</tr>
<%		}	// for 
%>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>&nbsp; </font></td> 
		<td class=rbg3 align="left" valign="middle" nowrap><font class=rtabletext>
			<input type="hidden" name="delPredefinedValue" value="">
			<input type="text" style="width: 200px;" size="20" maxlength="50" name="newPredefinedValue" value="">
			<input type="Submit" att-button btn-type="primary" size="small" class=button value="Add To List" onClick="if(document.forma.newPredefinedValue.value=='') {alert('Value cannot be empty.'); return false; } if(! stepDataValidate(false)) return false;  <%= (predefinedValues==null||predefinedValues.size()==0)?"if(! confirm('If you create a list of predefined values, it will be displayed instead of the list selected from the database.\\nAre you sure you want to do that?')) return false; ":"" %>document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_USER %>';">
			</font></td>
	</tr>
</table>
<br>

<script language="JavaScript">
<!--
function stepDataValidate(checkCol) {
	if(document.getElementById('fieldName').value=="") {
		alert("Please enter Field Name");
		document.getElementById('fieldName').focus();
		document.getElementById('fieldName').select();
		
		return false;
	}   // if
	
<% if(rdef.getFormFieldList()!=null) { %>
	if(false
<% 		for(Iterator iter=rdef.getFormFieldList().getFormField().iterator(); iter.hasNext(); ) { 
			FormFieldType fft = (FormFieldType) iter.next();
			
			if(! (isEdit&&fft.getFieldId().equals(currField.getFieldId()))) { %>
			    ||(document.getElementById('fieldName').value=="<%= fft.getFieldName() %>")
<% 		    }
		} %>
	   ) {
		alert("Form field with name "+document.getElementById('fieldName').value+" already exists.\nPlease select another name.");
		document.getElementById('fieldName').focus();
		document.getElementById('fieldName').select();
		
		return false;
	}
<% } %>
	
<% if(predefinedValues==null||predefinedValues.size()==0) { %>
	if(checkCol)
		if(document.getElementById('fieldColId').selectedIndex==0)
			if(document.forma.fieldType.selectedIndex!=1&&document.forma.fieldType.selectedIndex!=2) {
				alert("Field Type can be only Text Box or Text Area if this field is not based on a column.\nPlease change the Field Type or select a column from the list.");
				document.forma.fieldType.focus();
				
				return false;
			}	// if
<% } %>
	
<% if((Globals.getAllowSQLBasedReports()||AppUtils.isAdminUser(request)) && (rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED))) { %>
	if(document.forma.fieldSQL.value!=""&&document.forma.fieldSQL.value!=document.forma.fieldSQLOrig.value) {
		alert("You must verify the SQL.");
		document.forma.fieldSQL.focus();
		document.forma.fieldSQL.select();
		
		return false;
	}   // if
	if(document.forma.fieldDefaultSQL.value!=""&&document.forma.fieldDefaultSQL.value!=document.forma.fieldDefaultSQLOrig.value) {
		alert("You must verify the Default SQL.");
		document.forma.fieldDefaultSQL.focus();
		document.forma.fieldDefaultSQL.select();
		
		return false;
	}   // if
	if(document.forma.fieldSQL.value.length<=0 && document.forma.fieldDefaultSQL.value.length > 1 ) {
      alert("SQL Field cannot be empty when Default sql has value.");
	  return false;
	}	
<% } %>

if(document.getElementById('multiSelectListSize') && document.getElementById('multiSelectListSize').options.selectedIndex == 0) {
	if(!checkNonNegativeInteger(document.forma.multiSelectListSize.options[document.forma.multiSelectListSize.options.selectedIndex].text)) {
		alert("Please enter number in Multi Select List Size. No Characters are allowed.");
		return false;
	} else {
		if(document.getElementById('multiSelectListSize').options[document.getElementById('multiSelectListSize').options.selectedIndex].text>20) {
			alert("Please not only 20 items are allowed in Multi-Select form field.");
			return false;
		}
		document.getElementById('multiSelectListSize').options[document.getElementById('multiSelectListSize').options.selectedIndex].value=document.getElementById('multiSelectListSize').options[document.getElementById('multiSelectListSize').options.selectedIndex].text;
	}
}

	return true;
}   // dataValidate

function dataValidate() {
	return stepDataValidate(true);
}   // dataValidate

toggleFieldTypeSelection();
//-->
</script>

<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/CalendarPopup.js"></script> 
<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/calendar.css">
<iframe id="calendarFrame" class="nav" z-index:199; scrolling="no"  frameborder="0"  width=165px height=165px src="" style="position:absolute; display:none;">
</iframe>
<div id="calendarDiv" name="calendarDiv" style="position:absolute; z-index:20000; visibility:hidden; background-color:white;layer-background-color:white;"></div>

	<SCRIPT LANGUAGE="JavaScript">
 	var oCalendar = new CalendarPopup("calendarDiv", "calendarFrame");    	
    	oCalendar.setCssPrefix("raptor");
	</SCRIPT>


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
