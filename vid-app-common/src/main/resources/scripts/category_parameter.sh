#!/bin/bash
usage() { echo -e "Usage: $0 [-o <ADD|GET>] [-p <FILE_PATH>] [-c category_name] [-f FAMILY]" 1>&2; exit 1; }
while getopts ":o:p:c:f:" opt; do
    case ${opt} in
        o)
            OPERATION=${OPTARG}
            ;;
        p)
            FILE=${OPTARG}
            ;;
        c)
            CATEGORY=${OPTARG}
            ;;
        f)
            FAMILY=${OPTARG}
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
if [ ${OPERATION} != 'ADD' ] && [ ${OPERATION} != 'GET' ]; then
   usage
fi
if [ ${OPERATION} = 'ADD' ]; then
    if [ -z "${CATEGORY}" ] ; then
        usage
    fi
    OPERATION='POST'
fi
if [ ${OPERATION} = 'GET' ]; then
    if [ -z "${FAMILY}" ] ; then
        usage
    fi
fi
URL="http://127.0.0.1:8080/vid/maintenance/category_parameter"
if [ ${OPERATION} = 'GET' ]; then
    FULLURL="${URL}?familyName=${FAMILY}"
    echo "Sending request: ${FULLURL}"
    echo -e "------------------------\nwget output:"
    wget -nv -O "${FILE}" "${FULLURL}"
    RC=$?
    echo "---------------------------"
    if [ $RC -eq 0 ]; then
        echo "Result saved to ${FILE}"
    else
       echo "Failed to get category parameters list"
    fi
else
    BODY=$(cat ${FILE} | awk '  BEGIN {  ; print " {\"options\" : [ "}   {  gsub(/ /, "", $0) ; printf "%s\"%s\"",separator,$1 ;separator = ", ";} END { printf " ]} "}')
    FULLURL="${URL}/${CATEGORY}"
    echo "Sending request: ${FULLURL} ${OPERATION} ${BODY}"
    echo -e "------------------------\nwget output:"
    wget --method="${OPERATION}" --body-data="${BODY}" --header=Content-Type:application/json --content-on-error -nv -O - "${FULLURL}"
    RC=$?
    echo "---------------------------"
    if [ $RC -ne 0 ]; then
       echo "Failed to ADD options to category ${CATEGORY}"
    fi
fi
