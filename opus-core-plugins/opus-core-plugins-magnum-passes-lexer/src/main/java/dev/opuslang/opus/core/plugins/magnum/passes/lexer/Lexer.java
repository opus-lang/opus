package dev.opuslang.opus.core.plugins.magnum.passes.lexer;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules.AbstractLexer;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.utils.Characters;

public class Lexer extends AbstractLexer {

    public Lexer(SourceScanner scanner) {
        super(scanner);
    }

    public Token next(){
        char c = this.scanner.peek();
        return switch (c){
            case '{' -> this.simple.simple(Token.Type.LCURLY);
            case '}' -> this.simple.simple(Token.Type.RCURLY);
            case '(' -> this.simple.simple(Token.Type.LPARENTHESIS);
            case ')' -> this.simple.simple(Token.Type.RPARENTHESIS);
            case '[' -> this.simple.simple(Token.Type.LSQUARE);
            case ']' -> this.simple.simple(Token.Type.RSQUARE);
            case '\\' -> this.simple.simple(Token.Type.KEYWORD_BACKSLASH);

            case '#' -> this.simple.simple(Token.Type.VOID);
            case '@' -> this.simple.simple(Token.Type.AT);
            case ';' -> this.simple.simple(Token.Type.SEMICOLON);

            case ',' -> this.simple.simple(Token.Type.COMMA);

            case '?' -> this.simple.simple(Token.Type.QUESTION);

            case '&' -> this.simple.simple(Token.Type.OPERATOR_BITWISE_AND);
            case '|' -> this.simple.simple(Token.Type.OPERATOR_BITWISE_OR);
            case '^' -> this.simple.simple(Token.Type.OPERATOR_BITWISE_XOR);
            case '~' -> {
                if(this.scanner.peek(1) == '/'){
                    yield this.simple.simple(2, Token.Type.OPERATOR_INTEGERDIVIDE);
                }
                yield this.simple.simple(Token.Type.OPERATOR_BITWISE_NOT);
            }
            case '$' -> this.simple.simple(Token.Type.DOLLAR);

            case '.' -> {
                if(Characters.isDecimal(this.scanner.peek(1))){
                    yield this.number.lex();
                }
                if(this.scanner.peek(1) == '.' && this.scanner.peek(2) == '.'){
                    yield this.simple.simple(3, Token.Type.ELLIPSIS);
                }
                yield this.simple.simple(Token.Type.DOT);
            }

            case '!' -> {
                if(this.scanner.peek(1) == '='){
                    yield this.simple.simple(2, Token.Type.LOGIC_NOTEQUALS);
                }
                yield this.simple.simple(Token.Type.BANG);
            }

            case '-' -> {
                if(this.scanner.peek(1) == '>'){
                    yield this.simple.simple(2, Token.Type.ARROW);
                }
                yield this.simple.simple(Token.Type.OPERATOR_MINUS);
            }
            case '+' -> this.simple.simple(Token.Type.OPERATOR_PLUS);
            case '*' -> {
                if(this.scanner.peek(1) == '*'){
                    yield this.simple.simple(2, Token.Type.OPERATOR_POWER);
                }
                yield this.simple.simple(Token.Type.OPERATOR_MULTIPLY);
            }
            case '/' -> switch (this.scanner.peek(1)){
                case '/' -> this.comment.lexSingleline();
                case '*' -> this.comment.lexMultiline();
                default -> this.simple.simple(Token.Type.OPERATOR_DIVIDE);
            };
            case '>' -> {
                // TODO: handle bit shift operators.
                if(this.scanner.peek(1) == '='){
                    yield this.simple.simple(2, Token.Type.LOGIC_GREATEREQUALS);
                }
                yield this.simple.simple(Token.Type.LOGIC_GREATER);
            }
            case '<' -> {
                // TODO: handle bit shift operators.
                if(this.scanner.peek(1) == '='){
                    yield this.simple.simple(2, Token.Type.LOGIC_LESSEQUALS);
                }
                yield this.simple.simple(Token.Type.LOGIC_LESS);
            }

            case ':' -> switch (this.scanner.peek(1)){
                case '=' -> this.simple.simple(2, Token.Type.OPERATOR_WALRUS);
                case ':' -> this.simple.simple(2, Token.Type.DOUBLECOLON);
                default ->  this.simple.simple(Token.Type.COLON);
            };

            case '\'' -> this.character.lex();
            case '"' -> this.string.lexNormal();
            case '`' -> this.string.lexRaw();
            case '\0' -> this.simple.simple(Token.Type.EOF);
            default -> {
                if(Characters.isWhitespace(c)){
                    this.scanner.next();
                    yield this.next();
                }

                if(Characters.isDecimal(c)){
                    yield this.number.lex();
                }

                if(Characters.isIdentifierStart(c)){
                    yield this.identifier.lex();
                }

                throw new IllegalStateException(String.format("Unexpected character '%c' at '%d:%d'.", c, this.scanner.cursor().line(), this.scanner.cursor().column()));
            }
        };
    }

}
