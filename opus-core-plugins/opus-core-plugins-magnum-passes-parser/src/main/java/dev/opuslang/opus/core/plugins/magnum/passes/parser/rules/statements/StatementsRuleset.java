package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.StatementNode;

public class StatementsRuleset extends AbstractStatementsRuleset {

    public StatementsRuleset(Parser parser) {
        super(parser);
    }

    public StatementNode parse(){
        Token token = this.parser.peek();
        return switch (token.type()){
            case SEMICOLON -> this.empty.parse();
            case KEYWORD_DEF -> this.definition.parse();
            default -> this.ignoredExpressionOrAssignment.parse();
        };
    }

}
