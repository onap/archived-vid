package org.onap.vid.aai;

import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.RoleValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oren on 7/5/17.
 */
public class SubscriberListWithFilterData {

    public SubscriberListWithFilterData(SubscriberList subscriberList, RoleValidator roleValidator){
        List<Subscriber> subscribers = subscriberList.customer;
        List<SubscriberWithFilter> subscribersWithFilter = new ArrayList<>();
        for (Subscriber subscriber :subscribers){
            SubscriberWithFilter subscriberWithFilter = new SubscriberWithFilter();
            subscriberWithFilter.setIsPermitted(roleValidator.isSubscriberPermitted(subscriber.globalCustomerId));
            subscriberWithFilter.subscriberType = subscriber.subscriberType;
            subscriberWithFilter.resourceVersion = subscriber.resourceVersion;
            subscriberWithFilter.subscriberName = subscriber.subscriberName;
            subscriberWithFilter.globalCustomerId = subscriber.globalCustomerId;
            subscribersWithFilter.add(subscriberWithFilter);
        }
        this.customer = subscribersWithFilter;
     }

    public List<SubscriberWithFilter> customer;
}
