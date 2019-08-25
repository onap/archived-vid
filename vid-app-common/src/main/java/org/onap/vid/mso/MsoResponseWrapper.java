/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

package org.onap.vid.mso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.ws.rs.core.Response;

/**
 * This wrapper encapsulates the MSO response in the format expected by the pages.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	    "status",
	    "entity"
})

public class MsoResponseWrapper implements MsoResponseWrapperInterface {

	/** The status. */
	@JsonProperty("status")
	private int status;
	
	/** The entity. */
	@JsonProperty("entity")
	private String entity;

    public MsoResponseWrapper() {
    }

    public MsoResponseWrapper(Response response) {
        setEntity(response.readEntity(String.class));
        setStatus(response.getStatus());
    }

	public MsoResponseWrapper(int status, String entity) {
		this.status = status;
		this.entity = entity;
	}

	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	@Override
	@JsonProperty("entity")
    public String getEntity() {
        return entity;
    }

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	@Override
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
    @JsonIgnore
    public String getResponse () {
    	
    	StringBuilder b = new StringBuilder ("{ \"status\": ");
        b.append(getStatus()).append(", \"entity\": " );
        if (this.getEntity() == null || this.getEntity().isEmpty()) {
        	b.append("\"\"");
		} else {
			b.append(this.getEntity());
		}
        b.append("}");
        return (b.toString());
    }
    
}
