#!/bin/bash

yarn --cwd ./src/main/tailwind build:css && mvn clean:clean && mvn compiler:compile && mvn war:war
rm -rf $CATALINA_HOME/webapps/image-gallery && rm $CATALINA_HOME/webapps/image-gallery.war
cp ./target/image-gallery.war $CATALINA_HOME/webapps/