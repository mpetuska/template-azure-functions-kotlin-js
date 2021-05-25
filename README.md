# AZURE FUNCTIONS KOTLIN
A sample project demonstrating how to develop NodeJS based azure functions with kotlin.

## Configuring
Main configuration goes into `build.gradle.kts`.
Azure function configs go into `src/main/resources/function.json`

## Building
To generate deployable artefact, use `assemble`(raw) or `pack`(tarball) tasks.

## Running
After building the project, you can test your functions locally.
* `cd build/publications/npm/js`
* `npm install`
* `func extensions install`
* `func start -p 7070`

Alternatively you can just run `./gradlew runFunctions` that does the same for you via gradle.

The sample endpoints will then be available at:
* [http://localhost:7070/api/MyFunction](http://localhost:7071/api/MyFunction)
8 [http://localhost:7070/api/MyGreeterFunction](http://localhost:7071/api/MyGreeterFunction)