package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.utils.Characters;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.lang.Character;

@Symphonia.DI.Component(name = "string")
public class StringRule {

    @Symphonia.DI.Inject
    SourceScanner scanner;

    public Token lexNormal(){
        assert this.scanner.peek() == '"';
        Cursor position = new Cursor(this.scanner.cursor());
        this.scanner.next();
        StringBuilder sb = new StringBuilder();
        int start = this.scanner.cursor().offset();
        while(true){
            char c = this.scanner.next();

            if(Characters.isLineEnd(c)) throw new IllegalStateException("Unterminated string.");
            if(c == '"') break;
            if(c == '\\'){ // handle escape sequences
                char escaped = this.scanner.next();
                Character escapedC = switch (escaped) {
                    case 'n' -> '\n';
                    case 'b' -> '\b';
                    case 't' -> '\t';
                    case 'r' -> '\r';
                    case '0' -> '\0';
                    case '"' -> '"';
                    // TODO: add unicode escape sequence handler.
                    default -> null;
                };
                if(escapedC != null){
                    sb.append(this.scanner.source(), start, this.scanner.cursor().offset()-2);
                    sb.append(escapedC);
                    start = this.scanner.cursor().offset();
                }else{
                    throw new IllegalStateException("Unexpected escaped character: " + escaped);
                }
            }
        }
        sb.append(this.scanner.source(), start, this.scanner.cursor().offset()-1);
        return new Token(Token.Type.STRING, sb.toString(), position);
    }

    public Token lexRaw(){
        // TODO: handle indentation.
        /*
        Prototype:
        mystr: String := `Line 1
        |Line 2
        |Line 3`
        (each new line must start with |, which shows the indentation).
         */
        assert this.scanner.peek() == '`';
        Cursor position = new Cursor(this.scanner.cursor());
        this.scanner.next();
        int start = this.scanner.cursor().offset(); // skip leading backtick
        while(true){
            char c = this.scanner.next();

            if(c == '`') break;
            if(c == '\0') throw new IllegalStateException("Unterminated raw string.");
        }
        return new Token(Token.Type.STRING, this.scanner.source().substring(start, this.scanner.cursor().offset()-1), position);
    }

}
