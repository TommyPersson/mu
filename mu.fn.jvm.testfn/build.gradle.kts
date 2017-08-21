version = "1.0-SNAPSHOT"

apply { from(rootProject.file("gradle/versions.gradle.kts")) }

val kotlinVersion: String by extra

dependencies {
    compile(project(":mu.fn.jvm.api"))

    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
}
