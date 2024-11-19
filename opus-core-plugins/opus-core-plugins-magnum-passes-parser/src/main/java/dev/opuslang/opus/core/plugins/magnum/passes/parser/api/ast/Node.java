package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.core.plugins.magnum.passes.parser.utils.Utils;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Map;
import java.util.UUID;

@Symphonia.Visitor.Visitable(order = Integer.MAX_VALUE)
public non-sealed abstract class Node implements Visitable{

    public record Id(String name, String id){
        public static Id generate(String name){
            return new Id(
                    name,
                    Utils.generateUniqueIdentifier()
            );
        }

        public String irIdentifier(){
            return String.format(
                    "<Generated[%s]: %s>",
                    this.name,
                    this.id
            );
        }
    }
    public record Position(int line, int column){
        public Position(Position position){
            this(position.line, position.column);
        }
    }
    public record Annotation(String name, Map<String, ExpressionNode> arguments){

    }

    private final String _name;
    private final Id id;

    protected Node(){
        this._name = this.getClass().getSimpleName();
        this.id = Id.generate(this._name);
    }

    public final Id id(){
        return this.id;
    }

    @Symphonia.Builder.Final
    public abstract Position position();
    @Symphonia.Builder.Final
    public abstract Annotation[] annotations();

}
