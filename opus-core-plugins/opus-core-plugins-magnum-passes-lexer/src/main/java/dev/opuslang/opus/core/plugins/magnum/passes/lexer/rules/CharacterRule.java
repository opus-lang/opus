package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Component(name = "character")
public class CharacterRule {

    @Symphonia.Inject
    SourceScanner scanner;

    public Token lex() {
        assert this.scanner.peek() == '\'';
        Cursor position = new Cursor(this.scanner.cursor());
        this.scanner.next();
        char value = this.scanner.next();
        if(value == '\'') throw new IllegalStateException("No character specified.");
        if(value == '\\'){
            char escaped = this.scanner.next();
            java.lang.Character escapedC = switch (escaped) {
                case 'n' -> '\n';
                case 'b' -> '\b';
                case 't' -> '\t';
                case 'r' -> '\r';
                case '0' -> '\0';
                case '\'' -> '\'';
                // TODO: add unicode escape sequence handler.
                // Looks like unicode escaping works only in strings??
                default -> null;
            };
            if(escapedC != null){
                value = escapedC;
            }else{
                throw new IllegalStateException("Unexpected escaped character: " + escaped);
            }
        }
        if(this.scanner.peek() != '\'') throw new IllegalStateException("Unterminated character literal.");
        this.scanner.next();
        return new Token(Token.Type.CHARACTER, String.valueOf(value), position);
    }

}
