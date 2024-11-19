package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;

import java.util.ArrayList;
import java.util.List;

public class WhileLoopExpressionRule extends TDOPNUDRule<ExpressionNode> {

    private final BlockExpressionRule blockExpressionRule;

    public WhileLoopExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence, BlockExpressionRule blockExpressionRule) {
        super(parserComponent, precedence);
        this.blockExpressionRule = blockExpressionRule;
    }

    @Override
    public LoopExpressionNode nud() {
        LoopExpressionNode.Builder nodeBuilder = new LoopExpressionNode.Builder(this.parser.copyCurrentPosition(), new Node.Annotation[0]);

        this.parser
                .nextIfType(Token.Type.KEYWORD_WHILE)
                .orElseThrow(() -> new IllegalStateException("Keyword 'while' was expected."));

        this.parser
                .nextIfType(Token.Type.LPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Opening parenthesis for a conditional expression was expected."));

        ExpressionNode condition = this.parser.parseExpression();

        this.parser
                .nextIfType(Token.Type.RPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Closing parenthesis for a conditional expression was expected."));

        BlockExpressionNode body = this.blockExpressionRule.nud();
        List<StatementNode> modifiedStatementList = new ArrayList<>(List.of(body.statements()));
        modifiedStatementList.addFirst(
                new IgnoredExpressionStatementNode.Builder(nodeBuilder.position())
                        .expression(
                                new ConditionalExpressionNode.Builder(nodeBuilder.position())
                                        .condition(
                                                new UnaryExpressionNode.Builder(nodeBuilder.position())
                                                        .operator(Operator.LOGIC_INVERT)
                                                        .right(condition)
                                                        .build()
                                        )
                                        .body(
                                                new BlockExpressionNode.Builder(nodeBuilder.position())
                                                        .statements(new StatementNode[]{
                                                            new YieldStatementNode.Builder(nodeBuilder.position())
                                                                    .defaultValue()
                                                                    .label(body.label())
                                                                    .build()
                                                        })
                                                        .generatedLabel()
                                                        .build()
                                        )
                                        .defaultElseExpression()
                                        .build()
                        )
                        .build()
        );
        nodeBuilder.body(
                new BlockExpressionNode.Builder(body)
                        .statements(modifiedStatementList.toArray(StatementNode[]::new))
                        .build()
        );

        return nodeBuilder.build();
    }
}
