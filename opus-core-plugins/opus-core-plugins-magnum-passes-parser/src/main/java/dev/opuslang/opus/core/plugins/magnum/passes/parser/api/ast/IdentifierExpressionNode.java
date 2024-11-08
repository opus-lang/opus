package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class IdentifierExpressionNode extends ExpressionNode implements Visitable {

    private final String identifier;

    public IdentifierExpressionNode(Position position, Annotation[] annotations, String identifier) {
        super(position, annotations);
        this.identifier = identifier;
    }

    public String identifier(){
        return this.identifier;
    }

}
