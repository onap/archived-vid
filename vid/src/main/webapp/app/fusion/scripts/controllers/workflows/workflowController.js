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
app.controller('workflowsController', function($scope, $http, $uibModal, $log, modalService, $modal) {
		
	$scope.viewPerPage = 5;
    $scope.scrollViewsPerPage = 20;
    $scope.currentPage = 2;
    $scope.totalPage;
    $scope.searchCategory = "";
    $scope.searchString = "";
    $scope.radio = {
        value: ""
    };
	
	
    $scope.showModal = false;
    $scope.toggleModal = function(){
        $scope.showModal = !$scope.showModal;
    };

    $scope.workflow = {};
    $scope.workflow.active = "true";

    $scope.updateAllWorkflowStatus = function() {
		angular.forEach($scope.workflows,function(value){
			$scope.checkWorkflowStatus(value);
	    })
	}

    $scope.fetchWorkflowsList = function() {
        $http.get('workflows/list').then(function(workflowList){
        	console.log('Got new list from server = ' + workflowList.data);
            $scope.workflows = workflowList.data;
            $scope.updateAllWorkflowStatus();
        });    	
    };

    $scope.addNewWorkflow = function(newWorkflow) {
        $http.post('workflows/addWorkflow/', JSON.stringify(newWorkflow)).success(function() {
            $scope.fetchWorkflowsList();
        });
        
        $scope.workflow.name = '';

    };

    $scope.updateWorkflow = function (workflowToEdit) {
    	//workflowToEdit.active='true';
		 var modalInstance = $uibModal.open({
			 animation: $scope.animationsEnabled,
			 templateUrl: 'app/fusion/scripts/view-models/workflows/workflow-new.html',
			 //size : modalSize,
			 controller: ['$scope', '$uibModalInstance', '$http', function ($scope, $uibModalInstance, $http) {
		    		$scope.workflow = workflowToEdit;		    		
					$scope.ok = function() {	      		  
		              console.log('Updating existing workflow ... ' + JSON.stringify($scope.workflow));	              
		              $http.post('workflows/editWorkflow/', JSON.stringify($scope.workflow)).then(function(returnedWorkflow){
		            	  console.log('Returned Workflow = ' + JSON.stringify(returnedWorkflow));
		            	  $uibModalInstance.close($scope.workflow);
		              });
		      	  };
		
		      	  $scope.cancel = function() {
		      		  $uibModalInstance.dismiss();
		      	  };
		    }],
		    //End of inner controller
		    resolve: {
		        workflow: function() {
		        	console.log('Passing ' + JSON.stringify($scope.workflow)); 
		        	return $scope.workflow; 
		        }
		    }
		  });

	      modalInstance.result.then(function (editedWorkFlow) {
	    	  //Need to convert to proper date - later
	    	  delete editedWorkFlow.created;
	    	  delete editedWorkFlow.updated;
	    	  
	    	  delete editedWorkFlow.createdBy;
	    	  delete editedWorkFlow.modifiedBy;
	    	  
	    	  console.log('selected Item ' + JSON.stringify(editedWorkFlow));	    	  
	    	  $scope.$emit('workflowAdded', editedWorkFlow);
	    	  
	      }, function () {
	        $log.info('Modal dismissed at: ' + new Date());
	      });
    };
    
    $scope.reset = function(){
    	console.log("Resetting ....");
    };
      
	$scope.update = function(){
		console.log("updating ....");
	};

    $scope.createWorkflow = function (modalSize) {
    	
		 var modalInstance = $uibModal.open({
		 animation: $scope.animationsEnabled,
		 templateUrl: 'app/fusion/scripts/view-models/workflows/workflow-new.html',
		 size : modalSize,		 
		 controller: ['$scope', '$uibModalInstance', '$http', function ($scope, $uibModalInstance, $http) {
	    		$scope.workflow = {};
	    		$scope.workflow.active = 'true'; 
				$scope.ok = function() {	      		  
	              console.log('Saving new workflow ... ' + JSON.stringify($scope.workflow));	              
	              $http.post('workflows/addWorkflow/', JSON.stringify($scope.workflow)).then(function(returnedWorkflow){
	            	  console.log('Returned Workflow = ' + JSON.stringify(returnedWorkflow));
	            	  $uibModalInstance.close($scope.workflow);
	              });
	      	  };
/*	      	  console.log(size);*/
	      	  $scope.cancel = function() {
	      		  $uibModalInstance.dismiss();
	      	  };
	    }],
	    //End of inner controller
	    resolve: {
	        workflow: function() {
	        	console.log('Passing ' + JSON.stringify($scope.workflow)); 
	        	return $scope.workflow; 
	        }
	    }
	  });

      modalInstance.result.then(function (newWorkflow) {
    	  console.log('selected Item ' + JSON.stringify(newWorkflow));
    	  $scope.$emit('workflowAdded', newWorkflow);
    	  
      }, function () {
        $log.info('Modal dismissed at: ' + new Date());
      });
   };//End of createWorkflow function
   
 
   $scope.removeWorkflow = function(workflowToRemove){	   
	   var modalInstance = $uibModal.open({
			 animation: $scope.animationsEnabled,
			 templateUrl: 'app/fusion/scripts/view-models/workflows/workflow-remove.html',
			 controller: ['$scope', '$uibModalInstance', '$http', function ($scope, $uibModalInstance, $http) {
		    		$scope.workflowToRemove = workflowToRemove;
					$scope.ok = function() {	      		  
		              console.log('Removing workflow ... ' + JSON.stringify($scope.workflowToRemove) + ' on client request.');	              
		              $http.post('workflows/removeWorkflow/', JSON.stringify($scope.workflowToRemove.id)).then(function(){
		            	  console.log('Workflow successfully removed !!!');
		            	  $uibModalInstance.close();
		              });
		      	  };
		
		      	  $scope.cancel = function() {
		      		  $uibModalInstance.dismiss();
		      	  };
		    }]
		  });

	      modalInstance.result.then(function () {
	    	  $scope.$emit('workflowRemoved');	    	  
	      }, function () {
	        $log.info('Modal dismissed at: ' + new Date());
	      });
	   
	};
	
	
	
	   $scope.scheduleWorkflow = function(workflowToSchedule){	   
		   var modalInstance = $uibModal.open({
				 animation: $scope.animationsEnabled,
				 templateUrl: 'app/fusion/scripts/view-models/workflows/workflow-schedule.html',
				 size:'lg',

				 controller: ['$scope', '$uibModalInstance', '$http','dateFilter', function ($scope, $uibModalInstance, $http,dateFilter) {
					 
					 $scope.workflowToSchedule = workflowToSchedule;
					 $scope.dt = new Date();
					 $scope.dt2 = new Date();
					 $scope.dateformat = 'MM/dd/yyyy',
					 $scope.datetimeformat = "hh:mm a";
					 
					 $scope.recurrenceOptions =[{						   
						  index:0, value:'One-Time', title:'One-Time'
					 },{
						 index:1, value: 'Hourly',title:'Hourly'
					 },{
						 index:2, value: 'Daily',title:'Daily'
					 },{
						 index:3, value: 'Weekly',title:'Weekly'								 		 		 
			 }]					 
			 $scope.selectRecurrenceOpt = $scope.recurrenceOptions[0];					 
					 
					 $scope.hours = [];
					 for (var i=0; i<24; i++){
						 var newObj={}
						 newObj.index = i;
						 newObj.value = ""+i;
						 newObj.title = ""+i;
						 $scope.hours.push(newObj);
					 }
					 
					 $scope.minutes = [];
					 for (var i=0; i<60; i++){
						 var newObj={}
						 newObj.index = i;
						 newObj.value = ""+i;
						 newObj.title = ""+i;
						 $scope.minutes.push(newObj);
					 }					 
					 
					 $scope.AMPMOptions =[
					   {
						  index:0, value:'AM', title:'AM'
					 },{
						 index:1, value: 'PM',title:'PM'
					 }]
					 
					 $scope.selectFirstHour =$scope.hours[0];					 
					 $scope.selectFirstMinute =$scope.minutes[0];
					 
					 $scope.selectLastHour =$scope.hours[0];					 
					 $scope.selectLastMinute =$scope.minutes[0];					 					 	
					 
					 $scope.selectStartAMPMOption=$scope.AMPMOptions[0];
					 $scope.selectLastAMPMOption=$scope.AMPMOptions[0];

					 var GenerateCronExpression = function(trigger_dt, RecurrenceOpt) {
					 	var CRON_sec = trigger_dt.getSeconds();
					 	var CRON_min = trigger_dt.getMinutes();
					 	var CRON_hr = trigger_dt.getHours();
					 	var CRON_date= trigger_dt.getDate();
					 	var CRON_month = trigger_dt.toLocaleString('en-US', {month: 'short'}).toUpperCase();
					 	var CRON_day = trigger_dt.toLocaleString('en-US', {weekday: 'short'}).toUpperCase();
					 	var CRON_year = trigger_dt.getFullYear();
					 	if (RecurrenceOpt ==="One-Time") {
					 		CRON_day = '?'
						} else {
							if (RecurrenceOpt ==="Hourly") {
								CRON_hr = '*';
								CRON_date = '*'
								CRON_month = '*'
								CRON_day = '?'
								CRON_year = '*'
							} else if (RecurrenceOpt ==="Daily") {
								CRON_date = '*'
								CRON_month = '*'
								CRON_day = '?'
								CRON_year = '*'
							} else if (RecurrenceOpt ==="Weekly") {
								CRON_date = '*'
								CRON_month = '*'
								CRON_year = '*'			
							}
						}

						var CRON_Expression = [CRON_sec, CRON_min, CRON_hr, CRON_date, CRON_month, CRON_day, CRON_year];
						return CRON_Expression.join(" ");
					}

			    		$scope.ok = function() {
						 	
							// DateTime for the start time: it should be noted that the start time
						 	// for a CRON job should be prior to the trigger time.
							$scope.trigger_dt = new Date( $scope.dt.getFullYear() +
												"-" + ("0"+($scope.dt.getMonth()+1)).slice(-2) +
												"-" +("0"+ $scope.dt.getDate()).slice(-2) +
												" " + ("0" + $scope.selectFirstHour.value).slice(-2) +
												":" +("0" + $scope.selectFirstMinute.value).slice(-2) +
												":00.0");

							$scope.startDateTime_CRON = GenerateCronExpression($scope.trigger_dt, $scope.selectRecurrenceOpt.value)

							//roll back the the start date time by 30 seconds (start time should be 30 seconds prior to trigger time)
							dt_st = new Date($scope.trigger_dt - 30*1000)
							
							startDateTime = dt_st.getFullYear() +
												"-" + ("0"+(dt_st.getMonth()+1)).slice(-2) +
												"-" +("0"+ dt_st.getDate()).slice(-2) +
												" " + ("0" + dt_st.getHours()).slice(-2) +
												":" +("0" + dt_st.getMinutes()).slice(-2) +
												":" + ("0" + dt_st.getSeconds()).slice(-2) +".0";
							$scope.startDateTime = startDateTime;
							
							$scope.endDateTime = $scope.dt2.getFullYear() + 
												"-" + ("0"+($scope.dt2.getMonth()+1)).slice(-2) +
												"-" +("0"+ $scope.dt2.getDate()).slice(-2) +
												" " + ("0"+ $scope.selectLastHour.value).slice(-2) +
												":" +("0" + $scope.selectLastMinute.value).slice(-2) +
												":00.0"
												
							$scope.WorkflowScheduleObject = {};
							$scope.WorkflowScheduleObject['startDateTime_CRON'] = $scope.startDateTime_CRON;
							$scope.WorkflowScheduleObject['startDateTime'] = $scope.startDateTime;
							$scope.WorkflowScheduleObject['endDateTime'] = $scope.endDateTime;
							$scope.WorkflowScheduleObject['workflowKey'] = $scope.workflowToSchedule.workflowKey;
							$scope.WorkflowScheduleObject['recurrence'] = $scope.selectRecurrenceOpt.value;
							$scope.WorkflowScheduleObject['workflow_arguments'] = "test";
							$scope.WorkflowScheduleObject['workflow_server_url'] = $scope.workflowToSchedule.runLink;
							
							
							TimeFromNowToStart = new Date($scope.startDateTime)-new Date()
							TimeStartToEnd = new Date($scope.endDateTime)-new Date($scope.startDateTime)
							
							if (TimeFromNowToStart<=0) {
								console.log("invalid start time input")
								alert("Please ensure the scheduled start date time is later than current time.")
								return;
							}
							if (TimeStartToEnd<=0) {
								console.log("invalid end time input")
								alert("Please ensure the schduled end date time is later than the start time.")
								return;								
							}
							// if successful then save and close
							$scope.saveCronJob($scope.WorkflowScheduleObject);
							$uibModalInstance.close();
							
					 	};
					 	
						$scope.saveCronJob = function(cronJobData){
							
							console.log('saving cron job data: ' + cronJobData);
							var uuu = "workflows/saveCronJob.htm";
							var postData={cronJobDataObj: cronJobData};
						  	$.ajax({
							  		 type : 'POST',
							  		 url : uuu,
							  		 //dataType: 'json',				// data type expected from server
							  		 contentType: 'application/json',  
							  		 data: JSON.stringify(postData),   // data type sent to server	
							  		 success : function(data){
													  			$scope.$apply(function(){
													  				//$scope.availableRoleFunctions=[];$scope.$apply();
													  				// new // $scope.availableFnMenuItems=data.availableFnMenuItems;
													  				}
													  				);  
													  			//alert("Update Successful.") ;
													  			//$scope.editRoleFunction = null;
													  			// new /// $modalInstance.close({availableFnMenuItems:$scope.availableRoleFunctions});
							  		 						},
									 error : function(data){
										 alert("Error while saving."); 
									 }
							  	  });							
							
						};
			
			      	  $scope.cancel = function() {
			      		  console.log("cancel triggered")
			      		  $uibModalInstance.dismiss();
			      	  };
			    }]
			  });

		      modalInstance.result.then(function () {
		    	  $scope.$emit('workflowRemoved');	    	  
		      }, function () {
		        $log.info('Modal dismissed at: ' + new Date());
		      });
		   
		};	
	
	
	
	
	
	
	
	
	   $scope.previewWorkflow = function(workflowToPreview,modalSize){	   
		   var modalInstance = $uibModal.open({
				 animation: $scope.animationsEnabled,
				 templateUrl: 'app/fusion/scripts/view-models/workflows/workflow-preview.html',
				 size:modalSize,
				 controller: ['$scope', '$uibModalInstance', '$http', function ($scope, $uibModalInstance, $http) {
			    		$scope.workflowToPreview = workflowToPreview;
			    		console.log('previewWorkFlow invoked');
			    		console.log($scope.workflowToPreview);
			
			      	  $scope.cancel = function() {
			      		  $uibModalInstance.dismiss();
			      	  };
			    }]
			  });

		      modalInstance.result.then(function () {
		    	  $scope.$emit('workflowRemoved');  
		      }, function () {
		        $log.info('Modal dismissed at: ' + new Date());
		      });
		   
		};   
	
	
	/* change work flow status based on the boolean variable "suspendBool" which corresponds whether 
	 * we would like to suspend or activate a workflow specified by key. */	
	$scope.changeWorkflowStatus = function(workflowToChangeStatus,suspendBool){
		if (workflowToChangeStatus!==null) {
		var statusUrl= workflowToChangeStatus.runLink+"/engine-rest/process-definition/key/"+workflowToChangeStatus.workflowKey
		var suspendedUrl= statusUrl+"/suspended"
		var xmlHttp = new XMLHttpRequest();
		xmlHttp.open('PUT', suspendedUrl, false); 
		xmlHttp.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
		xmlHttp.onload = function() {
			if (suspendBool) {
				console.log("process definition is now suspended"); 
				workflowToChangeStatus.active="false"
			} else {
				console.log("process definition is now activated");				
				workflowToChangeStatus.active="true"
			}
		};
		xmlHttp.send(JSON.stringify({
			"suspended" : suspendBool,
			"includeProcessInstances" : true,
			"executionDate" : "2013-11-21T10:49:45"
		}));
		}
		
	};

	$scope.activateWorkflow = function(workflowToActivate){
		$scope.changeWorkflowStatus(workflowToActivate,false)

	};

	$scope.suspendWorkflow = function(workflowToActivate){
		$scope.changeWorkflowStatus(workflowToActivate,true)
	};		
	
	$scope.checkWorkflowStatus = function(workflow) {
		if (workflow!==null) {
		var statusUrl= workflow.runLink+"/engine-rest/process-definition/key/"+workflow.workflowKey
		var xmlHttp3 = new XMLHttpRequest();
		xmlHttp3.open('GET', statusUrl, true);
		xmlHttp3.withCredentials = true;
		xmlHttp3.send();		
		xmlHttp3.onreadystatechange = function() {
			if (xmlHttp3.readyState == 4 && xmlHttp3.status == 200) {
				// do something with the response in the variable data
				var temp = JSON.parse(xmlHttp3.responseText)
				if (temp.suspended == false){
					console.log("Activated")
					workflow.active="true"
				} else {
					console.log("Suspended")
					workflow.active="false"
				}
			}
		}
		}
	};
	
	$scope.StartWorkflowInstance = function(workflowToStart){
		if (workflowToStart!==null) {
		var statusUrl= workflowToStart.runLink+"/engine-rest/process-definition/key/"+workflowToStart.workflowKey
		var suspendedUrl= statusUrl+"/submit-form"
		var xmlHttp = new XMLHttpRequest();
		xmlHttp.open('POST', suspendedUrl, false); 
		xmlHttp.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
		xmlHttp.onload = function() {
		};
		xmlHttp.send(JSON.stringify({
			  "variables": {
				    "customerId": {"value":"asdasda","type":"String"},
				    "amount":{"value":"100","type":"String"}
				  }
				}));
		}
		
	};	
	
	
	$scope.$on('workflowAdded', function(event, newWorkflow) { 
		console.log("New Workflow to be added in list scope " + JSON.stringify(newWorkflow)); 
		//$scope.workflows.push(newWorkflow);
		$scope.fetchWorkflowsList();
		console.log('newly added workflow = ' + JSON.stringify(newWorkflow));
	});
	
	$scope.$on('workflowRemoved', function(event) {
		$scope.fetchWorkflowsList();
	});

	$scope.fetchWorkflowsList();

	
	
});

