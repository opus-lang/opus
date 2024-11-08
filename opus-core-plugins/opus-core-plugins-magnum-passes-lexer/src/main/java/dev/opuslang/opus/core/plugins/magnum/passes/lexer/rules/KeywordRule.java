package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Optional;

@Symphonia.DI.Component(name = "keyword")
public class KeywordRule {

    @Symphonia.DI.Inject
    SourceScanner scanner;

    public Optional<Token.Type> lex(String identifier){
        return Optional.ofNullable(switch (identifier){
            case "import" -> Token.Type.KEYWORD_IMPORT;
            case "yield" -> Token.Type.KEYWORD_YIELD;
            case "_" -> Token.Type.KEYWORD_UNDERSCORE;
            case "as" -> Token.Type.KEYWORD_AS;
            case "fn" -> Token.Type.KEYWORD_FN;
            case "ret" -> Token.Type.KEYWORD_RET;
            case "def" -> Token.Type.KEYWORD_DEF;
            case "mut" -> Token.Type.KEYWORD_MUT;
            case "const" -> Token.Type.KEYWORD_CONST;
            case "defer" -> Token.Type.KEYWORD_DEFER;
            case "loop" -> Token.Type.KEYWORD_LOOP;
            case "for" -> Token.Type.KEYWORD_FOR;
            case "while" -> Token.Type.KEYWORD_WHILE;
            case "do" -> Token.Type.KEYWORD_DO;
            case "break" -> Token.Type.KEYWORD_BREAK;
            case "continue" -> Token.Type.KEYWORD_CONTINUE;
            case "switch" -> Token.Type.KEYWORD_SWITCH;
            case "case" -> Token.Type.KEYWORD_CASE;
            case "fallthrough" -> Token.Type.KEYWORD_FALLTHROUGH;
            case "if" -> Token.Type.KEYWORD_IF;
            case "else" -> Token.Type.KEYWORD_ELSE;
            case "null" -> Token.Type.KEYWORD_NULL;
            case "true" -> Token.Type.KEYWORD_TRUE;
            case "false" -> Token.Type.KEYWORD_FALSE;
            case "type" -> Token.Type.KEYWORD_TYPE;
            case "distinct" -> Token.Type.KEYWORD_DISTINCT;
            case "ext" -> Token.Type.KEYWORD_EXT;
            case "class" -> Token.Type.KEYWORD_CLASS;
            case "unsealed" -> Token.Type.KEYWORD_UNSEALED;
            case "struct" -> Token.Type.KEYWORD_STRUCT;
            case "unsafe" -> Token.Type.KEYWORD_UNSAFE;
            case "never" -> Token.Type.KEYWORD_NEVER;
            case "infer" -> Token.Type.KEYWORD_INFER;
            default -> null;
        });
    }

}
