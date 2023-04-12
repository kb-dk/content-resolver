# Content resolver

This project is a framework for looking up content dissemination from one or more content PIDs.

It consists of a model for representing content disseminations, a library containing implementations of content disseminations, and a webservice using the library to disseminate content from a set of directories as JSON.

For more information see https://sbprojects.statsbiblioteket.dk/display/INFRA/Content+Resolver

## Requirements
The project requires Java 11 to be build and run. Known to build with OpenJDK 11, other JDKs may work.

## Building
Use maven to build the project i.e. `mvn clean package`

## Test 
To run a local instance of the service do:

`mvn jetty:run-war`
The test instance will then be available on http://localhost:8080/content-resolver/?_wadl

Configuration used for the test instance is located in `conf/content-resolver-default.xml`

