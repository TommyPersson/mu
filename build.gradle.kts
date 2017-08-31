import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension
import java.net.URI

version = "1.0-SNAPSHOT"

val kotlinVersion: String by extra

buildscript {
    applyFrom(rootProject.file("gradle/versions.gradle.kts"))
    val kotlinVersion: String by extra

    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("http://repo.spring.io/plugins-release") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.springframework.build.gradle:propdeps-plugin:0.0.7")
        classpath("com.github.jengelman.gradle.plugins:shadow:2.0.1")
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0-RC2")
    }
}

task("wrapper", Wrapper::class) {
    gradleVersion = "4.1"
}

subprojects {
    version = "1.0-SNAPSHOT"

    apply {
        plugin("kotlin")
        plugin("propdeps")
        plugin("propdeps-idea")
        plugin("com.github.johnrengelman.shadow")
        plugin("org.junit.platform.gradle.plugin")
    }

    repositories {
        mavenCentral()
        jcenter()
        maven { url = URI("https://dl.bintray.com/kotlin/kotlinx") }
        maven { url = URI("https://dl.bintray.com/kotlin/ktor") }
        maven { url = URI("http://dl.bintray.com/jetbrains/spek") }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    configure<KotlinProjectExtension> {
        experimental.coroutines = Coroutines.ENABLE
    }

    configure<JUnitPlatformExtension> {
        filters {
            engines {
                include("spek")
            }
        }
    }

    dependencies {
        "compile"("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")

        "testCompile"("org.junit.jupiter:junit-jupiter-api:5.0.0-RC2")
        "testCompile"("org.jetbrains.spek:spek-api:1.1.4")
        "testRuntime"("org.jetbrains.spek:spek-junit-platform-engine:1.1.4")
    }
}

fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
        else -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}

fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
        else -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}