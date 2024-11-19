package dev.opuslang.opus.core.plugins.magnum.passes.lexer;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.TokenStream;

import java.util.ArrayList;
import java.util.List;

public class LexerTokenStream implements TokenStream {

    private final Lexer lexer;
    private final List<Token> buffer;

    public LexerTokenStream(Lexer lexer) {
        this.lexer = lexer;
        this.buffer = new ArrayList<>();
    }

    @Override
    public Token next(){
        Token token = this.peek();
        this.consume();
        return token;
    }

    @Override
    public boolean hasNext(){
        return this.peek(1).type() != Token.Type.EOF;
    }

    @Override
    public void consume(){
        this.buffer.removeFirst();
    }

    @Override
    public Token peek(){
        return this.peek(0);
    }

    @Override
    public Token peek(int offset){
        // Allows infinite amount of lookaheads:
        while(offset >= this.buffer.size()){
            this.buffer.add(this.lexer.next());
        }

        return this.buffer.get(offset);
    }

}
