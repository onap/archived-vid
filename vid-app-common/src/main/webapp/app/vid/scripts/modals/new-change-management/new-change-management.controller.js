(function () {
    'use strict';

    appDS2.controller("newChangeManagementModalController", ["$uibModalInstance", "$uibModal", "AaiService", "changeManagementService",
        "$log", "$scope", "_", "COMPONENT", newChangeManagementModalController]);

    function newChangeManagementModalController($uibModalInstance, $uibModal, AaiService, changeManagementService, $log, $scope, _, COMPONENT) {
        var vm = this;

        vm.softwareVersionRegex = "[-a-zA-Z0-9\.]+";

        var init = function () {
            vm.changeManagement = {};

            loadServicesCatalog();
            registerVNFNamesWatcher();
            vm.loadSubscribers();
        };

        var loadServicesCatalog = function () {
            changeManagementService.getAllSDCServices()
                .then(function (response) {
                    vm.SDCServicesCatalog = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                })
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
                                        }
                                    });
                                    var versions = _.uniqBy(availableVersions, 'modelInfo.modelVersion');
                                    newVNFName.availableVersions = _.sortBy(_.uniq(versions, response.data.service, true),"modelInfo.modelVersion");
                                }).catch(function (error) {
                                $log.error(error);
                            });
                        });
                    }
                }
            }, true);
        };

        var extractVNFModel = function (csarVNF, sdcService, selectionVNF) {
            var versionCsarData = {
                vnfInstanceId: "",
                vnfName: csarVNF.name,
                modelInfo: {
                    modelType: "vnf",
                    modelInvariantId: csarVNF.invariantUuid,
                    modelVersionId: selectionVNF.modelVersionId,
                    modelName: csarVNF.name,
                    modelVersion: csarVNF.version,
                    modelCustomizationName: csarVNF.modelCustomizationName,
                    modelCustomizationId: csarVNF.customizationUuid
                },
                cloudConfiguration: {
                    lcpCloudRegionId: "mdt1",
                    tenantId: "88a6ca3ee0394ade9403f075db23167e"
                },
                requestInfo: {
                    source: "VID",
                    suppressRollback: false,
                    requestorId: "az2016"
                },
                relatedInstanceList: [
                    {
                        relatedInstance: {
                            instanceId: selectionVNF["service-instance-node"]["0"].properties['service-instance-id'],
                            modelInfo: {
                                modelType: "service",
                                modelInvariantId: selectionVNF["service-instance-node"]["0"].properties['model-invariant-id'],
                                modelVersionId: selectionVNF.modelVersionId,
                                modelName: sdcService.name,
                                modelVersion: sdcService.version,
                                modelCustomizationName: selectionVNF["service-instance-node"]["0"].properties['model-customization-name'], //TODO: Missing
                                modelCustomizationId: selectionVNF["service-instance-node"]["0"].properties['model-customization-id']
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

        vm.schedule = function () {
            $uibModalInstance.close(vm.changeManagement);

            var modalInstance = $uibModal.open({
                templateUrl: 'app/vid/scripts/modals/new-scheduler/new-scheduler.html',
                controller: 'newSchedulerController',
                controllerAs: 'vm',
                resolve: {
                    changeManagement: function () {
                        return vm.changeManagement;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                console.log("This is the result of the new change management modal.", result);
            })
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
                });
        };

        vm.loadVNFTypes = function () {
            vm.vnfTypes = [];
            vm.vnfTypesTemp = [];
            vm.serviceInstances = [];

            var instances = vm.changeManagement.serviceType["service-instances"]["service-instance"];
            // var promiseArrOfGetVnfs = preparePromiseArrOfGetVnfs(instances);

            vm.vnfs = [];

            AaiService.getVnfsByCustomerIdAndServiceType(
                vm.changeManagement.subscriberId,
                vm.changeManagement.serviceType["service-type"]
            ).then(function (response) {
                    var vnfsData = response.data.results;
                    if (vnfsData) {
                        for (var i = 0; i < vnfsData.length; i++) {
                            if (vnfsData[i]) {
                                const nodeType = vnfsData[i]['node-type'];
                                if (nodeType === "generic-vnf") {
                                    if (_.find(vnfsData[i]['related-to'], function (node) {
                                            return node['node-type'] === 'vserver'
                                        }) !== undefined) {
                                        vm.vnfs.push(vnfsData[i]);
                                    }
                                } else if (nodeType === "service-instance") {
                                    vm.serviceInstances.push(vnfsData[i]);
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
                }
            );
        };

        var fromVNFVersions = [];

        vm.loadVNFVersions = function () {
            fromVNFVersions = [];
            vm.serviceInstancesToGetVersions = [];
            var versions = [];
            _.forEach(vm.vnfs, function (vnf) {
                if (vnf.properties['nf-role'] === vm.changeManagement['vnfType']) {

                    vm.serviceInstancesToGetVersions.push({
                        "model-invariant-id": vnf.properties["model-invariant-id"],
                        "model-version-id": vnf.properties["model-version-id"] }
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
                })
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
                    });
            });
        }

        var getVersionNameForId = function(versionId) {
            var version = _.find(fromVNFVersions, {"key": versionId});
            return version.value;
        };

        vm.loadVNFNames = function () {
            vm.vnfNames = [];
            const vnfs = vm.changeManagement.fromVNFVersion ? vm.vnfs : [];
            _.forEach(vnfs, function (vnf) {

                var selectedVersionNumber = getVersionNameForId(vm.changeManagement.fromVNFVersion);

                if (vnf.properties['nf-role'] === vm.changeManagement.vnfType &&
                    selectedVersionNumber === getVersionNameForId(vnf.properties["model-version-id"])) {
                    var vServer = {};

                    _.forEach(vnf['related-to'], function (node) {
                        if (node['node-type'] === 'vserver') {
                            vServer = extractLcpRegionIdAndTenantId(node.url);
                        }
                    });

                    var serviceInstancesIds =
                        _.filter(vnf['related-to'], {'node-type': 'service-instance'})
                            .map(function (serviceInstance) { return serviceInstance.id });

                    var serviceInstances = _.filter(vm.serviceInstances, function(serviceInstance) {
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

             "url": "https://aai-conexus-e2e.ecomp.cci.att.com:8443/aai/v10/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/0675e0709bd7444a9e13eba8b40edb3c/vservers/vserver/932b330d-733e-427d-a519-14eebd261f70"
             "url": "/aai/v11/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/0675e0709bd7444a9e13eba8b40edb3c/vservers/vserver/932b330d-733e-427d-a519-14eebd261f70"
             */

            var cloudRegionMatch = url.match(/\/cloud-regions\/cloud-region\/[^\/]+\/([^\/]+)/);
            var tenantMatch = url.match(/\/tenants\/tenant\/([^\/]+)/);

            cloudConfiguration.lcpCloudRegionId = cloudRegionMatch[1];
            cloudConfiguration.tenantId = tenantMatch[1];

            return cloudConfiguration;
        };

        vm.loadWorkFlows = function () {
            changeManagementService.getWorkflows(vm.changeManagement.vnfNames)
                .then(function(response) {
                    vm.workflows = response.data.workflows;
                })
                .catch(function(error) {
                    $log.error(error);
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
                    }
                };
                fileReader.readAsText(file);
            }
        };

        vm.selectVersionForVNFName = function (vnfName) {
            console.log("Will add version for selected vnf name: " + vnfName.name);
        };

        vm.shouldShowVnfInPlaceFields = function () {
            return vm.changeManagement.workflow === COMPONENT.WORKFLOWS.vnfInPlace;
        };

        init();
    }
})();