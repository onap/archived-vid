package org.onap.vid.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.local.LocalAsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.controllers.VidController;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.AaiServiceImpl;
import org.onap.vid.services.VidService;
import org.onap.vid.services.VidServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class LocalWebConfig {

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
        return new VidServiceImpl(asdcClient, null);
    }

    @Bean
    public AaiService getAaiService() {
        return new AaiServiceImpl();
    }

    @Bean
    public AaiClientInterface getAaiClientInterface() {
        return new AaiClient(null,null);
    }

    @Bean
    public AsdcClient asdcClient() throws IOException {


        final InputStream asdcServicesFile = VidController.class.getClassLoader().getResourceAsStream("sdcservices.json");

        final JSONTokener jsonTokener = new JSONTokener(IOUtils.toString(asdcServicesFile));
        final JSONObject sdcServicesCatalog = new JSONObject(jsonTokener);

        return new LocalAsdcClient.Builder().catalog(sdcServicesCatalog).build();

    }

    @Bean
    public ToscaParserImpl2 getToscaParser() {
        return new ToscaParserImpl2();
    }

}
