# Project for the exam of Web Technologies

This readme is a work in progress

## Requirements
1. `sudo` rights
1. Node and Yarn v1
1. Apache Tomcat 9
1. `$CATALINA_HOME` has to be set in your environment
1. Java OpenJDK 13
1. Apache Maven 3.6.3 with `mvn` command present in your path
1. MySQL 8+

## Instructions to run
1. Clone the repository
1. Edit `src/main/webapp/META-INF/context.xml` and insert your db configuration
1. Set global timezone in your mysql configuration
1. Run `db_dump.sql` to populate your database
1. Create inside `$CATALINA_HOME` a folder named `static` with tomcat user write, read and execute rights
1. Add to `$CATALINA_HOME/conf/catalina.properties` a property named `static.resources` pointing to `$CATALINA_HOME`
1. Add `<Context docBase="path/to/static/folder/created/before" path="/resources/images" />` to `$CATALINA_HOME/conf/server.xml` inside `<Host></Host>` configuration
1. Run `./build.sh`