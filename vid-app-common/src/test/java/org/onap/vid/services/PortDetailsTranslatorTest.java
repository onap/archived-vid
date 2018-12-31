package org.onap.vid.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.onap.vid.aai.model.CustomQuerySimpleResult;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.model.RelatedTo;
import org.onap.vid.aai.model.SimpleResult;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class PortDetailsTranslatorTest {

    private static final ObjectMapper om = new ObjectMapper();

    private PortDetailsTranslator portDetailsTranslator = new PortDetailsTranslator();

    @Test
    public void extractPortDetailsFromProperties_givenValidAaiResponse() throws IOException {

        final String aaiResponse = "{" +
                "    \"results\": [" +
                "        {" +
                "            \"id\": \"4876980240\"," +
                "            \"node-type\": \"l-interface\"," +
                "            \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/460f35aeb53542dc9f77105066483e83/vservers/vserver/15e46e2f-4b98-4e06-9644-f0e6e35cc79a/l-interfaces/l-interface/zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"," +
                "            \"properties\": {" +
                "                \"interface-name\": \"zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"," +
                "                \"selflink\": \"https://network-aic.rdm5b.cci.att.com:9696/v2.0/ports/6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"interface-id\": \"6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"macaddr\": \"02:6d:e7:bf:87:6f\"," +
                "                \"network-name\": \"APP-C-24595-D-T001-vprobe_int_pktmirror_net_1\"," +
                "                \"is-port-mirrored\": false," +
                "                \"resource-version\": \"1519383879190\"," +
                "                \"in-maint\": false," +
                "                \"is-ip-unnumbered\": false" +
                "            }" +
                "        }" +
                "    ]" +
                "}";

        CustomQuerySimpleResult aaiGetPortMirroringSourcePorts = om.readValue(aaiResponse, CustomQuerySimpleResult.class);


        PortDetailsTranslator.PortDetails portDetails = PortDetailsTranslator.extractPortDetailsFromProperties(aaiGetPortMirroringSourcePorts.getResults().get(0).getProperties(), aaiResponse);

        assertThat(portDetails, is(instanceOf(PortDetailsTranslator.PortDetailsOk.class)));

        PortDetailsTranslator.PortDetailsOk portDetailsOk = (PortDetailsTranslator.PortDetailsOk) portDetails;
        assertThat(portDetailsOk.getInterfaceName(), is("zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib"));
        assertThat(portDetailsOk.getInterfaceId(), is("6de7bf87-6faa-4984-9492-18d1188b3d4a"));
        assertThat(portDetailsOk.getIsPortMirrored(), is(false));
    }

    @Test
    public void extractPortDetailsFromProperties_givenAaiResponseWithInstanceNameNull_yieldException() throws IOException {
        final String aaiResponse = "{" +
                "    \"results\": [" +
                "        {" +
                "            \"id\": \"4876980240\"," +
                "            \"node-type\": \"l-interface\"," +
                "            \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/460f35aeb53542dc9f77105066483e83/vservers/vserver/15e46e2f-4b98-4e06-9644-f0e6e35cc79a/l-interfaces/l-interface/zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"," +
                "            \"properties\": {" +
                "                \"interface-name\": null," +
                "                \"selflink\": \"https://network-aic.rdm5b.cci.att.com:9696/v2.0/ports/6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"interface-id\": \"6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"macaddr\": \"02:6d:e7:bf:87:6f\"," +
                "                \"network-name\": \"APP-C-24595-D-T001-vprobe_int_pktmirror_net_1\"," +
                "                \"is-port-mirrored\": false," +
                "                \"resource-version\": \"1519383879190\"," +
                "                \"in-maint\": false," +
                "                \"is-ip-unnumbered\": false" +
                "            }" +
                "        }" +
                "    ]" +
                "}";

        CustomQuerySimpleResult aaiGetPortMirroringSourcePorts = om.readValue(aaiResponse, CustomQuerySimpleResult.class);
        PortDetailsTranslator.PortDetails portDetails = PortDetailsTranslator.extractPortDetailsFromProperties(aaiGetPortMirroringSourcePorts.getResults().get(0).getProperties(), aaiResponse);

        assertThat(portDetails, is(instanceOf(PortDetailsTranslator.PortDetailsError.class)));

        PortDetailsTranslator.PortDetailsError portDetailsError = (PortDetailsTranslator.PortDetailsError) portDetails;
        assertThat(portDetailsError.getErrorDescription(), is("Value of 'interface-name' is missing."));
        assertThat(portDetailsError.getRawAaiResponse(), is(aaiResponse));
    }

    @Test
    public void extractPortDetailsFromProperties_givenAaiResponseWithInstanceIdNull_yieldException() throws IOException {
        final String aaiResponse = "{" +
                "    \"results\": [" +
                "        {" +
                "            \"id\": \"4876980240\"," +
                "            \"node-type\": \"l-interface\"," +
                "            \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/460f35aeb53542dc9f77105066483e83/vservers/vserver/15e46e2f-4b98-4e06-9644-f0e6e35cc79a/l-interfaces/l-interface/zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"," +
                "            \"properties\": {" +
                "                \"interface-name\": \"zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"," +
                "                \"selflink\": \"https://network-aic.rdm5b.cci.att.com:9696/v2.0/ports/6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"interface-id\": null," +
                "                \"macaddr\": \"02:6d:e7:bf:87:6f\"," +
                "                \"network-name\": \"APP-C-24595-D-T001-vprobe_int_pktmirror_net_1\"," +
                "                \"is-port-mirrored\": false," +
                "                \"resource-version\": \"1519383879190\"," +
                "                \"in-maint\": false," +
                "                \"is-ip-unnumbered\": false" +
                "            }" +
                "        }" +
                "    ]" +
                "}";

        CustomQuerySimpleResult aaiGetPortMirroringSourcePorts = om.readValue(aaiResponse, CustomQuerySimpleResult.class);
        PortDetailsTranslator.PortDetails portDetails = PortDetailsTranslator.extractPortDetailsFromProperties(aaiGetPortMirroringSourcePorts.getResults().get(0).getProperties(), aaiResponse);

        assertThat(portDetails, is(instanceOf(PortDetailsTranslator.PortDetailsError.class)));

        PortDetailsTranslator.PortDetailsError portDetailsError = (PortDetailsTranslator.PortDetailsError) portDetails;
        assertThat(portDetailsError.getErrorDescription(), is("Value of 'interface-id' is missing."));
        assertThat(portDetailsError.getRawAaiResponse(), is(aaiResponse));
    }

    @Test
    public void extractPortDetailsFromProperties_givenAaiResponseWithEmptyInstanceId_yieldException() throws IOException {
        final String aaiResponse = "{" +
                "    \"results\": [" +
                "        {" +
                "            \"id\": \"4876980240\"," +
                "            \"node-type\": \"l-interface\"," +
                "            \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/460f35aeb53542dc9f77105066483e83/vservers/vserver/15e46e2f-4b98-4e06-9644-f0e6e35cc79a/l-interfaces/l-interface/zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"," +
                "            \"properties\": {" +
                "                \"interface-name\": \"\"," +
                "                \"selflink\": \"https://network-aic.rdm5b.cci.att.com:9696/v2.0/ports/6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"interface-id\": \"6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"macaddr\": \"02:6d:e7:bf:87:6f\"," +
                "                \"network-name\": \"APP-C-24595-D-T001-vprobe_int_pktmirror_net_1\"," +
                "                \"is-port-mirrored\": false," +
                "                \"resource-version\": \"1519383879190\"," +
                "                \"in-maint\": false," +
                "                \"is-ip-unnumbered\": false" +
                "            }" +
                "        }" +
                "    ]" +
                "}";

        CustomQuerySimpleResult aaiGetPortMirroringSourcePorts = om.readValue(aaiResponse, CustomQuerySimpleResult.class);
        PortDetailsTranslator.PortDetails portDetails = PortDetailsTranslator.extractPortDetailsFromProperties(aaiGetPortMirroringSourcePorts.getResults().get(0).getProperties(), aaiResponse);

        assertThat(portDetails, is(instanceOf(PortDetailsTranslator.PortDetailsError.class)));

        PortDetailsTranslator.PortDetailsError portDetailsError = (PortDetailsTranslator.PortDetailsError) portDetails;
        assertThat(portDetailsError.getErrorDescription(), is("Value of 'interface-name' is empty."));
        assertThat(portDetailsError.getRawAaiResponse(), is(aaiResponse));
    }

    @Test
    public void extractPortDetailsFromProperties_givenAaiResponseWithIsPortMirroredNull_yieldException() throws IOException {
        final String aaiResponse = "{" +
                "    \"results\": [" +
                "        {" +
                "            \"id\": \"4876980240\"," +
                "            \"node-type\": \"l-interface\"," +
                "            \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/rdm5b/tenants/tenant/460f35aeb53542dc9f77105066483e83/vservers/vserver/15e46e2f-4b98-4e06-9644-f0e6e35cc79a/l-interfaces/l-interface/zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"," +
                "            \"properties\": {" +
                "                \"interface-name\": \"zrdm5bfprbVLBA005-vlbagent_aff_int_pktmirror_1_port-dr5jhyxva5ib\"," +
                "                \"selflink\": \"https://network-aic.rdm5b.cci.att.com:9696/v2.0/ports/6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"interface-id\": \"6de7bf87-6faa-4984-9492-18d1188b3d4a\"," +
                "                \"macaddr\": \"02:6d:e7:bf:87:6f\"," +
                "                \"network-name\": \"APP-C-24595-D-T001-vprobe_int_pktmirror_net_1\"," +
                "                \"is-port-mirrored\": null," +
                "                \"resource-version\": \"1519383879190\"," +
                "                \"in-maint\": false," +
                "                \"is-ip-unnumbered\": false" +
                "            }" +
                "        }" +
                "    ]" +
                "}";

        CustomQuerySimpleResult aaiGetPortMirroringSourcePorts = om.readValue(aaiResponse, CustomQuerySimpleResult.class);
        PortDetailsTranslator.PortDetails portDetails = PortDetailsTranslator.extractPortDetailsFromProperties(aaiGetPortMirroringSourcePorts.getResults().get(0).getProperties(), aaiResponse);

        assertThat(portDetails, is(instanceOf(PortDetailsTranslator.PortDetailsError.class)));

        PortDetailsTranslator.PortDetailsError portDetailsError = (PortDetailsTranslator.PortDetailsError) portDetails;
        assertThat(portDetailsError.getErrorDescription(), is("Value of 'is-port-mirrored' is missing."));
        assertThat(portDetailsError.getRawAaiResponse(), is(aaiResponse));
    }

    @Test
    public void getFilteredPortList_givenEmptyList_ReturnEmptyList() {

        final ImmutableList<SimpleResult> input = ImmutableList.of();

        List<SimpleResult> result = portDetailsTranslator.getFilteredPortList(input);
        assertThat("List size if different than expected", result, is(empty()));
    }

    @Test
    public void getFilteredPortList_givenFeatureFlagIsOff_returnAllLInterfaces() {
        final String relationshipLabelSource = "org.onap.relationships.inventory.Source";
        final String nodeTypeLInterface = "l-interface";

        SimpleResult lInterfaceWithSource =
                buildSimpleResult(nodeTypeLInterface, relationshipLabelSource);
        SimpleResult lInterfaceWithTwoSources =
                buildSimpleResult(nodeTypeLInterface, relationshipLabelSource, relationshipLabelSource);
        SimpleResult lInterfaceWithSourceAndMore =
                buildSimpleResult(nodeTypeLInterface, relationshipLabelSource, "not a source");
        SimpleResult lInterfaceWithoutSource =
                buildSimpleResult(nodeTypeLInterface, "not a source");
        SimpleResult lInterfaceWithoutRelations =
                buildSimpleResult(nodeTypeLInterface);
        SimpleResult fooTypeWithSource =
                buildSimpleResult("not an l-interface", relationshipLabelSource);
        SimpleResult fooTypeWithoutSource =
                buildSimpleResult("not an l-interface", "not a source");

        final ImmutableList<SimpleResult> input = ImmutableList.of(
                fooTypeWithSource, fooTypeWithoutSource,
                lInterfaceWithTwoSources, lInterfaceWithSourceAndMore,
                lInterfaceWithoutSource, lInterfaceWithSource,
                lInterfaceWithoutRelations);

        List<SimpleResult> result = portDetailsTranslator.getFilteredPortList(input);

        assertThat("List should contain all l-interfaces with a related source", result, containsInAnyOrder(
                lInterfaceWithSource, lInterfaceWithSourceAndMore,
                lInterfaceWithTwoSources));

    }

    private SimpleResult buildSimpleResult(String nodeType, String... relationshipLabels) {
        SimpleResult simpleResult = new SimpleResult();
        simpleResult.setJsonNodeType(nodeType);
        simpleResult.setJsonRelatedTo(Stream.of(relationshipLabels).map(label ->
                new RelatedTo("my foo id", label, "logical-link", "foo url"))
                .collect(Collectors.toList())
        );
        return simpleResult;
    }

}
