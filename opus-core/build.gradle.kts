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

// Custom configuration to mark core plugins
val opusPlugin by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.register("copyOpusPlugins"){
    val opusPlugins = opusPlugin.dependencies.filterIsInstance<ProjectDependency>().map { it.dependencyProject }
    inputs.files(opusPlugin.files) // Run only if files changed
    println(opusPlugin.files)
    outputs.dir(layout.buildDirectory.dir("libs/plugins"))
    for( pluginProject in opusPlugins ){
        val outputDir = layout.buildDirectory.dir("libs/plugins/${pluginProject.name}")
        doLast{
            copy{
                println("Copying ${pluginProject.name}")
                from(pluginProject.layout.buildDirectory.dir("libs"))
                into(outputDir)
            }
        }
    }
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

    opusPlugin(project(":opus-core-plugins:opus-core-plugins-magnum"))
    opusPlugin(project(":opus-core-plugins:opus-core-plugins-maestro"))
}

tasks.register("rebuildAndCopyOpusPlugins"){
    dependsOn(opusPlugin)
    finalizedBy("copyOpusPlugins")
}

tasks.named("jar") {
    finalizedBy("rebuildAndCopyOpusPlugins")
}

tasks.test {
    useJUnitPlatform()
}