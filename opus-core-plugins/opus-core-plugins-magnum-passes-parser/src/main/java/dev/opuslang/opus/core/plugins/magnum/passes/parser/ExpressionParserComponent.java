package dev.opuslang.opus.core.plugins.magnum.passes.parser;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.LiteralKind;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.Operator;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast.ExpressionNode;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.components.tdop.TDOPParserComponent;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.rules.expressions.*;
import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Precedence;

import java.util.Arrays;
import java.util.Map;

public class ExpressionParserComponent extends TDOPParserComponent<ExpressionNode> {

    // TODO: Move to DAG-based precedence: Improved operator declarations (https://github.com/swiftlang/swift-evolution/blob/main/proposals/0077-operator-precedence.md)
    private enum Precedences{
        NONE,
        DISJUNCTION,
        CONJUNCTION,
        RELATIVE,
        CASTING,
        ADDITION,
        MULTIPLICATION,
        EXPONENTIATION,
        PREFIX,
        MEMBER
    }

//    private static final class Precedence{
//        private Precedence() {}
//
//        private static final AtomicInteger incrementer = new AtomicInteger(0);
//
//        public static final int NONE = incrementer.getAndIncrement();
//        public static final int RELATIVE = incrementer.getAndIncrement();
//        public static final int ADDITIVE = incrementer.getAndIncrement();
//        public static final int MULTIPLICATIVE = incrementer.getAndIncrement();
//
//        public static final int PREFIX = 70;
//    }

    public ExpressionParserComponent(Parser parser) {
        super(parser);
        Precedence<Precedences> precedence = new Precedence<>(Precedences.class, 0, 10);

        Arrays.stream(Precedences.values()).forEach(precedence::getOrInit);

        BlockExpressionRule blockExpressionRule;
        this.setRules(
                Map.ofEntries(
                        Map.entry(Token.Type.LOGIC_OR, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.DISJUNCTION), false, Token.Type.LOGIC_OR, Operator.LOGIC_OR)),

                        Map.entry(Token.Type.LOGIC_AND, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.CONJUNCTION), false, Token.Type.LOGIC_AND, Operator.LOGIC_AND)),

                        Map.entry(Token.Type.LOGIC_LESS, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.RELATIVE), false, Token.Type.LOGIC_LESS, Operator.LOGIC_LESS)),
                        Map.entry(Token.Type.LOGIC_LESSEQUALS, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.RELATIVE), false, Token.Type.LOGIC_LESSEQUALS, Operator.LOGIC_LESSEQUALS)),
                        Map.entry(Token.Type.LOGIC_GREATER, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.RELATIVE), false, Token.Type.LOGIC_GREATER, Operator.LOGIC_GREATER)),
                        Map.entry(Token.Type.LOGIC_GREATEREQUALS, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.RELATIVE), false, Token.Type.LOGIC_GREATEREQUALS, Operator.LOGIC_GREATEREQUALS)),
                        Map.entry(Token.Type.LOGIC_NOTEQUALS, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.RELATIVE), false, Token.Type.LOGIC_NOTEQUALS, Operator.LOGIC_NOTEQUALS)),
                        Map.entry(Token.Type.LOGIC_EQUALS, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.RELATIVE), false, Token.Type.LOGIC_EQUALS, Operator.LOGIC_EQUALS)),

                        Map.entry(Token.Type.KEYWORD_AS, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.CASTING), false, Token.Type.KEYWORD_AS, Operator.CASTING_AS)),

                        Map.entry(Token.Type.OPERATOR_PLUS, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.ADDITION), false, Token.Type.OPERATOR_PLUS, Operator.PLUS)),
                        Map.entry(Token.Type.OPERATOR_MINUS, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.ADDITION), false, Token.Type.OPERATOR_MINUS, Operator.MINUS)),
                        Map.entry(Token.Type.OPERATOR_BITWISE_OR, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.ADDITION), false, Token.Type.OPERATOR_BITWISE_OR, Operator.BITWISE_OR)),
                        Map.entry(Token.Type.OPERATOR_BITWISE_XOR, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.ADDITION), false, Token.Type.OPERATOR_BITWISE_XOR, Operator.BITWISE_XOR)),

                        // TODO: add % operator
                        // TODO: add wrapping operators
                        // TODO: add shift operators
                        Map.entry(Token.Type.OPERATOR_MULTIPLY, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.MULTIPLICATION), false, Token.Type.OPERATOR_MULTIPLY, Operator.MULTIPLY)),
                        Map.entry(Token.Type.OPERATOR_DIVIDE, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.MULTIPLICATION), false, Token.Type.OPERATOR_DIVIDE, Operator.DIVIDE)),
                        Map.entry(Token.Type.OPERATOR_INTEGERDIVIDE, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.MULTIPLICATION), false, Token.Type.OPERATOR_INTEGERDIVIDE, Operator.INTEGERDIVIDE)),
                        Map.entry(Token.Type.OPERATOR_BITWISE_AND, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.MULTIPLICATION), false, Token.Type.OPERATOR_BITWISE_AND, Operator.BITWISE_AND)),

                        Map.entry(Token.Type.OPERATOR_POWER, new BinaryExpressionRule(this, precedence.getOrInit(Precedences.EXPONENTIATION), true, Token.Type.OPERATOR_POWER, Operator.POWER)),

                        Map.entry(Token.Type.LPARENTHESIS, new MemberCallExpressionRule(this, precedence.getOrInit(Precedences.MEMBER))),
                        Map.entry(Token.Type.DOT, new MemberAccessExpressionRule(this, precedence.getOrInit(Precedences.MEMBER), false)),
                        Map.entry(Token.Type.DOUBLECOLON, new MemberAccessExpressionRule(this, precedence.getOrInit(Precedences.MEMBER), true))
                ),
                Map.ofEntries(
                        Map.entry(Token.Type.IDENTIFIER, new IdentifierExpressionRule(this, precedence.getOrInit(Precedences.NONE))),
                        Map.entry(Token.Type.STRING, new LiteralExpressionRule(this, precedence.getOrInit(Precedences.NONE), Token.Type.STRING, LiteralKind.STRING)),
                        Map.entry(Token.Type.CHARACTER, new LiteralExpressionRule(this, precedence.getOrInit(Precedences.NONE), Token.Type.CHARACTER, LiteralKind.CHARACTER)),
                        Map.entry(Token.Type.INTEGER, new LiteralExpressionRule(this, precedence.getOrInit(Precedences.NONE), Token.Type.INTEGER, LiteralKind.INTEGER)),
                        Map.entry(Token.Type.FLOATING, new LiteralExpressionRule(this, precedence.getOrInit(Precedences.NONE), Token.Type.FLOATING, LiteralKind.FLOATING)),
                        Map.entry(Token.Type.BOOLEAN, new LiteralExpressionRule(this, precedence.getOrInit(Precedences.NONE), Token.Type.BOOLEAN, LiteralKind.BOOLEAN)),
                        Map.entry(Token.Type.VOID, new LiteralExpressionRule(this, precedence.getOrInit(Precedences.NONE), Token.Type.VOID, LiteralKind.VOID)),

                        Map.entry(Token.Type.DOLLAR, blockExpressionRule = new BlockExpressionRule(this, precedence.getOrInit(Precedences.NONE))),

                        Map.entry(Token.Type.KEYWORD_FN, new FunctionTypeExpressionRule(this, precedence.getOrInit(Precedences.NONE))),

                        Map.entry(Token.Type.KEYWORD_BACKSLASH, new LambdaExpressionRule(this, precedence.getOrInit(Precedences.NONE), blockExpressionRule)),

                        Map.entry(Token.Type.KEYWORD_IMPORT, new ImportExpressionRule(this, precedence.getOrInit(Precedences.NONE))),

                        Map.entry(Token.Type.LPARENTHESIS, new GroupExpressionRule(this, precedence.getOrInit(Precedences.NONE))),

                        Map.entry(Token.Type.KEYWORD_WHILE, new WhileLoopExpressionRule(this, precedence.getOrInit(Precedences.NONE), blockExpressionRule)),
                        Map.entry(Token.Type.KEYWORD_LOOP, new LoopExpressionRule(this, precedence.getOrInit(Precedences.NONE), blockExpressionRule)),

                        Map.entry(Token.Type.OPERATOR_MINUS, new UnaryExpressionRule(this, precedence.getOrInit(Precedences.PREFIX), Token.Type.OPERATOR_MINUS, Operator.MINUS)),
                        Map.entry(Token.Type.OPERATOR_BITWISE_NOT, new UnaryExpressionRule(this, precedence.getOrInit(Precedences.PREFIX), Token.Type.OPERATOR_BITWISE_NOT, Operator.BITWISE_NOT)),
                        Map.entry(Token.Type.BANG, new UnaryExpressionRule(this, precedence.getOrInit(Precedences.PREFIX), Token.Type.BANG, Operator.LOGIC_INVERT))
                )
        );
    }


}
