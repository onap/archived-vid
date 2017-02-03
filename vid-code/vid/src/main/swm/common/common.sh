#!/bin/bash

if [[ "${INSTALL_ROOT}" = */ ]]; then
	export ROOT_DIR=${INSTALL_ROOT}opt/app/vid/portal
else
	export ROOT_DIR=${INSTALL_ROOT}/opt/app/vid/portal
fi

export JAVA_HOME=/opt/app/java/jdk/jdk180


OS=`uname`

if [ "${OS}" = "SunOS" ]; then
    CURRENT_USER=`/usr/xpg4/bin/id -un`
    CURRENT_GROUP=`/usr/xpg4/bin/id -gn`
else
    CURRENT_USER=`id -un`
    CURRENT_GROUP=`id -gn`
fi

export CURRENT_USER CURRENT_GROUP
export TOMCAT_HOME=/opt/app/vid/tomcat

if [ -z "${VID_ENDPOINT_NAME}" ]; then
	VID_ENDPOINT_NAME="vid"
fi

# Fail - used to quickly exit with a rc and error message
fail() {
    rc=$1
    shift;
    echo "ERROR: $@"
    exit $rc
}
