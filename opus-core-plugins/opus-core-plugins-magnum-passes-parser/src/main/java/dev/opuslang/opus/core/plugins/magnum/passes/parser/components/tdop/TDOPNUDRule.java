package dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;

public abstract non-sealed class TDOPNUDRule<T> extends TDOPRule<T> {

    protected TDOPNUDRule(TDOPParserComponent<T> parserComponent, int precedence) {
        super(parserComponent, precedence, true);
    }

    public abstract T nud();

    public final int precedence(){
        return this.precedence;
    }

}
