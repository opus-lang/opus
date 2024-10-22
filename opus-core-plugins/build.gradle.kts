plugins{
    id("java")
    id("java-library")
}

subprojects{
    apply(plugin = "java")
    apply(plugin = "java-library")
    dependencies {
        compileOnly(project(":opus-api"))
        annotationProcessor(project(":opus-api"))

        compileOnly("info.picocli:picocli:4.7.6") // Compile-only because a root project will always have it as a dependency.
        annotationProcessor("info.picocli:picocli-codegen:4.7.6")
    }

    tasks.register<Copy>("copyDependencies") {
        from(configurations.runtimeClasspath)
        into(layout.buildDirectory.dir("libs/libs"))
    }

    tasks.named("jar"){
        finalizedBy("copyDependencies")
    }
}