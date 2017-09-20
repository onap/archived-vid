package org.openecomp.vid.aai;

import org.openecomp.vid.model.ProxyResponse;
import org.openecomp.vid.model.SubscriberList;

/**
 * Created by Oren on 7/5/17.
 */
public class SubscriberAaiResponse extends ProxyResponse {


    private SubscriberList subscriberList;

    public SubscriberAaiResponse(SubscriberList subscriberList, String errorMessage, int aaiHttpCode) {
        this.subscriberList = subscriberList;
        this.errorMessage = errorMessage;
        this.httpCode = aaiHttpCode;
    }


    public SubscriberList getSubscriberList() {
        return subscriberList;
    }
}
