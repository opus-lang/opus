package dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;

public abstract sealed class TDOPRule<T> permits TDOPLEDRule, TDOPNUDRule {

    protected final Parser parser;
    protected final TDOPParserComponent<T> parserComponent;
    protected final int precedence;
    protected final boolean rightAssociative;

    protected TDOPRule(TDOPParserComponent<T> parserComponent, int precedence){
        this(parserComponent, precedence, false);
    }

    protected TDOPRule(TDOPParserComponent<T> parserComponent, int precedence, boolean rightAssociative) {
        this.parser = parserComponent.parser();
        this.parserComponent = parserComponent;
        this.precedence = precedence;
        this.rightAssociative = rightAssociative;
    }

    public final boolean isRightAssociative() {
        return this.rightAssociative;
    }

    protected final T parseWithAssociativity(){
        return this.parserComponent.parse(this.precedence - (this.rightAssociative ? 1 : 0));
    }

}
