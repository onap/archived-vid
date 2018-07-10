package org.onap.vid.asdc.parser;

import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.*;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.sdc.tosca.parser.impl.FilterType;
import org.onap.sdc.tosca.parser.impl.SdcToscaParserFactory;
import org.onap.sdc.tosca.parser.impl.SdcTypes;
import org.onap.sdc.toscaparser.api.Group;
import org.onap.sdc.toscaparser.api.*;
import org.onap.sdc.toscaparser.api.elements.Metadata;
import org.onap.sdc.toscaparser.api.parameters.Input;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.onap.vid.asdc.parser.ToscaParserImpl2.Constants.VF_MODULE_LABEL;

public class ToscaParserImpl2 {

    
	public class Constants {
		public final static String UUID = "UUID";
		public final static String DESCRIPTION = "description";
		public final static String SERVICE_TYPE = "serviceType";
        public final static String SERVICE_ROLE = "serviceRole";
        public final static String ECOMP_GENERATED_NAMING = "ecompGeneratedNaming";

        public final static String CUSTOMIZATION_UUID = "customizationUUID";
		public final static String VF_MODULE_MODEL_VERSION = "vfModuleModelVersion";
		public final static String VF_MODULE_MODEL_CUSTOMIZATION_UUID = "vfModuleModelCustomizationUUID";
		public final static String VOLUME_GROUP = "volume_group";
		public final static String VF_MODULE_MODEL_INVARIANT_UUID = "vfModuleModelInvariantUUID";
		public final static String VF_MODULE_MODEL_UUID = "vfModuleModelUUID";
		public final static String INVARIANT_UUID = "invariantUUID";
		public final static String VERSION = "version";
		public final static String NAME = "name";
		public final static String CATEGORY = "category";
		public final static String VF_MODULE_MODEL_NAME = "vfModuleModelName";
		public final static String GET_INPUT = "get_input";
		public final static String TYPE = "type";

		public final static String INSTANTIATION_TYPE = "instantiationType";
        //instantiation type
        public final static String BOTH = "Both";
        public final static String MACRO = "Macro";
        public final static String A_LA_CARTE = "A-La-Carte";
        public final static String CLIENT_CONFIG = "ClientConfig";

        //group properties
        public final static String MIN_VF_MODULE_INSTANCES = "min_vf_module_instances";
        public final static String MAX_VF_MODULE_INSTANCES = "max_vf_module_instances";
        public final static String INITIAL_COUNT = "initial_count";
        public final static String VF_MODULE_LABEL = "vf_module_label";

        //collection resource properties
        public final static String SUBCATEGORY = "subcategory";
        public final static String RESOURCE_VENDOR = "resourceVendor";
        public final static String RESOURCE_VENDOR_RELEASE = "resourceVendorRelease";
        public final static String RESOURCE_VENDOR_MODEL_NUMBER = "resourceVendorModelNumber";
        public final static String ORG_OPENECOMP_GROUPS_NETWORK_COLLECTION = "org.openecomp.groups.NetworkCollection";
        public final static String NETWORK_COLLECTION_FUNCTION = "network_collection_function";
        public final static String NETWORK_COLLECTION_DESCRIPTION = "network_collection_description";

        //vfc instance group properties
        public final static String VFC_INSTANCE_GROUP_TYPE = "org.openecomp.groups.VfcInstanceGroup";
        public final static String VFC_PARENT_PORT_ROLE = "vfc_parent_port_role";
        public final static String SUBINTERFACE_ROLE = "subinterface_role";
        public final static String VFC_INSTANCE_GROUP_FUNCTION = "vfc_instance_group_function";
    }
	
	public ToscaParserImpl2() {}

    public ServiceModel  makeServiceModel(Path path, Service asdcServiceMetadata) throws SdcToscaParserException {
        ServiceModel serviceModel = new ServiceModel();
        SdcToscaParserFactory factory = SdcToscaParserFactory.getInstance();
        ISdcCsarHelper sdcCsarHelper = factory.getSdcCsarHelper(path.toFile().getAbsolutePath(),false);

        serviceModel.setService(extractServiceFromCsar(asdcServiceMetadata, sdcCsarHelper));
        serviceModel.setVolumeGroups(extractVolumeGroups(sdcCsarHelper));
        serviceModel.setVfModules(extractVfModuleFromCsar(sdcCsarHelper));
        serviceModel.setVnfs(extractVnfsFromCsar(sdcCsarHelper));
        serviceModel.setConfigurations(extractPortMirroringConfigFromCsar(sdcCsarHelper));
        serviceModel.setServiceProxies(extractServiceProxyFromCsar(sdcCsarHelper));
        serviceModel.setNetworks(extractNetworksFromCsar(sdcCsarHelper));
        serviceModel.setPnfs(extractPnfsFromCsar(sdcCsarHelper));
        serviceModel.setCollectionResource(extractCRFromCsar(sdcCsarHelper));
        return serviceModel;
    }



    private  org.onap.vid.model.Service extractServiceFromCsar(Service asdcServiceMetadata, ISdcCsarHelper csarHelper) throws SdcToscaParserException {
        org.onap.vid.model.Service service = new  org.onap.vid.model.Service();

        service.setName(csarHelper.getServiceMetadata().getValue(Constants.NAME));
        service.setCategory(csarHelper.getServiceMetadata().getValue(Constants.CATEGORY));
        service.setInvariantUuid(csarHelper.getServiceMetadata().getValue(Constants.INVARIANT_UUID));
        service.setUuid(csarHelper.getServiceMetadata().getValue(Constants.UUID));
        service.setVersion(asdcServiceMetadata.getVersion());
        service.setDescription(csarHelper.getServiceMetadata().getValue(Constants.DESCRIPTION));
        service.setInputs(inputsListToInputsMap(csarHelper.getInputsWithAnnotations()));
        service.setServiceEcompNaming(csarHelper.getServiceMetadata().getValue(Constants.ECOMP_GENERATED_NAMING));
        service.setServiceType(csarHelper.getServiceMetadata().getValue(Constants.SERVICE_TYPE));
        service.setServiceRole(csarHelper.getServiceMetadata().getValue(Constants.SERVICE_ROLE));
        service.setInstantiationType(validateInstantiationType(csarHelper));
        return service;
    }

    private Map<String,CR> extractCRFromCsar(ISdcCsarHelper sdcCsarHelper) {
	    List<NodeTemplate> nodeTemplates = sdcCsarHelper.getServiceNodeTemplates();
	    Map<String, CR> collectionResourceMap = new HashMap<>();

	    for(NodeTemplate nodeTemplate: nodeTemplates){
	        if(nodeTemplate.getMetaData().getValue(Constants.TYPE).equals(SdcTypes.CR.getValue())) {
                CR cr = new CR();
                populateCrFromNodeTemplate(nodeTemplate, sdcCsarHelper, cr);
                collectionResourceMap.put(nodeTemplate.getName(), cr);
            }
        }

        return collectionResourceMap;
    }

    private void populateCrFromNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper sdcCsarHelper, CR cr) {
        populateNodeFromNodeTemplate(nodeTemplate, sdcCsarHelper, cr);
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
        Map<String, NetworkCollection> networksCollectionMap = new HashMap<String, NetworkCollection>();
        for(Group group: groups){
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

        String networkCollectionFunction = (String)((Map)(properties.get(Constants.NETWORK_COLLECTION_FUNCTION).getValue())).get(Constants.GET_INPUT);
        String networkCollectionDescription = (String)((Map)(properties.get(Constants.NETWORK_COLLECTION_DESCRIPTION).getValue())).get(Constants.GET_INPUT);

        networkCollection.getNetworkCollectionProperties().setNetworkCollectionDescription((String)nodeTemplateProperties.get(networkCollectionDescription).getValue());
        networkCollection.getNetworkCollectionProperties().setNetworkCollectionFunction((String)nodeTemplateProperties.get(networkCollectionFunction).getValue());
    }


    private Map<String, VNF> extractVnfsFromCsar(ISdcCsarHelper csarHelper) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceVfList();
        Map<String, VNF> vnfsMaps = new HashMap<String, VNF>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            VNF vnf = new VNF();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, vnf);
            vnf.setModelCustomizationName(nodeTemplate.getName());
            vnf.setVfModules(getVfModulesFromVF(csarHelper, vnf.getCustomizationUuid()));
            vnf.setVolumeGroups(getVolumeGroupsFromVF(csarHelper, vnf.getCustomizationUuid()));
            vnf.setVfcInstanceGroups(getVfcInstanceGroup(csarHelper, nodeTemplate));

            vnfsMaps.put(nodeTemplate.getName(), vnf);
        }
        return vnfsMaps;
    }

    private Map<String,VfcInstanceGroup> getVfcInstanceGroup(ISdcCsarHelper csarHelper, NodeTemplate nodeTemplate) {
	    List<Group> vfcList = csarHelper.getGroupsOfOriginOfNodeTemplateByToscaGroupType(nodeTemplate, Constants.VFC_INSTANCE_GROUP_TYPE);
        return vfcList.stream()
                .collect(toMap(Group::getName, group -> populateVfcInstanceGroupFromGroup(group, csarHelper, nodeTemplate)));
    }

    private VfcInstanceGroup populateVfcInstanceGroupFromGroup(Group group, ISdcCsarHelper csarHelper, NodeTemplate nodeTemplate) {
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

        String networkCollectionFunction = (String)((Map)(group.getProperties().get(Constants.NETWORK_COLLECTION_FUNCTION).getValue())).get(Constants.GET_INPUT);
        String vfcInstanceGroupFunction = (String)((Map)(group.getProperties().get(Constants.VFC_INSTANCE_GROUP_FUNCTION).getValue())).get(Constants.GET_INPUT);

        if(nodeTemplate.getProperties().get(networkCollectionFunction) != null)
            vfcInstanceGroupProperties.setNetworkCollectionFunction((String) nodeTemplate.getProperties().get(networkCollectionFunction).getValue());
        if(nodeTemplate.getProperties().get(vfcInstanceGroupFunction) != null)
            vfcInstanceGroupProperties.setVfcInstanceGroupFunction((String) nodeTemplate.getProperties().get(vfcInstanceGroupFunction).getValue());

        return vfcInstanceGroupProperties;
    }

    private Map<String, PortMirroringConfig> extractPortMirroringConfigFromCsar(ISdcCsarHelper csarHelper) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceNodeTemplateBySdcType(SdcTypes.CONFIGURATION);
        Map<String, PortMirroringConfig> configMaps = new HashMap<>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            PortMirroringConfig pmConfig = new PortMirroringConfig();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, pmConfig);

            pmConfig.setModelCustomizationName(nodeTemplate.getName());
            pmConfig.setRequirementAssignments(nodeTemplate.getRequirements());
            setSourceAndCollectorProxyNodes(csarHelper, pmConfig, nodeTemplate);

            configMaps.put(nodeTemplate.getName(), pmConfig);
        }

        return configMaps;
	}

    private Map<String, ServiceProxy> extractServiceProxyFromCsar(ISdcCsarHelper csarHelper) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceNodeTemplateBySdcType(SdcTypes.SERVICE_PROXY);
        Map<String, ServiceProxy> serviceProxies = new HashMap<>();
        for (NodeTemplate nodeTemplate: nodeTemplates) {
            ServiceProxy serviceProxy = new ServiceProxy();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, serviceProxy);

            Map<String, String> metadata = nodeTemplate.getMetaData().getAllProperties();
            serviceProxy.setSourceModelUuid(metadata.get("sourceModelUuid"));
            serviceProxy.setSourceModelInvariant(metadata.get("sourceModelInvariant"));
            serviceProxy.setSourceModelName(metadata.get("sourceModelName"));

            serviceProxies.put(nodeTemplate.getName(), serviceProxy);
        }

        return serviceProxies;
    }

	private void setSourceAndCollectorProxyNodes(ISdcCsarHelper csarHelper, PortMirroringConfig portMirroringConfig, NodeTemplate nodeTemplate) {
	    RequirementAssignments requirementAssignments = nodeTemplate.getRequirements();

        List<String> sourceNodes = getRequirementsNodesNames(requirementAssignments.getRequirementsByName("source").getAll());
        portMirroringConfig.setSourceNodes(sourceNodes);

        List<String> collectorNodes = getRequirementsNodesNames(requirementAssignments.getRequirementsByName("collector").getAll());
        if (!collectorNodes.isEmpty()) { // vprobe
            portMirroringConfig.setCollectorNodes(collectorNodes);
        } else { // pprobe - configuration by policy
            String collectorNodeName = csarHelper.getNodeTemplatePropertyLeafValue(nodeTemplate, "collector_node");
            if (collectorNodeName != null) {
                portMirroringConfig.setCollectorNodes(Arrays.asList(collectorNodeName));
                portMirroringConfig.setConfigurationByPolicy(true);
            }
        }
    }

    private List<String> getRequirementsNodesNames(List<RequirementAssignment> requirements) {

        List<String> requirementsNodes = new ArrayList<>();
        if (requirements != null && requirements.size() > 0) {
            requirementsNodes =  requirements.stream().map(RequirementAssignment::getNodeTemplateName).collect(Collectors.toList());
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

    private Map<String, Network> extractNetworksFromCsar(ISdcCsarHelper csarHelper) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceVlList();
        Map<String, Network> networksMap = new HashMap<String, Network>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            Network newNetwork = new Network();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, newNetwork);
            newNetwork.setModelCustomizationName(nodeTemplate.getName());
            networksMap.put(nodeTemplate.getName(), newNetwork);
        }
        return networksMap;
	}

	private Map<String,Node> extractPnfsFromCsar(ISdcCsarHelper csarHelper) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceNodeTemplateBySdcType(SdcTypes.PNF);
        HashMap<String, Node> pnfHashMap = new HashMap<>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            Node pnf = new Node();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, pnf);
            pnfHashMap.put(nodeTemplate.getName(), pnf);
        }
        return pnfHashMap;
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
            if(input.getAnnotations() == null)
                inputs.put(input.getName(), convertInput(input, new org.onap.vid.asdc.beans.tosca.Input(), null));
        }
        return inputs;
    }

    private Node populateNodeFromNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper csarHelper, Node newNode) {
        newNode.setCustomizationUuid(csarHelper.getNodeTemplateCustomizationUuid(nodeTemplate));
        newNode.setDescription(nodeTemplate.getMetaData().getValue(Constants.DESCRIPTION));
        newNode.setInvariantUuid(nodeTemplate.getMetaData().getValue(Constants.INVARIANT_UUID));
        newNode.setUuid(nodeTemplate.getMetaData().getValue(Constants.UUID));
        newNode.setName(nodeTemplate.getMetaData().getValue(Constants.NAME));
        newNode.setVersion(nodeTemplate.getMetaData().getValue(Constants.VERSION));
        newNode.setInputs(extractInputsAndCommandsForNodeTemplate(nodeTemplate, csarHelper, newNode));
        newNode.setType(nodeTemplate.getMetaData().getValue(Constants.TYPE));
        Map<String, String> propertiesMap = setPropertiesOfVnf(nodeTemplate.getPropertiesObjects());
        newNode.setProperties(propertiesMap);
        return newNode;
    }

    private VfModule populateVfModuleFromGroup(Group group, ISdcCsarHelper csarHelper){
        VfModule vfModule = new VfModule();
        extractBasicPropertiesForGroup(vfModule, group, csarHelper);
        vfModule.setVolumeGroupAllowed(isVolumeGroup(group));
        return vfModule;
    }

    private VolumeGroup populateVolumeGroupFromGroup(Group group, ISdcCsarHelper csarHelper){
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


    private Map<String,org.onap.vid.asdc.beans.tosca.Input> extractVfInputsFromCsarByAnnotation(ISdcCsarHelper csarHelper, org.onap.vid.model.Group group) {
        Map<String, org.onap.vid.asdc.beans.tosca.Input> inputMap = new HashMap<>();
        if(group.getProperties().getVfModuleLabel() != null){
            List<Input> inputsList = csarHelper.getInputsWithAnnotations();
            for(Input input: inputsList){
                if(input.getAnnotations() != null){
                    List<Property> annotationProperties = input.getAnnotations().get("source").getProperties();
                    if(isInputMatchesToGroup(annotationProperties, group)){
                        inputMap.put(input.getName(), new org.onap.vid.asdc.beans.tosca.Input(input ,annotationProperties));
                    }
                }
            }
        }
        return inputMap;
    }


    private boolean isInputMatchesToGroup(List<Property> annotationProperties, org.onap.vid.model.Group group){
        for(Property property: annotationProperties){
            if(property.getName().equals(VF_MODULE_LABEL)){
                return getPropertyValueAsString(property).equals(group.getProperties().getVfModuleLabel());
            }
        }
        return false;
    }

    public String getPropertyValueAsString(Property property) {
        return removeSquareBrackets(property.getValue().toString());
    }

    private String removeSquareBrackets(String stringWithSquareBrackets){
        return stringWithSquareBrackets.substring(1, stringWithSquareBrackets.length()-1);
    }

    private GroupProperties extractVfModuleProperties(Group group, ISdcCsarHelper csarHelper){
        GroupProperties vfModuleProperties = new GroupProperties();
        if(csarHelper.getGroupPropertyAsObject(group, Constants.MIN_VF_MODULE_INSTANCES) != null)
            vfModuleProperties.setMinCountInstances((Integer)csarHelper.getGroupPropertyAsObject(group, Constants.MIN_VF_MODULE_INSTANCES));
        if(csarHelper.getGroupPropertyAsObject(group, Constants.MAX_VF_MODULE_INSTANCES) != null)
            vfModuleProperties.setMaxCountInstances((Integer)csarHelper.getGroupPropertyAsObject(group, Constants.MAX_VF_MODULE_INSTANCES));
        if(csarHelper.getGroupPropertyAsObject(group, Constants.INITIAL_COUNT) != null)
            vfModuleProperties.setInitialCount((Integer)csarHelper.getGroupPropertyAsObject(group, Constants.INITIAL_COUNT));
        if(csarHelper.getGroupPropertyAsObject(group, VF_MODULE_LABEL) != null)
            vfModuleProperties.setVfModuleLabel((String) csarHelper.getGroupPropertyAsObject(group, VF_MODULE_LABEL));
        return vfModuleProperties;
    }




    private Map<String, org.onap.vid.asdc.beans.tosca.Input> extractInputsAndCommandsForNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper csarHelper, Node newNode){
        Map<String, org.onap.vid.asdc.beans.tosca.Input> inputMap = new HashMap<>();
        Map<String, CommandProperty> commandPropertyMap = new HashMap<>();

        List<Input> inputs = csarHelper.getServiceInputs();
        Map<String, String> properties  = csarHelper.filterNodeTemplatePropertiesByValue(nodeTemplate, FilterType.CONTAINS, Constants.GET_INPUT);
        for (Map.Entry<String, String> property : properties.entrySet()) {
            String inputKey = property.getValue();
            String key = extractInputValue(inputKey);
            for (Input input: inputs){
                if(input.getName().equals(key)){
                    org.onap.vid.asdc.beans.tosca.Input localInput = new org.onap.vid.asdc.beans.tosca.Input();
                    localInput = convertInput(input, localInput, nodeTemplate);
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
        return inputKey.substring(inputKey.indexOf(":") + 1);
    }

    private org.onap.vid.asdc.beans.tosca.Input convertInput(Input parserInput, org.onap.vid.asdc.beans.tosca.Input localInput, NodeTemplate nodeTemplate){
        localInput.setDefault(parserInput.getDefault());
        localInput.setDescription(parserInput.getDescription());
        localInput.setRequired(parserInput.isRequired());
        localInput.setType(parserInput.getType());
        localInput.setConstraints(parserInput.getConstraints());
//        localInput.setentry_schema()
        
        //if inputs of inner nodeTemplate - tell its details
        if(nodeTemplate != null) {
            Metadata metadata = nodeTemplate.getMetaData();
            localInput.setTemplateName(metadata.getValue("name"));
            localInput.setTemplateUUID(metadata.getValue("UUID"));
            localInput.setTemplateInvariantUUID(metadata.getValue("invariantUUID"));
            localInput.setTemplateCustomizationUUID(metadata.getValue("customizationUUID"));
        }
        
        return localInput;
    }

    private CommandProperty extractCommands(String displayName, String inputName){
        CommandProperty commandProperty = new CommandProperty();
        commandProperty.setDisplayName(displayName);
        commandProperty.setCommand(Constants.GET_INPUT);
        commandProperty.setInputName(inputName);
        return commandProperty;
    }

    private Map<String, String> setPropertiesOfVnf(List<Property> properties) {
        Map<String, String> propertiesMap = new HashMap<String, String>();
        for (Property property : properties) {
            //special handling to necessary sub-property "ecomp_generated_naming"
            if(property.getName().equals("nf_naming")){
                propertiesMap.put(removeSquareBrackets(((LinkedHashMap)(property.getValue())).keySet().toString()) ,((LinkedHashMap)(property.getValue())).get("ecomp_generated_naming").toString());
            }
            propertiesMap.put(property.getName(), property.getValue().toString());
        }
        return propertiesMap;
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


}