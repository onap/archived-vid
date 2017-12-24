package org.openecomp.vid.aai;

import com.google.common.base.MoreObjects;
import org.openecomp.vid.model.ProxyResponse;

/**
 * Created by Oren on 7/10/17.
 */
public class AaiResponse<T> extends ProxyResponse{

    T t;

    public AaiResponse() {
    }

    public AaiResponse(T t, String errorMessage, int aaiHttpCode) {
        this.t = t;
        this.errorMessage = errorMessage;
        this.httpCode = aaiHttpCode;
    }

    public T getT() {
        return t;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("httpCode", httpCode)
                .add("errorMessage", errorMessage)
                .add("t", t)
                .toString();
    }
}
