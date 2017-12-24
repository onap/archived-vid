package org.openecomp.vid.model;

import java.util.List;

public class ListOfErrorsResponse {
    protected List<String> errors;

    public ListOfErrorsResponse() {
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
