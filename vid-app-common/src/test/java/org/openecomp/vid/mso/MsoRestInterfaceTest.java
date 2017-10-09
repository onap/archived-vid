package org.openecomp.vid.mso;

import org.apache.poi.hssf.record.formula.functions.T;
import org.junit.Test;

public class MsoRestInterfaceTest {

	private MsoRestInterface createTestSubject() {
		return new MsoRestInterface();
	}

	@Test
	public void testInitMsoClient() throws Exception {
		MsoRestInterface testSubject;

		// default test
		testSubject = createTestSubject();
		testSubject.initMsoClient();
	}

}