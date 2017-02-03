/*-
 * ================================================================================
 * eCOMP Portal SDK
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
app.controller('droolsViewController', function ($scope,modalService,droolsService){ 


		$scope.resultsString = "";
		// Table Data
	    droolsService.getDroolDetails(droolsService.getSelectedFile()).then(function(data){
			
			var j = data;
			$scope.postDroolsBean = JSON.parse(j.data);
			//execute($scope.postDroolsBean);
		
		},function(error){
			console.log("failed");
			//reloadPageOnce();
		});
		
	
		
		$scope.execute = function(postDroolsBean) {
				console.log(postDroolsBean);
				var uuu = "post_drools/execute";
				  var postData={postDroolsBean:postDroolsBean};
			  	  $.ajax({
			  		 type : 'POST',
			  		 url : uuu,
			  		 dataType: 'json',
			  		 contentType: 'application/json',
			  		 data: JSON.stringify(postData),
			  		 success : function(data){
			  			$scope.$apply(function(){
			  				$scope.resultsString=data.resultsString;
			  				console.log($scope.resultsString);
			  				});  
					 },
					 error : function(data){
						 console.log(data);
						 modalService.showFailure("Fail","Error while executing: "+ data.responseText);
					 }
			  	  });
			
		};
		
		
		
	});	
