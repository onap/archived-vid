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

package org.onap.vid.services;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiationTemplate;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class InstantiationTemplatesServiceTest {

    @Mock
    private AsyncInstantiationRepository asyncInstantiationRepository;

    @Mock
    private ModelUtil modelUtil;

    @Mock
    private FeatureManager featureManager;

    @InjectMocks
    private InstantiationTemplatesService instantiationTemplatesService;

    @BeforeMethod
    public void initMocks() {
        TestUtils.initMockitoMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(featureManager);
    }

    @Test
    public void getJobRequestAsTemplate_whenIsCalled_asyncInstantiationRepositoryGetJobRequestIsInvoked() {
        UUID jobId = UUID.randomUUID();
        ServiceInstantiation serviceInstantiationMock = mock(ServiceInstantiation.class, RETURNS_DEEP_STUBS);
        doReturn(serviceInstantiationMock).when(asyncInstantiationRepository).getJobRequest(jobId);

        // When...
        instantiationTemplatesService.getJobRequestAsTemplate(jobId);

        verify(asyncInstantiationRepository).getJobRequest(jobId);
    }

    @Test
    public void getJobRequestAsTemplate_givenModelUtilReturnsValue_thenVnfCounterMapIsPopulatedWithThatValue() {
        Map<String, Integer> dummyNonEmptyMap = ImmutableMap.of("dummyKey", 9);
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class, RETURNS_DEEP_STUBS);
        doReturn(serviceInstantiation).when(asyncInstantiationRepository).getJobRequest(any());

        // Given...
        when(modelUtil.getExistingCounterMap(any(), any())).thenAnswer(
            // return empty counterMap if argument is an empty map; otherwise return a mocked response
            invocation -> ((Map)invocation.getArgument(0)).size() == 0 // isEmpty() does not work on mocks
                ? ImmutableMap.of()
                : dummyNonEmptyMap
        );

        // only vnf will have a non-empty value
        when(serviceInstantiation.getVnfs()).thenReturn(ImmutableMap.of("1", mock(Vnf.class)));

        // When...
        ServiceInstantiationTemplate result = instantiationTemplatesService.getJobRequestAsTemplate(UUID.randomUUID());

        assertThat(result, hasProperty("existingVNFCounterMap", jsonEquals(dummyNonEmptyMap)));
        assertThat(result, hasProperty("existingNetworksCounterMap", anEmptyMap()));
        assertThat(result, hasProperty("existingVnfGroupCounterMap", anEmptyMap()));
        assertThat(result, hasProperty("existingVRFCounterMap", anEmptyMap()));
    }

    @DataProvider
    public static Object[][] isTemplatesExistsByGivenServiceUuid() {
        return new Object[][]{{"1",TRUE},
                              {"3",FALSE}};
    }

    @Test(dataProvider = "isTemplatesExistsByGivenServiceUuid")
    public void setInServicesTemplateValue_givenServiceWithServiceModelId_thenIsTemplateExistsIsEatherTrueOrFalse(String givenUuid, Boolean expectedTemplatesExist){

        Service service = new Service();
        service.setUuid(givenUuid);

        Service newService = instantiationTemplatesService.setTemplateExistForService(service, ImmutableSet.of("1", "2"));
        assertThat(newService.getIsInstantiationTemplateExists(), is(expectedTemplatesExist));
    }

    @Test
    public void setTemplatesExistance_givenCollection__flagIsActive_thenSameCollectionReturnedWithTemplateExistsProperty(){
        when(featureManager.isActive(Features.FLAG_2004_INSTANTIATION_TEMPLATES_POPUP)).thenReturn(true);
        when(asyncInstantiationRepository.getAllTemplatesServiceModelIds()).thenReturn(ImmutableSet.of("1", "2"));
        Collection<Service> actualCollection = instantiationTemplatesService.setOnEachServiceIsTemplateExists(createGivenCollection());
        assertThat(actualCollection, containsInAnyOrder(
            allOf(hasProperty("uuid", is("1")), hasProperty("isInstantiationTemplateExists", is(true))),
            allOf(hasProperty("uuid", is("3")), hasProperty("isInstantiationTemplateExists", is(false)))
        ));
    }

    @Test
    public void setTemplatesExistance_givenCollection_flagIsNotActive_thenTemplatesExistNotAdded(){
        when(featureManager.isActive(Features.FLAG_2004_INSTANTIATION_TEMPLATES_POPUP)).thenReturn(false);
        Collection<Service> actualCollection = instantiationTemplatesService.setOnEachServiceIsTemplateExists(createGivenCollection());
        assertThat("was " + actualCollection, actualCollection, containsInAnyOrder(
            allOf(hasProperty("uuid", is("1")), hasProperty("isInstantiationTemplateExists", is(false))),
            allOf(hasProperty("uuid", is("3")), hasProperty("isInstantiationTemplateExists", is(false)))
        ));
    }

    private Collection<Service> createGivenCollection(){
        Service service1 = new Service();
        Service service2 = new Service();
        service1.setUuid("1");
        service2.setUuid("3");
        return ImmutableList.of(service1, service2);
    }

}
