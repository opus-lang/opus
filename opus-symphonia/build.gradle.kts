plugins {
    id("java")
}

group = "dev.opuslang.opus.symphonia"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/com.google.auto.service/auto-service
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")

    // https://mvnrepository.com/artifact/com.palantir.javapoet/javapoet
    implementation("com.palantir.javapoet:javapoet:0.5.0")
}

tasks.test {
    useJUnitPlatform()
}