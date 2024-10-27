package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Component(name = "simple")
public class SimpleRule {

    @Symphonia.Inject
    SourceScanner scanner;

    public Token simple(Token.Type type){
        return this.simple(1, type);
    }

    public Token simple(int length, Token.Type type){
        return this.simple(length, type, this.scanner.cursor());
    }

    public Token simple(int length, Token.Type type, Cursor position){
        if(length <= 0) throw new IllegalArgumentException("Token length cannot be negative.");
        this.scanner.next(length-1);
        return new Token(type, "", position);
    }

}
