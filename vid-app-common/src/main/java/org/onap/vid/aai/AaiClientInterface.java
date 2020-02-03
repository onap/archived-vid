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

package org.onap.vid.aai;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.aai.model.OwningEntityResponse;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.model.Properties;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.services.ProbeInterface;
import org.springframework.http.HttpMethod;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiClientInterface extends ProbeInterface {

    boolean isNodeTypeExistsByName(String name, ResourceType type);

    <T> T typedAaiGet(URI path, Class<T> clz);

    <T> T typedAaiRest(URI path, Class<T> clz, String payload, HttpMethod method, boolean propagateExceptions);

    AaiResponse<SubscriberList> getAllSubscribers();

    AaiResponse<Services> getSubscriberData(String subscriberId, boolean omitServiceInstances);

    AaiResponse getServices();

    AaiResponse<OwningEntityResponse> getServicesByOwningEntityId(List<String> owningEntityIds);

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType);

    AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus);

    AaiResponse getAllAicZones();

    AaiResponse getNetworkCollectionDetails(String serviceInstanceId);

    AaiResponse getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction);

    AaiResponse<AaiGetVnfResponse> getVNFData(String globalSubscriberId, String serviceType);

    AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId);

    AaiResponse getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion);

    Response getVersionByInvariantId(List<String> modelInvariantId);

    ModelVer getLatestVersionByInvariantId(String modelInvariantId);

    AaiResponse getServicesByProjectNames(List<String> projectNames);

    AaiResponse getServiceModelsByDistributionStatus();

    AaiResponse getPNFData(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel);

    AaiResponse<Pnf> getSpecificPnf(String pnfId);

    AaiResponse getServiceInstance(String globalCustomerId, String serviceType, String serviceInstanceId);

    AaiResponse getLogicalLink(String link);

    AaiResponse<JsonNode> getCloudRegionAndSourceByPortMirroringConfigurationId(String configurationId);

    List<PortDetailsTranslator.PortDetails> getPortMirroringSourcePorts(String configurationID);

    AaiResponse getInstanceGroupsByVnfInstanceId(String vnfInstanceId);

    Response doAaiGet(String uri, boolean xml);

    String getCloudOwnerByCloudRegionId(String cloudRegionId);

    GetTenantsResponse getHomingDataByVfModule(String vnfInstanceId, String vfModuleId);

    void resetCache(String cacheName);

    Map<String, Properties> getCloudRegionAndTenantByVnfId(String vnfId);

    AaiResponse<AaiGetVnfResponse> getVnfsByParamsForChangeManagement(String subscriberId, String serviceType, String nfRole, String cloudRegion);
}
