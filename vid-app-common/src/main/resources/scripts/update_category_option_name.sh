#!/bin/bash
usage() { echo -e "Usage: $0 [-c category_name] [-i option_id] [-n option_updated_name]" 1>&2; exit 1; }
while getopts ":c:i:n:" opt; do
    case ${opt} in
        i)
            ID=${OPTARG}
            ;;
        n)
            NAME=${OPTARG}
            ;;
        c)
            CATEGORY=${OPTARG}
            ;;
        *)
            usage
            ;;
    esac
done
shift $((OPTIND-1))
if [ -z "${ID}" ] || [ -z "${NAME}" ] || [ -z "${CATEGORY}" ]; then
    usage
fi
URL="http://127.0.0.1:8080/vid/maintenance/category_parameter/"
OPERATION="PUT"
FULLURL="${URL}${CATEGORY}"
BODY="{\"id\":\"${ID}\",\"name\":\"${NAME}\"}"
echo "Sending request: ${FULLURL} ${OPERATION} ${BODY}"
echo -e "------------------------\nwget output:"
wget --method="${OPERATION}" --body-data="${BODY}" --header=Content-Type:application/json --content-on-error -nv -O - "${FULLURL}"
RC=$?
echo "---------------------------"
if [ $RC -ne 0 ]; then
   echo "Failed to update option name ${NAME} for option id ${ID} of category ${CATEGORY}"
fi