package org.onap.vid.aai.model;


import com.google.common.collect.ImmutableList;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.properties.Features;
import org.togglz.core.manager.FeatureManager;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PortDetailsTranslator {

    @Inject
    FeatureManager featureManager;

    public static class PortDetailsOk extends PortDetails {

        private final String interfaceId;
        private final String interfaceName;
        private final boolean isPortMirrored;

        public PortDetailsOk(String interfaceId, String interfaceName, boolean isPortMirrored) {
            this.interfaceId = interfaceId;
            this.interfaceName = interfaceName;
            this.isPortMirrored = isPortMirrored;
        }

        public String getInterfaceId() {
            return interfaceId;
        }

        public String getInterfaceName() {
            return interfaceName;
        }

        public boolean getIsPortMirrored() {
            return isPortMirrored;
        }
    }

    public abstract static class PortDetails {
    }

    public static class PortDetailsError extends PortDetails {
        private final String errorDescription;
        private final String rawAaiResponse;

        public PortDetailsError(String errorDescription, String rawAaiResponse){
           this.errorDescription = errorDescription;
           this.rawAaiResponse = rawAaiResponse;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public String getRawAaiResponse() {
            return rawAaiResponse;
        }
    }

    public static PortDetails extractPortDetailsFromProperties(Properties properties, String rawPayload){
        List<String> errorDescriptions = new LinkedList<>();
        describeIfNullOrEmpty("interface-id", properties.getInterfaceId(), errorDescriptions);
        describeIfNullOrEmpty("interface-name", properties.getInterfaceName(), errorDescriptions);
        describeIfNullOrEmpty("is-port-mirrored", properties.getIsPortMirrored(), errorDescriptions);

        if(errorDescriptions.isEmpty()){
            return new PortDetailsOk(properties.getInterfaceId(), properties.getInterfaceName(), properties.getIsPortMirrored());
        } else {
            return new PortDetailsError(String.join(" ", errorDescriptions), rawPayload);
        }
    }

    private static void describeIfNullOrEmpty(String name, Object value, List<String> errorDescriptions) {
        if (value == null) {
            errorDescriptions.add("Value of '" + name + "' is missing.");
        } else if (value.toString().isEmpty()) {
            errorDescriptions.add("Value of '" + name + "' is empty.");
        }
    }

    private static Optional<List<PortDetails>> extractErrorResponseIfHttpError(AaiResponse aaiResponse, String rawPayload) {
        if (aaiResponse.getHttpCode() != org.springframework.http.HttpStatus.OK.value()) {
            final String errorMessage = aaiResponse.getErrorMessage();
            return Optional.of(ImmutableList.of(new PortDetailsError(
                    "Got " + aaiResponse.getHttpCode() + " from aai",
                    errorMessage != null ? errorMessage.toString() : rawPayload)
            ));
        } else {
            return Optional.empty();
        }
    }

    public List<PortDetails> extractPortDetailsInternal(AaiGetPortMirroringSourcePorts aaiGetPortsResponse, String rawPayload){
        List<SimpleResult> filteredResult = getFilteredPortList(aaiGetPortsResponse.getResults());

        return filteredResult.stream()
                .map(SimpleResult::getProperties)
                .map(p -> extractPortDetailsFromProperties(p, rawPayload))
                .collect(Collectors.toList());
    }

    public List<SimpleResult> getFilteredPortList(List<SimpleResult> results) {
        String LINTERFACE = "l-interface";

        final Predicate<SimpleResult> ifIsPort = (SimpleResult r) -> LINTERFACE.equals(r.getNodeType());
        Predicate<SimpleResult> ifIsSource = getIsSourcePredicate();

        return results.stream()
                .filter(ifIsPort)
                .filter(ifIsSource)
                .collect(Collectors.toList());
    }

    private Predicate<SimpleResult> getIsSourcePredicate() {
        boolean FLAG_ADVANCED_PORTS_FILTER = featureManager.isActive(Features.FLAG_ADVANCED_PORTS_FILTER);

        if (FLAG_ADVANCED_PORTS_FILTER) {
            String PORT_LABEL = "org.onap.relationships.inventory.Source";
            return (SimpleResult r) -> r.getRelatedTo().stream()
                    .anyMatch(relatedTo -> PORT_LABEL.equalsIgnoreCase(relatedTo.getRelationshipLabel()));
        } else {
            return (SimpleResult r) -> true;
        }
    }

    public List<PortDetails> extractPortDetails(AaiResponse<AaiGetPortMirroringSourcePorts> aaiGetPortsResponse, String rawPayload){
        return extractErrorResponseIfHttpError(aaiGetPortsResponse, rawPayload).orElseGet(() -> extractPortDetailsInternal(aaiGetPortsResponse.getT(), rawPayload));

    }

}
