package dev.opuslang.opus.core.plugins.magnum.passes.astdump;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class ASTDumpVisitor extends AbstractVisitor<String> {

    private final String filename;
    private int indentation;

    public ASTDumpVisitor(File file){
        this.filename = file.getName();
        this.indentation = 0;
    }

    @Override
    protected String visit_MemberCallExpressionNode(MemberCallExpressionNode visitable) {
        return String.format("%s(%s)", this.visit(visitable.callable()), String.join(", ", Arrays.stream(visitable.arguments()).map(this::visit).toList()));
    }

    @Override
    protected String visit_MemberAccessExpressionNode(MemberAccessExpressionNode visitable) {
        return String.format("(%s%s%s)", this.visit(visitable.parent()), visitable.isStatic() ? "::" : ".", visitable.member());
    }

    @Override
    protected String visit_SourceFile(SourceFile visitable) {
        return String.format("""
                 /* AST Dump of '%s' */
                 %s""",
                filename,
                String.join(System.lineSeparator(), Arrays.stream(visitable.statements()).map(this::visit).toList()));
    }

    @Override
    protected String visit_InferTypeExpressionNode(InferTypeExpressionNode visitable) {
        return "<infer>";
    }

    @Override
    protected String visit_BlockExpressionNode(BlockExpressionNode visitable) {
        this.indentation++;
        String result = String.format("""
                $%s {
                %s
                %s}""",
                visitable.label(),
                String.join(System.lineSeparator(), Arrays.stream(visitable.statements()).map((v) -> "\t".repeat(this.indentation) + this.visit(v)).toList()),
                "\t".repeat(--this.indentation)
        );
        return result;
    }

    @Override
    protected String visit_BinaryExpressionNode(BinaryExpressionNode visitable) {
        return String.format("(%s %s %s)", this.visit(visitable.left()), visitable.operator().name(), this.visit(visitable.right()));
    }

    @Override
    protected String visit_EmptyStatementNode(EmptyStatementNode visitable) {
        return "/* empty */;";
    }

    @Override
    protected String visit_YieldStatementNode(YieldStatementNode visitable) {
        return String.format("yield $%s %s;", visitable.label(), this.visit(visitable.value()));
    }

    @Override
    protected String visit_LambdaExpressionNode(LambdaExpressionNode visitable) {
        return String.format("\\(%s) -> %s",
                String.join(", ", Arrays.stream(visitable.argumentNames()).toList()),
                this.visit(visitable.body()));
    }

    @Override
    protected String visit_LoopExpressionNode(LoopExpressionNode visitable) {
        return String.format("loop %s", this.visit(visitable.body()));
    }

    @Override
    protected String visit_AssignmentStatementNode(AssignmentStatementNode visitable) {
        return String.format("%s %s %s;", this.visit(visitable.target()), visitable.operator().name(), this.visit(visitable.value()));
    }

    @Override
    protected String visit_UnaryExpressionNode(UnaryExpressionNode visitable) {
        return String.format("%s %s", visitable.operator().name(), this.visit(visitable.right()));
    }

    @Override
    protected String visit_IgnoredExpressionStatementNode(IgnoredExpressionStatementNode visitable) {
        return String.format("%s;", this.visit(visitable.expression()));
    }

    @Override
    protected String visit_DefinitionStatementNode(DefinitionStatementNode visitable) {
        return String.format("def %s %s: %s%s;",
                String.join(" ", visitable.modifiers().stream().map(Enum::name).toList()),
                visitable.name(),
                this.visit(visitable.type()),
                Optional.ofNullable(visitable.assignedValue()).map(v -> " := " + this.visit(v)).orElse("")
        );
    }

    @Override
    protected String visit_IdentifierExpressionNode(IdentifierExpressionNode visitable) {
        return visitable.identifier();
    }

    @Override
    protected String visit_FunctionTypeExpressionNode(FunctionTypeExpressionNode visitable) {
        return String.format("fn (%s) -> %s",
                String.join(", ", Arrays.stream(visitable.argumentTypes()).map(this::visit).toList()),
                this.visit(visitable.returnType())
        );
    }

    @Override
    protected String visit_ConditionalExpressionNode(ConditionalExpressionNode visitable) {
        return String.format("if (%s) %s else %s",
                this.visit(visitable.condition()),
                this.visit(visitable.body()),
                this.visit(visitable.elseExpression())
        );
    }

    @Override
    protected String visit_ImportExpressionNode(ImportExpressionNode visitable) {
        return String.format("import \"%s\" using \"%s\"", visitable.path(), visitable.processor());
    }

    @Override
    protected String visit_LiteralExpressionNode(LiteralExpressionNode visitable) {
        return switch (visitable.kind()){
            case STRING -> "\""+visitable.value()+"\"";
            case CHARACTER -> "'"+visitable.value()+"'";
            case INTEGER, BOOLEAN -> visitable.value();
            case FLOATING -> visitable.value()+"f";
            case VOID -> "#";
            case NEVER -> "<never>";
        };
    }

    @Override
    protected String visit_ExpressionNode(ExpressionNode visitable) {
        return super.visit_ExpressionNode(visitable);
    }

    @Override
    protected String visit_StatementNode(StatementNode visitable) {
        return super.visit_StatementNode(visitable);
    }

    @Override
    protected String visit_Node(Node visitable) {
        return super.visit_Node(visitable);
    }

    @Override
    protected String visit_default(Visitable visitable) {
        return String.format("<Unknown %s>", visitable.getClass().getName());
    }
}
