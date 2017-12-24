package org.openecomp.simulator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimulatorRequestResponseExpectation {

    Logger logger = LoggerFactory.getLogger(SimulatorRequestResponseExpectation.class);

    private SimulatorRequest simulatorRequest;
    private SimulatorResponse simulatorResponse;
    private Misc misc;

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

    public Misc getMisc() {
        return misc == null ? new Misc() : misc;
    }

    public void setMisc(Misc misc) {
        this.misc = misc;
    }

    @Override
    public String toString() {
        return "ExpectationModel{" +
                "simulatorRequest=" + simulatorRequest +
                ", simulatorResponse=" + simulatorResponse +
                ", misc=" + misc +
                '}';
    }
}
