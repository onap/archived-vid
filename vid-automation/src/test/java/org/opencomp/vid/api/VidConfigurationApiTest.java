package org.opencomp.vid.api;

import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class VidConfigurationApiTest extends BaseApiTest {

    @BeforeClass
    public void login() {
        super.login();
    }

    @Test
    public void whenGetUserTimezoneProperty_resultIsUTC() {
        String url = uri.toASCIIString() + "/get_property/user.timezone/abc";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getBody(), equalTo("UTC"));
    }

}
