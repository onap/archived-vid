package org.opencomp.vid.model.mso;


import org.codehaus.jackson.annotate.JsonProperty;

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
