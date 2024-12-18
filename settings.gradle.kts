rootProject.name = "opus"
include("opus-core")
include("opus-api")
include("opus-utils")
include("opus-symphonia")

include("opus-core-plugins")
include("opus-core-plugins:opus-core-plugins-magnum")
include("opus-core-plugins:opus-core-plugins-maestro")
include("opus-core-plugins:opus-core-plugins-magnum-passes-lexer")
include("opus-core-plugins:opus-core-plugins-magnum-passes-parser")
include("opus-core-plugins:opus-core-plugins-magnum-passes-astdump")
include("opus-core-plugins:opus-core-plugins-magnum-passes-analyzer-importgraph")
include("opus-core-plugins:opus-core-plugins-magnum-passes-importcollector")