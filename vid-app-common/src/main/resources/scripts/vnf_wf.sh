#!/bin/bash
usage() { echo -e "Usage: $0 [-o <ADD|DELETE|GET>] [-p <FILE_PATH>]\nCSV File Format: VNF_UUID,VNF_invariantUUID,workflowName" 1>&2; exit 1; }
while getopts ":o:p:" opt; do
    case ${opt} in
        o)
            OPERATION=${OPTARG}
            ;;
        p)
            FILE=${OPTARG}
            ;;
        *)
            usage
            ;;
    esac
done
shift $((OPTIND-1))
if [ -z "${FILE}" ] || [ -z "${OPERATION}" ]; then
    usage
fi
if [ ${OPERATION} != 'ADD' ] && [ ${OPERATION} != 'DELETE' ] && [ ${OPERATION} != 'GET' ]; then
   usage
fi
if [ ${OPERATION} = 'ADD' ]; then
	OPERATION='POST'
fi
URL="http://127.0.0.1:8080/vid/change-management/vnf_workflow_relation"
if [ ${OPERATION} = 'GET' ]; then
    echo "Sending request for get all vnf_workflow_relation"
    echo -e "------------------------\nwget output:"
    wget -nv -O "${FILE}" "${URL}"
    RC=$?
    echo "---------------------------"
    if [ $RC -eq 0 ]; then
        echo "Result saved to ${FILE}"
    else
       echo "Failed to get vnf to workflows relations"
    fi
else
    BODY=$(cat ${FILE} | awk '  BEGIN {  FS=","; print " {\"workflowsDetails\" : [ "}   {  gsub(/ /, "", $1) ; gsub(/ /, "", $2) ; gsub(/^[ \t]+/,"",$3); gsub(/[ \t]+$/,"",$3); printf "%s{\"vnfDetails\":{\"UUID\":\"%s\",\"invariantUUID\":\"%s\"},\"workflowName\":\"%s\"}",separator,$1,$2,$3 ;separator = ", ";} END { printf " ]} "}')
    echo "Sending request: ${OPERATION} ${BODY}"
    echo -e "------------------------\nwget output:"
    wget --method="${OPERATION}" --body-data="${BODY}" --header=Content-Type:application/json --content-on-error -nv -O - "${URL}"
    RC=$?
    echo "---------------------------"
    if [ $RC -ne 0 ]; then
       echo "Failed to ADD/DELETE vnf to workflows relations"
    fi
fi

