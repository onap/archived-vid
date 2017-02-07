#!/bin/bash

FINAL_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/cache.ccf
TEMPLATE_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/cache_template.ccf
BACKUP1_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/cache.ccf.bk.1
BACKUP2_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/cache.ccf.bk.2

echo "Localizing the VID cache configuration"

if [ -z "${CACHE_DIRECTORY}" ]; then
	CACHE_DIRECTORY=${ROOT_DIR}/cache
fi

mkdir -p "${CACHE_DIRECTORY}"

#------------------------------------------------------------------------
#- MAKE A BACKUP OF PREVIOUS BACKUP FILE, IF EXISTS
#------------------------------------------------------------------------
if [ -f ${BACKUP1_CONFIG_FILE} ]; then
    cp -f ${BACKUP1_CONFIG_FILE} ${BACKUP2_CONFIG_FILE} || {
        echo "ERROR: Could not copy ${BACKUP1_CONFIG_FILE} to ${BACKUP2_CONFIG_FILE}"
        exit 1
    }
fi
 
#------------------------------------------------------------------------
#- MAKE A BACKUP OF CURRENT FILE, IF EXISTS
#------------------------------------------------------------------------
if [ -f ${FINAL_CONFIG_FILE} ]; then
    cp -f ${FINAL_CONFIG_FILE} ${BACKUP1_CONFIG_FILE} || {
        echo "ERROR: Could not copy ${FINAL_CONFIG_FILE} to ${BACKUP1_CONFIG_FILE}"
        exit 2
    }
fi

sed -e 's,${CACHE_DIRECTORY},'${CACHE_DIRECTORY}',g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 3
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."
