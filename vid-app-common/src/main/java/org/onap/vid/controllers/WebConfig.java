package org.onap.vid.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.aai.*;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.util.*;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.asdc.rest.RestfulAsdcClient;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.properties.AsdcClientConfiguration;
import org.onap.vid.services.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class WebConfig {

    /**
     * Gets the object mapper.
     *
     * @return the object mapper
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public VidService vidService(AsdcClient asdcClient, FeatureManager featureManager) {
        return new VidServiceImpl(asdcClient, featureManager);
    }

    @Bean
    public AaiService getAaiService() {
        return new AaiServiceImpl();
    }

    @Bean
    public AaiResponseTranslator aaiResponseTranslator() {
        return new AaiResponseTranslator();
    }

    @Bean
    public PortDetailsTranslator portDetailsTranslator() {
        return new PortDetailsTranslator();
    }

    @Bean
    public AaiClientInterface getAaiRestInterface(@Qualifier("aaiRestInterface") AAIRestInterface restController, PortDetailsTranslator portsDetailsTranslator) {
        return new AaiClient(restController, portsDetailsTranslator);
    }

    @Bean(name = "aaiRestInterface")
    public AAIRestInterface aaiRestInterface(HttpsAuthClient httpsAuthClientFactory, ServletRequestHelper servletRequestHelper, SystemPropertyHelper systemPropertyHelper) {
        return new AAIRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper);
    }

    @Bean
    public PombaRestInterface getPombaRestInterface(HttpsAuthClient httpsAuthClientFactory, ServletRequestHelper servletRequestHelper, SystemPropertyHelper systemPropertyHelper) {
        return new PombaRestInterface(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper);
    }

    @Bean
    public SSLContextProvider sslContextProvider() {
        return new SSLContextProvider();
    }

    @Bean
    public SystemPropertyHelper systemPropertyHelper() {
        return new SystemPropertyHelper();
    }

    @Bean
    public ServletRequestHelper servletRequestHelper() {
        return new ServletRequestHelper();
    }

    @Bean
    public HttpsAuthClient httpsAuthClientFactory(ServletContext servletContext, SystemPropertyHelper systemPropertyHelper, SSLContextProvider sslContextProvider) {
        final String certFilePath = new File(servletContext.getRealPath("/WEB-INF/cert/")).getAbsolutePath();
        return new HttpsAuthClient(certFilePath, systemPropertyHelper, sslContextProvider);
    }

    @Bean
    public AsdcClient asdcClient(AsdcClientConfiguration asdcClientConfig) {

        final String protocol = asdcClientConfig.getAsdcClientProtocol();
        final String host = asdcClientConfig.getAsdcClientHost();
        final int port = asdcClientConfig.getAsdcClientPort();
        final String auth = asdcClientConfig.getAsdcClientAuth();
        Client cl = null;
        if (protocol.equalsIgnoreCase("https")) {
            try {
                SSLContext ctx = SSLContext.getInstance("TLSv1.2");
                ctx.init(null, null, null);
                cl = ClientBuilder.newBuilder().sslContext(ctx).build();
            } catch (NoSuchAlgorithmException n) {
                throw new GenericUncheckedException("SDC Client could not be instantiated due to unsupported protocol TLSv1.2", n);
            } catch (KeyManagementException k) {
                throw new GenericUncheckedException("SDC Client could not be instantiated due to a key management exception", k);
            }
        } else {
            cl = ClientBuilder.newBuilder().build();
        }

        try {
            final URI uri = new URI(protocol + "://" + host + ":" + port + "/");
            return new RestfulAsdcClient.Builder(cl, uri)
                    .auth(auth)
                    .build();
        } catch (URISyntaxException e) {
            throw new GenericUncheckedException("SDC Client could not be instantiated due to a syntax error in the URI", e);
        }
    }

    @Bean
    public ToscaParserImpl2 getToscaParser() {
        return new ToscaParserImpl2();
    }

    @Bean
    public PombaService getVerifyServiceInstanceService() {
        return new PombaServiceImpl();
    }

    @Bean
    public PombaClientInterface getVerifyServiceInstanceClientInterface() {
        return new PombaClientImpl();
    }
}
