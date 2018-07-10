package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ResourceType {

    SERVICE_INSTANCE("service-instance", "service-instance-name"),
    GENERIC_VNF("generic-vnf", "vnf-name"),
    VF_MODULE("vf-module", "vf-module-name"),
    VOLUME_GROUP("volume-group", "volume-group-name");

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
