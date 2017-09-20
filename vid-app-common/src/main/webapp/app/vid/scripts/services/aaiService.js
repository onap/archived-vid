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

"use strict";

var AaiService = function ($http, $log, PropertyService, UtilityService, COMPONENT, FIELD) {
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
                var subName = "";
                if (response.data) {
                    subName = response.data[FIELD.ID.SUBNAME];
                }
                successCallbackFunction(subName);
            })["catch"]
            (UtilityService.runHttpErrorHandler);
        },


        runNamedQuery: function (namedQueryId, globalCustomerId, serviceType, serviceInstanceId, successCallback, errorCallback) {

            var url = COMPONENT.AAI_SUB_VIEWEDIT_PATH +
                COMPONENT.FORWARD_SLASH + encodeURIComponent(namedQueryId) +
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
                                    if ((instValue[FIELD.ID.SERVICE_INSTANCE_ID] == selectedServiceInstance ) || (instValue[FIELD.ID.SERVICE_INSTANCE_NAME] == selectedServiceInstance)) {
                                        serviceInstanceId.push(inst);
                                    }
                                } else {
                                    serviceInstanceId.push(inst);
                                }
                            });
                        } else {
                            if (serviceInstanceId == []) {
                                serviceInstanceId = [FIELD.PROMPT.NO_SERVICE_INSTANCE];
                            }
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
                        subscriberName: selectedSubscriberName,
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

        getServices2: function (successCallback, errorCallback) {

            $http.get(FIELD.ID.AAI_GET_SERVICES, {}, {
                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).then(function (response) {
                var customerList = [];
                if (response.data != null) {
                    var serviceIdList = [];
                    angular.forEach(response.data, function (value, key) {
                        angular.forEach(value, function (subVal, key) {
                            var newVal = {
                                "id": subVal[FIELD.ID.SERVICE_ID],
                                "description": subVal[FIELD.ID.SERVICE_DESCRIPTION],
                                "isPermitted" : subVal[FIELD.ID.IS_PERMITTED]

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

        getSubscriptionServiceTypeList: function (globalCustomerId,
                                                  successCallbackFunction) {
            $log
                .debug("AaiService:getSubscriptionServiceTypeList: globalCustomerId: "
                    + globalCustomerId);
            if (UtilityService.hasContents(globalCustomerId)) {
                $http.get(
                    COMPONENT.AAI_SUB_DETAILS_PATH
                    + globalCustomerId + COMPONENT.ASSIGN + Math.random(),
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

                lcpCloudRegionTenants.push({
                    "cloudRegionId": "",
                    "tenantName": FIELD.PROMPT.REGION,
                    "tenantId": ""
                });

                for (var i = 0; i < aaiLcpCloudRegionTenants.length; i++) {
                    lcpCloudRegionTenants.push({
                        "cloudRegionId": aaiLcpCloudRegionTenants[i][COMPONENT.CLOUD_REGION_ID],
                        "tenantName": aaiLcpCloudRegionTenants[i][COMPONENT.TENANT_NAME],
                        "tenantId": aaiLcpCloudRegionTenants[i][COMPONENT.TENANT_ID],
                        "isPermitted": aaiLcpCloudRegionTenants[i][COMPONENT.IS_PERMITTED]
                    });
                }

                successCallbackFunction(lcpCloudRegionTenants);
            })["catch"]
            (UtilityService.runHttpErrorHandler);
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
        getLoggedInUserID: function (successCallbackFunction) {
            $log
                .debug("AaiService:getLoggedInUserID");
            var url = COMPONENT.GET_USER_ID;

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
        }
    }
}

appDS2.factory("AaiService", ["$http", "$log", "PropertyService",
    "UtilityService", "COMPONENT", "FIELD", AaiService]);
