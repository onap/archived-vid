(function () {
    'use strict';

    appDS2.controller("newChangeManagementModalController", ["$uibModalInstance", "$uibModal",'$q', "AaiService", "changeManagementService", "Upload",
        "$log", "$scope", "_", "COMPONENT", "VIDCONFIGURATION", newChangeManagementModalController]);

    function newChangeManagementModalController($uibModalInstance, $uibModal,$q, AaiService, changeManagementService, Upload, $log, $scope, _, COMPONENT, VIDCONFIGURATION) {

        var vm = this;
        vm.hasScheduler = !!VIDCONFIGURATION.SCHEDULER_PORTAL_URL;
        vm.configUpdatePatternError = "Invalid file type. Please select a file with a CSV extension.";
        vm.configUpdateContentError = "Invalid file structure.";
        vm.controllers = VIDCONFIGURATION.SCALE_OUT_CONTROLLERS;
        vm.wizardStep = 1;
        vm.nextStep = function(){
            vm.wizardStep++;
            $(".modal-dialog").animate({"width":"1200px"},400,'linear');
        };
        vm.prevStep = function(){
            vm.wizardStep--;
            $(".modal-dialog").animate({"width":"600px"},400,'linear');
        };

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

                                            //for scale out screen
                                            if(service.uuid === newVNFName["service-instance-node"][0].properties["model-version-id"]) {
                                                newVNFName.vfModules = vnf.vfModules;
                                                newVNFName.category = response.data.service.category;
                                                newVNFName.groupModules = _.groupBy(newVNFName.vfModules, "customizationUuid");
                                                _.forEach(newVNFName.vfModules, function (mdl, key) {
                                                    mdl.scale = false; //defaults to not scale unless user changes it
                                                    if(mdl.properties && mdl.properties.max_vf_module_instances) {
                                                        mdl.scalable = mdl.properties.max_vf_module_instances.value - newVNFName.groupModules[mdl.customizationUuid].length > 0;
                                                    }else{
                                                        mdl.scalable = false;
                                                    }
                                                });
                                            }
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

        vm.uploadConfigFile = function (file) {
            var defer = $q.defer();
            Upload.upload({
                url: "change-management/uploadConfigUpdateFile",
                file: file,
                transformResponse: [function (data) {
                    return data;
                }]
            })
            .then(function (configUpdateResponse) {
                vm.changeManagement.configUpdateFile = configUpdateResponse && JSON.parse(configUpdateResponse.data).payload;
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
				
				try{
				var requestInfoData ={};
				var requestParametersData ={};
                var moduleToScale = _.find(vnf.vfModules, function(key, item){
                    return !item.scale;
                });
				if (vnf.availableVersions && vnf.availableVersions.length!=0){
					
					requestInfoData ={
						source: vnf.availableVersions[0].requestInfo.source,
						suppressRollback: vnf.availableVersions[0].requestInfo.suppressRollback,
						requestorId: vnf.availableVersions[0].requestInfo.requestorId
					}
					
					if(workflowType=='Update'){
						requestParametersData = {
							usePreload: vnf.availableVersions[0].requestParameters.usePreload
						}
					}else if(workflowType=="Replace"){
						requestParametersData = {
							rebuildVolumeGroups: vnf.availableVersions[0].requestParameters.usePreload
						}
					}else if(workflowType=="VNF In Place Software Update"){
						var payloadObj = {
							'existing_software_version':changeManagement.existingSoftwareVersion,
							'new_software_version':changeManagement.newSoftwareVersion,
							'operations_timeout':changeManagement.operationTimeout
						};
						requestParametersData = {
                            controllerType: changeManagement.controllerType,
                            payload: JSON.stringify(payloadObj)
						}
					}else if(workflowType=="VNF Config Update"){
						requestParametersData = {
							payload: changeManagement.configUpdateFile
						}
                    }else if(workflowType=="VNF Scale Out"){
                        if(!moduleToScale) return null;

                        if(moduleToScale.userParams) {
                            requestParametersData = {
                                controllerType: changeManagement.controllerType,
                                userParams: moduleToScale.userParams
                                //,usePreload: true
                            }
                        }else{
                            requestParametersData = {
                                controllerType: changeManagement.controllerType,
                                userParams: []
                                //,usePreload: false
                            }
                        }
                    }
					$log.info('SchedulerWidgetCtrl:extractChangeManagementCallbackDataStr info:: workflowType '+ workflowType);
					$log.info('SchedulerWidgetCtrl:extractChangeManagementCallbackDataStr info:: requestParametersData '+ requestParametersData);

				}else if(workflowType=="VNF In Place Software Update"){
					var payloadObj = {
						'existing_software_version':changeManagement.existingSoftwareVersion,
						'new_software_version':changeManagement.newSoftwareVersion,
						'operations_timeout':changeManagement.operationTimeout
					};
					requestParametersData = {
						payload: JSON.stringify(payloadObj)
					}
				}else if(workflowType=="VNF Config Update"){
					requestParametersData = {
						payload: changeManagement.configUpdateFile
					}
				}


				var data;
				if(workflowType=="VNF Scale Out") {
				    var name = moduleToScale.modelCustomizationName.split('-')[0]; //example: vSAMP12..base..module-0
                    name = name + "-" + vnf.groupModules[moduleToScale.customizationUuid].length;

                    data = {
                        modelInfo: {
                            modelType: 'vfModule',
                            modelInvariantId: moduleToScale.invariantUuid,
                            modelName: name,
                            modelVersion: moduleToScale.version,
                            modelVersionId: moduleToScale.uuid
                        },
                        cloudConfiguration: vnf.cloudConfiguration,
                        requestInfo: requestInfoData,
                        relatedInstanceList: [],
                        requestParameters:requestParametersData
                    }
                    requestInfoData.instanceName = vnf.name;
                }else{
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
                        requestParameters:requestParametersData
                    }
                }

				var serviceInstanceId = '';
				_.forEach(vnf['service-instance-node'], function (instanceNode) {
					if(instanceNode['node-type'] === 'service-instance'){
						serviceInstanceId = instanceNode.properties['service-instance-id'];
					}
				});

				if (vnf.availableVersions && vnf.availableVersions.length!=0){
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
                    if(workflowType=="VNF Scale Out") {
                        //push vnf to related as well as the service instance
                        var relatedInstance = {
                            instanceId: vnf.id,
                            modelInfo: {
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
				}catch(err){
					$log.error('SchedulerCtrl::extractChangeManagementCallbackDataStr error: ' + err);
				}

				result.requestDetails.push(data);
			});
			return JSON.stringify(result);
		}
		
        vm.openModal = function () {
            if(vm.hasScheduler) { //scheduling supported
				$scope.widgetParameter = ""; // needed by the scheduler?

				// properties needed by the scheduler so it knows whether to show
				// policy or sniro related features on the scheduler UI or not.
				vm.changeManagement.policyYN = "Y";
				vm.changeManagement.sniroYN = "Y";

				var data = {
					widgetName: 'Portal-Common-Scheduler',
					widgetData: vm.changeManagement,
					widgetParameter: $scope.widgetParameter
				};
			
				window.parent.postMessage(data, VIDCONFIGURATION.SCHEDULER_PORTAL_URL);
			} else {
				//no scheduling support
				var dataToSo = extractChangeManagementCallbackDataStr(vm.changeManagement);
                if(dataToSo) {

                    if(vm.changeManagement.workflow==="VNF Scale Out") {
                        dataToSo = JSON.parse(dataToSo);
                        dataToSo = {requestDetails: dataToSo.requestDetails[0]};
                        changeManagementService.postChangeManagementScaleOutNow(dataToSo, vm.changeManagement.vnfNames[0]["service-instance-node"][0].properties["service-instance-id"], vm.changeManagement.vnfNames[0].id);
                    }else{
                        //TODO: foreach
                        var vnfName = vm.changeManagement.vnfNames[0].name;
                        changeManagementService.postChangeManagementNow(dataToSo, vnfName);
                    }
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

        vm.isConfigUpdate = function () {
            return vm.changeManagement.workflow === COMPONENT.WORKFLOWS.vnfConfigUpdate;
        }
		
        vm.isScaleOut = function () {
            return vm.changeManagement.workflow === COMPONENT.WORKFLOWS.vnfScaleOut;
        }

        vm.shouldShowVnfInPlaceFields = function () {
            return vm.changeManagement.workflow === COMPONENT.WORKFLOWS.vnfInPlace;
        };

        vm.setPreload = function (fileEl) {
            var files = fileEl.files;
            var file = files[0];
            var reader = new FileReader();

            reader.onloadend = function(evt) {
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