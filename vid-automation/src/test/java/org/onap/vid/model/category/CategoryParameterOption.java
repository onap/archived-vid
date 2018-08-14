package org.onap.vid.model.category;

public class CategoryParameterOption {

    private String appId;
    private String name;


    public CategoryParameterOption() {
    }

    public CategoryParameterOption(String appId, String name) {
        setAppId(appId);
        setName(name);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
