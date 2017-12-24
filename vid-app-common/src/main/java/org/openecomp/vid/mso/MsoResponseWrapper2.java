package org.openecomp.vid.mso;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	    "status",
	    "entity"
})

/*
This is a brother of MsoResponseWrapper. I (Ittay) think it's better.
It is generic, immutable, and has some knowledge about RestObject.
The serialized "entity" field may be either String or nested object.
 */
public class MsoResponseWrapper2<T>  {
	
	private final int status;
	private final T entity;
    private final String raw;

    public MsoResponseWrapper2(RestObject<T> msoResponse) {
        this.status = msoResponse.getStatusCode();
        this.entity = msoResponse.get();
        this.raw = msoResponse.getRaw();
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

    @JsonProperty
	public Object getEntity() {
		return entity != null ? entity : raw;
	}

}
