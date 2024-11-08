plugins {
    id("java")
}

group = "dev.opuslang.opus.core.plugins.magnum.passes.parser"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly(project(":opus-core-plugins:opus-core-plugins-magnum"))
    compileOnly(project(":opus-core-plugins:opus-core-plugins-magnum-passes-lexer"))

    compileOnly(project(":opus-symphonia"))
    annotationProcessor(project(":opus-symphonia"))
}

tasks.test {
    useJUnitPlatform()
}