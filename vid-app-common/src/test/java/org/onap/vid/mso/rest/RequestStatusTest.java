package org.onap.vid.mso.rest;

import org.assertj.core.api.AssertionsForClassTypes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestStatusTest {

    private RequestStatus RequestStatus;

    private String propertyName = "testProperty";
    private String additionalProperty = "testAdditionalProperty";

    @BeforeMethod
    public void setUp() {
        RequestStatus = new RequestStatus();
    }

    @Test
    public void shouldHaveProperSettersAndGetters() {
        assertThat(RequestStatus.class, hasValidGettersAndSettersExcluding("additionalProperties"));
    }

    @Test
    public void shouldHaveProperGetterAndSetterForAdditionalProperties() {
        //	when
        RequestStatus.setAdditionalProperty(propertyName,additionalProperty);

        //	then
        AssertionsForClassTypes.assertThat( RequestStatus.getAdditionalProperties().get(propertyName) ).isEqualTo(additionalProperty);
    }

    @Test
    public void shouldProperlyCheckIfObjectsAreEqual() {
        //	given
        RequestStatus sameRequestStatus = new RequestStatus();
        RequestStatus differentRequestStatus= new RequestStatus();

        RequestStatus.setRequestState("testState");
        sameRequestStatus.setRequestState("testState");

        RequestStatus.setTimestamp("100");
        sameRequestStatus.setTimestamp("100");

        //	when
        boolean sameResponse = RequestStatus.equals(RequestStatus);
        boolean equalResponse = RequestStatus.equals(sameRequestStatus);
        boolean differentResponse = RequestStatus.equals(differentRequestStatus);
        boolean differentClassResponse = RequestStatus.equals("RequestStatus");

        //	then
        AssertionsForClassTypes.assertThat(sameResponse).isEqualTo(true);
        AssertionsForClassTypes.assertThat(equalResponse).isEqualTo(true);

        AssertionsForClassTypes.assertThat(differentResponse).isEqualTo(false);
        AssertionsForClassTypes.assertThat(differentClassResponse).isEqualTo(false);
    }

    @Test
    public void shouldProperlyConvertRelatedInstanceObjectToString() {
        //	given
        RequestStatus.setAdditionalProperty(propertyName,additionalProperty);
        RequestStatus.setRequestState("testState");
        RequestStatus.setTimestamp("100");

        //	when
        String response = RequestStatus.toString();

        //	then
        System.out.println(response);
        AssertionsForClassTypes.assertThat(response).contains(
               "percentProgress=<null>," +
                       "requestState=testState," +
                       "statusMessage=<null>," +
                       "timestamp=100," +
                       "wasRolledBack=<null>," +
                       "additionalProperties={"+propertyName+"="+additionalProperty+"}]"
        );
    }

}
