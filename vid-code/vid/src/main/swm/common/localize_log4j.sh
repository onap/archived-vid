#!/bin/bash

FINAL_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/log4j.properties
TEMPLATE_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/log4j_template.properties
BACKUP1_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/log4j.properties.bk.1
BACKUP2_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/log4j.properties.bk.2

echo "Localizing the VID log4j configuration"

if [ -z "${LOG4J_LOGLEVEL}" ]; then
	LOG4J_LOGLEVEL=INFO
fi


if [ -z "${LOG4J_APPENDER}" ]; then
	LOG4J_APPENDER=rollingfile
fi

if [ -z "${LOG4J_LOGS_DIRECTORY}" ]; then
	LOG4J_LOGS_DIRECTORY=${ROOT_DIR}/logs
fi

mkdir -p ${LOG4J_LOGS_DIRECTORY}

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

sed -e 's/${LOG4J_LOGLEVEL}/'${LOG4J_LOGLEVEL}'/g' \
	-e 's/${LOG4J_APPENDER}/'${LOG4J_APPENDER}'/g' \
	-e 's,${LOG4J_LOGS_DIRECTORY},'${LOG4J_LOGS_DIRECTORY}',g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 3
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."


