.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0

Logging
=======

.. note::
   * This section is used to describe the informational or diagnostic messages emitted from 
     a software component and the methods or collecting them.
   
   * This section is typically: provided for a platform-component and sdk; and
     referenced in developer and user guides
   
   * This note must be removed after content has been added.


Where to Access Information
---------------------------

+------------------------------------------+---------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+
| Location                                 | Type                | Description                                                                                                                                                                               | Rolling             |
+------------------------------------------+---------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+
| /opt/app/vid/logs/vid/application.log    | Jetty server log    | This log describes inner flows inside VID                                                                                                                                                 | the log rolls daily |
+------------------------------------------+---------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+
| /opt/app/vid/logs/vid/audit.log          | application audit   | An audit record is created for some of the operations in VID                                                                                                                              | rolls at 20 mb      |
+------------------------------------------+---------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+
| /opt/app/vid/logs/vid/debug.log          | application logging | We can enable higher logging on demand by editing the logback.xml inside the server docker.                                                                                               | rolls at 20 mb      |
|                                          |                     | The file is located under: WEB-INF/classes/logback.xml.                                                                                                                                   |                     |
|                                          |                     | This log holds the debug and trace level output of the application.                                                                                                                       |                     |
+------------------------------------------+---------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+
| /opt/app/vid/logs/vid/error.log          | application logging | This log holds the info and error level output of the application.                                                                                                                        | rolls at 20 mb      |
+------------------------------------------+---------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------------------+


Error / Warning Messages
------------------------
