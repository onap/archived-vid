package org.opencomp.vid.api;

import org.opencomp.vid.model.category.AddCategoryOptionsRequest;
import org.opencomp.vid.model.category.CategoryParameterOption;
import org.opencomp.vid.model.category.CategoryParameterOptionRep;
import org.opencomp.vid.model.category.CategoryParametersResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;
import vid.automation.test.services.CategoryParamsService;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.assertEquals;


//This is integration test that require running tomcat
public class CategoryParametersApiTest extends BaseApiTest {

    public static final String GET_CATEGORY_PARAMETER_PROPERTIES = "maintenance/category_parameter";
    public static final String DELETE_CATEGORY_PARAMETER_PROPERTIES = "maintenance/delete_category_parameter";
    public static final String PROPERTY_NAME = "owningEntity";
    public static final String PROPERTY_FAMILY = "PARAMETER_STANDARDIZATION";

    public static final String NEW_PARAMETER_PROPERTY_NAME = "oren";
    public static final String UPDATE_PARAMETER_PROPERTY_NAME = "oren2";


    @Test(groups = { "worksOnlyWithLocalhostVID" })
    public void addCPProperties() throws IOException {
        AddCategoryOptionsRequest request = new AddCategoryOptionsRequest();
        String newParameter = UUID.randomUUID().toString();
        request.options.add(newParameter);
        addCPPropertiesRequest(HttpMethod.POST, request, HttpStatus.OK);
        findPropertyNameInGetResponse(newParameter);

    }

    @Test(groups = { "worksOnlyWithLocalhostVID" })
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

    @Test(groups = { "worksOnlyWithLocalhostVID" })
    //this test call to MaintenanceController which is restricted to localhost, so it can not run on jenkins pipeline
    public void getOrderedCPProperties() throws IOException {
        // Ensure there is some initial data when checking that the list is sorted
        CategoryParamsService categoryParamsService = new CategoryParamsService();
        List<CategoryParameterOptionRep> props = getProps();
        final List<String> propsNames = props.stream().map(CategoryParameterOptionRep::getName).collect(Collectors.toList());
        assertEquals("The list isn't sorted", propsNames, propsNames.stream().sorted(String::compareToIgnoreCase).collect(Collectors.toList()));
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
