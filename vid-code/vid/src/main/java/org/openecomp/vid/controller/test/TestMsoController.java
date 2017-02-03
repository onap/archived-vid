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

package org.openecomp.vid.controller.test;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.openecomp.vid.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.openecomp.portalsdk.core.controller.RestrictedBaseController;

/*
 * The "TestMsoController" class is primarily designed to help test "msoCommitController.js"
 * 
 * This class expects and receives JSON data in the same format as expected
 * in the "real" application version of this code. However, string versions of JSON are
 * maintained internally here instead of marshalled / unmarshalled JSON objects.
 * The primary reasons for this were to encapsulate all the test code in this single file and
 * minimize the time required to support initial test cases.
 * 
 * The non-test equivalent of this controller could alternatively incorporate POJO objects
 * instead of strings. However, the same data format sent to / received from the browser
 * JavaScript code would still be expected.
 * 
 * Two specific mechanisms used in this test class may be useful to the application version:
 * 
 * 		1) The use of "{variable}" elements in @RequestMappings along with the corresponding
 * 		@PathVariable declarations.
 * 
 * 		2) The use of @ExceptionHandler for general purpose exception handler.
 * 		(See @ExceptionHandler comments)
 *
 * This class is intended to be used in either:
 * 
 * 		A) Eclipse environments
 * 	OR
 * 		B) Linux environments with ONLY a single user running tests.
 *		The "quick and dirty" error simulation approach used here makes use of static states for some
 *		scenarios. Thus multiple users simultaneously testing in Linux environments
 *		may have contention issues.
 */

/**
 * The Class TestMsoController.
 */
@RestController
@RequestMapping("testmso")
public class TestMsoController extends RestrictedBaseController {

	/*
	 * Artificial delay (in milliseconds) added before responding to create /
	 * delete requests
	 */

	/** The Constant TEST_DELAY_SHORT_MSEC. */
	private final static int TEST_DELAY_SHORT_MSEC = 1000;

	/*
	 * Long delay to simulate non-responsive server test
	 */

	/** The Constant TEST_DELAY_LONG_MSEC. */
	private final static int TEST_DELAY_LONG_MSEC = 15000;

	/*
	 * Default number of polls expected before transaction complete.
	 */

	/** The Constant MAXIMUM_POLLS_DEFAULT. */
	private final static int MAXIMUM_POLLS_DEFAULT = 4;

	/*
	 * Number of polls to simulate "maximum polls exceeded" test.
	 */

	/** The Constant MAXIMUM_POLLS_LARGE. */
	private final static int MAXIMUM_POLLS_LARGE = 10;

	/*
	 * Simulated error types. The GUI front end is expected to set these values
	 * in the "modelName" field of the "mso_create_svc_instance" request.
	 */

	/** The Constant ERROR_POLICY_EXCEPTION. */
	private final static String ERROR_POLICY_EXCEPTION = "ERROR_POLICY_EXCEPTION";
	
	/** The Constant ERROR_SERVICE_EXCEPTION. */
	private final static String ERROR_SERVICE_EXCEPTION = "ERROR_SERVICE_EXCEPTION";
	
	/** The Constant ERROR_POLL_FAILURE. */
	private final static String ERROR_POLL_FAILURE = "ERROR_POLL_FAILURE";
	
	/** The Constant ERROR_INVALID_FIELD_INITIAL. */
	private final static String ERROR_INVALID_FIELD_INITIAL = "ERROR_INVALID_FIELD_INITIAL";
	
	/** The Constant ERROR_INVALID_FIELD_POLL. */
	private final static String ERROR_INVALID_FIELD_POLL = "ERROR_INVALID_FIELD_POLL";
	
	/** The Constant ERROR_GENERAL_SERVER_EXCEPTION. */
	private final static String ERROR_GENERAL_SERVER_EXCEPTION = "ERROR_GENERAL_SERVER_EXCEPTION";
	
	/** The Constant ERROR_MAX_POLLS. */
	private final static String ERROR_MAX_POLLS = "ERROR_MAX_POLLS";
	
	/** The Constant ERROR_SERVER_TIMEOUT_INITIAL. */
	private final static String ERROR_SERVER_TIMEOUT_INITIAL = "ERROR_SERVER_TIMEOUT_INITIAL";
	
	/** The Constant ERROR_SERVER_TIMEOUT_POLL. */
	private final static String ERROR_SERVER_TIMEOUT_POLL = "ERROR_SERVER_TIMEOUT_POLL";

	/** The simulated error. */
	private String simulatedError = "";
	
	/** The maximum polls. */
	private int maximumPolls = 0;
	
	/** The attempt count. */
	private int attemptCount = 0;

	/**
	 * Creates the svc instance.
	 *
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_create_svc_instance", method = RequestMethod.POST)
	public ResponseEntity<String> createSvcInstance(HttpServletRequest request) throws Exception {
		readAndLogRequest("CREATE SERVICE INSTANCE", request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;

		/*
		 * This block of code simulates various errors and would NOT be expected
		 * in a non-test method
		 */
		System.err.println("simulatedError: " + simulatedError);

		if (simulatedError.equals(ERROR_POLICY_EXCEPTION)) {
			return new ResponseEntity<String>(policyExceptionResponse, HttpStatus.OK);
		}
		if (simulatedError.equals(ERROR_SERVICE_EXCEPTION)) {
			return new ResponseEntity<String>(serviceExceptionResponse, HttpStatus.OK);
		}
		if (simulatedError.equals(ERROR_INVALID_FIELD_INITIAL)) {
			/*
			 * Force invalid response field name. Return
			 * "XXXXXrequestReferences" instead of "requestReferences"
			 */
			return new ResponseEntity<String>(acceptResponse.replace("requestReferences", "XXXXXrequestReferences"),
					HttpStatus.OK);
		}

		if (simulatedError.equals(ERROR_GENERAL_SERVER_EXCEPTION)) {
			throw new IOException("an example of an IO exception");
		}

		if (simulatedError.equals(ERROR_SERVER_TIMEOUT_INITIAL)) {
			Thread.sleep(TEST_DELAY_LONG_MSEC);
		}

		if (simulatedError.equals(ERROR_MAX_POLLS)) {
			maximumPolls = MAXIMUM_POLLS_LARGE;
		}

		/*
		 * End of block of simulated error code.
		 */

		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Delete svc instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_delete_svc_instance/{serviceInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteSvcInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			HttpServletRequest request) throws Exception {
		readAndLogRequest("DELETE SERVICE INSTANCE: serviceInstanceId: " + serviceInstanceId, request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Creates the vnf instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_create_vnf_instance/{serviceInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> createVnfInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			HttpServletRequest request) throws Exception {
		readAndLogRequest("CREATE VNF INSTANCE: serviceInstanceId: " + serviceInstanceId, request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Delete vnf instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_delete_vnf_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteVnfInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			@PathVariable("vnfInstanceId") String vnfInstanceId, HttpServletRequest request) throws Exception {
		readAndLogRequest(
				"DELETE VNF INSTANCE: serviceInstanceId: " + serviceInstanceId + " vnfInstanceId: " + vnfInstanceId,
				request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Creates the vf module instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	// /serviceInstances/v2/ff305d54-75b4-431b-adb2-eb6b9e5ff000/vnfs/ff305d54-75b4-ff1b-adb2-eb6b9e5460ff/vfModules
	@RequestMapping(value = "/mso_create_vfmodule_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> createVfModuleInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			@PathVariable("vnfInstanceId") String vnfInstanceId, HttpServletRequest request) throws Exception {
		readAndLogRequest("CREATE VF MODULE INSTANCE: serviceInstanceId: " + serviceInstanceId + " vnfInstanceId: "
				+ vnfInstanceId, request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Delete vf module instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param vfModuleInstanceId the vf module instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	// /serviceInstances/v2/ff305d54-75b4-431b-adb2-eb6b9e5ff000/vnfs/ff305d54-75b4-ff1b-adb2-eb6b9e5460ff/vfModules/ff305d54-75b4-ff1b-bdb2-eb6b9e5460ff
	@RequestMapping(value = "/mso_delete_vfmodule_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}/vfModules/{vfModuleInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteVfModuleInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			@PathVariable("vnfInstanceId") String vnfInstanceId,
			@PathVariable("vfModuleInstanceId") String vfModuleInstanceId, HttpServletRequest request)
			throws Exception {
		readAndLogRequest("DELETE VF MODULE INSTANCE: serviceInstanceId: " + serviceInstanceId + " vnfInstanceId: "
				+ vnfInstanceId + " vfModuleInstanceId: " + vfModuleInstanceId, request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	// POST
	/**
	 * Creates the volume group instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	// /serviceInstances/v2/ff305d54-75b4-431b-adb2-eb6b9e5ff000/volumeGroups
	@RequestMapping(value = "/mso_create_volumegroup_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> createVolumeGroupInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			@PathVariable("vnfInstanceId") String vnfInstanceId, HttpServletRequest request) throws Exception {
		readAndLogRequest("CREATE VOLUME GROUP INSTANCE: seviceInstanceId: " + serviceInstanceId + " vnfInstanceId: "
				+ vnfInstanceId, request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Delete volume group instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param volumeGroupInstanceId the volume group instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	// /serviceInstances/v2/ff305d54-75b4-431b-adb2-eb6b9e5ff000/volumeGroups/ff305d54-75b4-ff1b-cdb2-eb6b9e5460ff
	@RequestMapping(value = "/mso_delete_volumegroup_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}/volumeGroups/{volumeGroupInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteVolumeGroupInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			@PathVariable("vnfInstanceId") String vnfInstanceId,
			@PathVariable("volumeGroupInstanceId") String volumeGroupInstanceId, HttpServletRequest request)
			throws Exception {
		readAndLogRequest("DELETE NW INSTANCE: serviceInstanceId: " + serviceInstanceId + " vnfInstanceId: "
				+ vnfInstanceId + " volumeGroupInstanceId: " + volumeGroupInstanceId, request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Creates the nw instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_create_nw_instance/{serviceInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> createNwInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			HttpServletRequest request) throws Exception {
		readAndLogRequest("CREATE NW INSTANCE: serviceInstanceId: " + serviceInstanceId, request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Delete nw instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param networkInstanceId the network instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_delete_nw_instance/{serviceInstanceId}/networks/{networkInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteNwInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			@PathVariable("networkInstanceId") String networkInstanceId, HttpServletRequest request) throws Exception {
		readAndLogRequest("DELETE NW INSTANCE: serviceInstanceId: " + serviceInstanceId + " networkInstanceId: "
				+ networkInstanceId, request);
		Thread.sleep(TEST_DELAY_SHORT_MSEC);
		maximumPolls = MAXIMUM_POLLS_DEFAULT; // Simulates MSO polling behavior
		attemptCount = 0;
		return new ResponseEntity<String>(acceptResponse, HttpStatus.OK);
	}

	/**
	 * Gets the orchestration request.
	 *
	 * @param requestId the request id
	 * @param request the request
	 * @return the orchestration request
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_get_orch_req/{requestId}", method = RequestMethod.GET)
	public ResponseEntity<String> getOrchestrationRequest(@PathVariable("requestId") String requestId,
			HttpServletRequest request) throws Exception {

		System.err.println("GET ORCHESTRATION REQUEST: requestId: " + requestId);

		/*
		 * This block of code simulates various errors and would NOT be expected
		 * in a non-test method
		 */

		if (simulatedError.equals(ERROR_INVALID_FIELD_POLL)) {
			/*
			 * Force invalid response field name. Return "XXXXXrequestStatus"
			 * instead of "requestStatus"
			 */
			return new ResponseEntity<String>(inProgressResponse.replace("requestStatus", "XXXXXrequestStatus"),
					HttpStatus.OK);
		}

		if (simulatedError.equals(ERROR_POLL_FAILURE)) {
			/*
			 * Force status field with "Failure"
			 */
			return new ResponseEntity<String>(inProgressResponse.replace("InProgress", "Failure"), HttpStatus.OK);
		}

		if (simulatedError.equals(ERROR_SERVER_TIMEOUT_POLL)) {
			Thread.sleep(TEST_DELAY_LONG_MSEC);
		}

		/*
		 * End of block of simulated error code.
		 */

		/*
		 * This logic simulates how MSO might behave ... i.e. return different
		 * results depending on the value of 'maximumPolls'.
		 *
		 */
		int percentProgress = (++attemptCount * 100) / maximumPolls;

		System.err.println("attempts: " + attemptCount + " max: " + maximumPolls + " percent: " + percentProgress);

		String response = inProgressResponse.replace("\"50\"", "\"" + Integer.toString(percentProgress) + "\"");

		if (attemptCount < maximumPolls) {
			if (attemptCount > 1) {
				response = response.replace("vLan setup", "setup step " + Integer.toString(attemptCount));
			}
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(
					response.replace("InProgress", "Complete").replace("vLan setup complete", ""), HttpStatus.OK);
		}
	}

	/**
	 * Gets the orchestration requests.
	 *
	 * @param filterString the filter string
	 * @param request the request
	 * @return the orchestration requests
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_get_orch_reqs/{filterString}", method = RequestMethod.GET)
	public ResponseEntity<String> getOrchestrationRequests(@PathVariable("filterString") String filterString,
			HttpServletRequest request) throws Exception {

		System.err.println("GET ORCHESTRATION REQUESTS: filterString: " + filterString);

		return new ResponseEntity<String>(getOrchestrationRequestsResponse, HttpStatus.OK);

	}

	/*
	 * General purpose exception handler that could be used in application code.
	 * 
	 * The method returns exceptions as error code 500. Both the exception type
	 * and message are written as a JSON object.
	 * 
	 * See the following references:
	 * 
	 * 1) The ExceptionResponse POJO.
	 * 
	 * 2) The "getHttpErrorMessage" function in "utilityService.js" - an example
	 * of how the browser JavaScript code can interpret this response.
	 */

	/**
	 * Exception.
	 *
	 * @param e the e
	 * @param response the response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@ExceptionHandler(Exception.class)
	private void exception(Exception e, HttpServletResponse response) throws IOException {

		/*
		 * This logging step should preferably be replaced with an appropriate
		 * logging method consistent whatever logging mechanism the rest of the
		 * application code uses.
		 */

		e.printStackTrace(System.err);

		response.setContentType("application/json; charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setException(e.getClass().toString().replaceFirst("^.*\\.", ""));
		exceptionResponse.setMessage(e.getMessage());

		response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));

		response.flushBuffer();

	}

	/*
	 * 'readAndLogRequest' only intended to be used for testing.
	 * 
	 * The method reads JSON from the input stream and thus prevents other
	 * mechanisms from reading the input.
	 */

	/**
	 * Read and log request.
	 *
	 * @param label the label
	 * @param request the request
	 * @throws Exception the exception
	 */
	private void readAndLogRequest(String label, HttpServletRequest request) throws Exception {
		String input = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(input, Object.class);

		System.err.println(label + "\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

		/*
		 * Only needed for error simulation ...
		 */
		if (input.matches("^.*modelName.*$")) {
			simulatedError = input.replaceAll("^.*\"modelName\":\"", "").replaceAll("\".*$", "");
		}
	}

	/*
	 * Various test responses:
	 */

	// @formatter:off

	/** The accept response. */
	/*
	 * Sample responses to initial create / delete transaction
	 */
	private String acceptResponse =
	   "{" +
	    "  \"status\": 202," +
	    "  \"entity\": {" +
	    "      \"requestReferences\": {" +
	    "         \"instanceId\": \"bc305d54-75b4-431b-adb2-eb6b9e546014\"," +
	    "         \"requestId\": \"rq1234d1-5a33-55df-13ab-12abad84e331\"" +
	    "      }" +
	    "  }" +
	    "}";
	
	/** The policy exception response. */
	private String policyExceptionResponse = 
		"{" + 
		"  \"status\": 400," + 
		"  \"entity\": { " + 
		"    \"requestError\": {" + 
		"      \"policyException\": {" + 
		"        \"messageId\": \"POL9003\"," + 
		"        \"text\": \"Message content size exceeds the allowable limit\"" + 
		"      }" + 
		"    }" + 
		"  }" + 
		"}";

	/** The service exception response. */
	private String serviceExceptionResponse =
		"{" + 
		"  \"status\": 400," + 
		"  \"entity\": { " + 
		"    \"requestError\": {" + 
		"      \"serviceException\": {" + 
		"        \"messageId\": \"SVC2000\"," + 
		"        \"text\": \"Missing Parameter: %1. Error code is %2\"," + 
		"        \"variables\": [" + 
		"          \"severity\"," + 
		"          \"400\"" + 
		"        ]" + 
		"      }" + 
		"    }" + 
		"  }" + 
		"}" + 
		"";
	
	/** The in progress response. */
	/*
	 * Sample response to subsequent getOrchestrationRequest
	 */
	private String inProgressResponse =
		"{" +
		"   \"status\": 200," +
		"   \"entity\": {" +
		"      \"request\": {" +
		"         \"requestId\": \"rq1234d1-5a33-55df-13ab-12abad84e333\"," +
		"         \"startTime\": \"Thu, 04 Jun 2009 02:51:59 GMT\"," +
		"         \"instanceIds\": {" +
		"            \"serviceInstanceId\": \"bc305d54-75b4-431b-adb2-eb6b9e546014\"" +
		"         }," +
		"         \"requestScope\": \"service\"," +
		"         \"requestType\": \"createInstance\"," +
		"         \"requestDetails\": {" +
		"            \"modelInfo\": {" +
		"               \"modelType\": \"service\"," +
		"               \"modelId\": \"sn5256d1-5a33-55df-13ab-12abad84e764\"," +
		"               \"modelNameVersionId\": \"ab6478e4-ea33-3346-ac12-ab121484a333\"," +
		"               \"modelName\": \"WanBonding\"," +
		"               \"modelVersion\": \"1\"" +
		"            }," +
		"            \"subscriberInfo\": {" +
		"                \"globalSubscriberId\": \"C12345\"," +
		"                \"subscriberName\": \"General Electric Division 12\"" +
		"            }," +
		"            \"requestParameters\": {" +
		"               \"vpnId\": \"1a2b3c4d5e6f\"," +
		"               \"productName\": \"Trinity\"," +
		"               \"customerId\": \"icore9883749\"" +
		"            }" +
		"         }," +
		"         \"requestStatus\": {" +
		"            \"timestamp\": \"Thu, 04 Jun 2009 02:53:39 GMT\"," +
		"            \"requestState\": \"InProgress\"," +
		"            \"statusMessage\": \"vLan setup complete\"," +
		"            \"percentProgress\": \"50\"" +
		"         }" +
		"      }" +
		"   }" +
		"}";
	
	/*
	 * Sample response to subsequent getOrchestrationRequests
	 */
	
	/** The get orchestration requests response. */
	private String getOrchestrationRequestsResponse = 
		"{" +
		"   \"status\": 200," +
		"   \"entity\": {" +
		"      \"requestList\": [" +
		"         {" +
		"            \"request\": {" +
		"               \"requestId\": \"rq1234d1-5a33-55df-13ab-12abad84e333\"," +
		"               \"startTime\": \"Thu, 04 Jun 2009 02:51:59 GMT\"," +
		"               \"finishTime\": \"Thu, 04 Jun 2009 02:55:59 GMT\"," +
		"               \"instanceReferences\": {" +
		"                  \"serviceInstanceId\": \"bc305d54-75b4-431b-adb2-eb6b9e546014\"" +
		"               }," +
		"               \"requestScope\": \"service\"," +
		"               \"requestType\": \"createInstance\"," +
		"               \"requestDetails\": {" +
		"                  \"modelInfo\": {" +
		"                     \"modelType\": \"service\"," +
		"                     \"modelId\": \"sn5256d1-5a33-55df-13ab-12abad84e764\"," +
		"                     \"modelNameVersionId\": \"ab6478e4-ea33-3346-ac12-ab121484a333\"," +
		"                     \"modelName\": \"WanBonding\"," +
		"                     \"modelVersion\": \"1\"" +
		"                  }," +
		"                  \"subscriberInfo\": {" +
		"                      \"globalSubscriberId\": \"C12345\"," +
		"                      \"subscriberName\": \"General Electric Division 12\"" +
		"                  }," +
		"                  \"requestParameters\": {" +
		"                     \"vpnId\": \"1a2b3c4d5e6f\"," +
		"                     \"productName\": \"Trinity\"," +
		"                     \"customerId\": \"icore9883749\"" +
		"                  }" +
		"               }," +
		"               \"requestStatus\": {" +
		"                  \"timestamp\": \"Thu, 04 Jun 2009 02:54:49 GMT\"," +
		"                  \"requestState\": \"complete\"," +
		"                  \"statusMessage\": \"Resource Created\"," +
		"                  \"percentProgress\": \"100\"" +
		"               }" +
		"            }" +
		"         }," +
		"         {" +
		"            \"request\": {" +
		"               \"requestId\": \"rq1234d1-5a33-55df-13ab-12abad84e334\"," +
		"               \"startTime\": \"Thu, 04 Jun 2009 03:52:59 GMT\"," +
		"               \"instanceReferences\": {" +
		"                  \"serviceInstanceId\": \"bc305d54-75b4-431b-adb2-eb6b9e546014\"" +
		"               }," +
		"               \"requestScope\": \"service\"," +
		"               \"requestType\": \"updateInstance\"," +
		"               \"requestDetails\": {" +
		"                  \"modelInfo\": {" +
		"                     \"modelType\": \"service\"," +
		"                     \"modelId\": \"sn5256d1-5a33-55df-13ab-12abad84e764\"," +
		"                     \"modelNameVersionId\": \"ab6478e4-ea33-3346-ac12-ab121484a333\"," +
		"                     \"modelName\": \"WanBonding\"," +
		"                     \"modelVersion\": \"1\"" +
		"                  }," +
		"                  \"subscriberInfo\": {" +
		"                      \"globalSubscriberId\": \"C12345\"," +
		"                      \"subscriberName\": \"General Electric Division 12\"" +
		"                  }," +
		"                  \"requestParameters\": {" +
		"                     \"vpnId\": \"1a2b3c4d5e70\"," +
		"                     \"productName\": \"Trinity\"," +
		"                     \"customerId\": \"icore9883749\"" +
		"                  }" +
		"               }," +
		"               \"requestStatus\": {" +
		"                  \"timestamp\": \"Thu, 04 Jun 2009 03:53:39 GMT\"," +
		"                  \"requestState\": \"InProgress\"," +
		"                  \"statusMessage\": \"vLan setup complete\"," +
		"                  \"percentProgress\": \"50\"" +
		"               }" +
		"            }" +
		"         }" +
		"      ]" +
		"   }" +
		"}";
}
