package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ResourceType {

    SERVICE_INSTANCE("service-instances", "service-instance-name"),
    GENERIC_VNF("generic-vnfs", "vnf-name"),
    L3_NETWORK("l3-networks", "network-name"),
    VF_MODULE("vf-modules", "vf-module-name"),
    INSTANCE_GROUP("instance-groups", "instance-group-name"),
    VOLUME_GROUP("volume-groups", "volume-group-name");

    private static Map<String, ResourceType> AAI_FORMAT_MAP = Stream
            .of(ResourceType.values())
            .collect(Collectors.toMap(s -> s.aaiFormat, Function.identity()));

    private final String aaiFormat;
    private final String nameFilter;

    ResourceType(String formatted, String nameFilter) {
        this.aaiFormat = formatted;
        this.nameFilter = nameFilter;
    }

    public String getAaiFormat() {
        return aaiFormat;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    @JsonCreator
    public static ResourceType fromString(String string) {
        return Optional
                .ofNullable(AAI_FORMAT_MAP.get(string))
                .orElseThrow(() -> new IllegalArgumentException(string));
    }
}
