package org.onap.simulator.presetGenerator.presets.aai;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIGetCloudOwnersByCloudRegionId extends BaseAAIPreset {
    public static final String SOME_LEGACY_REGION = "some legacy region";
    public static final String JUST_ANOTHER_REGION = "just another region";
    public static final String MY_REGION = "my region";
    public static final String LCP_REGION_TEXT = "lcpRegionText";
    public static final String ATT_AIC = "irma-aic";
    public static final String MDT_1 = "mdt1";
    public static final String ATT_NC = "att-nc";
    public static final String hvf3 = "hvf3";
    public static final String olson3 = "olson3";
    public static final String olson5B = "olson5b";
    public static final String ATT_SABABA = "att-sababa";
    public static final String AAIAIC_25 = "JANET25";
    public static final String ONE = "One";
    public static final String hvf6 = "hvf6";
    private final String cloudRegionId;
    private final String cloudOwner;

    //Since there is a cache of cloudRegionToCloudOwner, we can't have 2 preset of same cloud region that return different cloud owner.
    //So all the preset instance must be declared here, for conflicts prevention.
    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_SOME_LEGACY_REGION_TO_ATT_AIC =
            new PresetAAIGetCloudOwnersByCloudRegionId(SOME_LEGACY_REGION, ATT_AIC);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_JUST_ANOTHER_REGION_TO_ATT_AIC =
            new PresetAAIGetCloudOwnersByCloudRegionId(JUST_ANOTHER_REGION, ATT_AIC);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_MDT1_TO_ATT_NC =
            new PresetAAIGetCloudOwnersByCloudRegionId(MDT_1, ATT_NC);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_RDM3_TO_ATT_NC =
            new PresetAAIGetCloudOwnersByCloudRegionId(olson3, ATT_NC);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_MTN3_TO_ATT_SABABA =
            new PresetAAIGetCloudOwnersByCloudRegionId(hvf3, ATT_SABABA);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_AAIAIC25_TO_ATT_AIC =
            new PresetAAIGetCloudOwnersByCloudRegionId(AAIAIC_25, ATT_AIC);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_ONE_TO_ATT_AIC =
            new PresetAAIGetCloudOwnersByCloudRegionId(ONE, ATT_AIC);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_MTN6_TO_ATT_AIC =
            new PresetAAIGetCloudOwnersByCloudRegionId(hvf6, ATT_AIC);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_MY_REGION_TO_ATT_AIC =
            new PresetAAIGetCloudOwnersByCloudRegionId(MY_REGION, ATT_AIC);

    public static final PresetAAIGetCloudOwnersByCloudRegionId PRESET_LCP_REGION_TEXT_TO_ATT_AIC =
            new PresetAAIGetCloudOwnersByCloudRegionId(LCP_REGION_TEXT, ATT_AIC);

    private PresetAAIGetCloudOwnersByCloudRegionId(String cloudRegionId, String cloudOwnerResult) {
        this.cloudRegionId = cloudRegionId;
        this.cloudOwner = cloudOwnerResult;
    }

    @Override
    public Object getResponseBody() {
        return "" +
                "{" +
                "  \"cloud-region\": [{" +
                "      \"cloud-owner\": \"" + cloudOwner + "\"," +
                "      \"cloud-region-id\": \"" + cloudRegionId + "\"," +
                "      \"cloud-region-version\": \"2.5\"," +
                "      \"identity-url\": \"http://" + randomAlphabetic(5) + ":5000/v2.0\"," +
                "      \"complex-name\": \"" + cloudRegionId + "\"," +
                "      \"resource-version\": \"" + randomNumeric(5) + "\"," +
                "      \"relationship-list\": {" +
                "        \"relationship\": [{" +
                "            \"related-to\": \"pserver\"," +
                "            \"relationship-label\": \"org.onap.relationships.inventory.LocatedIn\"," +
                "            \"related-link\": \"/aai/v12/cloud-infrastructure/pservers/pserver/" + randomAlphabetic(5) + "\"," +
                "            \"relationship-data\": [{" +
                "                \"relationship-key\": \"pserver.hostname\"," +
                "                \"relationship-value\": \"" + randomAlphabetic(5) + "\"" +
                "              }" +
                "            ]," +
                "            \"related-to-property\": [{" +
                "                \"property-key\": \"pserver.pserver-name2\"" +
                "              }" +
                "            ]" +
                "          }, {" +
                "            \"related-to\": \"l3-network\"," +
                "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "            \"related-link\": \"/aai/v12/network/l3-networks/l3-network/" + UUID.randomUUID() + "\"," +
                "            \"relationship-data\": [{" +
                "                \"relationship-key\": \"l3-network.network-id\"," +
                "                \"relationship-value\": \"" + UUID.randomUUID() + "\"" +
                "              }" +
                "            ]," +
                "            \"related-to-property\": [{" +
                "                \"property-key\": \"l3-network.network-name\"," +
                "                \"property-value\": \"" + randomAlphabetic(5) + "\"" +
                "              }" +
                "            ]" +
                "          }" +
                "        ]" +
                "      }" +
                "    }" +
                "  ]" +
                "}";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/cloud-infrastructure/cloud-regions";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("cloud-region-id", Collections.singletonList(cloudRegionId));
    }
}
