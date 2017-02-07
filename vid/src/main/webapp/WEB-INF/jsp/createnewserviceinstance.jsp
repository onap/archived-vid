<script>
app.controller('createNewSIController',['$scope','$http',function ($scope,$http) { 
	
    $scope.searchCategory = "";
    $scope.searchString = "";
 	$scope.tableData=[];
 	$scope.displayData=[];
 	$scope.selectedSubscriber = $scope.tableData[0]; 
    $scope.selectedServiceType = "Select Service Type";
 	function escapeRegExp(str) {
 		return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|\&]/g, " ");
 	}
 	$scope.serviceTypes=["Select Service Type"];
 	
 	$scope.getSubscribers = function(refresh){
 
 		var refreshTxt = 'refresh_subscribers';
 		var getTxt = 'get_subscribers';
 		var selectedTxt = getTxt;
 		if (refresh)
 			selectedTxt = refreshTxt;
		$http.get(selectedTxt).
			then(function(response){
				var data = JSON.parse(response.data.result);
				$scope.tableData = data['customer'];
				$scope.initSubscriberList();
			})
			.catch(function(data,status){
			console.log("Error: " + status + " : " + data); 					
			})
			.finally(function() {
				
			});
 		
 	}
 

 	$scope.initSubscriberList = function () {
 		//$scope.selectedSubscriber = $scope.selectedSubscriber['subscriber-name'] || $scope.tableData[0]['subscriber-name'];
 		$scope.selectedSubscriber = 'Select Subscriber Name';
 	}
 	
 	$scope.initSrvcTypeList = function () {
 		$scope.selectedServiceType = $scope.serviceTypes[0];  
 	}

 	$scope.getSubscriberDetails = function(selectedSubscriber){
		if (selectedSubscriber == 'Select Subscriber Name')
			return;
 		$http.get('createsubscriber/' + encodeURIComponent(selectedSubscriber)).
		then(function(response){
		//	window.location.href = 'createsubscriberdetails.htm';
		})
		.catch(function(data,status){
		console.log("Error: " + status + " : " + data); 					
		})
		.finally(function() {
			
		});
	}
	
	$scope.disableSubmitBtn = function (subName, svcType) {
		return (typeof subName === "undefined") && (svcType == 'Select Service Type');
 		//return (subName == 'Select Subscriber Name') && (svcType == 'Select Service Type');
 	}
	
    $scope.cancelCreateSI = function(selectedSubscriber){
 		
		window.location.href = 'vidhome.htm';
		
	}
	
 	$scope.getSubscribers(false);
 	//$scope.initSubscriberList();
 	
}]);


</script>

<div ng-controller="createNewSIController">
     
     <h1 class="heading1"><center>Create New Service Instance</center></h1>
	

<BR>
 <table> <tr>
   
   <td width="5%" align="left">
	<!--  <a ng-click="getSubscriberDetails(selectedSubscriber['global-customer-id']);" ng-disabled="disableSubmitBtn(selectedSubscriber['subscriber-name'], selectedServiceType);" ><img src="static/fusion/images/plus.png"></a>-->
  <input type="image" ng-click="getSubscriberDetails(selectedSubscriber['global-customer-id']);" ng-disabled="disableSubmitBtn(selectedSubscriber['subscriber-name'], selectedServiceType);" src="static/fusion/images/plus.png"/>
   </td>
   
	<td style="width:10%" align="left"><div class="fn-ebz-container">	
		<label  class="fn-ebz-text-label">Subscriber:</label>
		</div> </td>
	<td style="width:30%"><div class="fn-ebz-container">
		<select  ng-model="selectedSubscriber"  ng-options="resultdata['subscriber-name'] for resultdata in tableData" required>
		<option value="">Select Subscriber Name</option>
		</select>
	</div></td>
	 <td width="5%" align="left">
		  <input type="image" ng-click="getSubscribers(true);" src="static/fusion/images/refresh.jpg"/>
      </td>
	
	<td width="5%"> </td>
<td style="width:10%" align="left">

    
	<div class="fn-ebz-container">	
		<label  class="fn-ebz-text-label">Service Type:</label>
	</div> </td>
    <td style="width:32%"><div class="fn-ebz-container">
		<select  ng-model="selectedServiceType"  ng-options="sType for sType in serviceTypes" ng-init="initSrvcTypeList();" ></select>
	</div> </td>
	
	<td width="5%"> </td>
	
    </tr>
    </table>
 
	<center>
		<button type="cancel" ng-click="cancelCreateSI();" att-button size="small">Cancel</button>
	</center>
	


</div>
<!--  Temporary solution for footer overlapping the men after talking to EComp SDK developer on 06/16/2016 -->
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>


