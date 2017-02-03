#!/bin/bash

FINAL_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/logback.xml
TEMPLATE_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/logback_template.xml
BACKUP1_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/logback.xml.bk.1
BACKUP2_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/logback.xml.bk.2

echo "Localizing the VID logback configuration"

if [ -z "${VID_LOG_DIR}" ]; then
	VID_LOG_DIR="${ROOT_DIR}/logs"
fi

if [ -z "${VID_LOG_LEVEL}" ]; then
	VID_LOG_LEVEL="INFO"
fi

mkdir -p "${VID_LOG_DIR}"

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

sed -e 's/${VID_LOG_LEVEL}/'${VID_LOG_LEVEL}'/g' \
	-e 's,${VID_LOG_DIR},'${VID_LOG_DIR}',g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 3
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."
