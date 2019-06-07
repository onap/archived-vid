#!/bin/bash

source /tmp/vid/localize_portal.sh $1 || {
	echo "ERROR: Localizing portal.properties failed"
	exit 1
}

source /tmp/vid/localize_quartz.sh $1 || {
	echo "ERROR: Localizing quartz.properties failed"
	exit 1
}

source /tmp/vid/localize_system.sh $1 || {
	echo "ERROR: Localizing system.properties failed"
	exit 1
}

source /tmp/vid/localize_cache.sh $1 || {
	echo "ERROR: Localizing cache.ccf failed"
	exit 1
}

source /tmp/vid/localize_asdc.sh $1 || {
	echo "ERROR: Localizing asdc.properties failed"
	exit 1
}
