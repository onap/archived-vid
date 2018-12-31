package org.onap.vid.asdc.parser;

import com.google.common.collect.ImmutableMap;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
        final String fabricConfigurationUuid = "90fe6842-aa76-4b68-8329-5c86ff564407";
        final Map<String, Names> inflated = inflateModelByUuid(fabricConfigurationUuid);

        // see vf-with-annotation-csar.json
        assertThat(inflated, is(ImmutableMap.of(
                "8df1892c-377d-460b-8a8d-fc8a116e9d92", doubleName("201712-488_ADIOD-vPE-1 0"),
                "8d521692-7661-4296-b77e-a2058bb62e87", new Names("201712488AdiodVpe1..ADIOD_vRE_BV..module-1", "201712488_adiodvpe10..201712488AdiodVpe1..ADIOD_vRE_BV..module-1"),
                "79fbee20-7fba-4166-ae4b-b94c869e7d8b", new Names("201712488AdiodVpe1..ADIOD_vPFE_BV..module-2","201712488_adiodvpe10..201712488AdiodVpe1..ADIOD_vPFE_BV..module-2"),
                "806505b8-7a7c-47a2-acef-b4d26fe95a92", new Names("201712488AdiodVpe1..ADIOD_base_vPE_BV..module-0","201712488_adiodvpe10..201712488AdiodVpe1..ADIOD_base_vPE_BV..module-0")
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
