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
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties" %>
<c:set var="title" value="Login" />
<c:set var="isMobileEnabled"
	value="<%=(SystemProperties.getProperty(SystemProperties.MOBILE_ENABLE)!= null && SystemProperties.getProperty(SystemProperties.MOBILE_ENABLE).trim().equals(\"true\"))%>" />

<!DOCTYPE html>
<html ng-app="abs">
	<head>

	    <title>
	      VID Login
        </title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
		<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/fn-ebz.css" >
        <link rel="stylesheet" type="text/css" href="static/fusion/css/jquery-ui.css">
		<script src="static/js/jquery-1.10.2.js" type="text/javascript"></script>
		<script src= "app/fusion/external/ebz/angular_js/angular.js"></script> 
		<script src="app/fusion/external/ebz/angular_js/angular-route.min.js"></script>
		<script src= "app/fusion/external/ebz/angular_js/angular-sanitize.js"></script>
		<script src= "app/fusion/external/ebz/angular_js/angular-cookies.js"></script>
		<script src= "app/fusion/external/ebz/sandbox/att-abs-tpls.js" type="text/javascript"></script>
		<script src="static/fusion/js/att_angular_gridster/ui-gridster-tpls.js"></script>
		<script src="static/fusion/js/att_angular_gridster/angular-gridster.js"></script>	
		<script src= "app/fusion/external/ebz/angular_js/checklist-model.js"></script>
		<script src= "app/fusion/external/ebz/angular_js/app.js"></script>
		<script src= "app/fusion/external/ebz/angular_js/gestures.js"></script>
		<script src="static/js/modalService.js"></script>
		<script src="app/fusion/external/angular-ui/ui-bootstrap-tpls-1.1.2.min.js"></script>
	
		<style>
		.terms {
			font-family: Verdana,Arial,Helvetica, sans-serif;
			font-size: 9px;
		}
		
		</style>
	</head>
	<body style="padding-top: 15px;">
	<div ng-controller="externalLoginController">
		<div class="centered style="-webkit-transform: translateZ(0);background:white, z-index:0;">
			<div align="center" id="errorInfo" style="display:none;float:left;font-size:12px;margin-left:5px"><span style="color:red">Invaild username or password, Please try again</span></div>
			<br/>
			<div align="center" style="margin-left:auto;margin-right:auto;width:40%;padding:6px;opacity:0.7;background-color:white">
          		<img src="static/fusion/images/ecomp_trans.png"/>
				<h2> VID ECOMP Portal </h2>
				<label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label class="form-field__label">Login ID:</label>
				</label>
				<input  type="text" class="fn-ebz-text" ng-model="loginId" style="width: 140px;height:25px;border-radius:7px;font-size:18px;padding-left:5px;"
					maxlength="30" />
				<br/>
				<br/>
				<label >&nbsp;Password:</label>
				<input type="password" class="span3" ng-model="password" style="width: 140px;height:25px;border-radius:7px;font-size:18px;padding-left:5px;"
					maxlength="30" onkeydown="if (event.keyCode == 13) document.getElementById('loginBtn').click()"/> 
				<br />
				<br />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="loginBtn" type="image" ng-click="loginExternal();" src="static/fusion/images/login_button.gif" alt="Login" />
				<br>
			</div>
		</div>
		<br/><br/><br/><br/><br/><br/><br/>
		<div id="footer">
		</div>
    </div>
    </body>
<script>
app.controller("externalLoginController", function ($scope) { 
	// Table Data
	
	$scope.viewPerPage = 200;
	$scope.currentPage = 2;
	$scope.totalPage;
	$scope.searchCategory = "";
	$scope.searchString = "";
	$scope.loginId="";
	$scope.password="";
	$scope.loginError=true;
	$scope.viewPerPage = 200;
	$scope.currentPage = 2;
	$scope.totalPage;
	$scope.searchCategory = "";
	$scope.searchString = "";
	$scope.loginId="";
	$scope.password="";
	
	
	$scope.loginExternal = function() {
		
		$.ajax({
	            url: "login_external/login?"+"loginId="+$scope.loginId+"&password="+$scope.password,
                type : "POST",
                success:function (response){
                  if(response=="success"){
                    window.location.href = 'vidhome.htm';
                  }else{
                	$("#errorInfo").show();
                  }
                },
                error:function( jqXHR, status,error ){
                	$("#errorInfo").show();
                }
                
        });

    };
	
	
	/**
	$scope.loginExternal = function() {

        
		  var redirectUrl = "login_external/login";
          var form = $('<form action="' + redirectUrl + '" method="post">' +
                  '<input type="hidden" name="loginId" value='+$scope.loginId+' />' +
                  '<input type="hidden" name="password" value='+$scope.password+' />' +
                  '</form>');
                  
                  $('body').append(form);
                  $(form).submit();
         

    };
    */
	
});
</script>
</html>
