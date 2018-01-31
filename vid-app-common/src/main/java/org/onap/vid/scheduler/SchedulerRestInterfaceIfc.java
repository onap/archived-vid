
package org.onap.vid.scheduler;

import org.json.simple.JSONObject;
import org.onap.vid.scheduler.RestObjects.RestObject;
import org.springframework.stereotype.Service;

@Service
public interface SchedulerRestInterfaceIfc {

	public void initRestClient();

	public <T> void Get (T t, String sourceId, String path, org.onap.vid.scheduler.RestObject<T> restObject ) throws Exception;

	public <T> void Delete(T t, String sourceID, String path, org.onap.vid.scheduler.RestObject<T> restObject)
			throws Exception;

	public <T> void Post(T t, JSONObject r, String path, RestObject<T> restObject) throws Exception;

	public void logRequest(JSONObject requestDetails);
}


