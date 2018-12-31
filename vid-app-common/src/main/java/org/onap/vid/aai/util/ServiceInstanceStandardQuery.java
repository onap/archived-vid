package org.onap.vid.aai.util;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StrSubstitutor;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.*;
import org.onap.vid.aai.model.interfaces.AaiModelWithRelationships;
import org.onap.vid.utils.Multival;
import org.onap.vid.utils.Unchecked;

import javax.inject.Inject;
import java.net.URI;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ServiceInstanceStandardQuery {

    private static final String SERVICE_INSTANCE_URI_TEMPLATE = "" +
            "business/customers/customer/${global-customer-id}" +
            "/service-subscriptions/service-subscription/${service-type}" +
            "/service-instances/service-instance/${service-instance-id}";
    
    private final AaiClientInterface aaiClient;

    @Inject
    public ServiceInstanceStandardQuery(AaiClientInterface aaiClient) {
        this.aaiClient = aaiClient;
    }

    public ServiceInstance fetchServiceInstance(String globalCustomerId, String serviceType, String serviceInstanceId) {
        final String serviceInstanceUri = getServiceInstanceUri(globalCustomerId, serviceType, serviceInstanceId);

        return fetchServiceInstance(Unchecked.toURI(serviceInstanceUri));
    }

    ServiceInstance fetchServiceInstance(URI serviceInstanceUri) {
        return objectByUri(ServiceInstance.class, serviceInstanceUri);
    }

    protected <T> T objectByUri(Class<T> clazz, URI aaiResourceUri) {
        return aaiClient.typedAaiGet(aaiResourceUri, clazz);
    }

    public Multival<ServiceInstance, Vnf> fetchRelatedVnfs(ServiceInstance serviceInstance) {
        return fetchRelated("service", serviceInstance, "generic-vnf", Vnf.class);
    }

    public <K extends AaiModelWithRelationships> Multival<K, Network> fetchRelatedL3Networks(String sourceType, K source) {
        return fetchRelated(sourceType, source, "l3-network", Network.class);
    }

    public Multival<Network, Vlan> fetchRelatedVlanTags(Network network) {
        return fetchRelated("network", network, "vlan-tag", Vlan.class);
    }

    private String getServiceInstanceUri(String globalCustomerId, String serviceType, String serviceInstanceId) {
        return new StrSubstitutor(ImmutableMap.of(
                "global-customer-id", globalCustomerId,
                "service-type", serviceType,
                "service-instance-id", serviceInstanceId
        )).replace(SERVICE_INSTANCE_URI_TEMPLATE);
    }

    private <K extends AaiModelWithRelationships, V> Multival<K, V> fetchRelated(String sourceType, K source, String destType, Class<V> destClass) {
        return Multival.of(
                sourceType,
                source,
                destType,
                fetchRelatedInner(source, destType, destClass)
        );
    }

    private <K extends AaiModelWithRelationships, V> Set<V> fetchRelatedInner(K source, String destType, Class<V> destClass) {
        return getURIsOf(source, relationship -> relatedTo(relationship, destType))
                .map(destUri -> objectByUri(destClass, destUri))
                .collect(toSet());
    }

    protected Stream<URI> getURIsOf(AaiModelWithRelationships aaiModel, Predicate<Relationship> predicate) {
        return aaiModel.getRelationshipList().getRelationship().stream()
                .filter(predicate)
                .map(r -> r.relatedLink)
                .map(Unchecked::toURI);
    }

    protected static boolean relatedTo(Relationship r, String relationshipName) {
        return relationshipName.equals(r.getRelatedTo());
    }

}
