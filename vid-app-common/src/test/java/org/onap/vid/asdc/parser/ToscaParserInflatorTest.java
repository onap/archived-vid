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
import static org.onap.vid.asdc.parser.ToscaParserInflatorTest.INFLATION_MODE.ByCustomizationId;
import static org.onap.vid.asdc.parser.ToscaParserInflatorTest.INFLATION_MODE.ByVersionId;

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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ToscaParserInflatorTest {

    private static final Logger log = LogManager.getLogger(ToscaParserInflatorTest.class);

    @InjectMocks
    private ToscaParserImpl2 toscaParserImpl2;

    @Mock
    private VidNotionsBuilder vidNotionsBuilder;

    private AsdcClient asdcClient;

    enum INFLATION_MODE {
        ByVersionId, ByCustomizationId
    }

    @DataProvider
    public static Object[][] inflationModes() {
        return new Object[][] {
            {ByVersionId}, {ByCustomizationId}
        };
    }

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


    @Test(dataProvider = "inflationModes")
    public void inflateFabricConfigurationModel_noIdsAreGiven(INFLATION_MODE inflationMode) throws Exception {
        final String fabricConfigurationUuid = "12344bb4-a416-4b4e-997e-0059973630b9";
        final Map<String, Names> inflated = inflateModelByUuid(fabricConfigurationUuid, inflationMode);

        // see vf-with-annotation-csar.json
        assertThat(inflated, is(ImmutableMap.of(
            inflationMode == ByVersionId ? "ea81d6f7-0861-44a7-b7d5-d173b562c350" : "41516cc6-5098-4b40-a619-f8d5f55fc4d8",
            doubleName("2017-488_PASQUALE-vPE 0"),

            inflationMode == ByVersionId ? "a5d8df05-11cb-4351-96e0-b6d4168ea4df" : "f3d97417-0c8d-424e-8ff7-b2eb4fbcecc3",
            new Names("2017488PasqualeVpe..PASQUALE_vRE_BV..module-1", "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1"),

            inflationMode == ByVersionId ? "b3e8b26e-cff0-49fc-a4e6-f3e16c8440fe" : "6e410843-257c-46d9-ba8a-8d94e1362452",
            new Names("2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2", "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2"),

            inflationMode == ByVersionId ? "040e591e-5d30-4e0d-850f-7266e5a8e013" : "5c5f91f9-5e31-4120-b892-5536587ec258",
            new Names("2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0", "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0")
        )));
    }


    @Test(dataProvider = "inflationModes")
    public void inflateVlModel_allIdsAreGiven(INFLATION_MODE inflationMode) throws Exception {
        final String fabricConfigurationUuid = "cb49608f-5a24-4789-b0f7-2595473cb997";
        final Map<String, Names> inflated = inflateModelByUuid(fabricConfigurationUuid, inflationMode);

        // see vl-csar.json
        if (inflationMode == ByVersionId) {
            assertThat(inflated, is(ImmutableMap.of(
                "af584529-d7f0-420e-a6f3-c38b689c030f", doubleName("ExtVL 0")
            )));
        } else {
            assertThat(inflated, is(ImmutableMap.of(
                "664f8aa7-3989-46ac-81c0-dd72a8a63f26", doubleName("ExtVL 0")
            )));
        }
    }

    @NotNull
    private Names doubleName(String modelCustomizationName) {
        return new Names(modelCustomizationName, modelCustomizationName);
    }

    @Test(dataProvider = "inflationModes")
    public void inflateConfigurationByPolicyFalseUuid_allIdsAreGiven(INFLATION_MODE inflationMode) throws Exception {
        final String configurationByPolicyFalseUuid = "ee6d61be-4841-4f98-8f23-5de9da845544";
        final Map<String, Names> inflated = inflateModelByUuid(configurationByPolicyFalseUuid, inflationMode);

        // see policy-configuration-by-policy-false.json
        // no relevant model here
        assertThat(inflated, is(emptyMap()));
    }

    private Map<String, Names> inflateModelByUuid(String fabricConfigurationUuid, INFLATION_MODE inflationMode) throws SdcToscaParserException, AsdcCatalogException {
        ServiceModel actualServiceModel = serviceModelByUuid(fabricConfigurationUuid);

        ServiceModelInflator serviceModelInflator = new ServiceModelInflator();
        return inflationMode == ByVersionId
            ? serviceModelInflator.toNamesByVersionId(actualServiceModel)
            : serviceModelInflator.toNamesByCustomizationId(actualServiceModel);
    }

    private ServiceModel serviceModelByUuid(String uuid) throws SdcToscaParserException, AsdcCatalogException {
        final Path modelPath = asdcClient.getServiceToscaModel(UUID.fromString(uuid));
        final Service modelMetadata = asdcClient.getService(UUID.fromString(uuid));

        return toscaParserImpl2.makeServiceModel(modelPath, modelMetadata);
    }


}
