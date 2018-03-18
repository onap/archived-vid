package org.onap.portalapp.conf;

import org.junit.Test;

public class HibernateMappingLocationsTest {

    private HibernateMappingLocations createTestSubject() {
        return new HibernateMappingLocations();
    }

    @Test
    public void testGetMappingLocations() throws Exception {
        HibernateMappingLocations testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.getMappingLocations();
    }

    @Test
    public void testGetPackagesToScan() throws Exception {
        HibernateMappingLocations testSubject;
        String[] result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getPackagesToScan();
    }
}