
package org.onap.vid.scheduler;

import org.springframework.stereotype.Service;

@Service
public interface SchedulerRestInterfaceIfc {

	void initRestClient();

	<T> void Get(T t, String sourceId, String path, org.onap.vid.scheduler.RestObject<T> restObject);

	<T> void Delete(T t, String sourceID, String path, org.onap.vid.scheduler.RestObject<T> restObject)
			throws Exception;

}


