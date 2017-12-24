package org.opencomp.vid.api;

import org.opencomp.vid.testUtils.TestUtils;
import org.openecomp.vid.category.AddCategoryOptionsRequest;
import org.openecomp.vid.category.CategoryParameterOptionRep;
import org.openecomp.vid.category.CategoryParametersResponse;
import org.openecomp.vid.model.CategoryParameterOption;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;


//This is integration test that require running tomcat
public class CategoryParametersApiTest extends BaseApiTest {

    public static final String GET_CATEGORY_PARAMETER_PROPERTIES = "maintenance/category_parameter";
    public static final String DELETE_CATEGORY_PARAMETER_PROPERTIES = "maintenance/delete_category_parameter";
    public static final String PROPERTY_NAME = "owningEntity";
    public static final String PROPERTY_FAMILY = "PARAMETER_STANDARDIZATION";

    public static final String NEW_PARAMETER_PROPERTY_NAME = "oren";
    public static final String UPDATE_PARAMETER_PROPERTY_NAME = "oren2";


//    @Test
    public void addCPProperties() throws IOException {
        AddCategoryOptionsRequest request = new AddCategoryOptionsRequest();
        request.options.add(NEW_PARAMETER_PROPERTY_NAME);
        addCPPropertiesRequest(HttpMethod.POST, request, HttpStatus.OK);
        findPropertyNameInGetResponse(NEW_PARAMETER_PROPERTY_NAME);

    }

//    @Test
    public void updateCPProperties() throws IOException {
        List<CategoryParameterOptionRep> props = getProps();
        CategoryParameterOptionRep updateReq = new CategoryParameterOptionRep();
        updateReq.setName(UPDATE_PARAMETER_PROPERTY_NAME);
        updateReq.setId(props.get(props.size()-1).getId());
        updateCPPropertiesRequest(HttpMethod.PUT, updateReq, HttpStatus.OK);
        findPropertyNameInGetResponse(UPDATE_PARAMETER_PROPERTY_NAME);
        CategoryParameterOption deleteReq = new CategoryParameterOption();
        deleteReq.setName(UPDATE_PARAMETER_PROPERTY_NAME);
        deleteCPPropertiesRequest(HttpMethod.POST, deleteReq, HttpStatus.OK);
    }

    private List<CategoryParameterOptionRep> getProps() throws IOException {
        Response response = getCPPropertiesRequest(HttpMethod.GET, HttpStatus.OK);
        String expectedJsonAsString = response.readEntity(String.class);
        CategoryParametersResponse categoryParameterResponse =  objectMapper.readValue(expectedJsonAsString, CategoryParametersResponse.class);
        List<CategoryParameterOptionRep> props = categoryParameterResponse.getCategoryParameters().get(PROPERTY_NAME);
        return props;
    }

    private void findPropertyNameInGetResponse(String propertyName) throws IOException{
        List<CategoryParameterOptionRep> props = getProps();
        boolean found = false;
        for (CategoryParameterOptionRep prop :
                props) {
            if(prop.getName().equals(propertyName))
                found = true;
        }
        Assert.assertTrue(found);
    }

    private Response getCPPropertiesRequest(String method, HttpStatus exceptedHttpStatus) throws IOException {
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUri(uri).path("/"+GET_CATEGORY_PARAMETER_PROPERTIES)
                .queryParam("familyName", PROPERTY_FAMILY);
        WebTarget webTarget = client.target(urlBuilder.toUriString());
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(null));
        TestUtils.assertHttpStatus(null, webTarget, response, exceptedHttpStatus);
        return response;
    }

    private Response addCPPropertiesRequest(String method, AddCategoryOptionsRequest request, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(GET_CATEGORY_PARAMETER_PROPERTIES+"/"+PROPERTY_NAME);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(request));
        TestUtils.assertHttpStatus(request, webTarget, response, exceptedHttpStatus);
        return response;
    }

    private Response updateCPPropertiesRequest(String method, CategoryParameterOptionRep request, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(GET_CATEGORY_PARAMETER_PROPERTIES+"/"+PROPERTY_NAME);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(request));
        TestUtils.assertHttpStatus(request, webTarget, response, exceptedHttpStatus);
        return response;
    }

    private Response deleteCPPropertiesRequest(String method, CategoryParameterOption request, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(DELETE_CATEGORY_PARAMETER_PROPERTIES+"/"+PROPERTY_NAME);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(request));
        TestUtils.assertHttpStatus(request, webTarget, response, exceptedHttpStatus);
        return response;
    }
}
