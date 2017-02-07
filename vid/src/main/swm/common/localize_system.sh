#!/bin/bash

FINAL_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/system.properties
TEMPLATE_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/system_template.properties
BACKUP1_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/system.properties.bk.1
BACKUP2_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/system.properties.bk.2

echo "Localizing the VID system configuration"

if [ -z "${VID_MYSQL_HOST}" ]; then
	VID_MYSQL_HOST=localhost
fi

if [ -z "${VID_MYSQL_PORT}" ]; then
	VID_MYSQL_PORT=3306
fi

if [ -z "${VID_MYSQL_DBNAME}" ]; then
	VID_MYSQL_DBNAME=vid_portal
fi

if [ -z "${VID_MYSQL_USER}" ]; then
	VID_MYSQL_USER=vid_admin
fi

if [ -z "${VID_MYSQL_MAXCONNECTIONS}" ]; then
	VID_MYSQL_MAXCONNECTIONS=5
fi

if [ -z "${VID_AAI_HOST}" ]; then
	VID_AAI_HOST=aai.api.openecomp.org
fi

if [ -z "${VID_AAI_PORT}" ]; then
	VID_AAI_PORT=8443
fi

if [ -z "${VID_APP_DISPLAY_NAME}" ]; then
	VID_APP_DISPLAY_NAME=VID
fi

if [ -z "${VID_ECOMP_SHARED_CONTEXT_REST_URL}" ]; then
	VID_ECOMP_SHARED_CONTEXT_REST_URL="https://portal.openecomp.org:8080/ecompportal/context"
fi

if [ -z "${VID_ECOMP_REDIRECT_URL}" ]; then
	VID_ECOMP_REDIRECT_URL=https://portal.openecomp.org/ecompportal/ecompportal/process_csp
fi

if [ -z "${VID_ECOMP_REST_URL}" ]; then
	VID_ECOMP_REST_URL=https://portal.openecomp.org/ecompportal/auxapi
fi

if [ -z "${VID_MSO_SERVER_URL}" ]; then
	VID_MSO_SERVER_URL=https://mso.api.openecomp.org:8443
fi

if [ -z "${VID_TRUSTSTORE_FILE}" ]; then
	VID_TRUSTSTORE_FILE=${ROOT_DIR}/etc/vid_keystore.jks
fi

if [ -z "${VID_MYLOGIN_FEED_DIRECTORY}" ]; then
	VID_MYLOGIN_FEED_DIRECTORY=/tmp/MyLogins
fi

if [ -z "${VID_TRUSTSTORE_PASS}" ]; then
	echo "ERROR: Missing required parameter VID_TRUSTSTORE_PASS"
	exit 1
fi

if [ -z "${VID_MSO_USER}" ]; then
	echo "ERROR: Missing required parameter VID_MSO_USER"
	exit 1
fi

if [ -z "${VID_MSO_PASS}" ]; then
	echo "ERROR: Missing required parameter VID_MSO_PASS";
	exit 1
fi

if [ -z "${VID_MYSQL_PASS}" ]; then
	echo "ERROR: Missing required parameter VID_MYSQL_PASS"
	exit 1
fi

if [ -z "${MSO_DME2_CLIENT_TIMEOUT}" ]; then
	echo "ERROR: Missing required parameter MSO_DME2_CLIENT_TIMEOUT"
	exit 1
fi

if [ -z "${MSO_DME2_CLIENT_READ_TIMEOUT}" ]; then
	echo "ERROR: Missing required parameter MSO_DME2_CLIENT_READ_TIMEOUT"
	exit 1
fi

if [ -z "${MSO_DME2_SERVER_URL}" ]; then
	echo "ERROR: Missing required parameter MSO_DME2_SERVER_URL"
	exit 1
fi
if [ -z "${MSO_DME2_ENABLED}" ]; then
	echo "ERROR: Missing required parameter MSO_DME2_ENABLED"
	exit 1
fi
if [ -z "${MSO_POLLING_INTERVAL_MSECS}" ]; then
	MSO_POLLING_INTERVAL_MSECS=10000
fi

if [ -z "${AAI_TRUSTSTORE_FILENAME}" ]; then
	AAI_TRUSTSTORE_FILENAME=tomcat_keystore
fi

if [ -z "${AAI_TRUSTSTORE_PASSWD_X}" ]; then
	AAI_TRUSTSTORE_PASSWD_X=70c87528c88dcd9f9c2558d30e817868
fi

if [ -z "${AAI_KEYSTORE_FILENAME}" ]; then
	AAI_KEYSTORE_FILENAME=aai-client-cert.p12
fi

if [ -z "${AAI_KEYSTORE_PASSWD_X}" ]; then
	AAI_KEYSTORE_PASSWD_X=70c87528c88dcd9f9c2558d30e817868
fi

mkdir -p "${VID_MYLOGIN_FEED_DIRECTORY}"

#------------------------------------------------------------------------
#- MAKE A BACKUP OF PREVIOUS BACKUP FILE, IF EXISTS
#------------------------------------------------------------------------
if [ -f ${BACKUP1_CONFIG_FILE} ]; then
    cp -f ${BACKUP1_CONFIG_FILE} ${BACKUP2_CONFIG_FILE} || {
        echo "ERROR: Could not copy ${BACKUP1_CONFIG_FILE} to ${BACKUP2_CONFIG_FILE}"
        exit 2
    }
fi
 
#------------------------------------------------------------------------
#- MAKE A BACKUP OF CURRENT FILE, IF EXISTS
#------------------------------------------------------------------------
if [ -f ${FINAL_CONFIG_FILE} ]; then
    cp -f ${FINAL_CONFIG_FILE} ${BACKUP1_CONFIG_FILE} || {
        echo "ERROR: Could not copy ${FINAL_CONFIG_FILE} to ${BACKUP1_CONFIG_FILE}"
        exit 3
    }
fi

sed -e 's/${VID_MYSQL_HOST}/'${VID_MYSQL_HOST}'/g' \
	-e 's/${VID_MYSQL_PORT}/'${VID_MYSQL_PORT}'/g' \
	-e 's/${VID_MYSQL_DBNAME}/'${VID_MYSQL_DBNAME}'/g' \
	-e 's/${VID_MYSQL_USER}/'${VID_MYSQL_USER}'/g' \
	-e 's/${VID_MYSQL_PASS}/'${VID_MYSQL_PASS}'/g' \
	-e 's/${VID_AAI_HOST}/'${VID_AAI_HOST}'/g' \
	-e 's/${VID_AAI_PORT}/'${VID_AAI_PORT}'/g' \
	-e 's,${AAI_TRUSTSTORE_FILENAME},'${AAI_TRUSTSTORE_FILENAME}',g' \
	-e 's/${AAI_TRUSTSTORE_PASSWD_X}/'${AAI_TRUSTSTORE_PASSWD_X}'/g' \
	-e 's,${AAI_KEYSTORE_FILENAME},'${AAI_KEYSTORE_FILENAME}',g' \
	-e 's/${AAI_KEYSTORE_PASSWD_X}/'${AAI_KEYSTORE_PASSWD_X}'/g' \
	-e 's/${VID_APP_DISPLAY_NAME}/'${VID_APP_DISPLAY_NAME}'/g' \
	-e 's ${VID_ECOMP_SHARED_CONTEXT_REST_URL} '${VID_ECOMP_SHARED_CONTEXT_REST_URL}' g' \
	-e 's ${VID_ECOMP_REDIRECT_URL} '${VID_ECOMP_REDIRECT_URL}' g' \
	-e 's ${VID_ECOMP_REST_URL} '${VID_ECOMP_REST_URL}' g' \
	-e 's ${VID_MSO_SERVER_URL} '${VID_MSO_SERVER_URL}' g' \
	-e 's/${VID_MSO_USER}/'${VID_MSO_USER}'/g' \
	-e 's/${VID_MSO_PASS}/'${VID_MSO_PASS}'/g' \
	-e 's,${VID_MYLOGIN_FEED_DIRECTORY},'${VID_MYLOGIN_FEED_DIRECTORY}',g' \
	-e 's,${MSO_DME2_CLIENT_TIMEOUT},'${MSO_DME2_CLIENT_TIMEOUT}',g' \
	-e 's,${MSO_DME2_CLIENT_READ_TIMEOUT},'${MSO_DME2_CLIENT_READ_TIMEOUT}',g' \
	-e 's,${MSO_DME2_SERVER_URL},'${MSO_DME2_SERVER_URL}',g' \
	-e 's,${MSO_DME2_ENABLED},'${MSO_DME2_ENABLED}',g' \
	-e 's,${MSO_POLLING_INTERVAL_MSECS},'${MSO_POLLING_INTERVAL_MSECS}',g' \
	-e 's,${VID_TRUSTSTORE_FILE},'${VID_TRUSTSTORE_FILE}',g' \
	-e 's/${VID_TRUSTSTORE_PASS}/'${VID_TRUSTSTORE_PASS}'/g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 4
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."


