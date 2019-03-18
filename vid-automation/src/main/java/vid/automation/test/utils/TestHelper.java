package vid.automation.test.utils;

import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class TestHelper {

    public static String GET_SERVICE_MODELS_BY_DISTRIBUTION_STATUS = "getServiceModelsByDistributionStatus";
    public static String GET_TENANTS = "getTenants";

    public static void resetAaiCache(String cacheName, RestTemplate restTemplate, URI vidUri) {
        restTemplate.delete(vidUri + "/aai_reset_cache/"+cacheName);
    }
}
