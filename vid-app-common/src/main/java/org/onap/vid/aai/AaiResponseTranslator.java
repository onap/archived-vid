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

package org.onap.vid.aai;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AaiResponseTranslator {

    public PortMirroringConfigData extractPortMirroringConfigData(AaiResponse<JsonNode> aaiResponse) {
        return extractErrorResponseIfHttpError(aaiResponse).orElseGet(() -> extractPortMirroringConfigData(aaiResponse.getT()));
    }

    public PortMirroringConfigData extractPortMirroringConfigData(JsonNode cloudRegionAndSourceFromConfigurationResponse) {
        final JsonNode payload = cloudRegionAndSourceFromConfigurationResponse;
        if (payload == null) {
            return new PortMirroringConfigDataError("Response payload is null", null);
        }

        final JsonNode results = payload.path("results");
        if (results.isMissingNode()) {
            return new PortMirroringConfigDataError("Root node 'results' is missing", payload.toString());
        }

        for (JsonNode resultNode : results) {
            final JsonNode nodeType = resultNode.path("node-type");
            if (nodeType.isTextual() && "cloud-region".equals(nodeType.textValue())) {
                return getPortMirroringConfigData(payload, resultNode);
            }
        }
        return new PortMirroringConfigDataError("Root node 'results' has no node where 'node-TYPE' is 'cloud-region'", payload.toString());
    }

    private PortMirroringConfigData getPortMirroringConfigData(JsonNode payload, JsonNode resultNode) {
        final JsonNode properties = resultNode.path("properties");
        if (properties.isMissingNode()) {
                    final String message = "The node-type 'cloud-region' does not contain a 'properties' node";
            return new PortMirroringConfigDataError(message, payload.toString());
        }

        final JsonNode cloudRegionIdNode = properties.path("cloud-region-id");
        if (cloudRegionIdNode.isMissingNode()) {
                    return new PortMirroringConfigDataError("The node-type 'cloud-region' does not contain the property 'cloud-region-id'", payload.toString());
        }
        if (!cloudRegionIdNode.isTextual()) {
                    return new PortMirroringConfigDataError("The node-type 'cloud-region' contains a non-textual value for the property 'cloud-region-id'", payload.toString());
        }

        final String cloudRegionId = cloudRegionIdNode.asText();
        if (StringUtils.isBlank(cloudRegionId)) {
                    return new PortMirroringConfigDataError("Node 'properties.cloud-region-id' of node-type 'cloud-region' is blank", payload.toString());
        }

        return new PortMirroringConfigDataOk(cloudRegionId);
    }

    private Optional<PortMirroringConfigData> extractErrorResponseIfHttpError(AaiResponse aaiResponse) {
        if (aaiResponse.getHttpCode() != org.springframework.http.HttpStatus.OK.value()) {
            final String errorMessage = aaiResponse.getErrorMessage();
            return Optional.of(new PortMirroringConfigDataError(
                    "Got " + aaiResponse.getHttpCode() + " from aai",
                    errorMessage != null ? errorMessage : null)
            );
        } else {
            return Optional.empty();
        }
    }

    public abstract static class PortMirroringConfigData {
    }

    public static class PortMirroringConfigDataOk extends PortMirroringConfigData {
        private final String cloudRegionId;

        public PortMirroringConfigDataOk(String cloudRegionId) {
            this.cloudRegionId = cloudRegionId;
        }

        public String getCloudRegionId() {
            return cloudRegionId;
        }
    }

    public static class PortMirroringConfigDataError extends PortMirroringConfigData {
        private final String errorDescription;
        private final String rawAaiResponse;

        public PortMirroringConfigDataError(String errorDescription, String rawAaiResponse) {
            this.errorDescription = errorDescription;
            this.rawAaiResponse = rawAaiResponse;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public String getRawAaiResponse() {
            return rawAaiResponse;
        }
    }
}
