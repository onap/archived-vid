#!/bin/bash

FINAL_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/portal.properties
TEMPLATE_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/portal_template.properties
BACKUP1_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/portal.properties.bk.1
BACKUP2_CONFIG_FILE=${ROOT_DIR}/war/WEB-INF/classes/portal.properties.bk.2

echo "Localizing the VID portal configuration"

if [ -z "${VID_ECOMP_REDIRECT_URL}" ]; then
	VID_ECOMP_REDIRECT_URL=https://portal.openecomp.org/ecompportal/ecompportal/process_csp
fi

if [ -z "${VID_WJ_LOGOUT_URL}" ]; then
	VID_WJ_LOGOUT_URL=https://portal.openecomp.org/ecompportal/ecompportal/process_csp
fi

if [ -z "${VID_ECOMP_REST_URL}" ]; then
	VID_ECOMP_REST_URL=https://portal.openecomp.org/ecompportal/auxapi
fi

if [ -z "${VID_UEB_URL_LIST}" ]; then
	VID_UEB_URL_LIST=ueb.openecomp.org
fi

if [ -z "${VID_UEB_CONSUMER_GROUP}" ]; then
	VID_UEB_CONSUMER_GROUP=VID
fi

if [ -z "${VID_ECOMP_PORTAL_INBOX_NAME}" ]; then
	VID_ECOMP_PORTAL_INBOX_NAME=ECOMP-PORTAL-INBOX-DEV-LOCAL
fi

if [ -z "${VID_UEB_APP_KEY}" ]; then
	VID_UEB_APP_KEY=sYH0NJnsKmJC1B2A
fi

if [ -z "${VID_UEB_APP_SECRET}" ]; then
	VID_UEB_APP_SECRET=YOtknsT2wVFz9WISlSPDaAtd
fi

if [ -z "${VID_UEB_APP_MAILBOX_NAME}" ]; then
	VID_UEB_APP_MAILBOX_NAME="ECOMP-PORTAL-OUTBOX-90"
fi

if [ -z "${VID_UEB_LISTENERS_ENABLE}" ]; then
	VID_UEB_LISTENERS_ENABLE="false"
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

sed -e 's ${VID_WJ_LOGOUT_URL} '${VID_WJ_LOGOUT_URL}' g' \
	-e 's ${VID_ECOMP_REDIRECT_URL} '${VID_ECOMP_REDIRECT_URL}' g' \
	-e 's ${VID_ECOMP_REST_URL} '${VID_ECOMP_REST_URL}' g' \
	-e 's ${VID_UEB_URL_LIST} '${VID_UEB_URL_LIST}' g' \
	-e 's ${VID_ECOMP_PORTAL_INBOX_NAME} '${VID_ECOMP_PORTAL_INBOX_NAME}' g' \
	-e 's ${VID_UEB_APP_KEY} '${VID_UEB_APP_KEY}' g' \
	-e 's ${VID_UEB_APP_SECRET} '${VID_UEB_APP_SECRET}' g' \
	-e 's ${VID_UEB_APP_MAILBOX_NAME} '${VID_UEB_APP_MAILBOX_NAME}' g' \
	-e 's ${VID_UEB_LISTENERS_ENABLE} '${VID_UEB_LISTENERS_ENABLE}' g' \
	-e 's/${VID_UEB_CONSUMER_GROUP}/'${VID_UEB_CONSUMER_GROUP}'/g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 4
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."
