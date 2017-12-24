package org.openecomp.vid.category;

import org.openecomp.vid.model.ListOfErrorsResponse;

import java.util.List;

public class AddCategoryOptionResponse extends ListOfErrorsResponse {

    public AddCategoryOptionResponse() {
    }

    public AddCategoryOptionResponse(List<String> errors) {
        super(errors);
    }
}
