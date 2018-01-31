package org.onap.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.local.LocalAsdcClient;
import org.onap.vid.asdc.memory.InMemoryAsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.asdc.rest.RestfulAsdcClient;
import org.onap.vid.properties.AsdcClientConfiguration;
import org.onap.vid.properties.AsdcClientConfiguration.AsdcClientType;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.AaiServiceImpl;
import org.onap.vid.services.VidService;
import org.onap.vid.services.VidServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
    public VidService vidService(AsdcClient asdcClient) {
        return new VidServiceImpl(asdcClient);
    }

    @Bean
    public AaiService getAaiService(){
        return new AaiServiceImpl();
    }

    @Bean
    public AaiClientInterface getAaiClientInterface(){
        return new AaiClient();
    }

    @Bean
    public AsdcClient asdcClient(AsdcClientConfiguration asdcClientConfig) throws IOException {
        switch (asdcClientConfig.getAsdcClientType()) {
            case IN_MEMORY:
                final InputStream asdcCatalogFile = VidController.class.getClassLoader().getResourceAsStream("catalog.json");
                final JSONTokener tokener = new JSONTokener(asdcCatalogFile);
                final JSONObject catalog = new JSONObject(tokener);

                return new InMemoryAsdcClient.Builder().catalog(catalog).build();
            case REST:

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
                        throw new RuntimeException("SDC Client could not be instantiated due to unsupported protocol TLSv1.2", n);
                    } catch (KeyManagementException k) {
                        throw new RuntimeException("SDC Client could not be instantiated due to a key management exception", k);
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
                    throw new RuntimeException("SDC Client could not be instantiated due to a syntax error in the URI", e);
                }

            case LOCAL:

                final InputStream asdcServicesFile = VidController.class.getClassLoader().getResourceAsStream("sdcservices.json");

                final JSONTokener jsonTokener = new JSONTokener(IOUtils.toString(asdcServicesFile));
                final JSONObject sdcServicesCatalog = new JSONObject(jsonTokener);

                return new LocalAsdcClient.Builder().catalog(sdcServicesCatalog).build();

            default:
                throw new RuntimeException(asdcClientConfig.getAsdcClientType() + " is invalid; must be one of " + Arrays.toString(AsdcClientType.values()));
        }
    }

    @Bean
    public ToscaParserImpl2 getToscaParser() {
        return new ToscaParserImpl2();
    }

}
