package org.onap.vid.services;

import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.ServiceModel;

import java.util.Collection;
import java.util.Map;

public interface VidService {

	Collection<Service> getServices(Map<String, String[]> requestParams)
			throws AsdcCatalogException;

	ServiceModel getService(String uuid) throws AsdcCatalogException;

}