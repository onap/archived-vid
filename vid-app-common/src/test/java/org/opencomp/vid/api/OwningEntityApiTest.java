package org.opencomp.vid.api;

import org.opencomp.vid.model.oeModel;
import org.opencomp.vid.testUtils.TestUtils;
import org.openecomp.vid.category.AddCategoryOptionsRequest;
import org.openecomp.vid.category.CategoryParameterOptionRep;
import org.openecomp.vid.model.CategoryParameterOption;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//This is integration test that require running tomcat
public class OwningEntityApiTest extends BaseApiTest {

    public static final String GET_OWNING_ENTITY_PROPERTIES = "maintenance/category_parameter";
    public static final String DELETE_OWNING_ENTITY_PROPERTIES = "maintenance/delete_category_parameter";
    public static final String PROPERTY_NAME = "owningEntity";

    public static final String NEW_OWNING_ENTITY_NAME = "oren";
    public static final String UPDATE_OWNING_ENTITY_NAME = "oren2";

    @Test
    public void addOEProperties() throws IOException {

        AddCategoryOptionsRequest request = new AddCategoryOptionsRequest();
        request.options.add(NEW_OWNING_ENTITY_NAME);
        addOEPropertiesRequest(HttpMethod.POST, request, HttpStatus.OK);
        Response response = getOEPropertiesRequest(HttpMethod.GET, HttpStatus.OK);
        String expectedJsonAsString = response.readEntity(String.class);
        oeModel oep =  objectMapper.readValue(expectedJsonAsString, oeModel.class);
        List<Map<String, String>> props = oep.categoryParameters.get(PROPERTY_NAME);
        boolean found = false;
        for (Map prop :
                props) {
            if(prop.containsValue(NEW_OWNING_ENTITY_NAME))
                found = true;
        }
        Assert.assertTrue(found);
    }

    @Test
    public void updateOEProperties() throws IOException {

        Response response = getOEPropertiesRequest(HttpMethod.GET, HttpStatus.OK);
        String expectedJsonAsString = response.readEntity(String.class);
        oeModel oep =  objectMapper.readValue(expectedJsonAsString, oeModel.class);
        List<Map<String, String>> props = oep.categoryParameters.get(PROPERTY_NAME);

        CategoryParameterOptionRep updateReq = new CategoryParameterOptionRep();
        updateReq.setName(UPDATE_OWNING_ENTITY_NAME);
        updateReq.setId(props.get(props.size()-1).get("id"));
        updateOEPropertiesRequest(HttpMethod.PUT, updateReq, HttpStatus.OK);

        response = getOEPropertiesRequest(HttpMethod.GET, HttpStatus.OK);
        expectedJsonAsString = response.readEntity(String.class);
        oep =  objectMapper.readValue(expectedJsonAsString, oeModel.class);
        props = oep.categoryParameters.get(PROPERTY_NAME);
        boolean found = false;
        for (Map prop :
                props) {
            if(prop.containsValue(UPDATE_OWNING_ENTITY_NAME))
                found = true;
        }
        Assert.assertTrue(found);
        CategoryParameterOption deleteReq = new CategoryParameterOption();
        deleteReq.setName(UPDATE_OWNING_ENTITY_NAME);
        deleteOEPropertiesRequest(HttpMethod.POST, deleteReq, HttpStatus.OK);
    }

    private Response getOEPropertiesRequest(String method, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(GET_OWNING_ENTITY_PROPERTIES);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(null));
        TestUtils.assertHttpStatus(null, webTarget, response, exceptedHttpStatus);
        return response;
    }

    private Response addOEPropertiesRequest(String method, AddCategoryOptionsRequest request, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(GET_OWNING_ENTITY_PROPERTIES+"/"+PROPERTY_NAME);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(request));
        TestUtils.assertHttpStatus(request, webTarget, response, exceptedHttpStatus);
        return response;
    }

    private Response updateOEPropertiesRequest(String method, CategoryParameterOptionRep request, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(GET_OWNING_ENTITY_PROPERTIES+"/"+PROPERTY_NAME);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(request));
        TestUtils.assertHttpStatus(request, webTarget, response, exceptedHttpStatus);
        return response;
    }

    private Response deleteOEPropertiesRequest(String method, CategoryParameterOption request, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(DELETE_OWNING_ENTITY_PROPERTIES+"/"+PROPERTY_NAME);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(request));
        TestUtils.assertHttpStatus(request, webTarget, response, exceptedHttpStatus);
        return response;
    }
}
