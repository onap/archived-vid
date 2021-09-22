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

"use strict";

var AaiService = function ($http, $log, PropertyService, UtilityService, COMPONENT, FIELD, $q, featureFlags) {

    function getServiceInstance(serviceInstanceIdentifier, findBy) {
        serviceInstanceIdentifier.trim();

        return $http.get(COMPONENT.AAI_GET_SERVICE_INSTANCE_PATH + serviceInstanceIdentifier + "/" + findBy + "?r=" + Math.random(), {}, {
            timeout: PropertyService.getServerResponseTimeoutMsec()
        });
    }

    function getPnfByName(pnfName) {
        var deferred = $q.defer();
        var url = COMPONENT.AAI_GET_PNF_BY_NAME + encodeURIComponent(pnfName);
        var config = {timeout: PropertyService.getServerResponseTimeoutMsec()};

        $http.get(url, config)
            .success(function (response) {
                deferred.resolve({data: response});
            })
            .error(function (data, status, headers, config) {
                deferred.reject({message: data, status: status});
            });

        return deferred.promise;
    }

    function getGlobalCustomerIdFromServiceInstanceResponse(response) {
        var globalCustomerId = "";
        if (angular.isArray(response.data[FIELD.ID.RESULT_DATA])) {
            var customerIndex = 5;
            var customerIdIndex = 6;
            var itemIndex = 0;

            var item = response.data[FIELD.ID.RESULT_DATA][itemIndex];
            var url = item[FIELD.ID.RESOURCE_LINK];
            var urlParts = url.split("/");
            if (urlParts[customerIndex] === FIELD.ID.CUSTOMER) {
                globalCustomerId = urlParts[customerIdIndex];
            }
        }
        return globalCustomerId;
    }

    function searchServiceInstances(query) {
        return $http.get(COMPONENT.SEARCH_SERVICE_INSTANCES + query, {}, {
            timeout: PropertyService.getServerResponseTimeoutMsec()
        }).then(function (response) {
            var displayData = response.data[FIELD.ID.SERVICE_INSTANCES];
            if (!displayData || !displayData.length) {
                displayData = [{
                    globalCustomerId: null,
                    subscriberName: null,
                    serviceType: FIELD.PROMPT.NO_SERVICE_SUB,
                    serviceInstanceId: FIELD.PROMPT.NO_SERVICE_INSTANCE
                }];
            }
            return {displayData: displayData};
        });
    };

    function getJoinedQueryString(queries) {
        return queries.filter(function (val) {
            return val;
        }).join("&");
    }

    function getConfigParams(nfRole, cloudRegion) {
        if (!featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH)) {
            return null
        }

        let data = {
            nfRole: nfRole,
            cloudRegion: cloudRegion,
        };

        let config = {
            params: data
        };

        return config;
    }

    return {
        getSubscriberName: function (globalCustomerId,
                                     successCallbackFunction) {
            $log
                .debug("AaiService:getSubscriberName: globalCustomerId: "
                    + globalCustomerId);
            $http.get(
                COMPONENT.AAI_SUB_DETAILS_PATH
                + globalCustomerId + COMPONENT.ASSIGN + Math.random(),
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                var result = {};
                if (response.data) {
                    result.subscriberName = response.data[FIELD.ID.SUBNAME];
                    result.serviceSubscriptions = response.data[FIELD.ID.SERVICE_SUBSCRIPTIONS];
                }
                successCallbackFunction(result);
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },
        getSubscriberNameAndServiceInstanceInfo: function (subscriberUuid, serviceInstanceIdentifier,
            serviceInstanceIdentifierType,successCallbackFunction ) {
            $log.debug("AaiService:getSubscriberNameAndServiceInstanceInfo: subscriberUuid: " + subscriberUuid +
                ",serviceInstanceIdentifier :"+serviceInstanceIdentifier +
                " , serviceInstanceIdentifierType="+serviceInstanceIdentifierType);

            if (UtilityService.hasContents(subscriberUuid) && UtilityService.hasContents(serviceInstanceIdentifier) &&
                UtilityService.hasContents(serviceInstanceIdentifierType) ) {
                $http.get(COMPONENT.AAI_SUB_DETAILS_SERVICE_INSTANCE_PATH +
                    subscriberUuid + COMPONENT.FORWARD_SLASH + serviceInstanceIdentifier +  COMPONENT.FORWARD_SLASH +
                    serviceInstanceIdentifierType + COMPONENT.ASSIGN + Math.random(),
                    {
                        timeout: PropertyService.getServerResponseTimeoutMsec()
                    }).then(function (response) {
                    var result = {};
                    if (response.data) {
                        result = response.data;
                    }
                    successCallbackFunction(result);
                })["catch"]
                (UtilityService.runHttpErrorHandler);
            }},
        runNamedQuery: function (namedQueryId, globalCustomerId, serviceType, serviceInstanceId, successCallback, errorCallback) {

            var url = COMPONENT.AAI_SUB_VIEWEDIT_PATH +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(namedQueryId) +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(globalCustomerId) +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(serviceType) +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(serviceInstanceId);
            return $http.get(url, {}, {


                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                if (response.data != null) {
                    successCallback(response);
                } else {
                    errorCallback(response);
                }
            }, function (response) {
                errorCallback(response);
            });
        },


        getVNFInformationByServiceTypeAndId: function (globalCustomerId, serviceType, serviceInstanceId, successCallback, errorCallback) {

            var url = COMPONENT.AAI_GET_VNF_INFO +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(globalCustomerId) +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(serviceType) +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(serviceInstanceId);
            $http.get(url, {}, {
                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                if (response.data != null) {
                    successCallback(response);
                } else {
                    errorCallback(response);
                }
            }, function (response) {
                errorCallback(response);
            });
        },

        getPNFInformationByServiceTypeAndId: function (globalCustomerId, serviceType, serviceInstanceId, successCallback, errorCallback) {

            var url = COMPONENT.AAI_GET_PNF_INSTANCE +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(globalCustomerId) +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(serviceType) +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(serviceInstanceId);
            $http.get(url, {}, {
                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                if (response.data != null) {
                    successCallback(response);
                } else {
                    errorCallback(response);
                }
            }, function (response) {
                errorCallback(response);
            });
        },

        getCRInformationByInstanceId: function (serviceInstanceId) {

            var deferred = $q.defer();

            var url = COMPONENT.AAI_GET_CR_INSTANCE +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(serviceInstanceId);
            $http.get(url, {}, {
                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                if (response.data != null) {
                    deferred.resolve(response);
                } else {
                    deferred.resolve(response);
                }
            }, function (response) {
                deferred.resolve(response);
            });
            return deferred.promise;
        },

        searchServiceInstances: searchServiceInstances,

        getModelVersionId: function (subscriberId, instanceId, identifierType) {
            var globalCustomerIdQuery = COMPONENT.SELECTED_SUBSCRIBER_SUB_PATH + subscriberId;
            var serviceInstanceQuery = COMPONENT.SELECTED_SERVICE_INSTANCE_SUB_PATH + instanceId;
            var serviceIdentifierType = COMPONENT.SELECTED_SERVICE_INSTANCE_TYPE_SUB_PATH + identifierType;
            var query = "?" + getJoinedQueryString([globalCustomerIdQuery, serviceInstanceQuery, serviceIdentifierType]);

            var deferred = $q.defer();

            searchServiceInstances(query).then(function (response) {
                var displayData = response.displayData;
                if (displayData[0] && displayData[0].aaiModelVersionId) {
                    deferred.resolve(displayData[0].aaiModelVersionId);
                } else {
                    deferred.reject(FIELD.ERROR.MODEL_VERSION_ID_MISSING);
                }
            }).catch(function (err) {
                deferred.reject(err);
            });

            return deferred.promise;
        },

        getSubDetails: function (selectedSubscriber, selectedServiceInstance, successCallback, errorCallback) {
            var subscriber;
            var displayData;
            $http.get(COMPONENT.AAI_SUB_DETAILS_PATH + selectedSubscriber, {}, {


                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                displayData = [];
                subscriber = response.data;
                var subscriberName = subscriber[FIELD.ID.SUBNAME];
                if (subscriber[FIELD.ID.SERVICE_SUBSCRIPTIONS] != null) {
                    angular.forEach(subscriber[FIELD.ID.SERVICE_SUBSCRIPTIONS][FIELD.ID.SERVICE_SUBSCRIPTION], function (serviceSubscription, key) {
                        var serviceInstanceId = [];
                        var serviceType = "";
                        if (serviceSubscription[FIELD.ID.SERVICETYPE] != null) {
                            serviceType = serviceSubscription[FIELD.ID.SERVICETYPE];
                        } else {
                            serviceType = FIELD.PROMPT.NO_SERVICE_SUB;
                        }
                        if (serviceSubscription[FIELD.ID.SERVICE_INSTANCES] != null) {
                            angular.forEach(serviceSubscription[FIELD.ID.SERVICE_INSTANCES][FIELD.ID.SERVICE_INSTANCE], function (instValue, instKey) {
                                // put them together, i guess
                                var inst = {
                                    "serviceInstanceId": instValue[FIELD.ID.SERVICE_INSTANCE_ID],
                                    "aaiModelInvariantId": instValue[FIELD.ID.MODEL_INVAR_ID],
                                    "aaiModelVersionId": instValue[FIELD.ID.MODEL_VERSION_ID],
                                    "serviceInstanceName": instValue[FIELD.ID.SERVICE_INSTANCE_NAME]
                                };
                                if (selectedServiceInstance != null) {
                                    if ((instValue[FIELD.ID.SERVICE_INSTANCE_ID] == selectedServiceInstance) || (instValue[FIELD.ID.SERVICE_INSTANCE_NAME] == selectedServiceInstance)) {
                                        serviceInstanceId.push(inst);
                                    }
                                } else {
                                    serviceInstanceId.push(inst);
                                }
                            });
                        } else {
                            serviceInstanceId = [FIELD.PROMPT.NO_SERVICE_INSTANCE];
                        }
                        angular.forEach(serviceInstanceId, function (subVal, subKey) {
                            displayData.push({
                                globalCustomerId: selectedSubscriber,
                                subscriberName: subscriberName,
                                serviceType: serviceType,
                                serviceInstanceId: subVal.serviceInstanceId,
                                aaiModelInvariantId: subVal.aaiModelInvariantId,
                                aaiModelVersionId: subVal.aaiModelVersionId,
                                serviceInstanceName: subVal.serviceInstanceName,
                                isPermitted: serviceSubscription[FIELD.ID.IS_PERMITTED]
                            });
                        });
                    });
                } else {
                    displayData.push({
                        globalCustomerId: selectedSubscriber,
                        subscriberName: subscriberName,
                        serviceType: FIELD.PROMPT.NO_SERVICE_SUB,
                        serviceInstanceId: FIELD.PROMPT.NO_SERVICE_INSTANCE
                    });
                }
                successCallback(displayData, subscriberName);
            }, function (response) {
                errorCallback(response);
            });
        },

        getSubList: function (successCallback, errorCallback) {

            $http.get(FIELD.ID.AAI_GET_FULL_SUBSCRIBERS, {}, {


                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                var customerList = [];
                if (response.data.customer != null) {
                    angular.forEach(response.data.customer, function (subVal, subKey) {
                        var cust = {
                            "globalCustomerId": subVal[FIELD.ID.GLOBAL_CUSTOMER_ID],
                            "subscriberName": subVal[FIELD.ID.SUBNAME],
                            "isPermitted": subVal[FIELD.ID.IS_PERMITTED],
                        };
                        customerList.push(cust);
                    });
                    successCallback(customerList);
                } else {
                    errorCallback(response);
                }
            }, function (response) {
                errorCallback(response);
            });
        },

        getServiceInstance: getServiceInstance,
        getPnfByName: getPnfByName,

        getGlobalCustomerIdByInstanceIdentifier: function (serviceInstanceIdentifier, findBy) {
            serviceInstanceIdentifier.trim();

            return getServiceInstance(serviceInstanceIdentifier, findBy)
                .then(function (response) {
                    return getGlobalCustomerIdFromServiceInstanceResponse(response);
                });
        },

        getMultipleValueParamQueryString: function (values, paramSubPath) {
            if (values.length) {
                return paramSubPath + values.filter(function (val) {
                    return val;
                }).join("&" + paramSubPath);
            }
        },

        getJoinedQueryString: getJoinedQueryString,

        getServices2: function (successCallback, errorCallback) {
            $http.get(FIELD.ID.AAI_GET_SERVICES, {}, {
                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                if (response.data != null) {
                    var serviceIdList = [];
                    angular.forEach(response.data, function (value, key) {
                        angular.forEach(value, function (subVal, key) {
                            var newVal = {
                                "id": subVal[FIELD.ID.SERVICE_ID], "description": subVal[FIELD.ID.SERVICE_DESCRIPTION],
                                "isPermitted": subVal[FIELD.ID.IS_PERMITTED]

                            };
                            serviceIdList.push(newVal);
                        });
                    });
                    successCallback(serviceIdList);
                } else {
                    errorCallback(response);
                }
            }, function (response) {
                errorCallback(response);
            });
        },

        getPortMirroringData: function (ids) {
            var defer = $q.defer();

            var url = COMPONENT.AAI_GET_PORT_MIRRORING_CONFIGS_DATA + '?configurationIds=' + ids.join(',');
            $http.get(url).then(function (res) {
                defer.resolve(res);
            }).catch(function (err) {
                $log.error(err);
                defer.resolve({});
            });

            return defer.promise;

        },

        getPortMirroringSourcePorts: function (ids) {
            var defer = $q.defer();
            var url = COMPONENT.AAI_GET_PORT_MIRRORING_SOURCE_PORTS + '?configurationIds=' + ids.join(',');
            $http.get(url).then(function (res) {
                defer.resolve(res);
            }).catch(function (err) {
                $log.error(err);
                defer.resolve({});
            });
            return defer.promise;
        },

        getVlansByNetworksMapping: function (globalCustomerId, serviceType, serviceInstanceId, sdcModelUuid) {
            var defer = $q.defer();
            if (featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS)) {
                var url = COMPONENT.AAI_GET_PROVIDER_NETWORKS_ASSOCIATIONS + '?'
                    + 'globalCustomerId=' + globalCustomerId
                    + '&serviceType=' + serviceType
                    + '&serviceInstanceId=' + serviceInstanceId
                    + '&sdcModelUuid=' + sdcModelUuid
                ;

                $http.get(url).then(function (res) {
                    defer.resolve(res.data);
                }).catch(function (err) {
                    $log.error(err);
                    defer.resolve({});
                });

            } else {
                defer.resolve({});
            }
            return defer.promise;
        },

        getSubscriptionServiceTypeList: function (globalCustomerId,
                                                  successCallbackFunction) {
            $log
                .debug("AaiService:getSubscriptionServiceTypeList: globalCustomerId: "
                    + globalCustomerId);
            if (UtilityService.hasContents(globalCustomerId)) {
                $http.get(
                    COMPONENT.AAI_SUB_DETAILS_PATH
                    + globalCustomerId + COMPONENT.ASSIGN + Math.random() + COMPONENT.AAI_OMIT_SERVICE_INSTANCES + true,
                    {
                        timeout: PropertyService
                            .getServerResponseTimeoutMsec()
                    }).then(function (response) {
                    if (response.data && response.data[FIELD.ID.SERVICE_SUBSCRIPTIONS]) {
                        var serviceTypes = [];
                        var serviceSubscriptions = response.data[FIELD.ID.SERVICE_SUBSCRIPTIONS][FIELD.ID.SERVICE_SUBSCRIPTION];

                        for (var i = 0; i < serviceSubscriptions.length; i++) {
                            serviceTypes.push({
                                "name": serviceSubscriptions[i][FIELD.ID.SERVICETYPE],
                                "isPermitted": serviceSubscriptions[i][FIELD.ID.IS_PERMITTED],
                                "id": i
                            });
                        }
                        successCallbackFunction(serviceTypes);
                    } else {
                        successCallbackFunction([]);
                    }
                })["catch"]
                (UtilityService.runHttpErrorHandler);
            }
        },
        getLcpCloudRegionTenantList: function (globalCustomerId, serviceType,
                                               successCallbackFunction) {
            let self = this;
            $log
                .debug("AaiService:getLcpCloudRegionTenantList: globalCustomerId: "
                    + globalCustomerId);
            var url = COMPONENT.AAI_GET_TENANTS
                + globalCustomerId + COMPONENT.FORWARD_SLASH + serviceType + COMPONENT.ASSIGN + Math.random();

            $http.get(url,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                var lcpCloudRegionTenants = [];
                var aaiLcpCloudRegionTenants = response.data;

                for (var i = 0; i < aaiLcpCloudRegionTenants.length; i++) {
                    var cloudOwner = aaiLcpCloudRegionTenants[i][COMPONENT.CLOUD_OWNER];
                    var cloudRegionId = aaiLcpCloudRegionTenants[i][COMPONENT.CLOUD_REGION_ID];
                    var cloudRegionOptionId = self.cloudRegionOptionId(cloudOwner, cloudRegionId);

                    lcpCloudRegionTenants.push({
                        "cloudOwner": cloudOwner,
                        "cloudRegionId": cloudRegionId,
                        "cloudRegionOptionId": cloudRegionOptionId,
                        "tenantName": aaiLcpCloudRegionTenants[i][COMPONENT.TENANT_NAME],
                        "tenantId": aaiLcpCloudRegionTenants[i][COMPONENT.TENANT_ID],
                        "isPermitted": aaiLcpCloudRegionTenants[i][COMPONENT.IS_PERMITTED]
                    });
                }

                successCallbackFunction(lcpCloudRegionTenants);
            }).catch(function (error) {
                (UtilityService.runHttpErrorHandler(error.data, error.status));
            });
        },
        getSubscribers: function (successCallbackFunction) {
            $log
                .debug("AaiService:getSubscribers");
            var url = FIELD.ID.AAI_GET_SUBSCRIBERS + COMPONENT.ASSIGN + Math.random();

            $http.get(url,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                if (response.data) {
                    successCallbackFunction(response.data.customer);
                } else {
                    successCallbackFunction([]);
                }
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },
        getProvOptionsFromSystemProp: function (successCallbackFunction) {
            $log
                .debug("AaiService:getProvOptionsFromSystemProp");
            var url = COMPONENT.GET_SYSTEM_PROP_VNF_PROV_STATUS_PATH;

            $http.get(url,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                if (response.data) {
                    successCallbackFunction(response);
                } else {
                    successCallbackFunction([]);
                }
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },
        getLoggedInUserID: function (successCallbackFunction, catchCallbackFunction) {
            $log
                .debug("AaiService:getLoggedInUserID");
            var url = COMPONENT.GET_USER_ID;

            $http.get(url,
                {
                    transformResponse: [function (data) {
                        return data;
                    }],
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                if (response.data) {
                    successCallbackFunction(response);
                } else {
                    successCallbackFunction([]);
                }
            })["catch"](function (response, status) {
                if (catchCallbackFunction) {
                    catchCallbackFunction();
                }
                UtilityService.runHttpErrorHandler(response, status);
            });
        },
        getServices: function (successCallbackFunction) {
            $log
                .debug("AaiService:getServices");
            var url = COMPONENT.AAI_GET_SERVICES + COMPONENT.ASSIGN + Math.random();

            $http.get(url,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                if (response.data) {
                    successCallbackFunction(response);
                } else {
                    successCallbackFunction([]);
                }
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },

        getAicZones: function (successCallbackFunction) {
            $log
                .debug("getAicZones:getAicZones");
            var url = COMPONENT.AAI_GET_AIC_ZONES + COMPONENT.ASSIGN + Math.random();

            $http.get(url,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                if (response.data) {
                    successCallbackFunction(response);
                } else {
                    successCallbackFunction([]);
                }
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },
        getAicZoneForPNF: function (globalCustomerId, serviceType, serviceInstanceId, successCallbackFunction) {
            $log
                .debug("getAicZones:getAicZones");
            var url = COMPONENT.AAI_GET_AIC_ZONE_FOR_PNF
                .replace('@serviceInstanceId', serviceInstanceId)
                .replace('@globalCustomerId', globalCustomerId)
                .replace('@serviceType', serviceType);
            $http.get(url,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                successCallbackFunction(response.data);
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },

        getServiceModels: function (globalCustomerId, serviceType, successCallbackFunction) {
            $log
                .debug("AaiService:getServices");
            var url = COMPONENT.AAI_GET_SERVICES + COMPONENT.FORWARD_SLASH + globalCustomerId + COMPONENT.FORWARD_SLASH + serviceType + COMPONENT.ASSIGN + Math.random();

            $http.get(url,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                if (response.data) {
                    successCallbackFunction(response);
                } else {
                    successCallbackFunction([]);
                }
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },
        getServiceModelsByServiceType: function (namedQueryId, globalCustomerId, serviceType, successCallbackFunction) {
            $log
                .debug("AaiService:getServiceModelsByServiceType");
            var url = COMPONENT.AAI_GET_SERVICES_BY_TYPE + COMPONENT.FORWARD_SLASH + namedQueryId + COMPONENT.FORWARD_SLASH + globalCustomerId + COMPONENT.FORWARD_SLASH + serviceType + COMPONENT.ASSIGN + Math.random();

            $http.get(url,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(function (response) {
                if (response.data) {
                    successCallbackFunction(response);
                } else {
                    successCallbackFunction([]);
                }
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },

        getVnfsByCustomerIdAndServiceType: function (globalSubscriberId, serviceType, nfRole, cloudRegion) {
            let deferred = $q.defer();

            let url = globalSubscriberId + COMPONENT.FORWARD_SLASH + serviceType

            const path = COMPONENT.AAI_GET_VNF_BY_CUSTOMERID_AND_SERVICETYPE + url;
            let config = getConfigParams(nfRole, cloudRegion);

            if (UtilityService.hasContents(globalSubscriberId) &&
                UtilityService.hasContents(serviceType)) {

                $http.get(path, config)
                    .success(function (response) {
                        if (response) {
                            deferred.resolve({data: response});
                        } else {
                            deferred.resolve({data: []});
                        }
                    }).error(function (data, status, headers, config) {
                    deferred.reject({message: data, status: status});
                });
            }

            return deferred.promise;
        },

        getVnfVersionsByInvariantId: function (modelInvariantId) {
            var deferred = $q.defer();

            if (UtilityService.hasContents(modelInvariantId)) {
                var body = {"versions": modelInvariantId};
                $http.post((COMPONENT.AAI_GET_VERSION_BY_INVARIANT_ID), body)

                    .success(function (response) {
                        if (response) {
                            deferred.resolve({data: response});
                        } else {
                            deferred.resolve({data: []});
                        }
                    }).error(function (data, status, headers, config) {
                    deferred.reject({message: data, status: status});
                });
            }

            return deferred.promise;
        },


        getSubscriberServiceTypes: function (subscriberUuid) {
            var deferred = $q.defer();
            $log.debug("AaiService:getSubscriberServiceTypes: subscriberUuid: " + subscriberUuid);

            if (UtilityService.hasContents(subscriberUuid)) {
                $http.get(COMPONENT.AAI_SUB_DETAILS_PATH + subscriberUuid + COMPONENT.ASSIGN + Math.random()+ COMPONENT.AAI_OMIT_SERVICE_INSTANCES + true)
                    .success(function (response) {
                        if (response && [FIELD.ID.SERVICE_SUBSCRIPTIONS]) {
                            deferred.resolve({data: response[FIELD.ID.SERVICE_SUBSCRIPTIONS][FIELD.ID.SERVICE_SUBSCRIPTION]});
                        } else {
                            deferred.resolve({data: []});
                        }
                    }).error(function (data, status, headers, config) {
                    deferred.reject({message: data, status: status});
                });
            }

            return deferred.promise;
        },
        getVnfInstancesList: function (globalSubscriberId, serviceType, modelVersionId, modelInvariantId, cloudRegionId) {
            var deferred = $q.defer();
            $http.get([COMPONENT.AAI_GET_VNF_INSTANCES_LIST,
                globalSubscriberId,
                serviceType,
                modelVersionId,
                modelInvariantId,
                cloudRegionId]
                .join(COMPONENT.FORWARD_SLASH))
                .success(function (response) {
                    deferred.resolve(response);
                }).error(function (data, status) {
                deferred.reject({message: data, status: status});
            });
            return deferred.promise;
        },
        getPnfInstancesList: function (globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegionId, equipVendor, equipModel) {
            var deferred = $q.defer();
            $http.get([COMPONENT.AAI_GET_PNF_INSTANCES_LIST,
                globalCustomerId, serviceType,
                modelVersionId, modelInvariantId,
                cloudRegionId,
                equipVendor, equipModel
            ].join(COMPONENT.FORWARD_SLASH))
                .success(function (response) {
                    deferred.resolve(response);
                }).error(function (data, status) {
                deferred.reject({message: data, status: status});
            });
            return deferred.promise;
        },
        getByUri: function (uri) {
            var deferred = $q.defer();

            $http.get(COMPONENT.AAI_GET_BY_URI + uri)
                .success(function (response) {
                    deferred.resolve({data: []});
                }).error(function (data, status, headers, config) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;
        },
        getConfiguration: function (configurationId) {
            var deferred = $q.defer();

            $http.get(COMPONENT.AAI_GET_CONFIGURATION + configurationId)
                .success(function (response) {
                    deferred.resolve({data: []});
                }).error(function (data, status, headers, config) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;
        },

        getInstanceGroupsByVNFInstanceId: function (vnf_instance_id, successCallback, errorCallback) {
            var url = COMPONENT.AAI_GET_INSTANCE_GROUPS_BY_VNF_INSTANCE_ID_PATH + "/" + vnf_instance_id;

            $http.get(url, {}, {
                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                successCallback(response);
            }, function (response) {
                errorCallback(response);
            });
        },

        postPOMBAverificationRequest: function (url, data, config) {
            $http.post(url, data, config)
                .success(function (data, status, headers, config) {
                    //If at some point in the future the result should be handled - this should be the entry point.
                    log.debug("POMBA was called successfully with data: " + data);
                })
                .error(function (data, status, header, config) {
                    log.debug("Error: " +
                        "Data: " + data +
                        "status: " + status +
                        "headers: " + header +
                        "config: " + config);
                });
        },

        cloudRegionOptionId: function (cloudOwner, cloudRegionId) {
            return ('option-' + cloudOwner + '-' + cloudRegionId).toLowerCase();
        },

        getHomingData: function (vnfInstanceId, vfModuleId) {
            let self = this;
            var url = COMPONENT.AAI_GET_HOMING_DATA.replace('@vnfInstanceId', vnfInstanceId)
                .replace('@vfModuleId', vfModuleId);

            var deferred = $q.defer();

            $http.get(url)
                .success(function (response) {
                    var cloudOwner = response[COMPONENT.CLOUD_OWNER];
                    var cloudRegionId = response[COMPONENT.CLOUD_REGION_ID];
                    if (cloudOwner && cloudRegionId) {
                        response["cloudRegionOptionId"] = self.cloudRegionOptionId(cloudOwner, cloudRegionId);
                    } else {
                        response["cloudRegionOptionId"] = null;
                    }

                    deferred.resolve({data: response});
                }).error(function (data, status, headers, config) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;

        },

        removeVendorFromCloudOwner: function (cloudOwner) {
            // Handle the case where cloud owner is formatted
            // like "{vendor}-{cloud-name}"
            return cloudOwner.trim().replace(/^[^-]*-/, '');
        }
    };
};

appDS2.factory("AaiService", ["$http", "$log", "PropertyService",
    "UtilityService", "COMPONENT", "FIELD", "$q", "featureFlags", AaiService]);
