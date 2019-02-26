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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiGetVnfResponse;
import org.onap.vid.aai.AaiOverTLSClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.VnfResult;
import org.onap.vid.roles.RoleValidator;

public class AaiServiceImplTest {

    private AaiClientInterface aaiClient = mock(AaiClientInterface.class);
    private AaiOverTLSClientInterface aaiSslClient = mock(AaiOverTLSClientInterface.class);
    private AaiResponseTranslator aaiResponseTranslator = mock(AaiResponseTranslator.class);
    private AAITreeNodeBuilder aaiTreeNode = mock(AAITreeNodeBuilder.class);
    private AAIServiceTree aaiServiceTree = mock(AAIServiceTree.class);

    private AaiServiceImpl aaiService = new AaiServiceImpl(
        aaiClient, aaiSslClient, aaiResponseTranslator, aaiTreeNode, aaiServiceTree
    );

    @Test
    public void shouldRetrievePnf() {
        // given
        String globalCustomerId = "global_customer";
        String serviceType = "service_type";
        String modelVersionId = "model_version";
        String modelInvariantId = "model_invariant_id";
        String cloudRegion = "cloud_region";
        String equipVendor = "equip_vendor";
        String equipModel = "equip_model";

        AaiResponse response = mock(AaiResponse.class);
        when(aaiClient.getPNFData(
            globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion, equipVendor, equipModel
        )).thenReturn(response);

        // when
        AaiResponse actual = aaiService.getPNFData(
            globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion, equipVendor, equipModel
        );

        // then
        assertThat(response).isEqualTo(actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRetrieveSpecificPnf() {
        // given
        String pnfId = "some_pnf_id";

        AaiResponse response = mock(AaiResponse.class);
        when(aaiClient.getSpecificPnf(pnfId)).thenReturn(response);

        // when
        AaiResponse actual = aaiService.getSpecificPnf(pnfId);

        // then
        assertThat(response).isEqualTo(actual);
    }

    @Test
    public void shouldRetrieveTenantsByInvariantId() {
        // given
        List<String> modelInvariantId = new ArrayList<>();

        Response response = mock(Response.class);
        when(aaiClient.getVersionByInvariantId(modelInvariantId)).thenReturn(response);

        // when
        Response actual = aaiService.getVersionByInvariantId(modelInvariantId);

        // then
        assertThat(response).isEqualTo(actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRetrieveTenants() {
        // given
        String globalCustomerId = "global_customer";
        String serviceType = "service_type";

        GetTenantsResponse permittedTenant = new GetTenantsResponse(
            "cloud_region", "cloud_owner", "permitted_tenant", "tenant_id", false
        );
        GetTenantsResponse unpermittedTenant = new GetTenantsResponse(
            "cloud_region", "cloud_owner", "unpermitted_tenant", "tenant_id", false
        );

        AaiResponse<GetTenantsResponse[]> response = mock(AaiResponse.class);
        when(response.getT()).thenReturn(new GetTenantsResponse[]{ permittedTenant, unpermittedTenant });
        when(aaiClient.getTenants(globalCustomerId, serviceType)).thenReturn(response);

        RoleValidator roleValidator = mock(RoleValidator.class);
        when(roleValidator.isTenantPermitted(globalCustomerId, serviceType, "permitted_tenant")).thenReturn(true);
        when(roleValidator.isTenantPermitted(globalCustomerId, serviceType, "unpermitted_tenant")).thenReturn(false);

        // when
        AaiResponse actual = aaiService.getTenants(globalCustomerId, serviceType, roleValidator);

        // then
        assertThat(response).isEqualTo(actual);
        assertThat(permittedTenant.isPermitted).isTrue();
        assertThat(unpermittedTenant.isPermitted).isFalse();
    }

    @Test
    public void shouldRetrieveVNFs() {
        // given
        String globalSubscriber = "global_subscriber";
        String serviceType = "service_type";
        String serviceInstanceId = "service_instance";

        AaiResponse response = mock(AaiResponse.class);
        when(aaiClient.getVNFData(globalSubscriber, serviceType, serviceInstanceId)).thenReturn(response);

        // when
        AaiResponse actual = aaiService.getVNFData(globalSubscriber, serviceType, serviceInstanceId);

        // then
        assertThat(response).isEqualTo(actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRetrieveAndFilterVNFsBySubscriberAndServiceType() {
        // given
        String globalSubscriber = "global_subscriber";
        String serviceType = "service_type";

        VnfResult genericVnf = new VnfResult();
        genericVnf.nodeType = "generic-vnf";

        VnfResult serviceInstance = new VnfResult();
        serviceInstance.nodeType = "service-instance";

        VnfResult someVnf = new VnfResult();
        someVnf.nodeType = "some-vnf";

        AaiResponse<AaiGetVnfResponse> response = mock(AaiResponse.class);
        AaiGetVnfResponse vnfs = new AaiGetVnfResponse();
        vnfs.results = Arrays.asList(genericVnf, serviceInstance, someVnf);
        when(response.getT()).thenReturn(vnfs);

        when(aaiClient.getVNFData(globalSubscriber, serviceType)).thenReturn(response);

        // when
        AaiResponse actual = aaiService.getVNFData(globalSubscriber, serviceType);

        // then
        assertThat(response).isEqualTo(actual);
        assertThat(response.getT().results).containsOnly(genericVnf, serviceInstance);
    }
}
