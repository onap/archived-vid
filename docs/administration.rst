.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0

Administration
==============

Actions
-------

- |  **Set the list of services to be instantiated by MACRO** 
  |  To set a certain service for the MACRO style instantiation instead of A La Carte, update MACRO_SERVICES in vidConfiguration.js file, with the service model invariant UUID (More info under the Configurations section).
  
- |  **Populate VID Project/Owning entity/Line of Business/Platform drop downs** 
  |  VID administrator has to populate this drop downs with at least one option each. This gets done easily by a POST request. In the following example we add a "Demonstration" option to the Line of Business drop down:
  |
  |  ``curl -X POST 'http://vid.api.simpledemo.onap.org:8080/vid/maintenance/category_parameter/lineOfBusiness' -H 'Accept-Encoding: gzip, deflate' -H 'Content-Type:application/json' -d '{"options":["Demonstration"]}'``
  |
  |  (Replace lineOfBusiness with: platform, project, owningEntity - in order to add Demonstration to all other drop downs as well)
  
- |  **VoLTE E2E services deployment support** 
  |  VID supports VoLTE E2E services deployment. In order to trigger the E2E flow, the service category in the model (as SDC generates it) has to be set to "E2E Service".