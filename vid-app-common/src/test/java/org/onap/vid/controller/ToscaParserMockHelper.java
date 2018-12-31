package org.onap.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.onap.vid.model.NewServiceModel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by moriya1 on 04/07/2017.
 */
public class ToscaParserMockHelper {
    private static final Logger logger = LogManager.getLogger(ToscaParserMockHelper.class);

    private static final ObjectMapper om = new ObjectMapper();
    private final String uuid;
    private final String filePath;
    private final NewServiceModel newServiceModel;

    public ToscaParserMockHelper(String uuid, String filePath) throws IOException {
        this.uuid = uuid;
        this.filePath = filePath;

        InputStream jsonFile = this.getClass().getClassLoader().getResourceAsStream(getFilePath());
        logger.info(jsonFile);
        String expectedJsonAsString = IOUtils.toString(jsonFile, StandardCharsets.UTF_8.name());
        om.registerModule(new KotlinModule());
        this.newServiceModel = om.readValue(expectedJsonAsString, NewServiceModel.class);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFilePath() {
        return filePath;
    }

    public NewServiceModel getNewServiceModel() {
        return newServiceModel;
    }
}
