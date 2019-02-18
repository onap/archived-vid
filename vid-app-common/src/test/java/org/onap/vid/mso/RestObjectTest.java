package org.onap.vid.mso;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RestObjectTest {

    private RestObject restObject;

    @BeforeSuite
    private void setUp() {
        restObject = new RestObject();
    }

    @Test
    public void set_get_withProperParameters_shouldSetNewObjectAndGetSameObject() {
        //  given
        String testString = "set/get_testString";

        //  when
        restObject.set(testString);

        //  then
        assertThat(testString).isSameAs(restObject.get());

        System.out.println(restObject.toString());
    }

    @Test
    public void set_get_StatusCode_withProperParameters_shouldSetNewStatusCodeAndGetSameCode() {
        //  given
        int statusCode = 202;

        //  when
        restObject.setStatusCode(statusCode);

        //  then
        assertThat(statusCode).isEqualTo(restObject.getStatusCode());
    }

    @Test
    public void set_get_Raw_withProperParameters_shouldSetNewRawStringAndGetString() {
        //  given
        String rawTestString = "set/get_rawTestString";

        //  when
        restObject.setRaw(rawTestString);

        //  then
        assertThat(rawTestString).isSameAs(restObject.getRaw());
    }

    @Test
    public void copyFrom_withProperParameters_shouldCopyFieldsFromOneRestObjectToAnother() {
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
    public void toString_shouldReturnProperString() {
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