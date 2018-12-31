package org.onap.vid.model;

import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public class SubscriberList {
    public SubscriberList(List<Subscriber> customer) {
        this.customer = customer;
    }

    public SubscriberList(){}

    public List<Subscriber> customer;


}
