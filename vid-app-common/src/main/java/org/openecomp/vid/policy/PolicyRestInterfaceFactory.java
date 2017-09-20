
package org.openecomp.vid.policy;

public class PolicyRestInterfaceFactory {

	
	public static PolicyRestInterfaceIfc getInstance () {
		PolicyRestInterfaceIfc obj = null;

		obj = new PolicyRestInterface();
		
		return ( obj );
	}
}