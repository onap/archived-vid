package org.onap.simulator.wiremock;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String rootDirectory) {
        super(format("Does not found directory `%s` on classpath", rootDirectory));
    }
}
