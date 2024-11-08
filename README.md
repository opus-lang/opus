# Opus Programming Language

Opus is a modern memory-safe and performant programming language, aimed to cater
for people from both low-level and high-level languages.

Opus provides a number of compile-time and run-time safeties, proposes various new
features, and borrows positive aspects from already existing languages.

## Features

Opus specifications and a core set of features was already established, and the development is currently
in the implementation stage.

### Opus Compiler
- [x] Plugin System
- [x] Service System
- [x] Command Line Interface
- [x] Parallelized Pipeline
- [x] Query-based Compilation
- [ ] Project Manager
- [ ] Plugin Repository

### Opus Programming Language
- [x] Specifications
- [x] Syntax
- [x] Safety Mechanisms
- [x] Lexer
- [x] Parser
- [x] Desugaring
- [ ] Import Resolution
- [ ] Hindley-Milner Type Checker (currently only simple)
- [ ] Linear-Type Constraint Checker
- [ ] Intent Checker
- [ ] Code Generation

## Reasons

Modern developers seek a reliable Systems Programming Language, which will
allow them to write a safe, bug-free, yet performant software, leading to an
emergence of numerous languages that try to satisfy these requirements.
However, there is yet to be a language that is able to do everything above,
while still being friendly towards the newcomers or people coming from  
high-level programming languages.

Therefore, the project introduces a new multi-paradigm memory-safe Systems
Programming Language - Opus that aims to learn from the mistakes made by
already existing languages, improve upon their positive sides and make the
overall memory-safe programming experience better and more accessible.

## Structure

| **Project Identifier**                  | **Description**                                                                                                                                       |
|-----------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| opus-core                               | Main bootstrapper. Contains CLI and related to it API. Discovers all the plugins, and connects the commands they expose.                              |
| opus-api                                | General API for the plugin development. Includes all the necessary annotations to develop plugins for Opus.                                           |
| opus-utils                              | Set of utilities used by most Opus core plugins.                                                                                                      |
| opus-symphonia                          | Specifically developed for Opus code-generation library. Currently contains a static Dependency Injection generator, and a Visitor Pattern generator. |
| opus-core-plugins                       | Subproject containing all core Opus plugins.                                                                                                          |
| opus-core-plugins-magnum                | Opus Magnum - compiler core. It defines the pipeline, exposes its API, and implements the fully parallelized executor for it.                         |
| opus-core-plugins-maestro               | Maestro - project manager. Used to configure and create new projects for Opus.                                                                        |
| opus-core-plugins-magnum-passes-lexer   | Lexer - lexing pass for Magnum.                                                                                                                       |
| opus-core-plugins-magnum-passes-parser  | Parser - parsing and desugaring pass for Magnum.                                                                                                      |
| opus-core-plugins-magnum-passes-astdump | ASTDump - primarily debugging pass - used to dump desugared AST.                                                                                      |

## Usage

### Pre-compiled binaries

Currently, no pre-compiled binaries are provided.

### Compiling from source

Prerequisites:
- Java Development Kit 21

To compile:
1. In the root project directory, execute:
   - For Unix:
       ```shell
       ./gradlew install
       ```
   - For Windows:
       ```
       gradlew.bat install
       ```
   This will create the `install` directory in the `opus-core/build/`.
2. Copy the contents of the `install` directory into the desired location.

Now, Opus Compiler can be run by executing:
- For Unix:
    ```shell
    ./opus <subcommand>
    ```
- For Windows:
    ```
    opus.bat <subcommand>
    ```

## Development

### Plugins

Opus Compiler (OpusC) is built on top of Java Platform Module System (JPMS). JPMS
is used for a new highly-performant plugin system, and the service discovery system,
inspired by OSGi.

#### Defining a plugin
Use annotation from Opus API in `module-info.java`:
```java
@Plugin(
        version = "0.1", // semver: MAJOR[.MINOR][.PATCH]
        name = "Magnum Parser Pass", // human-readable name
        description = "Description", 
        internalDependencyConfigurations = {
                // version configuration between plugins
                @Plugin.InternalDependencyConfiguration(
                        id = "dependency-id", // id of the plugin
                        // valid number range: supports inclusive [], exclusive ()
                        // and mixed ranges as well as single semver values
                        versionRange = "[1.2.3, 3.4.5]"
                )
        }
)
module newplugin.id {
    requires static dev.opuslang.opus.api;
}
```

#### Implementing a CLI command
Implement the logic for the command in a custom class. For example:`MyCommand.java`:
```java
@CommandLine.Command(
        name = "mycommand"
)
public class MyCommand implements SubcommandService<MyResult> {

    @Override
    public MyResult call() {
        // ...
        return result;
    }
}
```
Opus Compiler uses PicoCLI as a CLI backend-for further information on `Command` configuration, 
see PicoCLI documentation.

Register the command service in `module-info.java`:
```java
// ...
module newplugin.id {
    // ...
    provides SubcommandService with MyCommand;
}
```
Now, the command can be invoked using:
```shell
./opus mycommand
```

#### Implementing a custom Compiler Pass
Implement the pass in a custom class. For example: `MyPass.java`:
```java
@PassConfiguration(
        id = "mypass", // name of the pass and the corresponding to it query
        dependencies = { "somepass1", "somepass2" } // what passes MyPass relies on 
)
public class MyPassService extends IndependentPassService<MyResult> {
    @Override
    public MyResult execute(File file, PassContext context, String[] args) {
        // ...
        return result;
    }
}
```
Similarly, if you wish to implement a "Synchronized Pass" (pass that is aware of all the files in the project),
use:
```java
@PassConfiguration(
        id = "mypass", // name of the pass and the corresponding to it query
        dependencies = { "somepass1", "somepass2" } // what passes MyPass relies on 
)
public class MyPassService extends SynchronizedPassService<MyResult> {
    @Override
    public Map<File, MyResult> execute(List<File> files, Map<File, PassContext> contexts, String[] args) {
        // ...
        return result;
    }
}
```
Finally, register the implemented service in `module-info.java`:
```java
provides PassService with MyPassService;
```
Now, the pass can be invoked using:
```shell
./opus magnum mypass [files...] 
```

## Presentation Video

Click on the thumbnail to see the video:
[![Opus Presentation Video](https://img.youtube.com/vi/rMFsP_N9i3g/0.jpg)](https://www.youtube.com/watch?v=rMFsP_N9i3g)