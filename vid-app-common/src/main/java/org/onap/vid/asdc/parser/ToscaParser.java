package org.onap.vid.asdc.parser;

import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.beans.tosca.ToscaCsar;
import org.onap.vid.model.ServiceModel;

import java.nio.file.Path;

public interface ToscaParser{
	ToscaCsar parse(Path path) throws AsdcCatalogException;
	
	ServiceModel makeServiceModel(String uuid,Path path,Service asdcServiceMetadata) throws AsdcCatalogException;
}