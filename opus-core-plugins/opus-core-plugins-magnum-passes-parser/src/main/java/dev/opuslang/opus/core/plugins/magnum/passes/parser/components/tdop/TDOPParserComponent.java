package dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.ParserComponent;

import java.util.Map;

public abstract class TDOPParserComponent<T> extends ParserComponent<T> {

    private Map<Token.Type, TDOPNUDRule<T>> nudRules;
    private Map<Token.Type, TDOPLEDRule<T>> ledRules;

    protected TDOPParserComponent(Parser parser) {
        super(parser);
    }

    public void setRules(Map<Token.Type, TDOPLEDRule<T>> ledRules, Map<Token.Type, TDOPNUDRule<T>> nudRules){
        this.ledRules = ledRules;
        this.nudRules = nudRules;
    }

    @Override
    public T parse() {
        return this.parse(0);
    }

    public final T parse(int rbp){
        T lhs = this.nud(this.parser.peek());

        while(this.lbp(this.parser.peek()) > rbp){
            lhs = this.led(lhs, this.parser.peek());
        }
        return lhs;
    }

    private T nud(Token token){
//        System.out.println("NUD: " + token.type());
        TDOPNUDRule<T> parselet = this.nudRules.get(token.type());
        if(parselet == null){
            throw new IllegalArgumentException(token.type() + " cannot be used in a prefix position.");
        }
        return parselet.nud();
    }

    private T led(T left, Token token){
//        System.out.println("LED: " + token.type());
        TDOPLEDRule<T> parselet = this.ledRules.get(token.type());
        if(parselet == null){
            throw new IllegalArgumentException(token.type() + " cannot be used in a in/postfix position.");
        }
        return parselet.led(left);
    }

    private int lbp(Token token){
        System.out.println("LBP: " + token.type());
        TDOPLEDRule<T> parselet = this.ledRules.get(token.type());
        if(parselet != null) return parselet.lbp();
        return 0;
    }

}
