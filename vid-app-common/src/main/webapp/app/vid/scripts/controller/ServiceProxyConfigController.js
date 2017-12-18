/*-
* ============LICENSE_START=======================================================
* VID
* ================================================================================
* Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

/**
 * The Instantiation (or View/Edit) Controller controls the instantiation/removal of
 * deployable objects (Services, VNFs, VF-Modules, Networks, and Volume-Groups)
 */

"use strict";

appDS2.controller("ServiceProxyConfigController", ["COMPONENT", "$log", "FIELD", "PARAMETER", "DataService", "CreationService", "$scope", "$window", "$location", "AaiService", "$uibModal", "UtilityService",
    function (COMPONENT, $log, FIELD, PARAMETER, DataService, CreationService, $scope, $window, $location, AaiService, $uibModal, UtilityService) {

        $scope.selectedMetadata = {};

        $scope.serviceMetadataFields = [];
        $scope.nodeTemplateFields = {};

        $scope.collectorInstance;
        $scope.collectorInstanceName = "";
        $scope.collectorInstanceList = [];
        $scope.collectorMetadata = [];

        $scope.sourceInstance;
        $scope.sourceInstanceName = "";
        $scope.sourceInstanceList = [];
        $scope.sourceMetadata = [];

        $scope.errorMsg = FIELD.ERROR.INSTANCE_NAME_VALIDATE;

        $scope.modelName = DataService.getModelInfo(COMPONENT.VNF).modelCustomizationName;

        var handleGetParametersResponse = function(parameters) {
            $scope.serviceMetadataFields = parameters.summaryList;
            $scope.nodeTemplateFields = _.keyBy(parameters.userProvidedList, 'id');
        };

        var mustFind = function (collection, predicate) {
            const result = _.find(collection, predicate);
            const description = "result for find " + JSON.stringify(predicate);
            UtilityService.checkUndefined(description, result);
            $log.debug(description, result);
            return result;
        };

            // CreationService.initializeComponent(COMPONENT.CONFIGURATION);
        CreationService.initializeComponent(COMPONENT.VNF);
        CreationService.initializeComponent(COMPONENT.SERVICE);

        CreationService.getParameters(handleGetParametersResponse);

        $scope.back = function()  {
            $window.history.back();
        };

        var modalInstance;

        $scope.create = function()  {
            var portMirroringConfigFields = DataService.getPortMirroningConfigFields();
            portMirroringConfigFields.sourceInstance =  mustFind($scope.sourceInstanceList.results, {'id': $scope.sourceInstance});
            portMirroringConfigFields.destinationInstance = mustFind($scope.collectorInstanceList.results, {'id': $scope.collectorInstance});

            AaiService.getVnfVersionsByInvariantId(
                [
                    UtilityService.checkUndefined("sourceInstance model-invariant-id", portMirroringConfigFields.sourceInstance.properties['model-invariant-id']),
                    UtilityService.checkUndefined("destinationInstance model-invariant-id", portMirroringConfigFields.destinationInstance.properties['model-invariant-id'])
                ]
            )
                .then(function (response) {
                    $log.debug("getVnfVersionsByInvariantId: response", response);

                    [ portMirroringConfigFields.sourceInstance.properties,
                        portMirroringConfigFields.destinationInstance.properties ]
                        .map(function (inOutProperties) {
                            const model = mustFind(response.data.model, {'model-invariant-id': inOutProperties['model-invariant-id']});

                            const modelVer = mustFind(model["model-vers"]["model-ver"], {'model-version-id': inOutProperties['model-version-id']});

                            inOutProperties['model-version'] = modelVer['model-version'];
                            UtilityService.checkUndefined("model-version", modelVer);
                        });
                })

                .then(function () {
                    modalInstance = $uibModal.open({
                        templateUrl: 'app/vid/scripts/modals/mso-commit/mso-commit.html',
                        controller : "msoCommitModalController",
                        backdrop: false,
                        resolve: {
                            msoType: function () {
                                return COMPONENT.MSO_CREATE_REQ;
                            },
                            componentId: function () {
                                return COMPONENT.VNF;
                            }}
                    });
                })
                .catch(function (error) {
                    $log.error("error while configuration create", error);
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

        var sourceServiceProxies = DataService.getSourceServiceProxies();
        var collectorServiceProxies = DataService.getCollectorServiceProxies();
        var serviceProxiesList = DataService.getServiceProxies();

        var serviceProxiesObj = [{serviceList : sourceServiceProxies, scopePropertyName : "sourceInstanceList", name : "sourceInstanceName", metadata: "sourceMetadata"},
            {serviceList : collectorServiceProxies, scopePropertyName : "collectorInstanceList", name: "collectorInstanceName", metadata: "collectorMetadata"}];

        //Loop over source and collector service proxies ang get the list of instances from A&AI
        angular.forEach(serviceProxiesObj, function(service, index) {
            //TODO : Change the loop to support more than 1 item
            for(var i = 0; i < 1 ; i++)  {
                var modelVersionId = serviceProxiesList[(service.serviceList)[i]].version;
                var modelInvariantId  = serviceProxiesList[(service.serviceList)[i]].invariantUuid;
                $scope[service.name] = serviceProxiesList[(service.serviceList)[i]].name;

                $scope[service.metadata] = [
                    {"name" :"Name" ,"value" : serviceProxiesList[(service.serviceList)[i]].name},
                    {"name" :"Version",value : modelVersionId},
                    {"name" :"Description", value : serviceProxiesList[(service.serviceList)[i]].description},
                    {"name" :"Type", value : serviceProxiesList[(service.serviceList)[i]].type},
                    {"name" :"Invariant UUID", value : modelInvariantId},
                    {"name" :"UUID", value : serviceProxiesList[(service.serviceList)[i]].uuid},
                    {"name" :"Customization UUID", value : serviceProxiesList[(service.serviceList)[i]].customizationUuid},
                    {"name" :"Source Model Uuid", value : serviceProxiesList[(service.serviceList)[i]].sourceModelUuid},
                    {"name" :"Source Model Invariant", value : serviceProxiesList[(service.serviceList)[i]].sourceModelInvariant},
                    {"name" :"Source Model Name", value : serviceProxiesList[(service.serviceList)[i]].sourceModelName}
                ];

                var configNodeTemplateFields = DataService.getPortMirroningConfigFields();
                AaiService.getServiceProxyInstanceList(
                    DataService.getGlobalCustomerId(),
                    DataService.getServiceType(),
                    modelVersionId,
                    modelInvariantId,
                    configNodeTemplateFields.lcpRegion.value
                )
                .then(function (response) {
                    $scope[service.scopePropertyName] = response;
                })
                .catch(function (error) {

                });
            }
        });
    }]);


