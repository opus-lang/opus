package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.DI.Component(name = "end")
public class EndStatementRule {

    public static final class MissingSemicolonException extends RuntimeException{
        public MissingSemicolonException(String message) {
            super(message);
        }
    }

    @Symphonia.DI.Inject
    Parser parser;

    public void expect(){
        this.parser
                .nextIfType(Token.Type.SEMICOLON)
                .orElseThrow(() -> new MissingSemicolonException("Semicolon expected."));
    }

}
