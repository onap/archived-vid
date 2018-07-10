package org.onap.vid.services;

import org.onap.vid.model.PombaInstance.PombaRequest;

public interface PombaService {
    void verify(PombaRequest request);
}