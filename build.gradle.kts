import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
}

group = "net.axay"
version = "0.0.1"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.pl3x.net/")
}

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly("net.pl3x.purpur", "purpur-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.axay:spigot-language-kotlin:1.0.3")

    api("net.axay:kspigot:1.16.29")

    testCompileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
}

tasks {
    withType<JavaCompile> {
        options.release.set(11)
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}
