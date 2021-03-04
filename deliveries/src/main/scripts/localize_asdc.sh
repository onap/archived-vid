#!/bin/bash

FINAL_CONFIG_FILE=$1/conf/asdc.properties
TEMPLATE_CONFIG_FILE=$1/conf/asdc_template.properties

echo "Localizing the ASDC client configuration"

sed -e 's ${ASDC_CLIENT_TYPE} '${ASDC_CLIENT_TYPE}' g' \
	-e 's ${ASDC_CLIENT_REST_HOST} '${ASDC_CLIENT_REST_HOST}' g' \
	-e 's ${ASDC_CLIENT_REST_PORT} '${ASDC_CLIENT_REST_PORT}' g' \
	-e 's ${ASDC_CLIENT_REST_PROTOCOL} '${ASDC_CLIENT_REST_PROTOCOL}' g' \
	-e 's/${ASDC_CLIENT_REST_AUTH}/'"${ASDC_CLIENT_REST_AUTH}"'/g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 4
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."
