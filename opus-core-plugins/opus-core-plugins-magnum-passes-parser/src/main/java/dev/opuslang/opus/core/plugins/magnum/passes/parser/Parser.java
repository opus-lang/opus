package dev.opuslang.opus.core.plugins.magnum.passes.parser;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.TokenStream;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.*;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.ParserComponent;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Parser {

    private final TokenStream tokenStream;
    private final Deque<List<Node.Annotation>> annotationsStack;
    private final Deque<String> labelsStack;
    private final ParserComponent<StatementNode> statementParserComponent;
    private final ParserComponent<ExpressionNode> expressionParserComponent;
    private final ParserComponent<Node.Annotation> annotationParserComponent;

    public Parser(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
        this.annotationsStack = new ArrayDeque<>();
        this.labelsStack = new ArrayDeque<>();
        this.statementParserComponent = new StatementParserComponent(this);
        this.expressionParserComponent = new ExpressionParserComponent(this);
        this.annotationParserComponent = new AnnotationsParserComponent(this);
    }

    public SourceFile parse(){
        List<StatementNode> statements = new ArrayList<>();
        while(this.tokenStream.hasNext()){
            try {
                statements.add(this.parseStatement());
            }catch(Exception e){
                System.err.println("Error at: " + this.copyCurrentPosition());
                e.printStackTrace();
                break;
            }
        }
        return new SourceFile(statements.toArray(StatementNode[]::new));
    }

    public final StatementNode parseStatement() {
        return this.statementParserComponent.parse();
    }

    public final ExpressionNode parseExpression() {
        return this.expressionParserComponent.parse();
    }

    public final Deque<String> labelsStack(){
        return this.labelsStack;
    }

//    public final <T extends Node> T createNode(Function<Node.Position, T> nodeCreator){
//        return nodeCreator.apply(this.copyCurrentPosition());
//    }
//    public final <T extends Node> T createNode(BiFunction<Node.Position, Node.Annotation[], T> nodeCreator){
//        // TODO: fix annotations (see #next)
////        return nodeCreator.apply(this.currentPosition(), this.annotationsStack.pop().toArray(Node.Annotation[]::new));
//        return nodeCreator.apply(this.copyCurrentPosition(), new Node.Annotation[0]);
//    }

    public final void startAnnotationCapture(){
        this.annotationsStack.push(new ArrayList<>());
    }

    public Token next(){
        Token token = this.tokenStream.next();
        // TODO: add comments and annotations
        // Currently skipped as they cause a lot of problems.
        // Reference solution: https://meri-stuff.blogspot.com/2012/09/tackling-comments-in-antlr-compiler.html
        // Suggestion: store them in a Map, where Key is the ID of the node, and the value is the list of comments/annotations.
//        if(token.type() == Token.Type.COMMENT) return this.next();
        if(token.type() == Token.Type.COMMENT) return this.next();
        // TODO: fix annotations (currently, startAnnotationCapture is invoked too late due to the nature of parsers)
//        if(token.type() == Token.Type.AT) {
//            this.annotationsStack.peek().add(this.annotationParserComponent.parse());
//        }
        return token;
    }

    public Optional<Token> nextIf(Predicate<Token> condition) {
        return Optional.ofNullable(condition.test(this.peek()) ? this.next() : null);
    }

    public Optional<Token> nextIfType(Token.Type type){
        return this.nextIf(token -> token.type() == type);
    }

    public void consume(){
        this.tokenStream.consume();
    }

    public Token peek(){
        return this.peek(0);
    }

    public Token peek(int offset){
        Token token = this.tokenStream.peek(offset);

        if(token.type() == Token.Type.COMMENT) return this.tokenStream.peek(offset+1);

        return token;
    }

    public Node.Position copyCurrentPosition(){
        Cursor cursor = this.tokenStream.peek().position();
        return new Node.Position(cursor.line(), cursor.column());
    }

}
