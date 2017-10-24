.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0

Configuration
=============

Configurations files are located under ``WEB-INF/conf/``

system.properties file
----------------------

``db.connectionURL``
  The connection URL for VID database

``db.userName``
  The username for VID database

``db.password``
  The password for VID database

``aai.server.url.base``
  Base URL for A&AI server

``aai.server.url``
  URL for A&AI server including its version (default: v11)

``aai.vid.username``, ``aai.vid.pass``
  Credentials for accessing A&AI

``mso.server.url``
  Base URL for SO server

``mso.polling.interval.msecs``
  Interval in milliseconds for polling SO for instantiation status

``mso.max.polls``
  Max polling tryings fo SO instantiation status
  
``mso.user.name``,  ``mso.password.x``
  Credentials for accessing SO

``scheduler.server.url``
  Base URL for Scheduler

``scheduler.create.new.vnf.change.instance``, ``scheduler.get.time.slots``, ``scheduler.submit.new.vnf.change``, ``scheduler.get.schedules``
  Scheduler endpoints

asdc.properties file
----------------------------
``asdc.client.rest.protocol``
  States the protocol for accessing SDC: http or https

``asdc.client.rest.host``
  States the hostname of SDC instance

``asdc.client.rest.port``
  States the port of SDC instance

``asdc.client.rest.auth``
  Basic authorization string to access SDC