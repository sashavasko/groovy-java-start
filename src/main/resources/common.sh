#!/bin/bash

set -e

timestamp=$(date +"%Y%m%d-%H%M%S")

if [ "x$today" == "x" ]; then
    today=$(date +"%Y%m%d")
fi

PROJECT_NAME=groovy-java-start

BATCH_FILE=${PROJECT_NAME}-@version@/bin/${PROJECT_NAME}

BATCH_PATH=.
if test -f build/distributions/$BATCH_FILE ; then
    BATCH_PATH=build/distributions/;
elif  test -f /apps/${PROJECT_NAME}/@env@/batch/$BATCH_FILE ; then
    BATCH_PATH=/apps/${PROJECT_NAME}/@env@/batch;
fi

echo "${PROJECT_NAME} batch located at $BATCH_PATH/$BATCH_FILE"
if  [ "@env@x" == "devx" ] ; then
    export ${PROJECT_NAME}_OPTS="-Doracle.environment=PROD";
else
    export ${PROJECT_NAME}_OPTS="-Doracle.environment=PROD -Dmysql.environment=PROD";
fi

