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

package org.onap.vid.services;


import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.properties.Features;
import org.springframework.beans.factory.annotation.Autowired;
import org.togglz.core.manager.FeatureManager;

import java.util.List;
import java.util.Map;

public class CloudOwnerServiceImpl implements CloudOwnerService {

    private static final List<String> CLOUD_CONFIGURATION_PATH = ImmutableList.of("requestDetails", "cloudConfiguration");
    private static final List<String> LCP_CLOUD_REGION_ID_PATH = ImmutableList.of("requestDetails", "cloudConfiguration", "lcpCloudRegionId");

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(CloudOwnerService.class);

    private final AaiClientInterface aaiClient;
    private final FeatureManager featureManager;

    @Autowired
    public CloudOwnerServiceImpl(AaiClientInterface aaiClient, FeatureManager featureManager) {
        this.aaiClient = aaiClient;
        this.featureManager = featureManager;
    }

    @Override
    public void enrichRequestWithCloudOwner(RequestDetails msoRequest) {
        if (!featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)) {
            return;
        }
        try {

            //if cloudConfiguration field contains lcpRegion (e.g. in changeManagement scenarios)
            if (msoRequest.getCloudConfiguration()!=null && StringUtils.isNotEmpty(msoRequest.getCloudConfiguration().getLcpCloudRegionId())) {
                enrichCloudConfigurationWithCloudOwner(msoRequest.getCloudConfiguration(), msoRequest.getCloudConfiguration().getLcpCloudRegionId());
            }
            //otherwise the cloudConfiguration is in the additionalProperties field of RequestDetails (e.g. in ng1 view/edit scenario)
            else {
                enrichRequestWithCloudOwnerByAdditionalProperties(msoRequest);
            }
        }
        catch (Exception e) {
            throw new GenericUncheckedException("Failed to enrich requestDetails with cloudOwner", e);
        }
    }

    protected void enrichRequestWithCloudOwnerByAdditionalProperties(RequestDetails msoRequest) {
        String lcpCloudRegionId = null;
        try {
            lcpCloudRegionId = msoRequest.extractValueByPathUsingAdditionalProperties(LCP_CLOUD_REGION_ID_PATH, String.class);
        }
        catch (NotFoundException exception) {
            LOGGER.debug("Can't find lcp region in RequestDetails. Assume no cloudOwner enrichment is needed. Reason: "+exception.getMessage());
            return;
        }
        String cloudOwner = aaiClient.getCloudOwnerByCloudRegionId(lcpCloudRegionId);
        msoRequest.extractValueByPathUsingAdditionalProperties(CLOUD_CONFIGURATION_PATH, Map.class).put("cloudOwner", cloudOwner);
    }

    @Override
    public void enrichCloudConfigurationWithCloudOwner(CloudConfiguration cloudConfiguration, String lcpCloudRegionId) {
        if (featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)) {
            String cloudOwner = aaiClient.getCloudOwnerByCloudRegionId(lcpCloudRegionId);
            cloudConfiguration.setCloudOwner(cloudOwner);
        }
    }
}
