This source repository contains the OpenECOMP Virtual Infrastructure Deployment (VID). The settings file needs to support the standard Maven repositories (e.g. central = http://repo1.maven.org/maven2/), and any proxy settings needed in your environment. In addition, this code is dependent upon the following OpenECOMP artifacts, which are not part of VID:

    org.openecomp.ecompsdkos:ecompSDK-project
    org.openecomp.ecompsdkos:ecompSDK-core
    org.openecomp.ecompsdkos:ecompSDK-analytics
    org.openecomp.ecompsdkos:ecompSDK-workflow

# Prerequisites
   * Java 8
   * Maven 3
   * Docker

# Basic info
To build project, run: mvn clean install

To build docker images, run: mvn clean install -P docker 

# How to start VID locally?
1. Build a vid-ext-services-simulator docker image
   
    Go to the vid-ext-service-simulator folder and run
    ```
    mvn clean package
    ```
2. Build a vid docker image

    In the vid folder run
    ```
    mvn clean install -P docker
    ```
3. Start a vid with db and vid-ext-service-simulator

    Go to the vid/deliveres/src/main/docker/docker-files and run
    ```.env
    docker-compose up -d
   ```
4. Open a web browser and go to the http://localhost:8080/vid/login.htm page
   
   Login and password you can find in a 'fn_user' table inited by 
   vid/epsdk-app-onap/src/main/resources/db.changelog-01.sql script.
