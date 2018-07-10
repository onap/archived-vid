package org.onap.vid.services;

import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.model.ServiceModel;

public interface VidService {

	ServiceModel getService(String uuid) throws AsdcCatalogException;

    void invalidateServiceCache();
}