/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia.
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

package org.onap.vid.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.onap.vid.utils.SystemPropertiesWrapper;

@RunWith(MockitoJUnitRunner.class)
public class AaiControllerTest {

    @Mock
    private AaiService aaiService;
    @Mock
    private AAIRestInterface aaiRestInterface;
    @Mock
    private RoleProvider roleProvider;
    @Mock
    private SystemPropertiesWrapper systemPropertiesWrapper;

    private AaiController aaiController;

    @Before
    public void setUp(){
        aaiController = new AaiController(aaiService, aaiRestInterface, roleProvider, systemPropertiesWrapper);
    }

    @Test
    public void getPortMirroringConfigData_givenThreeIds_ReturnsThreeResults() {

        final AaiResponseTranslator.PortMirroringConfigDataOk toBeReturnedForA = new AaiResponseTranslator.PortMirroringConfigDataOk("foobar");
        final AaiResponseTranslator.PortMirroringConfigDataError toBeReturnedForB = new AaiResponseTranslator.PortMirroringConfigDataError("foo", "{ baz: qux }");
        final AaiResponseTranslator.PortMirroringConfigDataOk toBeReturnedForC = new AaiResponseTranslator.PortMirroringConfigDataOk("corge");

        Mockito
                .doReturn(toBeReturnedForA)
                .doReturn(toBeReturnedForB)
                .doReturn(toBeReturnedForC)
                .when(aaiService).getPortMirroringConfigData(Mockito.anyString());

        final Map<String, AaiResponseTranslator.PortMirroringConfigData> result = aaiController.getPortMirroringConfigsData(ImmutableList.of("a", "b", "c"));

        assertThat(result, is(ImmutableMap.of(
                "a", toBeReturnedForA,
                "b", toBeReturnedForB,
                "c", toBeReturnedForC
        )));
    }
}
