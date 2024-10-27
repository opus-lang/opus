package dev.opuslang.opus.core.plugins.magnum.passes.lexer.api;

// TODO: make it an interface, and make implementation hidden.
public interface TokenStream {

    Token next();
    boolean hasNext();
    void consume();
    Token peek();
    Token peek(int offset);

}
