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
<%@page import="org.openecomp.portalsdk.analytics.xmlobj.ReportMap"%>
<%
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
   	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED);    
	ReportMap repMap = rdef.getReportMap();
	String addressColumn = "";
	String latColumn = "";
	String longColumn = "";
	String legendColumn = "";
	String colorColumn = "";
	String dataColumn = "";
	String isMapAllowed = "";
	String addAddress = "N";
	String useDefaultSize = "";
	String width = "";
	String height = "";

	int reportMapSize = 0;
	if (repMap != null){
		if (repMap.getAddressColumn() != null)
			addressColumn = repMap.getAddressColumn();
		
		if (repMap.getDataColumn() != null)
			dataColumn = repMap.getDataColumn();
		if (repMap.getIsMapAllowedYN() != null)
			isMapAllowed = repMap.getIsMapAllowedYN();
		if (repMap.getAddAddressInDataYN() != null)
			addAddress = repMap.getAddAddressInDataYN();
		if (repMap.getLatColumn() != null)
			latColumn = repMap.getLatColumn();
		if (repMap.getLongColumn() != null)
			longColumn = repMap.getLongColumn();
		if (repMap.getColorColumn() != null)
			colorColumn = repMap.getColorColumn();
		if (repMap.getLegendColumn() != null)
			legendColumn = repMap.getLegendColumn();
		if (repMap.getUseDefaultSize() != null)
			useDefaultSize = repMap.getUseDefaultSize();
		if (repMap.getHeight() != null)
			height = repMap.getHeight();
		if (repMap.getWidth() != null)
			width = repMap.getWidth();
		
		reportMapSize = repMap.getMarkers().size();
	}
	
%>
<%@page import="org.openecomp.portalsdk.analytics.xmlobj.Marker"%>

<script>
function enable_text(status)
{
//status=!status; 
document.forma.height.disabled = status;
document.forma.width.disabled = status;
}

function isNumberKey(evt)
{

  if (document.forma.useDefaultSize.checked==true)
  {
	  document.forma.height.disabled = status;
	  document.forma.width.disabled = status;
	  return false;
  }
			
   var charCode = (evt.which) ? evt.which : event.keyCode
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;

   return true;
}


function checkValue()
{
	   
	   if (document.forma.isMapAllowed.checked==false)
	   {
		  return true;
	   }
	   if (document.forma.useDefaultSize.checked==true)
	   {
		  return true;
	   }
	   if ((document.forma.height.value < 1) )
	   {
		   alert(document.forma.height.name + ' can not be zero');
		   document.forma.height.focus(); return false;
	   } 
	   if ((document.forma.width.value < 1) )
	   {
		   alert(document.forma.width.name + ' can not be zero');
		   document.forma.width.focus(); return false;
	   } 
	   return true;
}

</script>
<table class=mTAB width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=8 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr class=rbg1>
		
	</tr>
	<tr>
		<td align="left" width="10%">Map Enabled ?</td>
		<td colspan=8>
			<input id = "isMapAllowed" name="isMapAllowed" type="checkbox" value="Y" <%if(isMapAllowed != null && isMapAllowed.equals("Y")){ %> checked <%} %>/>
		</td>
	</tr>
	
		<tr>
		<td align="left" width="10%">Default Size ?</td>
		<td width="15%">
			<input id = "useDefaultSize" name="useDefaultSize" type="checkbox" 
			 onclick="enable_text(this.checked)" 
			value="Y" <%if(useDefaultSize != null && useDefaultSize.equals("Y")){ %> checked <%} %>/>
		</td>
		
		<td align="left" width="10%">Height </td>
		<td width="15%">
			<input id = "height" name="height" onkeypress="return isNumberKey(event)"   type="textbox" value="<%=height%> "   />
			<!-- 
			<input id = "dummy" name=""dummy" type="textbox"  visible="false"  />
			 -->
			 <input type="hidden" name="checkbox" value="checked">
		</td>
		
		<td align="left" width="10%">Width </td>
		<td width="15%">
			<input id = "width" name="width"  onkeypress="return isNumberKey(event)"    type="textbox" value="<%=width%> "    />
		</td>
		<td colspan="2">&nbsp;</td>
	</tr>
	
	
	<tr>
		<td align="left" width="10%">Lat Column</td>
		<td width="15%">
			<select name="latColumn">
				<option value=""> --Select-- </option>
<%
				int iCount = 0;
				List reportCols = rdef.getAllColumns();
				Iterator iter = reportCols.iterator();
				for(; iter.hasNext(); iCount++) { 
					DataColumnType dct = (DataColumnType) iter.next();
%>				
					<option value="<%= dct.getColId() %>" <%if(latColumn.equals(dct.getColId())){ %> selected <%}%> ><%= dct.getDisplayName() %></option>
<%
				}
%>
			</select>
		
		
		</td>
		
		<td align="left" width="10%">Long Column</td>
		<td width="15%">
			<select name="longColumn">
				<option value=""> --Select-- </option>
<%
				iCount = 0;
				iter = reportCols.iterator();
				for(; iter.hasNext(); iCount++) { 
					DataColumnType dct = (DataColumnType) iter.next();
%>				
					<option value="<%= dct.getColId() %>" <%if(longColumn.equals(dct.getColId())){ %> selected <%}%> ><%= dct.getDisplayName() %></option>
<%
				}
%>
			</select>
		
		
		</td>
		<td align="left" width="10%">Color Column</td>
		<td width="15%">
			<select name="colorColumn">
				<option value=""> --Select-- </option>
<%
				iCount = 0;
				iter = reportCols.iterator();
				for(; iter.hasNext(); iCount++) { 
					DataColumnType dct = (DataColumnType) iter.next();
%>				
					<option value="<%= dct.getColId() %>" <%if(colorColumn.equals(dct.getColId())){ %> selected <%}%> ><%= dct.getDisplayName() %></option>
<%
				}
%>
			</select>
		
		
		</td>
		<td align="left" width="10%">Legend Column</td>
		<td width="15%">
			<select name="legendColumn">
				<option value=""> --Select-- </option>
<%
				iCount = 0;
				iter = reportCols.iterator();	
				for(; iter.hasNext(); iCount++) { 
					DataColumnType dct = (DataColumnType) iter.next();				
%>
					<option value="<%= dct.getColId() %>" <%if(legendColumn.equals(dct.getColId())){ %> selected <%}%> ><%= dct.getDisplayName() %></option>
<%
				}
%>
			</select>
		
		
		</td>
		
	</tr>
	<tr>
		<td align="left" width="10%">&nbsp;</td>
		<td colspan=7>
			<input type="button" value="Add Marker Details" onclick="javascript:createClicked();"/>
		</td>
	</tr>
	<input type="hidden" name="markerCount" id="markerCount" value="<%=reportMapSize%>" />
	<tr style="display:none;" id="cloneableRow">
				  	
		<td align="left" width="10%">Data Header </td>
		<td width="15%">
			<input type="text" name="dataHeader" value="" />
		
		</td>
		<td align="left" width="10%">Display Column</td>
		<td width="15%">
			<select name="dataColumn">
				<option value=""> --Select-- </option>
<%
				iCount = 0;
				iter = reportCols.iterator();	
				for(; iter.hasNext(); iCount++) { 
					DataColumnType dct = (DataColumnType) iter.next();				
%>
					<option value="<%= dct.getColId() %>" <%if(dataColumn.equals(dct.getColId())){ %> selected <%}%> ><%= dct.getDisplayName() %></option>
<%
				}
%>
			</select>
		
		
		</td>		
		<td width="5%">
			<img onclick="javascript:deleteRow(this);" height="12" alt="Remove" width="12" src="<%=AppUtils.getImgFolderURL()%>deleteicon.gif" border="0" value="" />
		</td>
	    <td colspan="3">&nbsp;</td>
	</tr>
	
	
<%		for (int i = 1; repMap != null && i < repMap.getMarkers().size(); i ++){ 
		Marker marker = (Marker) repMap.getMarkers().get(i);
%>
		<tr>
		<td align="left" width="10%">Data Header</td>
		<td width="15%">
			<input type="text" name="dataHeader<%=i%>" value="<%=marker.getDataHeader()%>" />
		</td>
		
		<td align="left" width="10%">Display Column</td>
		<td width="15%">
			<select name="dataColumn<%=i%>">
				<option value=""> --Select-- </option>
<%
				iCount = 0;
				iter = reportCols.iterator();	
				for(; iter.hasNext(); iCount++) { 
					DataColumnType dct = (DataColumnType) iter.next();				
%>
					<option value="<%= dct.getColId() %>" <%if(marker.getDataColumn().equals(dct.getColId())){ %> selected <%}%> ><%= dct.getDisplayName() %></option>
<%
				}
%>
			</select>
		
		
		</td>
		<td width="5%">
			<img onclick="javascript:deleteRow(this);" height="12" alt="Remove" width="12" src="<%=AppUtils.getImgFolderURL()%>deleteicon.gif" border="0" value="" />
		</td>
		<td colspan="3">&nbsp;</td>
		</tr>
		<%}%>

	
	
</table>
<br>

<script >

function createClicked() {
	var alreadyEditable = "New";
	
	var row = document.getElementById('cloneableRow');
	var newR = duplicateRowByIndex(row, 0);
	var tbl = getParentTable(newR);
	var editIndex = 0;
	if (document.all.markerCount != null || document.all.markerCount.value != ""){
		editIndex = document.all.markerCount.value;
	}
	if (tbl.rows.length%2 == 0){
		newR.className="alternateRow";
	}
	for (var xx = 0; xx < newR.cells.length; xx ++){
		for (var yy=0; yy < newR.cells[xx].childNodes.length; yy++){
			if (newR.cells[xx].childNodes[yy] != null 
				&& newR.cells[xx].childNodes[yy].name){
				newR.cells[xx].childNodes[yy].name = "" + newR.cells[xx].childNodes[yy].name + editIndex;
				newR.cells[xx].childNodes[yy].id = newR.cells[xx].childNodes[yy].name ;
			}
		}
	}
	editIndex = parseInt(editIndex) + parseInt(1);
	document.all.markerCount.value = editIndex;
	newR.style.display = "block";
	//document.getElementById('scrollableTable').scrollTop =0;
    return false;
}  

function deleteRow(item){
	var row = getParentRow(item);
	var tab = getParentTable(row);
	tab.deleteRow(row.rowIndex);
	
}

duplicateRowByIndex = function()
{
	if(arguments.length == 0 ) { return };
	pRow = arguments[0];
	var tbl = getParentTable(pRow);
	var newRowIndex = 1 ;
	if(arguments[1] && parseInt(arguments[1]) != NaN )
	{
		newRowIndex = parseInt(arguments[1]);
		if(newRowIndex == 0 ){ newRowIndex = tbl.rows.length - 1 ; }
	}

	if(pRow == null) { return ; }
	var oldRow = pRow;
	var newRow = oldRow.cloneNode(true);
	if (arguments[1] == 0){
		oldRow.parentNode.insertBefore(newRow, oldRow.nextSibling);
	}
	else{
		oldRow.parentNode.insertBefore(newRow, tbl.rows[newRowIndex]);
	}
	newRow.id = "";
	return newRow;
}

function getParentRow(cell)
{
	var pNode = cell.parentNode;
	for( var xx = 0 ; xx < 10 ; xx++)
	{
		if(pNode.cells) { return pNode;}
		else { pNode = pNode.parentNode; }
	}
	alert("Unable to find parent row"); return false;
}

function getParentTable(row)
{
	var pNode = row.parentNode;
	for( var xx = 0 ; xx < 10 ; xx++)
	{

		if(pNode.rows && (pNode.tagName == "TABLE" || pNode.tagName == "table")) {return pNode;}
		else { pNode = pNode.parentNode; }
	}
	alert("Unable to find parent table"); return false;
}



function dataValidate() {
	return checkValue();
	//return true;
}

</script>




<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
