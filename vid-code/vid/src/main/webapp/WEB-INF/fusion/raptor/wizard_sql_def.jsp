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
<%@ page import="org.openecomp.portalsdk.analytics.model.base.OrderBySeqComparator" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.TableSource" %>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();
	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED); 
   	
%>
<script language="JavaScript">
<!--
function addText(newText) {
	document.forma.reportSQL.value   += (""+newText+" ");
	document.forma.sqlValidated.value = "N";
}   // addText

function getSelectedTableName() {
	var tableName = "";
	tableName = document.forma.dbTables.options[document.forma.dbTables.selectedIndex].value;
	return tableName;
}   // getSelectedTableName

function addTable() {
	addText(getSelectedTableName());
}   // addTable

function showTableColsPopup() {
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.table.cols&<%= AppConstants.RI_TABLE_NAME %>="+getSelectedTableName(), "tableColsPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showTableColsPopup

function showTestRunSQLPopup() {
	//var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.testrun.sql&<%= AppConstants.RI_FORMATTED_SQL %>="+escape(""+document.forma.reportSQL.value), "testRunSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.test.jsp", "testRunSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showTestRunSQLPopup

function showFormFieldPopup() {
	var w = window.open("", "fieldPopup", "width=400,height=400,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
	
	w.document.writeln("<html><head>");
	w.document.writeln("<title>Form Fields</title>");
	w.document.writeln("<link rel=stylesheet type=text/css href='<%= AppUtils.getBaseFolderURL() %>css/raptor.css'>");
	w.document.writeln("<script language=JavaScript>");
	w.document.writeln("function setValue(newValue) {");
	w.document.writeln("    window.opener.addText(newValue);");
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
	w.document.writeln("	<tr class=rbg8 height=30>");
	w.document.writeln("		<td colspan=2 align=center valign=middle><b class=rtabletext>There are no form fields defined</b></td>");
	w.document.writeln("	</tr>");
	w.document.writeln("	<tr>");
	w.document.writeln("		<td colspan=2 align=center><br><button type='submit' onClick=\"window.close();\"  att-button btn-type=\"primary\" size=\"small\" title='Save'>Close</button></td>");
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
//-->
</script>

<table width="100%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr>
		<td class=rbg3 width="60%" align="left" valign="top">
			&nbsp;<font class=rtabletext>Report SQL: </font><br>
			<%	boolean sqlValidated = (nvl(AppUtils.getRequestValue(request, "sqlValidated"), nvl(rdef.getReportSQL())).length()>0); 
				if(request.getAttribute(AppConstants.RI_ERROR_LIST)!=null)
					sqlValidated = false;
					
				String  sql = nvl(rdef.getReportSQL(), "SELECT ");
				if(! sqlValidated) 
					sql = nvl(AppUtils.getRequestValue(request, "reportSQL"), sql);	%>
			<input type="hidden" id="sqlValidated" name="sqlValidated" value="<%= sqlValidated?"Y":"N" %>">
			&nbsp;<textarea name="reportSQL" cols="155" rows="32" onChange="document.getElementById('sqlValidated').value='N';" style="height: 400px;"><%= sql %></textarea>
		</td>
		<td class=rbg2 width="40%" align="left" valign="top" nowrap>
			&nbsp;<font class=rtabletext>Keyword Assistance </font><br>
			<b class=rtabletext>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('SELECT')">SELECT</a>&nbsp;&nbsp;<a href="javascript:addText('DISTINCT')">DISTINCT</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('FROM')">FROM</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('WHERE')">WHERE</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('GROUP BY')">GROUP BY</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('HAVING')">HAVING</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('ORDER BY')">ORDER BY</a>&nbsp;&nbsp;<a href="javascript:addText('ASC')">ASC</a>&nbsp;&nbsp;<a href="javascript:addText('DESC')">DESC</a>&nbsp;<br>
				<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('UNION')">UNION</a>&nbsp;&nbsp;<a href="javascript:addText('ALL')">ALL</a>&nbsp;&nbsp;<a href="javascript:addText('INTERSECT')">INTERSECT</a>&nbsp;&nbsp;<a href="javascript:addText('MINUS')">MINUS</a>&nbsp;<br>
				<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('AND')">AND</a>&nbsp;&nbsp;<a href="javascript:addText('OR')">OR</a>&nbsp;&nbsp;<a href="javascript:addText('NOT')">NOT</a>&nbsp;&nbsp;<a href="javascript:addText('EXISTS')">EXISTS</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('IS')">IS</a>&nbsp;&nbsp;<a href="javascript:addText('NULL')">NULL</a>&nbsp;&nbsp;<a href="javascript:addText('IN')">IN</a>&nbsp;&nbsp;<a href="javascript:addText('BETWEEN')">BETWEEN</a>&nbsp;<br>
				<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('COUNT(')">COUNT(</a>&nbsp;&nbsp;<a href="javascript:addText('SUM(')">SUM(</a>&nbsp;&nbsp;<a href="javascript:addText('AVG(')">AVG(</a>&nbsp;&nbsp;<a href="javascript:addText('MAX(')">MAX(</a>&nbsp;&nbsp;<a href="javascript:addText('MIN(')">MIN(</a>&nbsp;<br>
				<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('NVL(')">NVL(</a>&nbsp;&nbsp;<a href="javascript:addText('DECODE(')">DECODE(</a>&nbsp;&nbsp;<a href="javascript:addText('SYSDATE')">SYSDATE</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('TO_CHAR(')">TO_CHAR(</a>&nbsp;&nbsp;<a href="javascript:addText('TO_NUMBER(')">TO_NUMBER(</a>&nbsp;&nbsp;<a href="javascript:addText('TO_DATE(')">TO_DATE(</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('TRUNC(')">TRUNC(</a>&nbsp;&nbsp;<a href="javascript:addText('ROUND(')">ROUND(</a>&nbsp;&nbsp;<a href="javascript:addText('ABS(')">ABS(</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('SUBSTR(')">SUBSTR(</a>&nbsp;&nbsp;<a href="javascript:addText('REPLACE(')">REPLACE(</a>&nbsp;&nbsp;<a href="javascript:addText('LOWER(')">LOWER(</a>&nbsp;&nbsp;<a href="javascript:addText('UPPER(')">UPPER(</a>&nbsp;<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addText('LTRIM(')">LTRIM(</a>&nbsp;&nbsp;<a href="javascript:addText('RTRIM(')">RTRIM(</a>&nbsp;&nbsp;<a href="javascript:addText('LPAD(')">LPAD(</a>&nbsp;&nbsp;<a href="javascript:addText('RPAD(')">RPAD(</a>&nbsp;<br>
			</b>
		</td> 
	</tr>
	<tr>
		<td class=rbg2 height="40" align="left" valign="Middle" nowrap>
			<font class=rtabletext style="display: none">
				&nbsp;Tables:
				<select name="dbTables" style="width: 200px">
				<%	Vector reportTableSources = DataCache.getReportTableSources((String) session.getAttribute("remoteDB"));
					for(int i=0; i<reportTableSources.size(); i++) { 
						TableSource tableSource = (TableSource) reportTableSources.get(i); %>
					<option value="<%= tableSource.getTableName() %>"><%= tableSource.getTableName() %>
				<%	} %>
				</select>
				<button type="submit" onClick="addTable()" att-button btn-type="primary" size="small" title='Add Table'>Add Table</button>
				<button type="submit" onClick="showTableColsPopup()" att-button btn-type="primary" size="small" title='Columns'>Columns</button>
				<button type="submit" onClick="showFormFieldPopup()" att-button btn-type="primary" size="small" title='Form Fields'>Form Fields</button>
				&nbsp;
			</font>
		</td>
		<td class=rbg2 align="left" valign="Middle" nowrap>
			<font class=rtabletext>&nbsp;
				<button type="submit" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_VALIDATE %>';" att-button btn-type="primary" size="small" title='Validate SQL'>Validate SQL</button>
				&nbsp;
				<button type="Button" onClick="showTestRunSQLPopup()" att-button btn-type="primary" size="small" title='Test Run SQL'>Test Run SQL</button>
				&nbsp;
			</font>
		</td>
	</tr>
	<tr>
		<td class=rbg1 colspan=2 height="30" align="center" valign="Middle"><b class=rtableheader>You need to click the &quot;Validate SQL&quot; button in order to store the SQL before going forward</b></td>
	</tr>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
	if(document.getElementById("sqlValidated") && document.getElementById("sqlValidated").value!="Y") {
	<% if(nvl(rdef.getReportSQL()).length()>0) { %>
		return confirm("The changes made to the SQL have not been validated.\nYou need to click the \"Validate SQL\" button in order to do that.\nDo you want to continue now and lose the changes made to the SQL after the last validation?");
	<% } else { %>
		alert("The SQL has not been validated.\nPlease click the \"Validate SQL\" button in order to do that.\nYou cannot go forward without SQL validation.");
		return false;
	<% } %>
	}   // if
	
	return true;
}   // dataValidate
//-->
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
