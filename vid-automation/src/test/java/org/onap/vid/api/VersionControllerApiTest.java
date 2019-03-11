package org.onap.vid.api;

import org.junit.Assert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.util.HashMap;

public class VersionControllerApiTest extends BaseApiTest {

    @Test
    public void probeRequest_returnsResponseAsExpected() {
        // without log-in
        ResponseEntity<HashMap<String, String>> response = new RestTemplate().exchange(
                uri + "/version",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<HashMap<String, String>>() {
                });
        HashMap<String,String> versionResults = response.getBody();
        Assert.assertNotNull(versionResults.get("features"));
        Assert.assertNotNull(versionResults.get("build"));
    }
}
