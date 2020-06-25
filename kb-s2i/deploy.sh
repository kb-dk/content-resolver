#!/usr/bin/env bash

cp -- /tmp/src/conf/content-resolver-default.xml "$CONF_DIR/crs-configuration.xml"
 
ln -s -- "$TOMCAT_APPS/content-resolver.xml" "$DEPLOYMENT_DESC_DIR/content-resolver.xml"
