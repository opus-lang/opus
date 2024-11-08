package dev.opuslang.opus.core.plugins.magnum.passes.parser;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;

import java.util.Arrays;
import java.util.Stack;

public class YieldRepairVisitor extends AbstractVisitor<Void> {

    private final Stack<BlockExpressionNode> scopeStack;

    public YieldRepairVisitor() {
        this.scopeStack = new Stack<>();
    }

    @Override
    protected Void visit_default(Visitable visitable) {
        return null;
    }

    @Override
    protected Void visit_InferTypeExpressionNode(InferTypeExpressionNode visitable) {
        return super.visit_InferTypeExpressionNode(visitable);
    }

    @Override
    protected Void visit_BlockExpressionNode(BlockExpressionNode visitable) {
        scopeStack.push(visitable);
        Arrays.stream(visitable.statements()).forEach(this::visit);
        scopeStack.pop();
        return super.visit_BlockExpressionNode(visitable);
    }

    @Override
    protected Void visit_VoidExpressionNode(VoidExpressionNode visitable) {
        return super.visit_VoidExpressionNode(visitable);
    }

    @Override
    protected Void visit_BinaryExpressionNode(BinaryExpressionNode visitable) {
        this.visit(visitable.left());
        this.visit(visitable.right());
        return super.visit_BinaryExpressionNode(visitable);
    }

    @Override
    protected Void visit_EmptyStatementNode(EmptyStatementNode visitable) {
        return super.visit_EmptyStatementNode(visitable);
    }

    @Override
    protected Void visit_YieldStatementNode(YieldStatementNode visitable) {
        if(visitable.label() == null){
            visitable.setLabel(scopeStack.peek().label());
        }
        this.visit(visitable.value());
        return super.visit_YieldStatementNode(visitable);
    }

    @Override
    protected Void visit_LambdaExpressionNode(LambdaExpressionNode visitable) {
        this.visit(visitable.body());
        return super.visit_LambdaExpressionNode(visitable);
    }

    @Override
    protected Void visit_LoopExpressionNode(LoopExpressionNode visitable) {
        this.visit(visitable.body());
        return super.visit_LoopExpressionNode(visitable);
    }

    @Override
    protected Void visit_AssignmentStatementNode(AssignmentStatementNode visitable) {
        this.visit(visitable.target());
        this.visit(visitable.value());
        return super.visit_AssignmentStatementNode(visitable);
    }

    @Override
    protected Void visit_SourceFile(SourceFile visitable) {
        Arrays.stream(visitable.statements()).forEach(this::visit);
        return super.visit_SourceFile(visitable);
    }

    @Override
    protected Void visit_NeverTypeExpressionNode(NeverTypeExpressionNode visitable) {
        return super.visit_NeverTypeExpressionNode(visitable);
    }

    @Override
    protected Void visit_UnaryExpressionNode(UnaryExpressionNode visitable) {
        this.visit(visitable.right());
        return super.visit_UnaryExpressionNode(visitable);
    }

    @Override
    protected Void visit_IgnoredExpressionStatementNode(IgnoredExpressionStatementNode visitable) {
        this.visit(visitable.expression());
        return super.visit_IgnoredExpressionStatementNode(visitable);
    }

    @Override
    protected Void visit_DefinitionStatementNode(DefinitionStatementNode visitable) {
        this.visit(visitable.type()); // could be skipped, since types must be known during compilation
        visitable.assignedValue().ifPresent(this::visit);
        return super.visit_DefinitionStatementNode(visitable);
    }

    @Override
    protected Void visit_IdentifierExpressionNode(IdentifierExpressionNode visitable) {
        return super.visit_IdentifierExpressionNode(visitable);
    }

    @Override
    protected Void visit_FunctionTypeExpressionNode(FunctionTypeExpressionNode visitable) {
        return super.visit_FunctionTypeExpressionNode(visitable);
    }

    @Override
    protected Void visit_ConditionalExpressionNode(ConditionalExpressionNode visitable) {
        this.visit(visitable.condition());
        this.visit(visitable.body());
        this.visit(visitable.elseExpression());
        return super.visit_ConditionalExpressionNode(visitable);
    }

    @Override
    protected Void visit_ExpressionNode(ExpressionNode visitable) {
        return super.visit_ExpressionNode(visitable);
    }

    @Override
    protected Void visit_StatementNode(StatementNode visitable) {
        return super.visit_StatementNode(visitable);
    }

    @Override
    protected Void visit_Node(Node visitable) {
        return super.visit_Node(visitable);
    }
}
