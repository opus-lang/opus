package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.YieldStatementNode;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.DI.Component(name = "yield")
public class YieldStatementRule {

    @Symphonia.DI.Inject
    Parser parser;

    @Symphonia.DI.Inject
    EndStatementRule end;

    public YieldStatementNode parse(){
        YieldStatementNode.Builder nodeBuilder = new YieldStatementNode.Builder(this.parser.copyCurrentPosition(), new Node.Annotation[0]);
        this.parser.startAnnotationCapture();

        this.parser
                .nextIfType(Token.Type.KEYWORD_YIELD)
                .orElseThrow(() -> new IllegalStateException("Keyword 'yield' expected."));

        nodeBuilder.label(
                this.parser
                        .nextIfType(Token.Type.DOLLAR)
                        .map(ignore ->
                            this.parser
                                    .nextIfType(Token.Type.IDENTIFIER)
                                    .orElseThrow(() -> new IllegalStateException("Label identifier expected."))
                                    .value()
                        ).orElse(this.parser.labelsStack().peek())
        );

        try{
            this.end.expect();
        }catch (Exception e){
            nodeBuilder.value(this.parser.parseExpression());
            this.end.expect();
        }
        return nodeBuilder.build();
    }

}