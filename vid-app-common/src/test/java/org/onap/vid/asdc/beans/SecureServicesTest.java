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

package org.onap.vid.asdc.beans;

import com.google.common.collect.ImmutableList;
import java.util.Collection;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;

import java.util.Set;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.CloudRegion;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.services.InstantiationTemplatesService;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.BeforeMethod;

public class SecureServicesTest {

    @Mock
    private AsyncInstantiationRepository asyncInstantiationRepository;

    @InjectMocks
    private InstantiationTemplatesService instantiationTemplatesService;

    @BeforeMethod
    public void initMocks() {
        TestUtils.initMockitoMocks(this);
    }

    private SecureServices createTestSubject() {
        return new SecureServices();
    }

    @Test
    public void setTemplateExistsOnService(){
        Collection<Service> servcies = createGivenCollection();
        Collection<String> serviceModelIdsFromDB=  mock(Collection.class, RETURNS_DEEP_STUBS);
        doReturn(serviceModelIdsFromDB).when(asyncInstantiationRepository).getAllTemplatesServiceModelIds();

        instantiationTemplatesService.setOnEachServiceIsTemplateExists(servcies);
        verify(asyncInstantiationRepository).getAllTemplatesServiceModelIds();

    }
    @Test
    public void testSetServices() throws Exception {
        SecureServices testSubject;
        Collection<Service> services = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setServices(services);
    }

    @Test
    public void testGetServices() throws Exception {
        SecureServices testSubject;
        Collection<Service> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getServices();
    }

    @Test
    public void testIsReadOnly() throws Exception {
        SecureServices testSubject;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.isReadOnly();
    }

    @Test
    public void testSetReadOnly() throws Exception {
        SecureServices testSubject;
        boolean readOnly = false;

        // default test
        testSubject = createTestSubject();
        testSubject.setReadOnly(readOnly);
    }

    private Collection<Service> createGivenCollection(){
        Service service1 = new Service();
        Service service2 = new Service();
        service1.setUuid("1");
        service2.setUuid("3");
        return ImmutableList.of(service1, service2);
    }
}
