package dev.opuslang.opus.core.plugins.magnum.passes.lexer.api;

public record Token(Type type, String value, Cursor position) {

    public Token(Type type, String value, Cursor position) {
        this.type = type;
        this.value = value;
        this.position = new Cursor(position); // Clone
    }

    public enum Type{
        // Special Characters
        SEMICOLON,
        COLON,
        DOUBLECOLON,
        COMMA,
        DOT,
        ELLIPSIS,
        QUESTION,
        BANG,
        ARROW,
        AT,
        // TODO: this conflicts with the bitwise and. Resolve somehow??
//        AMPERSAND,
        DOLLAR,
        LPARENTHESIS, RPARENTHESIS,
        LCURLY, RCURLY,
        LSQUARE, RSQUARE,


        // Keywords
        KEYWORD_IMPORT,
        KEYWORD_YIELD,
        KEYWORD_BACKSLASH, // lambda
        KEYWORD_NEVER,
        KEYWORD_UNDERSCORE, // used for default values
        KEYWORD_AS, // explicit casting // TODO: maybe rename to operator??
        KEYWORD_FN, // fn
        KEYWORD_RET,
        KEYWORD_DEF, // def
        KEYWORD_MUT,
        KEYWORD_TYPE,
        KEYWORD_CONST, // const
        KEYWORD_DEFER,
        KEYWORD_LOOP,
        KEYWORD_FOR,
        KEYWORD_WHILE,
        KEYWORD_DO,
        KEYWORD_BREAK,
        KEYWORD_CONTINUE,
        KEYWORD_SWITCH,
        KEYWORD_CASE,
        KEYWORD_FALLTHROUGH,
        KEYWORD_IF,
        KEYWORD_ELSE,
        KEYWORD_NULL,
        KEYWORD_DISTINCT,
        KEYWORD_EXT, // extension of types (extension methods attachment)
        KEYWORD_CLASS,
        KEYWORD_UNSEALED,
        KEYWORD_STRUCT,
        KEYWORD_UNSAFE,

        OPERATOR_BITWISE_AND,
        OPERATOR_BITWISE_OR,
        OPERATOR_BITWISE_XOR,
        OPERATOR_BITWISE_NOT,

        OPERATOR_WALRUS,
        OPERATOR_PLUSEQUALS,
        OPERATOR_MINUSEQUALS,
        OPERATOR_MULTIPLYEQUALS,
        OPERATOR_DIVIDEEQUALS,
        OPERATOR_INTEGERDIVIDEEQUALS,
        OPERATOR_POWEREQUALS,

        OPERATOR_PLUS,
        OPERATOR_MINUS,
        OPERATOR_MULTIPLY,
        OPERATOR_DIVIDE,
        OPERATOR_INTEGERDIVIDE,
        OPERATOR_POWER,

        LOGIC_EQUALS,
        LOGIC_NOTEQUALS,
        LOGIC_GREATEREQUALS,
        LOGIC_LESSEQUALS,
        LOGIC_GREATER,
        LOGIC_LESS,
        LOGIC_AND,
        LOGIC_OR,

        IDENTIFIER,
        STRING,
        CHARACTER,
        INTEGER,
        BOOLEAN,
        FLOATING,
        VOID,

        COMMENT,

        EOF
    }
}
