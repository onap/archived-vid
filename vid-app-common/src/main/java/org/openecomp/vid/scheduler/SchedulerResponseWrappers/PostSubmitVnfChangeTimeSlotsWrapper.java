package org.openecomp.vid.scheduler.SchedulerResponseWrappers;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	    "uuid"
})
public class PostSubmitVnfChangeTimeSlotsWrapper extends SchedulerResponseWrapper  {
	@JsonProperty("uuid")
	private String uuid;
	
	@JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }
	
	@JsonProperty("uuid")
    public void setUuid(String v) {
        this.uuid = v;
    }
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

public String getResponse () {
    	
    	StringBuilder b = new StringBuilder ("{ \"status\": ");
    	
    	if(this.getEntity().equals("") || this.getEntity() == "") {
    		this.setEntity("null");//to keep json format when response arrive at UI
    	}
    	
    	b.append(getStatus()).append(" ,\"uuid\": \"" ).append(this.getUuid()).append("\", \"entity\": " ).append(this.getEntity()).append("}");
    	return (b.toString());
    }
}
