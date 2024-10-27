@Symphonia.Package(
        outputClass = "AbstractLexer",
        additionalProviders = {
                @Symphonia.Provider(type = SourceScanner.class, name = "scanner", required = true)
        }
)
package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.symphonia.annotation.Symphonia;