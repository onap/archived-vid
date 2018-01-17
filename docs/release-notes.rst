.. This work is licensed under a Creative Commons Attribution 4.0 International License.

VID Release Notes
=================

Version: 1.1.2
--------------

:Release Date: 2018-01-18

**Bug Fixes**

-  [`VID-95`_] - Unable to delete a VFModule
-  [`VID-96`_] - Failure to delete vf-module off VNF in the UI

.. _VID-95: https://jira.onap.org/browse/VID-95
.. _VID-96: https://jira.onap.org/browse/VID-96


Version: 1.1.1
--------------

:Release Date: 2017-11-16


**New Features**

1. Improved TOSCA parser.
2. Change Management - Provides the Operators a single tool for installing and maintaining the services as a self service activity. Provides the ability to schedule and execute change management workflows, Maintenance activities for vNFs that are already installed .
3. PNF Instantiation - PNFs are already installed on the edges of the cloud. In order to configure the PNF, the service needs to be connected to the PNF.

**Epics**

-  [`VID-25`_] - Role management: Global Read only role
-  [`VID-26`_] - Role management: Support VID specific Roles
-  [`VID-27`_] - Change management: Schedule workflow
-  [`VID-28`_] - Change management: Status of workflow
-  [`VID-29`_] - Change management: Manual intervention
-  [`VID-30`_] - PNF instantiation: Trigger PNF service information to SO
-  [`VID-31`_] - PNF Instantiation: support service type PNF & display new fields

.. _VID-25: https://jira.onap.org/browse/VID-25
.. _VID-26: https://jira.onap.org/browse/VID-26
.. _VID-27: https://jira.onap.org/browse/VID-27
.. _VID-28: https://jira.onap.org/browse/VID-28
.. _VID-29: https://jira.onap.org/browse/VID-29
.. _VID-30: https://jira.onap.org/browse/VID-30
.. _VID-31: https://jira.onap.org/browse/VID-31

**Stories**

-  [`VID-44`_] - Implement change management phase 1
-  [`VID-48`_] - Change management: Schedule workflow
-  [`VID-49`_] - Change management: Status of workflow
-  [`VID-50`_] - Change management: Manual intervention
-  [`VID-51`_] - Trigger PNF service information to MSO
-  [`VID-52`_] - support service type PNF & display new fields

.. _VID-44: https://jira.onap.org/browse/VID-44
.. _VID-48: https://jira.onap.org/browse/VID-48
.. _VID-49: https://jira.onap.org/browse/VID-49
.. _VID-50: https://jira.onap.org/browse/VID-50
.. _VID-51: https://jira.onap.org/browse/VID-51
.. _VID-52: https://jira.onap.org/browse/VID-52


**Bug Fixes**

-  [`VID-20`_] - Failed to get service models from ASDC in VID
-  [`VID-59`_] - VID Fails Robot Health Check
-  [`VID-62`_] - VID healthcheck failure in RS ORD ONAP 1.1.0
-  [`VID-64`_] - Got 500 from role/user API call
-  [`VID-79`_] - Not able to create service instance using VID portal
-  [`VID-82`_] - Create VF module with SDNC preload is throwing
   exception


.. _VID-20: https://jira.onap.org/browse/VID-20
.. _VID-59: https://jira.onap.org/browse/VID-59
.. _VID-62: https://jira.onap.org/browse/VID-62
.. _VID-64: https://jira.onap.org/browse/VID-64
.. _VID-79: https://jira.onap.org/browse/VID-79
.. _VID-82: https://jira.onap.org/browse/VID-82



**Known Issues**

- `VID-78 <https://jira.onap.org/browse/VID-78>`_ VID shows the HTML code output whenever the customer list is empty (Cosmetic)

**Upgrade Notes**

A scheduler is needed for the change management feature to work (not included in the Amsterdam release).

**Other**

In order to work properly, VID needs a working instance of SDC, A&AI and SO.

===========

End of Release Notes
