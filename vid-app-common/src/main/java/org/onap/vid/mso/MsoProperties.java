/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.mso;

import org.onap.portalsdk.core.util.SystemProperties;

/**
 * The Class MsoProperties.
 */
public class MsoProperties extends SystemProperties {

	/** The Constant MSO_SERVER_URL. */
	//VID Properties related to MSO
	public static final String MSO_SERVER_URL = "mso.server.url";

	/** The Constant MSO_DME2_SERVER_URL. */
	public static final String MSO_DME2_SERVER_URL = "mso.dme2.server.url";

	/** The Constant MSO_DME2_CLIENT_TIMEOUT. */
	public static final String MSO_DME2_CLIENT_TIMEOUT = "mso.dme2.client.timeout";
	
	/** The Constant MSO_DME2_CLIENT_READ_TIMEOUT. */
	public static final String MSO_DME2_CLIENT_READ_TIMEOUT = "mso.dme2.client.read.timeout";
	
	/** The Constant MSO_SERVER_URL_DEFAULT. */
	public static final String MSO_SERVER_URL_DEFAULT= "";
	
	/** The Constant MSO_POLLING_INTERVAL_MSECS. */
	// number of msecs to wait between polling requests
	public static final String MSO_POLLING_INTERVAL_MSECS = "mso.polling.interval.msecs";

	/** The Constant MSO_POLLING_INTERVAL_MSECS_DEFAULT. */
	public static final String MSO_POLLING_INTERVAL_MSECS_DEFAULT = "60000";
	
	/** The Constant MSO_DME2_ENABLED. */
	public static final String MSO_DME2_ENABLED = "mso.dme2.enabled";
	
	/** The Constant MSO_MAX_POLLS. */
	public static final String MSO_MAX_POLLS = "mso.max.polls";
	
	/** The Constant MSO_MAX_POLLS_DEFAULT. */
	public static final String MSO_MAX_POLLS_DEFAULT = "10"; //10
	
	/** The Constant MSO_USER_NAME. */
	public static final String MSO_USER_NAME = "mso.user.name"; //m03346
	
	/** The Constant MSO_PASSWORD. */
	public static final String MSO_PASSWORD = "mso.password.x";
	
	/** The Constant MSO_REST_API_SVC_INSTANCE. */
    public static final String MSO_REST_API_E2E_SVC_INSTANCE = "mso.restapi.svc.e2einstance"; // /e2eServiceInstances/v3

	/** The Constant MSO_REST_API_SVC_INSTANCE. */
	public static final String MSO_RESTAPI_SERVICE_INSTANCE = "mso.restapi.service.instance"; // /serviceInstances/v2

	/** The Constant MSO_REST_API_VNF_INSTANCE. */
	public static final String MSO_REST_API_VNF_INSTANCE = "mso.restapi.vnf.instance";
	
	/** The Constant MSO_REST_API_VNF_CHANGE_MANAGEMENT_INSTANCE. */
	public static final String MSO_REST_API_VNF_CHANGE_MANAGEMENT_INSTANCE = "mso.restapi.vnf.changemanagement.instance";
	
	/** The Constant MSO_REST_API_NETWORK_INSTANCE. */
	public static final String MSO_REST_API_NETWORK_INSTANCE = "mso.restapi.network.instance";
	
	/** The Constant MSO_REST_API_GET_ORC_REQ. */
	public static final String MSO_REST_API_GET_ORC_REQ = "mso.restapi.get.orc.req";
	
	/** The Constant MSO_REST_API_GET_ORC_REQS. */
	public static final String MSO_REST_API_GET_ORC_REQS = "mso.restapi.get.orc.reqs";

	/** The Constant MSO_REST_API_GET_MAN_TASK. */
	public static final String MSO_REST_API_GET_MAN_TASKS = "mso.restapi.get.man.tasks";

	/** The Constant MSO_REST_API_VF_MODULE_INSTANCE. */
	public static final String MSO_REST_API_VF_MODULE_INSTANCE = "mso.restapi.vf.module.instance";

	public static final String MSO_REST_API_VF_MODULE_SCALE_OUT = "mso.restapi.vf.module.scaleout";

	/** The Constant MSO_REST_API_WORKFLOW_INSTANCE. */
	public static final String MSO_REST_API_WORKFLOW_INSTANCE = "mso.restapi.workflow.invoke";

	/** The Constant MSO_REST_API_VOLUME_GROUP_INSTANCE. */
	public static final String MSO_REST_API_VOLUME_GROUP_INSTANCE = "mso.restapi.volume.group.instance";

	/** The Constant MSO_REST_API_VOLUME_GROUP_INSTANCE. */
	public static final String MSO_REST_API_INSTANCE_GROUP = "mso.restapi.instance.group";

	/** The Constant MSO_REST_API_CONFIGURATION_INSTANCE. */
	public static final String MSO_REST_API_CONFIGURATIONS = "mso.restapi.configurations";
	public static final String MSO_REST_API_CONFIGURATION_INSTANCE = "mso.restapi.configuration.instance";

	/** The Constant MSO_REST_API_OPERATIONAL_ENVIRONMENT */
	public static final String MSO_REST_API_OPERATIONAL_ENVIRONMENT_ACTIVATE = "mso.restapi.operationalEnvironment.activate";
	public static final String MSO_REST_API_OPERATIONAL_ENVIRONMENT_DEACTIVATE = "mso.restapi.operationalEnvironment.deactivate";

	/** The Constant MSO_REST_API_OPERATIONAL_ENVIRONMENT_CREATE */
	public static final String MSO_REST_API_OPERATIONAL_ENVIRONMENT_CREATE = "mso.restapi.operationalEnvironment.create";

	/** The Constant MSO_REST_API_CLOUD_RESOURCES_REQUEST_STATUS */
	public static final String MSO_REST_API_CLOUD_RESOURCES_REQUEST_STATUS = "mso.restapi.operationalEnvironment.cloudResourcesRequests.status";

	/** The Constant MSO_REST_API_SERVICE_INSTANCE_ASSIGN */
	public static final String MSO_REST_API_SERVICE_INSTANCE_ASSIGN = "mso.restapi.serviceInstanceAssign";

	public static final String MSO_REST_API_WORKFLOW_SPECIFICATIONS= "mso.restapi.changeManagement.workflowSpecifications";
}
