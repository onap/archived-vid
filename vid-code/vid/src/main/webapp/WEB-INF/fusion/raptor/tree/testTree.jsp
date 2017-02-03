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
<%@page import="org.openecomp.portalsdk.analytics.system.AppUtils" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
 <jsp:include page="header.jsp" flush="true" />	
<head>
	<meta http-equiv="content-type" content="text/html;charset=iso-8859-1">
	<title>Folder tree with Drag and Drop capabilities</title>
	<script>
		var imgFolder = '<%=AppUtils.getImgFolderURL()%>';
	</script>
	<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/tree/ajax.js"></script>
	<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/tree/context-menu.js"></script>
	<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/tree/drag-drop-folder-tree.js">
	
	/************************************************************************************************************
	(C) www.dhtmlgoodies.com, July 2006
	
	Update log:
	
	
	This is a script from www.dhtmlgoodies.com. You will find this and a lot of other scripts at our website.	
	
	Terms of use:
	You are free to use this script as long as the copyright message is kept intact.
	
	For more detailed license information, see http://www.dhtmlgoodies.com/index.html?page=termsOfUse 
	
	Thank you!
	
	www.dhtmlgoodies.com
	Alf Magne Kalleland
	
	************************************************************************************************************/	
	</script>
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/tree/drag-drop-folder-tree.css">
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/tree/context-menu.css">
		
	<style type="text/css">
	/* CSS for the demo */
	img{
		border:0px;
	}
	</style>
	<script type="text/javascript">
	//--------------------------------
	// Save functions
	//--------------------------------
	var ajaxObjects = new Array();
	
	// Use something like this if you want to save data by Ajax.
	function saveMyTree()
	{
			saveString = treeObj.getNodeOrders();
			alert(saveString);
			var ajaxIndex = ajaxObjects.length;
			ajaxObjects[ajaxIndex] = new sack();
			var url = 'tree';
			//var url = 'tree';
			ajaxObjects[ajaxIndex].requestFile = url;	// Specifying which file to get
			ajaxObjects[ajaxIndex].onCompletion = function() { saveComplete(ajaxIndex); } ;	// Specify function that will be executed after file has been found
			ajaxObjects[ajaxIndex].runAJAX(url);		// Execute AJAX function			
		
	}
	function saveComplete(index)
	{
		alert(ajaxObjects[index].response);			
	}

	
	// Call this function if you want to save it by a form.
	function saveMyTree_byForm()
	{
		document.myForm.elements['saveString'].value = treeObj.getNodeOrders();
		document.myForm.elements['task'].value = 'save';
		document.myForm.submit();		
	}
	
	function cancelMyTree_byForm(){
		document.myForm.elements['task'].value = '';
		document.myForm.submit();		
	}
	
	function deleteMyTree_byForm()
	{
		document.myForm.elements['task'].value = 'delete';
		document.myForm.submit();		
	}
	
	function addMyTree_byForm()
	{
		document.myForm.elements['task'].value = 'new';
		document.myForm.submit();		
	}
	
	function runReport()
	{
		var id = JSTreeObj.selectedItem.parentNode.id.replace(/[^0-9]/gi,'');
		alert("123" + id);
		//document.myForm.action='<%=AppUtils.getReportExecuteActionURL()%>'+JSTreeObj.selectedItem;
		document.myForm.action='dispatcher?action=raptor&r_action=report.run&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
		 
		document.myForm.submit();		
	}
	
	function editReport()
	{
		var id = JSTreeObj.selectedItem.parentNode.id.replace(/[^0-9]/gi,'');
		alert("123" + id);
		document.myForm.action='dispatcher?action=raptor&r_action=report.edit&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';			 
		document.myForm.submit();		
	}
	
	function expandTree(){
		document.getElementById('expandedTree').style.display='block';
		document.getElementById('expandedTreeHeader').style.display='block';
		document.getElementById('collapsedTree').style.display='none';
	}
	function collapseTree(){
		document.getElementById('expandedTree').style.display='none';
		document.getElementById('expandedTreeHeader').style.display='none';
		document.getElementById('collapsedTree').style.display='block';
	}
	</script>
	
</head>
<body>
	<form name="reportForm" method="post">
	<table width="94%" border="0" cellspacing="1" align="center">
	<tr><td>
		<div id="collapsedTree" style="position:absoulte;top:0;width:100%;height:25px;display:none;">
		<table width="94%" border="0" cellspacing="1" align="left">
			<tr>
				<td class=rtabletext align=left>
					<img border="0" src="<%= AppUtils.getImgFolderURL() %>tree/dhtmlgoodies_plus.gif" onClick="javascript:expandTree()" alt='Show Folder Tree'>
					Root Node
				</td>
			</tr>
		</table>
		</div>
	</td></tr>
	
	
	<tr><td>  
		<div id="expandedTreeHeader" style="position:absoulte;top:0;width:100%;height:25px;display:block;">
		<table width="94%" border="0" cellspacing="1" align="left">
			<tr>
				<td class=rtabletext >
					<img border="0" src="<%= AppUtils.getImgFolderURL() %>tree/dhtmlgoodies_minus.gif" onClick="javascript:collapseTree()"  alt='Hide Folder Tree'>
					&nbsp;&nbsp;&nbsp;
					<!--<input type='button' class=rsmallbutton id='createFolder' value='Create Folder' onclick='javascript:JSTreeObj.addItem(JSTreeObj.selectedItem, JSTreeObj.selectedItem);'/>
					<input type='button' class=rsmallbutton id='createFolder' value='Run' onclick='javascript:JSTreeObj.addItem(JSTreeObj.selectedItem, JSTreeObj.selectedItem);'/>
					<input type='button' class=rsmallbutton id='createFolder' value='Edit' onclick='javascript:JSTreeObj.addItem(JSTreeObj.selectedItem, JSTreeObj.selectedItem);'/>-->
					<a href="#" onclick='javascript:JSTreeObj.addItem(JSTreeObj.selectedItem, JSTreeObj.selectedItem);'>Create Folder</a> | 
					<a href="#" onclick='javascript:JSTreeObj.deleteItem(JSTreeObj.selectedItem, JSTreeObj.selectedItem);'>Delete Fodler</a> | 
					<a href="#" onclick='javascript:runReport();'>Run Report</a> | 
					<a href="#" onclick='javascript:editReport();'>Edit Report</a>  					
				</td>
			</tr>
		</table>
		</div>
	</td></tr>
	<tr>
		<td>
			<div id="expandedTree" style="position:absoulte;top:0;width:100%;height:160px;display:block">
			<table>
				<tr>
	
				<td width="50%">
					<div id="scrollableTable" class="scrollableTable" style="position:absoulte;top:0;width:100%;height:150px;display:block">
						<table width="94%" border="0" cellspacing="1" align="center">
							<tr>
								<td >
									<%=request.getAttribute("folderList")%>
								</td>
							</tr>
						</table>
					</div>
				</td>
				<td width="50%" valign="bottom">
					<table>
						<tr><td width="100%" class=rtabletext >
							<a href="#" onclick="treeObj.collapseAll()">Collapse all</a> | 
							<a href="#" onclick="treeObj.expandAll()">Expand all</a>		
						</td></tr>
						<tr><td width="100%">
							<input type="button" class=rsmallbutton onclick="saveMyTree_byForm()" value="Save">
							<input type="button" class=rsmallbutton onclick="cancelMyTree_byForm()" value="Cancel">
						</td></tr>			
					</table>
					
				</td>
				</tr>	
				
			</table>
			</div>
		</td>
	</tr>
	<tr><td colspan=2>
	<div class="scrollableTable" style="width:100%;height:400px;">
		<iframe id="reportFrame" name="reportFrame" width="100%" height="100%">
			
		</iframe>
	</div>
	</td></tr>
	</table>
	</Form>
	<script type="text/javascript">	
		treeObj = new JSDragDropTree();
		treeObj.setTreeId('reportFolderTree');
		treeObj.setMaximumDepth(7);
		treeObj.setMessageMaximumDepthReached('Maximum depth reached'); // If you want to show a message when maximum depth is reached, i.e. on drop.
		treeObj.initTree();
		treeObj.expandAll();
	
	</script>
	
	<!-- Form - if you want to save it by form submission and not Ajax -->
	<form name="myForm" action="tree" method="post">
		<input type="hidden" name="saveString">
		<input type="hidden" name="task">
		<input type="hidden" name="newName">
		<input type="hidden" name="deleteIds">
	</form>

</body>
</html>
