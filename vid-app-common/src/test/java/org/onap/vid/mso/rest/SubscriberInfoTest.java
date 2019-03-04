package org.onap.vid.mso.rest;

import org.assertj.core.api.AssertionsForClassTypes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.hamcrest.MatcherAssert.assertThat;

public class SubscriberInfoTest {

    private SubscriberInfo SubscriberInfo;

    private String propertyName = "testProperty";
    private String additionalProperty = "testAdditionalProperty";

    @BeforeMethod
    public void setUp() {
        SubscriberInfo = new SubscriberInfo();
    }

    @Test
    public void shouldHaveProperSettersAndGetters() {
        assertThat(SubscriberInfo.class, hasValidGettersAndSettersExcluding("additionalProperties"));
    }

    @Test
    public void shouldHaveProperGetterAndSetterForAdditionalProperties() {
        //	when
        SubscriberInfo.setAdditionalProperty(propertyName,additionalProperty);

        //	then
        AssertionsForClassTypes.assertThat( SubscriberInfo.getAdditionalProperties().get(propertyName) ).isEqualTo(additionalProperty);
    }

    @Test
    public void shouldProperlyCheckIfObjectsAreEqual() {
        //	given
        SubscriberInfo sameSubscriberInfo = new SubscriberInfo();
        SubscriberInfo differentSubscriberInfo= new SubscriberInfo();

        SubscriberInfo.setGlobalSubscriberId("testSubscriberId");
        sameSubscriberInfo.setGlobalSubscriberId("testSubscriberId");

        SubscriberInfo.setSubscriberName("testSubscriberName");
        sameSubscriberInfo.setSubscriberName("testSubscriberName");

        //	when
        boolean sameResponse = SubscriberInfo.equals(SubscriberInfo);
        boolean equalResponse = SubscriberInfo.equals(sameSubscriberInfo);
        boolean differentResponse = SubscriberInfo.equals(differentSubscriberInfo);
        boolean differentClassResponse = SubscriberInfo.equals("SubscriberInfo");

        //	then
        AssertionsForClassTypes.assertThat(sameResponse).isEqualTo(true);
        AssertionsForClassTypes.assertThat(equalResponse).isEqualTo(true);

        AssertionsForClassTypes.assertThat(differentResponse).isEqualTo(false);
        AssertionsForClassTypes.assertThat(differentClassResponse).isEqualTo(false);
    }

    @Test
    public void shouldProperlyConvertRelatedInstanceObjectToString() {
        //	given
        SubscriberInfo.setAdditionalProperty(propertyName,additionalProperty);
        SubscriberInfo.setGlobalSubscriberId("testSubscriberId");
        SubscriberInfo.setSubscriberName("testSubscriberName");

        //	when
        String response = SubscriberInfo.toString();

        //	then
        System.out.println(response);
        AssertionsForClassTypes.assertThat(response).contains(
                "globalSubscriberId=testSubscriberId," +
                        "subscriberCommonSiteId=<null>," +
                        "subscriberName=testSubscriberName," +
                        "additionalProperties={"+propertyName+"="+additionalProperty+"}]"
        );
    }
}
