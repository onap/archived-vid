#!/usr/bin/env bash

. $HOME/.nvm/nvm.sh

set -x

CYPRESS_HOME_DIR=$1
REMAIN_TESTS_FILE=cypress/pipeline/remain.generated.txt
ALL_TESTS_IN_GROUPS_FILE=cypress/pipeline/all_tests.txt

cd ${CYPRESS_HOME_DIR}
cat cypress/pipeline/group?.txt | sort > ${ALL_TESTS_IN_GROUPS_FILE}
# make group2 by "negating" group1.txt
ls -1 cypress/integration/*/*.e2e.ts | comm -3 - ${ALL_TESTS_IN_GROUPS_FILE} > ${REMAIN_TESTS_FILE}

cat ${REMAIN_TESTS_FILE}
npm run cypress:headless --max-old-space-size=4096 -- --spec=$(cat ${REMAIN_TESTS_FILE} | tr '\n' ',')
