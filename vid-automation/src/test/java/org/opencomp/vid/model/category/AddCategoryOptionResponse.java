package org.opencomp.vid.model.category;

import org.opencomp.vid.model.workflow.ListOfErrorsResponse;

import java.util.List;

public class AddCategoryOptionResponse extends ListOfErrorsResponse {

    public AddCategoryOptionResponse() {
    }

    public AddCategoryOptionResponse(List<String> errors) {
        super(errors);
    }
}
