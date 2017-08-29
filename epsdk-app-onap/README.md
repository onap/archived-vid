# ECOMP SDK Web Application for Open Source

## Overview

This is a Maven project with the ECOMP SDK web application for public release,
containing files specific to requirements of the open-source version.  This 
project uses the Maven war plugin to copy in ("overlay") the contents of the 
ECOMP SDK web application overlay files distribution at package time.

Use Apache Maven to build, package and deploy this webapp to a web container
like Apache Tomcat.  Eclipse users must install the M2E-WTP connector, see 
https://www.eclipse.org/m2e-wtp/

## Release Notes 

All of the release notes in the epsdk-app-common and epsdk-app-overlay areas apply here!

Build 1.2.6, 23 Mar 2017
- DE270905 Removed total page/current page text boxes from bottom of Profile Search page
- DE273128 Revise left menu to have unique IDs
- DE271378 fixed Camunda Page link in fn_menu; additional links were updated: Notebook
- US847688 Abstraction of support page content for AT&T vs Open Source in SQL Scripts
- Add sample defs/definitions.xml file for partner applications to configure jsp pages
- DE272154 SDK-App ATT: self profile page, changes on drop downs not saved
- Downgrade angularJS from 1.5.0 to 1.4.8

Build 1.2.5, 16 Mar 2017
- DE272709 Update icon names from DS1 to DS2 in database load script
- Upgrade to EPSDK libraries version 1.2.5

Build 1.2.4, 10 Mar 2017
- DE272042 Add missing includes so left menu appears on collaborate and notebook pages
- DE272193 Fix class/icon name to restore role icon on Self Profile page
- Restore spinners by changing class name from icon-spinner to icon-primary-spinner
- DE272202 updated ionicon.css for open source version of report search page
- Removed unused js files from samplePage.html in epsdk-app-onap
- Upgrade to EPSDK libraries version 1.2.4

Build 1.2.3, 8 Mar 2017
- DE271038 Remove references to famous telecommunications company from system.properties
- Upgrade to EPSDK libraries version 1.2.3

Build 1.2.2, 6 Mar 2017
- DE270658 EPSDK-app-onap header is covering part of left menu.
- DE271510 Pages not loading in IE and FF properly. Drop page-resource-ds2.js; load scripts in HTML directly
- Remove references to famous telecommunications company hosts from fusion.properties, portal.properties
- Upgrade to EPSDK libraries version 1.2.2

Build 1.2.1, 2 Mar 2017
- DE269231 Separated footer files on OS module displaying black footer with no content
- US847688 Split database scripts; include full set in distributions
- US847706 Refactored AdminAuthExtension to implement interface expected in OnboardingApiServiceImpl 
- Added class conf/HibernateMappingLocations - moved out of common to apps
- Added file logback.xml to src/main/resources with appropriate logger names
- Extend HibernateMappingLocations with method that returns list of package names to scan
- Remove references to famous telecommunications company hosts from system.properties file
- Upgrade to EPSDK libraries version 1.2.1

Build 1.2.0, 9 Feb 2017
- Initial release
