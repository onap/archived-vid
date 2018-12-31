package org.onap.vid.aai.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedToProperty {

	public String getPropertyKey() {
		return propertyKey;
	}


	@JsonAlias("property-key")
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}


	public String getPropertyValue() {
		return propertyValue;
	}


	@JsonAlias("property-value")
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}


	public String propertyKey;


	public String propertyValue;

}
