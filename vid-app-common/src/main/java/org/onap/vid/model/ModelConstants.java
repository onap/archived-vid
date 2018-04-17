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

package org.onap.vid.model;
/**
 * The Class ModelConstants
 */
public class ModelConstants {
	
	/** The Constant GET_INPUT_TAG. */
	public final static String GET_INPUT_TAG = "get_input";
	
	public static final String ASDC_MODEL_NAMESPACE = "asdc.model.namespace";
	public static final String ASDC_SVC_API_PATH = "sdc.svc.api.path";
	public static final String ASDC_RESOURCE_API_PATH = "sdc.resource.api.path";
	
	public static final String DEFAULT_ASDC_MODEL_NAMESPACE = "org.onap.";
	public static final String DEFAULT_ASDC_SVC_API_PATH = "sdc/v1/catalog/services";
	public static final String DEFAULT_ASDC_RESOURCE_API_PATH = "sdc/v1/catalog/resources";
	
	public final static String VF_MODULE = "groups.VfModule";
	public final static String VNF = "resource.vf";
	public final static String NETWORK = "resource.vl";

	public final static String ROLE_DELIMITER = "___";
	
}
