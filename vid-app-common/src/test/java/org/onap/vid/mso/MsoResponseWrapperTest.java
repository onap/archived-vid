package org.onap.vid.mso;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;


public class MsoResponseWrapperTest {

    private static final String properResponse = "{ \"status\": 1, \"entity\": testEntity}";
    private static final String properResponseWithNoEntity = "{ \"status\": 1, \"entity\": \"\"}";
    private static final String properToString = "[status=1,entity=testEntity]";

    @Mock
    private Response response;

    private MsoResponseWrapper responseWrapper;

    @BeforeSuite
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldProperlySetGetStatus() {
        responseWrapper = new MsoResponseWrapper();

        responseWrapper.setStatus(1);
        assertThat(responseWrapper.getStatus()).isEqualTo(1);
    }

    @Test
    public void shouldProperlySetGetEntity() {
        responseWrapper = new MsoResponseWrapper();

        responseWrapper.setEntity("testEntity");
        assertThat(responseWrapper.getEntity()).isEqualTo("testEntity");
    }

    @Test
    public void shouldProperlyConstructWithParameters(){
        responseWrapper = new MsoResponseWrapper(1,"testEntity");

        assertThat(responseWrapper.getStatus()).isEqualTo(1);
        assertThat(responseWrapper.getEntity()).isEqualTo("testEntity");
    }

    @Test
    public void shouldProperlyConstructFromResponse(){
        when(response.getStatus()).thenReturn(1);
        when(response.readEntity(String.class)).thenReturn("testEntity");

        responseWrapper = new MsoResponseWrapper(response);

        assertThat(responseWrapper.getStatus()).isEqualTo(1);
        assertThat(responseWrapper.getEntity()).isEqualTo("testEntity");
    }

    @Test
    public void shouldProperlyGetResponseWithEmptyEntity(){
        responseWrapper = new MsoResponseWrapper();
        responseWrapper.setStatus(1);

        assertThat(responseWrapper.getResponse()).isEqualToIgnoringWhitespace(properResponseWithNoEntity);
    }

    @Test
    public void shouldProperlyGetResponse(){
        responseWrapper = new MsoResponseWrapper(1,"testEntity");

        assertThat(responseWrapper.getResponse()).isEqualToIgnoringWhitespace(properResponse);
    }

    @Test
    public void shouldProperlyConvertToString(){
        responseWrapper = new MsoResponseWrapper(1,"testEntity");

        assertThat(responseWrapper.toString()).endsWith(properToString);
    }


}