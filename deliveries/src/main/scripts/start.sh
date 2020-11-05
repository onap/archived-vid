#!/bin/bash

# Set CATALINA_OPTS if not defined previously
# Enables late-evaluation of env variables, such as VID_KEYSTORE_PASSWORD
: "${CATALINA_OPTS:=-Dvid.keystore.password=${VID_KEYSTORE_PASSWORD} -Dvid.keyalias=vid@vid.onap.org -Dvid.keystore.filename=${VID_KEYSTORE_FILENAME} -Dcom.att.eelf.logging.file=logback.xml -Dcom.att.eelf.logging.path=/tmp/vid/}"
echo "CATALINA_OPTS: ${CATALINA_OPTS}"
export CATALINA_OPTS

catalina.sh run
