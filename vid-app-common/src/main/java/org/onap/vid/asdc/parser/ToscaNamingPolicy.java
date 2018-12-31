package org.onap.vid.asdc.parser;

import org.apache.commons.lang3.StringUtils;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.sdc.toscaparser.api.Property;

import java.util.Map;

public class ToscaNamingPolicy {

    public static String isUserProvidingServiceNameOptional(ISdcCsarHelper csarHelper){
        return csarHelper.getServiceMetadata().getValue(ToscaParserImpl2.Constants.ECOMP_GENERATED_NAMING);
    }

    private static Object isPropertyContainsEcompGeneratedNaming(Property property) {
        return ((Map) (property.getValue())).get(ToscaParserImpl2.Constants.ECOMP_GENERATED_NAMING_PROPERTY);
    }

    public static String getEcompNamingValueForNode(NodeTemplate node, String parentProperty) {
        return node.getPropertiesObjects().stream()
                .filter(property -> StringUtils.equals(property.getName(), parentProperty))
                .findFirst()
                .map(ToscaNamingPolicy::isPropertyContainsEcompGeneratedNaming)
                .map(Object::toString)
                .orElse("false");
    }
}
