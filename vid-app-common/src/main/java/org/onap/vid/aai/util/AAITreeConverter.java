package org.onap.vid.aai.util;

import org.apache.commons.lang3.StringUtils;
import org.onap.vid.model.aaiTree.*;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.services.AAITreeNodeBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.onap.vid.asdc.parser.ToscaParserImpl2.Constants.A_LA_CARTE;

@Component
public class AAITreeConverter {

    public static final String VNF_TYPE = "vnf-type";
    public static final String NETWORK_TYPE = "network-type";

    public static final String IS_BASE_VF_MODULE = "is-base-vf-module";

    public enum ModelType {
        service,
        vnf,
        network,
        instanceGroup,
        vfModule
    }

    public ServiceInstance convertTreeToUIModel(AAITreeNode rootNode, String globalCustomerId, String serviceType, String instantiationType) {
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setInstanceId(rootNode.getId());
        serviceInstance.setInstanceName(rootNode.getName());
        serviceInstance.setOrchStatus(rootNode.getOrchestrationStatus());
        serviceInstance.setGlobalSubscriberId(globalCustomerId);
        serviceInstance.setSubscriptionServiceType(serviceType);
        serviceInstance.setIsALaCarte(StringUtils.equals(instantiationType, A_LA_CARTE));

        serviceInstance.setModelInfo(createModelInfo(rootNode, ModelType.service));

        //set children: vnf, network,group
        rootNode.getChildren().forEach(child -> {
            if (child.getType().equals(AAITreeNodeBuilder.GENERIC_VNF)) {
                serviceInstance.getVnfs().put(child.getUniqueNodeKey(), Vnf.from(child));
            } else if (child.getType().equals(AAITreeNodeBuilder.NETWORK)) {
                serviceInstance.getNetworks().put(child.getUniqueNodeKey(), Network.from(child));
            } else if (child.getType().equals(AAITreeNodeBuilder.INSTANCE_GROUP)) {
                serviceInstance.getVnfGroups().put(child.getUniqueNodeKey(), VnfGroup.from(child));
            }
        });

        serviceInstance.setExistingVNFCounterMap(
                serviceInstance.getVnfs().entrySet().stream()
                        .map(k -> k.getValue().getModelInfo().getModelVersionId())
                        .collect(groupingBy(identity(), counting()))
        );

        serviceInstance.setExistingNetworksCounterMap(
                serviceInstance.getNetworks().entrySet().stream()
                .map(k -> k.getValue().getModelInfo().getModelVersionId())
                .filter(Objects::nonNull)
                        .collect(groupingBy(identity(), counting()))
        );


        serviceInstance.setExistingVnfGroupCounterMap(
                serviceInstance.getVnfGroups().entrySet().stream()
                .map(k -> k.getValue().getModelInfo().getModelVersionId())
                .filter(Objects::nonNull)
                        .collect(groupingBy(identity(), counting()))
        );

        return serviceInstance;
    }

    private static ModelInfo createModelInfo(AAITreeNode aaiNode, ModelType modelType) {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType(modelType.name());
        modelInfo.setModelName(aaiNode.getModelName());
        modelInfo.setModelVersion(aaiNode.getModelVersion());
        modelInfo.setModelVersionId(aaiNode.getModelVersionId());
        modelInfo.setModelInvariantId(aaiNode.getModelInvariantId());
        modelInfo.setModelCustomizationId(aaiNode.getModelCustomizationId());

        return modelInfo;
    }
}
