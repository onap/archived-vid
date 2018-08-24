/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;

@JsonPropertyOrder({
	    "status",
	    "entity"
})

/*
This is a brother of MsoResponseWrapper. I (Ittay) think it's better.
It is generic, immutable, and has some knowledge about RestObject.
The serialized "entity" field may be either String or nested object.
 */
public class MsoResponseWrapper2<T> implements MsoResponseWrapperInterface {

    final static ObjectMapper objectMapper = new ObjectMapper();

	private final int status;
	private final T entity;
    private final String raw;

    public MsoResponseWrapper2(RestObject<T> msoResponse) {
        this.status = msoResponse.getStatusCode();
        this.entity = msoResponse.get();
        this.raw = msoResponse.getRaw();
    }

  public MsoResponseWrapper2(HttpResponse<T> msoResponse) {
    this.status = msoResponse.getStatus();
    this.entity = msoResponse.getBody();
    this.raw = msoResponse.getBody().toString();
  }

    public MsoResponseWrapper2(
            @JsonProperty(value = "status", required = true) int status,
            @JsonProperty(value = "entity", required = true) T entity) {
        this.status = status;
        this.entity = entity;
        this.raw = null;
    }

    public int getStatus() {
		return status;
	}

    @Override
    @org.codehaus.jackson.annotate.JsonIgnore
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getResponse() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return getEntity() != null ? getEntity().toString() : null;
        }
    }

    @JsonProperty
	public Object getEntity() {
		return entity != null ? entity : raw;
	}

}
