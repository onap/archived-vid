#!/bin/bash

fillTemplateProperties() {
  source /tmp/vid/localize_war.sh $1 || {
  	echo "ERROR: Localization script failed"
  	exit 2
  }
}

deployWarOnTomcatManually() {
  cd /usr/local/tomcat/webapps/
  mkdir vid
  cd vid
  jar -xf /tmp/vid/stage/vid.war
}

deployWarOnTomcatManually

TEMPLATES_BASE_DIR=/usr/local/tomcat/webapps/vid/WEB-INF

fillTemplateProperties ${TEMPLATES_BASE_DIR}

catalina.sh run
