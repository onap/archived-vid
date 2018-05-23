package org.onap.vid.asdc.parser;

import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.*;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.sdc.tosca.parser.impl.FilterType;
import org.onap.sdc.tosca.parser.impl.SdcToscaParserFactory;
import org.onap.sdc.tosca.parser.impl.SdcTypes;
import org.onap.sdc.toscaparser.api.*;
import org.onap.sdc.toscaparser.api.elements.Metadata;
import org.onap.sdc.toscaparser.api.parameters.Input;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class ToscaParserImpl2 {

    
	public class Constants {
		public final static String uuid = "UUID";
		public final static String description = "description";
		public final static String serviceType = "serviceType";
        public final static String serviceRole = "serviceRole";
        public final static String ecompGeneratedNaming = "ecompGeneratedNaming";

        public final static String customizationUUID = "customizationUUID";
		public final static String vfModuleModelVersion = "vfModuleModelVersion";
		public final static String vfModuleModelCustomizationUUID = "vfModuleModelCustomizationUUID";
		public final static String volume_group = "volume_group";
		public final static String vfModuleModelInvariantUUID = "vfModuleModelInvariantUUID";
		public final static String vfModuleModelUUID = "vfModuleModelUUID";
		public final static String invariantUUID = "invariantUUID";
		public final static String version = "version";
		public final static String name = "name";
		public final static String category = "category";
		public final static String vfModuleModelName = "vfModuleModelName";
		public final static String getInput = "get_input";

		public final static String instantiationType = "instantiationType";
        //Instantiation Types
        public final static String both = "Both";
        public final static String macro = "Macro";
        public final static String aLaCarte = "A-La-Carte";
        public final static String clientConfig = "ClientConfig";


	}
	
	public ToscaParserImpl2() {

    }

    public ServiceModel  makeServiceModel(Path path, Service asdcServiceMetadata) throws Exception {
        ServiceModel serviceModel = new ServiceModel();
        SdcToscaParserFactory factory = SdcToscaParserFactory.getInstance();
        ISdcCsarHelper sdcCsarHelper = factory.getSdcCsarHelper(path.toFile().getAbsolutePath(),false);
        serviceModel.setService(extractServiceFromCsar(asdcServiceMetadata, sdcCsarHelper));

        serviceModel.setVolumeGroups(extractVolumeGroups(sdcCsarHelper));
        serviceModel.setVfModules(extractVfModuleFromCsar(sdcCsarHelper));
        serviceModel.setVnfs(extractVnfsFromCsar(serviceModel,sdcCsarHelper));
        serviceModel.setConfigurations(extractPortMirroringConfigFromCsar(sdcCsarHelper));
        serviceModel.setServiceProxies(extractServiceProxyFromCsar(sdcCsarHelper));
        serviceModel.setNetworks(extractNetworksFromCsar(sdcCsarHelper));
        serviceModel.setPnfs(extractPnfsFromCsar(sdcCsarHelper));
        return serviceModel;
    }

    private  org.onap.vid.model.Service extractServiceFromCsar(Service asdcServiceMetadata, ISdcCsarHelper csarHelper) throws SdcToscaParserException {
        org.onap.vid.model.Service service = new  org.onap.vid.model.Service();

        service.setName(csarHelper.getServiceMetadata().getValue(Constants.name));
        service.setCategory(csarHelper.getServiceMetadata().getValue(Constants.category));
        service.setInvariantUuid(csarHelper.getServiceMetadata().getValue(Constants.invariantUUID));
        service.setUuid(csarHelper.getServiceMetadata().getValue(Constants.uuid));
        service.setVersion(asdcServiceMetadata.getVersion());
        service.setDescription(csarHelper.getServiceMetadata().getValue(Constants.description));
        service.setInputs(inputsListToInputsMap(csarHelper.getServiceInputs()));
        service.setServiceEcompNaming(csarHelper.getServiceMetadata().getValue(Constants.ecompGeneratedNaming));
        service.setServiceType(csarHelper.getServiceMetadata().getValue(Constants.serviceType));
        service.setServiceRole(csarHelper.getServiceMetadata().getValue(Constants.serviceRole));
        service.setInstantiationType(validateInstantiationType(csarHelper));
        return service;
    }

    
    private Map<String, VNF> extractVnfsFromCsar(ServiceModel serviceModel,ISdcCsarHelper csarHelper) {
        List<NodeTemplate> nodeTemplates = csarHelper.getServiceVfList();
        Map<String, VNF> vnfsMaps = new HashMap<String, VNF>();

        for (NodeTemplate nodeTemplate : nodeTemplates) {
            VNF vnf = new VNF();
            populateNodeFromNodeTemplate(nodeTemplate, csarHelper, vnf);

            vnf.setModelCustomizationName(nodeTemplate.getName());
            Map<String, VfModule> vfModuleHashMap = getVfModulesFromVF(csarHelper, vnf.getCustomizationUuid());
            vnf.setVfModules(vfModuleHashMap);

            Map<String, VolumeGroup> volumeGroupMap = getVolumeGroupsFromVF(csarHelper, vnf.getCustomizationUuid());
            vnf.setVolumeGroups(volumeGroupMap);

            vnfsMaps.put(nodeTemplate.getName(), vnf);
        }
        return vnfsMaps;
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
                .collect(toMap(Group::getName, this::populateVfModuleFromGroup));
    }

    Map<String, VolumeGroup> getVolumeGroupsFromVF(ISdcCsarHelper csarHelper, String vfCustomizationUuid) {
        List<Group> vfModulesByVf = csarHelper.getVfModulesByVf(vfCustomizationUuid);
        return vfModulesByVf.stream()
                .filter((group -> isVolumeGroup(group)))
                .collect(toMap(Group::getName, this::populateVolumeGroupFromGroup));
    }

    private static Boolean isVolumeGroup(Group group) {
        return Boolean.valueOf(group.getPropertyValue(Constants.volume_group).toString());
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
                    getVfModulesFromVF(csarHelper, nodeTemplate.getMetaData().getValue(Constants.customizationUUID));
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
            inputs.put(input.getName(), convertInput(input, new org.onap.vid.asdc.beans.tosca.Input(), null));
        }
        return inputs;
    }

    private Node populateNodeFromNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper csarHelper, Node newNode) {
        newNode.setCustomizationUuid(csarHelper.getNodeTemplateCustomizationUuid(nodeTemplate));
        newNode.setDescription(nodeTemplate.getMetaData().getValue(Constants.description));
        newNode.setInvariantUuid(nodeTemplate.getMetaData().getValue(Constants.invariantUUID));
        newNode.setUuid(nodeTemplate.getMetaData().getValue(Constants.uuid));
        newNode.setName(nodeTemplate.getMetaData().getValue(Constants.name));
        newNode.setVersion(nodeTemplate.getMetaData().getValue(Constants.version));
        newNode.setInputs(extractInputsAndCommandsForNodeTemplate(nodeTemplate, csarHelper, newNode));
        newNode.setType(nodeTemplate.getMetaData().getValue("type"));
        Map<String, String> propertiesMap = setPropertiesOfVnf(nodeTemplate.getPropertiesObjects());
        newNode.setProperties(propertiesMap);
        return newNode;
    }

    private VfModule populateVfModuleFromGroup(Group group){
        VfModule vfModule = new VfModule();

        vfModule.setVersion(group.getMetadata().getValue(Constants.vfModuleModelVersion));
        vfModule.setCustomizationUuid(group.getMetadata().getValue(Constants.vfModuleModelCustomizationUUID));
        vfModule.setModelCustomizationName(group.getMetadata().getValue(Constants.vfModuleModelName));
        vfModule.setName(group.getMetadata().getValue(Constants.vfModuleModelName));
        vfModule.setVolumeGroupAllowed(isVolumeGroup(group));
        vfModule.setDescription(group.getDescription());
        vfModule.setInvariantUuid(group.getMetadata().getValue(Constants.vfModuleModelInvariantUUID));
        vfModule.setUuid(group.getMetadata().getValue(Constants.vfModuleModelUUID));
        vfModule.setProperties(group.getProperties());
        return vfModule;
    }

    private VolumeGroup populateVolumeGroupFromGroup(Group group){
        VolumeGroup volumeGroup = new VolumeGroup();
        volumeGroup.setDescription(group.getDescription());
        volumeGroup.setInvariantUuid(group.getMetadata().getValue(Constants.vfModuleModelInvariantUUID));
        volumeGroup.setName(group.getMetadata().getValue(Constants.vfModuleModelName));
        volumeGroup.setModelCustomizationName(group.getMetadata().getValue(Constants.vfModuleModelName));
        volumeGroup.setVersion(group.getMetadata().getValue(Constants.vfModuleModelVersion));
        volumeGroup.setUuid(group.getMetadata().getValue(Constants.vfModuleModelUUID));
        volumeGroup.setCustomizationUuid(group.getMetadata().getValue(Constants.vfModuleModelCustomizationUUID));

        return volumeGroup;
    }


    private Map<String, org.onap.vid.asdc.beans.tosca.Input> extractInputsAndCommandsForNodeTemplate(NodeTemplate nodeTemplate, ISdcCsarHelper csarHelper, Node newNode){
        Map<String, org.onap.vid.asdc.beans.tosca.Input> inputMap = new HashMap<>();
        Map<String, CommandProperty> commandPropertyMap = new HashMap<>();

        List<Input> inputs = csarHelper.getServiceInputs();
        Map<String, String> properties  = csarHelper.filterNodeTemplatePropertiesByValue(nodeTemplate, FilterType.CONTAINS, Constants.getInput);
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
        commandProperty.setCommand(Constants.getInput);
        commandProperty.setInputName(inputName);
        return commandProperty;
    }

    private Map<String, String> setPropertiesOfVnf(List<Property> properties) {
        Map<String, String> propertiesMap = new HashMap<String, String>();
        for (Property property : properties) {
            propertiesMap.put(property.getName(), property.getValue().toString());
        }
        return propertiesMap;
    }

    private String validateInstantiationType(ISdcCsarHelper csarHelper){
        String instantiationType = csarHelper.getServiceMetadata().getValue(Constants.instantiationType);
        String validatedInstantiationType = Constants.clientConfig;
        if(instantiationType != null && !instantiationType.isEmpty()){
            if(instantiationType.equalsIgnoreCase(Constants.macro) || instantiationType.equalsIgnoreCase(Constants.both))
                validatedInstantiationType = Constants.macro;
            else if(instantiationType.equalsIgnoreCase(Constants.aLaCarte))
                validatedInstantiationType = Constants.aLaCarte;
        }
        return validatedInstantiationType;
    }


}