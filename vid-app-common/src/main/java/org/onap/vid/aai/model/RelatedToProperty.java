package org.onap.vid.aai.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedToProperty {

	public String getPropertyKey() {
		return propertyKey;
	}


	@JsonProperty("property-key")
	public void setJsonPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}


	public String getPropertyValue() {
		return propertyValue;
	}


	@JsonProperty("property-value")
	public void setJsonPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}


	public String propertyKey;


	public String propertyValue;

}
