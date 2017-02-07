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
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>
<%@ include file="/WEB-INF/fusion/jsp/popup_modal_rolefunction.html" %>
<%@ include file="/WEB-INF/fusion/jsp/popup_modal_role.html" %>
<link rel="stylesheet" type="text/css" href="static/fusion/css/jquery-ui.css">

<div ng-controller="postSearchController" >

<h1 class="heading1" style="margin-top:20px;">WEBPHONE Search</h1>
      <br>
  		Please enter search criteria below:<br/>

            <div class="fn-ebz-container">
	          Last Name:<br/>
	          <input class="fn-ebz-text" type="text"   ng-model="postSearchBean.lastName" value="" size="25">
            </div>
          
            <div class="fn-ebz-container">
	          First Name:<br/>
	          <input class="fn-ebz-text" type="text" ng-model="postSearchBean.firstName" value="" size="25">
            </div>
         
            <div class="fn-ebz-container">
	          UserId:<br/>
	          <input class="fn-ebz-text" type="text" ng-model="postSearchBean.orgUserId" value="" size="25">
            </div>
           
            <div class="fn-ebz-container">
	          Manager OrgUserId:<br/>
	          <input class="fn-ebz-text" type="text" ng-model="postSearchBean.orgManagerUserId" value="" size="25">
            </div>
           <br>
            <div class="fn-ebz-container">
	          Organization:<br/>
	          <input class="fn-ebz-text" type="text" ng-model="postSearchBean.orgCode" value="" size="25">
            </div>
           
            <div class="fn-ebz-container">
	          Email:<br/>
	          <input class="fn-ebz-text" type="text" ng-model="postSearchBean.email" value="" size="25">
            </div>
     <br>
<!--      Sort By: <br/>
         	<div class="fn-ebz-container" >
       
 		<div class="fn-ebz-container">	
			<div class="form-field" att-select="sortByList" ng-model="postSearchBean.sortBy1"></div>
		</div>
        </div>
        
       
        

            <div class="fn-ebz-container" >
           <br/>
            &nbsp;

		<div class="fn-ebz-container">	
			<div class="form-field" att-select="sortByList" ng-model="postSearchBean.sortBy2"></div>
		</div>
       </div>
        

            <div class="fn-ebz-container" >
            <br/> &nbsp;

		<div class="fn-ebz-container">	
			<div class="form-field" att-select="sortByList" ng-model="postSearchBean.sortBy3"></div>
		</div>
        </div> -->
		<div>
		 	<input att-button btn-type="primary" size="small" class="button" type="submit" value="Search" ng-click="search(postSearchBean);" />
		
		     <input att-button btn-type="primary" size="small" class="button" type="submit" value="Reset"  ng-click="reset();"/>
		</div>
		<br>
		{{noResultsString}}
      <div ng-if="profileList.length != 0">
  	    <table att-table table-data="profileList" view-per-page="viewPerPage" current-page="currentPage" search-category="searchCategory" search-string="searchString" total-page="totalPage">
		  <thead att-table-row type="header" >
		    <tr>
  			<th att-table-header  width="5%" align="left">No</th>
  			<th att-table-header  width="30%" key="lastName" align="left">Name</th>
  			<th att-table-header  width="5%"  key="orgUserId" align="left">OrgUserId</th>
  			<th att-table-header  width="10%" key="orgCode" align="left">Organization</th>
  			<th att-table-header  width="20%" align="left">Phone</th>
  			<th att-table-header  width="20%" key="email" align="left">Email</th>
  			<th att-table-header  width="10%" align="left">Import?</th>
  			</tr>
		  </thead>
         	
            <tbody att-table-row type="body" row-repeat="profile in profileList" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody-->	
              <tr >
              	<td att-table-body width="5%" align="left">
                {{$index + 1}}
                </td>
            	<td att-table-body width="30%" align="left">                	
	              <div ng-if="ngexistingUsers[profile.orgUserId] == null">
        	          {{profile.lastName}},&nbsp;{{profile.firstName}}
            	  </div>
	              <div ng-if="ngexistingUsers[profile.orgUserId] != null">
    	              <a href="profile.htm?profile_id={{ngexistingUsers[profile.orgUserId]}}" alt="View/Edit Profile">
                  	  {{profile.lastName}},&nbsp;{{profile.firstName}}
	                  </a>
            	   </div>
               
                </td>
            	<td att-table-body width="5%" align="left">
                {{profile.orgUserId}}
                </td>
            	<td att-table-body width="10%" align="left">
                {{profile.orgCode}}
                </td>
            	<td att-table-body width="20%" align="left">
                {{profile.phone}}
                </td>
            	<td att-table-body width="20%" align="left">
                {{profile.email}}
                </td>
                
            	<td att-table-body width="10%" align="left">
		            <div ng-if="ngexistingUsers[profile.orgUserId] == null">
		            	<div ng-click="toggleSelection(profile);">
		                <input name="selected" type="checkbox" ng-model="profile.selected" att-checkbox/>
		                </div>
		             </div>
		             <div ng-if="ngexistingUsers[profile.orgUserId] != null">
		                Exists
		             </div>
                </td>
              </tr>
              </tbody>
	    </table>
	    <div class="fn-ebz-container">
	          Rows Per Page:
	          <input class="fn-ebz-text" type="text" ng-model="viewPerPage" size="5" style="width: 47px;">
        </div>
		<div class="fn-ebz-container">
	          Current Page:
	          <input class="fn-ebz-text" type="text" ng-model="currentPage" size="5" style="width: 47px;">
        </div>
        <div class="fn-ebz-container">
	          Total Page(s):
	          <input class="fn-ebz-text" type="text" ng-model="totalPage" size="5" readonly="true" style="width: 47px;">
        </div>
		
        <div ng-if="profileList.length != 0">
          <input att-button btn-type="primary" size="small" class="button" type="submit" value="Import" ng-click="process();"/>
        </div>

      </div>




</div>
<script>
	app.controller('postSearchController', function ($scope,modalService){ 

		$scope.viewPerPage = 200;
		$scope.currentPage = 1;
		$scope.totalPage;
		$scope.searchCategory = "";
		$scope.searchString = "";
		
		$scope.noResultsString = "";
		
		$scope.postSearchBean=${postSearchBean};
		$scope.profileList=${profileList};
		//console.log($scope.postSearchBean);
		
		$scope.ngexistingUsers=${existingUsers};
		console.log($scope.ngexistingUsers);
		
		$scope.ocisortByList=${sortByList};
		$scope.sortByList = [];
		if($scope.ocisortByList)
		$.each($scope.ocisortByList, function(i, a){ 
			var sortBy = {"index":i, "value":a.value, "title":a.label};
		    $scope.sortByList.push(sortBy);
		});	
		;
	
		$scope.search = function(postSearchBean) {
				console.log(postSearchBean);
				var uuu = "post_search/search";
				  var postData={postSearchBean:postSearchBean};
			  	  $.ajax({
			  		 type : 'POST',
			  		 url : uuu,
			  		 dataType: 'json',
			  		 contentType: 'application/json',
			  		 data: JSON.stringify(postData),
			  		 success : function(data){
			  			$scope.$apply(function(){
			  				$scope.profileList=data.postSearchBean.searchResult;
			  				//$scope.postSearchBean={};
			  				console.log($scope.profileList);
			  				if($scope.profileList.length == 0)
			  					$scope.noResultsString = "There is currently no one in WEBPHONE matching your search criteria.";
			  				else
			  					$scope.noResultsString = "";
			  				});  
					 },
					 error : function(data){
						 console.log(data);
						 modalService.showFailure("Fail","Error while searching: "+ data.responseText);
					 }
			  	  });
			
		};
		
		$scope.reset = function() {
				$scope.postSearchBean={};
				$scope.profileList=[];
				$scope.noResultsString = "";
				//console.log($scope.postSearchBean);
		};
		
		$scope.process = function() {
			$scope.prepareProfileSelection();
			var uuu = "post_search/process";
			  var postData={postSearchBean:$scope.postSearchBean};
		  	  $.ajax({
		  		 type : 'POST',
		  		 url : uuu,
		  		 dataType: 'json',
		  		 contentType: 'application/json',
		  		 data: JSON.stringify(postData),
		  		 success : function(data){
		  			$scope.$apply(function(){
		  				$scope.profileList=data.postSearchBean.searchResult;
		  				//$scope.postSearchBean={};
		  				$scope.ngexistingUsers=data.existingUsers;
		  				$scope.postSearchBean.selected=null;		  				
		  				});  
				 },
				 error : function(data){
					 console.log(data);
					 modalService.showFailure("Fail","Error while searching: "+ data.responseText);
				 }
		  	  });
		
	};
	$scope.importProfileList=[];
	$scope.toggleSelection = function(profile) {
		if(profile.selected)
			$scope.importProfileList.push(profile);
		else{			
			var index = $scope.importProfileList.indexOf(profile);
		 	if(index>=0)
				$scope.importProfileList.splice(index, 1);
		}
	};
	
	$scope.prepareProfileSelection = function() {
		if($scope.importProfileList)
			$.each($scope.importProfileList, function(i, profile){ 
			    $scope.preparePostSearchBean(profile);
			});	
			;
	}
	
	$scope.preparePostSearchBean = function(profile) {
		//console.log('Importing: '+profile.orgUserId);
		//console.log('ngexistinguser:'+$scope.ngexistingUsers[profile.orgUserId])
		if($scope.postSearchBean.selected==null){
			$scope.postSearchBean.selected=[];
			$scope.postSearchBean.postOrgUserId=[];
			$scope.postSearchBean.postHrid=[];
			$scope.postSearchBean.postFirstName=[];
			$scope.postSearchBean.postLastName=[];
			$scope.postSearchBean.postOrgCode=[];
			$scope.postSearchBean.postPhone=[];
			$scope.postSearchBean.postEmail=[];
			$scope.postSearchBean.postAddress1=[];
			$scope.postSearchBean.postAddress2=[];
			$scope.postSearchBean.postCity=[];
			$scope.postSearchBean.postState=[];
			$scope.postSearchBean.postZipCode=[];
			$scope.postSearchBean.postLocationClli=[];
			$scope.postSearchBean.postBusinessCountryCode=[];
			$scope.postSearchBean.postBusinessCountryName=[];
			$scope.postSearchBean.postDepartment=[];
			$scope.postSearchBean.postDepartmentName=[];
			$scope.postSearchBean.postBusinessUnit=[];
			$scope.postSearchBean.postBusinessUnitName=[];
			$scope.postSearchBean.postJobTitle=[];
			$scope.postSearchBean.postOrgManagerUserId=[];
			$scope.postSearchBean.postCommandChain=[];
			$scope.postSearchBean.postCompanyCode=[];
			$scope.postSearchBean.postCompany=[];
			$scope.postSearchBean.postCostCenter=[];
			$scope.postSearchBean.postSiloStatus=[];
			$scope.postSearchBean.postFinancialLocCode=[];
		}
			
		$scope.postSearchBean.selected.push(profile.orgUserId);	
		$scope.postSearchBean.postOrgUserId.push(profile.orgUserId);
		$scope.postSearchBean.postHrid.push(profile.hrid);
		$scope.postSearchBean.postFirstName.push(profile.firstName);
		$scope.postSearchBean.postLastName.push(profile.lastName);
		$scope.postSearchBean.postOrgCode.push(profile.orgCode);
		$scope.postSearchBean.postPhone.push(profile.phone);
		$scope.postSearchBean.postEmail.push(profile.email);
		$scope.postSearchBean.postAddress1.push(profile.address1);
		$scope.postSearchBean.postAddress2.push(profile.address2);
		$scope.postSearchBean.postCity.push(profile.city);
		$scope.postSearchBean.postState.push(profile.state);
		if(profile.zipCodeSuffix==null)
			$scope.postSearchBean.postZipCode.push(profile.zipCode);
		else
			$scope.postSearchBean.postZipCode.push(profile.zipCode+'-'+profile.zipCodeSuffix);
		$scope.postSearchBean.postLocationClli.push(profile.locationClli);
		$scope.postSearchBean.postBusinessCountryCode.push(profile.businessCountryCode);
		$scope.postSearchBean.postBusinessCountryName.push(profile.businessCountryName);
		$scope.postSearchBean.postDepartment.push(profile.department);
		$scope.postSearchBean.postDepartmentName.push(profile.departmentName);
		$scope.postSearchBean.postBusinessUnit.push(profile.businessUnit);
		$scope.postSearchBean.postBusinessUnitName.push(profile.businessUnitName);
		$scope.postSearchBean.postJobTitle.push(profile.jobTitle);
		$scope.postSearchBean.postOrgManagerUserId.push(profile.orgManagerUserId);
		$scope.postSearchBean.postCommandChain.push(profile.commandChain);
		$scope.postSearchBean.postCompanyCode.push(profile.companyCode);
		$scope.postSearchBean.postCompany.push(profile.company);
		$scope.postSearchBean.postCostCenter.push(profile.costCenter);
		$scope.postSearchBean.postSiloStatus.push(profile.siloStatus);
		$scope.postSearchBean.postFinancialLocCode.push(profile.financialLocCode);
	};
		
	});	
</script> 
