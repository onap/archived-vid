package org.openecomp.vid.asdc.parser;

import java.nio.file.Path;

import org.openecomp.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.beans.tosca.ToscaCsar;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.model.ServiceModel;

public interface ToscaParser{
	ToscaCsar parse(Path path) throws AsdcCatalogException;
	
	ServiceModel makeServiceModel(String uuid,Path path,Service asdcServiceMetadata) throws Exception;
}