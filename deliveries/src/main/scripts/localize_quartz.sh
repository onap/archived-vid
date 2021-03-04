#!/bin/bash

FINAL_CONFIG_FILE=$1/conf/quartz.properties
TEMPLATE_CONFIG_FILE=$1/conf/quartz_template.properties

echo "Localizing the VID quartz configuration"

sed -e 's/${VID_MYSQL_HOST}/'${VID_MYSQL_HOST}'/g' \
	-e 's/${VID_MYSQL_PORT}/'${VID_MYSQL_PORT}'/g' \
	-e 's/${VID_MYSQL_DBNAME}/'${VID_MYSQL_DBNAME}'/g' \
	-e 's/${VID_MYSQL_USER}/'${VID_MYSQL_USER}'/g' \
	-e 's/${VID_MYSQL_PASS}/'"$(printf '%q' "${VID_MYSQL_PASS}")"'/g' \
	-e 's/${VID_MYSQL_MAXCONNECTIONS}/'${VID_MYSQL_MAXCONNECTIONS}'/g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 4
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."


