package org.onap.vid.aai;

import org.onap.vid.model.PombaInstance.PombaRequest;

public interface PombaClientInterface {
    void verify(PombaRequest request);
}