#!/usr/bin/env bash

cd /tmp/src

cp -rp -- /tmp/src/target/content-resolver-service/content-resolver-service-*.war "$TOMCAT_APPS/content-resolver-service.war"
cp -- /tmp/src/conf/ocp/content-resolver.xml "$TOMCAT_APPS/content-resolver.xml"

export WAR_FILE=$(readlink -f "$TOMCAT_APPS/content-resolver-service.war")
