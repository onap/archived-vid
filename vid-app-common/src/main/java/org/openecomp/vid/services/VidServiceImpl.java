package org.openecomp.vid.services;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.AsdcClient;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.asdc.parser.ToscaParser;
import org.openecomp.vid.asdc.parser.ToscaParserImpl;
import org.openecomp.vid.asdc.parser.ToscaParserImpl2;
import org.openecomp.vid.model.ServiceModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Class VidController.
 */

public class VidServiceImpl implements VidService {
    /**
     * The Constant LOG.
     */
    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VidServiceImpl.class);
    /**
     * The Constant dateFormat.
     */
    private final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    protected final AsdcClient asdcClient;
    @Autowired
    private ToscaParserImpl2 toscaParser;

    public VidServiceImpl(AsdcClient asdcClient) {
        this.asdcClient = asdcClient;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openecomp.vid.controller.VidService#getServices(java.util.Map)
     */
    @Override
    public Collection<Service> getServices(Map<String, String[]> requestParams)
            throws AsdcCatalogException {
        return asdcClient.getServices(requestParams);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openecomp.vid.controller.VidService#getService(java.lang.String)
     */
    @Override
    public ServiceModel getService(String uuid) throws AsdcCatalogException {
        final Path serviceCsar = asdcClient.getServiceToscaModel(UUID.fromString(uuid));
        ToscaParser tosca = new ToscaParserImpl();
        serviceCsar.toFile().getAbsolutePath();
        ServiceModel serviceModel = null;
        try {
            final Service asdcServiceMetadata = asdcClient.getService(UUID.fromString(uuid));
            try {
                serviceModel = toscaParser.makeServiceModel(serviceCsar, asdcServiceMetadata);
            }
            catch (SdcToscaParserException e){
                serviceModel = tosca.makeServiceModel(uuid, serviceCsar, asdcServiceMetadata);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceModel;
    }


}