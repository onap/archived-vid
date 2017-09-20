(function () {
    'use strict';

    appDS2.controller("newSchedulerController", ["$scope", "$uibModal", "$uibModalInstance", "AaiService", "SchedulerService", "_",
        "$log", "changeManagement", "$timeout", "$interval", "$filter", "VIDCONFIGURATION", "changeManagementService", newSchedulerController]);

    function newSchedulerController($scope, $uibModal, $uibModalInstance, AaiService, SchedulerService, _, $log, changeManagement, $timeout, $interval, $filter, VIDCONFIGURATION, changeManagementService) {
        var vm = this;
        var pollpromise;

        var init = function () {
            vm.scheduler = {};
            vm.schedulingInfo = {};
            var callbackData = extractChangeManagementCallbackDataStr(changeManagement);
            vm.vnfObject = changeManagement;
            vm.schedulerObj = {
                domain: 'ChangeManagement',
                scheduleId: '',
                scheduleName: 'VnfUpgrade/DWF',
                userId: '',
                domainData: [{
                    'WorkflowName': vm.scheduler.policy,
                    'CallbackUrl': 'http://127.0.0.1:8989/scheduler/v1/loopbacktest/vid',
                    'CallbackData': callbackData
                }],

                schedulingInfo: {
                    normalDurationInSeconds: '',
                    additionalDurationInSeconds: '',
                    concurrencyLimit: '',
                    policyId: '',
                    vnfDetails: [
                        {
                            groupId: "",
                            node: [],
                            changeWindow: [{
                                startTime: '',
                                endTime: ''
                            }]

                        }
                    ]
                },

            }
            vm.schedulerObj1 = {
                "domain": "ChangeManagement",
                "scheduleName": "VnfUpgrade/DWF",
                "userId": "su7376",
                "domainData": [
                    {
                        "WorkflowName": "HEAT Stack Software Update for vNFs",
                        "CallbackUrl": "http://127.0.0.1:8989/scheduler/v1/loopbacktest/vid",
                        "CallbackData": "testing"
                    }
                ],
                "schedulingInfo": {
                    "normalDurationInSeconds": 60,
                    "additionalDurationInSeconds": 60,
                    "concurrencyLimit": 60,
                    "policyId": "SNIRO_CM_1707.Config_MS_Demo_TimeLimitAndVerticalTopology_zone_localTime.1.xml",
                    "vnfDetails": [
                        {
                            "groupId": "group1",
                            "node": ["satmo415vbc", "satmo455vbc"],
                            "changeWindow": [
                                {
                                    "startTime": "2017-08-08T16:37:30.521Z",
                                    "endTime": "2017-08-08T16:37:30.521Z"
                                }
                            ]
                        }
                    ]
                }
            };


            vm.format = 'yyyy/MM/dd';
            vm.todate = new Date();
            vm.checkboxSelection = 'false';
            vm.fromDate = '';
            vm.toDate = '';
            vm.timeSlots = [];

            vm.changeManagement = {};

            vm.subscribers = [];

            AaiService.getSubscribers(function (response) {
                vm.subscribers = response;
            });

            vm.serviceTypes = [];
            AaiService.getServices(function (response) {
                vm.serviceTypes = response.data.service;
            });

            changeManagementService.getWorkflows()
                .then(function (response) {
                    vm.workflows = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                });

            //TODO: Get the VNF names from backend dynamically
            vm.vnfNames = [];

            //TODO: Get the VNF types from backend dynamically
            vm.vnfTypes = [];

            AaiService.getLoggedInUserID(function (response) {
                vm.userID = response.data;
            });
            vm.policys = [];

            var policyName = JSON.stringify({
                policyName: "SNIRO_1710.*",
                configAttributes: {
                    service: "PlacementOptimizationPolicy"
                }
            });
            SchedulerService.getPolicyInfo(policyName, function (response) {
                vm.policys = response.data.entity;
            });
        };

        vm.radioSelections = function (test) {
            if (vm.checkboxSelection == "true") {
                vm.fromDate = '';
                vm.toDate = ''
            }
        }
        vm.close = function () {
            $uibModalInstance.close();
        };


        function convertToSecs(number) {
            var totalSecs;
            if (vm.selectedOption === 'hours') {
                totalSecs = number * 3600;

            }
            else if (vm.selectedOption === 'minutes') {
                totalSecs = number * 60;
            } else {
                totalSecs = number;
            }
            return totalSecs;
        }

        vm.submit = function () {
            vm.schedulingInfo = {
                scheduleId: vm.schedulerID,
                approvalDateTime: '2017-08-08T16:37:30.521Z',
                approvalUserId: "sy6266",
                approvalStatus: 'Accepted',
                approvalType: 'Tier 2'
            };

            var approvalObj = JSON.stringify(vm.schedulingInfo);
            SchedulerService.getSubmitForapprovedTimeslots(approvalObj, function (response) {
                if (response.status == 200) {
                    openConfirmationModal("Successfully Sent for Approval");
                }

            });
        };

        vm.reject = function () {
            vm.schedulingInfo = {
                scheduleId: vm.schedulerID,
                approvalDateTime: '2017-08-08T16:37:30.521Z',
                approvalUserId: "sy6266",
                approvalStatus: 'Rejected',
                approvalType: 'Tier 2'
            }

            var approvalObj = JSON.stringify(vm.schedulingInfo)
            SchedulerService.getSubmitForapprovedTimeslots(approvalObj, function (response) {
                if (response.status == 200) {
                    openConfirmationModal("Successfully sent for Rejection");
                }

            });
        };

        vm.schedule = function (myForm) {
            $scope.$watch('fromDate', validateDates(myForm));
            $scope.$watch('toDate', validateDates(myForm));
            if (myForm.$valid) {
                sendSchedulerReq()
            }
        };


        function sendSchedulerReq() {
            var changeWindow = [{
                startTime: '',
                endTime: ''
            }];
            vm.timeSlots = [];
            var fromDate = $filter('date')(new Date(vm.fromDate), "yyyy-MM-ddTHH:mmZ", "UTC");
            var toDate = $filter('date')(new Date(vm.toDate), "yyyy-MM-ddTHH:mmZ", "UTC");

            changeWindow[0].startTime = fromDate;
            changeWindow[0].endTime = toDate;
            vm.schedulerObj.userId = vm.userID;
            vm.schedulerObj.domainData[0].WorkflowName = changeManagement.workflow;
            vm.schedulerObj.schedulingInfo.normalDurationInSeconds = convertToSecs(vm.scheduler.duration);
            vm.schedulerObj.schedulingInfo.additionalDurationInSeconds = convertToSecs(vm.scheduler.fallbackDuration);
            vm.schedulerObj.schedulingInfo.concurrencyLimit = vm.scheduler.concurrency;
            vm.schedulerObj.schedulingInfo.policyId = vm.scheduler.policy.policyName;
            vm.schedulerObj.schedulingInfo['vnfDetails'][0].groupId = 'groupId';
            vm.schedulerObj.schedulingInfo['vnfDetails'][0].node = getVnfData(changeManagement.vnfNames);

            vm.schedulerObj.schedulingInfo['vnfDetails'][0].changeWindow = changeWindow;
            if (vm.checkboxSelection == "true") {               //When Scheduled now we remove the changeWindow
                delete vm.schedulerObj.schedulingInfo['vnfDetails'][0].changeWindow;
            }
            vm.schedulerObj.schedulingInfo['vnfDetails'][0].changeWindow = changeWindow;
            delete vm.schedulerObj.schedulingInfo['vnfDetails'][0].changeWindow;

            var requestScheduler = JSON.stringify(vm.schedulerObj);
            console.log(requestScheduler);
            SchedulerService.getStatusSchedulerId(requestScheduler, function (response) {
                vm.schedulerID = response.data.uuid;
                vm.isSpinnerVisible = true;
                if (vm.schedulerID) {
                    var scheduledID = JSON.stringify({scheduleId: vm.schedulerID});
                    seviceCallToGetTimeSlots();
                }
            });
        }

        function seviceCallToGetTimeSlots() {

            SchedulerService.getTimeSotsForSchedulerId(vm.schedulerID, function (response) {
                if (vm.checkboxSelection == "false") {
                    if (response.data.entity.schedule) {
                        var scheduleColl = JSON.parse(response.data.entity.schedule);
                        if (scheduleColl.length > 0) {
                            vm.timeSlots = scheduleColl;
                            vm.isSpinnerVisible = false;
                            hasvaluereturnd = false;
                            $scope.stopPoll();
                            openConfirmationModal(response.data.entity.scheduleId + " Successfully Returned TimeSlots.");
                        }

                    }
                    else {
                        if (vm.timeSlots.length == 0 && hasthresholdreached == false) {
                            var polltime = VIDCONFIGURATION.SCHEDULER_POLLING_INTERVAL_MSECS;
                            pollpromise = poll(polltime, function () {
                                if (vm.timeSlots.length == 0) {
                                    hasvaluereturnd = true;
                                    seviceCallToGetTimeSlots()
                                }
                                else {
                                    hasvaluereturnd = false;
                                }

                            });

                        } else {
                            openConfirmationModal("Timeout error.")
                        }
                    }

                }
                else {
                    if (response.data.entity) {
                        vm.isSpinnerVisible = false;
                        openConfirmationModal(response.data.entity.scheduleId + " Successfully Ready for Schedule.")
                    }
                }

            });

        }

        function openConfirmationModal(jobInfo) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/alert-new-scheduler/alert-new-scheduler.html',
                controller: 'alertNewSchedulerController',
                controllerAs: 'vm',
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });
        }

        var hasvaluereturnd = true; // Flag to check
        var hasthresholdreached = false;
        var thresholdvalue = VIDCONFIGURATION.SCHEDULER_MAX_POLLS; // interval threshold value

        function poll(interval, callback) {
            return $interval(function () {
                if (hasvaluereturnd) {  //check flag before start new call
                    callback(hasvaluereturnd);
                }

                thresholdvalue = thresholdvalue - 1;  //Decrease threshold value
                if (thresholdvalue == 0) {
                    $scope.stopPoll(); // Stop $interval if it reaches to threshold
                }
            }, interval)
        }


// stop interval.
        $scope.stopPoll = function () {
            $interval.cancel(pollpromise);
            thresholdvalue = 0;     //reset all flags.
            hasvaluereturnd = false;
            hasthresholdreached = true;
            vm.isSpinnerVisible = false;
        }

        function getVnfData(arrColl) {
            var vnfcolletion = [];

            for (var i = 0; i < arrColl.length; i++) {
                vnfcolletion.push(arrColl[i].name);
            }

            return vnfcolletion
        }

        function validateDates(form) {
            if (vm.checkboxSelection == "false") {

                if (form.startDate.$error.invalidDate || form.endDate.$error.invalidDate) {
                    form.startDate.$setValidity("endBeforeStart", true);  //already invalid (per validDate directive)
                } else {
                    //depending on whether the user used the date picker or typed it, this will be different (text or date type).
                    //creating a new date object takes care of that.
                    var endDate = new Date(vm.toDate);
                    var startDate = new Date(vm.fromDate);
                    form.startDate.$setValidity("endBeforeStart", endDate >= startDate);
                }
            }
        }


        function extractChangeManagementCallbackDataStr(changeManagement) {
            var result = {};

            result.requestType = changeManagement.workflow;
            result.requestDetails = [];

            _.forEach(changeManagement.vnfNames, function (vnf) {

                    var data = {
                        vnfName: vnf.name,
                        vnfInstanceId: vnf.id,
                        modelInfo: {
                            modelType: 'vnf',
                            modelInvariantId: vnf.properties['model-invariant-id'],
                            modelVersionId: vnf.modelVersionId,
                            modelName: vnf.properties['vnf-name'],
                            modelVersion: vnf.version,
                            modelCustomizationName: vnf.properties['model-customization-name'],
                            modelCustomizationId: vnf.properties['model-customization-id']
                        },
                        cloudConfiguration: {
                            lcpCloudRegionId: vnf.availableVersions[0].cloudConfiguration.lcpCloudRegionId,
                            tenantId: vnf.availableVersions[0].cloudConfiguration.tenantId
                        },
                        requestInfo: {
                            source: vnf.availableVersions[0].requestInfo.source,
                            suppressRollback: vnf.availableVersions[0].requestInfo.suppressRollback,
                            requestorId: vnf.availableVersions[0].requestInfo.requestorId
                        },
                        relatedInstanceList: [],
                        requestParameters: {
                            usePreload: vnf.availableVersions[0].requestParameters.usePreload
                        }
                    };

                    var serviceInstanceId = '';
                    _.forEach(vnf['service-instance-node'], function (instanceNode) {
                        if(instanceNode['node-type'] === 'service-instance'){
                            serviceInstanceId = instanceNode.properties['service-instance-id'];
                        }
                    });


                    _.forEach(vnf.availableVersions[0].relatedInstanceList, function (related) {

                        var rel = related.relatedInstance;

                        var relatedInstance = {
                            instanceId: serviceInstanceId,
                            modelInfo: {
                                modelType: rel.modelInfo.modelType,
                                modelInvariantId: rel.modelInfo.modelInvariantId,
                                modelVersionId: rel.modelInfo.modelVersionId,
                                modelName: rel.modelInfo.modelName,
                                modelVersion: rel.modelInfo.modelVersion,
                                modelCustomizationName: rel.modelInfo.modelCustomizationName,
                                modelCustomizationId: rel.modelInfo.modelCustomizationId
                            }
                        };

                        if (rel.vnfInstanceId)
                            relatedInstance.instanceId = rel.vnfInstanceId;

                        data.relatedInstanceList.push({relatedInstance: relatedInstance});
                    });


                    result.requestDetails.push(data);
                }
            );


            // _.forEach(changeManagement.vnfNames, function (vnfName) {
            //     if (vnfName && vnfName.version) {
            //         if (vnfName.selectedFile) {
            //             vnfName.version.requestParameters.userParams = vnfName.selectedFile;
            //         }
            //         result.requestDetails.push(vnfName.version)
            //     }
            // });

            return JSON.stringify(result);
        }

        init();
    };

    appDS2.directive('validDate', function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, element, attrs, control) {
                control.$parsers.push(function (viewValue) {
                    var newDate = model.$viewValue;
                    control.$setValidity("invalidDate", true);
                    if (typeof newDate === "object" || newDate == "") return newDate;  // pass through if we clicked date from popup
                    if (!newDate.match(/^\d{1,2}\/\d{1,2}\/((\d{2})|(\d{4}))$/))
                        control.$setValidity("invalidDate", false);
                    return viewValue;
                });
            }
        };
    })


})();

