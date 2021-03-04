#!/bin/bash

FINAL_CONFIG_FILE=$1/classes/portal.properties
TEMPLATE_CONFIG_FILE=$1/classes/portal_template.properties

echo "Localizing the VID portal configuration"

sed -e 's ${VID_WJ_LOGOUT_URL} '${VID_WJ_LOGOUT_URL}' g' \
	-e 's ${VID_ECOMP_REDIRECT_URL} '${VID_ECOMP_REDIRECT_URL}' g' \
	-e 's ${VID_ECOMP_REST_URL} '${VID_ECOMP_REST_URL}' g' \
	-e 's ${VID_UEB_URL_LIST} '${VID_UEB_URL_LIST}' g' \
	-e 's ${VID_ECOMP_PORTAL_INBOX_NAME} '${VID_ECOMP_PORTAL_INBOX_NAME}' g' \
	-e 's ${VID_DECRYPTION_KEY} '${VID_DECRYPTION_KEY}' g' \
	-e 's ${VID_UEB_APP_KEY} '${VID_UEB_APP_KEY}' g' \
	-e 's ${VID_UEB_APP_SECRET} '${VID_UEB_APP_SECRET}' g' \
	-e 's ${VID_UEB_APP_MAILBOX_NAME} '${VID_UEB_APP_MAILBOX_NAME}' g' \
	-e 's ${VID_UEB_LISTENERS_ENABLE} '${VID_UEB_LISTENERS_ENABLE}' g' \
	-e 's/${VID_UEB_CONSUMER_GROUP}/'${VID_UEB_CONSUMER_GROUP}'/g' ${TEMPLATE_CONFIG_FILE} > ${FINAL_CONFIG_FILE} || {
		echo "ERROR: Could not process template file ${TEMPLATE_CONFIG_FILE} into ${FINAL_CONFIG_FILE}"
		exit 4
	}

echo "Localized ${FINAL_CONFIG_FILE} successfully."
