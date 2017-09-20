package org.openecomp.vid.aai;

import org.openecomp.vid.model.ProxyResponse;
import org.openecomp.vid.model.SubscriberList;
import org.openecomp.vid.roles.RoleValidator;

/**
 * Created by Oren on 7/5/17.
 */

public class SubscriberFilteredResults extends ProxyResponse {

    private SubscriberListWithFilterData subscriberList;

    public SubscriberFilteredResults(RoleValidator roleValidator,SubscriberList subscribers, String errorMessage, int aaiHttpCode) {
        this.subscriberList = new SubscriberListWithFilterData(subscribers,roleValidator);
        this.errorMessage = errorMessage;
        this.httpCode = aaiHttpCode;
    }


    public SubscriberListWithFilterData getSubscriberList() {
        return subscriberList;
    }

    public void setSubscriberList(SubscriberListWithFilterData subscriberList) {
        this.subscriberList = subscriberList;
    }
}