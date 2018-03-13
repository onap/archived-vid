package org.onap.vid.aai.model;

import java.util.List;

import org.junit.Test;

public class GetServiceModelsByDistributionStatusResponseTest {

    private GetServiceModelsByDistributionStatusResponse createTestSubject() {
        return new GetServiceModelsByDistributionStatusResponse();
    }

    @Test
    public void testGetResults() throws Exception {
        GetServiceModelsByDistributionStatusResponse testSubject;
        List<Result> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResults();
    }

    @Test
    public void testSetResults() throws Exception {
        GetServiceModelsByDistributionStatusResponse testSubject;
        List<Result> results = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setResults(results);
    }
}