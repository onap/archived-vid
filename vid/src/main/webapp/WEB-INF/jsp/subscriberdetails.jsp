<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>
<div ng-controller="subscriberDetailsController">
	<div>
		<h1 class="heading1" style="margin-top:20px;">Selected Subscriber's Service Instance Details:</h1>
		<div style="margin-top:30px">
		 	<table att-table table-data="tableData">

			    <thead  att-table-row type="header">
					<tr>
					    <th att-table-header key="viewSubDetails">View/Edit</th>
						<th att-table-header key="global-customer-id">Global Customer ID</th>
			            <th att-table-header key="subscriber-name">Subscriber Name</th>        
			            <th att-table-header key="service-type">Service Type</th>    
			            <th att-table-header key="service-instance-id">Service Instance ID</th>
			        </tr>
			    </thead>
			    <tbody att-table-row type="body" row-repeat="disData in displayData">
			        <tr>
			        <td att-table-body>
			         <div ng-controller="viewEditSubController">
    	              <a href="#" ng-click="postsubscriberID(disData.subscriberName)" alt="View/Edit">
                  	     View/Edit
	                  </a>
            	   </div>
            	   </td>
		            	<td att-table-body >{{disData.globalCustomerId}}</td>
		            	<td att-table-body >{{disData.subscriberName}}</td>
		            	<td att-table-body >{{disData.serviceType}}</td>
		            	<td att-table-body >{{disData.serviceInstanceId}}</td>
			        </tr>     
			    </tbody>	  
			</table> 
		</div>
	</div>	
	<table><tr>
	<td>
	<div ng-controller="searchExistingSIController" align="left" >
		<button type="submit" ng-click="cancelSubDetails();" att-button
			btn-type="primary" size="small">Cancel</button>
	</div>
	</td></tr>
	
	</table>
	
</div>
<!--  Temporary solution for footer overlapping the men after talking to EComp SDK developer on 06/25/2016 -->

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
		
<script>
app.controller("subscriberDetailsController", function ($scope,$http,modalService, $modal) { 
	$scope.tableData=${model.customerInfo};
	$scope.displayData= [];
	$scope.subscriberData=[];
	$scope.serviceInstanceData= new Array();
	$scope.globalcustomerid = $scope.tableData['global-customer-id'];
	$scope.subscriberName = $scope.tableData['subscriber-name'];
	$scope.subscriberData = $scope.tableData['service-subscriptions'];
	   
	 $scope.setDisplaynoSTSI = function(){
    	 $scope.displayData.push({
				globalCustomerId : $scope.globalcustomerid,
			    subscriberName   :  $scope.subscriberName,
			    serviceType : "No Service Subscription Found",
			    serviceInstanceId : "No Service Instance Found"
			});
     }
	 
	 $scope.setDisplaynoSI = function(servcType){
    	 $scope.displayData.push({
				globalCustomerId : $scope.globalcustomerid,
			    subscriberName   :  $scope.subscriberName,
			    serviceType : servcType,
			    serviceInstanceId : "No Service Instance Found"
			});
     }
	 
	if ($scope.subscriberData != null)
		{
		    if ($scope.numberofSubscribers = $scope.subscriberData['service-subscription'] != null)
		    	{
					$scope.numberofSubscribers = $scope.subscriberData['service-subscription'].length;
					if ($scope.numberofSubscribers > 0)
						{
						    var index = 0;
							for(i=0; i < $scope.numberofSubscribers; i++) {
								$scope.serviceInstanceData[i] = new Array();
								var servicesubscription = $scope.subscriberData['service-subscription'][i]; 
								$scope.serviceInstanceData[i] = servicesubscription['service-instances'];
								if ($scope.serviceInstanceData[i] != null)
									{
									    if ($scope.serviceInstanceData[i]['service-instance'] != null)
									    	{
												var numberofserviceInstance = $scope.serviceInstanceData[i]['service-instance'].length;
												if (numberofserviceInstance > 0)
													{
														for(j=0; j < numberofserviceInstance; j++)
														{
															$scope.displayData.push({
																globalCustomerId : $scope.globalcustomerid,
															    subscriberName   :  $scope.subscriberName,
															    serviceType : servicesubscription['service-type'],
															    serviceInstanceId : $scope.serviceInstanceData[i]['service-instance'][j]['service-instance-id']
															});
														}
													}
												else
													{
													   $scope.setDisplaynoSI(servicesubscription['service-type']);
													}
									    	}
									    else
										{
									    	$scope.setDisplaynoSI(servicesubscription['service-type']);
										}
									}
								else
									{
									   $scope.setDisplaynoSI(servicesubscription['service-type']);
									}
					        }
						}
					else
						{
						  $scope.setDisplaynoSTSI();
						}
		    	}
		    else
				{
		    	   $scope.setDisplaynoSTSI();
				}
			}
		else
		{
			 $scope.setDisplaynoSTSI();
		}
	
	
});

app.controller('viewEditSubController',['$scope','$http',function ($scope,$http) { 
	
	$scope.postsubscriberID = function(subID){
		   $.ajax({
		            url: "vieweditsub/subedit?"+"subscriberID="+encodeURIComponent(subID),
	                type : "POST",
	                success:function (response){
	                    window.location.href = 'instantiate.htm';
	                },
	                error:function( jqXHR, status,error ){
	                	$("#errorInfo").show();
	                }
	                
	        });
		}
	
}]);

app.controller('searchExistingSIController',['$scope','$http',function ($scope,$http) { 
	
	$scope.cancelSubDetails = function(selectedSubscriber){
 		
		window.location.href = 'searchexistingsi.htm';
		
	}
	
}]);
</script>

    
