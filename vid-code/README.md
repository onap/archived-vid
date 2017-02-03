This source repository contains the OpenECOMP Virtual Infrastructure Deployment (VID). The settings file needs to support the standard Maven repositories (e.g. central = http://repo1.maven.org/maven2/), and any proxy settings needed in your environment. In addition, this code is dependent upon the following OpenECOMP artifacts, which are not part of VID:

    org.openecomp.ecompsdkos:ecompSDK-project
    org.openecomp.ecompsdkos:ecompSDK-core
    org.openecomp.ecompsdkos:ecompSDK-analytics
    org.openecomp.ecompsdkos:ecompSDK-workflow

To build it using Maven 3, run: mvn clean install
