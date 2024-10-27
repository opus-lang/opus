package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.utils.Characters;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Component(name = "comment")
public class CommentRule {

    @Symphonia.Inject
    SourceScanner scanner;

    public Token lexSingleline(){
        assert this.scanner.peek() == '/' && this.scanner.peek(1) == '/';
        Cursor position = new Cursor(this.scanner.cursor());
        this.scanner.next(1);
        int start = this.scanner.cursor().offset();
        while(Characters.isLineEnd(this.scanner.peek())){
            this.scanner.next();
        }
        return new Token(Token.Type.COMMENT, this.scanner.source().substring(start, this.scanner.cursor().offset()), position);
    }

    public Token lexMultiline(){
        assert this.scanner.peek() == '/' && this.scanner.peek(1) == '*';
        Cursor position = new Cursor(this.scanner.cursor());
        this.scanner.next(1);
        int start = this.scanner.cursor().offset();
        int nesting = 1;

        while(true){
            char c = this.scanner.next();

            if(c == '/' && this.scanner.peek() == '*') {
                this.scanner.next();
                nesting++;
            }
            else if(c == '*' && this.scanner.peek() == '/') {
                this.scanner.next();
                nesting--;
            }

            if(nesting <= 0) break;

            if(c == '\0') throw new IllegalStateException("Unterminated multiline comment.");
        }
        return new Token(Token.Type.COMMENT, this.scanner.source().substring(start, this.scanner.cursor().offset()-2), position);
    }

}
