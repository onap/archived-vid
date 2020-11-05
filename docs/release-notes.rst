.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. _release_notes:

VID Release Notes
=================

Version: Guilin (7.0.x)
------------------------

:In a nutshell...:


The following areas have received improvements in Guilin Release

   Pause:
      * Pause/Stop on Failures.
      * Pause Point Addition on Edit window inside the drawing board.
      * Pause Point removal feature.

   Audit Info:
      * Overhauling of the Audit Info screen popup.
      * MSO Status table can now be exported and viewed as .csv files (in Excel and other type software)
      * Audit Info page is also refreshable now. New status from MSO will arrive upon pressing the refresh button.

   VF Module Sequencing and Base Module Related Rules:
     * The sequence of VF Modules can now be changed by dragging up and down in the drawing board.
     * A sequence number box is introduced bearing the current position of the VF Module in the the list.
     * Various rules have been put in place for special check on the Base module in the drawing board.

*Security Issues*

This section is empty.

*Bug fixes*

This section is empty.

**Known Issues**

This section is empty.

**Resolved Issues**

       https://jira.onap.org/browse/VID-862
       https://jira.onap.org/browse/VID-902
       https://jira.onap.org/browse/VID-889
       https://jira.onap.org/browse/VID-875
       https://jira.onap.org/browse/VID-861
       https://jira.onap.org/browse/VID-860
       https://jira.onap.org/browse/VID-858
       https://jira.onap.org/browse/VID-890


**Security Notes**

*Fixed Security Issues*

This section is empty.


*Known Security Issues*

This section is empty.



Version: Frankfurt (6.0.x)
------------------------

:In a nutshell...:

    For an end-user VID is easier to use.

    For a system admin, VID is easier to deploy and manage.

    *Security*

    - Adherence to ONAP Logging Spec v1.2
    - Update to Portal SDK v2.6
    - Use common secret template to generate DB credentials
    - Java 11 migration from JDK 8
    - And more...

    *Bug fixes*

    - Refresh after Add VF Module fails
    - Generates different VF module name during scale out
    - And many more...

**Known Issues**

This section is empty.


**Resolved Issues**

  - `<https://jira.onap.org/browse/VID-365>`__ Upgrade AAI version to v16
  - `<https://jira.onap.org/browse/VID-479>`__ VID Does not refresh after Add VF Module fails
  - `<https://jira.onap.org/browse/VID-659>`__ VID generates always the same VF module name during scale out
  - `<https://jira.onap.org/browse/VID-773>`__ SDNC Preload URL updated to https

  - `<https://jira.onap.org/browse/VID-755>`__ Upgrade direct dependent packages to the latest version
  - `<https://jira.onap.org/browse/VID-498>`__ Java 11 migration from JDK 8
  - `<https://jira.onap.org/browse/VID-471>`__ Update to Portal SDK v2.6


**Security Notes**

*Fixed Security Issues*

This section is empty.


*Known Security Issues*

This section is empty.


Version: El-Alto (5.0.x)
------------------------
**Known Issues**

  - `<https://jira.onap.org/browse/VID-659>`__ VID generates always the same VF module name during scale out

**Resolved Issues**

  - `<https://jira.onap.org/browse/VID-520>`__ Remove "Dissociate" button for Macro-orchestrated PNF resources
  - `<https://jira.onap.org/browse/VID-517>`__ Added liquidBase for follow changes in DB
  - `<https://jira.onap.org/browse/VID-488>`__ Added "Report" popup for common diagnosable cases
  - `<https://jira.onap.org/browse/VID-358>`__ vid-mariadb-galera runs in high-availability mode


**Security Notes**

*Fixed Security Issues*

  - `<https://jira.onap.org/browse/OJSI-119>`__ Closed plain-text HTTP endpoint, port 30238

*Known Security Issues*




Version: 4.3.2
--------------

**Resolved Issues**

Reverted the soft-delete feature, to resolve this:
  - `VID-503 <https://jira.onap.org/browse/VID-503>`_ Delete VF module does not trigger any action


Version: 4.3.0
--------------

:Release Date: 2019-05-25

New Features
------------

-  [`VID-246`_] - *Centralized Representation and Consistent ID of Cloud Regions*
-  [`VID-397`_] - *Change Management - Flexible Designer & Orchestrator*: Utilize SO as a repository of workflows, instead of VID's workflows' configuration

.. _VID-246: /browse/VID-246
.. _VID-397: /browse/VID-397

**Security Notes**

*Fixed Security Issues*

*Known Security Issues*

- In default deployment VID (vid) exposes HTTP port 30238 outside of cluster. [`OJSI-119 <https://jira.onap.org/browse/OJSI-119>`_]

*Known Vulnerabilities in Used Modules*

Version: 3.2.3
--------------

:Release Date: 2019-03-28

**Notes**

It's v3.2.2, but repackaged with a new certificate valid until March 12, 2020.

**Resolved Issues**

VID certificates have been renewed to extend their expiry dates
  - `VID-446 <https://jira.onap.org/browse/VID-446>`_ Fix Certificate Expiry.


Version: 3.2.2
--------------

:Release Date: 2018-11-30

New Features
------------
1. PNF plug & play services instantiation.
2. HTTPS support.
3. Manual vNF Scale Out enhancements.

**Epics**

-  [`VID-159`_] - AAF integration
-  [`VID-194`_] - PNF Use case support
-  [`VID-248`_] - Support scaling use case
-  [`VID-254`_] - Usability
-  [`VID-258`_] - Security

.. _VID-159: /browse/VID-159
.. _VID-194: /browse/VID-194
.. _VID-248: /browse/VID-248
.. _VID-254: /browse/VID-254
.. _VID-258: /browse/VID-258

**Stories**

-  [`VID-166`_] - Improve CSIT coverage
-  [`VID-203`_] - PNF Plug & Play use case with vCPE flow
-  [`VID-236`_] - Align OOM templates
-  [`VID-249`_] - Change Manual Scaling Use Case to use the same SO API that Policy is using in Auto Scaling
-  [`VID-250`_] - Remove Controller Type from the VID GUI and from the request sent to SO
-  [`VID-256`_] - Add tutorials/user guides
-  [`VID-257`_] - AAF integration
-  [`VID-260`_] - Use HTTPS for external interfaces
-  [`VID-261`_] - Use HTTPS for internal interfaces

.. _VID-166: /browse/VID-166
.. _VID-203: /browse/VID-203
.. _VID-236: /browse/VID-236
.. _VID-249: /browse/VID-249
.. _VID-250: /browse/VID-250
.. _VID-256: /browse/VID-256
.. _VID-257: /browse/VID-257
.. _VID-260: /browse/VID-260
.. _VID-261: /browse/VID-261

**Security Notes**

VID code has been formally scanned during build time using NexusIQ and all Critical vulnerabilities have been addressed, items that remain open have been assessed for risk and determined to be false positive. The VID open Critical security vulnerabilities and their risk assessment have been documented as part of the `project <https://wiki.onap.org/pages/viewpage.action?pageId=45300871>`_.

Quick Links:

- `VID project page <https://wiki.onap.org/display/DW/Virtual+Infrastructure+Deployment+Project>`__
- `Passing Badge information for VID <https://bestpractices.coreinfrastructure.org/en/projects/1658>`__
- `Project Vulnerability Review Table for VID <https://wiki.onap.org/pages/viewpage.action?pageId=45300871>`__

**Other**

In order to work properly, VID needs a working instance of SDC, A&AI and SO.

Version: 2.0.0
--------------

:Release Date: 2018-06-07

New Features
------------
1. VoLTE E2E services instantiation.
2. Change Management - invoking vNF In-Place SW Update without a scheduler.
3. Manual vNF Scale Out.

**Epics**

-  [`VID-101`_] - Port Mirroring
-  [`VID-106`_] - Cancel "Pending" workflows
-  [`VID-110`_] - Owning Entity
-  [`VID-114`_] - Preload Automation
-  [`VID-116`_] - Tenant Isolation
-  [`VID-120`_] - Active/Deactivate service type transport
-  [`VID-124`_] - Agnostic vNF In-Place SW Update
-  [`VID-127`_] - Agnostic vNF Configuration Update
-  [`VID-131`_] - Port mirroring - pProbe configuration
-  [`VID-136`_] - Support for pProbes
-  [`VID-139`_] - Refactor Scheduler
-  [`VID-148`_] - Non-Functional requirements - Resiliency
-  [`VID-154`_] - Non-Functional requirements - Stability
-  [`VID-157`_] - Non-Functional requirements - Performance
-  [`VID-158`_] - Non-Functional requirements - Usability
-  [`VID-160`_] - Non-Functional requirements - Scalability
-  [`VID-161`_] - Non-Functional requirements - Security (CII passing badge + 50% test coverage)
-  [`VID-162`_] - OOM integration
-  [`VID-179`_] - Change management - working without scheduler
-  [`VID-180`_] - Support manual scale out
-  [`VID-192`_] - Verify features merged from ECOMP 1802

.. _VID-101: https://jira.onap.org/browse/VID-101
.. _VID-106: https://jira.onap.org/browse/VID-106
.. _VID-110: https://jira.onap.org/browse/VID-110
.. _VID-114: https://jira.onap.org/browse/VID-114
.. _VID-116: https://jira.onap.org/browse/VID-116
.. _VID-120: https://jira.onap.org/browse/VID-120
.. _VID-124: https://jira.onap.org/browse/VID-124
.. _VID-127: https://jira.onap.org/browse/VID-127
.. _VID-131: https://jira.onap.org/browse/VID-131
.. _VID-136: https://jira.onap.org/browse/VID-136
.. _VID-139: https://jira.onap.org/browse/VID-139
.. _VID-148: https://jira.onap.org/browse/VID-148
.. _VID-154: https://jira.onap.org/browse/VID-154
.. _VID-157: https://jira.onap.org/browse/VID-157
.. _VID-158: https://jira.onap.org/browse/VID-158
.. _VID-160: https://jira.onap.org/browse/VID-160
.. _VID-161: https://jira.onap.org/browse/VID-161
.. _VID-162: https://jira.onap.org/browse/VID-162
.. _VID-179: https://jira.onap.org/browse/VID-179
.. _VID-180: https://jira.onap.org/browse/VID-180
.. _VID-192: https://jira.onap.org/browse/VID-192

**Stories**

-  [`VID-16`_] - Replace any remaining openecomp reference by onap
-  [`VID-86`_] - Migrate to org.onap
-  [`VID-102`_] - Create "Port mirror" configuration - Attach Source & collector VNFs
-  [`VID-103`_] - Create "Port mirror" configuration - General Required Fields
-  [`VID-104`_] - Configuration supporting actions
-  [`VID-105`_] - Create "Port mirror" configuration - Get model information
-  [`VID-107`_] - 3rd party simulator
-  [`VID-108`_] - Cancel Scheduled workflows
-  [`VID-109`_] - Change information of columns in the "Pending" section of the "dashboard"
-  [`VID-111`_] - New properties logic
-  [`VID-112`_] - Filter service instance by new properties
-  [`VID-113`_] - Implement new properties & their values
-  [`VID-115`_] - Resume VF- module (after pause)
-  [`VID-117`_] - Create Application ENV
-  [`VID-118`_] - Deploy Services on VID operational
-  [`VID-119`_] - Activate/Deactivate Application ENV
-  [`VID-121`_] - Deactivate a Transport service logic
-  [`VID-122`_] - Service Instance Deactivate - API MSO
-  [`VID-123`_] - "Activate" avilable only for service instance from type= transport
-  [`VID-125`_] - VNF In Place Software Update API - MSO
-  [`VID-126`_] - Support new workflow "Agnostic vNF In-Place SW Update"
-  [`VID-128`_] - Support workflow "vnf Config Update"
-  [`VID-129`_] - Rules For Converting Payload Spreadsheet To JSON
-  [`VID-130`_] - VNF Config Update - API MSO
-  [`VID-132`_] - Get pnf-fromModel-byRegion - A&AI API
-  [`VID-133`_] - pProbe config create request - MSO API
-  [`VID-134`_] - Associate PNF instance to port mirroring configuration by policy
-  [`VID-135`_] - Get Port Mirroring Configuration By Policy Node type
-  [`VID-137`_] - Associate PNF instance to service instance
-  [`VID-138`_] - Disassociate PNF instance from service instance
-  [`VID-140`_] - New function to support Scheduler in widget
-  [`VID-151`_] - ONAP Support
-  [`VID-174`_] - Change management: bypassing scheduler for immediate operations
-  [`VID-185`_] - UI changes for working without scheduler
-  [`VID-186`_] - docker alignment analyzes
-  [`VID-188`_] - UI for Scale Out workflow
-  [`VID-189`_] - VoLTE use case support
-  [`VID-191`_] - Changes to API to SO for Manual scale out use case
-  [`VID-197`_] - Reach 50% unit test coverage
-  [`VID-201`_] - User inteface for invoking upgrade workflow
-  [`VID-202`_] - Verify R1 and R2 features - integration and regression tests
-  [`VID-216`_] - Update ReadTheDocs docs folder

.. _VID-16: https://jira.onap.org/browse/VID-16
.. _VID-86: https://jira.onap.org/browse/VID-86
.. _VID-102: https://jira.onap.org/browse/VID-102
.. _VID-103: https://jira.onap.org/browse/VID-103
.. _VID-104: https://jira.onap.org/browse/VID-104
.. _VID-105: https://jira.onap.org/browse/VID-105
.. _VID-107: https://jira.onap.org/browse/VID-107
.. _VID-108: https://jira.onap.org/browse/VID-108
.. _VID-109: https://jira.onap.org/browse/VID-109
.. _VID-111: https://jira.onap.org/browse/VID-111
.. _VID-112: https://jira.onap.org/browse/VID-112
.. _VID-113: https://jira.onap.org/browse/VID-113
.. _VID-115: https://jira.onap.org/browse/VID-115
.. _VID-117: https://jira.onap.org/browse/VID-117
.. _VID-118: https://jira.onap.org/browse/VID-118
.. _VID-119: https://jira.onap.org/browse/VID-119
.. _VID-121: https://jira.onap.org/browse/VID-121
.. _VID-122: https://jira.onap.org/browse/VID-122
.. _VID-123: https://jira.onap.org/browse/VID-123
.. _VID-125: https://jira.onap.org/browse/VID-125
.. _VID-126: https://jira.onap.org/browse/VID-126
.. _VID-128: https://jira.onap.org/browse/VID-128
.. _VID-129: https://jira.onap.org/browse/VID-129
.. _VID-130: https://jira.onap.org/browse/VID-130
.. _VID-132: https://jira.onap.org/browse/VID-132
.. _VID-133: https://jira.onap.org/browse/VID-133
.. _VID-134: https://jira.onap.org/browse/VID-134
.. _VID-135: https://jira.onap.org/browse/VID-135
.. _VID-137: https://jira.onap.org/browse/VID-137
.. _VID-138: https://jira.onap.org/browse/VID-138
.. _VID-140: https://jira.onap.org/browse/VID-140
.. _VID-151: https://jira.onap.org/browse/VID-151
.. _VID-174: https://jira.onap.org/browse/VID-174
.. _VID-185: https://jira.onap.org/browse/VID-185
.. _VID-186: https://jira.onap.org/browse/VID-186
.. _VID-188: https://jira.onap.org/browse/VID-188
.. _VID-189: https://jira.onap.org/browse/VID-189
.. _VID-191: https://jira.onap.org/browse/VID-191
.. _VID-197: https://jira.onap.org/browse/VID-197
.. _VID-201: https://jira.onap.org/browse/VID-201
.. _VID-202: https://jira.onap.org/browse/VID-202
.. _VID-216: https://jira.onap.org/browse/VID-216

**Security Notes**

VID code has been formally scanned during build time using NexusIQ and all Critical vulnerabilities have been addressed, items that remain open have been assessed for risk and determined to be false positive. The VID open Critical security vulnerabilities and their risk assessment have been documented as part of the `project <https://wiki.onap.org/pages/viewpage.action?pageId=28378623>`__.

Quick Links:

- `VID project page <https://wiki.onap.org/display/DW/Virtual+Infrastructure+Deployment+Project>`_
- `Passing Badge information for VID <https://bestpractices.coreinfrastructure.org/en/projects/1658>`_
- `Project Vulnerability Review Table for VID <https://wiki.onap.org/pages/viewpage.action?pageId=28378623>`_

**Other**

In order to work properly, VID needs a working instance of SDC, A&AI and SO.

Version: 1.1.1
--------------

:Release Date: 2017-11-16

New Features
------------

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


Bug Fixes
----------

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

End of Release Notes
