package org.openecomp.vid.scheduler.RestObjects;

public class GetTimeSlotsRestObject<T> extends org.openecomp.vid.scheduler.RestObject<T> {

	public String uuid;
	
	public void setUUID(String uuid) { this.uuid = uuid; }
	    
	public String getUUID() { return this.uuid; }

}
