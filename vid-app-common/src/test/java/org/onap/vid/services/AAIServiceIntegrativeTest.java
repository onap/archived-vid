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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import com.google.common.util.concurrent.MoreExecutors;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.ws.rs.core.Response;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.ResponseWithRequestInfo;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.AAITreeConverter;
import org.onap.vid.aai.util.CacheProvider;
import org.onap.vid.aai.util.TestWithAaiClient;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.aaiTree.Network;
import org.onap.vid.model.aaiTree.VpnBinding;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AAIServiceIntegrativeTest extends TestWithAaiClient {

    private AAIRestInterface aaiRestInterface;
    private AaiServiceImpl aaiServiceWithoutMocks;
    private Logging logging = new Logging();

    private AaiServiceImpl createAaiServiceWithoutMocks(AAIRestInterface aaiRestInterface, CacheProvider cacheProvider) {
        AaiClient aaiClient = new AaiClient(aaiRestInterface, null, cacheProvider);
        ExecutorService executorService = MoreExecutors.newDirectExecutorService();
        AAIServiceTree aaiServiceTree = new AAIServiceTree(
                aaiClient,
                new AAITreeNodeBuilder(aaiClient, logging),
                new AAITreeConverter(new ModelUtil()),
                null,
                null,
                null,
                executorService
        );
        return new AaiServiceImpl(aaiClient, null, aaiServiceTree, executorService, logging);
    }

    @BeforeMethod
    public void setUp() {
        aaiRestInterface = mock(AAIRestInterface.class);
        CacheProvider cacheProvider = mock(CacheProvider.class);
        CacheProvider.Cache cache = mock(CacheProvider.Cache.class);
        when(cache.get(any())).thenReturn("ddd");
        when(cacheProvider.aaiClientCacheFor(eq("getCloudOwnerByCloudRegionId"), any())).thenReturn(cache);
        aaiServiceWithoutMocks = createAaiServiceWithoutMocks(aaiRestInterface, cacheProvider);
    }

    @Test
    public void getVpnListTest_successResponse() {

        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.OK,
                "{" +
                        "    \"vpn-binding\": [" +
                        "        {" +
                        "            \"vpn-id\": \"7a7b327d9-287aa00-82c4b0-100001\"," +
                        "            \"vpn-name\": \"GN_EVPN_direct_net_0_ST_Subnets_Pools_Ipv4\"," +
                        "            \"resource-version\": \"1494001848224\"," +
                        "            \"relationship-list\": {" +
                        "                \"relationship\": [" +
                        "                    {" +
                        "                        \"related-to\": \"l3-network\"," +
                        "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                        "                        \"related-link\": \"/aai/v15/network/l3-networks/l3-network/148b7892-c654-411d-9bb1-0d31fa6f9cc0\"," +
                        "                        \"relationship-data\": [" +
                        "                            {" +
                        "                                \"relationship-key\": \"l3-network.network-id\"," +
                        "                                \"relationship-value\": \"148b7892-c654-411d-9bb1-0d31fa6f9cc0\"" +
                        "                            }" +
                        "                        ]," +
                        "                        \"related-to-property\": [" +
                        "                            {" +
                        "                                \"property-key\": \"l3-network.network-name\"," +
                        "                                \"property-value\": \"GN_EVPN_direct_net_0_ST_Subnets_Pools_Ipv4\"" +
                        "                            }" +
                        "                        ]" +
                        "                    }" +
                        "                ]" +
                        "            }" +
                        "        }" +
                        "       ]}"
        );

        mockForGetRequest(aaiRestInterface, responseWithRequestInfo);

        List<VpnBinding> vpnList = aaiServiceWithoutMocks.getVpnListByVpnType("aaa");
        assertThat(vpnList, hasSize(1));
    }

    @Test
    public void getVpnListTest_notFoundResponse() {

        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.NOT_FOUND,
                "{" +
                        "    \"requestError\": {" +
                        "        \"serviceException\": {" +
                        "            \"messageId\": \"SVC3001\"," +
                        "            \"text\": \"Resource not found for %1 using id %2 (msg=%3) (ec=%4)\"," +
                        "            \"variables\": [" +
                        "                \"GET\"," +
                        "                \"network/vpn-bindings\"," +
                        "                \"Node Not Found:No Node of type vpn-binding found at: network/vpn-bindings\"," +
                        "                \"ERR.5.4.6114\"" +
                        "            ]" +
                        "        }" +
                        "    }" +
                        "}"
        );

        mockForGetRequest(aaiRestInterface, responseWithRequestInfo);

        List<VpnBinding> vpnList = aaiServiceWithoutMocks.getVpnListByVpnType("aaa");
        assertThat(vpnList, empty());
    }

    @Test(expectedExceptions = ExceptionWithRequestInfo.class, expectedExceptionsMessageRegExp = ".*errorCode.*500.*raw.*entity")
    public void getVpnListTest_errorResponse() {

        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.INTERNAL_SERVER_ERROR,
                "entity"
        );

        mockForGetRequest(aaiRestInterface, responseWithRequestInfo);

        aaiServiceWithoutMocks.getVpnListByVpnType("aaa");
    }

    @Test
    public void getNetworkListTest_successResponse() {
        String rawResponse = TestUtils.readFileAsString("/responses/aai/l3-networks-by-cloud-region-and-tenantId.json");
        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.OK,
                rawResponse,
                "/my/mocked/url",
                HttpMethod.PUT);
        mockForPutRequest(aaiRestInterface, responseWithRequestInfo);
        List<Network> networkList = aaiServiceWithoutMocks.getL3NetworksByCloudRegion("aaa", "bbb", "ccc");
        assertThat(networkList, hasSize(4));
        verify(aaiRestInterface).doRest(anyString(), anyString(), eq(URI.create("query?format=resource")),
                eq("{\"start\":\"/cloud-infrastructure/cloud-regions/cloud-region/ddd/aaa\",\"query\":\"query/l3-networks-by-cloud-region?tenantId=bbb&networkRole=ccc\"}"), eq(HttpMethod.PUT), anyBoolean(), anyBoolean());
    }

    @DataProvider
    public static Object[][] networkRoles() {
        return new Object[][]{
                {"", "{\"start\":\"/cloud-infrastructure/cloud-regions/cloud-region/ddd/aaa\",\"query\":\"query/l3-networks-by-cloud-region?tenantId=bbb\"}"},
                {null, "{\"start\":\"/cloud-infrastructure/cloud-regions/cloud-region/ddd/aaa\",\"query\":\"query/l3-networks-by-cloud-region?tenantId=bbb\"}"},
                {"ccc", "{\"start\":\"/cloud-infrastructure/cloud-regions/cloud-region/ddd/aaa\",\"query\":\"query/l3-networks-by-cloud-region?tenantId=bbb&networkRole=ccc\"}"}
        };
    }

    @Test(dataProvider = "networkRoles")
    public void testBuildPayloadForL3NetworksByCloudRegion(String networkRole, String expected) {
        String payload = aaiServiceWithoutMocks.buildPayloadForL3NetworksByCloudRegion("aaa", "bbb", networkRole);
        assertEquals(expected, payload);

    }

    @Test
    public void getNetworkListTest_notFoundResponse() {

        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.NOT_FOUND,
                "{" +
                        "    \"requestError\": {" +
                        "        \"serviceException\": {" +
                        "            \"messageId\": \"SVC3001\"," +
                        "            \"text\": \"Some text\"," +
                        "            \"variables\": []" +
                        "        }" +
                        "    }" +
                        "}",
                "/my/mocked/url",
                HttpMethod.PUT);

        mockForPutRequest(aaiRestInterface, responseWithRequestInfo);

        List<Network> networkList = aaiServiceWithoutMocks.getL3NetworksByCloudRegion("aaa", "bbb", "ccc");
        assertThat(networkList, empty());
    }

    @Test(expectedExceptions = ExceptionWithRequestInfo.class, expectedExceptionsMessageRegExp = ".*errorCode.*500.*raw.*entity")
    public void getNetworkListTest_errorResponse() {

        final ResponseWithRequestInfo responseWithRequestInfo = mockedResponseWithRequestInfo(Response.Status.INTERNAL_SERVER_ERROR,
                "entity",
                "/my/mocked/url",
                HttpMethod.PUT);

        mockForPutRequest(aaiRestInterface, responseWithRequestInfo);

        aaiServiceWithoutMocks.getL3NetworksByCloudRegion("aaa", "bbb", "ccc");
    }
}
