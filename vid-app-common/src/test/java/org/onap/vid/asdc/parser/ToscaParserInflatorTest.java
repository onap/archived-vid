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

package org.onap.vid.asdc.parser;

import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.local.LocalAsdcClient;
import org.onap.vid.asdc.parser.ServiceModelInflator.Names;
import org.onap.vid.model.ServiceModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ToscaParserInflatorTest {

    private static final Logger log = LogManager.getLogger(ToscaParserInflatorTest.class);

    @InjectMocks
    private ToscaParserImpl2 toscaParserImpl2;

    @Mock
    private VidNotionsBuilder vidNotionsBuilder;

    private AsdcClient asdcClient;

    @BeforeClass
    void init() throws IOException {

        final InputStream asdcServicesFile = this.getClass().getClassLoader().getResourceAsStream("sdcservices.json");

        final JSONTokener jsonTokener = new JSONTokener(IOUtils.toString(asdcServicesFile));
        final JSONObject sdcServicesCatalog = new JSONObject(jsonTokener);

        asdcClient = new LocalAsdcClient.Builder().catalog(sdcServicesCatalog).build();

    }

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void inflateFabricConfigurationModel_allIdsAreGiven() throws Exception {
        final String fabricConfigurationUuid = "12344bb4-a416-4b4e-997e-0059973630b9";
        final Map<String, Names> inflated = inflateModelByUuid(fabricConfigurationUuid);

        // see vf-with-annotation-csar.json
        assertThat(inflated, is(ImmutableMap.of(
                "ea81d6f7-0861-44a7-b7d5-d173b562c350", doubleName("2017-488_PASQUALE-vPE 0"),
                "a5d8df05-11cb-4351-96e0-b6d4168ea4df", new Names("2017488PasqualeVpe..PASQUALE_vRE_BV..module-1", "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1"),
                "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe", new Names("2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2","2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2"),
                "040e591e-5d30-4e0d-850f-7266e5a8e013", new Names("2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0","2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0")
        )));
    }


    @Test
    public void inflateVlModel_allIdsAreGiven() throws Exception {
        final String fabricConfigurationUuid = "cb49608f-5a24-4789-b0f7-2595473cb997";
        final Map<String, Names> inflated = inflateModelByUuid(fabricConfigurationUuid);

        // see vl-csar.json
        assertThat(inflated, is(ImmutableMap.of(
                "af584529-d7f0-420e-a6f3-c38b689c030f", doubleName("ExtVL 0")
        )));
    }

    @NotNull
    private Names doubleName(String modelCustomizationName) {
        return new Names(modelCustomizationName, modelCustomizationName);
    }

    @Test
    public void inflateConfigurationByPolicyFalseUuid_allIdsAreGiven() throws Exception {
        final String configurationByPolicyFalseUuid = "ee6d61be-4841-4f98-8f23-5de9da845544";
        final Map<String, Names> inflated = inflateModelByUuid(configurationByPolicyFalseUuid);

        // see policy-configuration-by-policy-false.json
        // no relevant model here
        assertThat(inflated, is(emptyMap()));
    }

    private Map<String, Names> inflateModelByUuid(String fabricConfigurationUuid) throws SdcToscaParserException, AsdcCatalogException {
        ServiceModel actualServiceModel = serviceModelByUuid(fabricConfigurationUuid);

        ServiceModelInflator serviceModelInflator = new ServiceModelInflator();
        return serviceModelInflator.toNamesByVersionId(actualServiceModel);
    }

    private ServiceModel serviceModelByUuid(String uuid) throws SdcToscaParserException, AsdcCatalogException {
        final Path modelPath = asdcClient.getServiceToscaModel(UUID.fromString(uuid));
        final Service modelMetadata = asdcClient.getService(UUID.fromString(uuid));

        return toscaParserImpl2.makeServiceModel(modelPath, modelMetadata);
    }


}
