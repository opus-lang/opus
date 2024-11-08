package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.utils.Characters;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.DI.Component(name = "identifier")
public class IdentifierRule {

    @Symphonia.DI.Inject
    SourceScanner scanner;

    @Symphonia.DI.Inject
    KeywordRule keyword;

    public Token lex(){
        assert Characters.isIdentifierStart(this.scanner.peek());
        Cursor position = new Cursor(this.scanner.cursor());

        int start = this.scanner.cursor().offset();
        while(Characters.isIdentifierPart(this.scanner.peek())){
            this.scanner.next();
        }
        String identifier = this.scanner.source().substring(start, this.scanner.cursor().offset());

        Token.Type type = this.keyword.lex(identifier).orElse(Token.Type.IDENTIFIER);

        return new Token(type, type == Token.Type.IDENTIFIER ? identifier : "", position);
    }

}
