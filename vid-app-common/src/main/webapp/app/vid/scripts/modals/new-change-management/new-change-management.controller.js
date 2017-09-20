(function () {
    'use strict';

    appDS2.controller("newChangeManagementModalController", ["$uibModalInstance", "$uibModal", "AaiService", "changeManagementService",
        "$log", "$scope", "_", newChangeManagementModalController]);

    function newChangeManagementModalController($uibModalInstance, $uibModal, AaiService, changeManagementService, $log, $scope, _) {
        var vm = this;

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
                                    var versions = _.uniqBy(availableVersions, ['modelInfo.modelVersion']);
                                    newVNFName.availableVersions = _.uniq(versions, response.data.service, true);
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
                                    _.forEach(vnfsData[i]['related-to'], function (node) {
                                        if (node['node-type'] === 'vserver') {
                                            vm.vnfs.push(vnfsData[i]);
                                        }
                                    })
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

        vm.loadVNFVersions = function () {
            vm.fromVNFVersions = [];
            vm.serviceInstancesToGetVersions = [];
            var versions = [];
            _.forEach(vm.vnfs, function (vnf) {
                if (vnf.properties['nf-role'] === vm.changeManagement['vnfType']) {

                vm.serviceInstancesToGetVersions.push(vnf);

                versions.push(vnf.properties["model-invariant-id"]);


                }
            });

            AaiService.getVnfVersionsByInvariantId(versions).then(function (response) {
                if (response.data) {
                    var key = response.data.model["0"]["model-invariant-id"];
                    var value = response.data.model["0"]["model-vers"]["model-ver"]["0"]["model-version"];
                    var element = {"key": key, "value": value};
                    vm.fromVNFVersions.push(element);
                }
                //TODO promise all and call the new api to get the versions.
                // vm.fromVNFVersions.push(response.data.model["0"]["model-vers"]["model-ver"]["0"]["model-version"]);
                // if(vm.serviceInstancesToGetVersions.length > 0){
                //
                // var promiseArrOfGetVnfs = preparePromiseArrOfGetVersions('a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb');
                //
                // Promise.all(promiseArrOfGetVnfs).then(function (allData) {
                //     vm.vnfs = _.flattenDeep(_.without(allData, null));
                //     var filteredVnfs = _.sortedUniqBy(vm.vnfs, function (vnf) {
                //         return vnf.properties.vnfType;
                //     });
                //
                //     _.forEach(filteredVnfs, function (vnf) {
                //         vm.vnfTypes.push(vnf.properties.vnfType)
                //     });
                //
                // }).catch(function (error) {
                //     $log(error);
                // });
                // }
            })
            // debugger;

        };

        // function preparePromiseArrOfGetVersions(serviceInstances) {
        //     var promiseArr = [];
        //     for (var i = 0; i < serviceInstances.length; i++) {
        //         var modelInvariantId = serviceInstances[i].properties["model-invariant-id"];
        //         promiseArr.push(
        //             getVnfs(modelInvariantId)
        //         );
        //     }
        //     return promiseArr;
        // }

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

        vm.loadVNFNames = function () {
            vm.vnfNames = [];

            _.forEach(vm.vnfs, function (vnf) {

                if (vnf.properties['nf-role'] === vm.changeManagement.vnfType) {
                    var vServer = {};

                    _.forEach(vnf['related-to'], function (node) {
                        if (node['node-type'] === 'vserver') {
                            vServer = extractLcpRegionIdAndTenantId(node.url);
                        }
                    });

                    vm.vnfNames.push({
                        "id": vnf.properties["vnf-id"],
                        "name": vnf.properties["vnf-name"],
                        "invariant-id": vnf.properties["model-invariant-id"],
                        "service-instance-node": _.filter(vm.serviceInstances, {id: vnf["related-to"][0].id}),
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

            var splitedUrlByDash = _.split(url, '/', 100);

            cloudConfiguration.lcpCloudRegionId = splitedUrlByDash[7];
            cloudConfiguration.tenantId = splitedUrlByDash[10];

            return cloudConfiguration;
        };

        vm.loadWorkFlows = function () {
            var vnfs = [];
            angular.forEach(vm.changeManagement.vnfNames, function (vnfName) {
                vnfs.push(vnfName.name)
            });

            //TODO: When we'll have the mappings, use the backend call to get the workflows
            // changeManagementService.getWorkflows(vnfs)
            // .then(function(response) {
            //     vm.workflows = response.data;
            // })
            // .catch(function(error) {
            //     $log.error(error);
            // });

            vm.workflows = ["Update", "Replace"];
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

        init();
    }
})();