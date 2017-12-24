package org.opencomp.vid.mso;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.controller.MsoConfig;
import org.openecomp.vid.controller.OperationalEnvironmentController;
import org.openecomp.vid.controller.WebConfig;
import org.openecomp.vid.mso.MsoBusinessLogic;
import org.openecomp.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.openecomp.vid.mso.rest.RequestDetails;
import org.openecomp.vid.properties.AsdcClientConfiguration;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Test
@ContextConfiguration(classes = { WebConfig.class, AsdcClientConfiguration.class, SystemProperties.class, MsoConfig.class })
@WebAppConfiguration
public class MsoBusinessLogicTest extends AbstractTestNGSpringContextTests {

    @Inject
    private MsoBusinessLogic msoBusinessLogic;

    @Test(dataProvider = "getOperationalEnvironmentActivationPermutations")
    public void testJsonResultOfOperationalEnvironmentActivationRequestDetails(HashMap<String, String> permutation) throws IOException {

        // Convert 'manifest' to model
        final Object manifest = new ObjectMapper().readerFor(Object.class).readValue(permutation.get("<manifest>"));

        // build OperationalEnvironmentActivateInfo
        OperationalEnvironmentActivateInfo inputUnderTest = createOperationalEnvironmentActivateInfo("<instanceId>", permutation.get("<userId>"), manifest, permutation.get("<relatedInstanceId>"), permutation.get("<relatedInstanceName>"), permutation.get("<workloadType>"));

        // transform to RequestDetails, and serialize to json
        //   -> actually, the function "msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails"
        //      is the code under test here
        final RequestDetails operationalEnvironmentActivationRequestDetails = msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails(inputUnderTest);
        final String requestDetailsAsString = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(ImmutableMap.of("requestDetails", operationalEnvironmentActivationRequestDetails));

        // load expected result, and populate it with current input 'permutation' values
        final URL resource = this.getClass().getResource("/activateOperationalEnvironmentsPayloadToMso.json");
        String expected = IOUtils.toString(resource, "UTF-8");

        for (Map.Entry<String, String> stringStringEntry : permutation.entrySet()) {
            expected = expected.replaceAll(stringStringEntry.getKey(), stringStringEntry.getValue());
        }

        // assert for exact match
        try {
            JSONAssert.assertEquals("built mso request is not ok", expected, requestDetailsAsString, JSONCompareMode.STRICT);
        } catch (Exception e) {
            System.out.println("requestDetailsAsString: \n" + requestDetailsAsString);
            System.out.println("expected: \n" + expected);
            throw e;
        }
    }

    @DataProvider
    private Object[][] getOperationalEnvironmentActivationPermutations() throws IOException {
        final String manifest = "" +
                "{" +
                "  \"serviceModelList\": [" +
                "    {" +
                "      \"serviceModelVersionId\": \"uuid2\"," +
                "      \"recoveryAction\": \"retry\"" +
                "    }" +
                "  ]" +
                "}";


        final ImmutableListMultimap<String, String> options = ImmutableListMultimap.<String, String>builder()
                .putAll("<instanceId>", "instanceId")
                .putAll("<userId>", "1", "0198adb8-87fd-46ef-94ae-258816629c8b")
                .putAll("<relatedInstanceId>", "relatedInstanceId", "2744cf56-4f00-4e48-917b-c3bd3b1f8984")
                .putAll("<relatedInstanceName>", "relatedInstanceName", "Brooklynn Puanani")
                .putAll("<workloadType>", "workloadType", "E2E-extreme")
                .putAll("<manifest>", manifest, "{ \"my-manifest-key\": \"my-manifest-value\"}")
                .build();

        List<HashMap<String, String>> permutations = permuteOptions(options);

        return permutations.stream().map(m -> new Object[] { m }).collect(Collectors.toList()).toArray(new Object[0][0]);
    }

    private List<HashMap<String, String>> permuteOptions(ImmutableListMultimap<String, String> options) {
        // try any value, where the other keys are on the default one (index zero)
        // result it's not the whole world of permutations, but rather a skim set, which its
        // size is as the number of unique values in "options"

        HashMap<String, String> baseMutation = new HashMap<>();
        for (String key : options.keySet()) {
            baseMutation.put(key, options.get(key).get(0));
        }

        List<HashMap<String, String>> res = new LinkedList<>();
        res.add(baseMutation);

        for (String key : options.keySet()) {
            final ImmutableList<String> entry = options.get(key);
            for (String value : entry.subList(1, entry.size())) { // skip the first option at index zero
                HashMap<String, String> mutation = new HashMap<>();
                mutation.putAll(baseMutation);
                mutation.put(key, value);
                res.add(mutation);
            }
        }
        return res;
    }

    private OperationalEnvironmentActivateInfo createOperationalEnvironmentActivateInfo(String operationalEnvId, String userId, Object manifest, String relatedInstanceId, String relatedInstanceName, String workloadType) {
        OperationalEnvironmentController.OperationalEnvironmentActivateBody body = new OperationalEnvironmentController.OperationalEnvironmentActivateBody(relatedInstanceId, relatedInstanceName, workloadType, manifest);
        return new OperationalEnvironmentActivateInfo(body, userId, operationalEnvId);
    }

}
