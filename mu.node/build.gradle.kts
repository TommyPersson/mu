version = "1.0-SNAPSHOT"

apply { from(rootProject.file("gradle/versions.gradle.kts")) }

val kotlinVersion: String by extra
val ktorVersion: String by extra
val logbackVersion: String by extra

dependencies {
    compile(project(":mu.fn.platform.base"))

    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
    compile("org.jetbrains.ktor:ktor-netty:$ktorVersion")

    compile("org.jetbrains.ktor:ktor-gson:$ktorVersion")
    compile("com.google.code.gson:gson:$ktorVersion")
    compile("com.github.salomonbrys.kotson:kotson:2.5.0") // TODO alternatives?
    compile("org.xeustechnologies:jcl-core:2.7")

    compile("ch.qos.logback:logback-classic:$logbackVersion")
}
