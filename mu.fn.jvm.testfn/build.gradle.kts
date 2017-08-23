apply { from(rootProject.file("gradle/versions.gradle.kts")) }

dependencies {
    compile(project(":mu.fn.jvm.api"))
}
