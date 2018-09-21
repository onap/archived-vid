package org.onap.vid.services;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.ServiceSubscription;
import org.onap.vid.aai.ServiceSubscriptions;
import org.onap.vid.aai.Services;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RoleGenaratorServiceImplTest {

    private static final String GLOBAL_CUSTOMER_ID = "997";
    private static final String SUBSCRIBER_NAME = "name";
    private static final String SUBSCRIBER_TYPE = "subscriber_type";
    private static final String RESOURCE_VERSION = "1";
    private static final String SERVICE_TYPE = "service_type";

    @Mock
    private AaiClientInterface aaiClientInterface;

    @InjectMocks
    private RoleGenaratorServiceImpl testSubject;

    @BeforeClass
    public void beforeClass() {
        initMocks(this);
    }

    @BeforeMethod
    public void resetMocks() {
        Mockito.reset(aaiClientInterface);
    }

    @Test
    public void tenerateRoleScript_firstRun() {
        boolean firstRun = true;

        Subscriber subscriber = createSubscriber();
        AaiResponse<SubscriberList> subscribers = createSubscriberListAaiResponse(subscriber);
        doReturn(subscribers).when(aaiClientInterface).getAllSubscribers();

        ServiceSubscription serviceSubscription = createServiceSubscription();
        AaiResponse<Services> subscriberResponse = createServicesAaiResponse(serviceSubscription);
        doReturn(subscriberResponse).when(aaiClientInterface).getSubscriberData(subscriber.globalCustomerId);

        String result = testSubject.generateRoleScript(firstRun);
        Assert.assertTrue(StringUtils.isNotBlank(result));
    }

    @Test
    public void tenerateRoleScript_notAFirstRun() {
        boolean firstRun = false;

        Subscriber subscriber = createSubscriber();
        AaiResponse<SubscriberList> subscribers = createSubscriberListAaiResponse(subscriber);
        doReturn(subscribers).when(aaiClientInterface).getAllSubscribers();

        ServiceSubscription serviceSubscription = createServiceSubscription();
        AaiResponse<Services> subscriberResponse = createServicesAaiResponse(serviceSubscription);
        doReturn(subscriberResponse).when(aaiClientInterface).getSubscriberData(subscriber.globalCustomerId);

        String result = testSubject.generateRoleScript(firstRun);
        Assert.assertTrue(StringUtils.isNotBlank(result));
    }

    @Test(expectedExceptions = { Exception.class })
    public void tenerateRoleScript_errorGettingDataFromAAIClient() {
        boolean firstRun = false;

        doThrow(new Exception("This is expected.")).when(aaiClientInterface).getAllSubscribers();

        String result = testSubject.generateRoleScript(firstRun);
        Assert.fail();
    }

    private ServiceSubscription createServiceSubscription() {
        ServiceSubscription serviceSubscription = new ServiceSubscription();
        serviceSubscription.serviceType = SERVICE_TYPE;
        return serviceSubscription;
    }

    private AaiResponse<SubscriberList> createSubscriberListAaiResponse(Subscriber subscriber) {
        AaiResponse<SubscriberList> subscribers = new AaiResponse<>(new SubscriberList(new ArrayList<>()), "", 200);
        subscribers.getT().customer.add(subscriber);
        return subscribers;
    }

    private AaiResponse<Services> createServicesAaiResponse(ServiceSubscription serviceSubscription) {
        AaiResponse<Services> subscriberResponse = new AaiResponse<>(new Services(), "", 200);
        subscriberResponse.getT().serviceSubscriptions = new ServiceSubscriptions();
        subscriberResponse.getT().serviceSubscriptions.serviceSubscription = new ArrayList<>();
        subscriberResponse.getT().serviceSubscriptions.serviceSubscription.add(serviceSubscription);
        return subscriberResponse;
    }

    private Subscriber createSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.globalCustomerId = GLOBAL_CUSTOMER_ID;
        subscriber.subscriberName = SUBSCRIBER_NAME;
        subscriber.subscriberType = SUBSCRIBER_TYPE;
        subscriber.resourceVersion = RESOURCE_VERSION;
        return subscriber;
    }


}