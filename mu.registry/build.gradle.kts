version = "1.0-SNAPSHOT"

apply { from(rootProject.file("gradle/versions.gradle.kts")) }

val ktorVersion: String by extra
val koroutinesCoreVersion: String by extra
val logbackVersion: String by extra

dependencies {
    compile(kotlin("stdlib-jre8"))
    compile("org.jetbrains.ktor:ktor-netty:$ktorVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
}