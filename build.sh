#!/bin/bash

yarn --cwd ./src/main/tailwind build:css && mvn clean:clean && mvn compiler:compile && mvn war:war && sudo rm -rf $CATALINA_HOME/webapps/image-gallery && sudo rm -f $CATALINA_HOME/webapps/image-gallery.war && sudo cp ./target/image-gallery.war $CATALINA_HOME/webapps/