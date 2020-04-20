/*-
* ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ============LICENSE_END=========================================================
*/

/**
 * The Instantiation (or View/Edit) Controller controls the instantiation/removal of
 * deployable objects (Services, VNFs, VF-Modules, Networks, and Volume-Groups)
 */

"use strict";

appDS2.controller(
    "ServiceProxyConfigController", ["COMPONENT", "$log", "FIELD", "PARAMETER", "DataService",
    "CreationService", "$scope", "$window", "$location", "AaiService", "$uibModal", "UtilityService", "$timeout",
    "featureFlags",
    function (COMPONENT, $log, FIELD, PARAMETER, DataService,
              CreationService, $scope, $window, $location, AaiService, $uibModal, UtilityService, $timeout,
              featureFlags
    ) {

        $scope.selectedMetadata = {};

        $scope.serviceMetadataFields = [];
        $scope.nodeTemplateFields = {};

        $scope.configurationByPolicy = DataService.getConfigurationByPolicy();

        $scope.collectorType = $scope.configurationByPolicy ? 'pnf' : 'vnf'; //default
        $scope.collectorInstance;
        $scope.collectorInstanceName = "";
        $scope.collectorInstanceList = null;
        $scope.collectorMetadata = [];
        $scope.collectorNoResults = false;

        $scope.sourceInstance;
        $scope.sourceInstanceName = "";
        $scope.sourceInstanceList = null;
        $scope.sourceMetadata = [];
        $scope.sourceNoResults = false;

        $scope.errorMsg = FIELD.ERROR.INSTANCE_NAME_VALIDATE;

        $scope.modelName = DataService.getModelInfo(COMPONENT.VNF).modelCustomizationName;

        $scope.serviceTypes = [];

        $scope.isSourceSubscriberEnabled = function() {
            return featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_2006_PORT_MIRRORING_LET_SELECTING_SOURCE_SUBSCRIBER);
        };

        function init() {
            if ($scope.isSourceSubscriberEnabled())  {
                loadSourceSubscriber();
            }
            generateMetadata(sourceServiceProxy);
            generateMetadata(collectorServiceProxy);

        }

        function loadSourceSubscriber() {
            AaiService.getSubList(function (response) {
                $scope.sourceSubscribers = response;
                $scope.sourceSubscriber = response.find((subscriber)=>{
                    return subscriber.globalCustomerId === DataService.getGlobalCustomerId();
                });
                $scope.loadServiceTypes($scope.sourceSubscriber);
            }, function (response) { // failure
                $scope.sourceSubscribers = [];
            });
        }

        function setDefaultCollectorServiceType() {
            const configServiceType = DataService.getServiceType();
            $scope.collectorServiceType = mustFind($scope.serviceTypes, {"service-type": configServiceType});
            loadCollectorProxies();
        }

        function handleGetServiceTypesResponse(response) {
            $scope.serviceTypes = response.data;
            setDefaultCollectorServiceType();
        }

        var handleGetParametersResponse = function(parameters) {
            $scope.serviceMetadataFields = parameters.summaryList;
            $scope.nodeTemplateFields =  DataService.getPortMirroningConfigFields();
        };

        var mustFind = function (collection, predicate) {
            const result = _.find(collection, predicate);
            const description = "result for find " + JSON.stringify(predicate);
            UtilityService.checkUndefined(description, result);
            $log.debug(description, result);
            return result;
        };


        $scope.back = function()  {
            $window.history.back();
        };


        $scope.loadServiceTypes = function(selectedSubscriber) {
            $scope.sourceSubscriber = selectedSubscriber;
            AaiService.getSubscriberServiceTypes(selectedSubscriber['globalCustomerId'])
                .then(handleGetServiceTypesResponse)
                .catch(function (error) {
                    $log.error(error);
                });
        }

        var modalInstance;

        $scope.create = function()  {
            $scope.disableCreate= true;
            var portMirroringConfigFields = DataService.getPortMirroningConfigFields();
            portMirroringConfigFields.sourceInstance = mustFind($scope.sourceInstanceList, {'id': $scope.sourceInstance});
            portMirroringConfigFields.destinationInstance = mustFind($scope.collectorInstanceList, {'id': $scope.collectorInstance});

            var selectedVnfsList;

            if ($scope.configurationByPolicy) {
                selectedVnfsList = [
                    portMirroringConfigFields.sourceInstance.properties
                ];
            } else {
                selectedVnfsList = [
                    portMirroringConfigFields.sourceInstance.properties,
                    portMirroringConfigFields.destinationInstance.properties
                ];
            }

            AaiService.getVnfVersionsByInvariantId(
                selectedVnfsList.map(function(x) {
                    return UtilityService.checkUndefined("model-invariant-id", x['model-invariant-id']);
                })
                )
                .then(function (response) {
                    $log.debug("getVnfVersionsByInvariantId: response", response);

                    selectedVnfsList
                        .map(function (inOutProperties) {
                            const model = mustFind(response.data.model, {'model-invariant-id': inOutProperties['model-invariant-id']});

                            const modelVer = mustFind(model["model-vers"]["model-ver"], {'model-version-id': inOutProperties['model-version-id']});

                            inOutProperties['model-version'] = modelVer['model-version'];
                            inOutProperties['model-name'] = modelVer['model-name'];
                            UtilityService.checkUndefined("model-version", modelVer);
                        });
                })

                .then(function () {
                    var requestParams = {
                        configurationModelInfo: DataService.getModelInfo(COMPONENT.VNF),
                        relatedTopModelsInfo: DataService.getModelInfo(COMPONENT.SERVICE),
                        portMirroringConfigFields:portMirroringConfigFields,
                        attuuid: DataService.getLoggedInUserId(),
                        topServiceInstanceId: DataService.getServiceInstanceId(),
                        configurationByPolicy: $scope.configurationByPolicy,
                        callbackFunction: updateViewCallbackFunction
                    };

                    modalInstance = $uibModal.open({
                        templateUrl: 'app/vid/scripts/modals/mso-commit/mso-commit.html',
                        controller : "msoCommitModalController",
                        backdrop: true,
                        resolve: {
                            msoType: function () {
                                return COMPONENT.MSO_CREATE_CONFIGURATION_REQ;
                            },
                            requestParams: function () {
                                return requestParams;
                            },
                            configuration: function () {
                                return null;
                            }
                        }
                    });
                })
                .catch(function (error) {
                    $log.error("error while configuration create", error);
                    $scope.disableCreate= false;
                });
        };

        $scope.openMetadataModal = function(name) {
            $scope.selectedMetadata = $scope[name];
            modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/service-metadata/service-metadata.html',
                backdrop: false,
                scope : $scope,
                resolve: {
                }
            });
        };

        $scope.cancel = function()  {
            modalInstance.dismiss('cancel');
        };

        var updateViewCallbackFunction = function(response) {
            $scope.callbackResults = "";
            var color = FIELD.ID.COLOR_NONE;
            $scope.callbackStyle = {
                "background-color" : color
            };

            /*
             * This 1/2 delay was only added to visually highlight the status
             * change. Probably not needed in the real application code.
             */
            $timeout(function() {
                $scope.callbackResults = UtilityService.getCurrentTime()
                    + FIELD.STATUS.IS_SUCCESSFUL + response.isSuccessful;
                if (response.isSuccessful) {
                    color = FIELD.ID.COLOR_8F8;
                    $window.history.go(-2);
                } else {
                    $scope.disableCreate=false;
                    color = FIELD.ID.COLOR_F88;
                }
                $scope.callbackStyle = {
                    "background-color" : color
                };
            }, 500);
        };

        CreationService.initializeComponent(COMPONENT.VNF);
        CreationService.initializeComponent(COMPONENT.SERVICE);
        CreationService.getParameters(handleGetParametersResponse);

        var sourceServiceProxies = DataService.getSourceServiceProxies();
        var collectorServiceProxies = DataService.getCollectorServiceProxies();
        var serviceProxiesList = DataService.getServiceProxies();

        var sourceServiceProxy = {
            serviceList: sourceServiceProxies,
            instanceListScopePropertyName: "sourceInstanceList",
            name: "sourceInstanceName",
            metadata: "sourceMetadata",
            noResults: "sourceNoResults"
        };

        var collectorServiceProxy = {
            serviceList: collectorServiceProxies,
            instanceListScopePropertyName: "collectorInstanceList",
            name: "collectorInstanceName",
            metadata: "collectorMetadata",
            noResults: "collectorNoResults"
        };

        $scope.onSourceServiceTypeSelected = function() {
            clearSourceProxySelection();
            loadSourceProxies();
        };

        $scope.onCollectorServiceTypeSelected = function() {
            clearCollectorProxySelection();
            loadCollectorProxies();
        };

        $scope.shouldLetSelectingCollectorType = function() {
            return $scope.collectorType === 'vnf' || featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_1810_CR_LET_SELECTING_COLLECTOR_TYPE_UNCONDITIONALLY);
        };

        function clearSourceProxySelection() {
            $scope.sourceInstance = undefined;
        }

        function clearCollectorProxySelection() {
            $scope.collectorInstance = undefined;
        }

        function loadSourceProxies() {
            var serviceProxy = serviceProxiesList[(sourceServiceProxy.serviceList)[0]];
            var selectedServiceType = $scope.sourceServiceType['service-type'];
            var selectedSubscriberId = $scope.sourceSubscriber['globalCustomerId'] || DataService.getGlobalCustomerId();
            loadProxyInstances(sourceServiceProxy, selectedServiceType, selectedSubscriberId, serviceProxy);
        }

        function loadCollectorProxies() {
            var serviceProxy = serviceProxiesList[(collectorServiceProxy.serviceList)[0]];
            var selectedServiceType = $scope.collectorServiceType['service-type'];
            loadProxyInstances(collectorServiceProxy, selectedServiceType, DataService.getGlobalCustomerId() , serviceProxy);
        }

        function loadProxyInstances(service, serviceType, selectedSubscriberId, serviceProxy) {
            $scope[service.instanceListScopePropertyName] = null;
            var configNodeTemplateFields = DataService.getPortMirroningConfigFields();
            if (service.name === 'collectorInstanceName' && $scope.configurationByPolicy) {
                var configurationModel = DataService.getModelInfo(COMPONENT.VNF);
                AaiService.getPnfInstancesList(
                    DataService.getGlobalCustomerId(),
                    serviceType,
                    serviceProxy.sourceModelUuid,
                    serviceProxy.sourceModelInvariant,
                    configNodeTemplateFields.cloudRegionId,
                    configurationModel.properties.equip_vendor,
                    configurationModel.properties.equip_model
                )
                    .then(function (response) {
                        var results = response.results || [];
                        $scope[service.instanceListScopePropertyName] = results;
                        $scope[service.noResults] = (results.length === 0);
                    })
                    .catch(function (error) {
                        $scope[service.noResults] = true;
                        $log.error('No pnf instance found for ' + service.name, error);
                    });
            } else {
                AaiService.getVnfInstancesList(
                    selectedSubscriberId,
                    serviceType,
                    serviceProxy.sourceModelUuid,
                    serviceProxy.sourceModelInvariant,
                    configNodeTemplateFields.cloudRegionId
                )
                    .then(function (response) {
                        var results = response.results || [];
                        $scope[service.instanceListScopePropertyName] = results;
                        $scope[service.noResults] = (results.length === 0);
                    })
                    .catch(function (error) {
                        $scope[service.noResults] = true;
                        $log.error("No vnf instance found for " + service.name, error);
                    });
            }
        }

        function generateMetadata(service) {
            const serviceProxy = serviceProxiesList[(service.serviceList)[0]];
            $scope[service.name] = serviceProxy.name;

            $scope[service.metadata] = [
                {"name" :"Name" ,"value" : serviceProxy.name},
                {"name" :"Version",value : serviceProxy.version},
                {"name" :"Description", value : serviceProxy.description},
                {"name" :"Type", value : serviceProxy.type},
                {"name" :"Invariant UUID", value : serviceProxy.invariantUuid},
                {"name" :"UUID", value : serviceProxy.uuid},
                {"name" :"Customization UUID", value : serviceProxy.customizationUuid},
                {"name" :"Source Model Uuid", value : serviceProxy.sourceModelUuid},
                {"name" :"Source Model Invariant", value : serviceProxy.sourceModelInvariant},
                {"name" :"Source Model Name", value : serviceProxy.sourceModelName}
            ];
        }

        init();
        $scope.$on('$routeChangeStart', function (event, next, current) {
            if(next.$$route.originalPath!=="/addNetworkNode"){
                DataService.setPortMirroningConfigFields(null);
            }
        });
    }]);


