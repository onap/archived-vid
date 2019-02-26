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

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VidNotions;
import org.onap.vid.properties.Features;
import org.togglz.core.manager.FeatureManager;

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

public class VidNotionsBuilder {

    private final FeatureManager featureManager;

    public VidNotionsBuilder(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    public VidNotions buildVidNotions(ISdcCsarHelper csarHelper, ServiceModel serviceModel) {
        final VidNotions.InstantiationUI instantiationUI = suggestInstantiationUI(csarHelper);

        return new VidNotions(instantiationUI, suggestModelCategory(csarHelper), suggestViewEditUI(csarHelper, serviceModel));
    }

    //UI route a-la-carte services to old UI only if InstantiationUI is LEGACY
    //So any other value for InstantiationUI other than LEGACY make UI to route
    //a-la-carte services to new UI
    VidNotions.InstantiationUI suggestInstantiationUI(ISdcCsarHelper csarHelper) {
        if(featureManager.isActive(Features.FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI) && isALaCarte(csarHelper)) {
            return VidNotions.InstantiationUI.ANY_ALACARTE_NEW_UI;
        }
        if (featureManager.isActive(Features.FLAG_1902_VNF_GROUPING) && isGrouping(csarHelper)) {
            return VidNotions.InstantiationUI.SERVICE_WITH_VNF_GROUPING;
        }
        if (featureManager.isActive(Features.FLAG_5G_IN_NEW_INSTANTIATION_UI)) {
            if (isUuidExactlyHardCoded1ffce89fef3f(csarHelper)) {
                return VidNotions.InstantiationUI.SERVICE_UUID_IS_1ffce89f_ef3f_4cbb_8b37_82134590c5de;
            } else if (isALaCarte(csarHelper) && hasAnyNetworkWithPropertyNetworkTechnologyEqualsStandardSriovOrOvs(csarHelper)) {
                return VidNotions.InstantiationUI.NETWORK_WITH_PROPERTY_NETWORK_TECHNOLOGY_EQUALS_STANDARD_SRIOV_OR_OVS;
            } else if (isALaCarte(csarHelper) && hasFabricConfiguration(csarHelper)) {
                return VidNotions.InstantiationUI.SERVICE_WITH_FABRIC_CONFIGURATION;
            }
        }

        return VidNotions.InstantiationUI.LEGACY;

    }

    VidNotions.ModelCategory suggestModelCategory(ISdcCsarHelper csarHelper) {
        if (isALaCarte(csarHelper) && hasAnyNetworkWithPropertyNetworkTechnologyEqualsStandardSriovOrOvs(csarHelper)){
            return VidNotions.ModelCategory.IS_5G_PROVIDER_NETWORK_MODEL;
          } else if(isALaCarte(csarHelper) && hasFabricConfiguration(csarHelper)) {
            return VidNotions.ModelCategory.IS_5G_FABRIC_CONFIGURATION_MODEL;
        } else {
            return VidNotions.ModelCategory.OTHER;
        }
    }

    VidNotions.InstantiationUI suggestViewEditUI(ISdcCsarHelper csarHelper, ServiceModel serviceModel) {
        if (!featureManager.isActive(Features.FLAG_ASYNC_INSTANTIATION)){
            return VidNotions.InstantiationUI.LEGACY;
        }
        if (featureManager.isActive(Features.FLAG_1902_VNF_GROUPING) && isGrouping(csarHelper)) {
            return VidNotions.InstantiationUI.SERVICE_WITH_VNF_GROUPING;
        }

        if (featureManager.isActive(Features.FLAG_1902_NEW_VIEW_EDIT)) {
            if (isMacro(serviceModel) && !isMacroExcludedFromAsyncFlow(serviceModel)) {
                return VidNotions.InstantiationUI.MACRO_SERVICE;
            }
            VidNotions.InstantiationUI instantiationUISuggestion = suggestInstantiationUI(csarHelper);
            if (instantiationUISuggestion!=VidNotions.InstantiationUI.LEGACY) {
                return instantiationUISuggestion;
            }
        }

        return VidNotions.InstantiationUI.LEGACY;
    }

    private boolean isMacro(ServiceModel serviceModel) {
        return ToscaParserImpl2.Constants.MACRO.equals(serviceModel.getService().getInstantiationType());
    }

    private boolean isUuidExactlyHardCoded1ffce89fef3f(ISdcCsarHelper csarHelper) {
        return equalsIgnoreCase(
                csarHelper.getServiceMetadata().getValue(ToscaParserImpl2.Constants.UUID), "95eb2c44-bff2-4e8b-ad5d-8266870b7717");
    }

    private boolean hasAnyNetworkWithPropertyNetworkTechnologyEqualsStandardSriovOrOvs(ISdcCsarHelper csarHelper) {
        return hasAnyNetworkWithPropertyEqualsToAnyOf(csarHelper, "network_technology","Standard-SR-IOV","OVS") ;
    }

    boolean hasFabricConfiguration(ISdcCsarHelper csarHelper) {
        return csarHelper.getServiceNodeTemplates().stream()
                .flatMap(nodeTemplate -> csarHelper.getNodeTemplateChildren(nodeTemplate).stream())
                .anyMatch(child -> child.getType().equals(ToscaParserImpl2.Constants.FABRIC_CONFIGURATION_TYPE));
    }

    boolean hasAnyNetworkWithPropertyEqualsToAnyOf(ISdcCsarHelper csarHelper, String propertyName,  String... propertyValues) {
        return csarHelper
                .getServiceVlList().stream()
                .map(NodeTemplate::getProperties)
                .flatMap(props -> props.entrySet().stream())
                .filter(prop -> equalsIgnoreCase(prop.getKey(), propertyName))
                // getValue().getValue() because value is Entry, where it's inner value is what we're looking for
                .anyMatch(prop -> equalsAnyIgnoreCase(prop.getValue().getValue().toString(), propertyValues));
    }

    boolean isALaCarte(ISdcCsarHelper csarHelper) {
        final String instantiationType = csarHelper.getServiceMetadata().getValue(ToscaParserImpl2.Constants.INSTANTIATION_TYPE);
        return StringUtils.equalsIgnoreCase(instantiationType, ToscaParserImpl2.Constants.A_LA_CARTE);
    }

    boolean isMacroExcludedFromAsyncFlow(ServiceModel serviceModel) {
        return (MapUtils.isNotEmpty(serviceModel.getPnfs()) ||
                MapUtils.isNotEmpty(serviceModel.getCollectionResource()) ||
                (MapUtils.isNotEmpty(serviceModel.getNetworks()) && !featureManager.isActive(Features.FLAG_NETWORK_TO_ASYNC_INSTANTIATION)));


    }

    private boolean isGrouping(ISdcCsarHelper csarHelper) {
        final String serviceRole = csarHelper.getServiceMetadata().getValue(ToscaParserImpl2.Constants.SERVICE_ROLE);
        return StringUtils.equalsIgnoreCase(serviceRole, ToscaParserImpl2.Constants.GROUPING);
    }
}
