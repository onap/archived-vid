/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.openecomp.vid.mso;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This wrapper encapsulates the MSO response in the format expected by the pages.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	    "status",
	    "entity"
})

public class MsoResponseWrapper {
	
	/** The status. */
	@JsonProperty("status")
	private int status;
	
	/** The entity. */
	@JsonProperty("entity")
	private String entity;

	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	@JsonProperty("entity")
    public String getEntity() {
        return entity;
    }

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	@JsonProperty("status")
    public int getStatus() {
        return status;
    }
	
	/**
	 * Sets the status.
	 *
	 * @param v the new status
	 */
	@JsonProperty("status")
    public void setStatus(int v) {
        this.status = v;
    }
	
	/**
	 * Sets the entity.
	 *
	 * @param v the new entity
	 */
	@JsonProperty("entity")
    public void setEntity(String v) {
        this.entity = v;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    /**
     * Gets the response.
     *
     * @return the response
     */
    @org.codehaus.jackson.annotate.JsonIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getResponse () {
    	
    	StringBuilder b = new StringBuilder ("{ \"status\": ");
        b.append(getStatus()).append(", \"entity\": " ).append(this.getEntity()).append("}");
        return (b.toString());
    }
    
}
