/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

(function () {
    'use strict';

    appDS2.controller("newChangeManagementModalController", ["$uibModalInstance", "$uibModal", '$q', "AaiService", "changeManagementService", "Upload",
        "$log", "$scope", "_", "COMPONENT", "VIDCONFIGURATION", "DataService", "featureFlags", newChangeManagementModalController]);

    function newChangeManagementModalController($uibModalInstance, $uibModal, $q, AaiService, changeManagementService, Upload, $log, $scope, _, COMPONENT, VIDCONFIGURATION, DataService, featureFlags) {


        var vm = this;
        vm.hasScheduler = !!VIDCONFIGURATION.SCHEDULER_PORTAL_URL;
        vm.errorMsg = '';

        vm.isSearchedVNF = false;

        vm.wizardStep = 1;
        vm.nextStep = function () {
            vm.wizardStep++;
            $(".modal-dialog").animate({"width": "1200px"}, 400, 'linear');
        };
        vm.prevStep = function () {
            vm.wizardStep--;
            $(".modal-dialog").animate({"width": "600px"}, 400, 'linear');
        };

        vm.softwareVersionRegex = "[-a-zA-Z0-9\.]+";

        var attuid;

        $scope.showReportWindow = function () {
            const modalWindow = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/report-modal/report-modal.html',
                controller: 'reportModalController',
                controllerAs: 'vm',
                resolve: {
                    errorMsg: function () {
                        return vm.errorMsg.message;
                    }
                }
            });

        };

        $scope.isShowErrorReport = function () {
            return featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_CREATE_ERROR_REPORTS);
        };

        $scope.isNewFilterChangeManagmentEnabled = function () {
            return (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH));
        };

        $scope.removeVendorFromCloudOwner = function (cloudOwner) {
            return AaiService.removeVendorFromCloudOwner(cloudOwner)
        };

        vm.isDisabledVNFmodelVersion = function (vnfTypePristine) {
            if ($scope.isNewFilterChangeManagmentEnabled()) {
                return !vm.isSearchedVNF;
            } else return vnfTypePristine;
        };

        function fetchAttUid() {
            var defer = $q.defer();
            if (attuid) {
                defer.resolve(attuid);
            } else {
                AaiService.getLoggedInUserID(function (response) {
                        attuid = response.data;
                        defer.resolve(attuid);
                    },
                    function (err) {
                        defer.reject(err);
                        vm.errorMsg = err;
                    });
            }
            return defer.promise;
        }

        var init = function () {
            vm.changeManagement = {};
            vm.changeManagement.workflowParameters = new Map();

            loadServicesCatalog();
            fetchAttUid().then(registerVNFNamesWatcher);
            vm.loadSubscribers();
        };

        var loadServicesCatalog = function () {
            changeManagementService.getAllSDCServices()
                .then(function (response) {
                    vm.SDCServicesCatalog = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                    vm.errorMsg = err;
                });
        };

        var registerVNFNamesWatcher = function () {
            $scope.$watch('vm.changeManagement.vnfNames', function (newVal, oldVal) {
                if (!oldVal || newVal && newVal.length > oldVal.length) { //JUST THE LAST ONE ADDED
                    var newVNFName = _.last(vm.changeManagement.vnfNames);
                    if (oldVal) {
                        vm.changeManagement.vnfNames = oldVal;
                        vm.changeManagement.vnfNames.push(newVNFName);
                    }
                    if (newVNFName && newVNFName["service-instance-node"]) {
                        var availableVersions = [];
                        var services = _.filter(vm.SDCServicesCatalog.services,
                            {"invariantUUID": newVNFName["service-instance-node"][0].properties["model-invariant-id"]});

                        _.each(services, function (service) {
                            changeManagementService.getSDCService(service.uuid)
                                .then(function (response) {
                                    _.each(response.data.vnfs, function (vnf) {
                                        if (newVNFName["invariant-id"] === vnf.invariantUuid) {
                                            availableVersions.push(extractVNFModel(vnf, response.data.service, newVNFName));

                                            //for scale out screen
                                            if (service.uuid === newVNFName["service-instance-node"][0].properties["model-version-id"]) {
                                                newVNFName.vfModules = vnf.vfModules;
                                                newVNFName.category = response.data.service.category;
                                                newVNFName.groupModules = _.groupBy(newVNFName.vfModules, "customizationUuid");

                                                //list vfmodules ids in AAI that belong to that vnf instance
                                                var modulesAaiIds = _.filter(newVNFName.relatedTo, function (item) {
                                                    return item["node-type"] === "vf-module";
                                                }).map(function (item) {
                                                    return item.id;
                                                });

                                                _.forEach(newVNFName.vfModules, function (mdl, key) {
                                                    mdl.scale = false; //defaults to not scale unless user changes it
                                                    if (mdl.properties && mdl.properties.maxCountInstances) {

                                                        //how many vf modules of the same customizationId belong to that vnf instance
                                                        mdl.currentCount = _.filter(vm.vfModules, function (item) {
                                                            return modulesAaiIds.indexOf(item.id) > -1 && item.properties["model-customization-id"] === mdl.customizationUuid;
                                                        }).length;

                                                        mdl.scalable = mdl.properties.maxCountInstances - mdl.currentCount > 0;
                                                    } else {
                                                        mdl.scalable = false;
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    var versions = _.uniqBy(availableVersions, 'modelInfo.modelVersion');
                                    newVNFName.availableVersions = _.sortBy(_.uniq(versions, response.data.service, true), "modelInfo.modelVersion");
                                }).catch(function (error) {
                                $log.error(error);
                                vm.errorMsg = error;
                            });
                        });
                    }
                }
            }, true);
        };

        var extractVNFModel = function (csarVNF, sdcService, selectionVNF) {
            /**
             @param selectionVNF A vnf *instance* selected in "available VNF" drop-down box
             @param csarVNF      A VNF *MODEL* that has an invariantUuid same as selectionVNF (might be
             a different version; i.e. selectionVNF.modelVersionId <> csarVNF.uuid)
             @param sdcService   The Service *MODEL* which has the related VNF `csarVNF`.
             */
            var versionCsarData = {
                vnfInstanceId: "",
                vnfName: csarVNF.name,
                modelInfo: {
                    modelType: "vnf",
                    modelInvariantId: csarVNF.invariantUuid,
                    modelVersionId: csarVNF.uuid,
                    modelName: csarVNF.name,
                    modelVersion: csarVNF.version,
                    modelCustomizationName: csarVNF.modelCustomizationName,
                    modelCustomizationId: csarVNF.customizationUuid
                },
                cloudConfiguration: selectionVNF.cloudConfiguration || {},
                requestInfo: {
                    source: "VID",
                    suppressRollback: false,
                    requestorId: attuid
                },
                relatedInstanceList: [
                    {
                        relatedInstance: {
                            instanceId: selectionVNF["service-instance-node"]["0"].properties['service-instance-id'],
                            modelInfo: {
                                modelType: "service",
                                modelInvariantId: sdcService.invariantUuid,
                                modelVersionId: sdcService.uuid,
                                modelName: sdcService.name,
                                modelVersion: sdcService.version
                            }
                        }
                    }
                ],
                requestParameters: {
                    usePreload: true
                }
            };

            return versionCsarData;
        };

        vm.close = function () {
            $uibModalInstance.close();
        };

        vm.uploadConfigFile = function (file, item) {
            var defer = $q.defer();
            Upload.upload({
                url: "change-management/uploadConfigUpdateFile",
                file: file,
                transformResponse: [function (data) {
                    return data;
                }]
            })
                .then(function (configUpdateResponse) {
                    item.value = configUpdateResponse && JSON.parse(configUpdateResponse.data).payload;
                    defer.resolve(true);
                })
                .catch(function (error) {
                    defer.resolve(false);
                });
            return defer.promise;
        };


        /***converting objects to scheduler format (taken from IST) - was altered for Scale out support ***/
        function extractChangeManagementCallbackDataStr(changeManagement) {
            console.log(changeManagement);
            var result = {};
            result.requestType = changeManagement.workflow;
            var workflowType = changeManagement.workflow;
            result.requestDetails = [];
            _.forEach(changeManagement.vnfNames, function (vnf) {

                try {
                    var requestInfoData = {};
                    var requestParametersData = {};
                    var userParam = {};
                    var moduleToScale = _.find(vnf.vfModules, {"scale": true});
                    if (vnf.availableVersions && vnf.availableVersions.length != 0) {

                        requestInfoData = {
                            source: vnf.availableVersions[0].requestInfo.source,
                            suppressRollback: vnf.availableVersions[0].requestInfo.suppressRollback,
                            requestorId: vnf.availableVersions[0].requestInfo.requestorId
                        };

                        if (workflowType == 'Update') {
                            requestParametersData = {
                                usePreload: vnf.availableVersions[0].requestParameters.usePreload
                            };
                        } else if (workflowType == "Replace") {
                            requestParametersData = {
                                rebuildVolumeGroups: vnf.availableVersions[0].requestParameters.usePreload
                            };
                        } else if (workflowType == "VNF In Place Software Update") {
                            var payloadObj = {
                                'existing_software_version': vm.getInternalWorkFlowParameter(workflowType, 'text', 'Existing software version').value,
                                'new_software_version': vm.getInternalWorkFlowParameter(workflowType, 'text', 'New software version').value,
                                'operations_timeout': vm.getInternalWorkFlowParameter(workflowType, 'text', 'Operations timeout').value
                            };
                            requestParametersData = {
                                payload: JSON.stringify(payloadObj)
                            };
                        } else if (workflowType == "PNF Software Upgrade") {
                            userParam = {
                                "name":"targetSoftwareVersion",
                                "value":vm.getInternalWorkFlowParameter(workflowType, 'text', 'Target software version').value
                            };
                        } else if (workflowType == "VNF Config Update") {
                            requestParametersData = {
                                payload: vm.getInternalWorkFlowParameter("VNF Config Update", "FILE", "Attach configuration file").value
                            };
                        } else if (workflowType == "VNF Scale Out") {
                            if (!moduleToScale) return null;

                            if (moduleToScale.userParams) {
                                requestParametersData = {
                                    userParams: moduleToScale.userParams
                                    //,usePreload: true
                                };
                            } else {
                                requestParametersData = {
                                    userParams: []
                                    //,usePreload: false
                                };
                            }
                        }
                        $log.info('SchedulerWidgetCtrl:extractChangeManagementCallbackDataStr info:: workflowType ' + workflowType);
                        $log.info('SchedulerWidgetCtrl:extractChangeManagementCallbackDataStr info:: requestParametersData ' + requestParametersData);

                    } else if (workflowType == "VNF In Place Software Update") {
                        var payloadObj = {
                            'existing_software_version': vm.getInternalWorkFlowParameter(workflowType, 'text', 'Existing software version').value,
                            'new_software_version': vm.getInternalWorkFlowParameter(workflowType, 'text', 'New software version').value,
                            'operations_timeout': vm.getInternalWorkFlowParameter(workflowType, 'text', 'Operations timeout').value
                        };
                        requestParametersData = {
                            payload: JSON.stringify(payloadObj)
                        };
                    } else if (workflowType == "PNF Software Upgrade") {
                        userParam = {
                            "name":"targetSoftwareVersion",
                            "value":vm.getInternalWorkFlowParameter(workflowType, 'text', 'Target software version').value
                        };
                    } else if (workflowType == "VNF Config Update") {
                        requestParametersData = {
                            payload: vm.getInternalWorkFlowParameter("VNF Config Update", "FILE", "Attach configuration file").value
                        };
                    }

                    var data;
                    if (workflowType == "VNF Scale Out") {
                        data = {
                            vnfName: vnf.name,
                            vnfInstanceId: vnf.id,
                            modelInfo: {
                                modelType: 'vfModule',
                                modelInvariantId: moduleToScale.invariantUuid,
                                modelName: moduleToScale.modelCustomizationName,
                                modelVersion: moduleToScale.version,
                                modelCustomizationName: moduleToScale.modelCustomizationName,
                                modelCustomizationId: moduleToScale.customizationUuid,
                                modelVersionId: moduleToScale.uuid
                            },
                            cloudConfiguration: vnf.cloudConfiguration,
                            requestInfo: requestInfoData,
                            relatedInstanceList: [],
                            requestParameters: requestParametersData,
                            configurationParameters: JSON.parse(vm.getInternalWorkFlowParameter("VNF Scale Out", "text", "Configuration Parameters").value)
                        };
                        requestInfoData.instanceName = vnf.name + "_" + (moduleToScale.currentCount + 1);
                    } else if (workflowType === "PNF Software Upgrade") {
                        data = {
                            pnfInstanceId: vnf.id,
                            modelInfo: {
                                modelType: 'pnf',
                                modelInvariantId: vnf.properties['model-invariant-id'],
                                modelVersionId: vnf.modelVersionId,
                                modelName: vnf.properties['vnf-name'],
                                modelVersion: vnf.version,
                                modelCustomizationName: vnf.properties['model-customization-name'],
                                modelCustomizationId: vnf.properties['model-customization-id']
                            },
                            cloudConfiguration: vnf.cloudConfiguration,
                            requestInfo: requestInfoData,
                            requestParameters: {
                                userParams: [

                                    {
                                        "name":"pnfId",
                                        "value":vnf.properties["vnf-id"]
                                    },
                                    {
                                        "name":"pnfName",
                                        "value": vnf.name
                                    },
                                    userParam
                                ]
                            }
                        };
                    } else {
                        data = {
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
                            cloudConfiguration: vnf.cloudConfiguration,
                            requestInfo: requestInfoData,
                            relatedInstanceList: [],
                            requestParameters: requestParametersData
                        };
                    }

                    var serviceInstanceId = '';
                    _.forEach(vnf['service-instance-node'], function (instanceNode) {
                        if (instanceNode['node-type'] === 'service-instance') {
                            serviceInstanceId = instanceNode.properties['service-instance-id'];
                        }
                    });

                    if (vnf.availableVersions && vnf.availableVersions.length != 0) {
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
                        if (workflowType == "VNF Scale Out") {
                            //push vnf to related as well as the service instance
                            var relatedInstance = {
                                instanceId: vnf.id,
                                modelInfo: {
                                    modelCustomizationId: vnf.availableVersions[0].modelInfo.modelCustomizationId,
                                    modelCustomizationName: vnf.availableVersions[0].modelInfo.modelCustomizationName,
                                    modelInvariantId: vnf.availableVersions[0].modelInfo.modelInvariantId,
                                    modelName: vnf.availableVersions[0].modelInfo.modelName,
                                    modelType: vnf.availableVersions[0].modelInfo.modelType,
                                    modelVersion: vnf.availableVersions[0].modelInfo.modelVersion,
                                    modelVersionId: vnf.availableVersions[0].modelInfo.modelVersionId
                                }
                            };
                            data.relatedInstanceList.push({relatedInstance: relatedInstance});
                        }
                    }
                } catch (err) {
                    $log.error('SchedulerCtrl::extractChangeManagementCallbackDataStr error: ' + err);
                    vm.errorMsg = err;
                }

                result.requestDetails.push(data);
            });
            return JSON.stringify(result);
        }

        function getWorkflowParametersFromForm() {
            let workflowParameters =
                {
                    requestDetails: {
                        cloudConfiguration: {},
                        requestParameters: {userParams: [{}]}
                    }
                };
            workflowParameters.requestDetails.cloudConfiguration = vm.changeManagement.vnfNames[0].cloudConfiguration;

            let parameters = vm.getRemoteWorkFlowParameters(vm.changeManagement.workflow);
            parameters.forEach((parameter) => {
                let inputField = document.getElementById('so-workflow-parameter-' + parameter.soFieldName);
                workflowParameters.requestDetails.requestParameters.userParams[0][parameter.soFieldName] = inputField.value;
            });

            return workflowParameters;
        }

        vm.openModal = function () {
            if (vm.hasScheduler) { //scheduling supported
                vm.scheduleWorkflow();
            } else {
                //no scheduling support
                vm.executeWorkflow();
            }
        };

        vm.collectWorkflowFieldsValues = function () {
            /**
             * Transforms items with name and value properties, to associative map, e.g the array
             * [{name: foo, value: bar}, {name: baz, value: fiz}] will become the object {foo: bar, baz: fiz}
             */
            return vm.getAllInternalWorkFlowParameters(
                    vm.changeManagement.workflow
            ).reduce(function (result, item) {
                result[item.name] = item.value;
                return result;
            }, {});
        };

        vm.scheduleWorkflow = function () {
            $scope.widgetParameter = ""; // needed by the scheduler?

            // properties needed by the scheduler so it knows whether to show
            // policy or sniro related features on the scheduler UI or not.
            vm.changeManagement.policyYN = "Y";
            vm.changeManagement.sniroYN = "Y";

            if (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_ADD_MSO_TESTAPI_FIELD)) {
                vm.changeManagement.testApi = DataService.getMsoRequestParametersTestApi();
            }
            var data = {
                widgetName: 'Portal-Common-Scheduler',
                widgetData: Object.assign({}, vm.changeManagement, vm.collectWorkflowFieldsValues()),
                widgetParameter: $scope.widgetParameter
            };

            console.log("vm.scheduleWorkflow data:", data);

            if (window.parent !== window.self) {
                window.parent.postMessage(data, VIDCONFIGURATION.SCHEDULER_PORTAL_URL);
            } else {
                vm.errorMsg = {message: "Portal not found. Cannot send: " + JSON.stringify(data)};
                throw vm.errorMsg; // prevent popup closure
            }
        };

        vm.executeWorkflow = function () {
            if (vm.localWorkflows && vm.localWorkflows.length > 0) {
                vm.triggerLocalWorkflow();
            } else {
                let source = vm.getRemoteWorkflowSource(vm.changeManagement.workflow);
                if (_.toUpper(source) === "NATIVE") {
                    vm.triggerLocalWorkflow();
                } else {
                    vm.triggerRemoteWorkflow();
                }
            }
        };

        vm.triggerLocalWorkflow = function () {
            var dataToSo = extractChangeManagementCallbackDataStr(vm.changeManagement);
            if (dataToSo) {
                var vnfName = vm.changeManagement.vnfNames[0].name;
                changeManagementService.postChangeManagementNow(dataToSo, vnfName);
            }
        };

        vm.triggerRemoteWorkflow = function () {
            let cachedWorkflowDetails = vm.getCachedWorkflowDetails(vm.changeManagement.workflow);
            if (cachedWorkflowDetails.length > 0) {
                let workflowParameters = getWorkflowParametersFromForm();
                if (workflowParameters) {
                    let servieInstanceId = vm.changeManagement.vnfNames[0]['service-instance-node'][0].properties['service-instance-id'];
                    let vnfInstanceId = vm.changeManagement.vnfNames[0].id;
                    let workflow_UUID = cachedWorkflowDetails[0].id;
                    changeManagementService.postWorkflowsParametersNow(servieInstanceId, vnfInstanceId, workflow_UUID, workflowParameters);
                }
            }
        };

        vm.loadSubscribers = function () {
            vm.subscribers = [];
            AaiService.getSubscribers(function (response) {
                vm.subscribers = response;
            });
        };

        vm.loadServiceTypes = function () {
            vm.serviceTypes = [];

            AaiService.getSubscriberServiceTypes(vm.changeManagement.subscriberId)
                .then(function (response) {
                    vm.serviceTypes = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                    vm.errorMsg = error;
                });
        };

        function isCompatibleNFRole(vnf) {

            return vnf.properties['nf-role'] === vm.changeManagement['vnfType'] || !vm.changeManagement['vnfType'];

        }

        function isValidVnf(vnf) {

            let result =  isCompatibleNFRole(vnf) && vnf.properties["model-invariant-id"]
                && vnf.properties["model-version-id"];

            return result;
        }

        function loadCloudRegions() {
            AaiService.getLcpCloudRegionTenantList(
                vm.changeManagement.subscriberId,
                vm.changeManagement.serviceType["service-type"],
                function (response) {
                    $scope.isFeatureFlagCloudOwner = featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST);
                    $scope.cloudRegionList = _.uniqBy(response, 'cloudRegionOptionId');
                });
        }

        vm.serviceTypeChanged = function () {
            if ($scope.isNewFilterChangeManagmentEnabled()) {
                loadCloudRegions();
            } else {
                vm.searchVNFs();
            }
        };

        vm.searchVNFs = function () {
            vm.vnfTypes = [];
            vm.vnfTypesTemp = [];
            vm.serviceInstances = [];
            vm.fromVNFVersions = [];
            vm.vnfNames = [];
            vm.changeManagement.vnfNames = [];

            var instances = vm.changeManagement.serviceType["service-instances"]["service-instance"];
            // var promiseArrOfGetVnfs = preparePromiseArrOfGetVnfs(instances);

            vm.vnfs = [];
            vm.vfModules = [];

            let nfRole = null;
            let cloudRegion = null;

            if ($scope.isNewFilterChangeManagmentEnabled()) {
                nfRole = vm.changeManagement.vnfType ? vm.changeManagement.vnfType : null;
                cloudRegion = vm.changeManagement.cloudRegion ? vm.changeManagement.cloudRegion : null;
            }

            AaiService.getVnfsByCustomerIdAndServiceType(
                vm.changeManagement.subscriberId,
                vm.changeManagement.serviceType["service-type"],
                nfRole,
                cloudRegion,
            ).then(function (response) {
                    vm.isSearchedVNF = true;
                    var vnfsData = response.data.results;
                    if (vnfsData) {
                        for (var i = 0; i < vnfsData.length; i++) {
                            if (vnfsData[i]) {
                                const nodeType = vnfsData[i]['node-type'];
                                if (nodeType === "generic-vnf") {
                                    if (_.find(vnfsData[i]['related-to'], function (node) {
                                        return node['node-type'] === 'vserver';
                                    }) !== undefined) {
                                        vm.vnfs.push(vnfsData[i]);
                                    }
                                } else if (nodeType === "service-instance") {
                                    vm.serviceInstances.push(vnfsData[i]);
                                } else if (nodeType === "vf-module") {
                                    vm.vfModules.push(vnfsData[i]);
                                }
                            }
                        }

                        vm.vnfs = _.flattenDeep(
                            _.remove(vm.vnfs, function (vnf) {
                                var nfRole = vnf.properties['nf-role'];
                                if (nfRole !== undefined) {
                                    return nfRole !== 'null' && nfRole !== '';
                                }
                            })
                        );

                        var filteredVnfs = _.uniqBy(vm.vnfs, function (vnf) {
                            return vnf.properties['nf-role'];
                        });

                        _.forEach(filteredVnfs, function (vnf) {
                            vm.vnfTypes.push(vnf.properties['nf-role'])
                        });
                    }
                    if ($scope.isNewFilterChangeManagmentEnabled()) {
                        vm.loadVNFVersions();
                    }
                }
            );
        };

        var fromVNFVersions = [];

        vm.loadVNFVersions = function () {
            fromVNFVersions = [];
            vm.serviceInstancesToGetVersions = [];
            var versions = [];
            _.forEach(vm.vnfs, function (vnf) {
                if (isValidVnf(vnf)) {
                    vm.serviceInstancesToGetVersions.push({
                            "model-invariant-id": vnf.properties["model-invariant-id"],
                            "model-version-id": vnf.properties["model-version-id"]
                        }
                    );

                    versions.push(vnf.properties["model-invariant-id"]);
                }
            });

            if (versions.length > 0) {
                AaiService.getVnfVersionsByInvariantId(versions).then(function (response) {
                    if (response.data) {

                        $log.debug("getVnfVersionsByInvariantId: response", response);

                        fromVNFVersions = vm.serviceInstancesToGetVersions
                            .map(function (serviceInstanceToGetVersion) {
                                const model = _.find(response.data.model, {'model-invariant-id': serviceInstanceToGetVersion['model-invariant-id']});
                                $log.debug("getVnfVersionsByInvariantId: model for " + serviceInstanceToGetVersion['model-invariant-id'], model);

                                const modelVer = _.find(model["model-vers"]["model-ver"], {'model-version-id': serviceInstanceToGetVersion['model-version-id']});
                                $log.debug("getVnfVersionsByInvariantId: modelVer for " + serviceInstanceToGetVersion['model-version-id'], modelVer);

                                var modelVersionId = serviceInstanceToGetVersion["model-version-id"];
                                var modelVersion = modelVer["model-version"];

                                return {"key": modelVersionId, "value": modelVersion};
                            });

                        vm.fromVNFVersions = _.uniqBy(fromVNFVersions, 'value');
                    }
                });
            }
        };


        function getVnfs(modelInvariantId) {
            return new Promise(function (resolve, reject) {
                AaiService.getVnfVersionsByInvariantId(modelInvariantId)
                    .then(function (response) {
                        if (response.data.httpCode !== null &&
                            response.data.httpCode === 200) {
                            var vnfsData = response.data.t.results;
                            for (var i = 0; i < vnfsData.length; i++) {
                                if (vnfsData[i]) {
                                    if (vnfsData[i].nodeType === "generic-vnf") {
                                        resolve(vnfsData[i]);
                                    } else if (vnfsData[i].nodeType === "service-instance") {
                                        vm.serviceInstances.push(vnfsData[i]);
                                    }
                                }
                            }
                            resolve(null);
                        }
                        resolve(null);
                    })
                    .catch(function (error) {
                        reject(error);
                        vm.errorMsg = error;
                    });
            });
        }

        var getVersionNameForId = function (versionId) {
            var version = _.find(fromVNFVersions, {"key": versionId});
            return version.value;
        };

        vm.loadVNFNames = function () {
            vm.changeManagement.vnfNames = [];
            vm.vnfNames = [];

            const vnfs = vm.changeManagement.fromVNFVersion ? vm.vnfs : [];
            _.forEach(vnfs, function (vnf) {

                var selectedVersionNumber = getVersionNameForId(vm.changeManagement.fromVNFVersion);

                if (isCompatibleNFRole(vnf) &&
                    selectedVersionNumber === getVersionNameForId(vnf.properties["model-version-id"])) {
                    var vServer = {};

                    _.forEach(vnf['related-to'], function (node) {
                        if (node['node-type'] === 'vserver') {
                            vServer = extractLcpRegionIdAndTenantId(node.url);
                        }
                    });

                    var serviceInstancesIds =
                        _.filter(vnf['related-to'], {'node-type': 'service-instance'})
                            .map(function (serviceInstance) {
                                return serviceInstance.id
                            });

                    var serviceInstances = _.filter(vm.serviceInstances, function (serviceInstance) {
                        return _.includes(serviceInstancesIds, serviceInstance.id);
                    });

                    // logging only
                    if (serviceInstancesIds.length === 0) {
                        $log.error("loadVNFNames: no serviceInstancesIds for vnf", vnf);
                    } else {
                        $log.debug("loadVNFNames: serviceInstancesIds", serviceInstancesIds);
                        $log.debug("loadVNFNames: serviceInstances", serviceInstances);
                    }

                    vm.vnfNames.push({
                        "id": vnf.properties["vnf-id"],
                        "name": vnf.properties["vnf-name"],
                        "invariant-id": vnf.properties["model-invariant-id"],
                        "service-instance-node": serviceInstances,
                        "modelVersionId": vnf.properties["model-version-id"],
                        "properties": vnf.properties,
                        'cloudConfiguration': vServer,
                        "relatedTo": vnf['related-to']
                    });
                }
            });
        };

        function extractLcpRegionIdAndTenantId(url) {

            var cloudConfiguration = {
                lcpCloudRegionId: '',
                tenantId: ''
            };

            /*
             e.g., in both URLs below -
               - lcpCloudRegionId == 'rdm5b'
               - tenantId == '0675e0709bd7444a9e13eba8b40edb3c'

             "url": "https://aai-conexus-e2e.ecomp.cci.att.com:8443/aai/v10/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/rdm5b/tenants/tenant/0675e0709bd7444a9e13eba8b40edb3c/vservers/vserver/932b330d-733e-427d-a519-14eebd261f70"
             "url": "/aai/v11/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/rdm5b/tenants/tenant/0675e0709bd7444a9e13eba8b40edb3c/vservers/vserver/932b330d-733e-427d-a519-14eebd261f70"
             */

            var cloudRegionMatch = url.match(/\/cloud-regions\/cloud-region\/[^\/]+\/([^\/]+)/);
            var tenantMatch = url.match(/\/tenants\/tenant\/([^\/]+)/);

            cloudConfiguration.lcpCloudRegionId = cloudRegionMatch[1];
            cloudConfiguration.tenantId = tenantMatch[1];

            return cloudConfiguration;
        };

        vm.loadWorkFlows = function () {
            vm.localWorkflowsParameters = new Map();
            vm.remoteWorkflowsParameters = new Map();
            if (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_HANDLE_SO_WORKFLOWS)) {
                return vm.loadRemoteWorkFlows()
                    .then(function () {
                        vm.workflows = vm.remoteWorkflows.map(item => item.name);
                    }).then(function () {
                        vm.loadRemoteWorkFlowsParameters();
                    });
            } else {
                return vm.loadLocalWorkFlows()
                    .then(vm.loadLocalWorkFlowsParameters)
                    .then(function () {
                        vm.workflows = vm.localWorkflows;
                    });
            }
        };

        vm.loadLocalWorkFlows = function () {
            return changeManagementService.getWorkflows(vm.changeManagement.vnfNames)
                .then(function (response) {
                    vm.localWorkflows = response.data.workflows || [];
                }).catch(function (error) {
                    $log.error(error);
                    vm.errorMsg = error;
                });
        };

        vm.loadRemoteWorkFlows = function () {
            let vnfModelIDs = (vm.changeManagement.vnfNames || []).map(vnfName => vnfName.modelVersionId);
            if (vnfModelIDs.length === 0) {
                vm.remoteWorkflows = [];
                return $q.resolve();
            }
            return changeManagementService.getSOWorkflows(vnfModelIDs)
                .then(function (response) {
                    vm.remoteWorkflows = response.data || [];
                }).catch(function (error) {
                    $log.error(error);
                    vm.errorMsg = error;
                });
        };

        vm.loadLocalWorkFlowsParameters = function () {
            vm.localWorkflows.forEach(function (workflow) {
                vm.loadLocalWorkFlowParameters(workflow);
            });
        };

        vm.loadLocalWorkFlowParameters = function (workflow) {
            changeManagementService.getLocalWorkflowParameter(workflow)
                .then(function (response) {
                    vm.localWorkflowsParameters.set(workflow, response.data.parameterDefinitions);
                })
                .catch(function (error) {
                    $log.error(error);
                    vm.errorMsg = error;
                });
        };

        vm.loadRemoteWorkFlowsParameters = function () {
            vm.remoteWorkflows.forEach(function (workflow) {
                if (workflow.source === 'SDC' || workflow.source === 'sdc') {
                    vm.loadRemoteWorkFlowParameters(workflow);
                } else {
                    vm.loadLocalWorkFlowParameters(workflow.name);
                }
            });
        };

        vm.loadRemoteWorkFlowParameters = function (workflow) {
            let parameters = [];
            workflow.workflowInputParameters
                .filter(function (param) {
                    return param.soPayloadLocation === "userParams"
                })
                .forEach(function (param) {
                        let workflowParams = vm.repackAttributes(param);
                        if (param.validation.length > 0) {
                            let validation = param.validation[0];
                            if ('maxLength' in validation) {
                                workflowParams.maxLength = validation.maxLength;
                            }
                            if ('allowableChars' in validation) {
                                workflowParams.pattern = validation.allowableChars;
                            }
                        }
                        workflowParams.type = param.inputType;

                        parameters.push(workflowParams);
                    }
                );
            vm.remoteWorkflowsParameters.set(workflow.name, parameters);
        };

        vm.repackAttributes = function (workflowParam) {
            return {
                name: workflowParam.label,
                required: workflowParam.required,
                id: workflowParam.soFieldName,
                soFieldName: workflowParam.soFieldName,
                maxLength: '500',
                pattern: '.*'
            }
        };

        vm.getRemoteWorkFlowParameters = function (workflow) {
            if (workflow && vm.remoteWorkflowsParameters.has(workflow)) {
                return vm.remoteWorkflowsParameters.get(workflow)
            }
            return [];
        };

        vm.hasPatternError = function (form, itemName) {
            return form[itemName].$error.pattern;
        };

        vm.hasAsyncFnError = function (form, itemName) {
            return form[itemName].$error.validateAsyncFn;
        };

        vm.getIdFor = function (type, item) {
            return "internal-workflow-parameter-" + type + "-" + item.id + "-" + (item.displayName ? item.displayName.split(' ').join('-').toLowerCase() : "");
        };

        vm.getAllInternalWorkFlowParameters = function (workflow) {
            if (workflow && vm.localWorkflowsParameters.has(workflow) && vm.localWorkflowsParameters.get(workflow)) {
                return vm.localWorkflowsParameters.get(workflow);
            }
            return [];
        };

        vm.getInternalWorkFlowParameters = function (workflow, type) {
            console.log("getInternalWorkFlowParameters", workflow, type);
            return vm.getAllInternalWorkFlowParameters(workflow).filter(parameter => parameter.type === type);
        };

        vm.getInternalWorkFlowParameter = function (workflow, type, parameterName) {
            return vm.getInternalWorkFlowParameters(workflow, type).filter(parameter => parameter.displayName === parameterName)[0];
        };

        vm.getRemoteWorkflowSource = (workflow) => {
            return vm.getCachedWorkflowDetails(workflow)[0].source;
        };

        vm.getCachedWorkflowDetails = function (workflow) {
            return vm.remoteWorkflows.filter(function (remoteWorkflow) {
                return remoteWorkflow.name === workflow;
            });

        };

        //Must be $scope because we bind to the onchange of the html (cannot attached to vm variable).
        $scope.selectFileForVNFName = function (fileInput) {
            if (fileInput && fileInput.id) {
                var vnfName = _.filter(vm.changeManagement.vnfNames, {"invariant-id": fileInput.id});
                var file = fileInput.files[0];
                var fileReader = new FileReader();
                fileReader.onload = function (load) {
                    try {
                        var lines = load.target.result;
                        vnfName[0].selectedFile = JSON.parse(lines);
                    } catch (error) {
                        $log.error(error);
                        vm.errorMsg = error;
                    }
                };
                fileReader.readAsText(file);
            }
        };

        vm.selectVersionForVNFName = function (vnfName) {
            console.log("Will add version for selected vnf name: " + vnfName.name);
        };

        vm.isConfigUpdate = function () {
            return vm.changeManagement.workflow === COMPONENT.WORKFLOWS.vnfConfigUpdate;
        };

        vm.isScaleOut = function () {
            return vm.changeManagement.workflow === COMPONENT.WORKFLOWS.vnfScaleOut;
        };

        vm.shouldShowVnfInPlaceFields = function () {
            return vm.changeManagement.workflow === COMPONENT.WORKFLOWS.vnfInPlace;
        };

        vm.setPreload = function (fileEl) {
            var files = fileEl.files;
            var file = files[0];
            var reader = new FileReader();

            reader.onloadend = function (evt) {
                if (evt.target.readyState === FileReader.DONE) {
                    $scope.$apply(function () {
                        $scope.moduleArr[0].userParams = JSON.parse(evt.target.result);
                    });
                }
            };

            reader.readAsText(file);
        };

        init();
    }
})();