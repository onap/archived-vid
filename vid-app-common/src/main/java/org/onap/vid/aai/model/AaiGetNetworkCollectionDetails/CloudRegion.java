package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

import static java.util.Collections.emptyList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudRegion {

    private final String cloudOwner;
    private final String cloudRegionId;

    public CloudRegion(
            @JsonProperty("cloud-owner") String cloudOwner,
            @JsonProperty("cloud-region-id") String cloudRegionId
    ) {
        this.cloudOwner = cloudOwner;
        this.cloudRegionId = cloudRegionId;
    }

    public String getCloudOwner() {
        return cloudOwner;
    }

    public String getCloudRegionId() {
        return cloudRegionId;
    }

    /*
    This will handle container like:
        {
          "cloud-region": [{
              "cloud-owner": "alfi",
              "cloud-region-id": "foo",
              . . .
          }, {
              "cloud-owner": "alba",
              "cloud-region-id": "bar",
     */
    public static class Collection {
        private final List<CloudRegion> cloudRegions;

        public Collection(@JsonProperty("cloud-region") List<CloudRegion> cloudRegions) {
            this.cloudRegions = ObjectUtils.defaultIfNull(cloudRegions, emptyList());
        }

        public List<CloudRegion> getCloudRegions() {
            return cloudRegions;
        }
    }
}
