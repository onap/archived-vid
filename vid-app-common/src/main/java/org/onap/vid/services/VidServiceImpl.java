/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.vid.services;

import static org.onap.vid.model.probes.ExternalComponentStatus.Component.SDC;
import static org.onap.vid.properties.Features.FLAG_SERVICE_MODEL_CACHE;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.joshworks.restclient.http.HttpResponse;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.parser.ToscaParser;
import org.onap.vid.asdc.parser.ToscaParserImpl;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.probes.ErrorMetadata;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.model.probes.StatusMetadata;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.utils.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.togglz.core.manager.FeatureManager;

/**
 * The Class VidController.
 */

@org.springframework.stereotype.Service
public class VidServiceImpl implements VidService {
    /**
     * The Constant LOG.
     */
    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VidServiceImpl.class);

    protected final AsdcClient asdcClient;
    private final FeatureManager featureManager;

    private ToscaParserImpl2 toscaParser;
    private final LoadingCache<String, ServiceModel> serviceModelCache;


    private class NullServiceModelException extends Exception {
        NullServiceModelException(String modelUuid) {
            super("Could not create service model for UUID " + modelUuid);
        }
    }

    @Autowired
    public VidServiceImpl(AsdcClient asdcClient, ToscaParserImpl2 toscaParser, FeatureManager featureManager) {
        this.asdcClient = asdcClient;
        this.featureManager = featureManager;
        this.toscaParser=toscaParser;
        this.serviceModelCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(7, TimeUnit.DAYS)
                .build(new CacheLoader<String, ServiceModel>() {
                    @Override
                    public ServiceModel load(String modelUuid) throws AsdcCatalogException, NullServiceModelException {
                        ServiceModel serviceModel = getServiceFromSdc(modelUuid);
                        if (serviceModel != null) {
                            return serviceModel;
                        } else {
                            throw new NullServiceModelException(modelUuid);
                        }
                    }
                });
    }

    @Override
    public ServiceModel getService(String uuid) throws AsdcCatalogException {
        if (featureManager.isActive(FLAG_SERVICE_MODEL_CACHE)) {
            return getServiceFromCache(uuid);
        } else {
            return getServiceFromSdc(uuid);
        }
    }

    @NonNull
    @Override
    public ServiceModel getServiceModelOrThrow(String modelVersionId) {
        try {
            final ServiceModel serviceModel = getService(modelVersionId);
            if (serviceModel == null) {
                throw new GenericUncheckedException("Model version '" + modelVersionId + "' not found");
            }
            return serviceModel;
        } catch (AsdcCatalogException e) {
            throw new GenericUncheckedException("Exception while loading model version '" + modelVersionId + "'", e);
        }
    }

    private ServiceModel getServiceFromCache(String uuid) throws AsdcCatalogException {
        try {
            return serviceModelCache.get(uuid);
        } catch (ExecutionException e) {
            LOG.error("Failed to get service info from cache ", e.getLocalizedMessage());
            if (e.getCause() instanceof AsdcCatalogException) {
                throw (AsdcCatalogException) e.getCause();
            } else if (e.getCause() instanceof NullServiceModelException) {
                return null;
            } else {
                throw new GenericUncheckedException(e);
            }
        }
    }

    private ServiceModel getServiceFromSdc(String uuid) throws AsdcCatalogException {
        final Path serviceCsar = asdcClient.getServiceToscaModel(UUID.fromString(uuid));
        ToscaParser tosca = new ToscaParserImpl();
        serviceCsar.toFile().getAbsolutePath();
        ServiceModel serviceModel = null;
        try {
            final Service asdcServiceMetadata = asdcClient.getService(UUID.fromString(uuid));
            return getServiceModel(uuid, serviceCsar, tosca, asdcServiceMetadata);
        } catch (Exception e) {
            LOG.error("Failed to download and process service from SDC", e);
        }
        return serviceModel;
    }

    private ServiceModel getServiceModel(String uuid, Path serviceCsar, ToscaParser tosca, Service asdcServiceMetadata) throws AsdcCatalogException {
        try {
            return toscaParser.makeServiceModel(serviceCsar, asdcServiceMetadata);
        } catch (SdcToscaParserException e) {
            LOG.error("Failed to create service model using sdc tosca library", e);
            return tosca.makeServiceModel(uuid, serviceCsar, asdcServiceMetadata);
        }
    }

    @Override
    public void invalidateServiceCache() {
        serviceModelCache.invalidateAll();
    }

    @Override
    public ExternalComponentStatus probeComponent() {
        UUID modelUUID;
        try {
            modelUUID = UUID.fromString(VidProperties.getPropertyWithDefault(
                VidProperties.PROBE_SDC_MODEL_UUID,""));
        }
        //in case of no such PROBE_SDC_MODEL_UUID property or non uuid there we check only sdc connectivity
        catch (Exception e) {
            return probeComponentBySdcConnectivity();
        }

        return probeSdcByGettingModel(modelUUID);
    }

    public ExternalComponentStatus probeComponentBySdcConnectivity() {
        long startTime = System.currentTimeMillis();
        ExternalComponentStatus externalComponentStatus;
        try {
            HttpResponse<String> stringHttpResponse = asdcClient.checkSDCConnectivity();
            HttpRequestMetadata httpRequestMetadata = new HttpRequestMetadata(HttpMethod.GET, stringHttpResponse.getStatus(), asdcClient.getBaseUrl() + AsdcClient.URIS.HEALTH_CHECK_ENDPOINT, stringHttpResponse.getBody(), "SDC healthCheck",
                    System.currentTimeMillis() - startTime);
            externalComponentStatus = new ExternalComponentStatus(ExternalComponentStatus.Component.SDC, stringHttpResponse.isSuccessful(), httpRequestMetadata);
        } catch (Exception e) {
            ErrorMetadata errorMetadata = new ErrorMetadata(Logging.exceptionToDescription(e), System.currentTimeMillis() - startTime);
            externalComponentStatus = new ExternalComponentStatus(ExternalComponentStatus.Component.SDC, false, errorMetadata);
        }
        return externalComponentStatus;
    }

    protected ExternalComponentStatus probeSdcByGettingModel(UUID modelUUID) {
        final long startTime = System.currentTimeMillis();

        HttpResponseWithRequestInfo<InputStream> response = null;
        try {
            response = asdcClient.getServiceInputStream(modelUUID, true);
            int responseStatusCode = response.getResponse().getStatus();

            if (responseStatusCode == 404) {
                return errorStatus(response, startTime, "model " + modelUUID + " not found in SDC" +
                    " (consider updating vid probe configuration '" + VidProperties.PROBE_SDC_MODEL_UUID + "')");
            } else if (responseStatusCode >= 400) {
                return errorStatus(response, startTime, "error while retrieving model " + modelUUID + " from SDC");
            } else {
                InputStream inputStreamEntity = response.getResponse().getRawBody();//validate we can read the input steam from the response
                if (inputStreamEntity.read() <= 0) {
                    return errorStatus(response, startTime, "error reading model " + modelUUID + " from SDC");
                } else {
                    // RESPONSE IS SUCCESS
                    return new ExternalComponentStatus(SDC, true,
                        new HttpRequestMetadata(response, "OK", startTime, false));
                }
            }
        } catch (ExceptionWithRequestInfo e) {
            return new ExternalComponentStatus(SDC, false,
                new HttpRequestMetadata(e, System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            return errorStatus(response, startTime, Logging.exceptionToDescription(e));
        }
    }

    private ExternalComponentStatus errorStatus(HttpResponseWithRequestInfo<InputStream> response, long startTime, String description) {
        final long duration = System.currentTimeMillis() - startTime;
        StatusMetadata statusMetadata = (response == null) ?
            new ErrorMetadata(description, duration) :
            new HttpRequestMetadata(response, description, duration, true);

        return new ExternalComponentStatus(SDC, false, statusMetadata);
    }
}
