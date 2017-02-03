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
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="org.openecomp.portalsdk.core.web.support.ControllerProperties" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>

  <script src="static/fusion/raptor/dy3/js/moment.min.js"></script>
  <script>
    function editMessage(messageLocationId, messageLocation, messageId) {
      window.location='broadcast.htm?message_location_id='+messageLocationId + '&message_location=' + messageLocation + ((messageId != null) ? '&message_id=' + messageId : '');
    }
  </script>

  <div class="pageTitle">
  <h3>
    Broadcast Messages
    </h3>
  </div>

  <%-- Display a table for the broadcast messages of each message location --%>
<div ng-controller="broadcastListController" > 	

	<div ng-repeat="location in messageLocations" >
		{{location.label}} Messages
		<div title="{{location.label}} Messages" >

		  	<table att-table table-data="location.messages" current-page="1">
			  	<thead att-table-row type="header">
			  		<tr>
			  			<th att-table-header sortable="false" width="10%">No.</th>
			  			<th att-table-header sortable="false" width="30%">Message Text</th>
			  			<th att-table-header sortable="false" width="10%">Start Date</th>
			  			<th att-table-header sortable="false" width="10%">End Date</th>
			  			<th att-table-header sortable="false" width="10%">Sort Order</th>
			  			<th att-table-header sortable="false" width="10%">Server</th>
			  			<th att-table-header sortable="false" width="10%">Active?</th>
			  			<th att-table-header sortable="false" width="10%">Delete?</th>
			  		</tr>
			  	</thead>
			  	<tbody att-table-row type="body" row-repeat="message in location.messages" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody-->
				  <tr>
				   {{message.id}}
				    <td width="10%"><a href="javascript:editMessage({{location.value}},'{{location.label}}',{{message.id}});">{{$index+1}}</a></td>
				    <td width="30%">{{message.messageText}}</td>
				    <td width="10%">
				    	{{message.displayStartDate}}
				    </td>
				    <td width="10%">{{message.displayEndDate}}</td>
				    <td width="10%">{{message.sortOrder}}</td>
				    <td width="10%">{{message.siteCd}}</td>
				    <td width="10%">
				    	<div ng-click="toggleActive(message);">
				    		<input type="checkbox" ng-model="message.active" att-toggle-main>
				    	</div>
				    </td>
				     <td att-table-body width="10%">
					    <div ng-click="remove(message);" style="font-size:20px;"><a href="javascript:void(0)" class="icon-trash"></a></div>
				     </td>
				  </tr>
				 
				</tbody>
			</table>
		</div>
		<input att-button btn-type="primary" size="small" class="button" type="button" value="Add" ng-click="editMessage(location);"/>
  	  <br/><br/><br/>
	</div>
</div>

<script>
app.controller('broadcastListController', function ($scope){
	//$scope.model.messagesList=${model.messagesList};
	var messagesMap = {};
	<%
		ObjectMapper mapper = new ObjectMapper();
		HashMap hmMessages = new HashMap();
	    
		HashMap objModel = (HashMap)request.getAttribute("model");
		
	    Object objMessages = objModel.get("messagesList");
	    //System.out.println("messagesList: "+objMessages);
	    if((objMessages!=null) && (objMessages instanceof HashMap))
	    {
	    	hmMessages= (HashMap)objMessages;
	      Iterator it = hmMessages.entrySet().iterator();
	      while (it.hasNext()) {
	          Map.Entry pair = (Map.Entry)it.next();
	          //System.out.println(pair.getKey() + " = " + pair.getValue());
	          String pairValue = mapper.writeValueAsString(pair.getValue());
	          //System.out.println(pairValue);
	          %>
	          messagesMap['<%=pair.getKey()%>'] = '<%=pairValue%>'
	          <%
	      }
	    }
	    
	    Object messageLocationsObject =  objModel.get("messageLocations");
	    //System.out.println("messageLocations: "+messageLocationsObject);
		String messageLocationsString = mapper.writeValueAsString(messageLocationsObject);
		//System.out.println(messageLocationsString);
	%>
	$scope.messagesList=messagesMap;	
	$scope.messageLocations=<%=messageLocationsString%>;
	console.log($scope.messageLocations);
	
	$.each($scope.messageLocations, function(i, a){ 
		//var result = [];
	    angular.forEach($scope.messagesList, function(value, key) {
	    	if (key+'' === a.value+'') {
	    		var objsJSON = JSON.parse(value);
	    		
	    		$.each(objsJSON, function(i, a){ 
	    			var startDateLong = a.startDate;
	    			var tempStartDate = new Date(startDateLong);
	    			tempStartDate = moment(tempStartDate).format('DD MMM YYYY');//03 Jun 2013 04:15PM EDT - 'DD MMM YYYY hh:mmA zz'
	    			a.displayStartDate=tempStartDate.toString();
	    			
	    			var endDateLong = a.endDate;
	    			var tempEndDate = new Date(endDateLong);
	    			tempEndDate = moment(tempEndDate).format('DD MMM YYYY');//03 Jun 2013 04:15PM EDT
	    			a.displayEndDate=tempEndDate.toString();
	    		});
	    		a.messages = objsJSON;
	        }
	    }); 
	    console.log(a.messages);
	});	
	;
	
	$scope.editMessage = function(location) {
		editMessage(location.value, location.label);
	};
	
	$scope.toggleActive = function(broadcastMessage) {

		//alert('deleted'+role.name);
		var uuu = "broadcast_list/toggleActive";
		  var postData={broadcastMessage:broadcastMessage};
	  	  $.ajax({
	  		 type : 'POST',
	  		 url : uuu,
	  		 dataType: 'json',
	  		 contentType: 'application/json',
	  		 data: JSON.stringify(postData),
	  		 success : function(data){
	  			//window.location.reload();  
			 },
			 error : function(data){
				 console.log(data);
				 alert("Error while toggling: "+ data.responseText);
			 }
	  	  });
	
	
	};
	
	$scope.remove = function(broadcastMessage) {

			//alert('deleted'+role.name);
			var uuu = "broadcast_list/remove";
			  var postData={broadcastMessage:broadcastMessage};
		  	  $.ajax({
		  		 type : 'POST',
		  		 url : uuu,
		  		 dataType: 'json',
		  		 contentType: 'application/json',
		  		 data: JSON.stringify(postData),
		  		 success : function(data){
		  			window.location.reload();  
				 },
				 error : function(data){
					 console.log(data);
					 alert("Error while deleting: "+ data.responseText);
				 }
		  	  });
		
		
	};
});
</script>
