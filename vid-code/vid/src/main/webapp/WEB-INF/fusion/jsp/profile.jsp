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
<%@ page import="org.openecomp.portalsdk.core.domain.User"%>
<%@ page import="org.openecomp.portalsdk.core.web.support.UserUtils"%>

<%@page import="org.openecomp.portalsdk.core.web.support.ControllerProperties"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" type="text/css" href="static/fusion/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/fn-ebz.css" >
<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>

<%@ include file="/WEB-INF/fusion/jsp/include.jsp"%>

<div class="pageTitle">
	<h3>
	<c:choose>
		<c:when test="${!empty profileId}">
			<h1 class="heading1" style="margin-top:20px;">Profile Edit</h1>
		</c:when>
		<c:otherwise>
			<h1 class="heading1" style="margin-top:20px;">Profile Edit</h1>
		</c:otherwise>
	</c:choose>
	</h3>
</div>

<div ng-controller="profileController" >

     Please edit the profile details below:&nbsp;<br><br>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label"><sup><b>*</b></sup>First Name:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.firstName"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label"><sup><b>*</b></sup>Last Name:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.lastName"
			maxlength="30" /> 
	</div>
     
	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label">OrgUserId:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.orgUserId"
			maxlength="30" style="margin-right:0px;"/> 
	</div>

	<div class="fn-ebz-container" >
	<label class="fn-ebz-text-label">Manager OrgUserId:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.orgManagerUserId"
			maxlength="30" style="margin-right:0px;"/> 
	</div>
<BR>
	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label">Login Id:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.loginId"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container" style="margin-right:20px">
		<label class="fn-ebz-text-label">Password:</label><BR>
		<input type="password" ng-model="profile.loginPwd"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label"><sup><b>*</b></sup>Phone:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.phone"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container" >
	<label class="fn-ebz-text-label">Fax:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.fax"
			maxlength="30" /> 
	</div>
<BR>
	<div class="fn-ebz-container" >
	<label class="fn-ebz-text-label">Cellular:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.cellular"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label"><sup><b>*</b></sup>Email:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.email"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label">Address 1:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.address1"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label">Address 2:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.address2"
			maxlength="30" /> 
	</div>
<BR>
	<div class="fn-ebz-container" >
	<label class="fn-ebz-text-label">City:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.city"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container">	
		<label  class="fn-ebz-text-label">State:</label><BR>
		<div class="form-field" att-select="stateList.options" ng-model="stateList.selected"></div>
	</div>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label">Zip Code:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="profile.zipCode"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container">	
		<label  class="fn-ebz-text-label">Country:</label><BR>
		<div class="form-field" att-select="countries" ng-model="selectedCountry"></div>
	</div>
	<BR>
	<div class="fn-ebz-container">	
		<label  class="fn-ebz-text-label">Time Zone:</label><BR>
		<div class="form-field" att-select="timeZones" ng-model="selectedTimeZone"></div>
	</div>
	
	<div align="left" >
		<button type="submit" ng-click="saveProfile();" att-button
			btn-type="primary" size="small">Save</button>
	</div>
	
<br>
	<div class="pageTitle">
		<label>Roles</label>
		<a ng-click="addNewRolePopup();" class="icon-add" size="small"></a>
   
	</div>

	<table 	att-table table-data="profile.roles" view-per-page="viewPerPage" current-page="currentPage" search-category="searchCategory" search-string="searchString" total-page="totalPage">
<!-- 	<table border="1" class="hovertable_1"> -->
	     <thead att-table-row type="header">
  		<tr>
  			<th att-table-header sortable="false" align="left"  width="90%">Name</th>
  			<th att-table-header sortable="false"  width="10%">Remove?</th>
  		</tr>
  		 </thead>
  		 <tbody att-table-row type="body" row-repeat="role in profile.roles" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody-->
<!-- 		  <tr ng-repeat="role in profile.roles track by role.id"> -->
		  <tr>
		    <td att-table-body width="90%" >{{ role.name }}</td>
		    <td att-table-body width="10%">
		    	<a ng-click="removeRole(role);" ><img src="static/fusion/images/deleteicon.gif"></a>
		     </td>
		  </tr>
		 </tbody>
	</table>
	
	<div id="dialog" style="overflow:scroll" class="modal__informative font-showcase" >
            <div class="modal__header">
                <h2 class="font-showcase-font-name" style="color:#157BB2">Select Roles</h2>
            </div>
            <button align="right" class="button button--primary button--small" herf="javascript:void(0)" ng-click="close()">Close</button>
            <div class="divider-container"><hr></div>  
            <div class="modal__content">           	 	
				<table table-data="availableRoles" att-table  >
			        <thead att-table-row type="header">
				  		<tr>
				  			<th att-table-header sortable="false" width="10%"> </th>
				  			<th att-table-header sortable="false" width="90%">Role</th>
				  		</tr>
			  		</thead>
				  
				    <tbody att-table-row type="body" row-repeat="availableRole in availableRoles" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody-->
				  	  <tr>
					  	<td att-table-body width="10%">
					  		<div ng-click="toggleRole(availableRole.selected,availableRole);">
					    	<input type="checkbox" ng-model="availableRole.selected" att-toggle-main>
					    	</div>
					    </td>
					    <td att-table-body width="90%">{{ availableRole.name }}</td>
				  	  </tr>
				    </tbody>
		        </table>
            </div>
    </div>
	
</div>



<script>
app.controller('profileController', function ($scope,modalService){

	$scope.profile=${model.profile};
	$scope.profileId = $scope.profile.id;
	$scope.orgUserId=$scope.profile.orgUserId;
	$scope.orgManagerUserId=$scope.profile.orgManagerUserId;
	
	$scope.viewPerPage = 2;
	$scope.currentPage = 1;
	$scope.totalPage;
	$scope.searchCategory = "";
	$scope.searchString = "";
	
	$( "#dialog" ).hide();
	
	$scope.ociavailableRoles=${model.availableRoles};
	//modalService.showFailure('Error','') ; 
	$scope.availableRoles = []; 
	if($scope.ociavailableRoles)
		$.each($scope.ociavailableRoles, function(i, a){ 
			var availableRole = a;
			availableRole.selected = false;
		    $.each($scope.profile.roles, function(j, b){ 
		    	if(a.id === b.id) {
		    		availableRole.selected = true;
		    	} 
		    });
		    $scope.availableRoles.push(availableRole);	    
		});	
		;
	
	$scope.ociTimeZones = ${model.timeZones};
	$scope.timeZones = []; 
	$scope.selectedTimeZone = null;
	if($scope.ociTimeZones){
		$.each($scope.ociTimeZones, function(i, a){ 
			var timeZone = {"index":i, "value":a.value, "title":a.label};
		    $scope.timeZones.push(timeZone);
		    if($scope.profile.timeZoneId !== null && a.value === $scope.profile.timeZoneId.toString()){
		    	$scope.selectedTimeZone = timeZone;
			}	    
		});	
	};
	
	$scope.ociCountries = ${model.countries};
	$scope.countries = []; 
	$scope.selectedCountry = null;
	//alert($scope.ociCountries[0].label);
	if($scope.ociCountries)
	$.each($scope.ociCountries, function(i, a){ 
		var country = {"index":i, "value":a.value, "title":a.label};
	    $scope.countries.push(country);
	    if(a.value === $scope.profile.country){
	    	$scope.selectedCountry = country;
		}	    
	});	
	;
	
	var stateList=${model.stateList};
	//alert(stateList[0].label);
	stateList = stateList== null? []: stateList;
	var selectedState= $scope.profile.state ? $scope.profile.state:"";
	$scope.stateList = initDropdownWithLookUp(stateList,selectedState );
	
	$scope.saveProfile = function() {
		  var uuu = "profile/saveProfile?profile_id="+$scope.profileId;
		  var postData={profile: $scope.profile,
				        selectedCountry:$scope.selectedCountry!=null?$scope.selectedCountry.value:"",
				        selectedState:$scope.stateList.selected!=null?$scope.stateList.selected.value:"",
				        selectedTimeZone:$scope.selectedTimeZone!=null?$scope.selectedTimeZone.value:""
		               };
	  	  $.ajax({
	  		 type : 'POST',
	  		 url : uuu,
	  		 dataType: 'json',
	  		 contentType: 'application/json',
	  		 data: JSON.stringify(postData),
	  		 success : function(data){
	  			modalService.showSuccess("Success","Update Successful.");
			 },
			 error : function(data){
				modalService.showFailure("Fail","Error while saving.");
			 }
	  	  });
	};
		
	$scope.addNewRolePopup = function(role) {
		$( "#dialog" ).dialog({
		      modal: true,
		      width: 500,
		      height:600
		    });
		$(".ui-dialog-titlebar").hide();     
	};
		
		$scope.toggleRole = function(selected,availableRole) {
				//alert('toggleRole: '+selected);
				if(!selected) {
					//remove role
					var uuu = "profile/removeRole?profile_id=" + $scope.profileId;
					modalService.popupConfirmWinWithCancel("Confirm","You are about to remove the role "+availableRole.name+" from the profile for "+$scope.profile.firstName+" "+$scope.profile.lastName+". Do you want to continue?",
			    			function(){
									var postData={role:availableRole};
								  	  $.ajax({
								  		 type : 'POST',
								  		 url : uuu,
								  		 dataType: 'json',
								  		 contentType: 'application/json',
								  		 data: JSON.stringify(postData),
								  		 success : function(data){
								  			$scope.$apply(function(){$scope.profile=data;}); 
										 },
										 error : function(data){
											 modalService.showFailure("Fail","Error while saving.");
										 }
								  	  });
					},
				    function(){
				          availableRole.selected=!availableRole.selected;
					});
				
				} else {
					//add role
					var uuu = "profile/addNewRole?profile_id=" + $scope.profileId;
					modalService.popupConfirmWinWithCancel("Confirm","You are about to add the role "+availableRole.name+" from the profile for "+$scope.profile.firstName+" "+$scope.profile.lastName+". Do you want to continue?",
			    			function(){
									var postData={role:availableRole};
								  	  $.ajax({
								  		 type : 'POST',
								  		 url : uuu,
								  		 dataType: 'json',
								  		 contentType: 'application/json',
								  		 data: JSON.stringify(postData),
								  		 success : function(data){
								  			$scope.$apply(function(){$scope.profile=data;}); 
										 },
										 error : function(data){
											 modalService.showFailure("Fail","Error while saving.");
										 }
								  	  });
						
			    	},function(){
				          availableRole.selected=!availableRole.selected;
			    	})
				}
				
				  
		};

		$scope.removeRole = function(role) {
			modalService.popupConfirmWin("Confirm","You are about to remove the role "+role.name+" from the profile for "+$scope.profile.firstName+" "+$scope.profile.lastName+". Do you want to continue?",
	    			function(){
							  var uuu = "profile/removeRole?profile_id=" + $scope.profileId;
							  var postData={role:role};
						  	  $.ajax({
						  		 type : 'POST',
						  		 url : uuu,
						  		 dataType: 'json',
						  		 contentType: 'application/json',
						  		 data: JSON.stringify(postData),
						  		 success : function(data){
						  			$scope.$apply(function(){
						  				$scope.profile=data;
						  				$.each($scope.availableRoles, function(k, c){ 
						  			    	if(c.id === role.id) {
						  			    		c.selected = false;
						  			    	}
						  			    });
						  			});
						  			
								 },
								 error : function(data){
									 modalService.showFailure("Fail","Error while saving.");
								 }
						  	  });
				
	    	})
			
		};
		
		function initDropdownWithLookUp(arr,selectedValue){
			var dropdownArray=[];
			var selected = null;
			if(arr){
				for(var i = 0,l = arr.length; i < l; i++) {
					var option = { 
					        "index" : i ,
					        "value" : arr[i].value,
					        "title" : arr[i].label
					    };
					dropdownArray.push(option);
					if(arr[i].value === selectedValue){
						selected = option;
					}
				}
			}
			var dropDown={};
			dropDown.options = dropdownArray;
			dropDown.selected = selected;
			return dropDown;
		};
		
		$scope.doRolePopup = function() {	
			 var modalInstance = $modal.open({
			    templateUrl: 'roles_popup.html',
			    controller: 'rolepopupController',
			    resolve: {
			    	 message: function () {
			    		 var message ={
			    				 availableRoles: $scope.availableRoles
		        		  };
			          return message;
			        }
			      }
			  });   
			 	modalInstance.result.then(function (opts) {
					if(opts!=null){
						$scope.profile=opts.profile;
					}		
	           });
		} 
		
		$scope.close = function(){
			$('#dialog').dialog('close');
		}
	

});
</script>
