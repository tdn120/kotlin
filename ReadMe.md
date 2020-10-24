[![official project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![TeamCity (simple build status)](https://img.shields.io/teamcity/http/teamcity.jetbrains.com/s/Kotlin_KotlinPublic_Compiler.svg)](https://teamcity.jetbrains.com/buildConfiguration/Kotlin_KotlinPublic_Compiler?branch=%3Cdefault%3E&buildTypeTab=overview&mode=builds)
[![Maven Central](https://img.shields.io/maven-central/v/org.jetbrains.kotlin/kotlin-maven-plugin.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.jetbrains.kotlin%22)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

# Kotlin Programming Language

Welcome to [Kotlin](https://kotlinlang.org/)!   
It is an open-source, statically typed programming language supported and developed by [JetBrains](https://www.jetbrains.com/) and open-source contributors.

Some handy links:

 * [Kotlin Site](https://kotlinlang.org/)
 * [Getting Started Guide](https://kotlinlang.org/docs/tutorials/getting-started.html)
 * [Try Kotlin](https://play.kotlinlang.org/)
 * [Kotlin Standard Library](https://kotlinlang.org/api/latest/jvm/stdlib/index.html)
 * [Issue Tracker](https://youtrack.jetbrains.com/issues/KT)
 * [Forum](https://discuss.kotlinlang.org/)
 * [Kotlin Blog](https://blog.jetbrains.com/kotlin/)
 * [Follow Kotlin on Twitter](https://twitter.com/kotlin)
 * [Public Slack channel](https://slack.kotlinlang.org/)
 * [TeamCity CI build](https://teamcity.jetbrains.com/project.html?tab=projectOverview&projectId=Kotlin)

## Kotlin Multiplatform capabilities

Support for multiplatform programming is one of Kotlin’s key benefits. It reduces time spent writing and maintaining the same code for [different platforms](https://kotlinlang.org/docs/reference/mpp-supported-platforms.html) while retaining the flexibility and benefits of native programming.

 * [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/) for sharing code between Android and iOS
 * [Getting Started with Kotlin Multiplatform Mobile Guide](https://kotlinlang.org/docs/mobile/create-first-app.html)
 * [Kotlin Multiplatform Benefits](https://kotlinlang.org/docs/reference/multiplatform.html)
 * [Share code on all platforms](https://kotlinlang.org/docs/reference/mpp-share-on-platforms.html#share-code-on-all-platforms)
 * [Share code on similar platforms](https://kotlinlang.org/docs/reference/mpp-share-on-platforms.html#share-code-on-similar-platforms)

## Editing Kotlin

 * [Kotlin IntelliJ IDEA Plugin](https://kotlinlang.org/docs/tutorials/getting-started.html)
 * [Kotlin Eclipse Plugin](https://kotlinlang.org/docs/tutorials/getting-started-eclipse.html)
 * [Kotlin Sublime Text Package](https://github.com/vkostyukov/kotlin-sublime-package)

## Build environment requirements

In order to build Kotlin distribution you need to have:

- JDK 1.6, 1.7, 1.8 and 9
- Setup environment variables as following:

        JAVA_HOME="path to JDK 1.8"
        JDK_16="path to JDK 1.6"
        JDK_17="path to JDK 1.7"
        JDK_18="path to JDK 1.8"
        JDK_9="path to JDK 9"

For local development, if you're not working on bytecode generation or the standard library, it's OK to have only JDK 1.8 and JDK 9 installed, and to point `JDK_16` and `JDK_17` environment variables to your JDK 1.8 installation.

You also can use [Gradle properties](https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties) to setup `JDK_*` variables.

Note: The JDK 6 for MacOS is not available on Oracle's site. You can install it by

```bash
$ brew tap caskroom/versions
$ brew cask install java6
```

On Windows you might need to add long paths setting to the repo:

    git config core.longpaths true 

## Building

The project is built with Gradle. Run Gradle to build the project and to run the tests 
using the following command on Unix/macOS:

    ./gradlew <tasks-and-options>
    
or the following command on Windows:

    gradlew <tasks-and-options>

On the first project configuration gradle will download and setup the dependencies on

