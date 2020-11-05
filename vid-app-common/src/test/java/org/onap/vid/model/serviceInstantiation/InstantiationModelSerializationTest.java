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

package org.onap.vid.model.serviceInstantiation;

import static java.util.Collections.emptyMap;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonNodeAbsent;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.onap.vid.model.Action.Create;
import static org.onap.vid.model.serviceInstantiation.BaseResource.PauseInstantiation.afterCompletion;
import static org.onap.vid.testUtils.TestUtils.setStringsInStringFields;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.VidNotions.InstantiationType;
import org.onap.vid.model.VidNotions.InstantiationUI;
import org.onap.vid.model.VidNotions.ModelCategory;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.UserParamNameAndValue;
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
                InstantiationType.Macro),
            "originalName"
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
            5,
            "originalName");

        verifySerializationAndDeserialization(vnf);
    }

    @Test
    public void serializeAndDeserializeVfModule() throws Exception {

        List<UserParamNameAndValue> supplementaryParams = ImmutableList.of(
            new UserParamNameAndValue("uno", "1"),
            new UserParamNameAndValue("dos", "2"),
            new UserParamNameAndValue("tres", "3")
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
            1,
            afterCompletion,
            "originalName");

        verifySerializationAndDeserialization(vfModule);
    }

    @Test
    public void VfModule_sdncPreLoad_shouldBeSerializedWithCorrectName() {

        final boolean USE_PRELOAD = true;

        VfModule vfModule = new VfModule(newModelInfo(), null, null, null,
            null, null, null, null, null, false,
            /* HERE ====> */ USE_PRELOAD,
            null, null, null, null, null, null, null, null , null);

        assertThat(vfModule, jsonPartEquals("sdncPreLoad", USE_PRELOAD));
        assertThat(vfModule, jsonNodeAbsent("usePreload"));
    }

    @Test
    public void VfModule_volumeGroupName_shouldBeSerializedWithCorrectName() {

        final String VOLUME_GROUP_INSTANCE_NAME = "my volume group name";

        VfModule vfModule = new VfModule(newModelInfo(), null,
            /* HERE ====> */ VOLUME_GROUP_INSTANCE_NAME,
            null, null, null, null, null, null,
            false, null, null, null, null, null,
            null, null, null, null, null);

        assertThat(vfModule, jsonPartEquals("volumeGroupName", VOLUME_GROUP_INSTANCE_NAME));
        assertThat(vfModule, jsonNodeAbsent("volumeGroupInstanceName"));
    }

    private ModelInfo newModelInfo() {
        ModelInfo modelInfo = new ModelInfo();
        setStringsInStringFields(modelInfo);
        return modelInfo;
    }

    private void verifySerializationAndDeserialization(Object object) throws Exception {

        assertThatAllValuesAreNotDefaultValues(object);

        String valueAsString = JACKSON_OBJECT_MAPPER.writeValueAsString(object);
        Object objectReconstructed = JACKSON_OBJECT_MAPPER.readValue(valueAsString, object.getClass());

        // verify that all fields' values were reconstructed
        assertThat(objectReconstructed, samePropertyValuesAs(object));
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
