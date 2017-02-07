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
<%@ page import="org.openecomp.portalsdk.analytics.model.base.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%	ReportDefinition rdef = (ReportDefinition) request.getSession().getAttribute(AppConstants.SI_REPORT_DEFINITION); 
	
	String semaphoreId = AppUtils.getRequestNvlValue(request, "semaphoreId");
	String semaphoreType = AppUtils.getRequestNvlValue(request, "semaphoreType");	
	SemaphoreType semaphore = rdef.getSemaphoreById(semaphoreId);
	String semaphoreName = null;
	List listColumns = rdef.getAllColumns();
	if(semaphore!=null)
		semaphoreName = semaphore.getSemaphoreName();
	else
		if(rdef.getSemaphoreList()!=null)
			semaphoreName = "Display Formatting "+(rdef.getSemaphoreList().getSemaphore().size()+1);
		else
			semaphoreName = "Display Formatting 1";	
	
	String submitBtn = AppUtils.getRequestNvlValue(request, "submit_btn"); %>

<html>
<head>
	<title>Advanced Display Formatting</title>
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">
	
<% if(submitBtn.startsWith("Save")) { %>
<script language=JavaScript>
<!--
	function updateOpenerList() {	// Returns the position of the current semaphore in the list
		return window.opener.updateSemaphoreList("<%= semaphoreId %>", "<%= semaphoreName %>");
	}	// updateOpenerList
	
	function saveAndClose() {
		var idx = 0;
		idx = updateOpenerList();
		window.opener.document.forma.semaphore.selectedIndex = idx;
		window.opener.document.forma.semaphoreTypeHidden.value='<%=semaphoreType%>';
		window.close();
	}	// saveAndClose
//-->
</script>
<% } %>
	
<% if(submitBtn.equals("Save")) { %>
</head>
<body onLoad="saveAndClose()">
	<b class=rerrortext>Please wait...</b>
<% } else { %>

<script language=JavaScript>
<!--
	function setBold(iCount) {
		var flag = document.dataform.boldChk[iCount].checked;
		document.dataform.bold[iCount].value = (flag?"Y":"N");
		document.getElementById("preview"+iCount).style.fontWeight = (flag?"bold":"normal");
	}   // setBold
	
	function setItalic(iCount) {
		var flag = document.dataform.italicChk[iCount].checked;
		document.dataform.italic[iCount].value = (flag?"Y":"N");
		document.getElementById("preview"+iCount).style.fontStyle = (flag?"italic":"normal");
	}   // setItalic
	
	function setUnderline(iCount) {
		var flag = document.dataform.underlineChk[iCount].checked;
		document.dataform.underline[iCount].value = (flag?"Y":"N");
		document.getElementById("preview"+iCount).style.textDecorationUnderline = flag;
	}   // setUnderline
	
	function setBgColor(iCount) {
		var value = document.dataform.bgColor[iCount].options[document.dataform.bgColor[iCount].selectedIndex].value;
		document.getElementById("preview"+iCount).style.backgroundColor = value;
	}   // setBgColor
	
	function setFontColor(iCount) {
		var value = document.dataform.fontColor[iCount].options[document.dataform.fontColor[iCount].selectedIndex].value;
		document.getElementById("preview"+iCount).style.color = value;
	}   // setFontColor
	
	function setFontFace(iCount) {
		var value = document.dataform.fontFace[iCount].options[document.dataform.fontFace[iCount].selectedIndex].value;
		document.getElementById("preview"+iCount).style.fontFamily = value;
	}   // setFontFace
	
	function setFontSize(iCount) {
		var value = document.dataform.fontSize[iCount].options[document.dataform.fontSize[iCount].selectedIndex].value;
		document.getElementById("preview"+iCount).style.fontSize = value+"px";
	}   // setFontSize
	
/*	function setAlignment(iCount) {
		var value = document.dataform.alignment[iCount].options[document.dataform.alignment[iCount].selectedIndex].value;
		document.getElementById("preview"+iCount).style.textAlign = value;
	}   // setAlignment
*/	
	var semaphoreIds   = new Array(<%= 2+((rdef.getSemaphoreList()==null)?0:rdef.getSemaphoreList().getSemaphore().size()) %>);
	var semaphoreNames = new Array(<%= 2+((rdef.getSemaphoreList()==null)?0:rdef.getSemaphoreList().getSemaphore().size()) %>);
	<%	int iCount = 0;
		if(rdef.getSemaphoreList()!=null)
			for(Iterator iter=rdef.getSemaphoreList().getSemaphore().iterator(); iter.hasNext(); iCount++) {
				SemaphoreType sem = (SemaphoreType) iter.next(); %>
	semaphoreIds[<%= iCount %>]   = "<%= sem.getSemaphoreId() %>";
	semaphoreNames[<%= iCount %>] = "<%= sem.getSemaphoreName() %>";
	<%		}	// for 
	%>
	semaphoreIds[<%= iCount %>]     = "-";
	semaphoreNames[<%= iCount++ %>] = "-";
	semaphoreIds[<%= iCount %>]     = "-";
	semaphoreNames[<%= iCount++ %>] = "-";
	
	function dataValidate() {
		if(document.dataform.semaphoreName.value=="") {
			alert("Please enter Display Name");
			document.forma.semaphoreName.focus();
			document.forma.semaphoreName.select();
			
			return false;
		}	// if
		
		for(var i=0; i<semaphoreIds.length; i++)
			if((document.dataform.semaphoreName.value==semaphoreNames[i])&&(semaphoreIds[i]!="<%= semaphoreId %>")) {
				alert("Advanced Formatting with that name already exists.\nPlease select another name");
				document.dataform.semaphoreName.focus();
				document.dataform.semaphoreName.select();
				
				return false;
			}   // if
		
		for(var i=0; i<document.dataform.lessThanValue.length; i++)
			for(var j=i+1; j<document.dataform.lessThanValue.length; j++)
				if(	document.dataform.lessThanValue[i].value!=""&&
					document.dataform.lessThanValue[j].value!=""&&
					document.dataform.lessThanValue[i].value==document.dataform.lessThanValue[j].value) {
					alert("You cannot have duplicate values in the list.\nPlease change one of the values ["+document.dataform.lessThanValue[j].value+"]");
					document.dataform.lessThanValue[j].focus();
					document.dataform.lessThanValue[j].select();
					
					return false;
				}   // if
		
		return true;
	}	// dataValidate
	
	function doInit() {
	<% if(submitBtn.startsWith("Save")) { %>
		updateOpenerList();
	<% } %>
		
<%	for(int i = 0; i<3+((semaphore==null)?2:semaphore.getFormatList().getFormat().size()); i++) { 
		FormatType ft = null;
		if(semaphore!=null&&i<semaphore.getFormatList().getFormat().size())
			ft = (FormatType) semaphore.getFormatList().getFormat().get(i);
		if(ft!=null) { %>
		setBold(<%= i %>);
		setItalic(<%= i %>);
		setUnderline(<%= i %>);
		setBgColor(<%= i %>);
		setFontColor(<%= i %>);
		setFontFace(<%= i %>);
		setFontSize(<%= i %>);
		//setAlignment(<%= i %>);
	<%	}	// if
	}	// for 
%>
	}   // doInit
//-->
</script>

</head>
<body onLoad="doInit()">

<form name="dataform" action="<%= AppUtils.getBaseURL() %>" method="post" onSubmit="return dataValidate()">
	<input type="hidden" name="action" value="raptor">
	<input type="hidden" name="<%= AppConstants.RI_ACTION %>" value="report.popup.semaphore.save">

<table class="mTAB" width=94% border=0 cellspacing=1 align=center>
	<tr class=rbg1>
		<td valign="middle" colspan="10" height="24"><b class=rtableheader>&nbsp;Advanced Display Formatting Definition</b></td>
	</tr>
	<tr>
		<td class=rbg2 colspan="4" align="right" height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;"><font class=rtabletext>Display Name: </font></td> 
		<td class=rbg3 colspan="6" align="left"><font class=rtabletext>
			<input type="hidden" name="semaphoreId" value="<%= semaphoreId %>">
			<input type="text" size="30" maxlength="30" name="semaphoreName" value="<%= semaphoreName %>"></font></td>
	</tr>
	<tr>
		<td class=rbg2 colspan="4" align="right" height="30"><font class=rtabletext>Apply Formatting To: </font></td> 
		<td class=rbg3 colspan="6" align="left"><font class=rtabletext>
			<%	String sValue = AppConstants.ST_CELL; 
				if(semaphore!=null)
					sValue = nvl(semaphore.getSemaphoreType(), AppConstants.ST_CELL); %>
			<select name="semaphoreType">
			  <%
				for (Iterator iterC = listColumns.iterator(); iterC.hasNext();) {
					DataColumnType dc = (DataColumnType) iterC.next();
					if(dc.isVisible()) {
			  %>
			     <option value="<%= AppConstants.ST_CELL+"|"+dc.getColId() %>"<%= sValue.equals(AppConstants.ST_CELL+"|"+dc.getColId() )?" selected":"" %>><%=dc.getColName() %>
			  <%			
 				    }
     			}
			  %>
				<option value="<%= AppConstants.ST_CELL %>"<%= sValue.equals(AppConstants.ST_CELL)?" selected":"" %>>Current Value Only
				<option value="<%= AppConstants.ST_ROW %>"<%=  sValue.equals(AppConstants.ST_ROW) ?" selected":"" %>>The Entire Row
			</select></font></td>
	</tr>
	<tr>
		<td colspan="10">&nbsp;</td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="middle" colspan=2 height="30"><b class=rtableheader>Column Value Is</b></td>
		<td align="center" valign="middle"><b class=rtableheader>Bold?</b></td>
		<td align="center" valign="middle"><b class=rtableheader>Italic?</b></td>
		<td align="center" valign="middle"><b class=rtableheader>Under-<br>line?</b></td>
		<td align="center" valign="middle"><b class=rtableheader>Background Color</b></td>
		<td align="center" valign="middle"><b class=rtableheader>Font Color</b></td>
		<td align="center" valign="middle"><b class=rtableheader>Font Face</b></td>
		<td align="center" valign="middle"><b class=rtableheader>Font Size</b></td>
		<!--td align="center" valign="middle"><b class=rtableheader>Alignment</b></td-->
		<td align="center" valign="middle"><b class=rtableheader>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Preview&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
	</tr>
<%	for(int i = 0; i<3+((semaphore==null)?2:semaphore.getFormatList().getFormat().size()); i++) { 
		FormatType ft = null;
		if(semaphore!=null&&i<semaphore.getFormatList().getFormat().size())
			ft = (FormatType) semaphore.getFormatList().getFormat().get(i); %>
	<tr<%= (i%2==0)?" class=rowalt1":" class=rowalt2" %>>
		<% if(i==0) { %>
		<td align="center" valign="middle" colspan=2 height="30">
			<font class=rtabletext>Any Other</font>
			<input type="hidden" name="formatId" value="<%= (ft==null)?"":ft.getFormatId() %>">
			<input type="hidden" name="expression" value="">
			<input type="hidden" name="lessThanValue" value="">
			<!-- <input type="hidden" name="anyFmt" value="Y"> -->
		</td>
		<% } else { %>
		<!-- <input type="hidden" name="anyFmt" value="N"> -->
		<td align="center" valign="middle" height="30">
			<input type="hidden" name="formatId" value="<%= (ft==null)?"":ft.getFormatId() %>">
			<%	sValue = "="; 
				if(ft!=null)
					sValue = nvl(ft.getExpression(), "="); %>
			<select name="expression">
				<option value="="<%=        sValue.equals("=") ?" selected":"" %>>=
				<option value="&lt;&gt;"<%= sValue.equals("<>")?" selected":"" %>>&lt;&gt;
				<option value="&gt;"<%=     sValue.equals(">") ?" selected":"" %>>&gt;
				<option value="&gt;="<%=    sValue.equals(">=")?" selected":"" %>>&gt;=
				<option value="&lt;"<%=     sValue.equals("<") ?" selected":"" %>>&lt;
				<option value="&lt;="<%=    sValue.equals("<=")?" selected":"" %>>&lt;=
			</select>
		</td>
		<td align="center" valign="middle">
			<input type="text" size="10" name="lessThanValue" value="<%= (ft==null)?"":ft.getLessThanValue() %>">
		</td>
		<% } %>
		<td align="center" valign="middle">
			<%	boolean bValue = false; 
				if(ft!=null)
					bValue = ft.isBold(); %>
			<input type="hidden" name="bold" value="<%= bValue?"Y":"N" %>">
			<input type="checkbox" name="boldChk" value="Y"<%= bValue?" checked":"" %> onClick="setBold(<%= i %>)">
		</td>
		<td align="center" valign="middle">
			<%	bValue = false; 
				if(ft!=null)
					bValue = ft.isItalic(); %>
			<input type="hidden" name="italic" value="<%= bValue?"Y":"N" %>">
			<input type="checkbox" name="italicChk" value="Y"<%= bValue?" checked":"" %> onClick="setItalic(<%= i %>)">
		</td>
		<td align="center" valign="middle">
			<%	bValue = false; 
				if(ft!=null)
					bValue = ft.isUnderline(); %>
			<input type="hidden" name="underline" value="<%= bValue?"Y":"N" %>">
			<input type="checkbox" name="underlineChk" value="Y"<%= bValue?" checked":"" %> onClick="setUnderline(<%= i %>)">
		</td>
		<td align="center" valign="middle">
			<%	sValue = ""; 
				if(ft!=null)
					sValue = nvl(ft.getBgColor()); %>
			<select name="bgColor" onChange="setBgColor(<%= i %>)">
				<option value=""<%=        sValue.equals("")       ?" selected":"" %>>
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
		</td>
		<td align="center" valign="middle">
			<%	sValue = ""; 
				if(ft!=null)
					sValue = nvl(ft.getFontColor()); %>
			<select name="fontColor" onChange="setFontColor(<%= i %>)">
				<option value=""<%=        sValue.equals("")       ?" selected":"" %>>
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
		</td>
		<td align="center" valign="middle">
			<%	sValue = ""; 
				if(ft!=null)
					sValue = nvl(ft.getFontFace()); %>
			<select name="fontFace" onChange="setFontFace(<%= i %>)">
				<option value=""<%=                                    sValue.equals("")                                   ?" selected":"" %>>--- Default ---
				<option value="Arial,Helvetica,sans-serif"<%=          sValue.equals("Arial,Helvetica,sans-serif")         ?" selected":"" %>>Arial
				<option value="Courier New,Courier,mono"<%=            sValue.equals("Courier New,Courier,mono")           ?" selected":"" %>>Courier New
				<option value="Geneva,Arial,Helvetica,sans-serif"<%=   sValue.equals("Geneva,Arial,Helvetica,sans-serif")  ?" selected":"" %>>Geneva
				<option value="Georgia,Times New Roman,Times,serif"<%= sValue.equals("Georgia,Times New Roman,Times,serif")?" selected":"" %>>Georgia
				<option value="Times New Roman,Times,serif"<%=         sValue.equals("Times New Roman,Times,serif")        ?" selected":"" %>>Times New Roman
				<option value="Verdana,Arial,Helvetica,sans-serif"<%=  sValue.equals("Verdana,Arial,Helvetica,sans-serif") ?" selected":"" %>>Verdana
			</select>
		</td>
		<td align="center" valign="middle">
			<%	sValue = "11"; 
				if(ft!=null)
					sValue = nvl(ft.getFontSize(), "11"); %>
			<select name="fontSize" onChange="setFontSize(<%= i %>)">
				<option value="6"<%=  sValue.equals("6") ?" selected":"" %>>6
				<option value="8"<%=  sValue.equals("8") ?" selected":"" %>>8
				<option value="9"<%=  sValue.equals("9") ?" selected":"" %>>9
				<option value="10"<%= sValue.equals("10")?" selected":"" %>>10
				<option value="11"<%= sValue.equals("11")?" selected":"" %>>11
				<option value="12"<%= sValue.equals("12")?" selected":"" %>>12
				<option value="14"<%= sValue.equals("14")?" selected":"" %>>14
				<option value="16"<%= sValue.equals("16")?" selected":"" %>>16
				<option value="18"<%= sValue.equals("18")?" selected":"" %>>18
				<option value="24"<%= sValue.equals("24")?" selected":"" %>>24
				<option value="36"<%= sValue.equals("36")?" selected":"" %>>36
				<option value="48"<%= sValue.equals("48")?" selected":"" %>>48
				<option value="72"<%= sValue.equals("72")?" selected":"" %>>72
			</select>
		</td>
		<!--td align="center" valign="middle">
			< %	sValue = "left"; 
				if(ft!=null)
					sValue = nvl(ft.getAlignment(), "left"); % >
			<select name="alignment" onChange="setAlignment(< %= i % >)">
				<option value="left"  < %= sValue.equals("left")  ?" selected":"" % >>Left
				<option value="center"< %= sValue.equals("center")?" selected":"" % >>Center
				<option value="right" < %= sValue.equals("right") ?" selected":"" % >>Right
			</select>
		</td-->
		<td valign="middle">
			<span id="preview<%= i %>" style="font-family:Arial,Helvetica,sans-serif;font-size:11px;color:#000000;width:100">Sample</span>
		</td>
	</tr>
<%	}	// for 
%>
	<tr>
		<td colspan="10" align="center">
			<br>
			<input type="Submit" class=button name="submit_btn" value="Save">
			<input type="Submit" class=button name="submit_btn" value="Save & More Rows">
			<input type="Button" class=button value="Cancel" onClick="window.close();">
		</td>
	</tr>
</table>

</form>

<% }	// if(submitBtn.equals("Save")) { ... } else { 
%>

</body>
</html>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

