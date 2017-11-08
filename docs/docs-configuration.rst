.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0

Configuration
=============

Configurations .properties files are located under ``WEB-INF/conf/``, while .js files are under ``scripts/constants/``

system.properties file
----------------------

``db.connectionURL``
  The connection URL for the VID database

``db.userName``
  The username for the VID database

``db.password``
  The password for the VID database

``aai.server.url.base``
  Base URL for the A&AI server

``aai.server.url``
  URL for the A&AI server including its version (default: v11)

``aai.vid.username``, ``aai.vid.pass``
  Credentials for accessing A&AI server offered APIs

``mso.server.url``
  Base URL for the SO server

``mso.polling.interval.msecs``
  Interval in milliseconds for polling the SO for the instantiation status

``mso.max.polls``
  Max attempts for polling the SO instantiation status
  
``mso.user.name``,  ``mso.password.x``
  Credentials for accessing the SO server

``scheduler.server.url``
  Base URL for the Scheduler

``scheduler.create.new.vnf.change.instance``, ``scheduler.get.time.slots``, ``scheduler.submit.new.vnf.change``, ``scheduler.get.schedules``
  Scheduler endpoints

asdc.properties file
----------------------
``asdc.client.rest.protocol``
  States the protocol used for accessing SDC: http or https

``asdc.client.rest.host``
  States the hostname of the SDC instance

``asdc.client.rest.port``
  States the port of the SDC instance

``asdc.client.rest.auth``
  Basic authorization string for accessing SDC
  
vidConfiguration.js file
------------------------
``MSO_MAX_POLLS``
  Max attempts for polling the SO instantiation status
  
``MSO_POLLING_INTERVAL_MSECS``
  Interval in milliseconds for polling the SO for the instantiation status
  
``SCHEDULER_POLLING_INTERVAL_MSECS``
  Interval in milliseconds for polling the Scheduler

``SCHEDULER_MAX_POLLS``
  Max attempts for polling the Scheduler
  
``MACRO_SERVICES``
  |  List of all service model invariant UUIDs that need macro instantiation.
  |  Example: ["3cf30cbb-5fe7-4fb3-b049-559a4997b221", "b135a703-bab5-4295-a37f-580a4f2d0961"]
  