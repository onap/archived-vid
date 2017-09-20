package org.opencomp.vid.controller;

import org.openecomp.vid.model.NewServiceModel;

/**
 * Created by moriya1 on 04/07/2017.
 */
public class ToscaParserMockHelper {

    private String uuid;
    private String filePath;
    private NewServiceModel newServiceModel;

    public ToscaParserMockHelper(String uuid, String filePath) {
        this.uuid = uuid;
        this.filePath = filePath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public NewServiceModel getNewServiceModel() {
        return newServiceModel;
    }

    public void setNewServiceModel(NewServiceModel newServiceModel) {
        this.newServiceModel = newServiceModel;
    }
}
