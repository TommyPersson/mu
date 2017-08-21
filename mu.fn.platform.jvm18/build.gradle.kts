import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = "1.0-SNAPSHOT"

dependencies {
    shadow(project(":mu.fn.platform.base"))
    compile(project(":mu.fn.jvm.api"))

    compile(kotlin("stdlib-jre8"))

    compile("org.xeustechnologies:jcl-core:2.7")
}

val shadowJar: ShadowJar by tasks
shadowJar.apply {
    relocate("net", "shaded.net")
    relocate("org", "shaded.org")

    dependencies {
        // Deps applied by main build.gradle
        exclude(dependency("ch.qos.logback::"))
        exclude(dependency("org.jetbrains.*::"))
        exclude(dependency("org.slf4j::"))
    }
}