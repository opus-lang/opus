package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPNUDRule;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Utils;

public class ConditionalExpressionRule extends TDOPNUDRule<ExpressionNode> {

    private final BlockExpressionRule blockExpressionRule;

    public ConditionalExpressionRule(TDOPParserComponent<ExpressionNode> parserComponent, int precedence, BlockExpressionRule blockExpressionRule) {
        super(parserComponent, precedence);
        this.blockExpressionRule = blockExpressionRule;
    }

    @Override
    public ConditionalExpressionNode nud() {
        ConditionalExpressionNode.Builder nodeBuilder = new ConditionalExpressionNode.Builder(this.parser.copyCurrentPosition(), new Node.Annotation[0]);

        this.parser
                .nextIfType(Token.Type.KEYWORD_IF)
                .orElseThrow(() -> new IllegalStateException("Keyword 'if' was expected."));

        this.parser
                .nextIfType(Token.Type.LPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Opening parenthesis for a conditional expression was expected."));

        nodeBuilder.condition(this.parser.parseExpression());

        this.parser
                .nextIfType(Token.Type.RPARENTHESIS)
                .orElseThrow(() -> new IllegalStateException("Closing parenthesis for a conditional expression was expected."));

        nodeBuilder.body(this.blockExpressionRule.nud());

        if(this.parser.nextIfType(Token.Type.KEYWORD_ELSE).isEmpty()){
            return nodeBuilder.defaultElseExpression().build();
        }

        if(this.parser.peek().type() == Token.Type.DOLLAR){
            return nodeBuilder.elseExpression(this.blockExpressionRule.nud())
                    .build();
        }

        if(this.parser.peek().type() == Token.Type.KEYWORD_IF){
            return nodeBuilder.elseExpression(
                    Utils.evaluate(() -> {
                                BlockExpressionNode.Builder elseBodyBuilder = new BlockExpressionNode.Builder(nodeBuilder.position())
                                        .generatedLabel();

                                return  elseBodyBuilder.statements(new StatementNode[]{
                                        new YieldStatementNode.Builder(nodeBuilder.position())
                                                .value(this.nud())
                                                .label(elseBodyBuilder.label())
                                                .build()
                                        })
                                        .build();
                            }
                    )
            ).build();
        }

        throw new IllegalStateException("Ill formed Conditional Expression");
//        BlockExpressionNode elseExpression = this.parser
//                .nextIfType(Token.Type.KEYWORD_ELSE)
//                .map(tokenElse -> this.parser
//                                    .nextIfType(Token.Type.KEYWORD_IF)
//                                    .map(tokenElseIf ->
//                                        this.parser.createNode((position1, annotations1) -> new BlockExpressionNode(
//                                                position1,
//                                                annotations1,
//                                                new StatementNode[]{
//                                                        this.parser.createNode((position2, annotations2) -> new YieldStatementNode(position2, annotations2, null, this.nud()))
//                                                })
//                                        )
//                                    )
//                                    .orElse(this.blockExpressionRule.nud())
//                ).orElse(
//                        this.parser.createNode((position1, annotations1) ->
//                                new BlockExpressionNode(
//                                        position1,
//                                        annotations1,
//                                        new StatementNode[]{
//                                                this.parser.createNode((position2, annotations2) -> new YieldStatementNode(
//                                                        position2,
//                                                        annotations2,
//                                                        null,
//                                                        this.parser.createNode(VoidExpressionNode::new)
//                                                ))
//                                        }
//                                )
//                        )
//                );
//
//        return this.parser.createNode((ignore, annotations) -> new ConditionalExpressionNode(position, annotations, condition, body, elseExpression));
    }
}

