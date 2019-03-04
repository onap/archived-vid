package org.onap.vid.mso.rest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class InstanceIdsTest {

    private InstanceIds instanceIds;

    private String propertyName = "testProperty";
    private String additionalProperty = "testAdditionalProperty";
    private String networkInstanceId = "testNetworkId";
    private String serviceInstanceId = "testServiceId";

    @BeforeMethod
    public void setUp() {
        instanceIds = new InstanceIds();
    }

    @Test
    public void shouldHaveProperSettersAndGetters() {
        assertThat(InstanceIds.class, hasValidGettersAndSettersExcluding("additionalProperties"));
    }

    @Test
    public void shouldHaveProperGetterAndSetterForAdditionalProperties() {
        //	when
        instanceIds.setAdditionalProperty(propertyName,additionalProperty);

        //	then
        assertThat( instanceIds.getAdditionalProperties().get(propertyName) ).isEqualTo(additionalProperty);
    }

    @Test
    public void shouldProperlyConvertRelatedInstanceObjectToString() {
        //	given
        instanceIds.setNetworkInstanceId(networkInstanceId);
        instanceIds.setServiceInstanceId(serviceInstanceId);
        instanceIds.setAdditionalProperty(propertyName,additionalProperty);

        //	when
        String response = instanceIds.toString();

        //	then
        assertThat(response).contains(
                "[networkInstanceId="+networkInstanceId +
                        ",serviceInstanceId="+serviceInstanceId +
                        ",vfModuleInstanceId=<null>," +
                        "vnfInstanceId=<null>" +
                        ",volumeGroupInstanceId=<null>" +
                        ",additionalProperties={"+propertyName+"="+additionalProperty+"}]"
        );
    }

    @Test
    public void shouldProperlyCheckIfObjectsAreEqual() {
        //	given
        InstanceIds sameInstanceIds = new InstanceIds();
        InstanceIds differentInstanceIds = new InstanceIds();

        instanceIds.setNetworkInstanceId(networkInstanceId);
        sameInstanceIds.setNetworkInstanceId(networkInstanceId);

        instanceIds.setServiceInstanceId(serviceInstanceId);
        sameInstanceIds.setServiceInstanceId(serviceInstanceId);

        //	when
        boolean sameResponse = instanceIds.equals(instanceIds);
        boolean equalResponse = instanceIds.equals(sameInstanceIds);
        boolean differentResponse = instanceIds.equals(differentInstanceIds);
        boolean differentClassResponse = instanceIds.equals("RelatedInstance");

        //	then
        assertThat(sameResponse).isEqualTo(true);
        assertThat(equalResponse).isEqualTo(true);

        assertThat(differentResponse).isEqualTo(false);
        assertThat(differentClassResponse).isEqualTo(false);
    }
}
