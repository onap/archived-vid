/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.asdc.rest;

import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.apache.http.client.config.RequestConfig.custom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertTrue;
import static org.onap.vid.client.SyncRestClientInterface.HEADERS.X_ECOMP_INSTANCE_ID;
import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xebialabs.restito.semantics.Call;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import javax.net.ssl.SSLContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.testUtils.StubServerUtil;
import org.onap.vid.utils.Logging;


public class SdcRestClientITTest {
    private static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private static final String[] SUPPORTED_SSL_VERSIONS = {"TLSv1", "TLSv1.2"};
    private static StubServerUtil stubServer;
    private static SdcRestClient sdcRestClient;

    @BeforeClass
    public static void setUpClass() throws GeneralSecurityException {
        stubServer = new StubServerUtil();
        stubServer.runSecuredServer();
        SyncRestClient syncRestClient = new SyncRestClient(createNaiveHttpClient(), Logging.getRequestsLogger("sdc"));
        String serverUrl = stubServer.constructTargetUrl("https", "");
        sdcRestClient = new SdcRestClient(serverUrl, "", syncRestClient);
    }

    @AfterClass
    public static void tearDown() {
        stubServer.stopServer();
    }

    @Test
    public void shouldDownloadToscaArtifactUsingSecuredEndpoint() throws AsdcCatalogException, IOException {
        UUID uuid = UUID.randomUUID();
        String expectedEndpoint = String.format("/sdc/v1/catalog/services/%s/toscaModel", uuid);

        stubServer.prepareGetCall(
                expectedEndpoint, stringContent("sampleFileContent"), ok(), "application/octet-stream");


        Path serviceToscaModel = sdcRestClient.getServiceToscaModel(uuid);
        serviceToscaModel.toFile().deleteOnExit();


        assertThat(Files.readAllLines(serviceToscaModel), contains("sampleFileContent"));
        assertThatRequestHasRequiredHeaders(expectedEndpoint);
    }

    @Test
    public void shouldGetServiceDetailsUsingSecuredEndpoint() throws AsdcCatalogException, JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        String expectedEndpoint = String.format("/sdc/v1/catalog/services/%s/metadata", uuid);
        Service expectedService = getExpectedService(uuid.toString());


        stubServer.prepareGetCall(expectedEndpoint, expectedService, ok());


        Service actualService = sdcRestClient.getService(uuid);


        assertThat(actualService, is(expectedService));
        assertThatRequestHasRequiredHeaders(expectedEndpoint);
    }

    private void assertThatRequestHasRequiredHeaders(String expectedEndpoint) {
        Optional<Call> first = stubServer
                .getServerCalls()
                .stream()
                .filter(x -> x.getUri().contains(expectedEndpoint))
                .findFirst();

        assertTrue(first.isPresent());

        assertThat(first.get().getHeaders().keySet(),
                hasItems(X_ECOMP_INSTANCE_ID.toLowerCase(), REQUEST_ID_HEADER_KEY.toLowerCase()));
        assertThat(first.get().getHeaders().get(REQUEST_ID_HEADER_KEY.toLowerCase()).get(0),
                matchesPattern(UUID_REGEX));
    }

    private Service getExpectedService(String stringId) {
        return new Service.ServiceBuilder().setUuid(stringId)
                .setInvariantUUID(stringId)
                .setCategory("sampleCategory")
                .setVersion("sampleVersion")
                .setName( "sampleName")
                .setDistributionStatus("sampleDistStatus")
                .setToscaModelURL("sampleToscaUrl")
                .setLifecycleState(Service.LifecycleState.CERTIFIED)
                .setArtifacts(Collections.emptyList())
                .setResources(Collections.emptyList()).build();
    }


    private static CloseableHttpClient createNaiveHttpClient() throws GeneralSecurityException {
        final SSLContext context = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();

        final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, SUPPORTED_SSL_VERSIONS,
                null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", socketFactory)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(custom().setConnectionRequestTimeout(10000).build())
                .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
                .setSSLSocketFactory(socketFactory).build();
    }
}
