plugins {
    kotlin("js") version "1.5.0"
    id("lt.petuska.npm.publish") version "1.1.4"
}

repositories {
    jcenter()
    mavenCentral()
}


kotlin {
    js(IR) {
        binaries.executable()
        useCommonJs()
        nodejs {}
    }
    sourceSets {
        main {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
                implementation(npm("@azure/functions", "1.2.3", true))
                implementation(devNpm("azure-functions-core-tools", "^3"))
            }
        }
    }
}

afterEvaluate {
    tasks {
        val compileProductionExecutableKotlinJs by getting(org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink::class)
        val processResources by getting(Copy::class)
        val publicPackageJson by getting(org.jetbrains.kotlin.gradle.targets.js.npm.PublicPackageJsonTask::class)
        named("assembleJsNpmPublication", lt.petuska.npm.publish.task.NpmPackageAssembleTask::class) {
            dependsOn(compileProductionExecutableKotlinJs, processResources, publicPackageJson)
            doLast {
                copy {
                    into(destinationDir)
                    from(publicPackageJson.packageJsonFile)
                    from(compileProductionExecutableKotlinJs.destinationDir)
                    from(processResources.destinationDir)
                }
            }
            doLast {
                copy {
                    into(destinationDir.resolve("MyFunction"))
                    from(destinationDir) {
                        include("function.json", "*.js", "*.d.ts")
                    }
                }
            }
        }
    }
}