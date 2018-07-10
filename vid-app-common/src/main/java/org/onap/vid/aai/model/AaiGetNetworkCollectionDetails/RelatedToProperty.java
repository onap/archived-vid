package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedToProperty {

	public String getPropertyKey() {
		return propertyKey;
	}


	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}


	public String getPropertyValue() {
		return propertyValue;
	}


	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}


	@JsonProperty("property-key")
	public String propertyKey;


	@JsonProperty("property-value")
	public String propertyValue;

}
