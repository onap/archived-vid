.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0

Installation
============

VID is delivered in a Docker image format.

Installing VID Using a Docker Image
-----------------------------------

Please follow the instructions given below, for installing VID using a Docker image.

1. Download the vid & mariadb Docker image

.. code-block:: bash

  docker pull mariadb:10
  docker login -u docker -p docker nexus3.onap.org:10001
  docker pull nexus3.onap.org:10001/onap/vid:7.0.0

2. Install by running following command (Use the path for the lf_config folder under the VID git repository as CONFIG_PATH and path for .jks files for CERTS_PATH, usually epsdk-app-onap/src/main/webapp/WEB-INF/cert )

.. code-block:: bash

   #start Maria-DB
   docker run --name vid-mariadb -e MYSQL_DATABASE=vid_openecomp_epsdk -e MYSQL_USER=vidadmin -e MYSQL_PASSWORD=YOUR_PASSWORD -e MYSQL_ROOT_PASSWORD=ROOT_PASSWORD -v CONFIG_PATH/vid-my.cnf:/etc/mysql/my.cnf -v /var/lib/mysql -d mariadb:10
   
   #start VID server
   docker run -e VID_MYSQL_DBNAME=vid_openecomp_epsdk -e VID_MYSQL_PASS=YOUR_PASSWORD -v CERTS_PATH:/opt/app/vid/etc --name vid-server -p 8080:8080 --link vid-mariadb:vid-mariadb-docker-instance -d nexus3.onap.org:10001/onap/vid:7.0.0

Or use docker-compose:

.. code-block:: bash

   docker-compose up
