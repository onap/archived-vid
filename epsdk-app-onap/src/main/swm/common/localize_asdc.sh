#!/bin/bash

FINAL_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/asdc.properties
TEMPLATE_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/asdc_template.properties
BACKUP1_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/asdc.properties.bk.1
BACKUP2_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/asdc.properties.bk.2

echo "Localizing the SDC client configuration"

if [ -z "${ASDC_CLIENT_TYPE}" ]; then
	ASDC_CLIENT_TYPE=REST
fi

if [ "${ASDC_CLIENT_TYPE}" = "REST" ]; then
	if [ -z "${ASDC_CLIENT_REST_HOST}" ]; then
		echo 'ASDC_CLIENT_REST_HOST must be provided when ASDC_CLIENT_TYPE=REST'
		exit 1
	fi

	if [ -z "${ASDC_CLIENT_REST_AUTH}" ]; then
		echo 'ASDC_CLIENT_REST_AUTH must be provided when ASDC_CLIENT_TYPE=REST'
		exit 2
	fi

	if [ -z "${ASDC_CLIENT_REST_PROTOCOL}" ]; then
		ASDC_CLIENT_REST_PROTOCOL=http
	fi

	if [ -z "${ASDC_CLIENT_REST_PORT}" ]; then
		ASDC_CLIENT_REST_PORT=8080
	fi
else
	ASDC_CLIENT_REST_HOST=""
	ASDC_CLIENT_REST_AUTH=""
	ASDC_CLIENT_REST_PROTOCOL="http"
	ASDC_CLIENT_REST_PORT="8080"
fi

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

sed -e 's ${ASDC_CLIENT_TYPE} '${ASDC_CLIENT_TYPE}' g' \
	-e 's ${ASDC_CLIENT_REST_HOST} '${ASDC_CLIENT_REST_HOST}' g' \
	-e 's ${ASDC_CLIENT_REST_PORT} '${ASDC_CLIENT_REST_PORT}' g' \
	-e 's ${ASDC_CLIENT_REST_PROTOCOL} '${ASDC_CLIENT_REST_PROTOCOL}' g' \
	-e 's/${ASDC_CLIENT_REST_AUTH}/'"${ASDC_CLIENT_REST_AUTH}"'/g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 4
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."
