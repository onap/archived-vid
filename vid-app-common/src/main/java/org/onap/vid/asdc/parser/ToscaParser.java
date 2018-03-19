package org.onap.vid.asdc.parser;

import java.nio.file.Path;

import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.tosca.ToscaCsar;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.ServiceModel;

public interface ToscaParser{
	ToscaCsar parse(Path path) throws AsdcCatalogException;
	
	ServiceModel makeServiceModel(String uuid,Path path,Service asdcServiceMetadata) throws Exception;
}