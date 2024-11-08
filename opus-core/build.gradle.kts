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

tasks.register<Copy>("copyOpusExamples"){
    val examplesDir = rootProject.layout.projectDirectory.dir("examples");
    inputs.dir(examplesDir)
    outputs.dir(layout.buildDirectory.dir("libs/examples"))
    from(examplesDir)
    into(layout.buildDirectory.dir("libs/examples/"))
}

application {
    applicationName = "opus"

    mainClass = "dev.opuslang.opus.core.Main"
    mainModule = "dev.opuslang.opus.core"

    applicationDefaultJvmArgs = listOf("-ea") // Enable assertions
    executableDir = "" // Put scripts in the root instead of "./bin"

    applicationDistribution.from(tasks.named("copyOpusPlugins")){
        into("plugins")
    }
    applicationDistribution.from(tasks.named("copyOpusExamples")){
        into("examples")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(project(":opus-api"))
    implementation(project(":opus-utils"))
    implementation(project(":opus-symphonia")) // Bundle together for convenience

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.11.0")

    implementation("info.picocli:picocli:4.7.6")
    annotationProcessor("info.picocli:picocli-codegen:4.7.6")

    opusPlugin(project(":opus-core-plugins:opus-core-plugins-magnum"))
    opusPlugin(project(":opus-core-plugins:opus-core-plugins-maestro"))
    opusPlugin(project(":opus-core-plugins:opus-core-plugins-magnum-passes-lexer"))
    opusPlugin(project(":opus-core-plugins:opus-core-plugins-magnum-passes-parser"))
    opusPlugin(project(":opus-core-plugins:opus-core-plugins-magnum-passes-astdump"))
}

tasks.register("rebuildAndCopyOpusPlugins"){
    dependsOn(opusPlugin)
    finalizedBy("copyOpusPlugins")
}

tasks.named("jar") {
    finalizedBy("rebuildAndCopyOpusPlugins")
    finalizedBy("copyOpusExamples")
}

tasks.named<JavaExec>("run") {
    workingDir = layout.buildDirectory.asFile.get().resolve("libs/")
    standardInput = System.`in` // Enable "System.in"
}

tasks.test {
    useJUnitPlatform()
}