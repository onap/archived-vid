package org.openecomp.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openecomp.vid.aai.AaiClient;
import org.openecomp.vid.aai.AaiClientInterface;
import org.openecomp.vid.asdc.AsdcClient;
import org.openecomp.vid.asdc.local.LocalAsdcClient;
import org.openecomp.vid.asdc.parser.ToscaParserImpl2;
import org.openecomp.vid.services.AaiService;
import org.openecomp.vid.services.AaiServiceImpl;
import org.openecomp.vid.services.VidService;
import org.openecomp.vid.services.VidServiceImpl;
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
        return new VidServiceImpl(asdcClient);
    }

    @Bean
    public AaiService getAaiService() {
        return new AaiServiceImpl();
    }

    @Bean
    public AaiClientInterface getAaiClientInterface() {
        return new AaiClient();
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
