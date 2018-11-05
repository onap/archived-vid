#!/bin/bash

BASE_DIR=/etc/onap/vid/conf.d
FINAL_CONFIG_FILE=${BASE_DIR}/logback.xml
TEMPLATE_CONFIG_FILE=${BASE_DIR}/logback_template.xml

echo "Localizing the VID logback configuration"

mkdir -p "${VID_LOG_DIR}"

sed -e 's/${VID_LOG_LEVEL}/'${VID_LOG_LEVEL}'/g' \
	-e 's,${VID_LOG_DIR},'${VID_LOG_DIR}',g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 3
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."
