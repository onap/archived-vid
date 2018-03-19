package org.onap.vid.services;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.parser.ToscaParser;
import org.onap.vid.asdc.parser.ToscaParserImpl;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.model.ServiceModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

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
    protected final AsdcClient asdcClient;
    @Autowired
    private ToscaParserImpl2 toscaParser;

    public VidServiceImpl(AsdcClient asdcClient) {
        this.asdcClient = asdcClient;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.onap.vid.controller.VidService#getServices(java.util.Map)
     */
    @Override
    public Collection<Service> getServices(Map<String, String[]> requestParams)
            throws AsdcCatalogException {
        return asdcClient.getServices(requestParams);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.onap.vid.controller.VidService#getService(java.lang.String)
     */
    @Override
    public ServiceModel getService(String uuid) throws AsdcCatalogException {
        final Path serviceCsar = asdcClient.getServiceToscaModel(UUID.fromString(uuid));
        ToscaParser tosca = new ToscaParserImpl();
        serviceCsar.toFile().getAbsolutePath();
        ServiceModel serviceModel = null;
        try {
            final Service asdcServiceMetadata = asdcClient.getService(UUID.fromString(uuid));
            return getServiceModel(uuid, serviceCsar, tosca, asdcServiceMetadata);
        } catch (Exception e) {
            LOG.error("Failed to download and proccess service from ASDC", e);
        }
        return serviceModel;
    }

    private ServiceModel getServiceModel(String uuid, Path serviceCsar, ToscaParser tosca, Service asdcServiceMetadata) throws Exception {
        try {
            return toscaParser.makeServiceModel(serviceCsar, asdcServiceMetadata);
        } catch (SdcToscaParserException e) {
            return tosca.makeServiceModel(uuid, serviceCsar, asdcServiceMetadata);
        }
    }


}