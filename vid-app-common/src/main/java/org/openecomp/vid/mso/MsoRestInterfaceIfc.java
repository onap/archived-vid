/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.openecomp.vid.mso;

import org.openecomp.vid.mso.rest.RequestDetails;

/**
 * The Interface MsoRestInterfaceIfc.
 */
public interface MsoRestInterfaceIfc {
	
	/**
	 * Inits the rest client.
	 */
	public void initRestClient();
	
	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @param sourceId the source id
	 * @param path the path
	 * @param restObject the rest object
	 * @throws Exception the exception
	 */
	public <T> void Get (T t, String sourceId, String path, RestObject<T> restObject ) throws Exception;
	
	/**
	 * Delete.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @param r the r
	 * @param sourceID the source ID
	 * @param path the path
	 * @param restObject the rest object
	 * @throws Exception the exception
	 */
	public <T> void Delete(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) throws Exception;
	
	/**
	 * Post.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @param r the r
	 * @param sourceID the source ID
	 * @param path the path
	 * @param restObject the rest object
	 * @throws Exception the exception
	 */
	public <T> void Post(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) throws Exception;
	
	/***
	 * Log request.
	 *
	 * @param r the r
	 */
	public void logRequest ( RequestDetails r  );
}
