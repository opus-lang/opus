package dev.opuslang.opus.core.plugins.magnum.passes.importcollector;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportCollectorVisitor extends AbstractVisitor<Void> {

    private final File currentFile;
    private final List<File> collectedImports;

    public ImportCollectorVisitor(File currentFile){
        this.currentFile = currentFile;
        this.collectedImports = new ArrayList<>();
    }

    public List<File> collectedImports() {
        return this.collectedImports;
    }

    @Override
    protected Void visit_MemberAccessExpressionNode(MemberAccessExpressionNode visitable) {
        this.visit(visitable.parent());
        return null;
    }

    @Override
    protected Void visit_MemberCallExpressionNode(MemberCallExpressionNode visitable) {
        this.visit(visitable.callable());
        Arrays.stream(visitable.arguments()).forEach(this::visit);
        return null;
    }

    @Override
    protected Void visit_InferTypeExpressionNode(InferTypeExpressionNode visitable) {
        return null;
    }

    @Override
    protected Void visit_BlockExpressionNode(BlockExpressionNode visitable) {
        Arrays.stream(visitable.statements()).forEach(this::visit);
        return null;
    }

    @Override
    protected Void visit_BinaryExpressionNode(BinaryExpressionNode visitable) {
        this.visit(visitable.left());
        this.visit(visitable.right());
        return null;
    }

    @Override
    protected Void visit_EmptyStatementNode(EmptyStatementNode visitable) {
        return null;
    }

    @Override
    protected Void visit_ImportExpressionNode(ImportExpressionNode visitable) {
        this.collectedImports().add(currentFile.toPath().toAbsolutePath().getParent().resolve(visitable.path()).toFile());
        return null;
    }

    @Override
    protected Void visit_YieldStatementNode(YieldStatementNode visitable) {
        this.visit(visitable.value());
        return null;
    }

    @Override
    protected Void visit_LambdaExpressionNode(LambdaExpressionNode visitable) {
        this.visit(visitable.body());
        return null;
    }

    @Override
    protected Void visit_LoopExpressionNode(LoopExpressionNode visitable) {
        this.visit(visitable.body());
        return null;
    }

    @Override
    protected Void visit_AssignmentStatementNode(AssignmentStatementNode visitable) {
        this.visit(visitable.target());
        this.visit(visitable.value());
        return null;
    }

    @Override
    protected Void visit_SourceFile(SourceFile visitable) {
        Arrays.stream(visitable.statements()).forEach(this::visit);
        return null;
    }

    @Override
    protected Void visit_UnaryExpressionNode(UnaryExpressionNode visitable) {
        this.visit(visitable.right());
        return null;
    }

    @Override
    protected Void visit_IgnoredExpressionStatementNode(IgnoredExpressionStatementNode visitable) {
        this.visit(visitable.expression());
        return null;
    }

    @Override
    protected Void visit_LiteralExpressionNode(LiteralExpressionNode visitable) {
        return null;
    }

    @Override
    protected Void visit_DefinitionStatementNode(DefinitionStatementNode visitable) {
        this.visit(visitable.assignedValue());
        this.visit(visitable.type());
        return null;
    }

    @Override
    protected Void visit_IdentifierExpressionNode(IdentifierExpressionNode visitable) {
        return null;
    }

    @Override
    protected Void visit_FunctionTypeExpressionNode(FunctionTypeExpressionNode visitable) {
        this.visit(visitable.returnType());
        Arrays.stream(visitable.argumentTypes()).forEach(this::visit);
        return null;
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

    @Override
    protected Void visit_default(Visitable visitable) {
        return null;
    }
}
