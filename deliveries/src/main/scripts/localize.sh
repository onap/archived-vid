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

# Set CATALINA_OPTS if not defined previously
# Enables late-evaluation of env variables, such as VID_KEYSTORE_PASSWORD
: "${CATALINA_OPTS:=-Dvid.keystore.password=${VID_KEYSTORE_PASSWORD} -Dvid.keyalias=vid@vid.onap.org -Dvid.keystore.filename=${VID_KEYSTORE_FILENAME} -Dcom.att.eelf.logging.file=logback.xml -Dcom.att.eelf.logging.path=/tmp}"
echo "CATALINA_OPTS: ${CATALINA_OPTS}"
export CATALINA_OPTS

catalina.sh run
