package org.onap.vid.mso;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.assertj.core.api.Assertions.assertThat;

public class RestObjectTest {

    private RestObject restObject;

    @BeforeSuite
    private void setUp() {
        restObject = new RestObject();
    }

    @Test
    public void shouldHaveValidGettersAndSetters(){
        MatcherAssert.assertThat(RestObject.class, hasValidGettersAndSettersExcluding("t"));
    }

    @Test
    public void shouldHaveValidGetterAndSetterForBody() {
        //  given
        String testString = "set/get_testString";

        //  when
        restObject.set(testString);

        //  then
        assertThat(testString).isSameAs(restObject.get());
    }

    @Test
    public void shouldProperlyCopyRestObject() {
        //  given
        MsoResponseWrapper testResponseWraper = new MsoResponseWrapper();
        String rawTestString = "rawTestString";
        int statusCode = 404;

        RestObject restObjectToCopyFrom = new RestObject<>();
        restObjectToCopyFrom.set(testResponseWraper);
        restObjectToCopyFrom.setRaw(rawTestString);
        restObjectToCopyFrom.setStatusCode(statusCode);

        //  when
        restObject.copyFrom(restObjectToCopyFrom);

        //  then
        assertThat(restObject).isEqualToComparingFieldByField(restObjectToCopyFrom);
    }

    @Test
    public void shouldProperlyConvertRestObjectToString() {
        //  given
        String testString = "testString";
        String rawTestString = "rawTestString";
        int statusCode = 202;

        restObject.set(testString);
        restObject.setRaw(rawTestString);
        restObject.setStatusCode(statusCode);

        String properString = "RestObject{t=testString, rawT=rawTestString, statusCode=202}";

        //  when
        String toStringResponse = restObject.toString();

        //  then
        assertThat(toStringResponse).isEqualTo(properString);
    }
}