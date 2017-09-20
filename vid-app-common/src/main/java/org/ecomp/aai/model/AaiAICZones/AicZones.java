package org.ecomp.aai.model.AaiAICZones;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class AicZones {
	@JsonProperty("zone")
	public List<Zone> zones;
}
