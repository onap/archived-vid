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
<%@page import="org.openecomp.portalsdk.analytics.model.runtime.ReportRuntime"%>
<%@page import="org.openecomp.portalsdk.analytics.util.AppConstants"%>
<%@page import="org.openecomp.portalsdk.analytics.system.Globals"%>

<%
	boolean isFolderAllowed = false;
	ReportRuntime rr = (ReportRuntime) request.getSession().getAttribute(AppConstants.SI_REPORT_RUNTIME);
	boolean adminUser = AppUtils.isAdminUser(request);
	if (Globals.isFolderTreeAllowed()) {
		if(adminUser) {
			isFolderAllowed = true;
		} else if (!Globals.isFolderTreeAllowedOnlyForAdminUsers()) { 
			isFolderAllowed = true; 
		} else isFolderAllowed = false;
	}
	
    
	
%>


<script src='dwr/engine.js'></script>
<script src='dwr/util.js'></script>
<script src='dwr/interface/folderNavAjax.js'></script>


<script>
	var imgFolder = '<%=AppUtils.getImgFolderURL()%>';
	var isFolderAllowed = '<%= isFolderAllowed%>';
</script>
<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/tree/ajax.js"></script>
<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/tree/context-menu.js"></script>
<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/tree/drag-drop-folder-tree.js">
<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/raptor.js">
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
<script>
	var doCollapse = '<%=request.getParameter("doCollapseTree")%>';
	var selectedFolderId;
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
			displayFolderInfo();
			var data=folderNavAjax.saveFolderStructure(treeObj.getNodeOrders(), "<%=AppUtils.getUserID(request)%>", fillFolderStructure);
			
		}
		
		function cancelMyTree_byForm(){
			//document.treeFrm.submit();		
			displayFolderInfo();
			var data=folderNavAjax.getFolderListString("<%=AppUtils.getUserID(request)%>", fillFolderStructure);
		}
		
		function deleteMyTree_byForm()
		{
			//document.treeForm.submit();
			displayFolderInfo();
			var data=folderNavAjax.deleteFolderStructure(document.treeFrm.deleteIds.value, "<%=AppUtils.getUserID(request)%>", fillFolderStructure);
		}
		
		function addMyTree_byForm()
		{
			//document.treeFrm.submit();
			displayFolderInfo();
			selectedFolderId = document.treeFrm.saveString.value;
			var data=folderNavAjax.createNewFolder(document.treeFrm.saveString.value, document.treeFrm.newName.value, "<%=AppUtils.getUserID(request)%>", fillFolderStructure);
					
		}
		
		function renameMyTree_byForm()
		{
			//document.treeFrm.submit();
			displayFolderInfo();
			var data=folderNavAjax.renameFolder(document.treeFrm.renameId.value, document.treeFrm.newName.value, "<%=AppUtils.getUserID(request)%>", fillFolderStructure);
					
		}
		
		function createFolder(){
			if(JSTreeObj.selectedItem == null || JSTreeObj.selectedItem.parentNode.id.substr(0,3) == '000') 
				return; 
			if (JSTreeObj.isAddAllowed(JSTreeObj.selectedItem.parentNode.id) == false){
				alert("You do not have rights on this folder");
				return;
			}
			selectedFolderId = JSTreeObj.selectedItem.parentNode.id;
			displayFolderInfo();
			if (JSTreeObj.addItem(JSTreeObj.selectedItem, JSTreeObj.selectedItem) == false){
				hideFolderInfo();
			}
		}
		
		function deleteFolder(){
			if(JSTreeObj.selectedItem == null) {
				return; 
			}
			var id = JSTreeObj.selectedItem.parentNode.id.replace(/[^0-9]/gi,'');
			if (JSTreeObj.isDeleteAllowed(id) == false){
				alert("You do not have rights on this folder");
				return;
			}
			if(id.substr(0,3) == '000') {
				return;
			}			
			displayFolderInfo();
			if (JSTreeObj.deleteItem(JSTreeObj.selectedItem, JSTreeObj.selectedItem) == false){
				hideFolderInfo();
			}
		}
		
		function runReport()
		{
			
			if (JSTreeObj == null || JSTreeObj.selectedItem == null){
				return;
			}
			
			var id = JSTreeObj.selectedItem.parentNode.id.replace(/[^0-9]/gi,'');
			if(id.substr(0,3) != '000') {
				return;
			}			
			//displayFolderInfo();
			id = id.substr(3, id.length);
			addBreadCrumb(true, id);	
			if (window.frames && window.frames[0] && window.frames[0].name == 'content_Iframe'){
				window.frames[0].location='<%= AppUtils.getRaptorActionURL() %>report.run&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
			}
			else if (document.getElementById('searchContentContainer')){
				document.getElementById('searchContentContainer').innerHTML = "<div id='breadCrumbArea'> </div><iframe name='content_Iframe' frameborder='0'  id='content_Iframe' src='<%= AppUtils.getRaptorActionURL() %>report.run&c_master=" + id + "&PAGE_ID=HOME&refresh=Y' width='100%' height='450' ></iframe>";
				//document.getElementById('content_Iframe').height=document.body.offsetHeight - 210;
				resizeWindow();
				document.getElementById('content_Iframe').location = '<%= AppUtils.getRaptorActionURL() %>report.run&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
									
			}else{
			
				document.treeFrm.action='<%= AppUtils.getRaptorActionURL() %>report.run&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
				document.treeFrm.submit();
			}
			
		}
		function editReport()
		{
			if (JSTreeObj == null || JSTreeObj.selectedItem == null){
				return;
			}
			
			var id = JSTreeObj.selectedItem.parentNode.id.replace(/[^0-9]/gi,'');
			if(id.substr(0,3) != '000') {
				return;
			}			
			id = id.substr(3, id.length);
			if (window.frames && window.frames[0] && window.frames[0].name == 'content_Iframe'){
				window.frames[0].location='<%= AppUtils.getRaptorActionURL() %>report.edit&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
			}
			else if (document.getElementById('searchContentContainer')){
				document.getElementById('searchContentContainer').innerHTML = "<div id='breadCrumbArea'> </div><iframe name='content_Iframe' frameborder='0'  id='content_Iframe' src='<%= AppUtils.getRaptorActionURL() %>report.edit&c_master=" + id + "&PAGE_ID=HOME&refresh=Y' width='100%' height='450' ></iframe>";
				//document.getElementById('content_Iframe').height=document.body.offsetHeight - 210;
				resizeWindow();
				document.getElementById('content_Iframe').location = '<%= AppUtils.getRaptorActionURL() %>report.edit&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
						
			}else{
				document.treeFrm.action='<%= AppUtils.getRaptorActionURL() %>report.edit&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
				document.treeFrm.submit();			
			}
		}
		function deleteReport()
		{
			if (JSTreeObj == null || JSTreeObj.selectedItem == null){
				return;
			}
			
			var id = JSTreeObj.selectedItem.parentNode.id.replace(/[^0-9]/gi,'');
			if(id.substr(0,3) != '000') {
				return;
			}			
			id = id.substr(3, id.length);
			if (window.frames && window.frames[0] && window.frames[0].name == 'content_Iframe'){
				window.frames[0].location='<%= AppUtils.getRaptorActionURL() %>report.delete&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
			}
			else if (document.getElementById('searchContentContainer')){
				document.getElementById('searchContentContainer').innerHTML = "<div id='breadCrumbArea'> </div><iframe name='content_Iframe' frameborder='0'  id='content_Iframe' src='<%= AppUtils.getRaptorActionURL() %>report.delete&c_master=" + id + "&PAGE_ID=HOME&refresh=Y' width='100%' height='450' ></iframe>";
				//document.getElementById('content_Iframe').height=document.body.offsetHeight - 210;
				resizeWindow();
				document.getElementById('content_Iframe').location = '<%= AppUtils.getRaptorActionURL() %>report.delete&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
						
			}
			else{
				document.treeFrm.action='<%= AppUtils.getRaptorActionURL() %>report.delete&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
				document.treeFrm.submit();			
			}
			
		}
		function createReport()
		{
			if (JSTreeObj == null || JSTreeObj.selectedItem == null){
				return;
			}	
			var id = JSTreeObj.selectedItem.parentNode.id.replace(/[^0-9]/gi,'');
			addBreadCrumb(false, id);
			if (window.frames && window.frames[0] && window.frames[0].name == 'content_Iframe'){
				window.frames[0].location='<%= AppUtils.getRaptorActionURL() %>report.create&folder_id=' + id + '&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
			}
			else if (document.getElementById('searchContentContainer')){
				document.getElementById('searchContentContainer').innerHTML = "<div id='breadCrumbArea'> </div><iframe name='content_Iframe' frameborder='0'  id='content_Iframe' src='<%= AppUtils.getRaptorActionURL() %>report.create&folder_id=" + id + "&c_master=' + id + '&PAGE_ID=HOME&refresh=Y' width='100%' height='450' ></iframe>";
				//document.getElementById('content_Iframe').height=document.body.offsetHeight - 210;
				resizeWindow();
				document.getElementById('content_Iframe').location = '<%= AppUtils.getRaptorActionURL() %>report.create.container&folder_id=' + id + '&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
						
			}
			else{
				displayFolderInfo();
				document.treeFrm.action='<%= AppUtils.getRaptorActionURL() %>report.create.container&folder_id=' + id + '&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
				document.treeFrm.submit();
			}
		}
		function getFolderReports(id)
		{	
			addBreadCrumb(false, id);
			if (window.frames && window.frames[0] && window.frames[0].name == 'content_Iframe'){
				window.frames[0].location='<%= AppUtils.getRaptorActionURL() %>report.folderlist_iframe&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';				
			}
			else if (document.getElementById('searchContentContainer')){
				document.getElementById('searchContentContainer').innerHTML = "<div id='breadCrumbArea'> </div><iframe name='content_Iframe' frameborder='0'  id='content_Iframe' src='<%= AppUtils.getRaptorActionURL() %>report.folderlist_iframe&c_master=" + id + "&PAGE_ID=HOME&refresh=Y' width='100%' height='450' ></iframe>";
				//document.getElementById('content_Iframe').height=document.body.offsetHeight - 210;
				resizeWindow();
				document.getElementById('content_Iframe').location = "<%= AppUtils.getRaptorActionURL() %>report.folderlist_iframe&c_master=" + id + "&PAGE_ID=HOME&refresh=Y";
			
			}
			else{
				document.treeFrm.action='<%= AppUtils.getRaptorActionURL() %>report.folderlist&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
				document.treeFrm.submit();
			}
		}
		
		function getAllFolderReports()
		{
			var id = JSTreeObj.selectedItem.parentNode.id.replace(/[^0-9]/gi,'');
			document.treeFrm.action='<%= AppUtils.getRaptorActionURL() %>report.folderlist&c_master=' + id + '&PAGE_ID=HOME&refresh=Y';
			document.treeFrm.submit();
		}
		
		
		function displayFolderInfo()
		{
			var infoDiv = document.getElementById('loadingMessageDiv');
			if (infoDiv == null)
				return;			
			infoDiv.style.left = document.body.offsetWidth - 100 ;
			infoDiv.style.top = document.getElementById('scrollableTable').style.top;
			infoDiv.style.display='block';			
		}
		function hideFolderInfo()
		{
			var infoDiv = document.getElementById('loadingMessageDiv');
			if (infoDiv == null)
				return;						
			infoDiv.style.display='none';
		}
		
		function displayTree(id){
			if (isFolderAllowed ){
				if (id != null)
					selectedFolderId = id;
				if (document.getElementById('reportFolderTree') == null){
					displayFolderInfo();
					var data=folderNavAjax.getFolderListString("<%=AppUtils.getUserID(request)%>", fillFolderStructure);
				}			
				document.getElementById('expandedTree').style.display='block';
				document.getElementById('collapsedTree').style.display='none';
				if (document.getElementById('folderTreeContainer'))
					document.getElementById('folderTreeContainer').width="25%";
					
				//document.getElementById('scrollableTable').style.height=window.screen.height - 320;
				//document.getElementById('scrollableTable').style.height=document.body.offsetHeight - 260;
				resizeWindow();
				folderNavAjax.displayTree();
			}


		}
		function hideTree(){
			document.getElementById('expandedTree').style.display='none';
			document.getElementById('collapsedTree').style.display='block';
			//GET BREAD CRUMBS and assign
			if (document.getElementById('folderTreeContainer'))
				document.getElementById('folderTreeContainer').width="2px";
			folderNavAjax.hideTree();
		}
		function refreshTree(id){

			if (isFolderAllowed ){
				if (id != null)
					selectedFolderId = id;
				displayFolderInfo();
				var data=folderNavAjax.getFolderListString("<%=AppUtils.getUserID(request)%>", fillFolderStructure);
				
				document.getElementById('expandedTree').style.display='block';
				document.getElementById('collapsedTree').style.display='none';
				if (document.getElementById('folderTreeContainer'))
					document.getElementById('folderTreeContainer').width="25%";
					
				//document.getElementById('scrollableTable').style.height=document.body.offsetHeight - 210;
				resizeWindow();
				folderNavAjax.displayTree(selectedFolderId);
			}
		}
		function addBreadCrumb(isReport, id){
		//	if (id != null)
		//		var data=folderNavAjax.getBreadCrumb(isReport, id, addBreadCrumbCallback);
			
		}
		function addBreadCrumbCallback(data){
			var collapsedTree = document.getElementById('breadCrumbArea');
			if (collapsedTree == null)
				return;
			collapsedTree.innerHTML = "<font class=rtabletext style='background-color:white'>" + data + "</font>";
		}
		function collapseAllTree(){
			treeObj.collapseAll();
		}
</script>

<form name='treeFrm' method='post'>

	<input type='hidden' id='newName' name='newName'/>
	<input type='hidden' id='saveString' name='saveString'/>
	<input type='hidden' id='deleteIds' name='deleteIds'/>
	<input type='hidden' id='renameId' name='renameId'/>
	
	
	<tr id="def" height="0">
		<td colspan="10"></td>
	</tr>
	
	<tr width="100%" ><td valign='top' width='300'>
	<% if(isFolderAllowed) { %>
		<div id="expandedTree" style="position:absoulte;top:0;width:100%;height:25px;display:none;">
		<table width="100%" border="0" cellspacing="1" align="left">
			<tr>
				<td colspan=2 align="right"><img border="0" src="<%= AppUtils.getImgFolderURL() %>tree/grnarrowleft.gif" onClick="javascript:hideTree()"  alt='Hide Folder Tree' style='cursor:hand'>
					&nbsp;&nbsp;&nbsp;
				</td>
				
			</tr>
			<tr>
				<td colspan=3>
					<div id="scrollableTable" class="scrollableTable" style="position:absoulte;top:0;width:100%;height:430px;display:block;overflow:scroll">
						<table id="folderTree" width="100%" border="0" cellspacing="1" align="center">
							<tr>
								<td nowrap>	
								</td>
							</tr>
						</table>
					</div>
					<div id='loadingMessageDiv' style="position:absolute;top:150;left:650;width:40px;height:25px;display:none">
						<table><tr bgcolor='red'><td class=rcolheader> Loading...</td></tr></table>
					</div>			
					
				</td>				
			</tr>
			<tr>
				<td class=rtabletext width='100%'>
					<a href="#" onclick='javascript:createFolder();'>New Folder</a> | 
					<a href="#" onclick='javascript:deleteFolder();'>Delete Folder</a> | 
					<a href="#" onclick='javascript:runReport();'>Run</a> | 
					<a href="#" onclick="treeObj.collapseAll()">Collapse</a> | 
					<a href="#" onclick="treeObj.expandAll()">Expand</a> |
					<a href="#" onclick="javascript:refreshTree()">Refresh Tree</a>
				</td>
				<td ></td>
			</tr>
			
		</table>
		</div>
		<% } %>
		

	</td></tr>
	<% if(isFolderAllowed) { %>
	<div id="collapsedTree" style="position:absolute;top=122;display:block;width:550">
		<img border="0" src="<%= AppUtils.getImgFolderURL() %>tree/grnarrowright.gif" onClick="javascript:displayTree()"  alt='Show Folder Tree' style='cursor:hand'> 
	</div>
	<% } %>
	


</form>


<script type="text/javascript">	
	
	<% if(isFolderAllowed) { %>
		document.body.scroll="no";
	<% } %>
	
	//window.onscroll = floatMenu;
	window.onresize = resizeWindow;
	
	function floatMenu()
	{
		var elem = document.getElementById("def");
		var sy = document.body.scrollTop;
		//elem.style.left = startX;
		if (sy == null || sy == 0)
			sy = 1;
		elem.height = sy ;
	}
	

	function fillFolderStructure(data){
		document.getElementById("folderTree").childNodes[0].childNodes[0].childNodes[0].innerHTML=data;
		initializeTree();
		hideFolderInfo();

	}
	
	function initializeTree(){
		treeObj = new JSDragDropTree();
		treeObj.setTreeId('reportFolderTree');
		treeObj.setMaximumDepth(7);
		treeObj.setMessageMaximumDepthReached('Maximum depth reached'); // If you want to show a message when maximum depth is reached, i.e. on drop.
		treeObj.initTree(selectedFolderId);
		treeObj.expandAll();
		treeObj.collapseAll();
		if (selectedFolderId && selectedFolderId != '0')
			treeObj.showSelectedNode(selectedFolderId);

	}
	
	if (doCollapse == 'N')
		refreshTree();
</script>
					

