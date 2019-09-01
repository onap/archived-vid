package org.onap.vid.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.model.aaiTree.Network;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AaiController2Test {

    @InjectMocks
    private AaiController2 aaiController;

    @Mock
    private AaiService aaiService;

    @Mock
    private RoleProvider roleProvider;

    @Mock
    private AaiClientInterface aaiClientInterface;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetActiveNetworkList(){

        Network expected1 = mockNetwork("name6", true, "Active");
        Network expected2 = mockNetwork("name6", true, "Active");

        List<Network> rawNetworks = new ArrayList<>();
        rawNetworks.add(mockNetwork("", true, "Active"));
        rawNetworks.add(mockNetwork(null, true, "Active"));
        rawNetworks.add(mockNetwork("name", false, "Active"));
        rawNetworks.add(mockNetwork("name2", true, "Created"));
        rawNetworks.add(mockNetwork("name3", true, null));
        rawNetworks.add(mockNetwork("name4", true, ""));
        rawNetworks.add(expected1);
        rawNetworks.add(expected2);

        when(aaiService.getL3NetworksByCloudRegion(any(), any(), any())).thenReturn(rawNetworks);
        List<Network> networks = aaiController.getActiveNetworkList("just", "fake", "params");
        Assert.assertEquals(2, networks.size());
        assertThat(networks, containsInAnyOrder(expected1, expected2));
    }

    private Network mockNetwork(String name, boolean isBoundToVpn, String orchStatus) {
        Network network = mock(Network.class);
        when(network.getInstanceName()).thenReturn(name);
        when(network.isBoundToVpn()).thenReturn(isBoundToVpn);
        when(network.getOrchStatus()).thenReturn(orchStatus);
        return network;
    }

    @Test
    public void testGetNewestModelVersionByInvariant() {

        String modelInvariantId = "model-invariant-v2.0";
        String modelVersion ="2.0";
        ModelVer expectedModelVer = new ModelVer();
        expectedModelVer.setModelVersion(modelVersion);

        when(aaiService.getNewestModelVersionByInvariantId(modelInvariantId)).thenReturn(expectedModelVer);

        ModelVer actualModelVer = aaiController.getNewestModelVersionByInvariant(modelInvariantId);

        assertEquals (actualModelVer.getModelVersion(),modelVersion);

    }
}
