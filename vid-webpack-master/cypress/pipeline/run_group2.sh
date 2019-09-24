#!/usr/bin/env bash

. $HOME/.nvm/nvm.sh

set -x

CYPRESS_HOME_DIR=$1
TESTS_GROUP_FILE_1=cypress/pipeline/group1.txt
TESTS_GROUP_FILE=cypress/pipeline/group2.generated.txt

cd ${CYPRESS_HOME_DIR}

# make group2 by "negating" group1.txt
ls -1 cypress/integration/*/*.e2e.ts | comm -3 - ${TESTS_GROUP_FILE_1} > ${TESTS_GROUP_FILE}

cat ${TESTS_GROUP_FILE}
npm run cypress:headless --max-old-space-size=4096 -- --spec=$(cat ${TESTS_GROUP_FILE} | tr '\n' ',')
