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