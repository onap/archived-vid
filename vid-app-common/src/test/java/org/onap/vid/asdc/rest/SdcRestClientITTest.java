/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia Intellectual Property. All rights reserved.
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

import com.google.gson.Gson;
import com.xebialabs.restito.semantics.Call;
import com.xebialabs.restito.server.StubServer;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.client.SyncRestClient;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.apache.http.client.config.RequestConfig.custom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertTrue;
import static org.onap.vid.client.SyncRestClient.SUPPORTED_PROTOCOLS;


public class SdcRestClientITTest {
    private static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private static StubServer stubServer;
    private SdcRestClient sdcRestClient;


    @BeforeClass
    public static void setUpClass() {
        stubServer = new StubServer();
        stubServer.secured().run();
    }


    @Before
    public void setUp() throws GeneralSecurityException {
        SyncRestClient syncRestClient = new SyncRestClient(createNaiveHttpClient());
        String url = "https://localhost:" + stubServer.getPort() + "/";
        sdcRestClient = new SdcRestClient(url, "", syncRestClient);
    }

    @AfterClass
    public static void tearDown() {
        stubServer.stop();
    }

    @Test
    public void shouldDownloadToscaArtifactUsingSecuredEndpoint() throws AsdcCatalogException, IOException {
        UUID uuid = UUID.randomUUID();
        String expectedEndpoint = String.format("sdc/v1/catalog/services/%s/toscaModel", uuid);
        whenHttp(stubServer)
                .match("toscaModel")
                .then(ok(), stringContent("sampleFileContent"), contentType("application/octet-stream"));


        Path serviceToscaModel = sdcRestClient.getServiceToscaModel(uuid);
        serviceToscaModel.toFile().deleteOnExit();


        assertThat(Files.readAllLines(serviceToscaModel), contains("sampleFileContent"));
        assertRequestHeaders(expectedEndpoint);
    }

    @Test
    public void shouldGetServiceDetailsUsingSecuredEndpoint() throws AsdcCatalogException {
        UUID uuid = UUID.randomUUID();
        String expectedEndpoint = String.format("sdc/v1/catalog/services/%s/metadata", uuid);
        Service expectedService = getExpectedService(uuid.toString());

        whenHttp(stubServer)
                .match("metadata")
                .then(ok(), stringContent(new Gson().toJson(expectedService)), contentType("application/json"));


        Service actualService = sdcRestClient.getService(uuid);


        assertThat(actualService, is(expectedService));
        assertRequestHeaders(expectedEndpoint);
    }

    private void assertRequestHeaders(String expectedEndpoint) {
        Optional<Call> first = stubServer
                .getCalls()
                .stream()
                .filter(x -> x.getUri().contains(expectedEndpoint))
                .findFirst();

        assertTrue(first.isPresent());

        assertThat(first.get().getHeaders().keySet(), hasItems("x-ecomp-instanceid", "x-ecomp-requestid"));
        assertThat(first.get().getHeaders().get("x-ecomp-requestid").get(0), matchesPattern(UUID_REGEX));
    }

    private Service getExpectedService(String stringId) {
        return new Service(stringId, stringId,
                "sampleCategory", "sampleVersion",
                "sampleName", "sampleDistStatus",
                "sampleToscaUrl", Service.LifecycleState.CERTIFIED, Collections.emptyList(), Collections.emptyList());
    }


    private HttpClient createNaiveHttpClient() throws GeneralSecurityException {
        final SSLContext context = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();

        final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, SUPPORTED_PROTOCOLS,
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
