version = "1.0-SNAPSHOT"

apply { from(rootProject.file("gradle/versions.gradle.kts")) }

dependencies {
    compile(project(":mu.fn.jvm.api"))
}
