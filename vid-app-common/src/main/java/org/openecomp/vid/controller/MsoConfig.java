package org.openecomp.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.vid.factories.MsoRequestFactory;
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



}
