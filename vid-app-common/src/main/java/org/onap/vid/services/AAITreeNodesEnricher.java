/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2020 AT&T Intellectual Property. All rights reserved.
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

import static java.util.stream.Collectors.toSet;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.parser.ServiceModelInflator;
import org.onap.vid.asdc.parser.ServiceModelInflator.Names;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.properties.Features;
import org.springframework.stereotype.Component;
import org.togglz.core.manager.FeatureManager;

@Component
public class AAITreeNodesEnricher {

    private final AaiClientInterface aaiClient;

    private final VidService sdcService;

    private final ServiceModelInflator serviceModelInflator;

    private final FeatureManager featureManager;

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AAITreeNodesEnricher.class);

    @Inject
    public AAITreeNodesEnricher(
        AaiClientInterface aaiClient,
        VidService sdcService,
        FeatureManager featureManager,
        ServiceModelInflator serviceModelInflator
    ) {
        this.aaiClient = aaiClient;
        this.sdcService = sdcService;
        this.featureManager = featureManager;
        this.serviceModelInflator = serviceModelInflator;
    }

    public ServiceModel getServiceModel(String modelVersionId) {
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

    void enrichNodesWithModelCustomizationName(Collection<AAITreeNode> nodes, ServiceModel serviceModel) {
        final Map<String, Names> customizationNameByVersionId = serviceModelInflator.toNamesByVersionId(serviceModel);

        nodes.forEach(node -> {
            final Names names = customizationNameByVersionId.get(node.getModelVersionId());
            if (names != null) {
                node.setKeyInModel(names.getModelKey());
                node.setModelCustomizationName(names.getModelCustomizationName());
            }
        });
    }

    void enrichVfModulesWithModelCustomizationNameFromOtherVersions(Collection<AAITreeNode> nodes, String modelInvariantId) {
        if (!featureManager.isActive(Features.FLAG_EXP_TOPOLOGY_TREE_VFMODULE_NAMES_FROM_OTHER_TOSCA_VERSIONS)) {
            return;
        }

        return;
    }

    public void enrichNodesWithModelVersionAndModelName(Collection<AAITreeNode> nodes) {

        Collection<String> invariantIDs = getModelInvariantIds(nodes);

        Map<String, String> modelVersionByModelVersionId = new HashMap<>();
        Map<String, String> modelNameByModelVersionId = new HashMap<>();

        JsonNode models = getModels(aaiClient, invariantIDs);
        if (models!=null) {
            for (JsonNode model : models) {
                JsonNode modelVersions = model.get("model-vers").get("model-ver");
                for (JsonNode modelVersion : modelVersions) {
                    final String modelVersionId = modelVersion.get("model-version-id").asText();
                    modelVersionByModelVersionId.put(modelVersionId, modelVersion.get("model-version").asText());
                    modelNameByModelVersionId.put(modelVersionId, modelVersion.get("model-name").asText());
                }
            }
        }

        nodes.forEach(node -> {
            node.setModelVersion(modelVersionByModelVersionId.get(node.getModelVersionId()));
            node.setModelName(modelNameByModelVersionId.get(node.getModelVersionId()));
        });

    }

    private JsonNode getModels(AaiClientInterface aaiClient, Collection<String> invariantIDs) {
        Response response = aaiClient.getVersionByInvariantId(ImmutableList.copyOf(invariantIDs));
        try {
            JsonNode responseJson = JACKSON_OBJECT_MAPPER.readTree(response.readEntity(String.class));
            return responseJson.get("model");
        } catch (Exception e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to getVersionByInvariantId from A&AI", e);
        }
        return JACKSON_OBJECT_MAPPER.createObjectNode();
    }

    private Set<String> getModelInvariantIds(Collection<AAITreeNode> nodes) {
        return nodes.stream()
                .map(AAITreeNode::getModelInvariantId)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

}
