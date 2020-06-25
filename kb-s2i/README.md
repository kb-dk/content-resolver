# kb-s2i

Files needed for building a container image with the application using `kb-s2i`.
`kb-s2i` is an extension/adaption of Redhats Source-2-Image stategy where a builder image + source automatically becomes an application container image.

In this context (tomcat application) two files are needed:
- `build.sh` The file ensures that the application binaries are in place in the image
- `deploy.sh` The file ensure that the application is deployed and configured

The files are sourced by the kb-s2i image to build an application container image that can be started.

For more information see: https://sbprojects.statsbiblioteket.dk/stash/projects/OC/repos/kb-s2i-tomcat/browse
