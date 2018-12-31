package org.onap.vid.aai.model.AaiGetAicZone;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AicZones {
	@JsonProperty("zone")
	public List<Zone> zones;
}
