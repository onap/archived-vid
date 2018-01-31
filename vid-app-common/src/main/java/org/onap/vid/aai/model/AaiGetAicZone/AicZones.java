package org.onap.vid.aai.model.AaiGetAicZone;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class AicZones {
	@JsonProperty("zone")
	public List<Zone> zones;
}
