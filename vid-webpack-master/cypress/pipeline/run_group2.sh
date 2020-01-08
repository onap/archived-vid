#!/usr/bin/env bash

. $HOME/.nvm/nvm.sh

set -x

CYPRESS_HOME_DIR=$1
TESTS_GROUP_FILE=cypress/pipeline/group2.txt

cd ${CYPRESS_HOME_DIR}

cat ${TESTS_GROUP_FILE}
npm run cypress:headless --max-old-space-size=4096 -- --spec=$(cat ${TESTS_GROUP_FILE} | tr '\n' ',')
