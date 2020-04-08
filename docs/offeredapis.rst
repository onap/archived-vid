.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. _offeredapis:


Offered APIs
=================

+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
| Entity              | Method   | Path Info                                             | Description                                                                              |
+=====================+==========+=======================================================+==========================================================================================+
| Health Check        | GET      | /healthCheck                                          | The Health Status of the application checks the DB connection.                           |
+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
|                     |          |                                                       | | Used by IDNS for redundancy                                                            |
|                     |          |                                                       | |                                                                                        |
|                     |          |                                                       | | return response entity:                                                                |
|                     |          |                                                       | | - statusCode Either 200 or 500                                                         |
|                     |          |                                                       | | - detailedMsg of the result, in case of failure particular error message               |
|                     |          |                                                       | |                                                                                        |
|                     |          |                                                       | | Expected:                                                                              |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       | .. code-block:: javascript                                                               |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       |     {                                                                                    |
|                     |          |                                                       |       "statusCode": 200,                                                                 |
|                     |          |                                                       |       "detailedMsg": "health check succeeded",                                           |
|                     |          |                                                       |       "date": current date                                                               |
|                     |          |                                                       |     }                                                                                    |
|                     |          |                                                       |                                                                                          |
+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
| Health Check        | GET      | rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}     | The Health Status of the application checks the DB connection                            |
+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
|                     |          |                                                       | | return response entity:                                                                |
|                     |          |                                                       | | - statusCode Either 200 or 500                                                         |
|                     |          |                                                       | | - detailedMsg of the result, in case of failure particular error message               |
|                     |          |                                                       | | - date string indicating the current date & time                                       |
|                     |          |                                                       | |                                                                                        |
|                     |          |                                                       | | Expected:                                                                              |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       | .. code-block:: javascript                                                               |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       |    {                                                                                     |
|                     |          |                                                       |      "statusCode": 200,                                                                  |
|                     |          |                                                       |      "detailedMsg": "health check succeeded",                                            |
|                     |          |                                                       |      "date": current date                                                                |
|                     |          |                                                       |    }                                                                                     |
|                     |          |                                                       |                                                                                          |
+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
| Commit Version      | GET      | /commitInfo                                           | Displays info about the last commit of the running build                                 |
+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
|                     |          |                                                       | | return response entity:                                                                |
|                     |          |                                                       | | - commitId full id of the commit                                                       |
|                     |          |                                                       | | - commitMessageShort short message from the commit                                     |
|                     |          |                                                       | | - commitTime time of the commit                                                        |
|                     |          |                                                       | |                                                                                        |
|                     |          |                                                       | | Expected:                                                                              |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       | .. code-block:: javascript                                                               |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       |    {                                                                                     |
|                     |          |                                                       |      "commitId": id of the last commit                                                   |
|                     |          |                                                       |      "commitMessageShort": short message of the last commit                              |
|                     |          |                                                       |      "commitTime": time of the last commit                                               |
|                     |          |                                                       |    }                                                                                     |
|                     |          |                                                       |                                                                                          |
+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+


.. _vid-maintenance-apis:

Maintenance APIs
------------------

+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
| Maintenance:        | POST     | /maintenance/category_parameter/{categoryName}        | Populate VID Project/Owning entity/Line of Business/Platform drop downs                  |
| Category Parameters |          |                                                       |                                                                                          |
+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
|                     |          |                                                       | ``{categoryName}`` is one of lineOfBusiness, platform, project, owningEntity.            |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       | POST body:                                                                               |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       | .. code-block:: javascript                                                               |
|                     |          |                                                       |                                                                                          |
|                     |          |                                                       |    {                                                                                     |
|                     |          |                                                       |      "options": [ <list of strings> ]                                                    |
|                     |          |                                                       |    }                                                                                     |
|                     |          |                                                       |                                                                                          |
+---------------------+----------+-------------------------------------------------------+------------------------------------------------------------------------------------------+
