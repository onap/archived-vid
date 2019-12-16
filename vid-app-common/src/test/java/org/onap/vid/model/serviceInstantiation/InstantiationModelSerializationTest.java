package org.onap.vid.model.serviceInstantiation;

import static java.util.Collections.emptyMap;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.onap.vid.model.Action.Create;
import static org.onap.vid.testUtils.TestUtils.setStringsInStringProperties;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.VidNotions.InstantiationType;
import org.onap.vid.model.VidNotions.InstantiationUI;
import org.onap.vid.model.VidNotions.ModelCategory;
import org.onap.vid.mso.model.ModelInfo;
import org.testng.annotations.Test;

public class InstantiationModelSerializationTest {

    final ImmutableList<Map<String, String>> instanceParams =
        ImmutableList.of(
            ImmutableMap.of("one", "1"),
            ImmutableMap.of("two", "2")
        );

    @Test
    public void serializeAndDeserializeServiceInstantiation() throws Exception {

        ServiceInstantiation serviceInstantiation = new ServiceInstantiation(
            newModelInfo(),
            "owningEntityId",
            "owningEntityName",
            "projectName",
            "globalSubscriberId",
            "subscriberName",
            "productFamilyId",
            "instanceName",
            "subscriptionServiceType",
            "lcpCloudRegionId",
            "legacyRegion",
            "tenantId",
            "tenantName",
            "aicZoneId",
            "aicZoneName",
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap(),
            instanceParams,
            true,
            1,
            true,
            true,
            "testApi",
            "instanceId",
            "Delete",
            "trackById",
            true,
            "statusMessage",
            new VidNotions(InstantiationUI.ANY_ALACARTE_WHICH_NOT_EXCLUDED,
                ModelCategory.INFRASTRUCTURE_VPN,
                InstantiationUI.INFRASTRUCTURE_VPN,
                InstantiationType.Macro)
        );

        verifySerializationAndDeserialization(serviceInstantiation);
    }

    @Test
    public void serializeAndDeserializeVnf() throws Exception {

        Vnf vnf = new Vnf(
            newModelInfo(), "productFamilyId",
            "instanceName",
            "Upgrade",
            "platformName",
            "lcpCloudRegionId",
            "legacyRegion",
            "tenantId",
            instanceParams,
            "lineOfBusinessName",
            true,
            "instanceId",
            emptyMap(),
            "trackById",
            true,
            "statusMessage",
            5);

        verifySerializationAndDeserialization(vnf);
    }

    @Test
    public void serializeAndDeserializeVfModule() throws Exception {

        ImmutableMap<String, String> supplementaryParams = ImmutableMap.of(
            "uno", "1",
            "dos", "2",
            "tres", "3"
        );

        VfModule vfModule = new VfModule(
            newModelInfo(),
            "instanceName",
            "volumeGroupInstanceName",
            "Delete",
            "lcpCloudRegionId",
            "legacyRegion",
            "tenantId",
            instanceParams,
            supplementaryParams,
            true,
            true,
            "instanceId",
            "trackById",
            true,
            "statusMessage",
            true,
            true,
            1);

        verifySerializationAndDeserialization(vfModule);
    }

    private ModelInfo newModelInfo() {
        ModelInfo modelInfo = new ModelInfo();
        setStringsInStringProperties(modelInfo);
        return modelInfo;
    }

    private void verifySerializationAndDeserialization(Object object) throws Exception {

        assertThatAllValuesAreNotDefaultValues(object);

        String valueAsString = JACKSON_OBJECT_MAPPER.writeValueAsString(object);
        Object objectReconstructed = JACKSON_OBJECT_MAPPER.readValue(valueAsString, object.getClass());

        assertThat(valueAsString, jsonEquals(object));
        assertThat(objectReconstructed, samePropertyValuesAs(object));
        assertThat(JACKSON_OBJECT_MAPPER.writeValueAsString(objectReconstructed), jsonEquals(object));
    }

    private void assertThatAllValuesAreNotDefaultValues(Object object)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        assertThat("setup is expected to have no field with a default Java value",
            PropertyUtils.describe(object).entrySet(),
            not(hasItem(hasProperty("value",
                either(nullValue())
                    .or(equalTo(0))
                    .or(equalTo(""))
                    .or(equalTo(false))
                    .or(equalTo(Create))))));
    }

}
