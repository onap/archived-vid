/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.Response;
import org.onap.vid.aai.AaiGetVnfResponse;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.SubscriberFilteredResults;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.aaiTree.Network;
import org.onap.vid.model.aaiTree.RelatedVnf;
import org.onap.vid.model.aaiTree.VpnBinding;
import org.onap.vid.roles.RoleValidator;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiService {


    SubscriberFilteredResults getFullSubscriberList(RoleValidator roleValidator);

    AaiResponse getSubscriberData(String subscriberId, RoleValidator roleValidator, boolean omitServiceInstances);

    AaiResponse getServiceInstanceSearchResults(String subscriberId, String instanceIdentifier, RoleValidator roleProvider, List<String> owningEntities, List<String> projects);

    AaiResponse<SubscriberList> getFullSubscriberList();

    AaiResponse getServices(RoleValidator roleValidator);
    
    AaiResponse getAaiZones();

    AaiResponse getNetworkCollectionDetails(String serviceInstanceId);

    AaiResponse<AaiGetInstanceGroupsByCloudRegion> getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction);

    AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus);

    AaiResponse getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId);

    AaiResponse<AaiGetVnfResponse> getVNFData(String globalSubscriberId, String serviceType);

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType, RoleValidator roleValidator);

    AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId);

    AaiResponse getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion);

    Response getVersionByInvariantId(List<String> modelInvariantId);

    Collection<Service> getServicesByDistributionStatus();

    AaiResponse<Pnf> getSpecificPnf(String pnfId);

    List<String> getServiceInstanceAssociatedPnfs(String globalCustomerId, String serviceType, String serviceInstanceId);

    AaiResponse getPNFData(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel);

    AaiResponseTranslator.PortMirroringConfigData getPortMirroringConfigData(String configurationId);

    List<PortDetailsTranslator.PortDetails> getPortMirroringSourcePorts(String configurationId);

    AaiResponse getInstanceGroupsByVnfInstanceId(String vnfInstanceId);

    String getAAIServiceTree(String globalCustomerId, String serviceType, String serviceInstanceId);

    GetTenantsResponse getHomingDataByVfModule(String vnfInstanceId, String vfModuleId);

    List<RelatedVnf> searchGroupMembers(String globalCustomerId, String serviceType, String invariantId, String groupType, String groupRole);

    List<VpnBinding> getVpnListByVpnType(String vpnType);

    List<Network> getL3NetworksByCloudRegion(String cloudRegionId, String tenantId, String networkRole);

    ModelVer getNewestModelVersionByInvariantId(String modelInvariantId);

    AaiResponse getServiceInstanceBySubscriberIdAndInstanceIdentifier(String globalCustomerId, String identifierType, String serviceIdentifier);
    AaiResponse getServiceInstanceSearchResultsByIdentifierType(String subscriberId, String instanceIdentifier, String instanceIdentifierType, RoleValidator roleValidator, List<String> owningEntities, List<String> projects);
    AaiResponse getServiceInstanceBySubscriberIdAndSIID(String globalCustomerId, String serviceType, String serviceId);
}
