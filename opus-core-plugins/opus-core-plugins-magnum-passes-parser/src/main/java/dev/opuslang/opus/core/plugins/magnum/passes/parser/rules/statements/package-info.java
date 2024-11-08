@Symphonia.DI.Package(
        outputClass = "AbstractStatementsRuleset",
        additionalProviders = {
                @Symphonia.DI.Provider(type = Parser.class, name = "parser")
        }
)
package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.symphonia.annotation.Symphonia;