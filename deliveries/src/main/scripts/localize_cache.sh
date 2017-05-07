#!/bin/bash

FINAL_CONFIG_FILE=/tmp/vid/stage/WEB-INF/classes/cache.ccf
TEMPLATE_CONFIG_FILE=/tmp/vid/stage/WEB-INF/classes/cache_template.ccf

echo "Localizing the VID cache configuration"

mkdir -p "${CACHE_DIRECTORY}"

sed -e 's,${CACHE_DIRECTORY},'${CACHE_DIRECTORY}',g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 3
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."
