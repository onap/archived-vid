package org.onap.vid.asdc.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.Before;
import org.junit.Test;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.Artifact;
import org.onap.vid.asdc.beans.Resource;
import org.onap.vid.asdc.beans.Service;

import nu.xom.Builder;

public class RestfulAsdcClientTest {

    private RestfulAsdcClient createTestSubject() {
        return new RestfulAsdcClient.Builder(restClient, uri).auth(auth)
                .build();
    }

    /** The rest client. */
    private Client restClient;

    /** The uri. */
    private URI uri;

    /** The properties. */
    private Properties properties;

    /** The auth. */
    private String auth;

    /**
     * Sets the up.
     *
     * @throws URISyntaxException
     *             the URI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Before
    public void setUp() throws URISyntaxException, IOException {
        final InputStream propertiesFile = getClass().getClassLoader()
                .getResourceAsStream("asdc.properties");

        properties = new Properties();
        properties.load(propertiesFile);

        final String protocol = properties.getProperty("protocol", "http");

        restClient = ClientBuilder.newBuilder()
                .hostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                })
                .build();
        uri = new URI(protocol + "://" + properties.getProperty("host", "localhost") + ":"
                + properties.getProperty("port", "80") + "/");
        auth = properties.getProperty("auth");
    }

    @Test
    public void testGetResource() throws Exception {
        RestfulAsdcClient testSubject;
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Resource result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getResource(uuid);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetResourceArtifact() throws Exception {
        RestfulAsdcClient testSubject;
        UUID resourceUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        UUID artifactUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Artifact result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getResourceArtifact(resourceUuid, artifactUuid);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetResources() throws Exception {
        RestfulAsdcClient testSubject;
        Collection<Resource> result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getResources();
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetResources_1() throws Exception {
        RestfulAsdcClient testSubject;
        Map<String, String[]> filter = null;
        Collection<Resource> result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getResources(filter);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetResourceToscaModel() throws Exception {
        RestfulAsdcClient testSubject;
        UUID resourceUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");

        // default test
        testSubject = createTestSubject();
        try {
            testSubject.getResourceToscaModel(resourceUuid);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetService() throws Exception {
        RestfulAsdcClient testSubject;
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Service result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getService(uuid);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetServiceArtifact() throws Exception {
        RestfulAsdcClient testSubject;
        UUID serviceUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        UUID artifactUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        Artifact result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getServiceArtifact(serviceUuid, artifactUuid);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetServices() throws Exception {
        RestfulAsdcClient testSubject;
        Collection<Service> result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getServices();
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetServices_1() throws Exception {
        RestfulAsdcClient testSubject;
        Map<String, String[]> filter = null;
        Collection<Service> result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getServices(filter);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetServiceToscaModel() throws Exception {
        RestfulAsdcClient testSubject;
        UUID serviceUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");

        // default test
        testSubject = createTestSubject();
        try {
            testSubject.getServiceToscaModel(serviceUuid);
        } catch (Exception e) {

        }
    }

}