/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.mso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.controller.OperationalEnvironmentController;
import org.onap.vid.controller.OperationalEnvironmentController.OperationalEnvironmentManifest;
import org.onap.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.onap.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.onap.vid.mso.rest.OperationalEnvironment.OperationEnvironmentRequestDetails;
import org.onap.vid.mso.rest.RequestDetails;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MsoOperationalEnvironmentTest {

    private MsoBusinessLogic msoBusinessLogic = new MsoBusinessLogicImpl(null, null);
    private static final Logger logger = LogManager.getLogger(MsoOperationalEnvironmentTest.class);

    @Test(dataProvider = "getOperationalEnvironmentActivationPermutations")
    public void testJsonResultOfOperationalEnvironmentActivationRequestDetails(HashMap<String, String> permutation) throws IOException {

        // Convert 'manifest' to model
        final OperationalEnvironmentManifest manifest = new ObjectMapper().readerFor(OperationalEnvironmentManifest.class).readValue(permutation.get("<manifest>"));

        // build OperationalEnvironmentActivateInfo
        OperationalEnvironmentActivateInfo inputUnderTest = createOperationalEnvironmentActivateInfo("<instanceId>", permutation.get("<userId>"), manifest, permutation.get("<relatedInstanceId>"), permutation.get("<relatedInstanceName>"), permutation.get("<workloadContext>"));

        // transform to RequestDetails, and serialize to json
        //   -> actually, the function "msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails"
        //      is the code under test here
        final RequestDetailsWrapper<RequestDetails> operationalEnvironmentActivationRequestDetails = msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails(inputUnderTest);

        String expected = buildExpectation("payload_jsons/activateOperationalEnvironmentsPayloadToMso.json", permutation);

        assertThatExpectationIsLikeObject(expected, operationalEnvironmentActivationRequestDetails);
    }

    @DataProvider
    private Object[][] getOperationalEnvironmentActivationPermutations() {
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
                .putAll("<workloadContext>", "workloadContext", "VNF_E2E-extreme")
                .putAll("<manifest>", manifest)
                .build();

        List<HashMap<String, String>> permutations = permuteOptions(options);

        return permutations.stream().map(m -> new Object[] { m }).collect(Collectors.toList()).toArray(new Object[0][0]);
    }

    @Test(dataProvider = "getOperationalEnvironmentCreationPermutations")
    public void testJsonResultOfOperationalEnvironmentCreationRequestDetails(HashMap<String, String> permutation) throws IOException {

        // build OperationalEnvironmentCreateBody
        OperationalEnvironmentController.OperationalEnvironmentCreateBody inputUnderTest = createOperationalEnvironmentCreateBody(permutation.get("<instanceName>"), permutation.get("<ecompInstanceId>"), permutation.get("<ecompInstanceName>"), permutation.get("<operationalEnvType>"), permutation.get("<tenantContext>"), permutation.get("<workloadContext>"));

        // transform to RequestDetails, and serialize to json
        //   -> actually, the function "msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails"
        //      is the code under test here
        final RequestDetailsWrapper<OperationEnvironmentRequestDetails> operationalEnvironmentCreationRequestDetails = msoBusinessLogic.convertParametersToRequestDetails(inputUnderTest, permutation.get("<userId>"));

        String expected = buildExpectation("payload_jsons/createOperationalEnvironmentsPayloadToMso.json", permutation);

        assertThatExpectationIsLikeObject(expected, operationalEnvironmentCreationRequestDetails);
    }

    @DataProvider
    private Object[][] getOperationalEnvironmentCreationPermutations() {

        final ImmutableListMultimap<String, String> options = ImmutableListMultimap.<String, String>builder()
                // instanceName, ecompInstanceId, ecompInstanceName, operationalEnvType, tenantContext, workloadContext
                .putAll("<userId>", "1", "ceb60bba-7c18-49cd-a8f6-83ff2e1430b0", "Storm Landebert")
                .putAll("<instanceName>", "instanceName", "Slavica Hadrien")
                .putAll("<ecompInstanceId>", "ecompInstanceId", "58ec6753-957f-4124-8f92-c1c0bd2464a4")
                .putAll("<ecompInstanceName>", "ecompInstanceName", "Bente Keelin")
                .putAll("<operationalEnvType>", "operationalEnvType", "VNF")
                .putAll("<tenantContext>", "tenantContext", "Production")
                .putAll("<workloadContext>", "workloadContext", "E2E-extreme")
                .build();

        List<HashMap<String, String>> permutations = permuteOptions(options);

        return permutations.stream().map(m -> new Object[] { m }).collect(Collectors.toList()).toArray(new Object[0][0]);
    }

    @Test(dataProvider = "getOperationalEnvironmentDeactivationPermutations")
    public void testJsonResultOfOperationalEnvironmentDeactivationRequestDetails(HashMap<String, String> permutation) throws IOException {

        OperationalEnvironmentDeactivateInfo inputUnderTest = createOperationalEnvironmentDeactivateInfo("operationalEnvId>", permutation.get("<userId>"));

        final RequestDetailsWrapper<RequestDetails> operationalEnvironmentDeactivationRequestDetails = msoBusinessLogic.createOperationalEnvironmentDeactivationRequestDetails(inputUnderTest);

        String expected = buildExpectation("payload_jsons/deactivateOperationalEnvironmentsPayloadToMso.json", permutation);

        assertThatExpectationIsLikeObject(expected, operationalEnvironmentDeactivationRequestDetails);
    }

    @DataProvider
    private Object[][] getOperationalEnvironmentDeactivationPermutations() {

        final ImmutableListMultimap<String, String> options = ImmutableListMultimap.<String, String>builder()
                .putAll("<userId>", "instanceName", "Slavica Hadrien")
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

    public static void assertThatExpectationIsLikeObject(String expected, Object requestDetails) throws JsonProcessingException {
        final String requestDetailsAsString = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(requestDetails);

        // assert for exact match
        try {
            JSONAssert.assertEquals("built mso request is not ok", expected, requestDetailsAsString, JSONCompareMode.STRICT);
        } catch (AssertionError | Exception e) {
            logger.info("requestDetailsAsString: \n" + requestDetailsAsString);
            logger.info("expected: \n" + expected);
            throw e;
        }
    }

    private String buildExpectation(String modelFileName, HashMap<String, String> permutation) throws IOException {
        // load expected result, and populate it with current input 'permutation' values
        final URL resource = this.getClass().getResource("/" + modelFileName);
        String expected = IOUtils.toString(resource, "UTF-8");

        for (Map.Entry<String, String> stringStringEntry : permutation.entrySet()) {
            expected = expected.replaceAll(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        return expected;
    }

    private OperationalEnvironmentActivateInfo createOperationalEnvironmentActivateInfo(String operationalEnvId, String userId, OperationalEnvironmentManifest manifest, String relatedInstanceId, String relatedInstanceName, String workloadContext) {
        OperationalEnvironmentController.OperationalEnvironmentActivateBody body = new OperationalEnvironmentController.OperationalEnvironmentActivateBody(relatedInstanceId, relatedInstanceName, workloadContext, manifest);
        return new OperationalEnvironmentActivateInfo(body, userId, operationalEnvId);
    }

    private OperationalEnvironmentDeactivateInfo createOperationalEnvironmentDeactivateInfo(String operationalEnvId, String userId) {
        return new OperationalEnvironmentDeactivateInfo(userId, operationalEnvId);
    }

    private OperationalEnvironmentController.OperationalEnvironmentCreateBody createOperationalEnvironmentCreateBody(String instanceName, String ecompInstanceId, String ecompInstanceName, String operationalEnvType, String tenantContext, String workloadContext) {
        return new OperationalEnvironmentController.OperationalEnvironmentCreateBody(instanceName, ecompInstanceId, ecompInstanceName, operationalEnvType, tenantContext, workloadContext);
    }
}
