package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

public abstract class KeyValueModel {
	private String key;
    private String value;

    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setValue(String value) {
        this.value = value;
    }


}
