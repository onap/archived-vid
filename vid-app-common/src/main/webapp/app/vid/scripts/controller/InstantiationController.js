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

/**
 * The Instantiation (or View/Edit) Controller controls the instantiation/removal of
 * deployable objects (Services, VNFs, VF-Modules, Networks, and Volume-Groups)
 */
(function () {
    "use strict";

    appDS2.requires.push('ui.tree');

    appDS2.controller("InstantiationController", function ($scope, $route, $location, $timeout, $uibModal, COMPONENT, VIDCONFIGURATION, FIELD, DataService, PropertyService, UtilityService, VnfService, $http, vidService, AaiService, PnfService, CrService, AsdcService, $q, featureFlags, _, CreationService, $window, DeleteResumeService) {
        $scope.popup = new Object();
        $scope.defaultBaseUrl = "";
        $scope.responseTimeoutMsec = 60000;
        $scope.properties = UtilityService.getProperties();
        $scope.resumeStatus = COMPONENT.RESUME_STATUS;
        //isPermitted - returned as string from url and converted into boolean
        $scope.isPermitted = $location.search().isPermitted === "true";
        $scope.STATUS_CONSTANTS = FIELD.STATUS;
        $scope.pnfs = [];// PNF data init;
        $scope.collectionResource = {};
        $scope.collections = [];
        $scope.networks = [];
        $scope.isCR = false;
        $scope.isVFC = false;
        $scope.init = function () {
            /*
             * These 2 statements should be included in non-test code.
             */
            // takes a default value, retrieves the prop value from the file system and sets it
            var msecs = PropertyService.retrieveMsoMaxPollingIntervalMsec();
            PropertyService.setMsoMaxPollingIntervalMsec(msecs);

            // takes a default value, retrieves the prop value from the file system and sets it
            var polls = PropertyService.retrieveMsoMaxPolls();
            PropertyService.setMsoMaxPolls(polls);
        };


        $scope.convertModel = function (asdcModel) {
            if (!asdcModel) return undefined;
            var convertedAsdcModel = UtilityService.convertModel(asdcModel);
            return convertedAsdcModel;
        };

        $scope.prepareScopeWithModel = function () {
            // skip if no model
            if (!vidService.getModel()) return;

            $scope.service = {
                "model": vidService.getModel(),
                "convertedModel": $scope.convertModel(vidService.getModel()),
                "instance": vidService.getInstance()
            };


            $scope.isCR = !_.isEmpty($scope.service.model.collectionResources);
            if ($scope.isCR)
                prepareCr();
            else
                preparePnfs();
        };

        function preparePnfs() {
            var serviceInstance = {
                globalCustomerId: $location.search().subscriberId,
                serviceType: $location.search().serviceType,
                serviceInstanceId: $location.search().serviceInstanceId
            };

            _setPnf(serviceInstance).then(function (data) {
                $scope.pnfs = data;
            });
        }


        function prepareCr() {
            var serviceInstance = {
                globalCustomerId: $location.search().subscriberId,
                serviceType: $location.search().serviceType,
                serviceInstanceId: $location.search().serviceInstanceId
            };

            _setCr(serviceInstance).then(function (data) {
                $scope.collectionResource = data;
                $scope.collections.push($scope.collectionResource.collection);
                $scope.networks.push($scope.collectionResource.networks[0]);
            });
        }

        $scope.returnVfModules = function (vnfInstance) {

            var svcModel = $scope.service.convertedModel;
            //var vnfModelInvariantUuid = vnfInstance[FIELD.ID.MODEL_INVAR_ID];
            var vnfModelVersionId = vnfInstance[FIELD.ID.MODEL_VERSION_ID]; // model uuid
            var vnfModelCustomizationUuid = vnfInstance[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            var vnfModel = null;

            if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.vnfs)))) {
                if ((svcModel.isNewFlow) && (vnfModelCustomizationUuid != null)) {
                    vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];
                    if (vnfModel !== undefined) {
                        if (!($scope.isObjectEmpty(vnfModel.vfModules))) {
                            //console.log ("vnfModel.vfModules: "); console.log (JSON.stringify(vnfModel.vfModules, null, 4));
                            return (vnfModel.vfModules);
                        }
                    }
                }
                else {
                    // old flow
                    if (vnfModelVersionId != null) {
                        vnfModel = svcModel.vnfs[vnfModelVersionId];
                        if (vnfModel !== undefined) {
                            if (!($scope.isObjectEmpty(vnfModel.vfModules))) {
                                //console.log ("vnfModel.vfModules: "); console.log (JSON.stringify(vnfModel.vfModules, null, 4));
                                return (vnfModel.vfModules);
                            }
                        }
                    }
                }

            }
            return null;
        };
        $scope.hasVfModules = function (vnfInstance) {
            if ($scope.returnVfModules(vnfInstance) != null) {
                return true;
            }
            return false;
        };
        $scope.returnVolumeGroups = function (vnfInstance) {

            var svcModel = $scope.service.convertedModel;

            //var vnfModelInvariantUuid = vnfInstance[FIELD.ID.MODEL_INVAR_ID];
            var vnfModelVersionId = vnfInstance[FIELD.ID.MODEL_VERSION_ID];
            var vnfModelCustomizationUuid = vnfInstance[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            var vnfModel = null;

            if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.vnfs)))) {
                if ((svcModel.isNewFlow) && (vnfModelCustomizationUuid != null)) {
                    vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];
                    if (vnfModel !== undefined && !($scope.isObjectEmpty(vnfModel.volumeGroups))) {
                            //console.log ("vnfModel.volumeGroups: "); console.log (JSON.stringify(vnfModel.volumeGroups, null, 4));
                            return (vnfModel.volumeGroups);
                        }
                }
                else {
                    // old flow
                    if (vnfModelVersionId != null) {
                        vnfModel = svcModel.vnfs[vnfModelVersionId];
                        if (!($scope.isObjectEmpty(vnfModel.volumeGroups))) {
                            //console.log ("vnfModel.vfModules: "); console.log (JSON.stringify(vnfModel.volumeGroups, null, 4));
                            return (vnfModel.volumeGroups);
                        }
                    }
                }

            }
            return null;
        };
        $scope.hasVolumeGroups = function (vnfInstance) {
            if ($scope.returnVolumeGroups(vnfInstance) != null) {
                return true;
            }
            return false;
        };
        $scope.deleteNetwork = function (serviceObject, network) {

            console.log("Removing Network " + network.name);

            //Send delete network request to MSO

            //var networks = this.service.instance.networks;

            //networks.splice(networks.indexOf(network), 1);

            //Retrieve updated data from A&AI
            var serviceInstance = serviceObject.object;

            setCurrentNetworkModelInfoFromScope(network);

            DataService.setInventoryItem(network.object);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            DataService.setGlobalCustomerId(serviceObject[FIELD.ID.GLOBAL_CUST_ID]);
            DataService.setServiceInstanceName($scope.service.instance.name);
            DataService.setServiceName($scope.service.model.service.name);
            DataService.setServiceUuid($scope.service.model.service.uuid);
            DataService.setNetworkInstanceId(network.object[FIELD.ID.NETWORK_ID]);

            $scope.$broadcast(COMPONENT.DELETE_RESUME_COMPONENT, {
                componentId: COMPONENT.NETWORK,
                callbackFunction: deleteOrResumeCallback,
                dialogMethod: COMPONENT.DELETE
            });
        };

        $scope.allowTransferToNewScreenAndShowButton = function (isPermitted){
          if(featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_FLASH_MORE_ACTIONS_BUTTON_IN_OLD_VIEW_EDIT)) {
              return isPermitted;
          }
          return false;
        };

        $scope.navigateToNewEditViewScreen = function(){
                window.location.href = 'serviceModels.htm#/servicePlanning/EDIT?' +
                    'serviceModelId=' +     _.get($scope, 'service.model.service.uuid') +
                    '&subscriberId=' +      $location.search().subscriberId  +
                    '&serviceType=' +       $location.search().serviceType      +
                    '&serviceInstanceId=' + $location.search().serviceInstanceId;
        };

        $scope.deleteService = function (serviceObject, serviceOrchestrationStatus) {

            var serviceInstance = serviceObject.object;

            console.log("Removing Service " + $scope.service.instance.name);

            if ($scope.isMacro()) {
                DataService.setALaCarte(false);
            }
            else {
                DataService.setALaCarte(true);
            }
            DataService.setMacro($scope.isMacro());
            DataService.setInventoryItem(serviceInstance);
            setCurrentServiceModelInfoFromScope();


            DataService.setSubscriberName(serviceObject[FIELD.ID.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            DataService.setGlobalCustomerId(serviceObject[COMPONENT.GLOBAL_CUSTOMER_ID]);
            DataService.setServiceInstanceName($scope.service.instance.name);

            DataService.setServiceName($scope.service.model.service.name);

            DataService.setServiceUuid($scope.service.model.service.uuid);

            $scope.$broadcast(COMPONENT.DELETE_RESUME_COMPONENT, {
                componentId: COMPONENT.SERVICE,
                callbackFunction: deleteServiceInstanceCallbackFunction,
                dialogMethod: COMPONENT.DELETE,
                serviceStatus: serviceOrchestrationStatus
            });

        };

        function populate_popup_vfModule(serviceObject, vfModule, vnf) {
            var serviceInstance = serviceObject.object;

            DataService.setInventoryItem(vfModule.object);

            setCurrentVfModuleModelInfoFromScope(vnf, vfModule);
            var vfModuleInstanceID = vfModule.object[FIELD.ID.VF_MODULE_ID];
            if (vfModuleInstanceID == null) {
                vfModuleInstanceID = "";
            }

            DataService.setVnfInstanceId(vnf.object[FIELD.ID.VNF_ID]);
            DataService.setVfModuleInstanceId(vfModuleInstanceID);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            DataService.setGlobalCustomerId(serviceObject[FIELD.ID.GLOBAL_CUST_ID]);
            DataService.setServiceInstanceName($scope.service.instance.name);

            DataService.setServiceName($scope.service.model.service.name);

            DataService.setServiceUuid($scope.service.model.service.uuid);
        }

        var modalInstance;

        var openMsoModal = function (msoType, requestParams, callbackFunction, configuration) {
             modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/mso-commit/mso-commit.html',
                controller: "msoCommitModalController",
                backdrop: false,
                resolve: {
                    msoType: function () {
                        return msoType;
                    },
                    requestParams: function () {
                        requestParams.callbackFunction = callbackFunction;
                        return requestParams;
                    },
                    configuration: function () {
                        return configuration;
                    }
                }
            });
        };

        var openVfModuleWithHomingDataModal = function(action, vfModule)  {
            modalInstance = $uibModal.open({
                controller: 'vfModuleActionModalController',
                templateUrl: 'app/vid/scripts/modals/vf-module-homing-data-action/vf-module-homing-data-action.html',
                backdrop: false,
                resolve: {
                    action: function () {
                        return action;
                    },
                    vfModule: function() {
                        return vfModule;
                    }
                }
            });

            modalInstance.result.then(function (data) {
                if (data.msoType && data.requestParams) {
                    openMsoModal(data.msoType, data.requestParams, deleteOrResumeCallback, null);
                }
            });
        };

        function getLcpCloudRegionTenantList() {
            AaiService.getLcpCloudRegionTenantList(DataService
                .getGlobalCustomerId(), DataService.getServiceType(), function(
                response) {
                $scope.lcpAndTenant = response;
                $scope.isFeatureFlagCloudOwner = featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST);
                $scope.lcpRegionList = _.uniqBy(response, 'cloudRegionId');
            });
        }

        $scope.deleteVfModule = function (serviceObject, vfModule, vnf) {
            $scope.isSoftDeleteEnabled = true;

            populate_popup_vfModule(serviceObject, vfModule, vnf);

            if (DataService.getLoggedInUserId())  {
                openVfModuleWithHomingDataModal(COMPONENT.DELETE, vfModule);
            }
            else {
                AaiService.getLoggedInUserID(function (response) {
                    var userID = response.data;
                    DataService.setLoggedInUserId(userID);
                    openVfModuleWithHomingDataModal(COMPONENT.DELETE, vfModule);
                });
            }
            console.log("Removing VF-Module", vfModule);
        };

        function setCurrentServiceModelInfoFromScope() {
            DataService.setModelInfo(COMPONENT.SERVICE, {
                "modelInvariantId": $scope.service.model.service.invariantUuid,
                "modelVersion": $scope.service.model.service.version,
                "modelNameVersionId": $scope.service.model.service.uuid,
                "modelName": $scope.service.model.service.name,
                "inputs": ""
            });
        };

        function setCurrentVNFModelInfo(vnf) {
            var svcModel = $scope.service.convertedModel;
            var vnfModel;
            var vnfModelCustomizationUuid = vnf.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];
            var vnfModelVersionId = vnf.object[FIELD.ID.MODEL_VERSION_ID];
            if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.vnfs)))) {
                if ((svcModel.isNewFlow) && (vnfModelCustomizationUuid != null)) {
                    vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];
                }
                else {
                    vnfModel = svcModel.vnfs[vnfModelVersionId];
                }
                if (!($scope.isObjectEmpty(vnfModel))) {

                    DataService.setModelInfo(COMPONENT.VNF, {
                        "modelInvariantId": vnfModel.invariantUuid,
                        "modelVersion": vnfModel.version,
                        "modelNameVersionId": vnfModel.uuid,
                        "modelCustomizationName": vnfModel.modelCustomizationName,
                        "customizationUuid": vnfModel.customizationUuid,
                        "modelName": vnfModel.name,
                        "inputs": ""
                    });
                }
            }


        }

        function setCurrentNetworkModelInfoFromScope(network) {
            var svcModel = $scope.service.convertedModel;
            var netModel;
            // set model default and override later if found
            DataService.setModelInfo(COMPONENT.NETWORK, {});

            if (network.object != null) {

                //var netModelInvariantUuid = network.object[FIELD.ID.MODEL_INVAR_ID];
                var netModelVersionId = network.object[FIELD.ID.MODEL_VERSION_ID]; // model uuid
                var netModelCustomizationUuid = network.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];

                if (UtilityService.hasContents(netModelCustomizationUuid)) {
                    // set it to what came from a&ai
                    DataService.setResCustomizationUuid(netModelCustomizationUuid);
                }

                if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.networks)))) {
                    if ((svcModel.isNewFlow) && (UtilityService.hasContents(netModelCustomizationUuid))) {
                        netModel = svcModel.networks[netModelCustomizationUuid];
                    }
                    else {

                        if (UtilityService.hasContents(netModelVersionId)) {
                            netModel = svcModel.networks[netModelVersionId];
                        }

                    }
                }
            }
            if (!($scope.isObjectEmpty(netModel))) {
                DataService.setModelInfo(COMPONENT.NETWORK, {
                    "modelInvariantId": netModel.invariantUuid,
                    "modelVersion": netModel.version,
                    "modelNameVersionId": netModel.uuid,
                    "modelCustomizationName": netModel.modelCustomizationName,
                    "customizationUuid": netModel.customizationUuid,
                    "modelName": netModel.name,
                    "inputs": ""
                });
            }
        }

        function setCurrentVfModuleModelInfoFromScope(vnf, vfModule) {

            var svcModel = $scope.service.convertedModel;

            //var vnfModelInvariantUuid = vnf.object[FIELD.ID.MODEL_INVAR_ID];
            var vnfModelVersionId = vnf.object[FIELD.ID.MODEL_VERSION_ID];
            var vnfModelCustomizationUuid = vnf.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];
            var vnfModel = null;
            var vfModuleModel = null;

            DataService.setModelInfo(COMPONENT.VF_MODULE, {
                "modelInvariantId": "",
                "modelVersion": "",
                "modelNameVersionId": "",
                "modelCustomizationName": "",
                "customizationUuid": "",
                "modelName": "",
                "inputs": ""
            });

            if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.vnfs)))) {
                if ((svcModel.isNewFlow) && (vnfModelCustomizationUuid != null)) {
                    vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];

                    var vfModuleCustomizationUuid = vfModule.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];
                    if (!($scope.isObjectEmpty(vnfModel)) && !($scope.isObjectEmpty(vnfModel.vfModules)) && UtilityService.hasContents(vfModuleCustomizationUuid)) {

                        vfModuleModel = vnfModel.vfModules[vfModuleCustomizationUuid];

                    }
                }
                else {
                    // old flow
                    if (vnfModelVersionId != null) {
                        vnfModel = svcModel.vnfs[vnfModelVersionId];
                    }
                    //var vfModuleInvariantUuid = vfModule.object[FIELD.ID.MODEL_INVAR_ID];
                    var vfModuleModelVersionId = vfModule.object[FIELD.ID.MODEL_VERSION_ID];
                    if ((!($scope.isObjectEmpty(vnfModel))) && (!($scope.isObjectEmpty(vnfModel.vfModules))) &&
                        UtilityService.hasContents(vfModuleModelVersionId)) {
                        vfModuleModel = vnfModel.vfModules[vfModuleModelVersionId];
                    }
                }
                if (!($scope.isObjectEmpty(vfModuleModel))) {
                    DataService.setModelInfo(COMPONENT.VF_MODULE, {
                        "modelInvariantId": vfModuleModel.invariantUuid,
                        "modelVersion": vfModuleModel.version,
                        "modelNameVersionId": vfModuleModel.uuid,
                        "modelCustomizationName": vfModuleModel.modelCustomizationName,
                        "customizationUuid": vfModuleModel.customizationUuid,
                        "modelName": vfModuleModel.name,
                        "inputs": ""
                    });
                }
            }
        }

        function setCurrentVolumeGroupModelInfoFromScope(vnf, vfModule) {
            var svcModel = $scope.service.convertedModel;

            var vnfModelCustomizationUuid = null;
            var vnfModel = null;
            var vnfModelVersionId = null;

            vnfModelVersionId = vnf.object[FIELD.ID.MODEL_VERSION_ID];
            vnfModelCustomizationUuid = vnf.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
                "modelInvariantId": "",
                "modelVersion": "",
                "modelNameVersionId": "",
                "modelCustomizationName": "",
                "customizationUuid": "",
                "modelName": "",
                "inputs": ""
            });

            if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.vnfs)))) {
                if ((svcModel.isNewFlow) && (vnfModelCustomizationUuid != null)) {
                    vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];
                }
                else {
                    vnfModel = svcModel.vnfs[vnfModelVersionId];
                }
            }


            // volume groups don't have model-invariant-id/version in a&ai.
            // Their model-invariant-id/version is the one for the associated vfModule

            var vfModuleModelVersionId = vfModule.object[FIELD.ID.MODEL_VERSION_ID];
            var vfModuleCustomizationUuid = vfModule.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];
            var volGroupModel = null;

            if (!($scope.isObjectEmpty(vnfModel)) && !($scope.isObjectEmpty(vnfModel.volumeGroups))) {
                if ((svcModel.isNewFlow) && (UtilityService.hasContents(vfModuleCustomizationUuid))) {
                    volGroupModel = vnfModel.volumeGroups[vfModuleCustomizationUuid];
                }
                else {
                    volGroupModel = vnfModel.volumeGroups[vfModuleModelVersionId];
                }
                if (!($scope.isObjectEmpty(volGroupModel))) {
                    DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
                        "modelInvariantId": volGroupModel.invariantUuid,
                        "modelVersion": volGroupModel.version,
                        "modelNameVersionId": volGroupModel.uuid,
                        "modelCustomizationName": volGroupModel.modelCustomizationName,
                        "customizationUuid": volGroupModel.customizationUuid,
                        "modelName": volGroupModel.name,
                        "inputs": ""
                    });

                }
            }
        }

        function setCurrentVolumeGroupModelInfoByVfModuleFromScope(vnf, volumeGroup) {
            var svcModel = $scope.service.convertedModel;

            var vnfModelCustomizationUuid = null;
            var vnfModel = null;
            var vnfModelVersionId = null;

            vnfModelVersionId = vnf.object[FIELD.ID.MODEL_VERSION_ID];
            vnfModelCustomizationUuid = vnf.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
                "modelInvariantId": "",
                "modelVersion": "",
                "modelNameVersionId": "",
                "modelCustomizationName": "",
                "customizationUuid": "",
                "modelName": "",
                "inputs": ""
            });

            if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.vnfs)))) {
                if ((svcModel.isNewFlow) && (vnfModelCustomizationUuid != null)) {
                    vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];
                }
                else {
                    vnfModel = svcModel.vnfs[vnfModelVersionId];
                }
            }


            // volume groups don't have model-invariant-id/version in a&ai.
            // Their model-invariant-id/version is the one for the associated vfModule

            var vfModuleModelVersionId = volumeGroup.object[FIELD.ID.VF_MODULE_MODEL_VERSION_ID];
            var vfModuleCustomizationUuid = volumeGroup.object[FIELD.ID.VF_MODULE_MODEL_CUSTOMIZATION_ID];
            var volGroupModel = null;
            if ((!($scope.isObjectEmpty(vnfModel))) && (!($scope.isObjectEmpty(vnfModel.volumeGroups)))) {
                if ((svcModel.isNewFlow) && (UtilityService.hasContents(vfModuleCustomizationUuid))) {
                    volGroupModel = vnfModel.volumeGroups[vfModuleCustomizationUuid];
                }
                else {
                    volGroupModel = vnfModel.volumeGroups[vfModuleModelVersionId];
                }
                if (!($scope.isObjectEmpty(volGroupModel))) {
                    DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
                        "modelInvariantId": volGroupModel.invariantUuid,
                        "modelVersion": volGroupModel.version,
                        "modelNameVersionId": volGroupModel.uuid,
                        "modelCustomizationName": volGroupModel.modelCustomizationName,
                        "customizationUuid": volGroupModel.customizationUuid,
                        "modelName": volGroupModel.name,
                        "inputs": ""
                    });

                }
            }
        }

        $scope.deleteVnf = function (serviceObject, vnf) {
            console.log("Removing VNF " + vnf.name);

            var serviceInstance = serviceObject.object;
            var svcModel = $scope.service.convertedModel;
            DataService.setInventoryItem(vnf.object);

            /*var vnftype = vnf.object['vnf-type'];
            if (vnftype == null)
                vnftype = "";
            else
            {
                var n = vnftype.search("/");
                if (n >= 0)
                    vnftype = vnftype.substring(n+1);
            }*/

            var svcModel = $scope.service.convertedModel;
            var vnfModelInvariantUuid = null;
            var vnfModelVersion = null;
            var vnfModelCustomizationUuid = null;
            var vnfModel = null;
            var vnfModelVersionId = null;

            vnfModelInvariantUuid = vnf.object[FIELD.ID.MODEL_INVAR_ID];
            vnfModelVersionId = vnf.object[FIELD.ID.MODEL_VERSION_ID];
            vnfModelCustomizationUuid = vnf.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            DataService.setModelInfo(COMPONENT.VNF, {
                "modelInvariantId": vnfModelInvariantUuid,
                "modelVersion": "",
                "modelNameVersionId": vnfModelVersionId,
                "modelCustomizationName": "",
                "customizationUuid": vnfModelCustomizationUuid,
                "modelName": "",
                "inputs": ""
            });

            if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.vnfs)))) {
                if ((svcModel.isNewFlow) && (vnfModelCustomizationUuid != null)) {
                    vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];
                }
                else {
                    vnfModel = svcModel.vnfs[vnfModelVersionId];
                }
                //console.log ( "vnf models: "); console.log ( JSON.stringify ($scope.service.convertedModel.vnfs, null, 4) );
                if (!($scope.isObjectEmpty(vnfModel))) {

                    DataService.setModelInfo(COMPONENT.VNF, {
                        "modelInvariantId": vnfModel.invariantUuid,
                        "modelVersion": vnfModel.version,
                        "modelNameVersionId": vnfModel.uuid,
                        "modelCustomizationName": vnfModel.modelCustomizationName,
                        "customizationUuid": vnfModel.customizationUuid,
                        "modelName": vnfModel.name,
                        "inputs": ""
                    });
                }
            }

            DataService.setVnfInstanceId(vnf.object[FIELD.ID.VNF_ID]);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            DataService.setGlobalCustomerId(serviceObject[FIELD.ID.GLOBAL_CUST_ID]);
            DataService.setServiceInstanceName($scope.service.instance.name);

            DataService.setServiceName($scope.service.model.service.name);

            DataService.setServiceUuid($scope.service.model.service.uuid);

            $scope.$broadcast(COMPONENT.DELETE_RESUME_COMPONENT, {
                componentId: COMPONENT.VNF,
                callbackFunction: deleteOrResumeCallback,
                dialogMethod: COMPONENT.DELETE
            });

        };

        $scope.deleteVolumeGroup = function (serviceObject, vnf, vfModule, volumeGroup) {

            console.log("Removing Volume Group " + volumeGroup.name);
            var haveModel = false;

            var serviceInstance = serviceObject.object;

            setCurrentVolumeGroupModelInfoFromScope(vnf, vfModule);

            DataService.setInventoryItem(volumeGroup.object);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            DataService.setGlobalCustomerId(serviceObject[FIELD.ID.GLOBAL_CUST_ID]);
            DataService.setServiceInstanceName($scope.service.instance.name);

            DataService.setServiceName($scope.service.model.service.name);

            DataService.setServiceUuid($scope.service.model.service.uuid);
            DataService.setVnfInstanceId(vnf.nodeId);
            DataService.setVolumeGroupInstanceId(volumeGroup.nodeId);

            $scope.$broadcast(COMPONENT.DELETE_RESUME_COMPONENT, {
                componentId: COMPONENT.VOLUME_GROUP,
                dialogMethod: COMPONENT.DELETE
            });
        };

        $scope.deleteVnfVolumeGroup = function (serviceObject, vnf, volumeGroup) {

            console.log("Removing Volume Group " + volumeGroup.name);
            var serviceInstance = serviceObject.object;

            DataService.setInventoryItem(volumeGroup.object);

            var svcModel = $scope.service.convertedModel;

            var vnfModelInvariantUuid = vnf.object[FIELD.ID.MODEL_INVAR_ID];
            var vnfModelVersionId = vnf.object[FIELD.ID.MODEL_VERSION_ID];
            var vnfModelCustomizationUuid = vnf.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            var volGroupModelInvariantUuid = volumeGroup.object[FIELD.ID.MODEL_INVAR_ID];
            var volGroupModelVersionId = volumeGroup.object[FIELD.ID.MODEL_VERSION_ID];
            var volGroupModelCustomizationUuid = volumeGroup.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            var vnfModel = null;
            var volGroupModel = null;

            // send an empty model by default since model is not required for deletes
            DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {});

            if (svcModel.isNewFlow) {
                vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];
                if (UtilityService.hasContents(volGroupModelCustomizationUuid)) {
                    volGroupModel = vnfModel.volumeGroups[volGroupModelCustomizationUuid];
                }
            }
            else {

                vnfModel = svcModel.vnfs[vnfModelVersionId];
                if (UtilityService.hasContents(volGroupModelVersionId)) {
                    volGroupModel = vnfModel.volumeGroups[volGroupModelVersionId];
                }
            }
            if (!($scope.isObjectEmpty(volGroupModel))) {
                DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
                    "modelInvariantId": volGroupModel.invariantUuid,
                    "modelVersion": volGroupModel.version,
                    "modelNameVersionId": volGroupModel.uuid,
                    "modelName": volGroupModel.name,
                    "modelCustomizationName": volGroupModel.modelCustomizationName,
                    "customizationUuid": volGroupModel.customizationUuid,
                    "inputs": ""
                });
            }

            DataService.setVnfInstanceId(vnf.object[FIELD.ID.VNF_ID]);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            DataService.setGlobalCustomerId(serviceObject[FIELD.ID.GLOBAL_CUST_ID]);
            DataService.setServiceInstanceName($scope.service.instance.name);

            DataService.setServiceName($scope.service.model.service.name);

            DataService.setServiceUuid($scope.service.model.service.uuid);
            DataService.setVnfInstanceId(vnf.nodeId);
            DataService.setVolumeGroupInstanceId(volumeGroup.nodeId);

            $scope.$broadcast(COMPONENT.DELETE_RESUME_COMPONENT, {
                componentId: COMPONENT.VOLUME_GROUP,
                callbackFunction: deleteOrResumeCallback,
                dialogMethod: COMPONENT.DELETE
            });
        };

        $scope.describeNetwork = function (serviceObject, networkObject) {
            var serviceInstance = serviceObject.object;
            var network = networkObject.object;
            //console.log ("networkObject="); console.log (JSON.stringify(networkObject, null, 4));

            DataService.setResCustomizationUuid(" ");

            setCurrentServiceModelInfoFromScope();
            setCurrentNetworkModelInfoFromScope(networkObject);

            DataService.setNetworkInstanceId(network[FIELD.ID.NETWORK_ID]);
            DataService.setInventoryItem(networkObject);
            DataService.setSubscriberName(serviceObject.subscriberName);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            $scope.$broadcast(COMPONENT.SHOW_COMPONENT_DETAILS, {
                componentId: COMPONENT.NETWORK
            });
        };

        // for service instance id - no need for this!
        $scope.describeService = function (serviceObject) {
            var serviceInstance = serviceObject.object;
            setCurrentServiceModelInfoFromScope();

            DataService.setInventoryItem(serviceInstance);
            //DataService.setModelInfo(serviceInstance['service-instance-id'], serviceInstance);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            //Display popup with additional service information
            $scope.$broadcast(COMPONENT.SHOW_COMPONENT_DETAILS, {
                componentId: COMPONENT.SERVICE
            });

        };

        $scope.describeVfModule = function (serviceObject, vfModuleObject, vnf) {
            var serviceInstance = serviceObject.object;
            var vfModule = vfModuleObject.object;

            /*var vfModuleInvariantUuid = vfModule[FIELD.ID.MODEL_INVAR_ID];
            var vfModuleModelVersionId = vfModule[FIELD.ID.MODEL_VERSION_ID];*/
            var vfModuleCustomizationUuid = vfModule[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            setCurrentServiceModelInfoFromScope();
            setCurrentVfModuleModelInfoFromScope(vnf, vfModuleObject);

            DataService.setCustomizationUuid(" ");
            if (UtilityService.hasContents(vfModuleCustomizationUuid)) {
                DataService.setCustomizationUuid(vfModuleCustomizationUuid);
            }

            //Display popup with additional VF-Module information
            DataService.setVfModuleInstanceId(vfModule[FIELD.ID.VF_MODULE_ID]);
            DataService.setInventoryItem(vfModule);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            $scope.$broadcast(COMPONENT.SHOW_COMPONENT_DETAILS, {
                componentId: COMPONENT.VF_MODULE
            });
        };

        $scope.getStatusOfVnf = function (serviceObject, vnfObject) {
            var serviceInstance = serviceObject.object;
            var vnf = vnfObject.object;

            DataService.setVnfInstanceId(vnf[FIELD.ID.VNF_ID]);
            DataService.setInventoryItem(vnf);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);
            DataService.setServiceInstanceName(serviceInstance[FIELD.ID.SERVICE_INSTANCE_NAME]);

            $scope.$broadcast(COMPONENT.COMPONENT_STATUS, {
                componentId: COMPONENT.VNF,
                callbackFunction: updateProvStatusVnfCallbackFunction
            });
        };

        $scope.describeVnf = function (serviceObject, vnfObject) {
            var serviceInstance = serviceObject.object;
            var vnf = vnfObject.object;
            DataService.setResCustomizationUuid(" ");

            //var vnfInvariantUuid = vnf[FIELD.ID.MODEL_INVAR_ID];
            //var vnfVersionId = vnf[FIELD.ID.MODEL_VERSION_ID];
            var vnfCustomizationUuid = vnf[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            if (UtilityService.hasContents(vnfCustomizationUuid)) {
                DataService.setResCustomizationUuid(vnfCustomizationUuid);
            }

            setCurrentServiceModelInfoFromScope();
            setCurrentVNFModelInfo(vnfObject);

            //Display popup with additional VNF information
            DataService.setVnfInstanceId(vnf[FIELD.ID.VNF_ID]);
            DataService.setInventoryItem(vnf);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            $scope.$broadcast(COMPONENT.SHOW_COMPONENT_DETAILS, {
                componentId: COMPONENT.VNF
            });
        };

        $scope.describeVolumeGroup = function (serviceObject, vnf, volumeGroupObject) {

            var serviceInstance = serviceObject.object;
            var volumeGroup = volumeGroupObject.object;

            var volGroupCustomizationUuid = volumeGroup[FIELD.ID.MODEL_CUSTOMIZATION_ID];

            setCurrentServiceModelInfoFromScope();
            setCurrentVolumeGroupModelInfoByVfModuleFromScope(vnf, volumeGroupObject);

            DataService.setCustomizationUuid(" ");
            if (UtilityService.hasContents(volGroupCustomizationUuid)) {
                DataService.setCustomizationUuid(volGroupCustomizationUuid);
            }
            DataService.setVolumeGroupInstanceId(volumeGroup[FIELD.ID.VOLUME_GROUP_ID]);
            DataService.setInventoryItem(volumeGroup);

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);


            $scope.$broadcast(COMPONENT.SHOW_COMPONENT_DETAILS, {
                componentId: COMPONENT.VOLUME_GROUP
            });
        };

        $scope.addNetworkInstance = function (netModel, existingVnfs) {

            // For networks we assume that we always follow the new flow
            console.log("Adding network to service instance" + this.service.instance.name);
            if (VIDCONFIGURATION.VNF_STATUS_CHECK_ENABLED && (UtilityService.hasContents(existingVnfs)) && (existingVnfs.length > 0)) {
                var msg = VnfService.isVnfListStatusValid(existingVnfs);
                if (msg != "") {
                    alert(msg);
                    return;
                }
            }

            DataService.setSubscriberName($scope.service.instance.subscriberName);
            DataService.setGlobalCustomerId($scope.service.instance.globalCustomerId);
            DataService.setServiceType($scope.service.instance.serviceType);
            DataService.setServiceInstanceName($scope.service.instance.name);
            DataService.setServiceInstanceId($scope.service.instance.id);
            DataService.setServiceName($scope.service.model.service.name);

            DataService.setModelInfo(COMPONENT.NETWORK, {
                "modelType": "network",
                "modelInvariantId": netModel.invariantUuid,
                "modelVersion": netModel.version,
                "modelNameVersionId": netModel.uuid,
                "modelName": netModel.name,
                "modelCustomizationName": netModel.modelCustomizationName,
                "customizationUuid": netModel.customizationUuid,
                "inputs": "",
                "displayInputs": netModel.displayInputs
            });
            setCurrentServiceModelInfoFromScope();


            $scope.$broadcast(COMPONENT.CREATE_COMPONENT, {
                componentId: COMPONENT.NETWORK,
                callbackFunction: createVnfCallbackFunction
            });
        };

        $scope.addVnfInstance = function (vnf, existingVnfs) {

            if (VIDCONFIGURATION.VNF_STATUS_CHECK_ENABLED && (UtilityService.hasContents(existingVnfs)) && (existingVnfs.length > 0)) {
                var msg = VnfService.isVnfListStatusValid(existingVnfs);
                if (msg != "") {
                    alert(msg);
                    return;
                }
            }

            var isVfc = false;

            _.map($scope.service.model.vnfs, function (value, key) {
                if (value.uuid == vnf.uuid && !_.isEmpty(value.vfcInstanceGroups)) {
                        isVfc = true;
                        var queryData = {
                            serviceModelId: $scope.service.model.service.uuid,
                            serviceInstanceID: $scope.service.instance.id,
                            globalCustomerId: $scope.service.instance.globalCustomerId,
                            serviceType: $scope.service.instance.serviceType,
                            serviceInstanceName: $scope.service.instance.name,
                            modelCustomizationName: value.modelCustomizationName,
                            modelCustomizationId: value.customizationUuid,
                            subscriberName: $scope.service.instance.subscriberName
                        };

                        $scope.$broadcast(COMPONENT.IFRAME_DIALOG, queryData);
                        return;
                    }
            });

            DataService.setSubscriberName($scope.service.instance.subscriberName);
            DataService.setGlobalCustomerId($scope.service.instance.globalCustomerId);
            DataService.setServiceType($scope.service.instance.serviceType);
            DataService.setServiceInstanceName($scope.service.instance.name);
            DataService.setServiceInstanceId($scope.service.instance.id);
            DataService.setServiceName($scope.service.model.service.name);

            console.log("existingVnfs: ");
            console.log(JSON.stringify(existingVnfs, null, 4));
            console.log("existingVnfs: ");
            console.log(JSON.stringify(existingVnfs, null, 4));
            var vnf_type = "";
            var vnf_role = "";
            var vnf_function = "";
            var vnf_code = "";

            if (UtilityService.hasContents(vnf.nfType)) {
                vnf_type = vnf.nfType;
            }
            if (UtilityService.hasContents(vnf.nfRole)) {
                vnf_role = vnf.nfRole;
            }
            if (UtilityService.hasContents(vnf.nfFunction)) {
                vnf_function = vnf.nfFunction;
            }
            if (UtilityService.hasContents(vnf.nfCode)) {
                vnf_code = vnf.nfCode;
            }
            DataService.setModelInfo(COMPONENT.VNF, {
                "modelType": vnf.isPnf ? "pnf" : "vnf",
                "modelInvariantId": vnf.invariantUuid,
                "modelVersion": vnf.version,
                "modelNameVersionId": vnf.uuid,
                "modelName": vnf.name,
                "modelCustomizationName": vnf.modelCustomizationName,
                "customizationUuid": vnf.customizationUuid,
                "inputs": "",
                "displayInputs": vnf.displayInputs,
                "vnfType": vnf_type,
                "vnfRole": vnf_role,
                "vnfFunction": vnf_function,
                "vnfCode": vnf_code,
                "properties": vnf.properties
            });

            DataService.setModelInstanceName($scope.service.model.service.name);
            setCurrentServiceModelInfoFromScope();

            if (vnf.isConfig) {
                DataService.setServiceProxies($scope.service.model.serviceProxies);
                DataService.setSourceServiceProxies(vnf.sourceNodes);
                DataService.setCollectorServiceProxies(vnf.collectorNodes);
                DataService.setConfigurationByPolicy(vnf.isConfigurationByPolicy);
                $location.path("/addNetworkNode");
            } else if (vnf.isPnf) {
                $location.path("/pnfSearchAssociation");
            } else if (isVfc) {
                //do nothing
            } else {
                $scope.$broadcast(COMPONENT.CREATE_COMPONENT, {
                    componentId: COMPONENT.VNF,
                    callbackFunction: createVnfCallbackFunction
                });
            }
        };

        $scope.addVfModuleInstance = function (vnfInstance, vfModuleModel) {

            if (VIDCONFIGURATION.VNF_STATUS_CHECK_ENABLED) {
                var msg = VnfService.isVnfStatusValid(vnfInstance);
                if (msg != "") {
                    alert(msg);
                    return;
                }

            }
            var svcModel = $scope.service.convertedModel;
            DataService.setSubscriberName($scope.service.instance.subscriberName);
            DataService.setGlobalCustomerId($scope.service.instance.globalCustomerId);
            DataService.setServiceType($scope.service.instance.serviceType);
            DataService.setServiceInstanceName($scope.service.instance.name);
            DataService.setServiceInstanceId($scope.service.instance.id);
            DataService.setServiceName($scope.service.model.service.name);

            var vnfModelInvariantUuid = vnfInstance.object[FIELD.ID.MODEL_INVAR_ID];
            var vnfModelVersionId = vnfInstance.object[FIELD.ID.MODEL_VERSION_ID];
            var vnfModelCustomizationUuid = vnfInstance.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];
            var vnfModel = null;
            if (svcModel.isNewFlow) {
                vnfModel = svcModel.vnfs[vnfModelCustomizationUuid];
            }
            else {
                vnfModel = svcModel.vnfs[vnfModelVersionId];
            }

            var availableVolumeGroupList = [];
            angular.forEach(vnfInstance[FIELD.ID.AVAILABLEVOLUMEGROUPS], function (volumeGroupInstance, key) {
                availableVolumeGroupList.push({"instance": volumeGroupInstance});
            });

            DataService.setAvailableVolumeGroupList(availableVolumeGroupList);
            setCurrentServiceModelInfoFromScope();

            DataService.setVnfInstanceId(vnfInstance.object[FIELD.ID.VNF_ID]);

            DataService.setModelInfo(COMPONENT.VNF, {
                "modelInvariantId": vnfModel.invariantUuid,
                "modelVersion": vnfModel.version,
                "modelNameVersionId": vnfModel.uuid,
                "modelName": vnfModel.name,
                "modelCustomizationName": vnfModel.modelCustomizationName,
                "customizationUuid": vnfModel.customizationUuid,
                "inputs": ""
            });

            DataService.setModelInfo(COMPONENT.VF_MODULE, {
                "modelInvariantId": vfModuleModel.invariantUuid,
                "modelVersion": vfModuleModel.version,
                "modelNameVersionId": vfModuleModel.uuid,
                "customizationUuid": vfModuleModel.customizationUuid,
                "modelCustomizationName": vfModuleModel.modelCustomizationName,
                "modelName": vfModuleModel.name,
                "inputs": ""
            });

            $scope.$broadcast(COMPONENT.CREATE_COMPONENT, {
                componentId: COMPONENT.VF_MODULE,
                callbackFunction: createVfModuleCallbackFunction
            });

        };

        $scope.addVolumeGroupInstance = function (vnfInstance, volumeGroupModel) {
            if (VIDCONFIGURATION.VNF_STATUS_CHECK_ENABLED) {
                var msg = VnfService.isVnfStatusValid(vnfInstance);
                if (msg != "") {
                    alert(msg);
                    return;
                }
            }
            var svcModel = $scope.service.convertedModel;
            DataService.setSubscriberName($scope.service.instance.subscriberName);
            DataService.setGlobalCustomerId($scope.service.instance.globalCustomerId);
            DataService.setServiceType($scope.service.instance.serviceType);
            DataService.setServiceInstanceName($scope.service.instance.name);
            DataService.setServiceInstanceId($scope.service.instance.id);
            DataService.setServiceName($scope.service.model.service.name);
            setCurrentServiceModelInfoFromScope();

            DataService.setModelInfo(COMPONENT.SERVICE, {
                "modelInvariantId": $scope.service.model.service.invariantUuid,
                "modelVersion": $scope.service.model.service.version,
                "modelNameVersionId": $scope.service.model.service.uuid,
                "modelName": $scope.service.model.service.name,
                "inputs": ""
            });

            DataService.setVnfInstanceId(vnfInstance.object[FIELD.ID.VNF_ID]);

            var vnfModelInvariantUuid = vnfInstance.object[FIELD.ID.MODEL_INVAR_ID];
            var vnfModelVersionId = vnfInstance.object[FIELD.ID.MODEL_VERSION_ID];
            var vnfCustomizationUuid = vnfInstance.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];
            var vnfModel = null;

            if (svcModel.isNewFlow) {
                vnfModel = svcModel.vnfs[vnfCustomizationUuid];
            }
            else {
                vnfModel = svcModel.vnfs[vnfModelVersionId];
            }

            DataService.setModelInfo(COMPONENT.VNF, {
                "modelInvariantId": vnfModel.invariantUuid,
                "modelVersion": vnfModel.version,
                "modelNameVersionId": vnfModel.uuid,
                "modelName": vnfModel.name,
                "modelCustomizationName": vnfModel.modelCustomizationName,
                "customizationUuid": vnfModel.customizationUuid,
                "inputs": ""
            });

            DataService.setModelInfo(COMPONENT.VOLUME_GROUP, {
                "modelInvariantId": volumeGroupModel.invariantUuid,
                "modelVersion": volumeGroupModel.version,
                "modelNameVersionId": volumeGroupModel.uuid,
                "modelName": volumeGroupModel.name,
                "modelCustomizationName": volumeGroupModel.modelCustomizationName,
                "customizationUuid": volumeGroupModel.customizationUuid,
                "inputs": ""
            });

            $scope.$broadcast(COMPONENT.CREATE_COMPONENT, {
                componentId: COMPONENT.VOLUME_GROUP,
                callbackFunction: createVolumeGroupCallbackFunction
            });
        };

        $scope.resume = function (serviceObject, vfModule, vnfModel) {
            populate_popup_vfModule(serviceObject, vfModule, vnfModel);
            setCurrentVNFModelInfo(vnfModel);

            var availableVolumeGroupList = [];
            angular.forEach(vfModule.volumeGroups, function (volumeGroupInstance, key) {
                availableVolumeGroupList.push({"instance": volumeGroupInstance});
            });

            DataService.setAvailableVolumeGroupList(availableVolumeGroupList);

            DataService.setVfModuleInstanceName(vfModule.object[FIELD.ID.VF_MODULE_NAME]);
            setCurrentServiceModelInfoFromScope();

            if (DataService.getLoggedInUserId())  {
                openVfModuleWithHomingDataModal(COMPONENT.RESUME, vfModule);
            }
            else {
                AaiService.getLoggedInUserID(function (response) {
                    var userID = response.data;
                    DataService.setLoggedInUserId(userID);
                    openVfModuleWithHomingDataModal(COMPONENT.RESUME, vfModule);
                });
            }
        };

        $scope.deleteConfiguration = function (serviceObject, configuration) {
            console.log("Deleting Configuration " + configuration.name);

            var serviceInstance = serviceObject.object;
            var svcModel = $scope.service.convertedModel;
            var configModel;
            DataService.setInventoryItem(configuration.object);
            // set model default and override later if found
            DataService.setModelInfo(COMPONENT.CONFIGURATION, {});

            if (configuration.object != null) {

                //var netModelInvariantUuid = network.object[FIELD.ID.MODEL_INVAR_ID];
                var configModelVersionId = configuration.object[FIELD.ID.MODEL_VERSION_ID]; // model uuid
                var configModelCustomizationUuid = configuration.object[FIELD.ID.MODEL_CUSTOMIZATION_ID];

                //configurations added to vnfs list, in order to be part of the "Add VNF" drop-down list
                if ((!($scope.isObjectEmpty(svcModel))) && (!($scope.isObjectEmpty(svcModel.vnfs)))) {
                    if ((svcModel.isNewFlow) && (UtilityService.hasContents(configModelCustomizationUuid))) {
                        configModel = svcModel.vnfs[configModelCustomizationUuid];
                    }
                    else {

                        if (UtilityService.hasContents(configModelVersionId)) {
                            configModel = svcModel.vnfs[configModelVersionId];
                        }

                    }
                }
            }
            if (!($scope.isObjectEmpty(configModel))) {
                DataService.setModelInfo(COMPONENT.CONFIGURATION, {
                    "modelInvariantId": configModel.invariantUuid,
                    "modelVersion": configModel.version,
                    "modelNameVersionId": configModel.uuid,
                    "modelCustomizationName": configModel.modelCustomizationName,
                    "customizationUuid": configModel.customizationUuid,
                    "modelName": configModel.name,
                    "inputs": ""
                });
            }

            DataService.setSubscriberName(serviceObject[COMPONENT.SUBSCRIBER_NAME]);
            DataService.setServiceType(serviceObject[COMPONENT.SERVICE_TYPE]);
            DataService.setServiceInstanceId(serviceInstance[FIELD.ID.SERVICE_INSTANCE_ID]);

            DataService.setGlobalCustomerId(serviceObject[FIELD.ID.GLOBAL_CUST_ID]);
            DataService.setServiceInstanceName($scope.service.instance.name);
            DataService.setServiceName($scope.service.model.service.name);
            DataService.setServiceUuid($scope.service.model.service.uuid);
            DataService.setConfigurationInstanceId(configuration.object[FIELD.ID.CONFIGURATION_ID]);

            $scope.$broadcast(COMPONENT.DELETE_RESUME_COMPONENT, {
                componentId: COMPONENT.CONFIGURATION,
                dialogMethod: COMPONENT.DELETE,
                callbackFunction: deleteCallbackFunction
            });
        };

        var deleteCallbackFunction = function () {
            console.log('hi')
        };

        $scope.resetProgress = function () {
            $scope.percentProgress = 0;
            $scope.progressClass = FIELD.STYLE.PROGRESS_BAR_INFO;
        };

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
        };
        $scope.isObjectEmpty = function (o) {
            var len = 0;
            if (UtilityService.hasContents(o)) {
                var keys = Object.keys(o);
                len = keys.length;
                if (len == 0) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        };
        $scope.isMacro = function () {
            return $scope.service && AsdcService.isMacro($scope.service.model);
        };
        $scope.reloadRoute = function () {
            $route.reload();
        };


        /*
Private metthods
*/

        /*
        setPnf
        * set the controller pnf param using api call
        * return: void
         */
        function _setPnf(data) { // data is the $scope.service.instance object
            return PnfService.getPnfs(data)
                .then(
                    function (response) {
                        return response.data;
                        // * can add here changes on the data that are needed to the view ( filter, ect..)
                    },
                    function (error) {
                        console.error(error);
                    }
                );
        }


        function _setCr(data) { // data is the $scope.service.instance object
            return CrService.getCr(data)
                .then(
                    function (response) {
                        return response.data.results;
                        // * can add here changes on the data that are needed to the view ( filter, ect..)
                    },
                    function (error) {
                        console.error(error);
                    }
                );
        }


        /*
        Callbaks functions

         */
        var updateProvStatusVnfCallbackFunction = function (response) {
            $scope.callbackResults = "";
            var color = FIELD.ID.COLOR_NONE;
            $scope.callbackStyle = {
                "background-color": color
            };
            $scope.reloadRoute();
            /*
             * This 1/2 delay was only added to visually highlight the status
             * change. Probably not needed in the real application code.
             */
            $timeout(function () {
                $scope.callbackResults = UtilityService.getCurrentTime()
                    + FIELD.STATUS.IS_SUCCESSFUL + response.isSuccessful;
                if (response.isSuccessful) {
                    color = FIELD.ID.COLOR_8F8;

                } else {
                    color = FIELD.ID.COLOR_F88;
                }
                $scope.callbackStyle = {
                    "background-color": color
                };
            }, 500);

        };

        var createVnfCallbackFunction = function (response) {
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

        var deleteOrResumeCallback = function (response) {
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

        var createVfModuleCallbackFunction = function (response) {
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
                    $scope.reloadRoute();
                }
                $scope.callbackStyle = {
                    "background-color": color
                };
            }, 500);

        };

        var deleteServiceInstanceCallbackFunction = function (response) {
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
                    $location.path(COMPONENT.SERVICEMODELS_MODELS_SERVICES_PATH);
                } else {
                    color = FIELD.ID.COLOR_F88;
                }
                $scope.callbackStyle = {
                    "background-color": color
                };
            }, 500);

        };

        var createVolumeGroupCallbackFunction = function (response) {
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

    });
})();
