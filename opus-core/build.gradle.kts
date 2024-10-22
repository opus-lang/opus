plugins {
    id("java")
    id("java-library")
    id("application")
}

group = "dev.opuslang.opus.core"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(project(":opus-api"))
    implementation(project(":opus-utils"))

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.11.0")

    implementation("info.picocli:picocli:4.7.6")
    annotationProcessor("info.picocli:picocli-codegen:4.7.6")
}

tasks.test {
    useJUnitPlatform()
}