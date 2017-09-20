package org.openecomp.vid.scheduler.SchedulerResponseWrappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This wrapper encapsulates the Scheduler response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	    "status",
	    "entity"
})

public class SchedulerResponseWrapper {
	
	@JsonProperty("status")
	private int status;

	@JsonProperty("entity")
	private String entity;

	@JsonProperty("entity")
    public String getEntity() {
        return entity;
    }
	
	@JsonProperty("status")
    public int getStatus() {
        return status;
    }
	
	@JsonProperty("status")
    public void setStatus(int v) {
        this.status = v;
    }

	@JsonProperty("entity")
    public void setEntity(String v) {
        this.entity = v;
    }
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getResponse () {
    	
    	StringBuilder b = new StringBuilder ("{ \"status\": ");
    
    	b.append(getStatus()).append(", \"entity\": " ).append(this.getEntity()).append("}");
        return (b.toString());
    }
}
