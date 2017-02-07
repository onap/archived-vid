#!/bin/bash

#Extract the WAR so it can be customized by the localization script
cd /tmp/vid/stage
jar -xf vid.war

source /tmp/vid/localize_war.sh || {
	echo "ERROR: Localization script failed"
	exit 2
}

#Create the customized WAR and deploy it to Tomcat
mkdir -p /tmp/vid/deployed
cd /tmp/vid/stage
jar -cvf /tmp/vid/deployed/vid.war .
cd
mv -f /tmp/vid/deployed/vid.war /usr/local/tomcat/webapps
catalina.sh run
