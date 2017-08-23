apply { from(rootProject.file("gradle/versions.gradle.kts")) }

val ktorVersion: String by extra
val logbackVersion: String by extra

dependencies {
    compile(project(":mu.libs.cqrs"))
    compile(project(":mu.libs.utils"))

    compile("org.jetbrains.ktor:ktor-netty:$ktorVersion")
    compile("org.jetbrains.ktor:ktor-auth:$ktorVersion")

    compile("org.jetbrains.ktor:ktor-gson:$ktorVersion")
    compile("com.google.code.gson:gson:$ktorVersion")
    compile("com.github.salomonbrys.kotson:kotson:2.5.0")

    compile("com.graphql-java:graphql-java:2.4.0")

    compile("de.mkammerer:argon2-jvm:2.2")

    compile("ch.qos.logback:logback-classic:$logbackVersion")
}