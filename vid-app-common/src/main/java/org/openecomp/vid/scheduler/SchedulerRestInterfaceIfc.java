
package org.openecomp.vid.scheduler;

import org.json.simple.JSONObject;
import org.openecomp.vid.scheduler.RestObjects.RestObject;
import org.springframework.stereotype.Service;

@Service
public interface SchedulerRestInterfaceIfc {

	public void initRestClient();

	public <T> void Get (T t, String sourceId, String path, org.openecomp.vid.scheduler.RestObject<T> restObject ) throws Exception;

	public <T> void Delete(T t, JSONObject requestDetails, String sourceID, String path, RestObject<T> restObject)
			throws Exception;

	public <T> void Post(T t, JSONObject r, String path, RestObject<T> restObject) throws Exception;

	public void logRequest(JSONObject requestDetails);
}