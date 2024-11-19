package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.MemberCallExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPLEDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

import java.util.ArrayList;
import java.util.List;

public class MemberCallExpressionRule extends TDOPLEDRule<ExpressionNode> {

    public MemberCallExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int lbp) {
        super(parserComponent, lbp, false);
    }

    @Override
    public MemberCallExpressionNode led(ExpressionNode left) {
        MemberCallExpressionNode.Builder nodeBuilder = new MemberCallExpressionNode.Builder(this.parser.copyCurrentPosition())
                .callable(left);

        this.parser
                .nextIfType(Token.Type.LPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Argument list opening parenthesis was expected."));

        List<ExpressionNode> arguments = new ArrayList<>();
        if(this.parser.peek().type() != Token.Type.RPARENTHESIS) {
            do{
                arguments.add(this.parser.parseExpression());
            }while(this.parser.nextIfType(Token.Type.COMMA).isPresent());
        }
        this.parser
                .nextIfType(Token.Type.RPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Argument list closing parenthesis expected."));

        nodeBuilder.arguments(arguments.toArray(ExpressionNode[]::new));

        return nodeBuilder.build();
    }
}
