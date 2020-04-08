.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. _architecture:


VID Architecture
=================
VID provides a well-structured organization of infrastructure deployment, 
instantiation and change-management operations used by Operations to derive orchestrations and change-management.


Scope
------
-  Invoke instantiation of a Service and all of its sub-components (e.g.
   VNF/VF, VNFC/VFC, Modules, Deployment Flavor, etc).
-  Invoke instantiation and creation of cloud logical environment (e.g.
   tenants).
-  Integration with Operations ticketing system for instantiation and
   change-management ordering requests.
-  Instantiation

   -  Instantiation modes.

      -  Macro orchestration.
      -  A la carte orchestration.

   -  Choose the target instantiation environment (e.g. multi-clouds,
      testing environment, etc.).

      -  Check the availability of already-created and reserved cloud
         resources.
      -  Invoke the creations of cloud resources.

   -  Customize Services and VNF/VF to fit a current instantiation.

      -  Based on SDC Design specific assignments.

   -  Feedback on the instantiation process according to the instantiation
      workflow.

      -  Ability to invoke maintenance operations on an unsuccessful
         instantiation.

-   Change-management

   -  Integration with inventory (A&AI) to retrieve currently deployed
      Services.
   -  Agnostic and specific change-management workflows derived from SDC
      Service and VNF/VF models.
   -  

      -  Invoke a CM for a given Service and VNF/VF.
      -  Invoke a CM for monitoring-templates (MTs: control-loops).
      -  Invoke a CM for policy changes
      -  Invoke a CM for license changes
      -  etc..

   -  Ability to perform operations on a workflow (e.g. actions as stop, start,
      restart, resume).
   -  Ability to schedule a workflow

      -  notify
      -  automatic instantiation upon reaching the scheduled time.

-  Invoke security, load and performance tests on a given Service and
   VNF/VF.
-  Collaboration

   -  Project/Admin dashboard (includes user management, VNF/VF and
      Services relevant to the project).

-   Interfaces:

   -  SDC - get models
   -  A&AI - get current deployment (inventory)
   -  MSO - invoke instantiation and change-management.
   -  BYO Scheduler - set/get schedule
