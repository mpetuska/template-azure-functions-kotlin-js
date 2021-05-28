plugins {
  kotlin("js")
  kotlin("plugin.serialization")
  id("dev.petuska.npm.publish")
  id("com.github.jakemarsden.git-hooks")
//  id("org.jlleitschuh.gradle.ktlint")
  idea
}

repositories {
  mavenCentral()
  jcenter()
}

gitHooks {
  setHooks(
    mapOf(
//      "post-checkout" to "ktlintApplyToIdea",
      "pre-commit" to "ktlintFormat",
      "pre-push" to "check"
    )
  )
}

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
  js(IR) {
    binaries.library()
    useCommonJs()
    nodejs {}
  }
  sourceSets {
    main {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:_")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
        implementation(npm("@azure/functions", "1.2.3", true))
      }
    }
  }
}

npmPublishing {
  publications {
    named("js") {
      packageJson {
        scripts = mutableMapOf(
          Pair("func:install", "func extensions install"),
          Pair("func:run", "func start"),
        )
        devDependencies {
          "azure-functions-core-tools" to "^3"
        }
      }
    }
  }
}

afterEvaluate {
  tasks {
    val assembleJsNpmPublication by getting(dev.petuska.npm.publish.task.NpmPackageAssembleTask::class)
    val packJsNpmPublication by getting(dev.petuska.npm.publish.task.NpmPackTask::class)
    val assembleFunctions = create("assembleFunctions", Copy::class) {
      dependsOn(assembleJsNpmPublication)
      group = "build"
      from(assembleJsNpmPublication.destinationDir)
      destinationDir = temporaryDir
    }
    create(
      "runFunctions",
      dev.petuska.npm.publish.task.NpmExecTask::class,
      packJsNpmPublication.nodeJsDir!!
    ).apply {
      dependsOn(assembleFunctions)
      group = "run"
      val execConfig: ExecSpec.() -> Unit = {
        workingDir = assembleFunctions.destinationDir
      }
      val port = 7071
      doFirst {
        npmExec(listOf("install", "--scripts-prepend-node-path"), execConfig)
        npmExec(listOf("run", "func:install", "--scripts-prepend-node-path"), execConfig)
      }
      doLast {
        npmExec(listOf("run", "func:run", "--scripts-prepend-node-path", "--", "-p", "$port"), execConfig)
      }
    }
  }
}
