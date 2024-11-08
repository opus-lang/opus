package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.YieldStatementNode;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Optional;

@Symphonia.DI.Component
public class YieldStatementRule {

    @Symphonia.DI.Inject
    Parser parser;

    @Symphonia.DI.Inject
    EndStatementRule end;

    public YieldStatementNode parse(){
        Node.Position position = new Node.Position(this.parser.currentPosition());
        this.parser.startAnnotationCapture();

        this.parser
                .nextIfType(Token.Type.KEYWORD_YIELD)
                .orElseThrow(() -> new IllegalStateException("Keyword 'yield' expected."));

        String label = this.parser
                .nextIfType(Token.Type.DOLLAR)
                .map(ignore ->
                    this.parser
                            .nextIfType(Token.Type.IDENTIFIER)
                            .orElseThrow(() -> new IllegalStateException("Label identifier expected."))
                            .value()
                ).orElse(null);

        try{
            this.end.expect();
            return this.parser.createNode((ignore, annotations) -> new YieldStatementNode(position, annotations, label, null /* TODO: FIX!!!! Optional.empty() */));
        }catch (Exception e){
            ExpressionNode expression = this.parser.parseExpression();
            this.end.expect();
            return this.parser.createNode((ignore, annotations) -> new YieldStatementNode(position, annotations, label, null/* TODO: FIX!!!!  Optional.of(expression) */));
        }
    }

}
