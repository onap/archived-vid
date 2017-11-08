.. This work is licensed under a Creative Commons Attribution 4.0 International License.

Release Notes
=============

Version: 1.1.0
--------------


:Release Date: 2017-11-16



**New Features**

1. Improved TOSCA parser.
2. Change Management - Provides the Operators a single tool for installing and maintaining the services as a self service activity. Provides the ability to schedule and execute change management workflows, Maintenance activities for vNFs that are already installed .
3. PNF - PNFs are already installed on the edges of the cloud. In order to configure the PNF, the service needs to be connected to the PNF.


**Known Issues**
	- `VID-78 <https://jira.onap.org/browse/VID-78>`_ 
	  VID shows the HTML code output whenever the customer list is empty (Cosmetic)

**Upgrade Notes**
A scheduler is needed for the change management feature to work (not included in the Amsterdam release).

**Other**
In order to work properly, VID needs a working instance of SDC, A&AI and SO.

===========

End of Release Notes
