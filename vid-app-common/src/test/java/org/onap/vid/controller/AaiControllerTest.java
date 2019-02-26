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

package org.onap.vid.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.services.AaiService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AaiControllerTest {

    @InjectMocks
    AaiController aaiController = new AaiController();

    @Mock
    AaiService aaiService;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
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
