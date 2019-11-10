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

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.sdc.toscaparser.api.elements.Metadata;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.VidNotions.ModelCategory;
import org.onap.vid.properties.Features;
import org.togglz.core.manager.FeatureManager;

public class VidNotionsBuilder {

    private final FeatureManager featureManager;

    //map of service type that are always macro services, and their relevant featureFlag
    private static final Map<VidNotions.ModelCategory, Features> macroServicesByModelCategory = ImmutableMap.of(
            VidNotions.ModelCategory.INFRASTRUCTURE_VPN, Features.FLAG_1908_INFRASTRUCTURE_VPN,
            VidNotions.ModelCategory.Transport, Features.FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI,
            VidNotions.ModelCategory.SERVICE_WITH_COLLECTION_RESOURCE, Features.FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI
    );

    public VidNotionsBuilder(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    VidNotions buildVidNotions(ISdcCsarHelper csarHelper, ServiceModel serviceModel) {
        VidNotions.ModelCategory modelCategory = suggestModelCategory(csarHelper, serviceModel);
        return new VidNotions(
                suggestInstantiationUI(csarHelper, serviceModel),
                modelCategory,
                suggestViewEditUI(csarHelper, serviceModel),
                suggestInstantiationType(serviceModel, modelCategory));
    }

    private boolean isMacroTypeByModelCategory(VidNotions.ModelCategory modelCategory) {
        Features featureOfMacroType = macroServicesByModelCategory.get(modelCategory);
        //if featureOfMacroType is null this service is not a macro by its type
        return (featureOfMacroType!=null && featureManager.isActive(featureOfMacroType));
    }

    VidNotions.InstantiationType suggestInstantiationType(ServiceModel serviceModel, VidNotions.ModelCategory modelCategory) {
        if (isMacroTypeByModelCategory(modelCategory)) {
            return VidNotions.InstantiationType.Macro;
        }
        if (serviceModel==null || serviceModel.getService()==null || isEmpty(serviceModel.getService().getInstantiationType())) {
            return VidNotions.InstantiationType.ClientConfig;
        }
        String instantiationType = serviceModel.getService().getInstantiationType();
        if (instantiationType.equals(ToscaParserImpl2.Constants.MACRO)) {
            return VidNotions.InstantiationType.Macro;
        }
        if (instantiationType.equals(ToscaParserImpl2.Constants.A_LA_CARTE)) {
            return VidNotions.InstantiationType.ALaCarte;
        }

        return VidNotions.InstantiationType.ClientConfig;
    }

    //UI route a-la-carte services to old UI only if InstantiationUI is LEGACY
    //So any other value for InstantiationUI other than LEGACY make UI to route
    //a-la-carte services to new UI
    VidNotions.InstantiationUI suggestInstantiationUI(ISdcCsarHelper csarHelper, ServiceModel serviceModel) {
        if(featureManager.isActive(Features.FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI) && isALaCarte(csarHelper)) {
            return VidNotions.InstantiationUI.ANY_ALACARTE_NEW_UI;
        }
        if (featureManager.isActive(Features.FLAG_1902_VNF_GROUPING) && isGrouping(csarHelper)) {
            return VidNotions.InstantiationUI.SERVICE_WITH_VNF_GROUPING;
        }
        if (featureManager.isActive(Features.FLAG_5G_IN_NEW_INSTANTIATION_UI)) {
            VidNotions.InstantiationUI instantiationUI = determine5GInstantiationUI(csarHelper);
            if ( instantiationUI != null ) return instantiationUI;
        }
        if (featureManager.isActive(Features.FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI) && isTransportService(csarHelper)){
            return VidNotions.InstantiationUI.TRANSPORT_SERVICE;
        }
        if (featureManager.isActive(Features.FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI) && isServiceWithCollectionResource(serviceModel)){
            return VidNotions.InstantiationUI.SERVICE_WITH_COLLECTION_RESOURCE;
        }
        if (featureManager.isActive(Features.FLAG_1908_INFRASTRUCTURE_VPN) && isInfraStructureVpn(csarHelper)){
            return VidNotions.InstantiationUI.INFRASTRUCTURE_VPN;
        }
        if (featureManager.isActive(Features.FLAG_1908_A_LA_CARTE_VNF_NEW_INSTANTIATION_UI) && isVnfServiceRole(csarHelper)){
            return VidNotions.InstantiationUI.A_LA_CARTE_VNF_SERVICE_ROLE;
        }
        return VidNotions.InstantiationUI.LEGACY;

    }

    private boolean isVnfServiceRole(ISdcCsarHelper csarHelper) {
        final String serviceRole = csarHelper.getServiceMetadata().getValue(ToscaParserImpl2.Constants.SERVICE_ROLE );
        return StringUtils.equalsIgnoreCase("VNF" , serviceRole);
    }

    @Nullable
    private VidNotions.InstantiationUI determine5GInstantiationUI(ISdcCsarHelper csarHelper) {
        if (isUuidExactlyHardCoded1ffce89fef3f(csarHelper)) {
            return VidNotions.InstantiationUI.SERVICE_UUID_IS_1ffce89f_ef3f_4cbb_8b37_82134590c5de;
        } else if (isALaCarte(csarHelper) && hasAnyNetworkWithPropertyNetworkTechnologyEqualsStandardSriovOrOvs(csarHelper)) {
            return VidNotions.InstantiationUI.NETWORK_WITH_PROPERTY_NETWORK_TECHNOLOGY_EQUALS_STANDARD_SRIOV_OR_OVS;
        } else if (isALaCarte(csarHelper) && hasFabricConfiguration(csarHelper)) {
            return VidNotions.InstantiationUI.SERVICE_WITH_FABRIC_CONFIGURATION;
        }
        return null;
    }

    private boolean isTransportService(ISdcCsarHelper csarHelper) {
        return ("TRANSPORT".equalsIgnoreCase(csarHelper.getServiceMetadata().getValue(ToscaParserImpl2.Constants.SERVICE_TYPE)));
    }

    private boolean isServiceWithCollectionResource(ServiceModel serviceModel){
        return MapUtils.isNotEmpty(serviceModel.getCollectionResources());
    }

    private boolean isInfraStructureVpn(ISdcCsarHelper csarHelper) {
        Metadata serviceMetadata = csarHelper.getServiceMetadata();
        return ("BONDING".equalsIgnoreCase(serviceMetadata.getValue(ToscaParserImpl2.Constants.SERVICE_TYPE)) &&
                "INFRASTRUCTURE-VPN".equalsIgnoreCase(serviceMetadata.getValue(ToscaParserImpl2.Constants.SERVICE_ROLE)));
    }

    VidNotions.ModelCategory suggestModelCategory(ISdcCsarHelper csarHelper, ServiceModel serviceModel) {
        if (isALaCarte(csarHelper) && hasAnyNetworkWithPropertyNetworkTechnologyEqualsStandardSriovOrOvs(csarHelper)){
            return VidNotions.ModelCategory.IS_5G_PROVIDER_NETWORK_MODEL;
        }
        if(isALaCarte(csarHelper) && hasFabricConfiguration(csarHelper)) {
            return VidNotions.ModelCategory.IS_5G_FABRIC_CONFIGURATION_MODEL;
        }
        if (isPortMirroringService(serviceModel)) {
            return ModelCategory.PORT_MIRRORING;
        }
        if (isVlanTaggingService(serviceModel)) {
            return ModelCategory.VLAN_TAGGING;
        }
        if (isInfraStructureVpn(csarHelper)) {
            return VidNotions.ModelCategory.INFRASTRUCTURE_VPN;
        }
        if (isTransportService(csarHelper)) {
            return VidNotions.ModelCategory.Transport;
        }
        if (isServiceWithCollectionResource(serviceModel)) {
            return VidNotions.ModelCategory.SERVICE_WITH_COLLECTION_RESOURCE;
        }
        return VidNotions.ModelCategory.OTHER;
    }

    VidNotions.InstantiationUI suggestViewEditUI(ISdcCsarHelper csarHelper, ServiceModel serviceModel) {
        if (featureManager.isActive(Features.FLAG_1902_VNF_GROUPING) && isGrouping(csarHelper)) {
            return VidNotions.InstantiationUI.SERVICE_WITH_VNF_GROUPING;
        }

        if (featureManager.isActive(Features.FLAG_1908_MACRO_NOT_TRANSPORT_NEW_VIEW_EDIT) &&
            isMacro(serviceModel) &&
            !isTransportService(csarHelper) &&
            //till new view/edit would support fabric service activation
            !hasFabricConfiguration(csarHelper)) {
            return VidNotions.InstantiationUI.MACRO_SERVICE;
        }

        if (featureManager.isActive(Features.FLAG_1902_NEW_VIEW_EDIT)) {
            VidNotions.InstantiationUI instantiationUISuggestion = suggestInstantiationUI(csarHelper, serviceModel);
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
                MapUtils.isNotEmpty(serviceModel.getCollectionResources()) ||
                (MapUtils.isNotEmpty(serviceModel.getNetworks()) && !featureManager.isActive(Features.FLAG_NETWORK_TO_ASYNC_INSTANTIATION)));
    }

    private boolean isGrouping(ISdcCsarHelper csarHelper) {
        final String serviceRole = csarHelper.getServiceMetadata().getValue(ToscaParserImpl2.Constants.SERVICE_ROLE);
        return StringUtils.equalsIgnoreCase(serviceRole, ToscaParserImpl2.Constants.GROUPING);
    }

    private boolean isPortMirroringService(ServiceModel serviceModel) {
        return (serviceModel.getService()!=null &&
            StringUtils.equals(serviceModel.getService().getServiceType(), "portMirroring"));
    }

    private boolean isVlanTaggingService(ServiceModel serviceModel) {
        if (serviceModel==null || serviceModel.getVnfs()==null) {
            return false;
        }

        return serviceModel.getVnfs().values().stream().anyMatch(
            vnf-> MapUtils.isNotEmpty(vnf.getVfcInstanceGroups())
        );

    }
}
