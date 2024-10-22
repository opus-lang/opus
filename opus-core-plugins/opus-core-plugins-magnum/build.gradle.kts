plugins {
    id("java")
    id("java-library")
}

group = "dev.opuslang.opus.core.plugins.magnum"
version = "0.1"
description = "Compiler"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}