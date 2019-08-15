/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

package org.onap.vid.asdc;

import io.joshworks.restclient.http.HttpResponse;
import java.io.InputStream;
import org.onap.vid.aai.GenericResponseWithRequestInfo;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.asdc.beans.Service;

import java.nio.file.Path;
import java.util.UUID;

/**
 * The Interface AsdcClient.
 */
public interface AsdcClient {
	class URIS{
		public static final String METADATA_URL_TEMPLATE = "%s%s/%s/metadata";
		public static final String TOSCA_MODEL_URL_TEMPLATE = "%s%s/%s/toscaModel";
		public static final String HEALTH_CHECK_ENDPOINT = "sdc2/rest/healthCheck";
	}
	/**
	 * Gets the service.
	 *
	 * @param uuid the uuid
	 * @return the service
	 * @throws AsdcCatalogException the sdc catalog exception
	 */
	Service getService(UUID uuid) throws AsdcCatalogException;

	/**
	 * Gets the service tosca model.
	 *
	 * @param uuid the uuid
	 * @return the service tosca model
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	Path getServiceToscaModel(UUID uuid) throws AsdcCatalogException;


	HttpResponseWithRequestInfo<InputStream> getServiceInputStream(UUID serviceUuid, boolean warpException);

	HttpResponse<String> checkSDCConnectivity();

	String getBaseUrl();
}
