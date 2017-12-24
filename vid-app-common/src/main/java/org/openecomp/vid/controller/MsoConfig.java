package org.openecomp.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.vid.factories.MsoRequestFactory;
import org.openecomp.vid.mso.MsoBusinessLogic;
import org.openecomp.vid.mso.MsoBusinessLogicImpl;
import org.openecomp.vid.mso.MsoInterface;
import org.openecomp.vid.mso.rest.MsoRestClientNew;
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
