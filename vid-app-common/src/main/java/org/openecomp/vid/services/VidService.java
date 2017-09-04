package org.openecomp.vid.services;

import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.model.ServiceModel;

import java.util.Collection;
import java.util.Map;

public interface VidService {

	Collection<Service> getServices(Map<String, String[]> requestParams)
			throws AsdcCatalogException;

	ServiceModel getService(String uuid) throws AsdcCatalogException;

}