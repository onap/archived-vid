
package org.onap.vid.scheduler;

public class SchedulerRestInterfaceFactory {

	
	public static SchedulerRestInterfaceIfc getInstance () {
		SchedulerRestInterfaceIfc obj = null;

		obj = new SchedulerRestInterface();
		
		return ( obj );
	}

}
