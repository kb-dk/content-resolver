# Content resolver

This project is a framework for looking up content dissemination from one or more content PIDs.

It consists of a model for representing content disseminations, a library containing implementations of content disseminations, and a webservice using the library to disseminate content from a set of directories as JSON.

For more information see https://sbprojects.statsbiblioteket.dk/display/INFRA/Content+Resolver

## Requirements
The project requires Java 8 be build and run. Known to build with OpenJDK 1.8, other JDK's may work

## Building
Use maven to build the project i.e. `mvn clean package`

## Test 
To run a local instance of the service do:

```bash
mvn install
cd content-resolver-service
mvn test-compile exec:java -Dexec.classpathScope=test -Dexec.mainClass=dk.statsbiblioteket.medieplatform.contentresolver.service.Server```
