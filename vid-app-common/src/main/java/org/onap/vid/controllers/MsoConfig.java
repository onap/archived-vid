package org.onap.vid.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.factories.MsoRequestFactory;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MsoConfig {

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
    public MsoRequestFactory createRequestDetailsFactory(){
        return new MsoRequestFactory();
    }

    @Bean
    public MsoInterface getMsoClient(){
        return new MsoRestClientNew();
    }

    @Bean
    public MsoBusinessLogic getMsoBusinessLogic(){
        return new MsoBusinessLogicImpl(getMsoClient());
    }


}
