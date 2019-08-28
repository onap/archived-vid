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

var vfModuleActionModalController = function(COMPONENT, FIELD, $scope, $uibModal, CreationService,
    MsoService, AaiService, DeleteResumeService, DataService, $uibModalInstance, action, vfModule, featureFlags) {


    var _this = this;

    $scope.action = action;
    $scope.vfModuleName = vfModule.name;
    $scope.volumeGroups = vfModule.volumeGroups;
    $scope.lcpAndTenant = null;
    $scope.regionSelection = {optionId: null, legacyRegion: null, tenant: null};
    $scope.lcpRegionList = null;
    $scope.isHomingData = false;
    $scope.megaRegion = ['AAIAIC25'];
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
            DataService.setCloudRegionTenantList(response);
            $scope.lcpAndTenant = response;
            $scope.isFeatureFlagCloudOwner = featureFlags.isOn(COMPONENT.FEATURE_FLAGS.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST);
            $scope.lcpRegionList = _.uniqBy(response, 'cloudRegionOptionId');
            $scope.vendorInCloudOwnerRegex = /^[^-]*-/;
        });
    }

    function initHomingData() {
        AaiService.getHomingData(DataService.getVnfInstanceId(), DataService.getVfModuleInstanceId())
            .then(function (res) {
                if (res && res.data) {
                    $scope.regionSelection.optionId = (res.data.cloudRegionOptionId);
                    $scope.regionSelection.tenant = (res.data[COMPONENT.TENANT_ID]) ? res.data[COMPONENT.TENANT_ID] : null;
                    $scope.isHomingData = $scope.regionSelection.optionId !== null && $scope.regionSelection.tenant !== null;
                    $scope.isHomingData = $scope.isHomingData && (($scope.megaRegion).indexOf(res.data[COMPONENT.CLOUD_REGION_ID]) === -1);
                }

                getLcpCloudRegionTenantList();
            })
            .catch(function (error) {
                getLcpCloudRegionTenantList();
            });
    }

    function getLcpRegionId()  {
        if(_.isEmpty($scope.regionSelection.legacyRegion)) {
            return DataService.getCloudOwnerAndLcpCloudRegionFromOptionId($scope.regionSelection.optionId).cloudRegionId;
        }
        return $scope.regionSelection.legacyRegion;
    }

    $scope.deleteOrResume = function()  {

        var msoParameterList = [({id: "lcpRegion", value: $scope.regionSelection.optionId})];
        msoParameterList.push({id: "tenant", value: $scope.regionSelection.tenant});

        var requestParams = {};
        var requestDetails;
        var msoType;
        if ($scope.isResumeEnabled)  {
            CreationService.initializeComponent(COMPONENT.VF_MODULE);
            CreationService.setInventoryInfo();

            var availableVolumeGroupList = $scope.volumeGroups;

            if (availableVolumeGroupList && availableVolumeGroupList.length > 0) {
                var volumeGroupList = FIELD.PARAMETER.AVAILABLE_VOLUME_GROUP;
                volumeGroupList.value = _.map(availableVolumeGroupList, function (volumeGroup) {
                    return volumeGroup.name;
                });
                msoParameterList.push(volumeGroupList);
            }

            requestDetails = CreationService.getMsoRequestDetails(msoParameterList);
            requestParams.url = CreationService.getMsoUrl();
            msoType = COMPONENT.MSO_CREATE_REQ;
        }
        else {
            DeleteResumeService.initializeComponent(COMPONENT.VF_MODULE);

            requestDetails = DeleteResumeService.getMsoRequestDetails(msoParameterList);
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

    $scope.removeVendorFromCloudOwner = function(cloudOwner) {
        return AaiService.removeVendorFromCloudOwner(cloudOwner)
    };

    $scope.selectedLcpRegionIsMegaRegion = function() {
        if ($scope.regionSelection.optionId) {
            let cloudRegionId = DataService.getCloudOwnerAndLcpCloudRegionFromOptionId($scope.regionSelection.optionId).cloudRegionId;
            return ($scope.megaRegion).indexOf(cloudRegionId) > -1
        } else {
            return false;
        }
    };

    $scope.cancel = function() {
        $uibModalInstance.dismiss('cancel');
    };

};

appDS2.controller("vfModuleActionModalController", [ "COMPONENT", "FIELD", "$scope", "$uibModal", "CreationService",
     "MsoService", "AaiService", "DeleteResumeService", "DataService", "$uibModalInstance", "action", "vfModule", "featureFlags",
    vfModuleActionModalController ]);
