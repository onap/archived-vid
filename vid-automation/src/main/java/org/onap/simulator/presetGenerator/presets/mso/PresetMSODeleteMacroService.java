package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSODeleteMacroService extends PresetMSODeleteService {

    public PresetMSODeleteMacroService(String requestId, String serviceInstanceId)
    {
        super(requestId, serviceInstanceId);
    }

    @Override
    public Object getRequestBody()
    {
        return "{  " +
                "   \"requestDetails\":{  " +
                "      \"modelInfo\":{  " +
                "         \"modelInvariantId\":\"dfc2c44c-2429-44ca-ae26-1e6dc1f207fb\"," +
                "         \"modelVersionId\":\"f028b2e2-7080-4b13-91b2-94944d4c42d8\"," +
                "         \"modelName\":\"Service with VRF\"," +
                "         \"modelType\":\"service\"," +
                "         \"modelVersion\":\"5.0\"" +
                "      }," +
                "      \"requestInfo\":{  " +
                "         \"source\":\"VID\"," +
                "         \"requestorId\":\"us16807000\"" +
                "      }," +
                "      \"requestParameters\":{  " +
                "         \"aLaCarte\":false" +
                "      }" +
                "   }" +
                "}";

    }
}
