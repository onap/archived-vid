package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.model.Subscriber;

/**
 * Created by Oren on 7/5/17.
 */
public class SubscriberWithFilter extends Subscriber{

    @JsonProperty("is-permitted")
    private boolean isPermitted;

    public boolean getIsPermitted() {
        return isPermitted;
    }

    public void setIsPermitted(boolean isPermitted) {
        this.isPermitted = isPermitted;
    }
}
