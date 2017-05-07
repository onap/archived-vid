#!/bin/bash

FINAL_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/quartz.properties
TEMPLATE_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/quartz_template.properties
BACKUP1_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/quartz.properties.bk.1
BACKUP2_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/conf/quartz.properties.bk.2

echo "Localizing the VID quartz configuration"

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
	VID_MYSQL_USER=$(id -un)
fi

if [ -z "${VID_MYSQL_MAXCONNECTIONS}" ]; then
	VID_MYSQL_MAXCONNECTIONS=5
fi

if [ -z "${VID_MYSQL_PASS}" ]; then
	echo "Missing required parameter VID_MYSQL_PASS"
	exit 1
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

sed -e 's/${VID_MYSQL_HOST}/'${VID_MYSQL_HOST}'/g' \
	-e 's/${VID_MYSQL_PORT}/'${VID_MYSQL_PORT}'/g' \
	-e 's/${VID_MYSQL_DBNAME}/'${VID_MYSQL_DBNAME}'/g' \
	-e 's/${VID_MYSQL_USER}/'${VID_MYSQL_USER}'/g' \
	-e 's/${VID_MYSQL_PASS}/'${VID_MYSQL_PASS}'/g' \
	-e 's/${VID_MYSQL_MAXCONNECTIONS}/'${VID_MYSQL_MAXCONNECTIONS}'/g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 4
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."


