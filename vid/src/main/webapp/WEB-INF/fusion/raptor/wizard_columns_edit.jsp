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
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.openecomp.portalsdk.analytics.model.ReportLoader"%>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.DataColumnType" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.AppConstants" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportDefinition" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.AppUtils" %>
<%@ page import="org.openecomp.portalsdk.analytics.controller.WizardSequence" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.List" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.DataCache" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.DataSourceType" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.DBColumnInfo" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.TableSource" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.IdNameValue" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.SemaphoreType" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.FormFieldType" %>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED);    
	String dbInfo = null;
	dbInfo = rdef.getDBInfo();	
    boolean isEdit = curSubStep.equals(AppConstants.WSS_EDIT) || curSubStep.equals(AppConstants.WA_MODIFY);
	DataColumnType currColumn = null;
	if(isEdit)
		currColumn = rdef.getColumnById(AppUtils.getRequestNvlValue(request, AppConstants.RI_DETAIL_ID));
	Vector reportTableSources = DataCache.getReportTableSources((String) session.getAttribute("remoteDB"));

    List reportCols = rdef.getAllColumns();
    
    HashMap actionImgMap = ReportLoader.loadActionImgLookUp();

	String[] numberFormats = { "9999999990", "9,999,999,990", "9999999990.99", "9,999,999,990.99", "$9,999,999,990.99", "$9,999,999,990.999" };
	String[] dateFormats   = { "MM/DD/YYYY", "MM/YYYY", "DD-MON-YYYY", "Month DD, YYYY", "Month, YYYY", "MM/DD/YYYY HH24:MI:SS", "YYYY" };
	String[] charFormats   = { "N/A" }; 
	
	String dispName  = "";
	String dbColType = "";
	String colType   = "";
	if(isEdit) { 
		dbColType = currColumn.getDbColType();
		colType   = currColumn.getColType();
	} 

    String dependsOnFormField = currColumn.getDependsOnFormField();
    %>
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/editabledropdown.js"></script>    
<script language="JavaScript">
<!--
var tableDrillDownCount = 0;

function verifyCrossTabDrillDown() {
<%	if(isCrossTab) { %>
	if(	(document.forma.crossTabValue.options[document.forma.crossTabValue.selectedIndex].value!="<%= AppConstants.CV_VALUE %>")
		&&(document.forma.drillDownCtl.selectedIndex!=0)) {
			alert("You cannot assign drill-down to this column unless the Column Usage in Cross-Tab is Report values.");
			document.forma.drillDownURL.value         = "";
			document.forma.drillDownParams.value      = "";
			document.forma.drillDownSuppress.value    = "";
			document.forma.drillDownRequest.value    = "";
			document.forma.drillDownCtl.selectedIndex = 0;
			return false;
		}
<%	} %>
	return true;
}	// verifyCrossTabDrillDown

function showDrillDownPopup(resetParams) {
	if(! verifyCrossTabDrillDown())
		return;
	
	if(resetParams) {
		document.forma.drillDownParams.value   = "";
		document.forma.drillDownSuppress.value = "";
                document.forma.drillDownRequest.value = "";
	}
	
	if(document.forma.drillDownCtl.selectedIndex==0) {
		document.forma.drillDownURL.value = "";
		return;
	}
	
	var ddValue = "";
	ddValue = document.forma.drillDownCtl.options[document.forma.drillDownCtl.selectedIndex].value;
	
	var url = "";
	if(document.forma.drillDownCtl.selectedIndex<=tableDrillDownCount) {
		document.forma.drillDownURL.value = ddValue;
		url = "<%= AppUtils.getRaptorActionURL() %>report.popup.drilldown.table&<%= AppConstants.RI_VIEW_ACTION %>="+ddValue;
	} else {
		document.forma.drillDownURL.value = ddValue;
		url = "<%= AppUtils.getRaptorActionURL() %>report.popup.drilldown.report&<%= AppConstants.RI_REPORT_ID %>="+ddValue+"&drillDownParams="+escape(document.forma.drillDownParams.value)+"&drillDownSuppress="+escape(document.forma.drillDownSuppress.value)+"&drillDownRequest="+escape(document.forma.drillDownRequest.value);
	}
	var w = window.open(url, "drillDownPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showArgPopup

function setDrillDownValuesSuppress(newParams, newSuppress) {
	document.forma.drillDownParams.value   = newParams;
	document.forma.drillDownSuppress.value = newSuppress;
}   // setDrillDownValuesSuppress

function setDrillDownValues(newParams) {
	setDrillDownValuesSuppress(newParams, "")
}   // setDrillDownValues

function setTotalDropDownValues(canHaveTotal) {
<% if(! isSQLBased) { %>
	if(canHaveTotal) {
		if(document.forma.displayTotal.options.length>1)
			return;
	} else {
		if(document.forma.displayTotal.options.length==1)
			return;
	}
	
	document.forma.displayTotal.options.length = 0;
	document.forma.displayTotal.options[0] = new Option("--- Do Not Display (Faster Report Execution) ---", "");
<%	if(isCrossTab) { %>
	document.forma.displayTotalPerRow.options.length = 0;
	document.forma.displayTotalPerRow.options[0] = new Option("--- Do Not Display (Faster Report Execution) ---", "");
<%  } %>
	
	if(canHaveTotal) {
		<% for(int i=0; i<AppConstants.TOTAL_FUNCTIONS.getCount(); i++) { 
				IdNameValue tValue = AppConstants.TOTAL_FUNCTIONS.getValue(i); %>
			document.forma.displayTotal.options[<%= (i+1) %>] = new Option("<%= tValue.getName() %>", "<%= tValue.getId() %>");
			<%	if(isCrossTab) { %>
			document.forma.displayTotalPerRow.options[<%= (i+1) %>] = new Option("<%= tValue.getName() %>", "<%= tValue.getId() %>");
			<%  } %>
		<%  }	// for 
                %>
	} // if
    
	document.forma.displayTotal.selectedIndex = 0;
<%	if(isCrossTab) { %>
	document.forma.displayTotalPerRow.selectedIndex = 0;
	if(document.layers)
		history.go(0);
<%  } %>
<% } // if(! isSQLBased) 
%>
}   // setTotalDropDownValues

<% if(! isSQLBased) { %>
<%--
function showFormatPopup() {
	if((document.forma.colType.value!="< %= AppConstants.CT_NUMBER % >")&&(document.forma.colType.value!="< %= AppConstants.CT_DATE % >")) {
		alert("Display Format is not applicable to this column");
		return;
	}
	
	var w = window.open("", "formatHint", "width=200,height=300,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
	w.document.writeln("<html><head>");
	w.document.writeln("<title>Display Formats</title>");
	//w.document.writeln("<link rel=stylesheet type=text/css href='< %= AppUtils.getBaseFolderURL() % >css/raptor.css'>");
	w.document.writeln("<script language=JavaScript>");
	w.document.writeln("function setValue(newValue) {");
	w.document.writeln("    window.opener.document.forma.displayFormat.value=newValue;");
	w.document.writeln("    window.close();");
	w.document.writeln("}   // setValue");
	w.document.writeln("</"+"script>");
	w.document.writeln("</head><body>");
	if(document.forma.colType.value=="< %= AppConstants.CT_NUMBER % >") {
		w.document.writeln("<font face='Arial, Helvetica, sans-serif' size=2><b>Number formats:</b></font><ul>");
		w.document.writeln("<li><font face='Arial, Helvetica, sans-serif' size=2><b><a href=\"javascript:setValue('9999999990.99')\">9999999990.99</a></b></font></li>");
		w.document.writeln("<li><font face='Arial, Helvetica, sans-serif' size=2><b><a href=\"javascript:setValue('9,999,999,990.99')\">9,999,999,990.99</a></b></font></li>");
		w.document.writeln("<li><font face='Arial, Helvetica, sans-serif' size=2><b><a href=\"javascript:setValue('$9,999,999,990.99')\">$9,999,999,990.99</a></b></font></li>");
		w.document.writeln("</ul>");
	} else if(document.forma.colType.value=="< %= AppConstants.CT_DATE % >") {
		w.document.writeln("<font face='Arial, Helvetica, sans-serif' size=2><b>Date formats:</b></font><ul>");
		w.document.writeln("<li><font face='Arial, Helvetica, sans-serif' size=2><b><a href=\"javascript:setValue('MM/DD/YYYY')\">MM/DD/YYYY</a></b></font></li>");
		w.document.writeln("<li><font face='Arial, Helvetica, sans-serif' size=2><b><a href=\"javascript:setValue('MM/YYYY')\">MM/YYYY</a></b></font></li>");
		w.document.writeln("<li><font face='Arial, Helvetica, sans-serif' size=2><b><a href=\"javascript:setValue('DD-MON-YYYY')\">DD-MON-YYYY</a></b></font></li>");
		w.document.writeln("<li><font face='Arial, Helvetica, sans-serif' size=2><b><a href=\"javascript:setValue('MONTH DD, YYYY')\">MONTH DD, YYYY</a></b></font></li>");
		w.document.writeln("</ul>");
	}
	w.document.writeln("</body></html>");
	w.document.close();
}   // showFormatPopup
--%>

function showFormFieldPopup() {
	var w = window.open("", "fieldPopup", "width=400,height=400,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
	
	w.document.writeln("<html><head>");
	w.document.writeln("<title>Form Fields</title>");
	w.document.writeln("<link rel=stylesheet type=text/css href='<%= AppUtils.getBaseFolderURL() %>css/raptor.css'>");
	w.document.writeln("<script language=JavaScript>");
	w.document.writeln("function setValue(newValue) {");
	w.document.writeln("    window.opener.document.forma.exprFormula.selectedIndex = 12;");
	w.document.writeln("    window.opener.exprFormulaChange(true);");
	w.document.writeln("    window.opener.document.forma.exprText.value += newValue;");
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
	w.document.writeln("			<b class=rtabletext><a href=\"javascript:setValue('[<%= fft.getFieldName() %>]')\"><%= fft.getFieldName() %></a></b>");
	w.document.writeln("		</td>");
	w.document.writeln("	</tr>");
<%			}	// for
	if(iCnt==0) { %>
	w.document.writeln("	<tr class=rbg3 height=30>");
	w.document.writeln("		<td colspan=2 align=center valign=middle><b class=rtabletext>There are no form fields defined</b></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr>");
	w.document.writeln("		<td colspan=2 align=center><br><input type=Submit class=button value=Close onClick=\"window.close();\"></td>");
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

function exprFormulaChange(handleText) {
	var exprValue = "";
	exprValue = document.forma.exprFormula.options[document.forma.exprFormula.selectedIndex].value;
	
	if(handleText)
		if(exprValue!='_exprText_') { 
			document.forma.exprText.value    = ''; 
			document.forma.exprText.disabled = true; 
		} else 
			document.forma.exprText.disabled = false;
	
	var oldColType = "";
	oldColType = document.forma.colType.value;
	
	if((exprValue=="")||(exprValue=="MAX(")||(exprValue=="MIN(")||(exprValue=="_exprText_"))
		document.forma.colType.value = document.forma.dbColType.value;
	else
		document.forma.colType.value = "<%= AppConstants.CT_NUMBER %>";
	
	if(document.forma.colType.value!=oldColType) {
		// Updating displayFormat select list
		document.forma.displayFormat.options.length = 0;
		
		if(document.forma.colType.value=="<%= AppConstants.CT_NUMBER %>") {
		<% for(int i=0; i<numberFormats.length; i++) { %>
			document.forma.displayFormat.options[<%= i %>] = new Option("<%= numberFormats[i] %>", "<%= numberFormats[i] %>");
		<%  } %>
		} else if(document.forma.colType.value=="<%= AppConstants.CT_DATE %>") {
		<% for(int i=0; i<dateFormats.length; i++) { %>
			document.forma.displayFormat.options[<%= i %>] = new Option("<%= dateFormats[i] %>", "<%= dateFormats[i] %>");
		<%  } %>
		} else {
		<% for(int i=0; i<charFormats.length; i++) { %>
			document.forma.displayFormat.options[<%= i %>] = new Option("<%= charFormats[i] %>", "<%= charFormats[i].equals("N/A")?"":charFormats[i] %>");
		<%  } %>
		}	// else
		
		document.forma.displayFormat.selectedIndex = 0;
<%	if(! isCrossTab) { %>
		setTotalDropDownValues(document.forma.colType.value=="<%= AppConstants.CT_NUMBER %>");
<%  } %>
		if(document.layers)
			history.go(0);
	}	// if
}   // exprFormulaChange

<% if(! isEdit) { %>
function columnDetailsChange() {
	var selText  = "";
	selText  = document.forma.columnDetails.options[document.forma.columnDetails.selectedIndex].text;
	document.forma.displayName.value = selText.substr(selText.indexOf('.')+1);
	
	var selValue = "";
	selValue = document.forma.columnDetails.options[document.forma.columnDetails.selectedIndex].value;
	document.forma.dbColType.value  = selValue.substr(selValue.lastIndexOf('|')+1);
	
	exprFormulaChange(false);
	
	document.forma.displayFormat.selectedIndex = 0;
}   // columnDetailsChange
<% } %>
	
function showMapPopup() {
	var colType = document.forma.colType.value;
	var displayName = document.forma.displayName.value;
	var displayFormat = document.forma.displayFormat.options[document.forma.displayFormat.selectedIndex].value;
<% if(isEdit) { %>
	var colName = "<%= currColumn.getDbColName() %>";
	var tableId = "<%= rdef.getColumnTableById(currColumn.getColId()).getTableId() %>";
<% } else { %>
	var selValue = "";
	selValue = document.forma.columnDetails.options[document.forma.columnDetails.selectedIndex].value;
	var colName = selValue.substr(selValue.indexOf('|')+1, selValue.lastIndexOf('|')-selValue.indexOf('|')-1);
	var tableId = selValue.substr(0, selValue.indexOf('|'));
<% } %>
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.map&colName="+colName+"&colType="+colType+"&displayName="+escape(displayName)+"&displayFormat="+escape(displayFormat)+"&tableId="+tableId, "mapPopup", "width=400,height=400,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showMapPopup
<% }	// if(! isSQLBased) 
%>

function verifyCrossTabSemaphore() {
<%	if(isCrossTab) { %>
	if(	(document.forma.crossTabValue.options[document.forma.crossTabValue.selectedIndex].value!="<%= AppConstants.CV_VALUE %>")
		&&(document.forma.semaphore.selectedIndex!=0)) {
			alert("You cannot assign Advanced Formatting to this column unless the Column Usage in Cross-Tab is Report values.");
			document.forma.semaphore.selectedIndex = 0;
			return false;
		}
<%	} %>
	return true;
}	// verifyCrossTabSemaphore
	
function showSemaphorePopup() {
	var semaphoreId = "";
	semaphoreId = document.forma.semaphore.options[document.forma.semaphore.selectedIndex].value;
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.semaphore&semaphoreId="+semaphoreId, "semaphorePopup", "width=720,height=400,location=no,menubar=no,toolbar=no,status=yes,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showSemaphorePopup
	
function updateSemaphoreList(semId, semName) {	// Returns the position of the current semaphore in the list
	var lSize  = 0;
	var selIdx = 0;
	lSize  = document.forma.semaphore.options.length;
	selIdx = document.forma.semaphore.selectedIndex;	
	for(var i=0; i<lSize; i++)
		if(document.forma.semaphore.options[i].value==semId) {
			if(document.forma.semaphore.options[i].text!=semName) {
				document.forma.semaphore.options[i] = new Option(semName, semId);
				document.forma.semaphore.selectedIndex = selIdx;
			}	// if
			
			return i;
		}	// if
	
	document.forma.semaphore.options[lSize] = new Option(semName, semId);
	return lSize;
}   // updateSemaphoreList
	
function showSemaphoreImportPopup() {
	if(document.forma.drillDownCtl.selectedIndex==0||document.forma.drillDownCtl.selectedIndex<=tableDrillDownCount) {
		alert("You need to select a report from the list, and then click this button\n"+
			  "to import all the Advanced Display Formattings from selected report into\n"+
			  "this report. No Advanced Display Formattings have been imported.");
		return;
	}	// if
	
	var ddValue = "";
	ddValue = document.forma.drillDownCtl.options[document.forma.drillDownCtl.selectedIndex].value;
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.import.semaphore&<%= AppConstants.RI_REPORT_ID %>="+ddValue, "semaphoreImportPopup", "width=400,height=250,location=no,menubar=no,toolbar=no,status=yes,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showSemaphoreImportPopup

function showImageOptions() {
	var selectionType= document.forma.anchor.options[document.forma.anchor.selectedIndex].value;
	if (selectionType == "IMAGE") {
	 //document.getElementById("imageSelectionDiv").style.display='block';
	} else {
		 document.getElementById("imageSelectionDiv").style.display='none';
	}
}

function showDisplayFormats() {
	
	var colType= document.forma.colType.options[document.forma.colType.selectedIndex].value;
	if (colType == "<%=AppConstants.CT_HYPERLINK%>") {
		document.getElementById("dataFormatDiv").style.display='none';
		//document.getElementById("hyperlinkDiv").style.display='block';
	} else {
		document.getElementById("hyperlinkDiv").style.display='none';
		//document.getElementById("dataFormatDiv").style.display='block';
	}
	var dataFormat = document.forma.colDataFormat; 
	dataFormat.options.length = 0; 

	dataFormat.options[dataFormat.options.length] = new Option('Select', -1); 
	if (colType == "<%=AppConstants.CT_NUMBER%>") { 
		dataFormat.options[dataFormat.options.length] = new Option('9999999990','9999999990'); 
		dataFormat.options[dataFormat.options.length] = new Option('9,999,999,990','9,999,999,990'); 
		dataFormat.options[dataFormat.options.length] = new Option('9999999990.99','9999999990.99'); 
		dataFormat.options[dataFormat.options.length] = new Option('9,999,999,990.99','9,999,999,990.99'); 
		dataFormat.options[dataFormat.options.length] = new Option('$9,999,999,990.99','$9,999,999,990.99'); 
		dataFormat.options[dataFormat.options.length] = new Option('$9,999,999,990.999','$9,999,999,990.999'); 
		var lSize = dataFormat.options.length;
		for(var i=0; i<lSize; i++) {
			if(dataFormat.options[i].value=="<%=currColumn.getColFormat()%>") {
				dataFormat.options[i].selected = true;
			}
		}
		if(document.getElementById("filterDateColumn")) document.getElementById("filterDateColumn").style.display = "none";

	} 
	if (colType == "<%=AppConstants.CT_DATE%>") { 
		dataFormat.options[dataFormat.options.length] = new Option('MM/DD/YYYY','MM/DD/YYYY'); 
		dataFormat.options[dataFormat.options.length] = new Option('MM/YYYY','MM/YYYY'); 
		dataFormat.options[dataFormat.options.length] = new Option('DD-MON-YYYY','DD-MON-YYYY'); 
		dataFormat.options[dataFormat.options.length] = new Option('Month DD, YYYY','Month DD, YYYY'); 
		dataFormat.options[dataFormat.options.length] = new Option('Month, YYYY','Month, YYYY'); 
		dataFormat.options[dataFormat.options.length] = new Option('MM/DD/YYYY HH24:MI:SS','MM/DD/YYYY HH24:MI:SS'); 
		dataFormat.options[dataFormat.options.length] = new Option('YYYY/MM/DD HH24:MI:SS','YYYY/MM/DD HH24:MI:SS'); 
		dataFormat.options[dataFormat.options.length] = new Option('YYYY','YYYY'); 
		var lSize = dataFormat.options.length;
		for(var i=0; i<lSize; i++) {
			if(dataFormat.options[i].value=="<%=currColumn.getColFormat()%>") {
				dataFormat.options[i].selected = true;
			}
		}
		//if(document.getElementById("filterDateColumn")) document.getElementById("filterDateColumn").style.display = "block";

	} 
	if (colType == "<%=AppConstants.CT_CHAR%>") { 
		dataFormat.options[dataFormat.options.length] = new Option('N/A','N/A'); 
		if(document.getElementById("filterDateColumn")) document.getElementById("filterDateColumn").style.display = "none";
	} 
	
	
}
//-->
</script>

<table width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %> - <%= curSubStep %></b></td>
	</tr>
<% if(isSQLBased) { %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Column ID: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<%= currColumn.getColId() %>
			</font></td>
	</tr>
	<tr>
	  <td class=rbg2 align="right" width="25%" height="30" style="background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Depends on formfield: </font></td> 
	   <td class=rbg3 align="left" width="50%"><font class=rtabletext>
	      <input type="text" style="width: 200px;" name="dependsOnFormField" value="<%= nvl(dependsOnFormField,"")%>"></input>	</tr>
<% }	// if(! isSQLBased) 
%>
<% if(isCrossTab) { %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Column Usage in Cross-Tab: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<select name="crossTabValue"onChange="setTotalDropDownValues(this.options[this.selectedIndex].value=='<%= AppConstants.CV_VALUE %>')">
				<option value="<%= AppConstants.CV_ROW %>"<%= isEdit?(nvl(currColumn.getCrossTabValue()).equals(AppConstants.CV_ROW)?" selected":""):" selected" %>><%= rdef.getCrossTabDisplayValue(AppConstants.CV_ROW) %>
				<option value="<%= AppConstants.CV_COLUMN %>"<%= (isEdit&&nvl(currColumn.getCrossTabValue()).equals(AppConstants.CV_COLUMN))?" selected":"" %>><%= rdef.getCrossTabDisplayValue(AppConstants.CV_COLUMN) %>
				<option value="<%= AppConstants.CV_VALUE %>"<%= (isEdit&&nvl(currColumn.getCrossTabValue()).equals(AppConstants.CV_VALUE))?" selected":"" %>><%= rdef.getCrossTabDisplayValue(AppConstants.CV_VALUE) %>
				<option value=""<%= (isEdit&&(nvl(currColumn.getCrossTabValue()).length()==0))?" selected":"" %>>Invisible/Filter
			</select></font></td>
	</tr>
<% } %>
<% if(! isSQLBased) { %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Table Column: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
		<% if(isEdit) { %>
			<%= nvl(rdef.getColumnLabel(currColumn), currColumn.getDbColName()) /*currColumn.getColName()*/ %>
		<% } else { %>
			<select name="columnDetails" onChange="columnDetailsChange();">
<%				    int icnt = 0;
					String remoteDbPrefix = dbInfo;
					for(Iterator iter=rdef.getDataSourceList().getDataSource().iterator(); iter.hasNext(); ) { 
						DataSourceType dst = (DataSourceType) iter.next();
						
						Vector dbColumns = DataCache.getReportTableDbColumns(dst.getTableName().toUpperCase(),remoteDbPrefix);
						if(dbColumns!=null)
							for(int i=0; i<dbColumns.size(); i++) {
								DBColumnInfo dbCol = (DBColumnInfo) dbColumns.get(i);
								//if(dst.getTableName().toUpperCase().equals(dbCol.getTableName())) { 
%>
		    				    <option value="<%= dst.getTableId() %>|<%= dbCol.getColName() %>|<%= dbCol.getColType() %>"<%= (icnt==0)?" selected":"" %>>[<%= dst.getDisplayName() %>].<%= dbCol.getLabel() %>
<%									if(icnt==0) {
										dispName  = dbCol.getLabel();
										dbColType = dbCol.getColType();
										colType   = dbColType;
									}
									icnt++;
								//}   // if 
							}   // for 
					}   // for 
%>
			</select>
		<% } %>
		<input type="hidden" name="dbColType" value="<%= dbColType %>">
		<input type="hidden" name="colType" value="<%= colType %>">
		</font></td>
	</tr> 
<% }	// if(! isSQLBased) 
%>

<% if(isSQLBased) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Datatype: </font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
 	    <select name="colType" style="width: 100px;" onChange="showDisplayFormats();">
		  	<option value="0"> Select</option>
		  	<option value="<%=AppConstants.CT_NUMBER%>" <%=currColumn.getColType().equals(AppConstants.CT_NUMBER)?" selected": ""%>> Number </option>
		  	<option value="<%=AppConstants.CT_DATE%>" <%=currColumn.getColType().equals(AppConstants.CT_DATE)?" selected": ""%>> Date</option>
		  	<option value="<%=AppConstants.CT_CHAR%>" <%=currColumn.getColType().equals(AppConstants.CT_CHAR)?" selected": ""%>> Character</option>
		  	<option value="<%=AppConstants.CT_HYPERLINK%>" <%=currColumn.getColType().equals(AppConstants.CT_HYPERLINK)?" selected": ""%>> Hyperlink</option>
 	   </select>
 	   </font>
 	   </td>
 	</tr>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Group By Pos: </font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
 	    <select name="groupByPos" style="width: 100px;">
		  	<option value="0"> Select</option>
		  	<option value="1" <%=(currColumn.getGroupByPos()!=null && currColumn.getGroupByPos()==1)?" selected": ""%>> 1 </option>
 	   </select>
 	   </font>
 	   </td>
 	</tr>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Custom Text for Sub-Total: </font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
 	    <font class=rtabletext>
			<input type="text" size="30" maxlength="500" style="width: 100px;" class=rtabletext  name="subTotalCustomText" value="<%= isEdit?(currColumn.getSubTotalCustomText()!=null?currColumn.getSubTotalCustomText():"Sub Total"):"Sub Total" %>"/> 	  
	   </font>
 	   </font>
 	   </td>
 	</tr>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Hide Repeated Values : </font></td>
			<td class=rbg3 width="50%" align="left" nowrap><font class=rtabletext>
	 	    <input type="checkbox" name="hideRepeatedKeys" value="Y" <%=(currColumn.isHideRepeatedKey()!=null && currColumn.isHideRepeatedKey().booleanValue())?" checked":"" %>>
	 	   </font>
	 	   </td>
 	</tr>


	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Multi group Column Level: </font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
 	    <select name="multiGroupColLevel" style="width: 100px;">
		  	<option value="0"> Select</option>
		  	<option value="-1" <%=(currColumn.getLevel()!=null && currColumn.getLevel()==-1)?" selected": ""%>> AUXILIARY-COLUMN </option>
		  	<option value="1" <%=(currColumn.getLevel()!=null && currColumn.getLevel()==1)?" selected": ""%>> 1 </option>
		  	<option value="2" <%=(currColumn.getLevel()!=null && currColumn.getLevel()==2)?" selected": ""%>> 2 </option>
		  	<option value="3" <%=(currColumn.getLevel()!=null && currColumn.getLevel()==3)?" selected": ""%>> 3 </option>
		  	<option value="4" <%=(currColumn.getLevel()!=null && currColumn.getLevel()==4)?" selected": ""%>> 4 </option>
 	   </select>
 	   </font>
 	   </td>
 	</tr>

	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Multi group Column Range : </font>
				<font class=rtabletext>Colspan : </font>
		</td> 
		<td class=rbg3 align="left" nowrap>
		<font class=rtabletext>
			<input type="text" size="30" style="width: 100px;" maxlength="500" class=rtabletext  name="colspan" value="<%= isEdit?(currColumn.getColspan()!=null?currColumn.getColspan():""):"" %>"/> 	  
			
	   </font>
 	   </td>
 	</tr>

	<tr id ="dataFormatDiv">
	    <td colspan="2" class=rbg2> 	
		<table width="100%" cellspacing="1" cellpadding="3" align=center>
		<tr>
			<td class=rbg2 width="25%" align="right" height="30"><font class=rtabletext>Dataformat: </font></td> 
			<td class=rbg3 width="50%" align="left" nowrap><font class=rtabletext>
	 	    <select name="colDataFormat" style="width: 100px;">
	 	   </select>
	 	   </font>
	 	   </td>
	 	</tr>
	 <% if(!isCrossTab) { %>	   
		<tr id="filterDateColumn">
			<td class=rbg2 width="25%" align="right" height="30"><font class=rtabletext>Enhanced Pagination: </font></td> 
			<td class=rbg3 width="50%" align="left" nowrap><font class=rtabletext>
	 	    <input type="checkbox" name="enhancedPagination" value="Y" <%=(currColumn.isEnhancedPagination()!=null && currColumn.isEnhancedPagination().booleanValue())?" checked":"" %> />
	 	   </font>
	 	   </td>
	 	</tr>
 	<%} %>   
	 	
		</table>
		</td>
 	</tr>
 	<tr id ="hyperlinkDiv">   
	  <td colspan="2">
	    <table  width="100%" cellspacing="1" cellpadding="3" align=center>
		<tr>
			<td class=rbg2 width="25%" align="right" height="30"><font class=rtabletext>URL: </font></td> 
			<td class=rbg3 width="50%" align="left" nowrap><font class=rtabletext>
			  <input type="text" size="30" maxlength="500" class=rtabletext  name="hyperlinkURL" value="<%= isEdit?currColumn.getHyperlinkURL():"" %>"/>
	 	   </font>
	 	   </td>
	 	</tr>

		<tr>
			<td class=rbg2 width="25%" align="right" height="30"><font class=rtabletext>Anchor: </font></td> 
			<td class=rbg3 width="50%" align="left" nowrap><font class=rtabletext>
	 	    <select name="anchor" onChange="showImageOptions()">
			  	<option value="0"> Select</option>
			  	<option value="VALUE" <%=nvl(currColumn.getHyperlinkType()).equals("VALUE")?" selected": ""%>> Value Of the Column </option>
			  	<option value="IMAGE" <%=nvl(currColumn.getHyperlinkType()).equals("IMAGE")?" selected": ""%>> IMAGE</option>
	 	   </select>
	 	   </font>
	 	   </td>
	 	</tr>
		  <tr id="imageSelectionDiv" style="display:none;"> 
			<td class=rbg2 width="25%" height="30" align="right">
				<font class=rtabletext>Select Image for anchor: </font>
			</td> 
			<td width="50%" align="left">
				<font class=rtabletext>
		  <select id = "actionImg" name="actionImg" >
		    <option value=''> <!-- SELECT --></option>
		  <%
		        if(!actionImgMap.isEmpty()) {
				for( Iterator itr=actionImgMap.entrySet().iterator(); itr.hasNext(); ) {
					Map.Entry e = (Map.Entry)itr.next();
					String image_id = (String)e.getKey();
					String image_loc = (String)e.getValue();
           %>
           <%
			if (nvl(AppUtils.getRequestNvlValue(request, "actionImg")).length()>0 && !(AppUtils.getRequestNvlValue(request, "pdfImg").equals(currColumn.getActionImg())) ) {
				if(image_loc .equals (AppUtils.getRequestNvlValue(request, "actionImg"))) {
			%>
			
			<option value='<%= image_loc %>' selected> <%=image_id %></option>
			<%	
				} else {
            %> 					
					<option value='<%= image_loc %>'> <%=image_id %></option>
			<%		
           		} 
			} else {
				if(image_loc .equals (currColumn.getActionImg())) {
				%>
				
				<option value='<%= image_loc %>' selected> <%=image_id %> </option>
				<%	
				
			    } else {
		            %> 					
					<option value='<%= image_loc %>'> <%=image_id %></option>
			<%		
			    }
			}
           %>

		   <% }
		     }
		   %>
           </select>
		   </font>
           </td>		   
		  </tr>
	 	 </table>
         </td>		 
		  </tr>
<% } %>
	<tr>
		<td class=rbg2 align="right" height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Display Name: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<input type="text" style="width: 100px;" size="30" maxlength="60" class=rtabletext name="displayName" value="<%= isEdit?currColumn.getDisplayName():dispName %>"></font></td>
	</tr>
<% if(! isSQLBased) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Display Format: </font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<!--input type="text" size="20" maxlength="30" name="displayFormat" value="< %= isEdit?nvl(currColumn.getColFormat()):"" % >" onFocus="blur();"></font>
		    <a href="javascript:showFormatPopup()"><img border="0" src="<%= AppUtils.getImgFolderURL() %>shareicon.gif" alt="Select from list" width="12" height="12"></a-->
			<select name="displayFormat" style="width: 100px;" >
			<%  String[] fmt;
				if(colType.equals(AppConstants.CT_NUMBER)) 
					fmt = numberFormats;
				else if(colType.equals(AppConstants.CT_DATE)) 
					fmt = dateFormats;
				else 
					fmt = charFormats; 
				for(int i=0; i<fmt.length; i++) { %>
				<option value="<%= fmt[i].equals("N/A")?"":(fmt[i].startsWith("Month")?("fm"+fmt[i]):fmt[i]) %>"<%= (isEdit&&(nvl(currColumn.getColFormat()).toUpperCase().equals(fmt[i].toUpperCase())||("fm"+nvl(currColumn.getColFormat())).toUpperCase().equals(fmt[i].toUpperCase())))?" selected":"" %>><%= fmt[i] %>
			<%  } %>
			</select></font></td>
	</tr>
<% }	// if(! isSQLBased) 
%>

<% if (false) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Display Width: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
		<% if (currColumn.getDisplayWidth()<=0) { currColumn.setDisplayWidth(10); } %>
			<select name="displayWidth" style="width: 100px;"  onKeyDown="fnKeyDownHandler(this, event);" onKeyUp="fnKeyUpHandler_A(this, event); return false;" onKeyPress = "return fnKeyPressHandler_A(this, event);"  onChange="fnChangeHandler_A(this, event);">
				<option value="" style="COLOR:#ff0000;BACKGROUND-COLOR:#ffff00;">Custom</option> <!-- This is the Editable Option -->
				<option value="10"<%=  (isEdit&&(currColumn.getDisplayWidth()<=0 || currColumn.getDisplayWidth()==10))?"":" selected" %>>10%
				<option value="20"<%=  (isEdit&&(currColumn.getDisplayWidth()==20))?" selected":""  %>>20%
				<option value="30"<%=  (isEdit&&(currColumn.getDisplayWidth()==30))?" selected":""  %>>30%
				<option value="40"<%=  (isEdit&&(currColumn.getDisplayWidth()==40))?" selected":""  %>>40%
				<option value="50"<%=  (isEdit&&(currColumn.getDisplayWidth()==50))?" selected":""  %>>50%
				<option value="60"<%=  (isEdit&&(currColumn.getDisplayWidth()==60))?" selected":""  %>>60%
				<option value="70"<%=  (isEdit&&(currColumn.getDisplayWidth()==70))?" selected":""  %>>70%
				<option value="80"<%=  (isEdit&&(currColumn.getDisplayWidth()==80))?" selected":""  %>>80%
				<option value="90"<%=  (isEdit&&(currColumn.getDisplayWidth()==90))?" selected":""  %>>90%
				<option value="100"<%= (isEdit&&(currColumn.getDisplayWidth()==100))?" selected":"" %>>100%
				<% if(!((currColumn.getDisplayWidth()%10 == 0) && (currColumn.getDisplayWidth() > 100)) ) {
				%>
					<option value="<%=currColumn.getDisplayWidth()%>" selected><%=currColumn.getDisplayWidth()%>%</option> 
                <%				
					 } 
                %>						
				
			</select></font></td>
	</tr>
	<% } %>
	<% System.out.println("WidthInPxls " + currColumn.getDisplayWidthInPxls()); %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Display Width (In Pxls): </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
						<input type="text" style="width: 100px;" class="rtabletext" size="40" maxlength="100" id="widthInPxls" name="widthInPxls" value="<%= (nvl(AppUtils.getRequestNvlValue(request, "widthInPxls")).length() > 0)? 
			(!(AppUtils.getRequestNvlValue(request, "widthInPxls").equals(currColumn.getDisplayWidthInPxls()))?
                            AppUtils.getRequestNvlValue(request, "widthInPxls"):currColumn.getDisplayWidthInPxls()):
                            	currColumn.getDisplayWidthInPxls() %>">

</font></td>
	</tr>	
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>No Wrap ? </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="nowrap" style="width: 100px;" >
				<option value="Y"<%= isEdit?(nvl(currColumn.getNowrap(),"N").startsWith("Y")?" selected":(!nvl(currColumn.getNowrap(),"False").startsWith("N") && org.openecomp.portalsdk.analytics.system.Globals.IsGlobalNoWrap()?" selected": "")):"" %>>Yes
				<option value="N"<%= (isEdit&&(nvl(currColumn.getNowrap(),"False").startsWith("N")))?" selected":"" %>>No
			</select></font></td>
	</tr>
	
	<tr>
		<td class=rbg2 height="30" align="right"><font class=rtabletext>Indent Parameter to display value </font>
		</td> 
		<td class=rbg3 align="left">
			<select name="indentation" style="width: 100px;" >
			    <option value="0"> Select</option>
				<option value="1" <%= AppUtils.getRequestNvlValue(request, "indentation").equals("1") ? " selected":((AppUtils.getRequestNvlValue(request, "indentation").length()<=0)?(currColumn.getIndentation()!=null && currColumn.getIndentation().intValue()==1?" selected":""):"") %>> 1 </option>
				<option value="2" <%= AppUtils.getRequestNvlValue(request, "indentation").equals("2") ? " selected":((AppUtils.getRequestNvlValue(request, "indentation").length()<=0)?(currColumn.getIndentation()!=null && currColumn.getIndentation().intValue()==2?" selected":""):"") %>> 2 </option>
				<option value="3" <%= AppUtils.getRequestNvlValue(request, "indentation").equals("3") ? " selected":((AppUtils.getRequestNvlValue(request, "indentation").length()<=0)?(currColumn.getIndentation()!=null && currColumn.getIndentation().intValue()==3?" selected":""):"") %>> 3 </option>				
			</select>
		</td>
	</tr>
		
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Display Alignment: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="displayAlign" style="width: 100px;" >
				<option value="Left"<%=   isEdit?(nvl(currColumn.getDisplayAlignment(), "Left").equals("Left")?" selected":""):" selected" %>>Left
				<option value="Center"<%= (isEdit&&nvl(currColumn.getDisplayAlignment()).equals("Center"))?" selected":""                  %>>Center
				<option value="Right"<%=  (isEdit&&nvl(currColumn.getDisplayAlignment()).equals("Right"))?" selected":""                   %>>Right
			</select></font></td>
	</tr>

	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Display (Header) Alignment: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="displayHeaderAlign" style="width: 100px;" >
				<option value="Left"<%=   isEdit?(nvl(currColumn.getDisplayHeaderAlignment(), "Left").equals("Left")?" selected":""):" selected" %>>Left
				<option value="Center"<%= (isEdit&&nvl(currColumn.getDisplayHeaderAlignment()).equals("Center"))?" selected":""                  %>>Center
				<option value="Right"<%=  (isEdit&&nvl(currColumn.getDisplayHeaderAlignment()).equals("Right"))?" selected":""                   %>>Right
			</select></font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Sortable? </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="sortable" style="width: 100px;" >
				<option value="N"<%= isEdit?((currColumn.isIsSortable()!=null && !currColumn.isIsSortable())?" selected":""):" selected" %>>No
				<option value="Y"<%= (isEdit&&currColumn.isIsSortable()!=null && currColumn.isIsSortable())?" selected":""        %>>Yes
			</select></font></td>
	</tr>
	

	<% if(! isCrossTab) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Visible? </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="visible" style="width: 100px;" >
				<option value="Y"<%= isEdit?(currColumn.isVisible()?" selected":""):" selected" %>>Yes
				<option value="N"<%= (isEdit&&(! currColumn.isVisible()))?" selected":""        %>>No
			</select></font></td>
	</tr>
        <% if(! isSQLBased) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Group By? </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="groupBreak" style="width: 100px;" >
				<option value="Y"<%= (isEdit&&currColumn.isGroupBreak())?" selected":""            %>>Yes
				<option value="N"<%= isEdit?(currColumn.isGroupBreak()?"":" selected"):" selected" %>>No
			</select></font></td>
	</tr>
	<% } %>
        <% } %>
	<!--tr class=rbg2>
		<td class=rbg2  height="30" align="right" width="25%"><font face="Arial, Helvetica, sans-serif" size="1" class=rtabletext>Column Type </font></td> 
		<td align="left" width="50%" class=rbg3><font face="Arial, Helvetica, sans-serif" size="1" class=rtabletext>
			<select name="calculated">
				<option value="N"< %= (isEdit&&currColumn.isCalculated())?"":" selected" % >>Table Column
				<option value="Y"< %= (isEdit&&currColumn.isCalculated())?" selected":"" % >>Expression
			</select></font></td>
	</tr-->
        <% if(! isSQLBased) { %>  
<%
boolean isOtherExpr = isEdit&&currColumn.isCalculated()&&
						(! nvl(currColumn.getColName()).startsWith("SUM( "))&&
						(! nvl(currColumn.getColName()).startsWith("MAX( "))&&
						(! nvl(currColumn.getColName()).startsWith("MIN( "))&&
						(! nvl(currColumn.getColName()).startsWith("COUNT(*)"))&&
						(! nvl(currColumn.getColName()).startsWith("COUNT(ALL "))&&
						(! nvl(currColumn.getColName()).startsWith("COUNT(DISTINCT "))&&
						(! nvl(currColumn.getColName()).startsWith("AVG(ALL "))&&
						(! nvl(currColumn.getColName()).startsWith("AVG(DISTINCT "))&&
						(! nvl(currColumn.getColName()).startsWith("STDDEV(ALL "))&&
						(! nvl(currColumn.getColName()).startsWith("STDDEV(DISTINCT "))&&
						(! nvl(currColumn.getColName()).startsWith("VARIANCE(ALL "))&&
						(! nvl(currColumn.getColName()).startsWith("VARIANCE(DISTINCT "));
%>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Expression: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="exprFormula" onChange="exprFormulaChange(true);" style="width: 100px;" >
				<option value=""<%=                  (isEdit&&currColumn.isCalculated())?"":" selected"                                                                %>>
				<option value="SUM("<%=              (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("SUM( "))?" selected":""              %>>Sum
				<option value="MAX("<%=              (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("MAX( "))?" selected":""              %>>Max
				<option value="MIN("<%=              (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("MIN( "))?" selected":""              %>>Min
				<option value="COUNT(*)"<%=          (isEdit&&currColumn.isCalculated()&&(nvl(currColumn.getColName()).startsWith("COUNT(*)")||nvl(currColumn.getColName()).startsWith("COUNT(ALL ")))?" selected":"" %>>Count All
				<option value="COUNT(DISTINCT"<%=    (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("COUNT(DISTINCT "))?" selected":""    %>>Count Distinct
				<option value="AVG(ALL"<%=           (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("AVG(ALL "))?" selected":""           %>>Average All
				<option value="AVG(DISTINCT"<%=      (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("AVG(DISTINCT "))?" selected":""      %>>Average Distinct
				<option value="STDDEV(ALL"<%=        (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("STDDEV(ALL "))?" selected":""        %>>Standard Deviation All
				<option value="STDDEV(DISTINCT"<%=   (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("STDDEV(DISTINCT "))?" selected":""   %>>Standard Deviation Distinct
				<option value="VARIANCE(ALL"<%=      (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("VARIANCE(ALL "))?" selected":""      %>>Variance All
				<option value="VARIANCE(DISTINCT"<%= (isEdit&&currColumn.isCalculated()&&nvl(currColumn.getColName()).startsWith("VARIANCE(DISTINCT "))?" selected":"" %>>Variance Distinct
				<option value="_exprText_"<%=        isOtherExpr?" selected":""                                                                                        %>>---------- Other ----------
			</select></font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Expression Other: </font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<input type="text" size="30" name="exprText"<%= isOtherExpr?" value=\""+nvl(currColumn.getColName())+"\"":" value=\"\" disabled" %> onFocus="if(document.forma.exprFormula.options[document.forma.exprFormula.selectedIndex].value!='_exprText_') blur();"></font>
		    <a href="javascript:showMapPopup()"><img border="0" src="<%= AppUtils.getImgFolderURL() %>shareicon.gif" alt="Define custom values mapping" width="12" height="12"></a>&nbsp;
		    <a href="javascript:showFormFieldPopup()"><font class=rtabletext>Form Fields</font></a>
		</td>
	</tr>
<% }	// if(! isSQLBased) 
%>
<%-- if(! isCrossTab) { --%>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Drill-down Link: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<input type="hidden" name="drillDownURL" value="<%= isEdit?nvl(currColumn.getDrillDownURL()):"" %>">
			<input type="hidden" name="drillDownParams" value="<%= isEdit?nvl(currColumn.getDrillDownParams()):"" %>">
			<input type="hidden" name="drillDownSuppress" value="<%= isEdit?nvl(currColumn.getDrillDownType()):"" %>">
			<input type="hidden" name="drillDownRequest" value="<%= isEdit?getRequestParam(currColumn.getDrillDownParams()):"" %>">
			<input type="hidden" name="drillDownPopUp" value="<%= isEdit?(currColumn.isDrillinPoPUp()!=null?currColumn.isDrillinPoPUp():""):"" %>">
			<select name="drillDownCtl" onChange="showDrillDownPopup(true)" style="width: 300px;" >
				<option value=""<%= (isEdit&&nvl(currColumn.getDrillDownURL()).length()>0)?"":" selected" %>>----- No Drill-down -----
	<%	int tableDrillDownCount = 0;
		if(! isCrossTab)
			for(int i=0; i<reportTableSources.size(); i++) {
				TableSource tableSource = (TableSource) reportTableSources.get(i);
				if(nvl(tableSource.getViewAction()).length()>0&&rdef.getTableByDBName(tableSource.getTableName())!=null) { 
					tableDrillDownCount++; %>
					<option value="<%= tableSource.getViewAction() %>"<%= (isEdit&&nvl(currColumn.getDrillDownURL()).equals(AppUtils.getBaseActionURL()+tableSource.getViewAction()))?" selected":"" %>><%= tableSource.getDisplayName() %> Record Details
	<%			}	// if
		 	}	// for

		Vector publicReportIdNames = DataCache.getPublicReportIdNames();
		for(int i=0; i<publicReportIdNames.size(); i++) { 
			IdNameValue reportIdName = (IdNameValue) publicReportIdNames.get(i); %>
				<option value="<%= reportIdName.getId() %>"<%= (isEdit&&nvl(currColumn.getDrillDownURL()).equals(reportIdName.getId()))?" selected":"" %>>Public Report: <%= reportIdName.getName() %>
	<%	} %>:
	
	<%
	//if(!AppUtils.isSuperUser(request)) {
	Vector groupReportIdNames = DataCache.getGroupAccessibleReportIdNames(AppUtils.getUserID(request),AppUtils.getUserRoles(request));
		for(int j=0; j<groupReportIdNames.size(); j++) { 
			IdNameValue reportIdName = (IdNameValue) groupReportIdNames.get(j); %>
				<option value="<%= reportIdName.getId() %>"<%= (isEdit&&nvl(currColumn.getDrillDownURL()).equals(reportIdName.getId()))?" selected":"" %>>Group Report: <%= reportIdName.getName() %>
	    <% } %>			
	 <%// } 
         %>   
	<%
	//if(!AppUtils.isSuperUser(request)) {
	Vector privateReportIdNames = DataCache.getPrivateAccessibleReportIdNames(AppUtils.getUserID(request),AppUtils.getUserRoles(request));
		for(int j=0; j<privateReportIdNames.size(); j++) { 
			IdNameValue reportIdName = (IdNameValue) privateReportIdNames.get(j); %>
				<option value="<%= reportIdName.getId() %>"<%= (isEdit&&nvl(currColumn.getDrillDownURL()).equals(reportIdName.getId()))?" selected":"" %>>Private Report: <%= reportIdName.getName() %>
	    <% } %>			
	 <% // } 
%>   
			</select></font>
			<a href="javascript:showDrillDownPopup(false)"><img border="0" src="<%= AppUtils.getImgFolderURL() %>shareicon.gif" alt="Set new parameters configuration" width="12" height="12"></a>
			&nbsp;&nbsp;
			<a href="javascript:showSemaphoreImportPopup()"><img border="0" src="<%= AppUtils.getImgFolderURL() %>lookup_arrow.gif" alt="Import advanced formatting from selected report" width="17" height="17"></a>
		</td>
	</tr>
<script language="JavaScript">
<!--
	tableDrillDownCount = <%= tableDrillDownCount %>;
//-->
</script>
	<%	String curSemId = "";
		if(isEdit)
			curSemId = nvl(currColumn.getSemaphoreId()); %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Advanced Display Formatting: </font></td> 
		<td class=rbg3 align="left" nowrap><font class=rtabletext>
			<select name="semaphore"" onChange="verifyCrossTabSemaphore()" style="width: 300px;" >
				<option value=""<%= (curSemId.length()==0)?" selected":"" %>>----- Do Not Use Advanced Formatting -----
	<%	if(rdef.getSemaphoreList()!=null)
			for(Iterator iter=rdef.getSemaphoreList().getSemaphore().iterator(); iter.hasNext(); ) {
				SemaphoreType semaphore = (SemaphoreType) iter.next(); %>
				<option value="<%= semaphore.getSemaphoreId() %>"<%= curSemId.equals(semaphore.getSemaphoreId())?" selected":"" %>><%= semaphore.getSemaphoreName() %>
	<%		}	// for 
	%>
			</select></font>
			<input type="hidden" name="semaphoreTypeHidden" value=""/>
			<a href="javascript:showSemaphorePopup()"><img border="0" src="<%= AppUtils.getImgFolderURL() %>shareicon.gif" alt="Define advanced formatting" width="12" height="12"></a>
		</td>
	</tr>
<%-- } --%>

<%	boolean canHaveTotal = false; 
    	if(isSQLBased) {
		//canHaveTotal = (! isCrossTab);
		canHaveTotal = (!isCrossTab)||(isEdit&&nvl(currColumn.getCrossTabValue()).equals(AppConstants.CV_VALUE));
	} else  {
		if(isCrossTab) 
			canHaveTotal = (isEdit&&nvl(currColumn.getCrossTabValue()).equals(AppConstants.CV_VALUE));
		else
			canHaveTotal = colType.equals(AppConstants.CT_NUMBER);
	}
	
	String colTotalRow = "";
	String colTotal    = isEdit?nvl(currColumn.getDisplayTotal()):"";
	if(isCrossTab&&colTotal.indexOf('|')>=0) {
		colTotalRow = colTotal.substring(colTotal.indexOf('|')+1);
		colTotal    = colTotal.substring(0, colTotal.indexOf('|'));
	} %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Total for <%= isCrossTab?"each":"the" %> column: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="displayTotal" style="width: 300px;" >
				<option value=""<%= (colTotal.length()>0)?"":" selected" %>>--- Do Not Display (Faster Report Execution) ---
			<%	if(canHaveTotal) {
					for(int i=0; i<AppConstants.TOTAL_FUNCTIONS.getCount(); i++) { 
						IdNameValue tValue = AppConstants.TOTAL_FUNCTIONS.getValue(i); %>
				<option value="<%= tValue.getId() %>"<%= (colTotal.startsWith(tValue.getId()))?" selected":"" %>><%= tValue.getName() %>
			<%  	}	// for
				} // if  
                        %>
			</select></font></td>
	</tr>
<%	if(isCrossTab) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Total for each row </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="displayTotalPerRow">
				<option value=""<%= (colTotalRow.length()>0)?"":" selected" %>>--- Do Not Display (Faster Report Execution) ---
			<%	if(canHaveTotal) {
					for(int i=0; i<AppConstants.TOTAL_FUNCTIONS.getCount(); i++) { 
						IdNameValue tValue = AppConstants.TOTAL_FUNCTIONS.getValue(i); %>
				<option value="<%= tValue.getId() %>"<%= (colTotalRow.startsWith(tValue.getId()))?" selected":"" %>><%= tValue.getName() %>
			<%  	}	// for
				} // if  
			%>
			</select></font></td>
	</tr>
<%	}	// if(isCrossTab) 
%>
<% if(isSQLBased) { %>
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>&nbsp; </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<input type="Checkbox" name="no_parse_date" value="Y"<%= (isEdit&&rdef.getColumnNoParseDateFlag(currColumn))?" checked":"" %>>
			Do not attempt to parse values as date</font></td>
	</tr>
<% } %>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
<%	if(isCrossTab) {
		/*if(rdef.getCrossTabColColumns().size()>0) { 
			// Col headings column already defined
		}*/ %>
		
	if(document.forma.crossTabValue.options[document.forma.crossTabValue.selectedIndex].value=="<%= AppConstants.CV_VALUE %>") {
<%		DataColumnType valueCol = rdef.getCrossTabValueColumn();
		if((valueCol!=null)&&((currColumn==null)||(! currColumn.getColId().equals(valueCol.getColId())))) { 
			// Report data column already defined 
%>
		alert("There is a column already designated for the report values.\nYou cannot have more than one column for that purpose.\nPlease change the Column Usage in Cross-Tab.");
		document.forma.crossTabValue.focus();
		
		return false;
<%		} else if(! isSQLBased) { %>
		if(document.forma.exprFormula.selectedIndex==0) {
			alert("The column designated for the report values must be an expression.\nPlease change the Column Type and select Expression.");
			document.forma.exprFormula.focus();
			
			return false;
		}
<%		} %>
	} else {
		verifyCrossTabDrillDown();
		verifyCrossTabSemaphore();
	}
<%	} %>

if(document.forma.displayWidth!=null && document.forma.displayWidth.options.selectedIndex == 0) {
	if(!checkNonNegativeInteger(document.forma.displayWidth.options[document.forma.displayWidth.options.selectedIndex].text)) {
		alert("Please enter positive number greater than 1% in \"Display Width\". No Characters are allowed.");
		return false;
	} else {
/*				if(eval(document.forma.displayWidth.options[document.forma.displayWidth.options.selectedIndex].text) < 10) {
			alert("Please enter positive number less than 10% in \"Desired Container Height\". No Characters are allowed.");
			return false;
		}
*/				
		if(eval(document.forma.displayWidth.options[document.forma.displayWidth.options.selectedIndex].text) > 100) {
			alert("Please enter positive number less than 100% in \"Display Width\". No Characters are allowed.");
			return false;
		}
		document.forma.displayWidth.options[document.forma.displayWidth.options.selectedIndex].value=document.forma.displayWidth.options[document.forma.displayWidth.options.selectedIndex].text;
	}
}

	if(document.forma.displayName.value=="")
	<% if(isEdit) { %>
		document.forma.displayName.value = "<%= currColumn.getDisplayName() %>";
	<% } else { %>
		document.forma.displayName.value = document.forma.tableName.options[document.forma.tableName.selectedIndex].text.substr(document.forma.tableName.options[document.forma.tableName.selectedIndex].text.indexOf('.')+1);
	<% } %>

<% if(reportCols.size()>0) { %>
	if(false
<% 		for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
		    DataColumnType dc = (DataColumnType) iter.next();
			
			if(! (isEdit&&dc.getColId().equals(currColumn.getColId()))) { %>
			    ||(document.forma.displayName.value=="<%= dc.getDisplayName() %>")
<% 		    }
		} %>
	   ) {
		alert("A column with display name "+document.forma.displayName.value+" already exists.\nPlease select another name.");
		document.forma.displayName.focus();
		document.forma.displayName.select();
		
		return false;
	}
<% } %>
 
	return true;
}   // dataValidate
//-->
showDisplayFormats();
showImageOptions();
</script> 

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
	private String getRequestParam(String s) {
		if(nvl(s).equals("")) return s;
		else {
			String requestParam="";
			int pos = 0;
			int iCnt = 0;
			while(s.indexOf("#",pos)!=-1) {
				iCnt++;
				if(iCnt>1) requestParam += "|";
				pos = s.indexOf("#",pos)+1;
				requestParam += s.substring(s.indexOf("#")+1,s.indexOf("]",pos));
			}
			return requestParam;
		}

	} 
%>
