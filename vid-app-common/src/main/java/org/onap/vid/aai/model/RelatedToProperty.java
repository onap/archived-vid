package org.onap.vid.aai.model;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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
