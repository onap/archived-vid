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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.tosca.parser.enums.FilterType;
import org.onap.sdc.tosca.parser.enums.SdcTypes;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.sdc.tosca.parser.impl.SdcToscaParserFactory;
import org.onap.sdc.toscaparser.api.Group;
import org.onap.sdc.toscaparser.api.*;
import org.onap.sdc.toscaparser.api.parameters.Input;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.*;

import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.onap.vid.asdc.parser.ToscaParserImpl2.Constants.VF_MODULE_LABEL;

public class ToscaParserImpl2 {


    private final VidNotionsBuilder vidNotionsBuilder;

    public class Constants {
        public static final String UUID = "UUID";
        public static final String DESCRIPTION = "description";
        public static final String SERVICE_TYPE = "serviceType";
        public static final String SERVICE_ROLE = "serviceRole";
        public static final String ECOMP_GENERATED_NAMING = "ecompGeneratedNaming";

        public static final String CUSTOMIZATION_UUID = "customizationUUID";
        public static final String VF_MODULE_MODEL_VERSION = "vfModuleModelVersion";
        public static final String VF_MODULE_MODEL_CUSTOMIZATION_UUID = "vfModuleModelCustomizationUUID";
        public static final String VOLUME_GROUP = "volume_group";
        public static final String VF_MODULE_MODEL_INVARIANT_UUID = "vfModuleModelInvariantUUID";
        public static final String VF_MODULE_MODEL_UUID = "vfModuleModelUUID";
        public static final String INVARIANT_UUID = "invariantUUID";
        public static final String VERSION = "version";
        public static final String NAME = "name";
        public static final String CATEGORY = "category";
        public static final String VF_MODULE_MODEL_NAME = "vfModuleModelName";
        public static final String GET_INPUT = "get_input";
        public static final String TYPE = "type";
        public static final String QUANTITY = "quantity";

        public static final String INSTANTIATION_TYPE = "instantiationType";
        //instantiation type
        public static final String BOTH = "Both";
        public static final String MACRO = "Macro";
        public static final String A_LA_CARTE = "A-La-Carte";
        public static final String CLIENT_CONFIG = "ClientConfig";

        //service role
        public static final String GROUPING = "GROUPING";

        //group properties
        public static final String MIN_VF_MODULE_INSTANCES = "min_vf_module_instances";
        public static final String MAX_VF_MODULE_INSTANCES = "max_vf_module_instances";
        public static final String INITIAL_COUNT = "initial_count";
        public static final String VF_MODULE_LABEL = "vf_module_label";
        public static final String VF_MODULE_TYPE = "vf_module_type";

        //collection resource properties
        public static final String SUBCATEGORY = "subcategory";
        public static final String RESOURCE_VENDOR = "resourceVendor";
        public static final String RESOURCE_VENDOR_RELEASE = "resourceVendorRelease";
        public static final String RESOURCE_VENDOR_MODEL_NUMBER = "resourceVendorModelNumber";
        public static final String ORG_OPENECOMP_GROUPS_NETWORK_COLLECTION = "org.openecomp.groups.NetworkCollection";
        public static final String NETWORK_COLLECTION_FUNCTION = "network_collection_function";
        public static final String NETWORK_COLLECTION_DESCRIPTION = "network_collection_description";

        //vfc instance group properties
        public static final String VFC_INSTANCE_GROUP_TYPE = "org.openecomp.groups.VfcInstanceGroup";
        public static final String VFC_PARENT_PORT_ROLE = "vfc_parent_port_role";
        public static final String SUBINTERFACE_ROLE = "subinterface_role";
        public static final String VFC_INSTANCE_GROUP_FUNCTION = "vfc_instance_group_function";

        public static final String FABRIC_CONFIGURATION_TYPE = "org.openecomp.nodes.FabricConfiguration";

        public static final String RESOURCE_GROUP_TYPE = "org.openecomp.groups.ResourceInstanceGroup";
        public static final String RESOURCE_GROUP_CONTAINED_TYPE = "contained_resource_type";

        public static final String VNF_GROUP = "VnfGroup";

        public static final String VRF_NODE_TYPE = "org.openecomp.nodes.VRFEntry";

        public static final String PORT_MIRRORING_CONFIGURATION_NODE_TYPE = "org.openecomp.nodes.PortMirroringConfiguration";

        public static final String PORT_MIRRORING_CONFIGURATION_BY_POLICY_NODE_TYPE = "org.openecomp.nodes.PortMirroringConfigurationByPolicy";

        public static final String NAMING_POLICY_TYPE = "org.openecomp.policies.External";

        public static final String SCALING_POLICY_TYPE = "org.openecomp.policies.scaling.Fixed";

        public static final String ECOMP_GENERATED_NAMING_PROPERTY = "ecomp_generated_naming";
    }

    public ToscaParserImpl2(VidNotionsBuilder vidNotionsBuilder) {
        this.vidNotionsBuilder = vidNotionsBuilder;
    }

    public ServiceModel makeServiceModel(Path path, Service asdcServiceMetadata) throws SdcToscaParserException {
        ServiceModel serviceModel = new ServiceModel();
        ISdcCsarHelper sdcCsarHelper = getSdcCsarHelper(path);
        List<String> policiesTargets = extractNamingPoliciesTargets(sdcCsarHelper);
        Map<String, Integer> scalingPolicies = extractScalingPolicyOfGroup(sdcCsarHelper);

        serviceModel.setService(extractServiceFromCsar(asdcServiceMetadata, sdcCsarHelper));
        serviceModel.setVolumeGroups(extractVolumeGroups(sdcCsarHelper));
        serviceModel.setVfModules(extractVfModuleFromCsar(sdcCsarHelper));
        serviceModel.setVnfs(extractVnfsFromCsar(sdcCsarHelper, policiesTargets));
        serviceModel.setConfigurations(extractPortMirroringConfigFromCsar(sdcCsarHelper, policiesTargets));
        serviceModel.setServiceProxies(extractServiceProxyFromCsar(sdcCsarHelper, policiesTargets));
        serviceModel.setNetworks(extractNetworksFromCsar(sdcCsarHelper, policiesTargets));
        serviceModel.setPnfs(extractPnfsFromCsar(sdcCsarHelper, policiesTargets));
        serviceModel.setCollectionResources(extractCRFromCsar(sdcCsarHelper, policiesTargets));
        serviceModel.setFabricConfigurations(extractFabricConfigFromCsar(sdcCsarHelper, policiesTargets));
        serviceModel.setVnfGroups(extractVnfGroupsFromCsar(sdcCsarHelper, policiesTargets, scalingPolicies));
        serviceModel.setVrfs(extractVrfsFromCsar(sdcCsarHelper, policiesTargets));
        serviceModel.getService().setVidNotions(vidNotionsBuilder.buildVidNotions(sdcCsarHelper, serviceModel));
        return serviceModel;
    }

    public ISdcCsarHelper getSdcCsarHelper(Path path) throws SdcToscaParserException {
        SdcToscaParserFactory factory = SdcToscaParserFactory.getInstance();
        return factory.getSdcCsarHelper(path.toFile().getAbsolutePath(), false);
    }

    private org.onap.vid.model.Service extractServiceFromCsar(Service asdcServiceMetadata, ISdcCsarHelper csarHelper) {
        org.onap.vid.model.Service service = new org.onap.vid.model.Service();

        service.setName(csarHelper.getServiceMetadata().getValue(Constants.NAME));
        service.setCategory(csarHelper.getServiceMetadata().getValue(Constants.CATEGORY));
        service.setInvariantUuid(csarHelper.getServiceMetadata().getValue(Constants.INVARIANT_UUID));
        service.setUuid(csarHelper.getServiceMetadata().getValue(Constants.UUID));
        service.setVersion(asdcServiceMetadata.getVersion());
        service.setDescription(csarHelper.getServiceMetadata().getValue(Constants.DESCRIPTION));
        service.setInputs(inputsListToInputsMap(csarHelper.getInputsWithAnnotations()));
        service.setServiceEcompNaming(isUserProvidingServiceNameOptional(csarHelper));
        service.setServiceType(csarHelper.getServiceMetadata().getValue(Constants.SERVICE_TYPE));
        service.setServiceRole(csarHelper.getServiceMetadata().getValue(Constants.SERVICE_ROLE));
        service.setInstantiationType(validateInstantiationType(csarHelper));
        return service;
    }

    private String isUserProvidingServiceNameOptional(ISdcCsarHelper csarHelper) {
        return ToscaNamingPolicy.isUserProvidingServiceNameOptional(csarHelper);
    }

    private Map<String, CR> extractCRFromCsar(ISdcCsarHelper sdcCsarHelper, List<String> policiesTargets) {
        List<NodeTemplate> nodeTemplates = sdcCsarHelper.getServiceNodeTemplates();
        Map<String, CR> collectionResourceMap = new HashMap<>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            if ( nodeTemplate.getMetaData().getValue(Constants.TYPE).equals(SdcTypes.CR.getValue()) ) {
                CR cr = new CR();
                populateCrFromNodeTemplate(nodeTemplate, sdcCsarHelper, cr, policiesTargets);
                collectionResourceMap.put(nodeTemplate.getName(), cr);
            }
        }

        return collectionResourceMap;
    }

    private Map<String, Node> extractFabricConfigFromCsar(ISdcCsarHelper sdcCsarHelper, List<String> policiesTargets) {
        List<NodeTemplate> nodeTemplates = sdcCsarHelper.getServiceNodeTemplates();
        Map<String, Node> fabricConfiguration = new HashMap<>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            List<NodeTemplate> nodeTemplateChildren = sdcCsarHelper.getNodeTemplateChildren(nodeTemplate);
            for (NodeTemplate nodeTemplateChild : nodeTemplateChildren) {
                if ( nodeTemplateChild.getType().equals(Constants.FABRIC_CONFIGURATION_TYPE) ) {
                    Node node = new Node();
                    fabricConfiguration.put(nodeTemplateChild.getName(), populateNodeFromNodeTemplate(nodeTemplateChild, sdcCsarHelper, node, policiesTargets));
                }
            }

        }
        return fabricConfiguration;
    }


    private void populateCrFromNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper sdcCsarHelper, CR cr, List<String> policiesTargets) {
        populateNodeFromNodeTemplate(nodeTemplate, sdcCsarHelper, cr, policiesTargets);
        cr.setCustomizationUUID(nodeTemplate.getMetaData().getValue(Constants.CUSTOMIZATION_UUID));
        cr.setCategory(nodeTemplate.getMetaData().getValue(Constants.CATEGORY));
        cr.setSubcategory(nodeTemplate.getMetaData().getValue(Constants.SUBCATEGORY));
        cr.setResourceVendor(nodeTemplate.getMetaData().getValue(Constants.RESOURCE_VENDOR));
        cr.setResourceVendorRelease(nodeTemplate.getMetaData().getValue(Constants.RESOURCE_VENDOR_RELEASE));
        cr.setResourceVendorModelNumber(nodeTemplate.getMetaData().getValue(Constants.RESOURCE_VENDOR_MODEL_NUMBER));
        cr.setNetworksCollection(getNetworksCollectionMapFromGroupsList(sdcCsarHelper, nodeTemplate));
    }

    private Map<String, NetworkCollection> getNetworksCollectionMapFromGroupsList(ISdcCsarHelper sdcCsarHelper, NodeTemplate nodeTemplate) {
        List<Group> groups = sdcCsarHelper.getGroupsOfOriginOfNodeTemplateByToscaGroupType(nodeTemplate, Constants.ORG_OPENECOMP_GROUPS_NETWORK_COLLECTION);
        Map<String, NetworkCollection> networksCollectionMap = new HashMap<>();
        for (Group group : groups) {
            networksCollectionMap.put(group.getName(), populateCollectionNetworkFromGroup(group, nodeTemplate));
        }
        return networksCollectionMap;
    }

    private NetworkCollection populateCollectionNetworkFromGroup(Group group, NodeTemplate nodeTemplate) {
        NetworkCollection networkCollection = new NetworkCollection();
        networkCollection.setUuid(group.getMetadata().getValue(Constants.UUID));
        networkCollection.setInvariantUuid(group.getMetadata().getValue(Constants.INVARIANT_UUID));
        networkCollection.setVersion(group.getMetadata().getValue(Constants.VERSION));
        networkCollection.setName(group.getMetadata().getValue(Constants.NAME));
        extractPropertiesOfCollectionNetworkFromCsar(group, nodeTemplate, networkCollection);

        return networkCollection;
    }

    private void extractPropertiesOfCollectionNetworkFromCsar(Group group, NodeTemplate nodeTemplate, NetworkCollection networkCollection) {
        LinkedHashMap<String, Property> properties = group.getProperties();
        Map<String, Property> nodeTemplateProperties = nodeTemplate.getProperties();

        String networkCollectionFunction = (String) ((Map) (properties.get(Constants.NETWORK_COLLECTION_FUNCTION).getValue())).get(Constants.GET_INPUT);
        String networkCollectionDescription = (String) ((Map) (properties.get(Constants.NETWORK_COLLECTION_DESCRIPTION).getValue())).get(Constants.GET_INPUT);

        networkCollection.getNetworkCollectionProperties().setNetworkCollectionDescription((String) nodeTemplateProperties.get(networkCollectionDescription).getValue());
        networkCollection.getNetworkCollectionProperties().setNetworkCollectionFunction((String) nodeTemplateProperties.get(networkCollectionFunction).getValue());
    }


    private Map<String, VNF> extractVnfsFromCsar(ISdcCsarHelper csarHelper, List<String> policiesTargets) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceVfList();
        Map<String, VNF> vnfsMaps = new HashMap<>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            VNF vnf = new VNF();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, vnf, policiesTargets);
            vnf.setModelCustomizationName(nodeTemplate.getName());
            vnf.setVfModules(getVfModulesFromVF(csarHelper, vnf.getCustomizationUuid()));
            vnf.setVolumeGroups(getVolumeGroupsFromVF(csarHelper, vnf.getCustomizationUuid()));
            vnf.setVfcInstanceGroups(getVfcInstanceGroup(csarHelper, nodeTemplate));
            if ("true".equals(ToscaNamingPolicy.getEcompNamingValueForNode(nodeTemplate, "nf_naming"))) {
                setEcompNamingProperty(vnf.getProperties(), "true");
            }
            vnfsMaps.put(nodeTemplate.getName(), vnf);
        }
        return vnfsMaps;
    }

    private Map<String, VfcInstanceGroup> getVfcInstanceGroup(ISdcCsarHelper csarHelper, NodeTemplate nodeTemplate) {
        List<Group> vfcList = csarHelper.getGroupsOfOriginOfNodeTemplateByToscaGroupType(nodeTemplate, Constants.VFC_INSTANCE_GROUP_TYPE);
        return vfcList.stream()
                .collect(toMap(Group::getName, group -> populateVfcInstanceGroupFromGroup(group, nodeTemplate)));
    }

    private VfcInstanceGroup populateVfcInstanceGroupFromGroup(Group group, NodeTemplate nodeTemplate) {
        VfcInstanceGroup vfcInstanceGroup = new VfcInstanceGroup();
        vfcInstanceGroup.setUuid(group.getMetadata().getValue(Constants.UUID));
        vfcInstanceGroup.setInvariantUuid(group.getMetadata().getValue(Constants.INVARIANT_UUID));
        vfcInstanceGroup.setVersion(group.getMetadata().getValue(Constants.VERSION));
        vfcInstanceGroup.setName(group.getMetadata().getValue(Constants.NAME));
        vfcInstanceGroup.setVfcInstanceGroupProperties(getVfcPropertiesFromGroup(nodeTemplate, group));

        return vfcInstanceGroup;

    }

    private VfcInstanceGroupProperties getVfcPropertiesFromGroup(NodeTemplate nodeTemplate, Group group) {
        VfcInstanceGroupProperties vfcInstanceGroupProperties = new VfcInstanceGroupProperties();
        vfcInstanceGroupProperties.setVfcParentPortRole((String) group.getProperties().get(Constants.VFC_PARENT_PORT_ROLE).getValue());
        vfcInstanceGroupProperties.setSubinterfaceRole((String) group.getProperties().get(Constants.SUBINTERFACE_ROLE).getValue());

        String networkCollectionFunction = (String) ((Map) (group.getProperties().get(Constants.NETWORK_COLLECTION_FUNCTION).getValue())).get(Constants.GET_INPUT);
        String vfcInstanceGroupFunction = (String) ((Map) (group.getProperties().get(Constants.VFC_INSTANCE_GROUP_FUNCTION).getValue())).get(Constants.GET_INPUT);

        if ( nodeTemplate.getProperties().get(networkCollectionFunction) != null )
            vfcInstanceGroupProperties.setNetworkCollectionFunction((String) nodeTemplate.getProperties().get(networkCollectionFunction).getValue());
        if ( nodeTemplate.getProperties().get(vfcInstanceGroupFunction) != null )
            vfcInstanceGroupProperties.setVfcInstanceGroupFunction((String) nodeTemplate.getProperties().get(vfcInstanceGroupFunction).getValue());

        return vfcInstanceGroupProperties;
    }

    private Map<String, PortMirroringConfig> extractPortMirroringConfigFromCsar(ISdcCsarHelper csarHelper, List<String> policiesTargets) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceNodeTemplatesByType(Constants.PORT_MIRRORING_CONFIGURATION_NODE_TYPE);
        nodeTemplates.addAll(csarHelper.getServiceNodeTemplatesByType(Constants.PORT_MIRRORING_CONFIGURATION_BY_POLICY_NODE_TYPE));
        Map<String, PortMirroringConfig> configMaps = new HashMap<>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            PortMirroringConfig pmConfig = new PortMirroringConfig();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, pmConfig, policiesTargets);

            pmConfig.setModelCustomizationName(nodeTemplate.getName());
            pmConfig.setRequirementAssignments(nodeTemplate.getRequirements());
            setSourceAndCollectorProxyNodes(csarHelper, pmConfig, nodeTemplate);

            configMaps.put(nodeTemplate.getName(), pmConfig);
        }

        return configMaps;
    }

    private Map<String, ServiceProxy> extractServiceProxyFromCsar(ISdcCsarHelper csarHelper, List<String> policiesTargets) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceNodeTemplateBySdcType(SdcTypes.SERVICE_PROXY);
        return nodeTemplates.stream()
                .collect(toMap(NodeTemplate::getName, node -> getServiceProxyFromNodeTemplate(node, csarHelper, policiesTargets)));
    }

    private ServiceProxy getServiceProxyFromNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper csarHelper, List<String> policiesTargets) {
        ServiceProxy serviceProxy = new ServiceProxy();
        populateNodeFromNodeTemplate(nodeTemplate, csarHelper, serviceProxy, policiesTargets);

        Map<String, String> metadata = nodeTemplate.getMetaData().getAllProperties();
        serviceProxy.setSourceModelUuid(metadata.get("sourceModelUuid"));
        serviceProxy.setSourceModelInvariant(metadata.get("sourceModelInvariant"));
        serviceProxy.setSourceModelName(metadata.get("sourceModelName"));

        return serviceProxy;
    }

    private void setSourceAndCollectorProxyNodes(ISdcCsarHelper csarHelper, PortMirroringConfig portMirroringConfig, NodeTemplate nodeTemplate) {
        RequirementAssignments requirementAssignments = nodeTemplate.getRequirements();

        List<String> sourceNodes = getRequirementsNodesNames(requirementAssignments.getRequirementsByName("source").getAll());
        portMirroringConfig.setSourceNodes(sourceNodes);

        List<String> collectorNodes = getRequirementsNodesNames(requirementAssignments.getRequirementsByName("collector").getAll());
        if ( !collectorNodes.isEmpty() ) { // vprobe
            portMirroringConfig.setCollectorNodes(collectorNodes);
        } else { // pprobe - configuration by policy
            String collectorNodeName = csarHelper.getNodeTemplatePropertyLeafValue(nodeTemplate, "collector_node");
            if ( collectorNodeName != null ) {
                portMirroringConfig.setCollectorNodes(Arrays.asList(collectorNodeName));
                portMirroringConfig.setConfigurationByPolicy(true);
            }
        }
    }

    private List<String> getRequirementsNodesNames(List<RequirementAssignment> requirements) {

        List<String> requirementsNodes = new ArrayList<>();
        if ( !CollectionUtils.isEmpty(requirements) ) {
            requirementsNodes = requirements.stream().map(RequirementAssignment::getNodeTemplateName).collect(toList());
        }

        return requirementsNodes;
    }

    Map<String, VfModule> getVfModulesFromVF(ISdcCsarHelper csarHelper, String vfUuid) {
        List<Group> vfModulesByVf = csarHelper.getVfModulesByVf(vfUuid);
        return vfModulesByVf.stream()
                .collect(toMap(Group::getName, group -> populateVfModuleFromGroup(group, csarHelper)));
    }

    Map<String, VolumeGroup> getVolumeGroupsFromVF(ISdcCsarHelper csarHelper, String vfCustomizationUuid) {
        List<Group> vfModulesByVf = csarHelper.getVfModulesByVf(vfCustomizationUuid);
        return vfModulesByVf.stream()
                .filter((group -> isVolumeGroup(group)))
                .collect(toMap(Group::getName, group -> populateVolumeGroupFromGroup(group, csarHelper)));
    }

    private static Boolean isVolumeGroup(Group group) {
        return Boolean.valueOf(group.getPropertyValue(Constants.VOLUME_GROUP).toString());
    }

    private Map<String, Network> extractNetworksFromCsar(ISdcCsarHelper csarHelper, List<String> policiesTargets) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceVlList();
        Map<String, Network> networksMap = new HashMap<>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            Network newNetwork = new Network();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, newNetwork, policiesTargets);
            newNetwork.setModelCustomizationName(nodeTemplate.getName());
            if ("true".equals(ToscaNamingPolicy.getEcompNamingValueForNode(nodeTemplate, "exVL_naming"))) {
                setEcompNamingProperty(newNetwork.getProperties(), "true");
            }
            networksMap.put(nodeTemplate.getName(), newNetwork);
        }
        return networksMap;
    }

    private Map<String, Node> extractPnfsFromCsar(ISdcCsarHelper csarHelper, List<String> policiesTargets) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceNodeTemplateBySdcType(SdcTypes.PNF);
        return this.extractNodesFromCsar(csarHelper,policiesTargets,nodeTemplates);
    }

    private Map<String, VfModule> extractVfModuleFromCsar(ISdcCsarHelper csarHelper) {
        List<NodeTemplate> serviceVfList = csarHelper.getServiceVfList();
        HashMap<String, VfModule> vfModuleHashMap = new HashMap<>();

        for (NodeTemplate nodeTemplate : serviceVfList) {
            Map<String, VfModule> nodeTemplateVfModule =
                    getVfModulesFromVF(csarHelper, nodeTemplate.getMetaData().getValue(Constants.CUSTOMIZATION_UUID));
            vfModuleHashMap.putAll(nodeTemplateVfModule);
        }
        return vfModuleHashMap;
    }

    private Map<String, VolumeGroup> extractVolumeGroups(ISdcCsarHelper csarHelper) {
        HashMap<String, VolumeGroup> volumeGroupHashMap = new HashMap<>();
        for (NodeTemplate nodeTemplate : csarHelper.getServiceVfList()) {
            Map<String, VolumeGroup> nodeTemplateVolumeGroups =
                    getVolumeGroupsFromVF(csarHelper, csarHelper.getNodeTemplateCustomizationUuid(nodeTemplate));
            volumeGroupHashMap.putAll(nodeTemplateVolumeGroups);
        }
        return volumeGroupHashMap;
    }

    private Map<String, org.onap.vid.asdc.beans.tosca.Input> inputsListToInputsMap(List<org.onap.sdc.toscaparser.api.parameters.Input> inputList) {
        Map<String, org.onap.vid.asdc.beans.tosca.Input> inputs = new HashMap<>();
        for (org.onap.sdc.toscaparser.api.parameters.Input input : inputList) {
            //Set only inputs without annotation to the service level
            if ( input.getAnnotations() == null )
                inputs.put(input.getName(), convertInput(input, new org.onap.vid.asdc.beans.tosca.Input()));
        }
        return inputs;
    }

    private Node populateNodeFromNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper csarHelper, Node newNode, List<String> policiesTargets) {
        newNode.setCustomizationUuid(csarHelper.getNodeTemplateCustomizationUuid(nodeTemplate));
        newNode.setDescription(nodeTemplate.getMetaData().getValue(Constants.DESCRIPTION));
        newNode.setInvariantUuid(nodeTemplate.getMetaData().getValue(Constants.INVARIANT_UUID));
        newNode.setUuid(nodeTemplate.getMetaData().getValue(Constants.UUID));
        newNode.setName(nodeTemplate.getMetaData().getValue(Constants.NAME));
        newNode.setVersion(nodeTemplate.getMetaData().getValue(Constants.VERSION));
        newNode.setInputs(extractInputsAndCommandsForNodeTemplate(nodeTemplate, csarHelper, newNode));
        newNode.setType(nodeTemplate.getMetaData().getValue(Constants.TYPE));
        Map<String, String> propertiesMap = nodeTemplate.getPropertiesObjects().stream()
                .collect(toMap(Property::getName, p -> p.getValue().toString()));
        setEcompNamingProperty(propertiesMap, String.valueOf(policiesTargets.contains(nodeTemplate.getName())));
        newNode.setProperties(propertiesMap);
        return newNode;
    }

    private VfModule populateVfModuleFromGroup(Group group, ISdcCsarHelper csarHelper) {
        VfModule vfModule = new VfModule();
        extractBasicPropertiesForGroup(vfModule, group, csarHelper);
        vfModule.setVolumeGroupAllowed(isVolumeGroup(group));
        return vfModule;
    }

    private VolumeGroup populateVolumeGroupFromGroup(Group group, ISdcCsarHelper csarHelper) {
        VolumeGroup volumeGroup = new VolumeGroup();
        extractBasicPropertiesForGroup(volumeGroup, group, csarHelper);
        return volumeGroup;
    }

    private void extractBasicPropertiesForGroup(org.onap.vid.model.Group newGroup, Group group, ISdcCsarHelper csarHelper) {
        newGroup.setDescription(group.getDescription());
        newGroup.setVersion(group.getMetadata().getValue(Constants.VF_MODULE_MODEL_VERSION));
        newGroup.setCustomizationUuid(group.getMetadata().getValue(Constants.VF_MODULE_MODEL_CUSTOMIZATION_UUID));
        newGroup.setModelCustomizationName(group.getMetadata().getValue(Constants.VF_MODULE_MODEL_NAME));
        newGroup.setName(group.getMetadata().getValue(Constants.VF_MODULE_MODEL_NAME));
        newGroup.setUuid(group.getMetadata().getValue(Constants.VF_MODULE_MODEL_UUID));
        newGroup.setInvariantUuid(group.getMetadata().getValue(Constants.VF_MODULE_MODEL_INVARIANT_UUID));
        newGroup.setProperties(extractVfModuleProperties(group, csarHelper));
        newGroup.setInputs(extractVfInputsFromCsarByAnnotation(csarHelper, newGroup));
    }


    private Map<String, org.onap.vid.asdc.beans.tosca.Input> extractVfInputsFromCsarByAnnotation(ISdcCsarHelper csarHelper, org.onap.vid.model.Group group) {
        Map<String, org.onap.vid.asdc.beans.tosca.Input> inputMap = new HashMap<>();
        if ( group.getProperties().getVfModuleLabel() != null ) {
            List<Input> inputsList = csarHelper.getInputsWithAnnotations();
            for (Input input : inputsList) {
                extractVfModuleInput(group, inputMap, input);
            }
        }
        return inputMap;
    }

    private void extractVfModuleInput(org.onap.vid.model.Group group, Map<String, org.onap.vid.asdc.beans.tosca.Input> inputMap, Input input) {
        if ( input.getAnnotations() != null ) {
            List<Property> annotationProperties = input.getAnnotations().get("source").getProperties();
            if ( isInputMatchesToGroup(annotationProperties, group) ) {
                final org.onap.vid.asdc.beans.tosca.Input vfModuleInput = new org.onap.vid.asdc.beans.tosca.Input(input, annotationProperties);
                if ( vfModuleInput.getInputProperties() != null && vfModuleInput.getInputProperties().getParamName() != null ) {
                    inputMap.put(vfModuleInput.getInputProperties().getParamName(), vfModuleInput);
                } else {
                    inputMap.put(input.getName(), vfModuleInput);
                }
            }
        }
    }


    private boolean isInputMatchesToGroup(List<Property> annotationProperties, org.onap.vid.model.Group group) {
        for (Property property : annotationProperties) {
            if ( property.getName().equals(VF_MODULE_LABEL) ) {
                final Object values = property.getValue();
                final String vfModuleLabel = group.getProperties().getVfModuleLabel();
                if ( values instanceof List ) {
                    if ( listContainsAsString((List) values, vfModuleLabel) ) return true;
                } else {
                    return getPropertyValueAsString(property).equals(vfModuleLabel);
                }
            }
        }
        return false;
    }

    private boolean listContainsAsString(List list, String value) {
        for (Object v : list) {
            if ( StringUtils.equals(v.toString(), value) ) {
                return true;
            }
        }
        return false;
    }

    public String getPropertyValueAsString(Property property) {
        return removeSquareBrackets(property.getValue().toString());
    }

    private String removeSquareBrackets(String stringWithSquareBrackets) {
        return stringWithSquareBrackets.replaceAll("(^\\[|\\]$)", "");
    }

    private GroupProperties extractVfModuleProperties(Group group, ISdcCsarHelper csarHelper) {
        GroupProperties vfModuleProperties = new GroupProperties();
        if ( csarHelper.getGroupPropertyAsObject(group, Constants.MIN_VF_MODULE_INSTANCES) != null )
            vfModuleProperties.setMinCountInstances((Integer) csarHelper.getGroupPropertyAsObject(group, Constants.MIN_VF_MODULE_INSTANCES));
        if ( csarHelper.getGroupPropertyAsObject(group, Constants.MAX_VF_MODULE_INSTANCES) != null )
            vfModuleProperties.setMaxCountInstances((Integer) csarHelper.getGroupPropertyAsObject(group, Constants.MAX_VF_MODULE_INSTANCES));
        if ( csarHelper.getGroupPropertyAsObject(group, Constants.INITIAL_COUNT) != null )
            vfModuleProperties.setInitialCount((Integer) csarHelper.getGroupPropertyAsObject(group, Constants.INITIAL_COUNT));
        if ( csarHelper.getGroupPropertyAsObject(group, VF_MODULE_LABEL) != null )
            vfModuleProperties.setVfModuleLabel((String) csarHelper.getGroupPropertyAsObject(group, VF_MODULE_LABEL));
        vfModuleProperties.setBaseModule(isModuleTypeIsBaseObjectSafe(csarHelper.getGroupPropertyAsObject(group, Constants.VF_MODULE_TYPE)));
        return vfModuleProperties;
    }


    public static boolean isModuleTypeIsBaseObjectSafe(@Nullable Object vfModuleTypeValue) {
        return (vfModuleTypeValue instanceof String) && (isModuleTypeIsBase((String) vfModuleTypeValue));
    }

    protected static boolean isModuleTypeIsBase(String vfModuleTypeValue) {
        return "Base".equalsIgnoreCase(vfModuleTypeValue);
    }


    private Map<String, org.onap.vid.asdc.beans.tosca.Input> extractInputsAndCommandsForNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper csarHelper, Node newNode) {
        Map<String, org.onap.vid.asdc.beans.tosca.Input> inputMap = new HashMap<>();
        Map<String, CommandProperty> commandPropertyMap = new HashMap<>();

        List<Input> inputs = csarHelper.getServiceInputs();
        Map<String, String> properties = csarHelper.filterNodeTemplatePropertiesByValue(nodeTemplate, FilterType.CONTAINS, Constants.GET_INPUT);
        for (Map.Entry<String, String> property : properties.entrySet()) {
            String inputKey = property.getValue();
            String key = extractInputValue(inputKey);
            for (Input input : inputs) {
                if ( input.getName().equals(key) ) {
                    org.onap.vid.asdc.beans.tosca.Input localInput = new org.onap.vid.asdc.beans.tosca.Input();
                    localInput = convertInput(input, localInput);
                    String name = property.getKey();
                    commandPropertyMap.put(name, extractCommands(name, key));
                    inputMap.put(name, localInput);
                }
            }
        }
        newNode.setCommands(commandPropertyMap);
        return inputMap;
    }

    private String extractInputValue(String inputKey) {
        return inputKey.substring(inputKey.indexOf(':') + 1);
    }

    private org.onap.vid.asdc.beans.tosca.Input convertInput(Input parserInput, org.onap.vid.asdc.beans.tosca.Input localInput) {
        localInput.setDefault(parserInput.getDefault());
        localInput.setDescription(parserInput.getDescription());
        localInput.setRequired(parserInput.isRequired());
        localInput.setType(parserInput.getType());
        localInput.setConstraints(parserInput.getConstraints());
//        localInput.setentry_schema()
        return localInput;
    }

    private CommandProperty extractCommands(String displayName, String inputName) {
        CommandProperty commandProperty = new CommandProperty();
        commandProperty.setDisplayName(displayName);
        commandProperty.setCommand(Constants.GET_INPUT);
        commandProperty.setInputName(inputName);
        return commandProperty;
    }

    private void setEcompNamingProperty(Map<String, String> propertiesMap, String isUserProvidingVnfNameOptional) {
        propertiesMap.put(Constants.ECOMP_GENERATED_NAMING_PROPERTY, isUserProvidingVnfNameOptional);
    }

    private String validateInstantiationType(ISdcCsarHelper csarHelper){
        String instantiationType = csarHelper.getServiceMetadata().getValue(Constants.INSTANTIATION_TYPE);
        String validatedInstantiationType = Constants.CLIENT_CONFIG;
        if(instantiationType != null && !instantiationType.isEmpty()){
            if(instantiationType.equalsIgnoreCase(Constants.MACRO) || instantiationType.equalsIgnoreCase(Constants.BOTH))
                validatedInstantiationType = Constants.MACRO;
            else if(instantiationType.equalsIgnoreCase(Constants.A_LA_CARTE))
                validatedInstantiationType = Constants.A_LA_CARTE;
        }
        return validatedInstantiationType;
    }

    private Map<String, Node> extractVrfsFromCsar(ISdcCsarHelper csarHelper, List<String> policiesTargets) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceNodeTemplatesByType(Constants.VRF_NODE_TYPE);
        return this.extractNodesFromCsar(csarHelper,policiesTargets,nodeTemplates);
    }

    private Map<String, Node> extractNodesFromCsar(ISdcCsarHelper csarHelper, List<String> policiesTargets, List<NodeTemplate> nodeTemplates){
        HashMap<String, Node> nodeHashMap = new HashMap<>();
        for (NodeTemplate nodeTemplate : nodeTemplates) {
            Node node = new Node();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, node, policiesTargets);
            nodeHashMap.put(nodeTemplate.getName(), node);
        }
        return nodeHashMap;
    }

    private Map<String, ResourceGroup> extractVnfGroupsFromCsar(ISdcCsarHelper csarHelper, List<String> policiesTargets, Map<String, Integer> scalingPolicies) {
        List<Group> resourceGroups = csarHelper.getGroupsOfTopologyTemplateByToscaGroupType(Constants.RESOURCE_GROUP_TYPE);

        return resourceGroups.stream()
                .filter(group -> group.getProperties().get(Constants.RESOURCE_GROUP_CONTAINED_TYPE).getValue().equals("VF"))
                .collect(toMap(Group::getName, group -> parseResourceGroup(group, csarHelper, policiesTargets, scalingPolicies)));
    }

    private ResourceGroup parseResourceGroup(Group group, ISdcCsarHelper csarHelper, List<String> policiesTargets, Map<String, Integer> scalingPolicies) {
        return new ResourceGroup(
                Constants.VNF_GROUP,
                group.getMetadata().getValue(Constants.INVARIANT_UUID),
                group.getMetadata().getValue(Constants.UUID),
                group.getMetadata().getValue(Constants.VERSION),
                group.getMetadata().getValue(Constants.NAME),
                group.getMetadata().getValue(Constants.NAME),
                getPropertiesOfResourceGroup(group.getProperties(), policiesTargets.contains(group.getName()), scalingPolicies.get(group.getName())),
                csarHelper.getGroupMembersFromTopologyTemplate(group.getName()).stream()
                        .collect(toMap(NodeTemplate::getName, node -> getServiceProxyFromNodeTemplate(node, csarHelper, policiesTargets)))
        );
    }

    private Map<String, Object> getPropertiesOfResourceGroup(Map<String, Property> properties, boolean hasPolicyTarget, Integer qty) {
        Map<String, Object> propertiesMap = properties.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().getValue()));
        propertiesMap.put(Constants.ECOMP_GENERATED_NAMING_PROPERTY, String.valueOf(hasPolicyTarget));
        if (qty != null)  {
            propertiesMap.put(Constants.QUANTITY, qty);
        }

        return propertiesMap;
    }

    public List<String> extractNamingPoliciesTargets(ISdcCsarHelper csarHelper) {
        List<Policy> policies = csarHelper.getPoliciesOfTopologyTemplateByToscaPolicyType(Constants.NAMING_POLICY_TYPE);
        return policies.stream()
                .filter(policy -> policy.getProperties().get(Constants.TYPE) != null &&
                        policy.getProperties().get(Constants.TYPE).getValue() != null &&
                        StringUtils.equalsIgnoreCase(policy.getProperties().get(Constants.TYPE).getValue().toString(), "naming"))
                .flatMap(policy -> policy.getTargets().stream())
                .collect(toList());
    }

    public Map<String, Integer> extractScalingPolicyOfGroup(ISdcCsarHelper csarHelper)  {
        return csarHelper.getPoliciesOfTopologyTemplateByToscaPolicyType(Constants.SCALING_POLICY_TYPE)
                .stream()
                .filter(policy -> policy.getProperties().containsKey(Constants.QUANTITY))
                .flatMap(policy -> {
                    Integer qty = Integer.parseInt(policy.getProperties().get(Constants.QUANTITY).getValue().toString());
                    return policy
                            .getTargets().stream()
                            .map(target -> Pair.of(target, qty));
                })
                .collect(toMap(Pair::getKey, Pair::getValue));
    }
}