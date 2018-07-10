package org.onap.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.AaiServiceImpl;
import org.onap.vid.services.VidService;
import org.onap.vid.services.VidServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

import javax.servlet.ServletContext;
import java.io.File;

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
    public VidService vidService(AsdcClient asdcClient, FeatureManager featureManager) {
        return new VidServiceImpl(asdcClient, featureManager);
    }

    @Bean
    public AaiService getAaiService() {
        return new AaiServiceImpl();
    }

    @Bean
    public HttpsAuthClient httpsAuthClientFactory(ServletContext servletContext) {
        final String certFilePath = new File(servletContext.getRealPath("/WEB-INF/cert/")).getAbsolutePath();
        return new HttpsAuthClient(certFilePath);
    }

    @Bean(name = "aaiRestInterface")
    public AAIRestInterface aaiRestInterface(HttpsAuthClient httpsAuthClientFactory) {
        return new AAIRestInterface(httpsAuthClientFactory);
    }

    @Bean
    public AaiClientInterface getAaiClientInterface(@Qualifier("aaiRestInterface")AAIRestInterface aaiRestInterface, PortDetailsTranslator portDetailsTranslator) {
        return new AaiClient(aaiRestInterface, portDetailsTranslator);
    }

    @Bean
    public ToscaParserImpl2 getToscaParser() {
        return new ToscaParserImpl2();
    }

    @Bean
    public AaiResponseTranslator aaiResponseTranslator() {
        return new AaiResponseTranslator();
    }

    @Bean
    public PortDetailsTranslator portDetailsTranslator(){
        return new PortDetailsTranslator();
    }

}
