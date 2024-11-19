package dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.statements;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.Parser;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.DefinitionStatementNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.InferTypeExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.Node;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.*;

@Symphonia.DI.Component(name = "definition")
public class DefinitionStatementRule {

    @Symphonia.DI.Inject
    Parser parser;

    @Symphonia.DI.Inject
    EndStatementRule end;

    public DefinitionStatementNode parse(){
        DefinitionStatementNode.Builder nodeBuilder = new DefinitionStatementNode.Builder(this.parser.copyCurrentPosition(), new Node.Annotation[0]);

        this.parser.startAnnotationCapture();

        this.parser
                .nextIfType(Token.Type.KEYWORD_DEF)
                .orElseThrow(() -> new IllegalStateException("Keyword 'def' expected."));

        nodeBuilder.modifiers(EnumSet.noneOf(DefinitionStatementNode.Modifier.class));
        this.parser
                .nextIfType(Token.Type.KEYWORD_CONST)
                .ifPresent((ignore) -> nodeBuilder.modifiers().add(DefinitionStatementNode.Modifier.CONST));
        this.parser
                .nextIfType(Token.Type.KEYWORD_MUT)
                .ifPresent((ignore) -> nodeBuilder.modifiers().add(DefinitionStatementNode.Modifier.MUTABLE));

        nodeBuilder.name(this.parser
                .nextIfType(Token.Type.IDENTIFIER)
                .orElseThrow(
                        () -> new IllegalStateException("Variable identifier expected.")
                ).value()
        );

        nodeBuilder.type(
                this.parser
                        .nextIfType(Token.Type.COLON)
                        .map(ignore -> this.parser.parseExpression())
                        .orElse(new InferTypeExpressionNode.Builder(this.parser.copyCurrentPosition()).build())
        );

        nodeBuilder.assignedValue(
                this.parser
                        .nextIfType(Token.Type.OPERATOR_WALRUS)
                        .map(ignore -> this.parser.parseExpression())
                        .orElse(null)
        );

        this.end.expect();

        return nodeBuilder.build();
    }

}
