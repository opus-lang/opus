package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Visitor.Visitable
public final class FunctionTypeExpressionNode extends ExpressionNode implements Visitable{

    private final ExpressionNode[] argumentTypes;
    private final ExpressionNode returnType;

    public FunctionTypeExpressionNode(Position position, Annotation[] annotations, ExpressionNode[] argumentTypes, ExpressionNode returnType) {
        super(position, annotations);
        this.argumentTypes = argumentTypes;
        this.returnType = returnType;
    }

    public ExpressionNode[] argumentTypes() {
        return this.argumentTypes;
    }

    public ExpressionNode returnType() {
        return this.returnType;
    }

}