plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    testApi(project(":compiler:fir:entrypoint"))
    testApi(project(":compiler:cli"))
    testApi(intellijCoreDep()) { includeJars("intellij-core") }

    testCompileOnly(project(":kotlin-reflect-api"))
    testRuntimeOnly(project(":kotlin-reflect"))
    testRuntimeOnly(project(":core:descriptors.runtime"))

    testImplementation(projectTests(":compiler:test-infrastructure-utils"))

    testImplementation(intellijDep()) {
        // This dependency is needed only for FileComparisonFailure
        includeJars("idea_rt", rootProject = rootProject)
        isTransitive = false
    }

    testImplementation(intellijDep()) {
        includeJars(
            "commons-lang3",
            "commons-io",
            rootProject = rootProject
        )
    }

    // This is needed only for using FileComparisonFailure, which relies on JUnit 3 classes
    testRuntimeOnly(commonDep("junit:junit"))
    testRuntimeOnly(intellijDep()) {
        includeJars("jna", rootProject = rootProject)
    }
}

sourceSets {
    "main" { none() }
    "test" { projectDefault() }
}

testsJar()
