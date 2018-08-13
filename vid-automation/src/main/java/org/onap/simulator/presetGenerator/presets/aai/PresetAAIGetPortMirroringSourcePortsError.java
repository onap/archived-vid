package org.onap.simulator.presetGenerator.presets.aai;

public class PresetAAIGetPortMirroringSourcePortsError extends PresetAAIGetPortMirroringSourcePorts {

    public PresetAAIGetPortMirroringSourcePortsError(String configurationId, String interfaceId, String interfaceName, boolean isPortMirrored) {
        super(configurationId, interfaceId, interfaceName, isPortMirrored);
    }

    @Override
    public int getResponseCode() {
        return 503;
    }

    @Override
    public Object getResponseBody() {
        return "You are not allowed to do things";
    }
}
