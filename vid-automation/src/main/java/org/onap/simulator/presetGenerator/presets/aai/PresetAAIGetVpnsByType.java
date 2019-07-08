package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIGetVpnsByType extends BaseAAIPreset {

    private String vpnType;

    public PresetAAIGetVpnsByType() {
        this.vpnType = "SERVICE-INFRASTRUCTURE";
    }

    public PresetAAIGetVpnsByType(String vpnType) {
        this.vpnType = vpnType;
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("vpn-type",  Collections.singletonList(vpnType));
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "    \"vpn-binding\": [" +
                "        {" +
                "            \"vpn-id\": \"120d39fb-3627-473d-913c-d228dd0f8e5b\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1260\"," +
                "            \"resource-version\": \"1551904539767\"," +
                "            \"route-targets\" : [" +
                "            {" +
                "              \"global-route-target\":\"mock-global-1\"," +
                "              \"route-target-role\" : \"mock-role-x\"" +
                "            }," +
                "            {" +
                "              \"global-route-target\":\"mock-global-2\"," +
                "              \"route-target-role\" : \"mock-role-y\"" +
                "            }" +
                "            ],"+
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"configuration\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/network/configurations/configuration/f1e81ceb-ce90-4d54-b181-e1ce8552250e\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"configuration.configuration-id\"," +
                "                                \"relationship-value\": \"f1e81ceb-ce90-4d54-b181-e1ce8552250e\"" +
                "                            }" +
                "                        ]" +
                "                    }," +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"c70391f3-a6e3-4874-9834-cbe12d7bf8b6\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1274\"," +
                "            \"resource-version\": \"1552507588857\"," +
                "            \"model-customization-id\" : \"cc3bff3f-cd4d-49bb-aac3-77e8e1168297\"," +
                "            \"model-invariant-id\" : \"e73127d5-c2de-43b2-bc02-602fa5c9aa29\"," +
                "            \"model-version-id\" : \"94209bf1-67fa-4741-96fe-a2b3f86f84b2\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"4776516b-7da2-446c-9ba7-47ca8c30c571\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1275\"," +
                "            \"resource-version\": \"1552591517864\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"46fcb25a-e7ba-4d96-99ba-3bb6eae6aba7\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1271\"," +
                "            \"resource-version\": \"1552331549256\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"ffefbe38-3087-418a-87ae-f6582a15be78\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1272\"," +
                "            \"resource-version\": \"1552469097776\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"961d05be-ee41-40a2-8653-f603fc495175\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1273\"," +
                "            \"resource-version\": \"1552481667950\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"14bcfc2f-bbee-4fd9-89a5-42eb5dbb08d5\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"913443\"," +
                "            \"resource-version\": \"1553182405707\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"89d4c968-158c-4722-a22c-c5c2ccc17fd5\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1276\"," +
                "            \"resource-version\": \"1553018006071\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"3e7834fb-a8e0-4243-a837-5352ccab4602\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1259\"," +
                "            \"resource-version\": \"1551967976427\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"customer\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v14/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"customer.global-customer-id\"," +
                "                                \"relationship-value\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"customer.subscriber-name\"," +
                "                                \"property-value\": \"SILVIA ROBBINS\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }," +
                "        {" +
                "            \"vpn-id\": \"844a1ea7-556a-4e49-8aa3-171f1db4ea02\"," +
                "            \"vpn-name\": \"LPPVPN\"," +
                "            \"vpn-platform\": \"AVPN\"," +
                "            \"vpn-type\": \"" + vpnType + "\"," +
                "            \"vpn-region\": \"USA,EMEA\"," +
                "            \"customer-vpn-id\": \"VPN1277\"," +
                "            \"resource-version\": \"1553086769917\"," +
                "            \"relationship-list\": {" +
                "                \"relationship\": [" +
                "                    {" +
                "                        \"related-to\": \"l3-network\"," +
                "                        \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "                        \"related-link\": \"/aai/v15/network/l3-networks/l3-network/ac5224b1-71cc-4237-a401-c00b2fd65a78\"," +
                "                        \"relationship-data\": [" +
                "                            {" +
                "                                \"relationship-key\": \"l3-network.network-id\"," +
                "                                \"relationship-value\": \"ac5224b1-71cc-4237-a401-c00b2fd65a78\"" +
                "                            }" +
                "                        ]," +
                "                        \"related-to-property\": [" +
                "                            {" +
                "                                \"property-key\": \"l3-network.network-name\"," +
                "                                \"property-value\": \"APP-C-24595-T-IST-04B_int_SUB-INTERFACE_net_051\"" +
                "                            }" +
                "                        ]" +
                "                    }" +
                "                ]" +
                "            }" +
                "        }" +
                "    ]" +
                "}";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/network/vpn-bindings";
    }

}
