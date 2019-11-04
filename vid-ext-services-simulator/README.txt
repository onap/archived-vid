VID Simulator
************************************************************************************


************************************************************************************
Motivation:
************************************************************************************
Allow intuitive and extensible framework for mocking REST calls towards VID external peers,
both for dev and testing purposes.



************************************************************************************
Technologies:
************************************************************************************
Spring MVC
MockServer (Apache License 2.0)
http://www.mock-server.com



************************************************************************************
High-level description:
************************************************************************************

The Simulator uses MockServer instance running "under the hood" listening to its own HTTP port.
the Simulator allows to register the expected request and response to the MockServer instance
with an exposed REST call (see details below), and all other requests are automatically redirected to MockServer.
If a request was properly registered, the MockServer will reply with an expected response, which will be in turn
returned by the Simulator to the caller.

The Simulator supports both dynamic and preset (static) registration, looking for JSON files in correct registration format and
registrating them on startup - see details below under "Preset registration"

Note that the behaviour is generic, and no additional code is expected to be added when there are new
MSO/AAI/any other component APIs to be mocked. They will just need to be properly registered using the existing API.


Simulator can be used in both test and dev modes. You can change the server root of any of VID external REST peers 
to the one of the simulator (see details in "Usage"), and either register the expected request/response dynamically or preset it
to be loaded during startup. That's it - you're ready to use the simulator either in dev mode, or in test mode by running integration tests vs VID.


***********************************************************************************
Simulator configuration:
************************************************************************************

Under src/main/resources/:

1) simulator.properties - currently allows to configure the connection details of MockServer, preset registration mode, and other simulator-related props.

2) mockserver.properties - TBD (MockServer logging, SSL etc.)


************************************************************************************
Preset registration:
************************************************************************************

If enabled in the properties, the Simulator will also run preset registration, looking for JSON files in correct registration format and
registrating them on startup. 

The files must be placed under src/main/resources/preset_registration.

If preset registration is enabled, the simulator will run the scheduler "schedulerDetails" API registration, and any other JSON file which it will find under the folder.


get_scheduler_details_short.json:

{
  "simulatorRequest": {
    "method": "GET",
    "path": "/scheduler/v1/ChangeManagement/schedules/scheduleDetails"
  } ,
  "simulatorResponse": {
    "responseCode": 200,
  "body": "[{\"vnfName\":\"ZRDM1MMSC04c53a\",\"status\":\"Pending Schedule\",\"groupId\":\"\",\"policyId\":\"SNIRO.Config_MS_Demo_TimeLimitAndVerticalTopology_zone\",\"scheduleRequest\":{\"id\":1,\"createDateTime\":\"2017-09-06T13:29:43Z\",\"optimizerDateTime\":\"2017-09-06T13:29:55Z\",\"optimizerMessage\":\"\\n{\\n  \\\"requestError\\\": {\\n     \\\"serviceException\\\": {\\n        \\\"messageId\\\": \\\"SVC0001\\\",\\n        \\\"requestId\\\": \\\"CM-c098bd33-a51e-461b-8fd2-6c4d2666c706\\\",\\n        \\\"text\\\": \\\"sniro.operation.exceptions.PolicyNotFoundException: Cannot fetch policy SNIRO.Config_MS_Demo_TimeLimitAndVerticalTopology_zone: : HTTPSConnectionPool(host='policypdp', port=8081): Max retries exceeded with url: \/pdp\/getConfig (Caused by ConnectTimeoutError(<urllib3.connection.VerifiedHTTPSConnection object at 0x7f0ecc00d6d8>, 'Connection to policypdp timed out. (connect timeout=6.5)'))\\\",\\n        \\\"variables\\\": [\\\"severity\\\", 400]\\n     }\\n  }\\n}\",\"optimizerStatus\":\"HTTP Status: 400\",\"optimizerAttemptsToSchedule\":1,\"optimizerTransactionId\":\"08fb4c32-ecb4-4d72-b618-a6156d3fc53a\",\"scheduleId\":\"08fb4c32-ecb4-4d72-b618-a6156d3fc53a\",\"scheduleName\":\"08fb4c32-ecb4-4d72-b618-a6156d3fc53a\",\"status\":\"Schedule Failed\",\"userId\":\"su7376\",\"domain\":\"ChangeManagement\",\"domainData\":[{\"id\":1,\"name\":\"CallbackData\",\"value\":\"{\\\"requestDetails\\\": [{\\\"vnfInstanceId\\\": \\\"Test\\\", \\\"relatedInstanceList\\\": [{\\\"relatedInstance\\\": {\\\"instanceId\\\": \\\"{serviceInstanceId}\\\", \\\"modelInfo\\\": {\\\"modelName\\\": \\\"{parent service model name}\\\", \\\"modelVersion\\\": \\\"2.0\\\", \\\"modelType\\\": \\\"service\\\", \\\"modelInvariantId\\\": \\\"ff3514e3-5a33-55df-13ab-12abad84e7ff\\\", \\\"modelCustomizationName\\\": \\\"vSAMP12 1\\\", \\\"modelVersionId\\\": \\\"9ebb1521-2e74-47a4-aac7-e71a79f73a79\\\", \\\"modelCustomizationId\\\": \\\"c539433a-84a6-4082-a12e-5c9b00c3b960\\\"}}}], \\\"requestParameters\\\": {\\\"usePreload\\\": \\\"True\\\"}, \\\"requestInfo\\\": {\\\"source\\\": \\\"VID\\\", \\\"requestorId\\\": \\\"az2016\\\", \\\"suppressRollback\\\": \\\"False\\\"}, \\\"vnfName\\\": \\\"Name\\\", \\\"modelInfo\\\": {\\\"modelName\\\": \\\"vSAMP12\\\", \\\"modelVersion\\\": \\\"2.0\\\", \\\"modelType\\\": \\\"vnf\\\", \\\"modelInvariantId\\\": \\\"ff5256d1-5a33-55df-13ab-12abad84e7ff\\\", \\\"modelCustomizationName\\\": \\\"vSAMP12 1\\\", \\\"modelVersionId\\\": \\\"254583ad-b38c-498b-bdbd-b8de5e07541b\\\", \\\"modelCustomizationId\\\": \\\"c539433a-84a6-4082-a12e-5c9b00c3b960\\\"}, \\\"cloudConfiguration\\\": {\\\"lcpCloudRegionId\\\": \\\"mdt1\\\", \\\"tenantId\\\": \\\"88a6ca3ee0394ade9403f075db23167e\\\"}}]}\"},{\"id\":2,\"name\":\"WorkflowName\",\"value\":\"Build Software Upgrade for vNFs\"},{\"id\":3,\"name\":\"CallbackUrl\",\"value\":\"http:\/\/127.0.0.1:8989\/scheduler\/v1\/loopbacktest\/vid\"}],\"scheduleApprovals\":[]},\"schedulesId\":0}]"
  }
}


************************************************************************************
Dynamic registration:
*************************************************************************************
If you need dynamic registration, register API for dynamic registration:

POST {protocol}://{Tomcat host}:{Tomcat port}/vidSimulator/registerToVidSimulator

To unregister and clear *all* expectations, use DELETE action:

DELETE {protocol}://{Tomcat host}:{Tomcat port}/vidSimulator/registerToVidSimulator


***********************************************************************************
Registration body JSON specification (see and copy/paste examples below):
***************************************************************************************


   "simulatorRequest" - request wrapper.

          Note that from the below fields, it's mandatory to populate at least one.
          No field is mandatory by itself.

            "id" - String, will be expected as a value in an X-header with a key "x-simulator-id"
            "method" - String, HTTP method of the request.
            "path" - String, relative path of the request, MUST be WITH leading slash and WITHOUT trailing slash.
            "queryParams" - Map<String, List<String>>, query params of key-->list of values.
            "body" - String, body of the request in case of POST/PUT.
                     Note that JSON String should be properly escaped.

    "simulatorResponse" - response wrapper.

           Note that from the below fields, it's mandatory to populate at least "responseCode".

              "responseCode" - integer, HTTP response code.
              "responseHeaders" - Dictionary Object with HTTP headers and values.
              "body" - String, body of the response.
                               Note that JSON String should be properly escaped.
              "file" - String, a filename of the file sitting in "vid-ext-services-simulator\src\main\resources\download_files"
                                Used for simulating file download requests.

    "misc" - optional configurations.
              "numberOfTimes" - Integer. Limit this expectation to fire only a
                                given amount of times. Values like -1 or less are
                                treated as "unlimited". Default is unlimited.
              "replace" - Boolean. If there is already a registered expectation with
                          same simulatorRequest, remove the old registered expectation.
                          If set to 'False' -- the result will be appended to fire
                          after the old registered expectation(s) will fulfill their
                          numberOfTimes. Default is 'True'.

************************************************************************************
Usage:
************************************************************************************

1) In system.properties, change the API you want to mock - set server root to be 
{protocol}://{Tomcat host}:{Tomcat port}/vidSimulator (for example http://localhost:7080/vidSimulator)

Example of mocking the scheduler: 

scheduler.server.url=http://localhost:7080/vidSimulator/scheduler


2) Check the simulator.properties file under /resources to verify the desired properties of the inner MockServer instance.
    * Default MockServer URI is http://localhost:1080

3) Build VID and VID Simulator WARs

4) Deploy the Simulator WAR under Tomcat, either same as VID or another instance.
    * Application context path of the Simulator is /vidSimulator



********************************************************************************
Some more examples of usage with demo requests/responses:
********************************************************************************


********************************************************************************
1) Getting a response by "id" (method and path are insignificant in this case)

********************************************************************************
Registration:
-----------------

    Request:

    POST /vidSimulator/registerToVidSimulator HTTP/1.1
    Host: localhost:7080
    Content-Type: application/json
    Cache-Control: no-cache
    {
      "simulatorRequest": {
            "id": "pavelId"
      } ,
      "simulatorResponse": {
            "responseCode": 200,
            "responseHeaders": {
              "Content-Type": "application/json"
            },
            "body": "{\"value1\": \"kuku\",\"value2\": \"shmuku\"}"
      }
    }


    Response:

    200 OK
    Registration successful!


Running:
--------


    Request:

    GET /vidSimulator/scheduler/testApi HTTP/1.1
    Host: localhost:7080
    Content-Type: application/json
    X-Simulator-Id: pavelId
    Cache-Control: no-cache

    Response:

    200 OK
    {
        "value1": "kuku",
        "value2": "shmuku"
    }


********************************************************************************
2) Getting a response by "id", "method" and "path" - sunny and rainy flows
********************************************************************************
Registration:
-------------

    Request:

    POST /vidSimulator/registerToVidSimulator HTTP/1.1
    Host: localhost:7080
    Content-Type: application/json
    Cache-Control: no-cache

    {
      "simulatorRequest": {
    		"id": "pavelIdGet",
    		"method": "GET",
    		"path": "/scheduler/testApiGet"
      } ,
      "simulatorResponse": {
    		"responseCode": 200,
            "responseHeaders": {
              "Content-Type": "application/json"
            },
    		"body": "{\"value1\": \"kukuResponse\",\"value2\": \"shmukuResponse\"}"
      }
    }


    Response:

    200 OK
    Registration successful!


Running - sunny flow:
---------------------


    Request:

    GET /vidSimulator/scheduler/testApiGet HTTP/1.1
    Host: localhost:7080
    Content-Type: application/json
    X-Simulator-Id: pavelIdGet
    Cache-Control: no-cache

    Response:

    200 OK
    {
         "value1": "kukuResponse",
         "value2": "shmukuResponse"
    }


Running POST - will return 404 since GET method was explicitly registered:
--------------------------------------------------------------------------

    Request:

    POST /vidSimulator/scheduler/testApiGet HTTP/1.1
    Host: localhost:7080
    Content-Type: application/json
    X-Simulator-Id: pavelIdGet
    Cache-Control: no-cache
    {
      "id": "pavelId",
      "responseCode": 200,
      "body": {
        "value1": "kuku",
        "value2": "shmuku"
      }
    }

    Response:

    404 Not Found


********************************************************************************
3) Getting an error HTTP response (based on "id" in this example)
********************************************************************************

Registration:
-------------

    Request:

    POST /vidSimulator/registerToVidSimulator HTTP/1.1
    Host: localhost:7080
    Content-Type: application/json
    Cache-Control: no-cache

    {
      "simulatorRequest": {
            "id": "pavelIdError"
      } ,
      "simulatorResponse": {
            "responseCode": 417
      }
    }

    Response:

    200 OK
    Registration successful!

Running:
-----------

   Request:

   GET /vidSimulator/scheduler/anyApi HTTP/1.1
   Host: localhost:7080
   Content-Type: application/json
   X-Simulator-Id: pavelIdError
   Cache-Control: no-cache


   Response:

   417 Expectation Failed.


********************************************************************************
4) Query params
********************************************************************************

Registration:
-------------

    Request:

    POST /vidSimulator/registerToVidSimulator HTTP/1.1
    Host: localhost:7080
    Content-Type: application/json
    Cache-Control: no-cache
    Postman-Token: 0bbfeb0f-b8b6-368e-6fbd-38a90fc544b4

    {
      "simulatorRequest": {
    	    "method": "GET",
    		"path": "/cloudResourcesRequests/v1",
    		"queryParams": {
    			"requestId" : ["3212b08c-0dcd-4d20-8c84-51e4f325c14a", "3212b08c-0dcd-4d20-8c84-51e4f325c14b"]
    		}
      } ,
      "simulatorResponse": {
    		"responseCode": 200,
    		"body": "{\"requestId1\": \"3212b08c-0dcd-4d20-8c84-51e4f325c14a\",\"requestId2\": \"3212b08c-0dcd-4d20-8c84-51e4f325c14b\"}"
      }
    }

    Response:

    200 OK
    Registration successful!

Running:
-----------

   Request:

  GET /vidSimulator/cloudResourcesRequests/v1?requestId=3212b08c-0dcd-4d20-8c84-51e4f325c14b&amp;requestId=3212b08c-0dcd-4d20-8c84-51e4f325c14a HTTP/1.1
  Host: 127.0.0.1:7080
  Accept: application/json
  Cache-Control: no-cache
  Postman-Token: 9ef5d9d2-77f4-7631-7e9f-4404df10acb6


   Response:

    200 OK
   {"requestId1": "3212b08c-0dcd-4d20-8c84-51e4f325c14a","requestId2": "3212b08c-0dcd-4d20-8c84-51e4f325c14b"}



********************************************************************************
5) File Download
********************************************************************************

Registration:
-------------

    Request:

    POST /vidSimulator/registerToVidSimulator HTTP/1.1
    Host: localhost:7080
    Content-Type: application/json
    Cache-Control: no-cache
    Postman-Token: 0bbfeb0f-b8b6-368e-6fbd-38a90fc544b4

    {
      "simulatorRequest": {
        "method": "GET",
        "path": "/vidSimulator/getSomeFile"
    } ,
      "simulatorResponse": {
        "responseCode": 200,
        "file": "csar3933948645405128424.zip"
      }
    }

    Response:

    200 OK
    Registration successful!

Running:
-----------

   Request:

  GET /vidSimulator/getSomeFile HTTP/1.1
  Host: 127.0.0.1:7080
  Cache-Control: no-cache
  Postman-Token: 9ef5d9d2-77f4-7631-7e9f-4404df10acb6


   Response:

    200 OK
    File for download.
