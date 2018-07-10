package org.onap.vid.services;

import org.onap.vid.aai.PombaClientInterface;
import org.onap.vid.model.PombaInstance.PombaRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class PombaServiceImpl implements PombaService {

    @Autowired
    private PombaClientInterface pombaClientInterface;


    @Override
    public void verify(PombaRequest request) {
        pombaClientInterface.verify(request);
    }
}