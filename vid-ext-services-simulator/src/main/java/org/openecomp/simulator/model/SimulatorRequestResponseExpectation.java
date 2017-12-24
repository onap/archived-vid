package org.openecomp.simulator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openecomp.simulator.errorHandling.VidSimulatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimulatorRequestResponseExpectation {

    Logger logger = LoggerFactory.getLogger(SimulatorRequestResponseExpectation.class);

    private SimulatorRequest simulatorRequest;
    private SimulatorResponse simulatorResponse;

    public SimulatorRequest getSimulatorRequest() {
        return simulatorRequest;
    }

    public void setSimulatorRequest(SimulatorRequest simulatorRequest) {
        this.simulatorRequest = simulatorRequest;
    }

    public SimulatorResponse getSimulatorResponse() {
        return simulatorResponse;
    }

    public void setSimulatorResponse(SimulatorResponse simulatorResponse) {
        this.simulatorResponse = simulatorResponse;
    }

    @Override
    public String toString() {
        return "ExpectationModel{" +
                "simulatorRequest=" + simulatorRequest +
                ", simulatorResponse=" + simulatorResponse +
                '}';
    }
}
