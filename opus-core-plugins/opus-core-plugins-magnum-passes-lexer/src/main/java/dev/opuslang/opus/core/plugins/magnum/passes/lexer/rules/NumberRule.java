package dev.opuslang.opus.core.plugins.magnum.passes.lexer.rules;

import dev.opuslang.opus.core.plugins.magnum.passes.lexer.SourceScanner;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Cursor;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.api.Token;
import dev.opuslang.opus.core.plugins.magnum.passes.lexer.utils.Characters;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

@Symphonia.DI.Component(name = "number")
public class NumberRule {

    private enum Mode{
        BINARY(2), OCTAL(8), DECIMAL(10), HEXADECIMAL(16);

        public final int radix;
        Mode(int radix) {
            this.radix = radix;
        }
    }

    @Symphonia.DI.Inject
    SourceScanner scanner;

    public Token lex(){
        assert this.scanner.peek() == '.' || Characters.isDecimal(this.scanner.peek());
        StringBuilder sb = new StringBuilder();
        Cursor position = new Cursor(this.scanner.cursor());
        Token.Type type = Token.Type.INTEGER;

        Mode mode = Mode.DECIMAL;
        if(this.scanner.peek() == '0'){
            char marker = this.scanner.peek(1);
            mode = switch(marker){
                case 'b' -> {
                    this.scanner.next();
                    yield Mode.BINARY;
                }
                case 'o' -> {
                    this.scanner.next();
                    yield Mode.OCTAL;
                }
                case 'x' -> {
                    this.scanner.next();
                    yield Mode.HEXADECIMAL;
                }
                default -> {
                    if(!Characters.isDecimal(marker)) throw new IllegalStateException("Invalid number representation marker.");
                    yield Mode.DECIMAL;
                }
            };
        }
        // skip prefix
        int start = this.scanner.cursor().offset();

        while(true){
            char c = this.scanner.peek();

            if(Characters.isNumber(c, mode.radix)) {
                this.scanner.next();
            } else if(c == '_'){
                sb.append(this.scanner.source(), start, this.scanner.cursor().offset());
                this.scanner.next(); // skip the underscore
                start = this.scanner.cursor().offset();
            }else if(c == '.'){
                if(mode != Mode.DECIMAL) throw new IllegalStateException("Floating numbers must be written in a decimal representation.");
                if(type == Token.Type.FLOATING) throw new IllegalStateException("Floating number can contain only one dot.");
                type = Token.Type.FLOATING;
                this.scanner.next();
            }else{
                break;
            }
        }
        sb.append(this.scanner.source(), start, this.scanner.cursor().offset());
        if(type == Token.Type.FLOATING){
            return new Token(type, sb.toString(), position);
        }

        try {
            return new Token(type, String.valueOf(Long.parseLong(sb.toString(), mode.radix)), position);
        }catch(NumberFormatException e){
            throw new IllegalStateException("Invalid numeric literal.");
        }
    }

}
