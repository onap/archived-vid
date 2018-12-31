package org.onap.vid.utils;

import org.onap.vid.exceptions.GenericUncheckedException;

import java.net.URI;
import java.net.URISyntaxException;

public class Unchecked {
    private Unchecked() {
        // explicit private constructor, to hide the implicit public constructor
    }

    public static URI toURI(String uri) {
        try {
            // Indulge spaces in the URI by the replcement
            return new URI(uri.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            throw new GenericUncheckedException(e);
        }
    }


}
