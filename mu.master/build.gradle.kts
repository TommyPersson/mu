version = "1.0-SNAPSHOT"

apply { from(rootProject.file("gradle/versions.gradle.kts")) }

val kotlinVersion: String by extra
val ktorVersion: String by extra
val logbackVersion: String by extra

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
    compile("org.jetbrains.ktor:ktor-netty:$ktorVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
}