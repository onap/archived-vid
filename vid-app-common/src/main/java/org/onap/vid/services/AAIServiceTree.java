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

package org.onap.vid.services;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.util.AAITreeConverter;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.model.aaiTree.ServiceInstance;
import org.onap.vid.utils.Tree;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class AAIServiceTree {

    private final AAITreeNodeBuilder aaiTreeNodeBuilder;
    private final AAITreeNodesEnricher aaiTreeNodesEnricher;
    private final AAITreeConverter aaiTreeConverter;
    private final VidService sdcService;
    private final ExecutorService executorService;

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AAIServiceTree.class);

    public static final Tree<AaiRelationship> AAI_TREE_PATHS =
            new Tree<>(new AaiRelationship(NodeType.SERVICE_INSTANCE));

    static {
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.VOLUME_GROUP));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.VF_MODULE));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.NETWORK, NodeType.VPN_BINDING));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.NETWORK, NodeType.VPN_BINDING));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.INSTANCE_GROUP, NodeType.GENERIC_VNF));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.COLLECTION_RESOURCE, NodeType.INSTANCE_GROUP));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.CONFIGURATION, NodeType.NETWORK, NodeType.VPN_BINDING));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.CONFIGURATION, NodeType.VPN_BINDING));
    }

    public static List<AAIServiceTree.AaiRelationship> toAaiRelationshipList(NodeType... types) {
        return Stream.of(types).map(AAIServiceTree.AaiRelationship::new).collect(Collectors.toList());
    }

    @Inject
    public AAIServiceTree(
        AAITreeNodeBuilder aaiTreeNodeBuilder,
        AAITreeNodesEnricher aaiTreeNodesEnricher,
        AAITreeConverter aaiTreeConverter,
        VidService sdcService,
        ExecutorService executorService
    ) {
        this.aaiTreeNodeBuilder = aaiTreeNodeBuilder;
        this.aaiTreeNodesEnricher = aaiTreeNodesEnricher;
        this.aaiTreeConverter = aaiTreeConverter;
        this.sdcService = sdcService;
        this.executorService = executorService;
    }

    List<AAITreeNode> buildAAITreeForUniqueResource(String getUrl, NodeType nodeType) {
        return buildAAITreeForUniqueResourceFromCustomQuery(getUrl, null, HttpMethod.GET, nodeType);
    }

    List<AAITreeNode> buildAAITreeForUniqueResourceFromCustomQuery(String url, String payload, HttpMethod method, NodeType nodeType) {
        Tree<AAIServiceTree.AaiRelationship> pathsToSearch = new Tree<>(new AAIServiceTree.AaiRelationship(nodeType));
        return buildAAITree(url, payload, method, pathsToSearch, false);
    }

    public List<AAITreeNode> buildAAITree(String url, String payload, HttpMethod method, Tree<AaiRelationship> pathsToSearch, boolean enrichWithModelVersion) {

        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = createNodesAccumulator();

        List<AAITreeNode> aaiTreeNodes = fetchAAITree(url, payload, method, pathsToSearch, nodesAccumulator);

        if (enrichWithModelVersion) {
            aaiTreeNodesEnricher.enrichNodesWithModelVersionAndModelName(nodesAccumulator);
        }

        return aaiTreeNodes;
    }

    public ServiceInstance getServiceInstanceTopology(String globalCustomerId, String serviceType, String serviceInstanceId) {

        String getURL = "business/customers/customer/" +
                globalCustomerId + "/service-subscriptions/service-subscription/" +
                serviceType + "/service-instances/service-instance/" + serviceInstanceId;

        //Used later to get the nodes UUID
        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = createNodesAccumulator();

        AAITreeNode aaiTree = fetchAAITree(getURL, null, HttpMethod.GET, AAI_TREE_PATHS, nodesAccumulator).get(0);

        //Populate nodes with model-name & model-version (from aai)
        aaiTreeNodesEnricher.enrichNodesWithModelVersionAndModelName(nodesAccumulator);

        final ServiceModel serviceModel = getServiceModel(aaiTree.getModelVersionId());

        //Populate nodes with model-customization-name (from sdc model)
        aaiTreeNodesEnricher.enrichNodesWithModelCustomizationName(nodesAccumulator, serviceModel);

        return aaiTreeConverter.convertTreeToUIModel(aaiTree, globalCustomerId, serviceType, getInstantiationType(serviceModel), getInstanceRole(serviceModel), getInstanceType(serviceModel));
    }

    private String getInstanceType(ServiceModel serviceModel){
        if (serviceModel != null && serviceModel.getService() != null) {
            return serviceModel.getService().getServiceType();
        }
        return "";
    }

    private String getInstanceRole(ServiceModel serviceModel) {
        if (serviceModel != null && serviceModel.getService() != null) {
            return serviceModel.getService().getServiceRole();
        }
        return "";
    }

    private List<AAITreeNode> fetchAAITree(String url, String payload, HttpMethod method, Tree<AaiRelationship> pathsToSearch,
                                           ConcurrentSkipListSet<AAITreeNode> nodesAccumulator) {
        return aaiTreeNodeBuilder.buildNode(NodeType.fromString(pathsToSearch.getRootValue().type),
                url, payload, method, defaultIfNull(nodesAccumulator, createNodesAccumulator()),
                executorService, pathsToSearch);
    }

    private ConcurrentSkipListSet<AAITreeNode> createNodesAccumulator() {
        return new ConcurrentSkipListSet<>(comparing(AAITreeNode::getUniqueNodeKey));
    }

    private String getInstantiationType(ServiceModel serviceModel) {
        if (serviceModel.getService() != null && serviceModel.getService().getInstantiationType() != null) {
            return serviceModel.getService().getInstantiationType();
        } else {
            return null;
        }
    }

    private ServiceModel getServiceModel(String modelVersionId) {
        try {
            final ServiceModel serviceModel = sdcService.getService(modelVersionId);
            if (serviceModel == null) {
                throw new GenericUncheckedException("Model version '" + modelVersionId + "' not found");
            }
            return serviceModel;
        } catch (AsdcCatalogException e) {
            throw new GenericUncheckedException("Exception while loading model version '" + modelVersionId + "'", e);
        }
    }

    public static class AaiRelationship {

        public final String type;

        public AaiRelationship(String type) {
            this.type = type;
        }

        public AaiRelationship(NodeType nodeType) {
            this.type = nodeType.getType();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AaiRelationship)) return false;
            AaiRelationship that = (AaiRelationship) o;
            return Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }
}
