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
<%@ page import="org.openecomp.portalsdk.core.web.support.UserUtils" %>
<%@ page import="org.openecomp.portalsdk.core.web.support.ControllerProperties" %>
<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties" %>

<%-- <%@ include file="/WEB-INF/fusion/jsp/include.jsp" %> --%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" type="text/css" href="static/fusion/css/jquery-ui.css">

  <c:set var="clustered" value="<%=(\"true\".equals(SystemProperties.getProperty(SystemProperties.CLUSTERED)))%>"/>

  <script src="static/fusion/js/moment.min.js"></script>
  
  <div class="pageTitle">
   <h3>
	<c:choose>
		<c:when test="${!empty param.message_id}">
			<h1 class="heading1" style="margin-top:20px;">Broadcast Message Edit</h1>
		</c:when>
		<c:otherwise>
			<h1 class="heading1" style="margin-top:20px;">Broadcast Message Create</h1>
		</c:otherwise>
	</c:choose>
   </h3>
  </div>

<div ng-controller="broadcastController" >    
		 Please edit the broadcast message details below:&nbsp;<br><br>
	<div class="fn-ebz-container" >
	        <label class="fn-ebz-text-label"><sup><b>*</b></sup>Message Text:</label><BR>
	        <textarea name="comment" ng-model="broadcastMessage.messageText" rows="5" cols="200" style="height:100px"></textarea>
	</div>
	<br>
	<div class="fn-ebz-container" >
	          <label class="fn-ebz-text-label"><sup><b>*</b></sup>Start Date:</label><BR>
	          <input style="display:none" name="startDateHidden" ng-model="broadcastMessage.startDate">
	          <input type="text" class="fn-ebz-text" id="startDatepicker" />
	</div>
	
	<div class="fn-ebz-container" style="margin-left:3em" >
	          <label class="fn-ebz-text-label"><sup><b>*</b></sup>End Date:</label><BR>
	          <input style="display:none" name="endDateHidden" ng-model="broadcastMessage.endDate">
	          <input type="text" class="fn-ebz-text" id="endDatepicker" />
	</div>

	<div class="fn-ebz-container" style="margin-left:3em"  >
		<label class="fn-ebz-text-label"><sup><b>*</b></sup>Sort Order:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="broadcastMessage.sortOrder"
			maxlength="30" /> 
	</div>
	
          <c:if test="${clustered}">
          			<div class="fn-ebz-container" style="margin-left:1em" >	
						<label  class="fn-ebz-text-label">Server:</label><BR>
						<div class="form-field" att-select="broadcastSites" ng-model="broadcastMessage.siteCd"></div>
					</div>
          </c:if>
		<br>
	<div align="left" >
		<button type="submit" ng-click="save();" att-button
			btn-type="primary" size="small">Save</button>
	</div>
</div>
<script>
app.controller('broadcastController', function ($scope, modalService, $modal){
	$scope.broadcastMessage=${broadcastMessage};
	$scope.broadcastSites=${broadcastSites};
	console.log($scope.broadcastMessage);
	
	$scope.save = function() {
		  var uuu = "broadcast/save";
		  var postData={broadcastMessage: $scope.broadcastMessage};
	  	  $.ajax({
	  		 type : 'POST',
	  		 url : uuu,
	  		 dataType: 'json',
	  		 contentType: 'application/json',
	  		 data: JSON.stringify(postData),
	  		 success : function(data){
	  			//console.log(data);
	  			window.location.href = "broadcast_list";
			 },
			 error : function(data){
				 alert("Error while saving.");
			 }
	  	  });
	};
	
	$(function() {
	    $( "#startDatepicker" ).datepicker();
	    $( "#endDatepicker" ).datepicker();
	    
	    var startDateLong = $scope.broadcastMessage.startDate;
		var tempStartDate = new Date(startDateLong);
		tempStartDate = moment(tempStartDate).format('MM/DD/YY');//03 Jun 2013 04:15PM EDT
		console.log(tempStartDate.toString());
		$( "#startDatepicker" ).val(tempStartDate.toString());
	    
	    var endDateLong = $scope.broadcastMessage.endDate;
		var tempendDate = new Date(endDateLong);
		tempendDate = moment(tempendDate).format('MM/DD/YY');//03 Jun 2013 04:15PM EDT
		console.log(tempendDate.toString());
		$( "#endDatepicker" ).val(tempendDate.toString());
		
	    $( "#startDatepicker" ).change(function() {
	    	var tempStartDate = moment($( "#startDatepicker" ).val()).format('YYYY-MM-DD hh:mm:ss.S');
	    	$scope.broadcastMessage.startDate = new Date(tempStartDate.toString());
	    });
	    $( "#endDatepicker" ).change(function() {
	    	var tempEndDate = moment($( "#endDatepicker" ).val()).format('YYYY-MM-DD hh:mm:ss.S');
	    	$scope.broadcastMessage.endDate = new Date(tempEndDate.toString());
	  	});
	  });
});


</script>
