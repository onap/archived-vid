package org.onap.vid.mso;

import java.io.IOException;

public class MsoException extends RuntimeException {
    public MsoException(Exception cause) {
        super(cause);
    }
}
