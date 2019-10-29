package org.onap.simulator.presetGenerator.presets.aai;

public class Placement {
    public final String cloudOwner;
    public final String lcpRegionId;
    public final String tenantId;

    public Placement(String cloudOwner, String lcpRegionId, String tenantId) {
        this.cloudOwner = cloudOwner;
        this.lcpRegionId = lcpRegionId;
        this.tenantId = tenantId;
    }

    public static class Util {
        static String placementRelationship(String relatedTo, Placement placement) {
            return "" +
                "{" +
                "    \"related-to\": \"" + relatedTo + "\"," +
                "    \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "    \"related-link\": " + relatedLink(placement) + "," +
                "    \"relationship-data\": [" +
                "        {" +
                "            \"relationship-key\": \"cloud-region.cloud-owner\"," +
                "            \"relationship-value\": \"" + placement.cloudOwner + "\"" +
                "        }," +
                "        {" +
                "            \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                "            \"relationship-value\": \"" + placement.lcpRegionId + "\"" +
                "        }," +
                "        {" +
                "            \"relationship-key\": \"tenant.tenant-id\"," +
                "            \"relationship-value\": \"" + placement.tenantId + "\"" +
                "        }," +
                "        {" +
                "            \"relationship-key\": \"vserver.vserver-id\"," +
                "            \"relationship-value\": \"5eef9f6d-9933-4bc6-9a1a-862d61309437\"" +
                "        }" +
                "    ]," +
                "    \"related-to-property\": [" +
                "        {" +
                "            \"property-key\": \"vserver.vserver-name\"," +
                "            \"property-value\": \"zolson5bfapn01dns002\"" +
                "        }" +
                "    ]" +
                "}";
        }

        private static String relatedLink(Placement placement) {
            return ""
                + "\""
                + "/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/"
                + placement.lcpRegionId
                + "/tenants/tenant/"
                + placement.tenantId
                + "/vservers/vserver/5eef9f6d-9933-4bc6-9a1a-862d61309437"
                + "\"";
        }
    }
}

