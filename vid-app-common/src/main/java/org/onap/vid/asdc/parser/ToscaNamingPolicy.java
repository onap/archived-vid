/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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
