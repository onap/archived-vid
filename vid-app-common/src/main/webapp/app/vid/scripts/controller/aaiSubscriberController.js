/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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
 * ============LICENSE_END=========================================================
 */

"use strict";

appDS2.controller("aaiSubscriberController", ["COMPONENT", "FIELD", "PARAMETER", "DataService", "PropertyService", "$scope", "$http", "$timeout", "$location", "$log", "$route", "$uibModal", "VIDCONFIGURATION", "UtilityService", "vidService", "AaiService", "MsoService", "OwningEntityService", "AsdcService","featureFlags", "$q", "_",
    function (COMPONENT, FIELD, PARAMETER, DataService, PropertyService, $scope, $http, $timeout, $location, $log, $route, $uibModal, VIDCONFIGURATION, UtilityService, vidService, AaiService, MsoService, OwningEntityService, AsdcService, featureFlags, $q, _) {

        $scope.showReportWindow = function() {
            let GuiMessage;

            if ($scope.errorMsg !== undefined && $scope.errorMsg !== null) {
                GuiMessage = $scope.errorMsg;
            } else {
                GuiMessage = $scope.status;
            }

            const modalWindow = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/report-modal/report-modal.html',
                controller: 'reportModalController',
                controllerAs: 'vm',
                resolve: {
                    errorMsg: function () {
                        return GuiMessage;
                    }
                }
            });
        };

        $scope.showVnfDetails = function (vnf) {
            console.log("showVnfDetails");
            DataService.setVnfInstanceId(COMPONENT.VNF_INSTANCE_ID);
            DataService
                .setInventoryItem(aaiResult[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM][0]);

            $scope.$broadcast(COMPONENT.SHOW_COMPONENT_DETAILS, {
                componentId: COMPONENT.VNF,
                callbackFunction: callbackFunction
            });
        };
        $scope.popup = new Object();


        $scope.isPopupVisible = false;
        $scope.defaultBaseUrl = "";
        $scope.responseTimeoutMsec = 60000;

        $scope.serviceTypes = [FIELD.PROMPT.SELECT_SERVICE, COMPONENT.UCPE_VMS, COMPONENT.SDN_L3_BONDING, COMPONENT.SDN_ETHERNET_INTERNET];
        $scope.defaultSubscriberName = [FIELD.PROMPT.SELECT_SUB];

        $scope.verifyButtonEnabled = true;

        var callbackFunction = function (response) {
            alert(response);
        };

        $scope.getSubs = function () {
            $scope.init();
            $scope.fetchSubs(FIELD.PROMPT.FETCHING_SUBS);
            $scope.fetchServices();
        };

        $scope.cancelCreateSI = function () {

            window.location.href = COMPONENT.WELCOME_PATH;
        };

        $scope.getServiceTypes = function (globalCustomerId) {
            DataService.setGlobalCustomerId(globalCustomerId);
            DataService.setServiceIdList($scope.customerList);

            if (globalCustomerId !== "" && globalCustomerId !== undefined) {
                window.location.href = COMPONENT.SERVICE_TYPE_LIST_PATH + $scope.serviceTypeList;
            }
        };

        $scope.refreshServiceTypes = function (globalCustomerId) {
            DataService.setGlobalCustomerId(globalCustomerId);

            $scope.getServiceTypesList();
        };

        $scope.subId = "";
        $scope.createSubscriberName = "";
        $scope.serviceTypeList = {};
        $scope.custSubList = [];
        $scope.getServiceTypesList = function () {
            var notFound = true;
            var globalCustomerId = DataService.getGlobalCustomerId();
            $scope.custSubList = DataService.getServiceIdList();
            if (globalCustomerId !== "" && globalCustomerId !== undefined) {
                $scope.subId = globalCustomerId;
                $scope.init();
                $scope.status = FIELD.PROMPT.FETCHING_SERVICE_TYPES;
                DataService.setGlobalCustomerId(globalCustomerId);

                AaiService.getSubscriptionServiceTypeList(DataService
                    .getGlobalCustomerId(), function (response) {
                    notFound = false;
                    $scope.setProgress(100); // done
                    $scope.status = FIELD.STATUS.DONE;
                    $scope.isSpinnerVisible = false;
                    $scope.serviceTypeList = response;
                    for (var i = 0; i < $scope.custSubList.length; i++) {
                        if (globalCustomerId === $scope.custSubList[i].globalCustomerId) {
                            $scope.createSubscriberName = $scope.custSubList[i].subscriberName;
                        }
                    }
                }, function (response) { // failure
                    $scope.showError(FIELD.ERROR.AAI);
                    $scope.errorMsg = FIELD.ERROR.FETCHING_SERVICE_TYPES + response.status;
                    $scope.errorDetails = response.data;
                });
            } else {
                alert(FIELD.ERROR.SELECT);
            }

        };

        $scope.subList = [];
        $scope.getAaiServiceModels = function (selectedServicetype, subName) {
            DataService.setGlobalCustomerId(selectedServicetype);
            DataService.setServiceIdList($scope.serviceTypeList);
            DataService.setSubscriberName(subName);

            DataService.setSubscribers($scope.custSubList);

            if (selectedServicetype !== "" && selectedServicetype !== 'undefined'&& selectedServicetype !== undefined) {
                $location.path(COMPONENT.CREATE_INSTANCE_PATH);
            }
        };

        $scope.serviceTypeName = "";
        $scope.getAaiServiceModelsList = function () {
            var globalCustomerId = "";
            var serviceTypeId = DataService.getGlobalCustomerId();
            $scope.serviceTypeList = DataService.getServiceIdList();
            $scope.createSubscriberName = DataService.getSubscriberName();
            $scope.status = FIELD.STATUS.FETCHING_SERVICE_CATALOG;
            $scope.custSubList = DataService.getSubscribers();
            for (var i = 0; i < $scope.serviceTypeList.length; i++) {
                if (parseInt(serviceTypeId) === i) {
                    $scope.serviceTypeName = $scope.serviceTypeList[i].name;
                }
            }
            ;
            for (var i = 0; i < $scope.custSubList.length; i++) {
                if ($scope.createSubscriberName === $scope.custSubList[i].subscriberName) {
                    globalCustomerId = $scope.custSubList[i].globalCustomerId;
                    globalCustId = globalCustomerId;
                }
            }
            ;
            var pathQuery = "";

            if (null !== globalCustomerId && "" !== globalCustomerId && undefined !== globalCustomerId
                && null !== serviceTypeId && "" !== serviceTypeId && undefined !== serviceTypeId) {
                pathQuery = COMPONENT.SERVICES_PATH + globalCustomerId + "/" + $scope.serviceTypeName;
            }

            var namedQueryId = '6e806bc2-8f9b-4534-bb68-be91267ff6c8';
            AaiService.getServiceModelsByServiceType(namedQueryId, globalCustomerId, $scope.serviceTypeName, function (response) { // success
                $scope.services = [];
                if (angular.isArray(response.data['inventory-response-item']) && response.data['inventory-response-item'].length > 0 && response.data['inventory-response-item'][0]['inventory-response-items']) {
                    wholeData = response.data['inventory-response-item'][0]['inventory-response-items']['inventory-response-item'];
                    $scope.services = $scope.filterDataWithHigerVersion(response.data['inventory-response-item'][0]['inventory-response-items']['inventory-response-item']);
                    $scope.serviceType = response.data['inventory-response-item'][0]['service-subscription']['service-type'];
                    $scope.viewPerPage = 10;
                    $scope.totalPage = $scope.services.length / $scope.viewPerPage;
                    $scope.sortBy = "name";
                    $scope.scrollViewPerPage = 2;
                    $scope.currentPage = 1;
                    $scope.searchCategory;
                    $scope.searchString = "";
                    $scope.currentPageNum = 1;
                    $scope.isSpinnerVisible = false;
                    $scope.isProgressVisible = false;
                } else {
                    $scope.status = "Failed to get service models from SDC.";
                    $scope.error = true;
                    $scope.isSpinnerVisible = false;
                }
                DataService.setServiceIdList(response);
            }, function (response) { // failure
                $scope.showError(FIELD.ERROR.AAI);
                $scope.errorMsg = FIELD.ERROR.FETCHING_SERVICES + response.status;
                $scope.errorDetails = response.data;
            });

        };

        var globalCustId;// This value will be assigned only on create new service instance screen-macro
        $scope.createType = "a la carte";
        $scope.deployService = function (service, hideServiceFields) {
            hideServiceFields = hideServiceFields || false;
            var temp = service;
            service.uuid = service['service-instance']['model-version-id'];

            console.log("Instantiating ASDC service " + service.uuid);

            $http.get('rest/models/services/' + service.uuid)
                .then(function (getServiceResponse) {
                    getServiceResponse.data['service'].serviceTypeName = $scope.serviceTypeName;
                    getServiceResponse.data['service'].createSubscriberName = $scope.createSubscriberName;
                    var serviceModel = getServiceResponse.data;
                    DataService.setServiceName(serviceModel.service.name);

                    DataService.setModelInfo(COMPONENT.SERVICE, {
                        "modelInvariantId": serviceModel.service.invariantUuid,
                        "modelVersion": serviceModel.service.version,
                        "modelNameVersionId": serviceModel.service.uuid,
                        "modelName": serviceModel.service.name,
                        "description": serviceModel.service.description,
                        "serviceType": serviceModel.service.serviceType,
                        "serviceRole": serviceModel.service.serviceRole,
                        "category": serviceModel.service.category,
                        "serviceTypeName": serviceModel.service.serviceTypeName,
                        "createSubscriberName": serviceModel.service.createSubscriberName
                    });
                    DataService.setHideServiceFields(hideServiceFields);
                    if (hideServiceFields) {
                        DataService.setServiceType($scope.serviceTypeName);
                        DataService.setGlobalCustomerId(globalCustId);
                    }

                    DataService.setALaCarte(true);
                    $scope.createType = "a la carte";
                    var broadcastType = "createComponent";

                    DataService.setShouldIncludeInAsyncInstantiationFlow(AsdcService.shouldTakeTheAsyncInstantiationFlow(serviceModel));

                    if (AsdcService.isMacro(serviceModel)) {
                        DataService.setALaCarte(false);
                        $scope.createType = "Macro";
                        var convertedAsdcModel = UtilityService.convertModel(serviceModel);

                        //console.log ("display inputs ");
                        //console.log (JSON.stringify ( convertedAsdcModel.completeDisplayInputs));

                        DataService.setModelInfo(COMPONENT.SERVICE, {
                            "modelInvariantId": serviceModel.service.invariantUuid,
                            "modelVersion": serviceModel.service.version,
                            "modelNameVersionId": serviceModel.service.uuid,
                            "modelName": serviceModel.service.name,
                            "description": serviceModel.service.description,
                            "category": serviceModel.service.category,
                            "serviceEcompNaming": serviceModel.service.serviceEcompNaming,
                            "inputs": serviceModel.service.inputs,
                            "displayInputs": convertedAsdcModel.completeDisplayInputs,
                            "serviceTypeName": serviceModel.service.serviceTypeName,
                            "createSubscriberName": serviceModel.service.createSubscriberName,
                            "serviceType": serviceModel.service.serviceType,
                            "serviceRole": serviceModel.service.serviceRole
                        });
                    }

                    $scope.$broadcast(broadcastType, {
                        componentId: COMPONENT.SERVICE,
                        callbackFunction: function (response) {
                            if (response.isSuccessful) {
                                vidService.setModel(serviceModel);

                                var subscriberId = "Not Found";
                                var serviceType = "Not Found";

                                var serviceInstanceId = response.instanceId;

                                for (var i = 0; i < response.control.length; i++) {
                                    if (response.control[i].id == "subscriberName") {
                                        subscriberId = response.control[i].value;
                                    } else if (response.control[i].id == "serviceType") {
                                        serviceType = response.control[i].value;
                                    }
                                }


                                $scope.refreshSubs(subscriberId, serviceType, serviceInstanceId);

                            }
                        }
                    });

                }, function (response) {
                    $log.error("Error: ", response);
                });
        };

        var wholeData=[];
        //extract all models from service instances properties and filter by the higher model version
        $scope.filterDataWithHigerVersion = function(serviceData){

            //get all service instances & models collected into array. key = model invariant id
            var servicesByModelInvariant = _.reduce(serviceData, function(result, item) {
                if (item['extra-properties']['extra-property']) {
                    var invariantId = item['extra-properties']['extra-property'][4];
                    if (invariantId) {
                        (result[invariantId["property-value"]] || (result[invariantId["property-value"]] = [])).push(item);
                    }
                }
                return result;
            }, {});

            //iterate over the array and get the highest model version for each group of services
            var filterDataServicesByVersion = [];
            _.forEach(servicesByModelInvariant, function(items) {
                var maxVersionItem = _.maxBy(items, function(item) { return parseFloat(item['extra-properties']['extra-property'][6]["property-value"]); });
                filterDataServicesByVersion.push(maxVersionItem);
            });

            return filterDataServicesByVersion;
        };

        $scope.tableData = [];
        var oldData = [];
        $scope.loadPreviousVersionData = function (version, invariantUUID) {
            $scope.tableData = [];
            oldData = [];
            for (var i = 0; i < wholeData.length; i++) {
                if (wholeData[i]['extra-properties']['extra-property'] && wholeData[i]['extra-properties']['extra-property'][4]['property-value'] == invariantUUID && version != wholeData[i]['extra-properties']['extra-property'][6]['property-value']) {
                    oldData.push(wholeData[i]);
                }
            }
            $scope.tableData = oldData;
            $scope.createType = "Previous Version";
            var broadcastType = "createTableComponent";
            $scope.componentName = invariantUUID;
            $scope.$broadcast(broadcastType, {
                componentId: COMPONENT.OLDVERSION,
                callbackFunction: function (response) {
                }
            });
        };
        $scope.cancelCreateSIType = function () {

            window.location.href = COMPONENT.SERVICE_MODLES_INSTANCES_SUBSCRIBERS_PATH;

        };

        $scope.fetchServices = function () {
            var serviceIdList = [];

            AaiService.getServices2(function (response) { // success
                DataService.setServiceIdList(response);
            }, function (response) { // failure
                $scope.showError(FIELD.ERROR.AAI);
                $scope.errorMsg = FIELD.ERROR.FETCHING_SERVICES + response.status;
                $scope.errorDetails = response.data;
            });
        };

        $scope.refreshSubs = function () {
            $scope.init();
            $scope.fetchSubs(FIELD.PROMPT.REFRESH_SUB_LIST);
            $scope.fetchServices();
        };

        $scope.loadOwningEntity = function () {
            OwningEntityService.getOwningEntityProperties(function (response) {
                $scope.owningEntities = response.owningEntity;
                $scope.projects = response.project;

                // working project name: owning-entity-id-val-cp8128
                // working owning entity name: owning-entity-id-val-cp8128
            });
        };

        $scope.fetchSubs = function (status) {
            $scope.status = status;

            AaiService.getSubList(function (response) {
                $scope.setProgress(100); // done
                $scope.status = FIELD.STATUS.DONE;
                $scope.isSpinnerVisible = false;
                $scope.customerList = response;
            }, function (response) { // failure
                $scope.showError(FIELD.ERROR.AAI);
                $scope.errorMsg = FIELD.ERROR.AAI_FETCHING_CUST_DATA + response.status;
                $scope.errorDetails = response.data;
            });
        };

        $scope.getPermitted = function (item) {
            return item.isPermitted || item[FIELD.ID.IS_PERMITTED];

        };


        $scope.getSubDetails = function () {

            $scope.init();
            //$scope.selectedSubscriber = $location.search().selectedSubscriber;
            // $scope.selectedServiceInstance = $location.search().selectedServiceInstance;
            $scope.status = FIELD.STATUS.FETCHING_SUB_DETAILS + $scope.selectedSubscriber;
            var query = $location.url().replace($location.path(),'');

            $scope.displayData = [];
            AaiService.searchServiceInstances(query).then(function (response) {
                $scope.displayData = response.displayData;
                if (response.displayData.length)
                {
                    var first = response.displayData[0];
                    $scope.selectedSubscriberId = first.globalCustomerId;
                    $scope.selectedSubscriberName = first.subscriberName;
                }
                $scope.viewPerPage = 10;
                $scope.totalPage = $scope.displayData.length / $scope.viewPerPage;
                $scope.scrollViewPerPage = 2;
                $scope.currentPage = 1;
                $scope.searchCategory;
                $scope.searchString = "";
                $scope.currentPageNum = 1;
                $scope.defaultSort = COMPONENT.SUBSCRIBER_NAME;
                $scope.setProgress(100); // done
                $scope.status = FIELD.STATUS.DONE;
                $scope.isSpinnerVisible = false;

            }).catch(function (response) {
                $scope.showError(FIELD.ERROR.AAI);
                $scope.errorMsg = FIELD.ERROR.AAI_FETCHING_CUST_DATA + response.status;
                $scope.errorDetails = response.data;
            });
        };


        $scope.$on(COMPONENT.MSO_DELETE_REQ, function (event, request) {
            // $log.debug("deleteInstance: request:");
            // $log.debug(request);
            $scope.init();

            $http.post($scope.baseUrl + request.url, {
                requestDetails: request.requestDetails
            }, {
                timeout: $scope.responseTimeoutMsec
            }).then($scope.handleInitialResponse)
                ["catch"]($scope.handleServerError);
        });

        $scope.init = function () {
            //PropertyService.setAaiBaseUrl("testaai");
            //PropertyService.setAsdcBaseUrl("testasdc");

            // takes a default value, retrieves the prop value from the file system and sets it
            var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec();
            PropertyService.setMsoMaxPollingIntervalMsec(msecs);

            // takes a default value, retrieves the prop value from the file system and sets it
            var polls = PropertyService.retrieveMsoMaxPolls();
            PropertyService.setMsoMaxPolls(polls);

            /*
             * Common parameters that would typically be set when the page is
             * displayed for a specific service instance id.
             */

            $scope.baseUrl = $scope.defaultBaseUrl;

            $scope.isSpinnerVisible = true;
            $scope.isProgressVisible = true;
            $scope.isPopupVisible = true;
            $scope.requestId = "";
            $scope.error = "";
            $scope.pollAttempts = 0;
            $scope.log = "";
            $scope.enableCloseButton(false);
            $scope.resetProgress();
            $scope.setProgress(2); // Show "a little" progress
        };

        function getRelatedInstanceGroupsByVnfId(genericVnf) {
            var model = vidService.getModel();
            if (model && model.vnfs && !_.isEmpty(model.vnfs)) {
                Object.keys(model.vnfs).forEach(function (key) {
                    var vnf = model.vnfs[key];
                    if (vnf.vfcInstanceGroups && !_.isEmpty(vnf.vfcInstanceGroups) && vnf.uuid === genericVnf.modelVersionId) {
                            AaiService.getInstanceGroupsByVNFInstanceId(genericVnf.nodeId,
                                function (response) { //success
                                    handleGetRelatedInstanceGroupsResponseForVnf(response, genericVnf);
                                },
                                function (response) { //failure
                                    $scope.showError(FIELD.ERROR.AAI);
                                    $scope.errorMsg = FIELD.ERROR.FETCHING_SERVICE_INSTANCE_DATA + response.status;
                                    $scope.errorDetails = response.data;
                                }
                            )
                        }
                });
            }
        }

        function resolveModelDataIfMissing(modelVersionId) {
            var model = vidService.getModel();
            if (model && model.service && model.service.uuid === modelVersionId) {
                return $q.resolve(); // no need to resolve anything
            } else {
                // resolve model and value of isPermitted
                return resolveIfIsPermitted()
                    .then(function() {
                        return getAsdcModelByVersionId(modelVersionId);
                    });
            }
        }

        function resolveIfIsPermitted() {
            var deferred = $q.defer();
            AaiService.searchServiceInstances(
                '?subscriberId=' + $location.search().subscriberId +
                '&serviceInstanceIdentifier=' + $location.search().serviceInstanceId + '&serviceInstanceIdentifierType=Service Instance Id')
                .then(function (response) {
                    if (response.displayData && response.displayData.length) {
                        var first = response.displayData[0];
                        $scope.isPermitted = first.isPermitted;
                        deferred.resolve(first.isPermitted);
                    } else {
                        deferred.reject("some error while resolveIfIsPermitted");
                    }
                });

            return deferred.promise;
        }

        var serviceNetworkVlans = [];
        var vnfNetworksAndVlans = [];

        function fetchServiceIfMissing() {
            if (_.isEmpty(DataService.getServiceIdList())) {
                $scope.fetchServices();
            }
        }

        $scope.getComponentList = function (event, request) {

            $scope.isSpinnerVisible = true;
            $scope.isProgressVisible = true;
            $scope.isPopupVisible = true;
            $scope.requestId = "";
            $scope.error = "";
            $scope.pollAttempts = 0;
            $scope.log = "";

            $scope.resetProgress();
            $scope.setProgress(2); // Show "a little" progress

            $scope.globalCustomerId = $location.search().subscriberId;
            $scope.serviceType = $location.search().serviceType;
            $scope.serviceInstanceId = $location.search().serviceInstanceId;
            $scope.subscriberName = $location.search().subscriberName;

            //$scope.getAsdcModel($location.search().modelUuid);

            //make sure view/edit don't open new deploy service popup
            DataService.setShouldIncludeInAsyncInstantiationFlow(false);

            return getModelVersionIdForServiceInstance({
                globalCustomerId: $location.search().subscriberId,
                serviceInstanceId: $location.search().serviceInstanceId,
                aaiModelVersionId: $location.search().aaiModelVersionId
            })
                .then(resolveModelDataIfMissing)
                .then($scope.prepareScopeWithModel)
                .then(fetchServiceIfMissing)
                .then(function () {
                    return AaiService.getVlansByNetworksMapping($scope.globalCustomerId, $scope.serviceType, $scope.serviceInstanceId, $scope.service.model.service.uuid);
                })
                .then(function (vlanByNetwork) {
                    serviceNetworkVlans = vlanByNetwork.serviceNetworks ? vlanByNetwork.serviceNetworks : [];
                    vnfNetworksAndVlans =  vlanByNetwork.vnfNetworks ? vlanByNetwork.vnfNetworks : [];
                    $log.debug('vlanByNetwork', vlanByNetwork);
                    $scope.namedQueryId = VIDCONFIGURATION.COMPONENT_LIST_NAMED_QUERY_ID;
                    $scope.status = FIELD.STATUS.FETCHING_SERVICE_INST_DATA + $scope.serviceInstanceId;

                    $scope.hasFabricConfigurations = !UtilityService.isObjectEmpty($scope.service.model.fabricConfigurations);
                    return AaiService.runNamedQuery($scope.namedQueryId, $scope.globalCustomerId, $scope.serviceType, $scope.serviceInstanceId,
                        function (response) { //success
                            try {
                                $log.debug("Getting inventory reponse items from Named Query with AAI query optimization - Before ");
                                $scope.handleInitialResponseInventoryItemsOptimized(response);
                                $log.debug("Getting inventory reponse items from Named Query with AAI query optimization - After ");
                            }catch (error) {
                                $log.error("Exception: Fallback to original method of getting subscriber name and SI orch status.");
                                $scope.handleInitialResponseInventoryItems(response);
                            }
                            $scope.setProgress(100); // done
                            $scope.status = FIELD.STATUS.DONE;
                            $scope.isSpinnerVisible = false;
                            //TODO
                        },
                        function (response) { //failure
                            $scope.showError(FIELD.ERROR.AAI);
                            $scope.errorMsg = FIELD.ERROR.FETCHING_SERVICE_INSTANCE_DATA + response.status;
                            $scope.errorDetails = response.data;
                        }
                    );
                });
        };

        $scope.handleServerError = function (response, status) {
            alert(response.statusText);
        };

        function handleGetRelatedInstanceGroupsResponseForVnf(response, genericVnf) {
            _.forEach(response.data, function (instanceGroup) {
                var newInstanceGroup = {
                    name: instanceGroup.name,
                    type: instanceGroup.type
                };
                genericVnf.instanceGroups.push(newInstanceGroup);
            });
        }



        function getModelVersionIdForServiceInstance(instance) {
            if (UtilityService.hasContents(instance.aaiModelVersionId)) {
                return $q.resolve(instance.aaiModelVersionId);
            } else if ($scope.modelVersionIdForServiceInstance
                && $scope.modelVersionIdForServiceInstance.globalCustomerId === instance.globalCustomerId
                && $scope.modelVersionIdForServiceInstance.serviceInstanceId === instance.serviceInstanceId
            ) {
                return $q.resolve($scope.modelVersionIdForServiceInstance.aaiModelVersionId);
            } else {
                $scope.status = FIELD.STATUS.FETCHING_SERVICE_INST_DATA + $scope.serviceInstanceId;
                return AaiService.getModelVersionId(instance.globalCustomerId, instance.serviceInstanceId, 'Service Instance Id')
                    .then(function (aaiModelVersionId) {
                        $scope.modelVersionIdForServiceInstance = {
                            globalCustomerId: instance.globalCustomerId,
                            serviceInstanceId: instance.serviceInstanceId,
                            aaiModelVersionId: aaiModelVersionId
                        };
                        return $q.resolve(aaiModelVersionId);
                    });
            }
        }

        $scope.onViewEditClick = function (disData) {
            $log.debug("disData", disData, null, 4);

            getModelVersionIdForServiceInstance(disData)
                .then(getAsdcModelByVersionId, handleErrorGettingModelVersion)
                .then(navigateToViewEditPage);


            function navigateToViewEditPage() {
                window.location.href = AsdcService.shouldTakeTheDrawingBoardViewEdit(vidService.getModel()) ?
                    drawingBoardViewEditUrl() : oldViewEditUrl();
            }

            function oldViewEditUrl() {
                return COMPONENT.INSTANTIATE_ROOT_PATH + disData.globalCustomerId +
                    COMPONENT.SUBSCRIBERNAME_SUB_PATH + disData.subscriberName +
                    COMPONENT.SERVICETYPE_SUB_PATH + disData.serviceType +
                    COMPONENT.SERVICEINSTANCEID_SUB_PATH + disData.serviceInstanceId +
                    COMPONENT.MODELVERSIONID_SUB_PATH + disData.aaiModelVersionId +
                    COMPONENT.IS_PERMITTED_SUB_PATH + disData.isPermitted;
            }

            function drawingBoardViewEditUrl() {
                return 'serviceModels.htm#/servicePlanning/EDIT?' +
                'serviceModelId=' +     disData.aaiModelVersionId +
                '&subscriberId=' +      disData.globalCustomerId  +
                '&serviceType=' +       disData.serviceType      +
                '&serviceInstanceId=' + disData.serviceInstanceId;
            }
        };

        function handleErrorGettingModelVersion(err) {
            $log.error("aaiSubscriber getModelVersionIdForServiceInstance - " + err);
            $scope.errorMsg = err;
            alert($scope.errorMsg);
            return $q.reject();
        }

        function getAsdcModelByVersionId(aaiModelVersionId) {
            // aaiModelVersionId is the model uuid
            var pathQuery = COMPONENT.SERVICES_PATH + aaiModelVersionId;
            return $http({
                method: 'GET',
                url: pathQuery
            }).then(function (response) {
                if (response.headers()['content-type'].includes('json')) {
                    vidService.setModel(response.data);
                    console.log("aaiSubscriber getAsdcModel DONE!!!!");
                } else {
                    // the response is erroneous
                    console.log("aaiSubscriber getAsdcModel BAD RESPONSE");
                    errorCallback(response);
                    return $q.reject();
                }
            }, errorCallback);

            function errorCallback(response) {
                $log.error("aaiSubscriber getAsdcModel - " + FIELD.ERROR.NO_MATCHING_MODEL_AAI + aaiModelVersionId);
                $scope.errorMsg = FIELD.ERROR.NO_MATCHING_MODEL_AAI + aaiModelVersionId;
                alert($scope.errorMsg);
            }
        }

        function returnMatchingServiceSubscription(serviceSubs, serviceId){
            var orchStatus;
            serviceSubs.forEach(function(item){
                if (item[FIELD.ID.SERVICE_INSTANCES] != null) {
                    item[FIELD.ID.SERVICE_INSTANCES][FIELD.ID.SERVICE_INSTANCE].forEach(function (service) {
                        if (service[FIELD.ID.SERVICE_INSTANCE_ID] === serviceId) {
                            orchStatus = service['orchestration-status'];
                        }
                    });
                }
            });
            return orchStatus;
        }

        $scope.getTenants = function (globalCustomerId) {
            $http.get(FIELD.ID.AAI_GET_TENTANTS + globalCustomerId)
                .then(function (response) {
                    return response.data;
                    //$location.path("/instantiate");
                }, function (response) {
                    //TODO
                });
        };

        $scope.isConfigurationDataAvailiable = function (configuration) {
            $log.debug(configuration);
            return configuration.configData && (!configuration.configData.errorDescription);
        };

        $scope.allowConfigurationActions = function (configuration) {
            $log.debug(configuration);
            return configuration.nodeStatus &&
                _.some(
                    [FIELD.STATUS.AAI_ACTIVE, FIELD.STATUS.AAI_INACTIVE, FIELD.STATUS.AAI_CREATED],
                    function (s) {
                        return s.toLowerCase() === configuration.nodeStatus.toLowerCase();
                    });
        };

        $scope.isActivateDeactivateEnabled = function(btnType) {
            if ($scope.serviceOrchestrationStatus) {
                var status = $scope.serviceOrchestrationStatus.toLowerCase();
                switch (btnType) {
                    case "activate":
                        return _.includes(COMPONENT.ACTIVATE_SERVICE_STATUSES, status);
                        break;
                    case "deactivate":
                        return status === 'active';
                        break;
                }
            }

            return false;
        };

        $scope.isShowVerifyService = function () {
            return featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_SHOW_VERIFY_SERVICE);
        }

        $scope.isShowErrorReport = function() {
            return featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_CREATE_ERROR_REPORTS);
        }

        $scope.isEnableVerifyService = function () {
            //Button should only be enabled when the service type is A la carte
            if (DataService.getALaCarte()) {
                return $scope.verifyButtonEnabled;
            }
            return false;
        };

        $scope.activateVerifyService = function () {
            $scope.verifyButtonEnabled = false;
            let url = COMPONENT.VERIFY_SERVICE_URL;

            let config = {
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            let data = {
                "serviceInstanceList": [
                    {
                        "serviceInstanceId": $scope.serviceInstanceId,
                        "modelVersionId": $scope.service.model.service.uuid,
                        "modelInvariantId": $scope.service.model.service.invariantUuid,
                        "customerId": $scope.globalCustomerId,
                        "serviceType": $scope.service.instance.serviceType,
                    }
                ]
            };
            AaiService.postPOMBAverificationRequest(url, data, config);

        };

        $scope.isShowAssignmentsEnabled = function () {
            if (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_SHOW_ASSIGNMENTS) && $scope.serviceOrchestrationStatus) {
                    return $scope.serviceOrchestrationStatus.toLowerCase() === 'assigned';
                }
            return false;
        };

        $scope.isActivateFabricConfiguration = function () {

            if (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS) && $scope.hasFabricConfigurations) {
                if ($scope.serviceOrchestrationStatus) {
                    return $scope.serviceOrchestrationStatus.toLowerCase() === 'assigned';
                }
            }
            return false;
        };

        $scope.isResumeShown = function (status) {
            var vfModuleStatus = status.toLowerCase();
            var vfModuleStatusHasAllowedResume = ['pendingactivation', 'assigned'];
            if (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_VF_MODULE_RESUME_STATUS_CREATE)) {
                vfModuleStatusHasAllowedResume.push('created');
            }
            return _.includes(vfModuleStatusHasAllowedResume, vfModuleStatus) && !$scope.isActivateDeactivateEnabled("activate");
        };

        $scope.handleInitialResponseInventoryItemsOptimized = function (response) {

            $scope.inventoryResponseItemList = response.data[FIELD.ID.INVENTORY_RESPONSE_ITEM]; // get data from json
            $log.debug($scope.inventoryResponseItemList);

            $scope.displayData = [];
            $scope.vnfs = [];

            $scope.counter = 100;

            $scope.subscriberName = "";

            $scope.allConfigurationsAssigned = true;
            // look up the subscriber name and service orchestration status from A&AI here...
            AaiService.getSubscriberNameAndServiceInstanceInfo( $scope.globalCustomerId,
                $scope.serviceInstanceId,
                'Service Instance Id',function (response) {
                    $scope.subscriberName = response['results'][0]['customer']['subscriber-name'];
                    DataService.setSubscriberName($scope.subscriberName);
                    $scope.serviceOrchestrationStatus = response['results'][0]['customer']['related-nodes'][0]['service-subscription']['related-nodes'][0]['service-instance']['orchestration-status'];
                    if ($scope.serviceOrchestrationStatus.toLowerCase() !== FIELD.STATUS.ASSIGNED.toLowerCase()) {
                        $scope.allConfigurationsAssigned = false;
                    }
                    angular.forEach($scope.inventoryResponseItemList, function (inventoryResponseItem, key) {

                        $scope.inventoryResponseItem = inventoryResponseItem;

                        $scope.service.instance = {
                            "name": $scope.inventoryResponseItem[FIELD.ID.SERVICE_INSTANCE][FIELD.ID.SERVICE_INSTANCE_NAME],
                            "serviceInstanceId": $scope.serviceInstanceId,
                            "serviceType": $scope.serviceType,
                            "globalCustomerId": $scope.globalCustomerId,
                            "subscriberName": $scope.subscriberName,
                            "id": $scope.serviceInstanceId,
                            "inputs": {
                                "a": {
                                    "type": PARAMETER.STRING,
                                    "description": FIELD.PROMPT.VAR_DESCRIPTION_A,
                                    "default": FIELD.PROMPT.DEFAULT_A
                                },
                                "b": {
                                    "type": PARAMETER.STRING,
                                    "description": FIELD.PROMPT.VAR_DESCRIPTION_B,
                                    "default": FIELD.PROMPT.DEFAULT_B
                                }
                            },
                            "object": $scope.inventoryResponseItem[FIELD.ID.SERVICE_INSTANCE],
                            "vnfs": [],
                            "networks": [],
                            "configurations": []
                        };

                        var portMirroringConfigurationIds = [];
                        if (inventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {

                            angular.forEach(inventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (subInventoryResponseItem, key) {
                                // i expect to find vnfs now

                                if (subInventoryResponseItem[FIELD.ID.L3_NETWORK] != null) {
                                    var l3NetworkObject = subInventoryResponseItem[FIELD.ID.L3_NETWORK];
                                    var l3Network = {
                                        "id": $scope.counter++,
                                        "name": l3NetworkObject[FIELD.ID.NETWORK_NAME],
                                        "itemType": FIELD.ID.L3_NETWORK,
                                        "nodeId": l3NetworkObject[FIELD.ID.NETWORK_ID],
                                        "nodeType": l3NetworkObject[FIELD.ID.NETWORK_TYPE],
                                        "nodeStatus": l3NetworkObject[FIELD.ID.ORCHESTRATION_STATUS],
                                        "object": l3NetworkObject,
                                        "nodes": [],
                                        "subnets": [],
                                        "vlans": []
                                    };
                                    if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
                                        //console.log ("subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS]=");
                                        //console.log (JSON.stringify (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS], null, 4 ));
                                        angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (subSubInventoryResponseItem, key) {
                                            //console.log (JSON.stringify (subSubInventoryResponseItem, null, 4 ));
                                            var subnet = {};
                                            var subnetObject;
                                            if (subSubInventoryResponseItem[FIELD.ID.SUB_NET] != null) {
                                                subnetObject = subSubInventoryResponseItem[FIELD.ID.SUB_NET];
                                                subnet = {
                                                    "subnet-id": subnetObject[FIELD.ID.SUBNET_ID],
                                                    "subnet-name": subnetObject[FIELD.ID.SUBNET_NAME],
                                                    "gateway-address": subnetObject[FIELD.ID.GATEWAY_ADDRESS],
                                                    "network-start-address": subnetObject[FIELD.ID.NETWORK_START_ADDRESS],
                                                    "cidr-mask": subnetObject[FIELD.ID.CIDR_MASK]
                                                };
                                                l3Network.subnets.push(subnet);
                                            }
                                        });
                                    }


                                    var networkObj = _.find(serviceNetworkVlans, { 'networkId': l3Network.nodeId});
                                    if (networkObj !== undefined && networkObj.vlans !== undefined) {
                                        l3Network["vlans"] = networkObj.vlans;
                                    }
                                    $scope.service.instance[FIELD.ID.NETWORKS].push(l3Network);
                                }

                                if (subInventoryResponseItem[FIELD.ID.GENERIC_VNF] != null) {
                                    var genericVnfObject = subInventoryResponseItem[FIELD.ID.GENERIC_VNF];

                                    var genericVnf = {
                                        "name": genericVnfObject[FIELD.ID.VNF_NAME],
                                        "id": $scope.counter++,
                                        "itemType": COMPONENT.VNF,
                                        "nodeType": genericVnfObject[FIELD.ID.VNF_TYPE],
                                        "nodeId": genericVnfObject[FIELD.ID.VNF_ID],
                                        "nodeStatus": genericVnfObject[FIELD.ID.ORCHESTRATION_STATUS],
                                        "modelVersionId" : genericVnfObject[FIELD.ID.MODEL_VERSION_ID],
                                        "object": genericVnfObject,
                                        "vfModules": [],
                                        "volumeGroups": [],
                                        "instanceGroups": [],
                                        "availableVolumeGroups": [],
                                        "networks": []
                                    };

                                    var vnfNetworkObj = _.find(vnfNetworksAndVlans, { 'vnfId': genericVnf.nodeId});
                                    if (vnfNetworkObj !== undefined && vnfNetworkObj.networks !== undefined) {
                                        genericVnf["networks"] = vnfNetworkObj.networks;
                                    }

                                    $scope.service.instance[FIELD.ID.VNFS].push(genericVnf);
                                    getRelatedInstanceGroupsByVnfId(genericVnf);



                                    // look for volume-groups
                                    if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
                                        angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (vfmodules, key) {

                                            if (vfmodules[FIELD.ID.VOLUME_GROUP] != null) {
                                                var volumeGroupObject = vfmodules[FIELD.ID.VOLUME_GROUP];
                                                var volumeGroup = {
                                                    "id": $scope.counter++,
                                                    "name": volumeGroupObject[FIELD.ID.VOLUME_GROUP_NAME],
                                                    "itemType": FIELD.ID.VOLUME_GROUP,
                                                    "nodeId": volumeGroupObject[FIELD.ID.VOLUME_GROUP_ID],
                                                    "nodeType": volumeGroupObject[FIELD.ID.VNF_TYPE],
                                                    "nodeStatus": volumeGroupObject[FIELD.ID.ORCHESTRATION_STATUS],
                                                    "object": volumeGroupObject,
                                                    "nodes": []
                                                };
                                                genericVnf[FIELD.ID.VOLUMEGROUPS].push(volumeGroup);
                                                genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS].push(volumeGroup);
                                            }
                                        });
                                    }
                                    // now we've loaded up the availableVolumeGroups, we can use it
                                    if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
                                        angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (vfmodules, key) {

                                            if (vfmodules[FIELD.ID.VF_MODULE] != null) {
                                                var vfModuleObject = vfmodules[FIELD.ID.VF_MODULE];
                                                var vfModule = {
                                                    "id": $scope.counter++,
                                                    "name": vfModuleObject[FIELD.ID.VF_MODULE_NAME],
                                                    "itemType": FIELD.ID.VF_MODULE,
                                                    "nodeType": FIELD.ID.VF_MODULE,
                                                    "nodeStatus": vfModuleObject[FIELD.ID.ORCHESTRATION_STATUS],
                                                    "volumeGroups": [],
                                                    "object": vfModuleObject,
                                                    "networks": []
                                                };
                                                genericVnf[FIELD.ID.VF_MODULES].push(vfModule);
                                                if (vfmodules[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
                                                    angular.forEach(vfmodules[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (networks, key) {
                                                        if (networks[FIELD.ID.L3_NETWORK] != null) {
                                                            var l3NetworkObject = networks[FIELD.ID.L3_NETWORK];
                                                            var l3Network = {
                                                                "id": $scope.counter++,
                                                                "name": l3NetworkObject[FIELD.ID.NETWORK_NAME],
                                                                "itemType": FIELD.ID.L3_NETWORK,
                                                                "nodeId": l3NetworkObject[FIELD.ID.NETWORK_ID],
                                                                "nodeType": l3NetworkObject[FIELD.ID.NETWORK_TYPE],
                                                                "nodeStatus": l3NetworkObject[FIELD.ID.ORCHESTRATION_STATUS],
                                                                "object": l3NetworkObject,
                                                                "nodes": []
                                                            };
                                                            vfModule[FIELD.ID.NETWORKS].push(l3Network);
                                                        }
                                                        if (networks[FIELD.ID.VOLUME_GROUP] != null) {
                                                            var volumeGroupObject = networks[FIELD.ID.VOLUME_GROUP];

                                                            var volumeGroup = {
                                                                "id": $scope.counter++,
                                                                "name": volumeGroupObject[FIELD.ID.VOLUME_GROUP_NAME],
                                                                "itemType": FIELD.ID.VOLUME_GROUP,
                                                                "nodeId": volumeGroupObject[FIELD.ID.VOLUME_GROUP_ID],
                                                                "nodeType": volumeGroupObject[FIELD.ID.VNF_TYPE],
                                                                "nodeStatus": volumeGroupObject[FIELD.ID.ORCHESTRATION_STATUS],
                                                                "object": volumeGroupObject,
                                                                "nodes": []
                                                            };
                                                            var tmpVolGroup = [];

                                                            angular.forEach(genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS], function (avgroup, key) {
                                                                if (avgroup.name != volumeGroup.name) {
                                                                    tmpVolGroup.push(avgroup);
                                                                }
                                                            });

                                                            genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS] = tmpVolGroup;

                                                            vfModule[FIELD.ID.VOLUMEGROUPS].push(volumeGroup);
                                                        }

                                                    });
                                                }
                                            }
                                        });
                                    }
                                }


                                if (subInventoryResponseItem[FIELD.ID.GENERIC_CONFIGURATION] != null) {
                                    var configObject = subInventoryResponseItem[FIELD.ID.GENERIC_CONFIGURATION];
                                    var config = {
                                        "id": $scope.counter++,
                                        "name": configObject[FIELD.ID.CONFIGURATION_NAME],
                                        "itemType": FIELD.ID.CONFIGURATION,
                                        "nodeId": configObject[FIELD.ID.CONFIGURATION_ID],
                                        "nodeType": configObject[FIELD.ID.CONFIGURATION_TYPE],
                                        "nodeStatus": configObject[FIELD.ID.ORCHESTRATION_STATUS],
                                        "modelInvariantId": configObject[FIELD.ID.MODEL_INVAR_ID],
                                        "modelVersionId": configObject[FIELD.ID.MODEL_VERSION_ID],
                                        "modelCustomizationId": configObject[FIELD.ID.MODEL_CUSTOMIZATION_ID],
                                        "object": configObject,
                                        "ports": [],
                                        "configData" : null
                                    };
                                    if( !$scope.hasFabricConfigurations ) {
                                        portMirroringConfigurationIds.push(configObject[FIELD.ID.CONFIGURATION_ID]);
                                        $scope.service.instance[FIELD.ID.CONFIGURATIONS].push(config);
                                    } else {
                                        if (config.nodeStatus.toLowerCase() !== FIELD.STATUS.ASSIGNED.toLowerCase()) {
                                            $scope.allConfigurationsAssigned = false;
                                            if ($scope.isActivateFabricConfiguration()) {
                                                $scope.errorMsg = "Activate fabric configuration button is not available as some of the configuration objects are not in Assigned status. Check MSO logs for the reasons for this abnormal case.";
                                            }
                                        }
                                    }
                                }

                            });

                            AaiService.getPortMirroringData(portMirroringConfigurationIds).then(function(result){
                                angular.forEach($scope.service.instance[FIELD.ID.CONFIGURATIONS], function(config){
                                    config['configData'] = result.data[config['nodeId']];

                                    if (config.configData && config.configData.errorDescription) {
                                        $scope.errorMsg = ($scope.errorMsg ? $scope.errorMsg + "\n" : "") +
                                            "Cannot read cloud-region for configuration \"" + config.name + "\": " +
                                            config.configData.errorDescription;
                                    }
                                });
                            });

                            AaiService.getPortMirroringSourcePorts(portMirroringConfigurationIds).then(function(result){
                                angular.forEach($scope.service.instance[FIELD.ID.CONFIGURATIONS], function(config){
                                    angular.forEach(result.data[config['nodeId']], function(port){
                                        if (port.errorDescription) {
                                            $scope.errorMsg = ($scope.errorMsg ? $scope.errorMsg + "\n" : "") +
                                                "Cannot read a source port for configuration \"" + config.name + "\": " +
                                                port.errorDescription;
                                        } else {
                                            config.ports.push({
                                                "portId": port[FIELD.ID.PORT_ID],
                                                "portName": port[FIELD.ID.PORT_NAME],
                                                "portStatus": port[FIELD.ID.PORT_MIRRORED] === true ? FIELD.STATUS.AAI_ENABLED : FIELD.STATUS.AAI_DISABLED
                                            });
                                        }
                                    });
                                });
                            });

                        }
                    });

                    var aaiNetworkIds = _.map(serviceNetworkVlans, 'networkId');
                    var serviceInstanceNetworkIds = _.map($scope.service.instance[FIELD.ID.NETWORKS], 'nodeId');
                    var isContains = aaiNetworkIds.every(function(val) { return serviceInstanceNetworkIds.indexOf(val) >= 0; });
                    if (aaiNetworkIds.length && !isContains)  {
                        $log.error("vlansByNetworks contain network that not found in service instance", aaiNetworkIds, serviceInstanceNetworkIds);
                    }

                });
        };

        $scope.handleInitialResponseInventoryItems = function (response) {

            $scope.inventoryResponseItemList = response.data[FIELD.ID.INVENTORY_RESPONSE_ITEM]; // get data from json
            $log.debug($scope.inventoryResponseItemList);

            $scope.displayData = [];
            $scope.vnfs = [];

            $scope.counter = 100;

            $scope.subscriberName = "";

            $scope.allConfigurationsAssigned = true;
            // just look up the subscriber name in A&AI here...
            AaiService.getSubscriberName($scope.globalCustomerId, function (response) {
                $scope.subscriberName = response.subscriberName;
                DataService.setSubscriberName($scope.subscriberName);
                $scope.serviceOrchestrationStatus = returnMatchingServiceSubscription(response.serviceSubscriptions[FIELD.ID.SERVICE_SUBSCRIPTION], $scope.serviceInstanceId);
                if ($scope.serviceOrchestrationStatus.toLowerCase() !== FIELD.STATUS.ASSIGNED.toLowerCase()) {
                    $scope.allConfigurationsAssigned = false;
                }
                angular.forEach($scope.inventoryResponseItemList, function (inventoryResponseItem, key) {

                    $scope.inventoryResponseItem = inventoryResponseItem;

                    $scope.service.instance = {
                        "name": $scope.inventoryResponseItem[FIELD.ID.SERVICE_INSTANCE][FIELD.ID.SERVICE_INSTANCE_NAME],
                        "serviceInstanceId": $scope.serviceInstanceId,
                        "serviceType": $scope.serviceType,
                        "globalCustomerId": $scope.globalCustomerId,
                        "subscriberName": $scope.subscriberName,
                        "id": $scope.serviceInstanceId,
                        "inputs": {
                            "a": {
                                "type": PARAMETER.STRING,
                                "description": FIELD.PROMPT.VAR_DESCRIPTION_A,
                                "default": FIELD.PROMPT.DEFAULT_A
                            },
                            "b": {
                                "type": PARAMETER.STRING,
                                "description": FIELD.PROMPT.VAR_DESCRIPTION_B,
                                "default": FIELD.PROMPT.DEFAULT_B
                            }
                        },
                        "object": $scope.inventoryResponseItem[FIELD.ID.SERVICE_INSTANCE],
                        "vnfs": [],
                        "networks": [],
                        "configurations": []
                    };

                    var portMirroringConfigurationIds = [];
                    if (inventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {

                        angular.forEach(inventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (subInventoryResponseItem, key) {
                            // i expect to find vnfs now

                            if (subInventoryResponseItem[FIELD.ID.L3_NETWORK] != null) {
                                var l3NetworkObject = subInventoryResponseItem[FIELD.ID.L3_NETWORK];
                                var l3Network = {
                                    "id": $scope.counter++,
                                    "name": l3NetworkObject[FIELD.ID.NETWORK_NAME],
                                    "itemType": FIELD.ID.L3_NETWORK,
                                    "nodeId": l3NetworkObject[FIELD.ID.NETWORK_ID],
                                    "nodeType": l3NetworkObject[FIELD.ID.NETWORK_TYPE],
                                    "nodeStatus": l3NetworkObject[FIELD.ID.ORCHESTRATION_STATUS],
                                    "object": l3NetworkObject,
                                    "nodes": [],
                                    "subnets": [],
                                    "vlans": []
                                };
                                if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
                                    //console.log ("subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS]=");
                                    //console.log (JSON.stringify (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS], null, 4 ));
                                    angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (subSubInventoryResponseItem, key) {
                                        //console.log (JSON.stringify (subSubInventoryResponseItem, null, 4 ));
                                        var subnet = {};
                                        var subnetObject;
                                        if (subSubInventoryResponseItem[FIELD.ID.SUB_NET] != null) {
                                            subnetObject = subSubInventoryResponseItem[FIELD.ID.SUB_NET];
                                            subnet = {
                                                "subnet-id": subnetObject[FIELD.ID.SUBNET_ID],
                                                "subnet-name": subnetObject[FIELD.ID.SUBNET_NAME],
                                                "gateway-address": subnetObject[FIELD.ID.GATEWAY_ADDRESS],
                                                "network-start-address": subnetObject[FIELD.ID.NETWORK_START_ADDRESS],
                                                "cidr-mask": subnetObject[FIELD.ID.CIDR_MASK]
                                            };
                                            l3Network.subnets.push(subnet);
                                        }
                                    });
                                }

                                
                                var networkObj = _.find(serviceNetworkVlans, { 'networkId': l3Network.nodeId});
                                if (networkObj !== undefined && networkObj.vlans !== undefined) {
                                    l3Network["vlans"] = networkObj.vlans;
                                }
                                $scope.service.instance[FIELD.ID.NETWORKS].push(l3Network);
                            }

                            if (subInventoryResponseItem[FIELD.ID.GENERIC_VNF] != null) {
                                var genericVnfObject = subInventoryResponseItem[FIELD.ID.GENERIC_VNF];

                                var genericVnf = {
                                    "name": genericVnfObject[FIELD.ID.VNF_NAME],
                                    "id": $scope.counter++,
                                    "itemType": COMPONENT.VNF,
                                    "nodeType": genericVnfObject[FIELD.ID.VNF_TYPE],
                                    "nodeId": genericVnfObject[FIELD.ID.VNF_ID],
                                    "nodeStatus": genericVnfObject[FIELD.ID.ORCHESTRATION_STATUS],
                                    "modelVersionId" : genericVnfObject[FIELD.ID.MODEL_VERSION_ID],
                                    "object": genericVnfObject,
                                    "vfModules": [],
                                    "volumeGroups": [],
                                    "instanceGroups": [],
                                    "availableVolumeGroups": [],
                                    "networks": []
                                };

                                var vnfNetworkObj = _.find(vnfNetworksAndVlans, { 'vnfId': genericVnf.nodeId});
                                if (vnfNetworkObj !== undefined && vnfNetworkObj.networks !== undefined) {
                                    genericVnf["networks"] = vnfNetworkObj.networks;
                                }

                                $scope.service.instance[FIELD.ID.VNFS].push(genericVnf);
                                getRelatedInstanceGroupsByVnfId(genericVnf);



                                // look for volume-groups
                                if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
                                    angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (vfmodules, key) {

                                        if (vfmodules[FIELD.ID.VOLUME_GROUP] != null) {
                                            var volumeGroupObject = vfmodules[FIELD.ID.VOLUME_GROUP];
                                            var volumeGroup = {
                                                "id": $scope.counter++,
                                                "name": volumeGroupObject[FIELD.ID.VOLUME_GROUP_NAME],
                                                "itemType": FIELD.ID.VOLUME_GROUP,
                                                "nodeId": volumeGroupObject[FIELD.ID.VOLUME_GROUP_ID],
                                                "nodeType": volumeGroupObject[FIELD.ID.VNF_TYPE],
                                                "nodeStatus": volumeGroupObject[FIELD.ID.ORCHESTRATION_STATUS],
                                                "object": volumeGroupObject,
                                                "nodes": []
                                            };
                                            genericVnf[FIELD.ID.VOLUMEGROUPS].push(volumeGroup);
                                            genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS].push(volumeGroup);
                                        }
                                    });
                                }
                                // now we've loaded up the availableVolumeGroups, we can use it
                                if (subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
                                    angular.forEach(subInventoryResponseItem[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (vfmodules, key) {

                                        if (vfmodules[FIELD.ID.VF_MODULE] != null) {
                                            var vfModuleObject = vfmodules[FIELD.ID.VF_MODULE];
                                            var vfModule = {
                                                "id": $scope.counter++,
                                                "name": vfModuleObject[FIELD.ID.VF_MODULE_NAME],
                                                "itemType": FIELD.ID.VF_MODULE,
                                                "nodeType": FIELD.ID.VF_MODULE,
                                                "nodeStatus": vfModuleObject[FIELD.ID.ORCHESTRATION_STATUS],
                                                "volumeGroups": [],
                                                "object": vfModuleObject,
                                                "networks": []
                                            };
                                            genericVnf[FIELD.ID.VF_MODULES].push(vfModule);
                                            if (vfmodules[FIELD.ID.INVENTORY_RESPONSE_ITEMS] != null) {
                                                angular.forEach(vfmodules[FIELD.ID.INVENTORY_RESPONSE_ITEMS][FIELD.ID.INVENTORY_RESPONSE_ITEM], function (networks, key) {
                                                    if (networks[FIELD.ID.L3_NETWORK] != null) {
                                                        var l3NetworkObject = networks[FIELD.ID.L3_NETWORK];
                                                        var l3Network = {
                                                            "id": $scope.counter++,
                                                            "name": l3NetworkObject[FIELD.ID.NETWORK_NAME],
                                                            "itemType": FIELD.ID.L3_NETWORK,
                                                            "nodeId": l3NetworkObject[FIELD.ID.NETWORK_ID],
                                                            "nodeType": l3NetworkObject[FIELD.ID.NETWORK_TYPE],
                                                            "nodeStatus": l3NetworkObject[FIELD.ID.ORCHESTRATION_STATUS],
                                                            "object": l3NetworkObject,
                                                            "nodes": []
                                                        };
                                                        vfModule[FIELD.ID.NETWORKS].push(l3Network);
                                                    }
                                                    if (networks[FIELD.ID.VOLUME_GROUP] != null) {
                                                        var volumeGroupObject = networks[FIELD.ID.VOLUME_GROUP];

                                                        var volumeGroup = {
                                                            "id": $scope.counter++,
                                                            "name": volumeGroupObject[FIELD.ID.VOLUME_GROUP_NAME],
                                                            "itemType": FIELD.ID.VOLUME_GROUP,
                                                            "nodeId": volumeGroupObject[FIELD.ID.VOLUME_GROUP_ID],
                                                            "nodeType": volumeGroupObject[FIELD.ID.VNF_TYPE],
                                                            "nodeStatus": volumeGroupObject[FIELD.ID.ORCHESTRATION_STATUS],
                                                            "object": volumeGroupObject,
                                                            "nodes": []
                                                        };
                                                        var tmpVolGroup = [];

                                                        angular.forEach(genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS], function (avgroup, key) {
                                                            if (avgroup.name != volumeGroup.name) {
                                                                tmpVolGroup.push(avgroup);
                                                            }
                                                        });

                                                        genericVnf[FIELD.ID.AVAILABLEVOLUMEGROUPS] = tmpVolGroup;

                                                        vfModule[FIELD.ID.VOLUMEGROUPS].push(volumeGroup);
                                                    }

                                                });
                                            }
                                        }
                                    });
                                }
                            }


                            if (subInventoryResponseItem[FIELD.ID.GENERIC_CONFIGURATION] != null) {
                                var configObject = subInventoryResponseItem[FIELD.ID.GENERIC_CONFIGURATION];
                                var config = {
                                    "id": $scope.counter++,
                                    "name": configObject[FIELD.ID.CONFIGURATION_NAME],
                                    "itemType": FIELD.ID.CONFIGURATION,
                                    "nodeId": configObject[FIELD.ID.CONFIGURATION_ID],
                                    "nodeType": configObject[FIELD.ID.CONFIGURATION_TYPE],
                                    "nodeStatus": configObject[FIELD.ID.ORCHESTRATION_STATUS],
                                    "modelInvariantId": configObject[FIELD.ID.MODEL_INVAR_ID],
                                    "modelVersionId": configObject[FIELD.ID.MODEL_VERSION_ID],
                                    "modelCustomizationId": configObject[FIELD.ID.MODEL_CUSTOMIZATION_ID],
                                    "object": configObject,
                                    "ports": [],
                                    "configData" : null
                                };
                                if( !$scope.hasFabricConfigurations ) {
                                portMirroringConfigurationIds.push(configObject[FIELD.ID.CONFIGURATION_ID]);
                                    $scope.service.instance[FIELD.ID.CONFIGURATIONS].push(config);
                                } else {
                                   if (config.nodeStatus.toLowerCase() !== FIELD.STATUS.ASSIGNED.toLowerCase()) {
                                       $scope.allConfigurationsAssigned = false;
                                       if ($scope.isActivateFabricConfiguration()) {
                                            $scope.errorMsg = "Activate fabric configuration button is not available as some of the configuration objects are not in Assigned status. Check MSO logs for the reasons for this abnormal case.";
                                       }
                                   }
                               }
                            }

                        });

                        AaiService.getPortMirroringData(portMirroringConfigurationIds).then(function(result){
                           angular.forEach($scope.service.instance[FIELD.ID.CONFIGURATIONS], function(config){
                                config['configData'] = result.data[config['nodeId']];

                               if (config.configData && config.configData.errorDescription) {
                                   $scope.errorMsg = ($scope.errorMsg ? $scope.errorMsg + "\n" : "") +
                                       "Cannot read cloud-region for configuration \"" + config.name + "\": " +
                                       config.configData.errorDescription;
                               }
                           });
                        });

                        AaiService.getPortMirroringSourcePorts(portMirroringConfigurationIds).then(function(result){
                           angular.forEach($scope.service.instance[FIELD.ID.CONFIGURATIONS], function(config){
                                angular.forEach(result.data[config['nodeId']], function(port){
                                   if (port.errorDescription) {
                                       $scope.errorMsg = ($scope.errorMsg ? $scope.errorMsg + "\n" : "") +
                                           "Cannot read a source port for configuration \"" + config.name + "\": " +
                                           port.errorDescription;
                                   } else {
                                       config.ports.push({
                                           "portId": port[FIELD.ID.PORT_ID],
                                           "portName": port[FIELD.ID.PORT_NAME],
                                           "portStatus": port[FIELD.ID.PORT_MIRRORED] === true ? FIELD.STATUS.AAI_ENABLED : FIELD.STATUS.AAI_DISABLED
                                       });
                                   }
                               });
                           });
                        });

                    }
                });

                var aaiNetworkIds = _.map(serviceNetworkVlans, 'networkId');
                var serviceInstanceNetworkIds = _.map($scope.service.instance[FIELD.ID.NETWORKS], 'nodeId');
                var isContains = aaiNetworkIds.every(function(val) { return serviceInstanceNetworkIds.indexOf(val) >= 0; });
                if (aaiNetworkIds.length && !isContains)  {
                    $log.error("vlansByNetworks contain network that not found in service instance", aaiNetworkIds, serviceInstanceNetworkIds);
                }

            });
        };




        $scope.handleInitialResponse = function (response) {
            try {
                $scope.enableCloseButton(true);
                $scope.updateLog(response);
                if (response.data.status < 200 || response.data.status > 202) {
                    $scope.showError(FIELD.ERROR.MSO);
                    $scope.status = FIELD.ERROR.AAI_FETCHING_CUST_DATA + response.data.status;

                    return;
                }

                $scope.setProgress(100); // done
                $scope.status = FIELD.STATUS.DONE;
                $scope.isSpinnerVisible = false;

                $scope.customer = response.data.customer; // get data from json

                $scope.customerList = [];

                angular.forEach($scope.customer, function (subVal, subKey) {
                    var cust = {
                        "globalCustomerId": subVal[FIELD.ID.GLOBAL_CUSTOMER_ID],
                        "subscriberName": subVal[FIELD.ID.SUBNAME],
                        "isPermitted": subVal[FIELD.ID.IS_PERMITTED]
                    };

                    $scope.customerList.push(cust);
                });

            } catch (error) {
                $scope.showContentError(error);
            }
        }

        $scope.autoGetSubs = function () {
            /*
             * Optionally comment in / out one of these method calls (or add a similar
             * entry) to auto-invoke an entry when the test screen is redrawn.
             */
            $scope.getSubs();

        }

        $scope.updateLog = function (response) {
//		$scope.log = UtilityService.getCurrentTime() + " HTTP Status: " + 
//		UtilityService.getHttpStatusText(response.data.status) + "\n" +
//		angular.toJson(response.data.entity, true) + "\n\n" + $scope.log;
//		UtilityService.checkUndefined("entity", response.data.entity);
//		UtilityService.checkUndefined("status", response.data.status);				
        }

        $scope.handleServerError = function (response, status) {
            $scope.enableCloseButton(true);
            var message = UtilityService.getHttpErrorMessage(response);
            if (message != "") {
                message = " (" + message + ")";
            }
            $scope.showError(FIELD.ERROR.SYSTEM_ERROR + message);
        }

        $scope.showContentError = function (message) {
            // $log.debug(message);
            console.log(message);
            if (UtilityService.hasContents(message)) {
                $scope.showError("System failure (" + message + ")");
            } else {
                $scope.showError(FIELD.ERROR.SYSTEM_ERROR);
            }
        }

        $scope.showError = function (message) {
            $scope.isSpinnerVisible = false;
            $scope.isProgressVisible = false;
            $scope.error = message;
            $scope.status = FIELD.STATUS.ERROR;
        }

        $scope.close = function () {
            if ($scope.timer != undefined) {
                $timeout.cancel($scope.timer);
            }
            $scope.isPopupVisible = false;
        }


        /*
         * Consider converting the progress bar mechanism, the disabled button handling
         * and the following methods to generic Angular directive(s) and/or approach.
         */

        $scope.enableCloseButton = function (isEnabled) {
            var selector = FIELD.STYLE.MSO_CTRL_BTN;

            $scope.isCloseEnabled = isEnabled;

            if (isEnabled) {
                $(selector).addClass(FIELD.STYLE.BTN_PRIMARY).removeClass(FIELD.STYLE.BTN_INACTIVE).attr(FIELD.STYLE.BTN_TYPE, FIELD.STYLE.PRIMARY);
            } else {
                $(selector).removeClass(FIELD.STYLE.BTN_PRIMARY).addClass(FIELD.STYLE.BTN_INACTIVE).attr(FIELD.STYLE.BTN_TYPE, FIELD.STYLE.DISABLED);
            }
        }

        $scope.resetProgress = function () {
            $scope.percentProgress = 0;
            $scope.progressClass = FIELD.STYLE.PROGRESS_BAR_INFO;
        }

        $scope.setProgress = function (percentProgress) {
            percentProgress = parseInt(percentProgress);
            if (percentProgress >= 100) {
                $scope.progressClass = FIELD.STYLE.PROGRESS_BAR_SUCCESS;
            }

            if (percentProgress < $scope.percentProgress) {
                return;
            }

            $scope.percentProgress = percentProgress;
            $scope.progressWidth = {width: percentProgress + "%"};
            if (percentProgress >= 5) {
                $scope.progressText = percentProgress + " %";
            } else {
                // Hidden since color combination is barely visible when progress portion is narrow.
                $scope.progressText = "";
            }
        }

        $scope.reloadRoute = function () {
            $route.reload();
        }

        $scope.prevPage = function () {
            $scope.currentPage--;
        }

        var getAicZoneAndSendToMso = function (msoType, requestParams) {
            AaiService.getAicZoneForPNF(
                $scope.service.instance.globalCustomerId,
                $scope.service.instance.serviceType,
                $scope.service.instance.serviceInstanceId, function (aicZone) {

                    requestParams.aicZone = aicZone;

                    openMsoModal(msoType, requestParams);
                });
        };

        var activateDeactivateServiceInstance = function (msoType) {
            var requestParams = {
                model: $scope.service.model,
                instance: $scope.service.instance
            };

            if (DataService.getLoggedInUserId()) {
                requestParams.userId = DataService.getLoggedInUserId();
                getAicZoneAndSendToMso(msoType, requestParams)
            } else {
                AaiService.getLoggedInUserID(function (response) {
                    var userID = response.data;
                    DataService.setLoggedInUserId(userID);
                    requestParams.userId = userID;

                    getAicZoneAndSendToMso(msoType, requestParams);
                });
            }
        };


        var activateFabricConfigurationInstance = function (msoType) {
            var requestParams = {
                model: $scope.service.model,
                service: $scope.service,
                serviceInstanceId: $scope.serviceInstanceId
            };

            if (DataService.getLoggedInUserId()) {
                requestParams.userId = DataService.getLoggedInUserId();
                openMsoModal(COMPONENT.MSO_ACTIVATE_FABRIC_CONFIGURATION_REQ, requestParams);
            } else {
                AaiService.getLoggedInUserID(function (response) {
                    var userID = response.data;
                    DataService.setLoggedInUserId(userID);
                    requestParams.userId = userID;
                    openMsoModal(COMPONENT.MSO_ACTIVATE_FABRIC_CONFIGURATION_REQ, requestParams);
                });
            }
        };

        $scope.showAssignmentsSDNC = function () {

            if ($scope.service && $scope.service.instance) {
                return VIDCONFIGURATION.SDNC_SHOW_ASSIGNMENTS_URL.replace("<SERVICE_INSTANCE_ID>" , $scope.service.instance.id);
            }
            return null;
        };

        $scope.activateFabricConfigurationMSO = function () {
            activateFabricConfigurationInstance(COMPONENT.MSO_ACTIVATE_FABRIC_CONFIGURATION_REQ);
        };

        $scope.activateMSOInstance = function () {

            activateDeactivateServiceInstance(COMPONENT.MSO_ACTIVATE_SERVICE_REQ);
        };

        $scope.deactivateMSOInstance = function () {

            activateDeactivateServiceInstance(COMPONENT.MSO_DEACTIVATE_SERVICE_REQ);
        };


        $scope.deleteConfiguration = function (serviceObject, configuration) {
            AaiService.getLoggedInUserID(function (response) {
                DataService.setLoggedInUserId(response.data);
                var requestParams = {
                    serviceModel: {
                        "modelType": "configuration",
                        "modelInvariantId": serviceObject.model.service.invariantUuid,
                        "modelVersionId": serviceObject.model.service.uuid,
                        "modelName": serviceObject.model.service.name,
                        "modelVersion": serviceObject.model.service.version
                    },
                    configurationModel: {
                        "modelType": "configuration",
                        "modelInvariantId": configuration.modelInvariantId,
                        "modelVersionId": configuration.modelVersionId,
                        "modelCustomizationId": configuration.modelCustomizationId
                    },
                    serviceInstanceId: serviceObject.instance.serviceInstanceId,
                    configurationId: configuration.nodeId,
                    configStatus: configuration.DELETE,
                    userId: DataService.getLoggedInUserId()
                };

                openMsoModal(COMPONENT.MSO_DELETE_CONFIGURATION_REQ, requestParams, configuration);
            });

        };

        $scope.toggleConfigurationStatus = function (serviceObject, configuration) {
            AaiService.getLoggedInUserID(function (response) {
                DataService.setLoggedInUserId(response.data);
                var requestParams = {
                    serviceModel: {
                        "modelType": "service",
                        "modelInvariantId": serviceObject.model.service.invariantUuid,
                        "modelVersionId": serviceObject.model.service.uuid,
                        "modelName": serviceObject.model.service.name,
                        "modelVersion": serviceObject.model.service.version
                    },
                    configurationModel: {
                        "modelType": "configuration",
                        "modelInvariantId": configuration.modelInvariantId,
                        "modelVersionId": configuration.modelVersionId,
                        "modelCustomizationId": configuration.modelCustomizationId
                    },
                    serviceInstanceId: serviceObject.instance.serviceInstanceId,
                    configurationId: configuration.nodeId,
                    configStatus: configuration.nodeStatus,
                    userId: DataService.getLoggedInUserId()
                };

                openMsoModal(COMPONENT.MSO_CHANGE_CONFIG_STATUS_REQ, requestParams, configuration);
            });

        };

        $scope.togglePortStatus = function (serviceObject, configuration, port) {
            AaiService.getLoggedInUserID(function (response) {
                DataService.setLoggedInUserId(response.data);

                var requestParams = {
                    serviceInstanceId: serviceObject.instance.serviceInstanceId,
                    configurationId: configuration.nodeId,
                    portId: port.portId,
                    portStatus: port.portStatus,
                    serviceModel: {
                        "modelType": "service",
                        "modelInvariantId": serviceObject.model.service.invariantUuid,
                        "modelVersionId": serviceObject.model.service.uuid,
                        "modelName": serviceObject.model.service.name,
                        "modelVersion": serviceObject.model.service.version
                    },
                    configurationModel: {
                        "modelType": "configuration",
                        "modelInvariantId": configuration.modelInvariantId,
                        "modelVersionId": configuration.modelVersionId,
                        "modelCustomizationId": configuration.modelCustomizationId
                    },
                    userId: DataService.getLoggedInUserId()
                };

                openMsoModal(COMPONENT.MSO_CHANGE_PORT_STATUS_REQ, requestParams, configuration);
            });
        };

        $scope.dissociatePnf = function (pnfName) {

            var jobInfo = {
                status: "confirm",
                message: "Are you sure you would like to dissociate " + pnfName + " from the service instance?"
            };

            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/alert-modal/alert-modal.html',
                controller: 'alertModalController',
                controllerAs: 'vm',
                appendTo: angular.element("#pnfs-tree"),
                resolve: {
                    jobInfo: function () {
                        return jobInfo;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the alert modal.", result);

                if (result) {
                    var requestParams = {
                        pnf: pnfName,
                        serviceModelInfo: {
                            invariantUuid: $scope.service.model.service.invariantUuid,
                            uuid: $scope.service.model.service.uuid,
                            version: $scope.service.model.service.version,
                            name: $scope.service.model.service.name
                        },
                        serviceInstanceId: $scope.service.instance.serviceInstanceId
                    };

                    if (DataService.getLoggedInUserId()) {
                        requestParams.userId = DataService.getLoggedInUserId();
                        requestParams.attuuid = DataService.getLoggedInUserId();
                        openMsoModal(COMPONENT.MSO_REMOVE_RELATIONSHIP, requestParams);
                    } else {
                        AaiService.getLoggedInUserID(function (response) {
                            DataService.setLoggedInUserId(response.data);

                            requestParams.userId = response.data;
                            requestParams.attuuid = response.data;
                            openMsoModal(COMPONENT.MSO_REMOVE_RELATIONSHIP, requestParams);
                        });
                    }
                }
            });


        };

        var openMsoModal = function (msoType, requestParams, configuration) {
            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/mso-commit/mso-commit.html',
                controller: "msoCommitModalController",
                backdrop: false,
                resolve: {
                    msoType: function () {
                        return msoType;
                    },
                    requestParams: function () {
                        requestParams.callbackFunction = updateViewCallbackFunction;
                        return requestParams;
                    },
                    configuration: function () {
                        return configuration;
                    }
                }
            });
        };

        var updateViewCallbackFunction = function (response) {
            $scope.callbackResults = "";
            var color = FIELD.ID.COLOR_NONE;
            $scope.callbackStyle = {
                "background-color": color
            };

            /*
             * This 1/2 delay was only added to visually highlight the status
             * change. Probably not needed in the real application code.
             */
            $timeout(function () {
                $scope.callbackResults = UtilityService.getCurrentTime()
                    + FIELD.STATUS.IS_SUCCESSFUL + response.isSuccessful;
                if (response.isSuccessful) {
                    color = FIELD.ID.COLOR_8F8;
                    $scope.reloadRoute();
                } else {
                    color = FIELD.ID.COLOR_F88;
                }
                $scope.callbackStyle = {
                    "background-color": color
                };
            }, 500);
        };

        $scope.nextPage = function () {
            $scope.currentPage++;
        };

        $scope.serviceInstanceses = [{"sinstance": FIELD.NAME.SERVICE_INSTANCE_Id}, {"sinstance": FIELD.NAME.SERVICE_INSTANCE_NAME}];

        function navigateToSearchResultsPage(globalCustomerId, selectedServiceInstance, selectedProjects, selectedOwningEntities, selectedInstanceIdentifierType) {
            var projectQuery = AaiService.getMultipleValueParamQueryString(_.map(selectedProjects, 'id'), COMPONENT.PROJECT_SUB_PATH);
            var owningEntityQuery = AaiService.getMultipleValueParamQueryString(_.map(selectedOwningEntities, 'id'), COMPONENT.OWNING_ENTITY_SUB_PATH);
            var globalCustomerIdQuery = globalCustomerId ? COMPONENT.SELECTED_SUBSCRIBER_SUB_PATH + globalCustomerId : null;
            var serviceInstanceQuery = selectedServiceInstance ? COMPONENT.SELECTED_SERVICE_INSTANCE_SUB_PATH + selectedServiceInstance : null;
            var serviceInstanceTypeQuery = selectedInstanceIdentifierType ? COMPONENT.SELECTED_SERVICE_INSTANCE_TYPE_SUB_PATH + selectedInstanceIdentifierType : null;
            var query = AaiService.getJoinedQueryString([projectQuery, owningEntityQuery, globalCustomerIdQuery, serviceInstanceQuery, serviceInstanceTypeQuery]);

            window.location.href = COMPONENT.SELECTED_SERVICE_SUB_PATH + query;
        }

        $scope.getServiceInstancesSearchResults =
            function (selectedCustomer, selectedInstanceIdentifierType, selectedServiceInstance, selectedProject, selectedOwningEntity) {
                var isSelectionValid = UtilityService.hasContents(selectedCustomer) || UtilityService.hasContents(selectedProject)
                    || UtilityService.hasContents(selectedOwningEntity) || UtilityService.hasContents(selectedServiceInstance);

                if (isSelectionValid) {
                    if (UtilityService.hasContents(selectedServiceInstance)) {
                        AaiService
                            .getGlobalCustomerIdByInstanceIdentifier(selectedServiceInstance, selectedInstanceIdentifierType)
                            .then(handleCustomerIdResponse);
                    } else {
                        navigateToSearchResultsPage(selectedCustomer, null, selectedProject, selectedOwningEntity, selectedInstanceIdentifierType);
                    }
                } else {
                    alert(FIELD.ERROR.SELECT);
                }

                function handleCustomerIdResponse(globalCustomerId) {
                    if (UtilityService.hasContents(globalCustomerId)) {
                        navigateToSearchResultsPage(globalCustomerId, selectedServiceInstance, selectedProject, selectedOwningEntity, selectedInstanceIdentifierType);
                    } else {
                        alert(FIELD.ERROR.SERVICE_INST_DNE);
                    }
                }
            };
    }
]).directive('restrictInput', function () {

    return {

        restrict: 'A',
        require: 'ngModel',
        link: function ($scope, element, attr, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {

                var types = $scope.$eval(attr.restrictInput);
                if (!types.regex && types.type) {

                    switch (types.type) {
                        case 'Service Instance Name' :
                            types.regex = '^[a-zA-Z0-9-_]*$';
                            break;
                        default:
                            types.regex = '';
                    }
                }
                var reg = new RegExp(types.regex);
                if (reg.test(viewValue)) {
                    return viewValue;
                } else {
                    var overrideValue = (reg.test(viewValue) ? viewValue : '');
                    element.val(overrideValue);
                    return overrideValue;
                }
            });
        }
    };

});
appDS2.controller('TreeCtrl', ['$scope', function ($scope) {
    $scope.remove = function (scope) {
        scope.remove();
    };

    $scope.toggle = function (scope) {
        scope.toggle();
    };

    $scope.moveLastToTheBeginning = function () {
        var a = $scope.data.pop();
        $scope.data.splice(0, 0, a);
    };

    $scope.newSubItem = function (scope) {
        var nodeData = scope.$modelValue;
        nodeData.nodes.push({
            id: nodeData.id * 10 + nodeData.nodes.length,
            title: nodeData.title + '.' + (nodeData.nodes.length + 1),
            nodes: []
        });
    };

    $scope.collapseAll = function () {
        $scope.$broadcast(FIELD.ID.ANGULAR_UI_TREE_COLLAPSEALL);
    };

    $scope.expandAll = function () {
        $scope.$broadcast(FIELD.ID.ANGULAR_UI_TREE_EXPANDALL);
    };

}]);



