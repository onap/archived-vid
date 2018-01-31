package org.onap.vid.asdc.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.onap.vid.controller.WebConfig;
import org.onap.vid.model.VfModule;
import org.onap.vid.model.VolumeGroup;
import org.onap.vid.properties.AsdcClientConfiguration;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.sdc.tosca.parser.api.ISdcCsarHelper;
import org.openecomp.sdc.toscaparser.api.Group;
import org.openecomp.sdc.toscaparser.api.NodeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@Test
@ContextConfiguration(classes = { WebConfig.class, AsdcClientConfiguration.class, SystemProperties.class })
@WebAppConfiguration
public class ToscaParserImpl2Test extends AbstractTestNGSpringContextTests {

    private final String myUUID = "myUUID";
    private static final Logger log = Logger.getLogger(ToscaParserImpl2Test.class);

    @Autowired
    private ToscaParserImpl2 toscaParserImpl2;

    @Autowired
    private WebApplicationContext wac;

    @BeforeMethod
    private void verifyWiring() {
        Assert.assertNotNull(wac);
        Assert.assertNotNull(toscaParserImpl2);
    }

    @Test
    public void testGetNFModuleFromVf() throws Exception {
        ISdcCsarHelper csarHelper = getMockedSdcCsarHelper();

        Map<String, VfModule> vfModulesFromVF = toscaParserImpl2.getVfModulesFromVF(csarHelper, myUUID);

        assertThat(vfModulesFromVF, allOf(
                aMapWithSize(2),
                hasKey("withoutVol"),
                hasKey("withVol")
        ));

        verify(csarHelper, only()).getVfModulesByVf(anyString());
    }

    @Test
    public void testGetVolumeGroupsFromVF() throws Exception {
        ISdcCsarHelper csarHelper = getMockedSdcCsarHelper();

        Map<String, VolumeGroup> volumeGroupsFromVF = toscaParserImpl2.getVolumeGroupsFromVF(csarHelper, myUUID);

        assertThat(volumeGroupsFromVF, allOf(
                aMapWithSize(1),
                hasKey("withVol")
        ));

        verify(csarHelper, only()).getVfModulesByVf(anyString());
    }

    private ISdcCsarHelper getMockedSdcCsarHelper() {
        ISdcCsarHelper csarHelper = mock(ISdcCsarHelper.class);

//        ThreadLocalsHolder.setCollector(new ExceptionCollector("c:\\temp\\foo"));

        Group withVol = createMinimalGroup("withVol", true);
        Group withoutVol = createMinimalGroup("withoutVol", false);

        when(csarHelper.getVfModulesByVf(myUUID))
                .thenReturn(ImmutableList.of(withVol, withoutVol));

        return csarHelper;
    }

    private static Group createMinimalGroup(String name, boolean isVolumeGroup) {
        LinkedHashMap<String, Object>
                templates,
                properties,
                metadata,
                customDef,
                vfModule,
                vfModuleProperties,
                volumeGroup;

        templates = new LinkedHashMap<>();
        templates.put("type", "org.onap.groups.VfModule");

        properties = addNewNamedMap(templates, "properties");
        properties.put("volume_group", isVolumeGroup);

        metadata = addNewNamedMap(templates, "metadata");

        ArrayList<NodeTemplate> memberNodes = new ArrayList<>();

        customDef = new LinkedHashMap<>();
        vfModule = addNewNamedMap(customDef, "org.onap.groups.VfModule");
        vfModuleProperties = addNewNamedMap(vfModule, "properties");
//        vfModule.put("derived_from", "tosca.groups.Root");
//        vfModule.put("description", "Grouped all heat resources which are in the same VF Module");

        volumeGroup = addNewNamedMap(vfModuleProperties, "volume_group");
//        volumeGroup.put("description", "volume_group");
        volumeGroup.put("type", "boolean");
        volumeGroup.put("default", false);
        volumeGroup.put("required", true);


        Group group = new Group(
                name,
                templates,
                memberNodes,
                customDef
        );

        try {
            log.info(String.format("Built a group: %s",
                    (new com.fasterxml.jackson.databind.ObjectMapper())
                            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                            .writeValueAsString(group)
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return group;
    }

    private static LinkedHashMap<String, Object> addNewNamedMap(LinkedHashMap<String, Object> root, String key) {
        LinkedHashMap<String, Object> properties = new LinkedHashMap<>();
        root.put(key, properties);
        return properties;
    }

}
