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
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.TableJoin" %>
<%@ page import="org.openecomp.portalsdk.analytics.error.UserDefinedException"%>

<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
	boolean isEdit = curSubStep.equals(AppConstants.WSS_EDIT);
	DataSourceType currTable = null;
	if(isEdit)
		currTable = rdef.getTableById(AppUtils.getRequestNvlValue(request, AppConstants.RI_DETAIL_ID));
	Vector reportTableSources = (isEdit)?DataCache.getReportTableSources((String) session.getAttribute("remoteDB")):DataCache.getReportTableSources(AppUtils.getUserRoles(request),((String) session.getAttribute("remoteDB")), AppUtils.getUserID(request), request);
	if(reportTableSources.size()<=0) {
        request.setAttribute(AppConstants.RI_EXCEPTION, new Exception("Please add table name to the raptor table for generating report"));
		throw new UserDefinedException("Please add table name to the raptor table for generating report");
	}
	Vector reportTableJoins   = (isEdit)?DataCache.getReportTableJoins():DataCache.getReportTableJoins(AppUtils.getUserRoles(request)); %>

<% if(! isEdit) { %>
<script language="JavaScript">
<!--
	var tablesExist = false;
	<% if(rdef.getDataSourceList().getDataSource().size()>0) { %>
		tablesExist = true;
	
	function setJoinTableName(tableId) {
		var selIndex = 0;
		for(var i=1; i<document.forma.joinTableName.options.length; i++)
			if(tableId==document.forma.joinTableName.options[i].value)
				selIndex = i;
	 
		document.forma.joinTableName.selectedIndex = selIndex;
	}   // setJoinTableName
	<% } %>
	
	function tableNameChange() {
		var newTableName = "";
		newTableName = document.forma.tableName.options[document.forma.tableName.selectedIndex].value;
		
		document.forma.displayName.value=document.forma.tableName.options[document.forma.tableName.selectedIndex].text;
		
		// Setting the PK values
		if(false) {}

	<%	for(int i=0; i<reportTableSources.size(); i++) { 
			TableSource tableSource = (TableSource) reportTableSources.get(i); %>
		else if(newTableName=="<%= tableSource.getTableName() %>") 
		    document.forma.tablePK.value = "<%= tableSource.getPkFields() %>";
	<%	} %>
		
	<% if(rdef.getDataSourceList().getDataSource().size()>0) {
			for(Iterator iter=rdef.getDataSourceList().getDataSource().iterator(); iter.hasNext(); ) { 
				DataSourceType dst = (DataSourceType) iter.next();
	
				for(int i=0; i<reportTableJoins.size(); i++) {
					TableJoin tableJoin = (TableJoin) reportTableJoins.get(i);
					if(dst.getTableName().equals(tableJoin.getSrcTableName())) { %>
		if(newTableName=="<%= tableJoin.getDestTableName() %>")
			setJoinTableName("<%= dst.getTableId() %>");
		else 
	<%              } else if(dst.getTableName().equals(tableJoin.getDestTableName())) { %>
		if(newTableName=="<%= tableJoin.getSrcTableName() %>")
			setJoinTableName("<%= dst.getTableId() %>");
		else 
	<% 		        }   // if
				}	// for
			} %>
			setJoinTableName("");
	<% } %>
		listJoinExpr(document.forma.tableName.options[document.forma.tableName.selectedIndex].value,
		             document.forma.joinTableName.options[document.forma.joinTableName.selectedIndex].value);
	}   // tableNameChange
	

//-->
</script >
<% } %>

<script language="JavaScript">
<!--
	//load table join info to an array of objects
	function table_join(src_table, dest_table, join_expr) {
		this.src_table = src_table;
		this.dest_table = dest_table;
		this.join_expr = join_expr;
	}
	
	var table_joins = new Array();
	
	<% for(int i=0; i<reportTableJoins.size(); i++) {
			TableJoin tableJoin = (TableJoin) reportTableJoins.get(i); %>
			table_joins[table_joins.length] = 
				new table_join("<%=tableJoin.getSrcTableName()%>","<%=tableJoin.getDestTableName()%>","<%=tableJoin.getJoinExpr()%>");
	<% } %>
	
	function table_id_name_mapping(table_id, table_name) {
		this.table_id = table_id;
		this.table_name = table_name;
	}
	
	var table_id_name_mappings = new Array();

	<% for(Iterator iter=rdef.getDataSourceList().getDataSource().iterator(); iter.hasNext(); ) { 
			DataSourceType dst = (DataSourceType) iter.next(); %>
			table_id_name_mappings["<%= dst.getTableId()%>"] = "<%= dst.getTableName()%>";
			table_id_name_mappings["<%= dst.getTableName()%>"] = "<%= dst.getTableId()%>"; 
	<% }	%>

	function listJoinExpr(tableName, joinTableId) {

		removeAllOptions(document.forma.joinExpr);
		var isJoinExprFound = false;
		
		<%if(isEdit) {%>
			addOption(document.forma.joinExpr, "" ,"No Change");
		<%}%>
		
		for ( var i=0; i<table_joins.length; i++) {
			if( (table_joins[i].src_table==tableName && table_joins[i].dest_table==table_id_name_mappings[joinTableId])
		     ||
			    (table_joins[i].dest_table==tableName && table_joins[i].src_table==table_id_name_mappings[joinTableId])
				)
			{
				addOption(document.forma.joinExpr, table_joins[i].join_expr ,table_joins[i].join_expr );
				isJoinExprFound = true;
			}
		}
		
		if(!isJoinExprFound)
		{		
			removeAllOptions(document.forma.joinExpr);
			addOption(document.forma.joinExpr, "" ,"No Join Condition Defined");
		}
	}
	
	function removeAllOptions(selectbox)
	{
		var i;
		for(i=selectbox.options.length-1;i>=0;i--)
		{
			selectbox.remove(i);
		}
	}
	
	function addOption(selectbox, value, text )
	{
		var optn = document.createElement("OPTION");
		optn.text = text;
		optn.value = value;
	
		selectbox.options.add(optn);
	}
-->	
</script>

<table width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %> - <%= curSubStep %></b></td>
	</tr>
	<tr>
		<td align="right" width="25%" class=rbg2 height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Table Name </font></td> 
		<td align="left" width="50%" class=rbg3><font class=rtabletext>
		<% if(isEdit) { 
				String tName = null;
				for(int i=0; i<reportTableSources.size(); i++) {
					TableSource tableSource = (TableSource) reportTableSources.get(i);
					if(currTable.getTableName().equals(tableSource.getTableName())) {
						tName = tableSource.getDisplayName();
						break;
					}	// if
				} %>
				<%= nvl(tName, currTable.getTableName()) %>
				<input type="hidden" name="tableName" value="<%=currTable.getTableName()%>">
		<% } else { %>
			<select name="tableName" class="rtabletext" onChange="tableNameChange()">
				<%	for(int i=0; i<reportTableSources.size(); i++) {
						TableSource tableSource = (TableSource) reportTableSources.get(i); %>
				    <option value="<%= tableSource.getTableName() %>"<%= (i==0)?" selected":"" %>><%= tableSource.getDisplayName() %>
				<%	} %>
			</select>
		<% } %>
			<input type="hidden" name="tablePK" value="<%= ((TableSource) reportTableSources.get(0)).getPkFields() %>">
		</font></td>
	</tr> 
	<tr>
		<td align="right" width="25%" class=rbg2 height="30"><font class=rtabletext>Display Name </font></td> 
		<td align="left" width="50%" class=rbg3><font class=rtabletext>
			<input type="text" class="rtabletext" size="30" maxlength="30" name="displayName" value="<%= isEdit?currTable.getDisplayName():((TableSource) reportTableSources.get(0)).getDisplayName() %>"></font></td>
	</tr>
<% if(rdef.getDataSourceList().getDataSource().size()>(isEdit?1:0)) { 
		String outerJoinType = (isEdit?rdef.getOuterJoinType(currTable):""); %>
	<tr>
		<td align="right" width="25%" class=rbg2 height="30"><font class=rtabletext>Join To Table </font></td> 
		<td align="left" width="50%" class=rbg3><font class=rtabletext>
		<% if(isEdit) { %>
			<% if(currTable.getRefTableId()==null){%>
					--- Table Not Joined ---
			<%} else { %>
					<%=rdef.getTableById(currTable.getRefTableId()).getDisplayName() %>
					</br>on :&nbsp;<%=currTable.getRefDefinition() %>
			<%} %>
			<input type="hidden" name="joinTableName" value="<%=currTable.getRefTableId()%>">
			
		<% } else { %>
			<select name="joinTableName" onChange="listJoinExpr();">
				<option value="" selected>--- Tables Not Joined ---
				<% for(Iterator iter=rdef.getDataSourceList().getDataSource().iterator(); iter.hasNext(); ) { 
						DataSourceType dst = (DataSourceType) iter.next(); %>
						<option value="<%= dst.getTableId() %>"><%= dst.getDisplayName() %>
				<% } %>
			</select>
		<% } %>
		</font></td>
	</tr> 
	
	<tr>
		<td align="right" width="25%" class=rbg2 height="30"><font class=rtabletext>All availabe Join Options </font></td> 
		<td align="left" width="50%" class=rbg3><font class=rtabletext>
			<select name="joinExpr">
				<option value="" selected>No Join Condition</option>
			</select>
		</font></td>
	</tr>
	<% if(isEdit) { %>
	<script language="JavaScript">
		listJoinExpr("<%=currTable.getTableName()%>","<%=currTable.getRefTableId()%>");
	</script>
	<%} %>
	
	<tr>
		<td align="right" width="25%" class=rbg2 height="30"><font class=rtabletext>Join Type </font></td> 
		<td align="left" width="50%" class=rbg3><font class=rtabletext>
			<select name="outerJoin">
				<option value=""<%= (outerJoinType.length()==0)?" selected":"" %>>Include only records present in both tables
				<option value="<%= AppConstants.OJ_CURRENT %>"<%= outerJoinType.equals(AppConstants.OJ_CURRENT)?" selected":"" %>>Include join table records without match in this table
				<option value="<%= AppConstants.OJ_JOINED %>"<%= outerJoinType.equals(AppConstants.OJ_JOINED)?" selected":"" %>>Include records from this table without match in the join table
			</select>
		</font></td>
	</tr> 
<% } %>
</table>
<br>

<script language="JavaScript">
<!--
function checkNotJoined() {
<% if(rdef.getDataSourceList().getDataSource().size()>(isEdit?1:0)) { %>
	if(document.forma.outerJoin.selectedIndex==0) {
	<% if(isEdit) { %>
		var selTable       = "<%= currTable.getTableName() %>";
		var selDisplayName = "<%= currTable.getDisplayName() %>";
	<% } else { %>
		var selTable       = document.forma.tableName.options[document.forma.tableName.selectedIndex].value;
		var selDisplayName = document.forma.tableName.options[document.forma.tableName.selectedIndex].text;
	<% } %>
		
		if(false
	<%	for(int i=0; i<reportTableSources.size(); i++) {
			TableSource tableSource = (TableSource) reportTableSources.get(i); 
			if(tableSource.getIsLargeData().equals("Y")) { %>
			||selTable=="<%= tableSource.getTableName() %>"
	<%		}	// if
		}	// for 
	%>
			) {
			alert("Table "+selDisplayName+" contains large amount of data\nand cannot be added unless it is joined to another table.\nPlease select a Join To Table from the list.");
			document.forma.outerJoin.focus();
			
			return false;
		}	// if
	}
<%	if(! isEdit) { %>
	else {	//  if(document.forma.outerJoin.selectedIndex>0)
		var selTable     = document.forma.tableName.options[document.forma.tableName.selectedIndex].value;
		var selJoinTblId = document.forma.outerJoin.options[document.forma.outerJoin.selectedIndex].value;
		var selJoinTable = "";
	<%	for(Iterator iter=rdef.getDataSourceList().getDataSource().iterator(); iter.hasNext(); ) { 
			DataSourceType dst = (DataSourceType) iter.next(); %>
		if(selJoinTblId=="<%= dst.getTableId() %>") 
			selJoinTable = "<%= dst.getTableName() %>";
	<%	}	// for
		for(Iterator iter=reportTableJoins.iterator(); iter.hasNext(); ) { 
			TableJoin tableJoin = (TableJoin) iter.next(); %>
		if((selTable=="<%= tableJoin.getSrcTableName() %>"&&selJoinTable=="<%= tableJoin.getDestTableName() %>")||
		   (selTable=="<%= tableJoin.getDestTableName() %>"&&selJoinTable=="<%= tableJoin.getSrcTableName() %>"))
			return true;
	<%	} %>
		
		alert("Table "+document.forma.tableName.options[document.forma.tableName.selectedIndex].text+
				" cannot be joined to table "+document.forma.outerJoin.options[document.forma.outerJoin.selectedIndex].text+
				".\nPlease select a different join.");
		return false;
	}	// if
<%	}	// if(! isEdit)
   } %>
	
	return true;
}	// checkNotJoined
	
function dataValidate() {
	if(document.forma.displayName.value=="")
	<% if(isEdit) { %>
		document.forma.displayName.value = "<%= currTable.getDisplayName() %>";
	<% } else { %>
		document.forma.displayName.value = document.forma.tableName.options[selectedIndex].text;
	<% } %>
	
	if(false
	<% for(Iterator iter=rdef.getDataSourceList().getDataSource().iterator(); iter.hasNext(); ) {
	    	DataSourceType dst = (DataSourceType) iter.next();
			
			if(! (isEdit&&dst.getTableId().equals(currTable.getTableId()))) { %>
				||document.forma.displayName.value=="<%= dst.getDisplayName() %>"
	<%      }
		} %>
	   ) {
			alert("Table with display name "+document.forma.displayName.value+" already exists.\nPlease select another name.");
			document.forma.displayName.focus();
			document.forma.displayName.select();
		
			return false;
		}
	
	if(! checkNotJoined())
		return false;
	
	return true;
}   // dataValidate
//-->
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
