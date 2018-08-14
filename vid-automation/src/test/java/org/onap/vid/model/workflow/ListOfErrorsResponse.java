package org.onap.vid.model.workflow;

import java.util.ArrayList;
import java.util.List;

public class ListOfErrorsResponse {
    protected List<String> errors;

    public ListOfErrorsResponse() {
        this.errors = new ArrayList<>();
    }

    public ListOfErrorsResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
