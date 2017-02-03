#!/bin/bash

source /tmp/vid/localize_logback.sh || {
	echo "ERROR: Localizing logback.xml failed"
	exit 1
}

source /tmp/vid/localize_portal.sh || {
	echo "ERROR: Localizing portal.properties failed"
	exit 1
}

source /tmp/vid/localize_quartz.sh || {
	echo "ERROR: Localizing quartz.properties failed"
	exit 1
}

source /tmp/vid/localize_system.sh || {
	echo "ERROR: Localizing system.properties failed"
	exit 1
}

source /tmp/vid/localize_cache.sh || {
	echo "ERROR: Localizing cache.ccf failed"
	exit 1
}

source /tmp/vid/localize_asdc.sh || {
	echo "ERROR: Localizing asdc.properties failed"
	exit 1
}
