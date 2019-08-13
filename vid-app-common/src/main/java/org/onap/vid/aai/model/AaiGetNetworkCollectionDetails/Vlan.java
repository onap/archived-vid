/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.interfaces.AaiModelWithRelationships;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vlan implements AaiModelWithRelationships {
	
     @JsonProperty("vlan-interface")
	 private final String vlanInterface;

	 @JsonProperty("vlan-id-inner")
	 private final String vlanIdInner;

	 @JsonProperty("relationship-list")
	 public final RelationshipList relationshipList;
	
    public Vlan(
            @JsonProperty("vlan-interface") String vlanInterface,
            @JsonProperty("vlan-id-inner") String vlanIdInner,
            @JsonProperty("relationship-list") RelationshipList relationshipList) {
        this.vlanInterface = vlanInterface;
        this.vlanIdInner = vlanIdInner;
        this.relationshipList = relationshipList;
    }

    

    public String getVlanInterface() {
        return vlanInterface;
    }

    public String getVlanIdInner() {
        return vlanIdInner;
    }

    @Override
    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

}
