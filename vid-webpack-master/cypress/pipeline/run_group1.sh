#!/usr/bin/env bash

. $HOME/.nvm/nvm.sh

set -x

CYPRESS_HOME_DIR=$1
TESTS_GROUP_FILE=cypress/pipeline/group1.txt

cd ${CYPRESS_HOME_DIR}

cat ${TESTS_GROUP_FILE}
npm run cypress:headless -- --spec=$(cat ${TESTS_GROUP_FILE} | tr '\n' ',')
