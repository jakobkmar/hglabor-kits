import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
}

group = "net.axay"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
    api("net.axay:kspigot:1.16.27")
}

tasks {
    withType<JavaCompile> {
        options.release.set(11)
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}
