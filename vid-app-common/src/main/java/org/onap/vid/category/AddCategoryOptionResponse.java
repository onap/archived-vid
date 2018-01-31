package org.onap.vid.category;

import org.onap.vid.model.ListOfErrorsResponse;

import java.util.List;

public class AddCategoryOptionResponse extends ListOfErrorsResponse {

    public AddCategoryOptionResponse() {
    }

    public AddCategoryOptionResponse(List<String> errors) {
        super(errors);
    }
}
