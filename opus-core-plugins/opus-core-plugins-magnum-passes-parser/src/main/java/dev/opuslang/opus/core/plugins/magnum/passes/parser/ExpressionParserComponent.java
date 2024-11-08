package dev.opuslang.opus.core.plugins.magnum.passes.parser;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions.BlockExpressionRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions.FunctionTypeExpressionRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions.IdentifierExpressionRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions.LambdaExpressionRule;

import java.util.Map;

public class ExpressionParserComponent extends TDOPParserComponent<ExpressionNode> {
    public ExpressionParserComponent(Parser parser) {
        super(parser);
        BlockExpressionRule blockExpressionRule;
        this.setRules(Map.ofEntries(
                Map.entry(Token.Type.IDENTIFIER, new IdentifierExpressionRule(this, 10)),
                Map.entry(Token.Type.DOLLAR, blockExpressionRule = new BlockExpressionRule(this, 20)),
                Map.entry(Token.Type.KEYWORD_FN, new FunctionTypeExpressionRule(this, 30)),
                Map.entry(Token.Type.KEYWORD_BACKSLASH, new LambdaExpressionRule(this, 40, blockExpressionRule))

        ), Map.of());
    }


}
