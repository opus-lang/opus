package dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;

public abstract non-sealed class TDOPLEDRule<T> extends TDOPRule<T> {

    protected TDOPLEDRule(TDOPParserComponent<T> parserComponent, int lbp, boolean rightAssociative) {
        super(parserComponent, lbp, rightAssociative);
    }

    public abstract T led(T left);

    public final int lbp(){
        return this.precedence;
    }

}
