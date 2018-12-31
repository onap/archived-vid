
package org.onap.vid.scheduler;

import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.springframework.stereotype.Service;

@Service
public interface SchedulerRestInterfaceIfc {

	void initRestClient();

	<T> RestObjectWithRequestInfo Get(T t, String path, RestObject<T> restObject);

	<T> void Delete(T t, String sourceID, String path, RestObject<T> restObject);

}


