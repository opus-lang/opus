package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.utils.Characters;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.Component(name = "identifier")
public class IdentifierRule {

    @Symphonia.Inject
    SourceScanner scanner;

    @Symphonia.Inject
    SimpleRule simple;

    public Token lex(){
        assert Characters.isIdentifierStart(this.scanner.peek());
        Cursor position = new Cursor(this.scanner.cursor());

        int start = this.scanner.cursor().offset();
        while(Characters.isIdentifierPart(this.scanner.peek())){
            this.scanner.next();
        }
        String identifier = this.scanner.source().substring(start, this.scanner.cursor().offset());
        return switch (identifier){
            case "namespace" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_NAMESPACE, position);
            case "import" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_IMPORT, position);
            case "_" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_UNDERSCORE, position);
            case "as" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_AS, position);
            case "fn" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_FN, position);
            case "ret" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_RET, position);
            case "def" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_DEF, position);
            case "mut" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_MUT, position);
            case "const" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_CONST, position);
            case "defer" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_DEFER, position);
            case "loop" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_LOOP, position);
            case "for" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_FOR, position);
            case "while" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_WHILE, position);
            case "do" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_DO, position);
            case "break" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_BREAK, position);
            case "continue" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_CONTINUE, position);
            case "switch" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_SWITCH, position);
            case "case" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_CASE, position);
            case "fallthrough" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_FALLTHROUGH, position);
            case "if" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_IF, position);
            case "else" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_ELSE, position);
            case "null" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_NULL, position);
            case "true" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_TRUE, position);
            case "false" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_FALSE, position);
            case "type" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_TYPE, position);
            case "distinct" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_DISTINCT, position);
            case "ext" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_EXT, position);
            case "class" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_CLASS, position);
            case "unsealed" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_UNSEALED, position);
            case "struct" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_STRUCT, position);
            case "unsafe" -> this.simple.simple(identifier.length(), Token.Type.KEYWORD_UNSAFE, position);
            default -> new Token(Token.Type.IDENTIFIER, identifier, position);
        };
    }

}
