#!/bin/bash

FINAL_CONFIG_FILE=$1/conf/log4j.properties
TEMPLATE_CONFIG_FILE=$1/conf/log4j_template.properties

echo "Localizing the VID log4j configuration"

mkdir -p ${LOG4J_LOGS_DIRECTORY}

sed -e 's/${LOG4J_LOGLEVEL}/'${LOG4J_LOGLEVEL}'/g' \
	-e 's/${LOG4J_APPENDER}/'${LOG4J_APPENDER}'/g' \
	-e 's,${LOG4J_LOGS_DIRECTORY},'${LOG4J_LOGS_DIRECTORY}',g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 3
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."


