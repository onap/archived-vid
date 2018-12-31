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

var vfModuleActionModalController = function(COMPONENT, $scope, $uibModal, CreationService,
    MsoService, AaiService, DeleteResumeService, DataService, $uibModalInstance, action, vfModule, featureFlags) {


    var _this = this;

    $scope.action = action;
    $scope.vfModuleName = vfModule.name;
    $scope.lcpAndTenant = null;
    $scope.regionSelection = {lcpRegion: null, legacyRegion: null, tenant: null};
    $scope.lcpRegionList = null;
    $scope.isHomingData = false;
    $scope.megaRegion = ['AAIAIC25', 'rdm3', 'rdm5a'];
    $scope.isSoftDeleteEnabled = vfModule.nodeStatus.toLowerCase() !== 'assigned' && action === COMPONENT.DELETE;

    $scope.isResumeEnabled = action === COMPONENT.RESUME;
    if ($scope.isResumeEnabled) {
        $scope.action = 'Instantiate';
    }

    initHomingData();

    function getLcpCloudRegionTenantList() {
        AaiService.getLcpCloudRegionTenantList(DataService
            .getGlobalCustomerId(), DataService.getServiceType(), function(
            response) {
            $scope.lcpAndTenant = response;
            $scope.isFeatureFlagCloudOwner = featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST);
            $scope.lcpRegionList = _.uniqBy(response, 'cloudRegionId');
        });
    }

    function initHomingData() {
        AaiService.getHomingData(DataService.getVnfInstanceId(), DataService.getVfModuleInstanceId())
            .then(function (res) {
                if (res && res.data) {
                    $scope.regionSelection = {
                        lcpRegion: (res.data[COMPONENT.CLOUD_REGION_ID]) ? res.data[COMPONENT.CLOUD_REGION_ID] : null,
                        legacyRegion: null,
                        tenant: (res.data[COMPONENT.TENANT_ID]) ? res.data[COMPONENT.TENANT_ID] : null
                    };
                    $scope.isHomingData = $scope.regionSelection.lcpRegion !== null && res.data.tenant !== null;
                    $scope.isHomingData = $scope.isHomingData && (($scope.megaRegion).indexOf($scope.regionSelection.lcpRegion) === -1);
                }

                if (!$scope.isHomingData) {
                    getLcpCloudRegionTenantList();
                }
            })
            .catch(function (error) {
                getLcpCloudRegionTenantList();
            });
    };

    function getLcpRegionId()  {
        if(_.isEmpty($scope.regionSelection.legacyRegion)) {
            return $scope.regionSelection.lcpRegion
        }
        return $scope.regionSelection.legacyRegion;
    }

    $scope.deleteOrResume = function()  {

        var regionSelectionList = [({id: "lcpRegion", value: getLcpRegionId()})];
        regionSelectionList.push({id: "tenant", value: $scope.regionSelection.tenant});

        var requestParams = {};
        var requestDetails;
        var msoType;
        if ($scope.isResumeEnabled)  {
            CreationService.initializeComponent(COMPONENT.VF_MODULE);
            CreationService.setInventoryInfo();

            requestDetails = CreationService.getMsoRequestDetails(regionSelectionList);
            requestParams.url = CreationService.getMsoUrl();
            msoType = COMPONENT.MSO_CREATE_REQ;
        }
        else {
            DeleteResumeService.initializeComponent(COMPONENT.VF_MODULE);

            requestDetails = DeleteResumeService.getMsoRequestDetails(regionSelectionList);
            if(DeleteResumeService.isMacro === true)  {
                requestDetails.requestParameters.aLaCarte = false;
            }
            requestParams.url = DeleteResumeService.getMsoUrl();
            msoType = COMPONENT.MSO_DELETE_REQ;
        }

        requestParams.requestDetails = requestDetails;
        requestParams.userId = DataService.getLoggedInUserId();
        $uibModalInstance.close({requestParams: requestParams, msoType: msoType});
    };

    $scope.softDelete = function()  {

        var requestParams = {
            tenantId: $scope.regionSelection.tenant,
            lcpCloudRegionId: getLcpRegionId(),
            serviceInstanceId: DataService.getServiceInstanceId(),
            vnfInstanceId: DataService.getVnfInstanceId(),
            vfModuleInstanceId: DataService.getVfModuleInstanceId()
        };

        requestParams.userId = DataService.getLoggedInUserId();
        $uibModalInstance.close({requestParams : requestParams, msoType: COMPONENT.MSO_DEACTIVATE_AND_CLOUD_DELETE});
    };

    $scope.cancel = function() {
        $uibModalInstance.dismiss('cancel');
    };

};

appDS2.controller("vfModuleActionModalController", [ "COMPONENT", "$scope", "$uibModal", "CreationService",
     "MsoService", "AaiService", "DeleteResumeService", "DataService", "$uibModalInstance", "action", "vfModule", "featureFlags",
    vfModuleActionModalController ]);
