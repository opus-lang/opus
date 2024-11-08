package dev.opuslang.opus.core.plugins.magnum.passes.parser.api.ast;

import dev.opuslang.opus.symphonia.annotation.Symphonia;

import java.util.Map;
import java.util.UUID;

@Symphonia.Visitor.Visitable(order = Integer.MAX_VALUE)
public non-sealed abstract class Node implements Visitable{

    public record Id(String name, String uuid, String threadId, String suffix){
        public static Id generate(String name){
            return new Id(
                    name,
                    UUID.randomUUID().toString(),
                    Thread.currentThread().getName(),
                    String.valueOf(Thread.currentThread().threadId())
            );
        }

        public String irIdentifier(){
            return String.format(
                    "<Generated[%s]: %s|%s|%s>",
                    this.name,
                    this.uuid,
                    this.threadId,
                    this.suffix
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
    private Id id;
    private final Position position;
    private final Annotation[] annotations;

    protected Node(Position position, Annotation[] annotations){
        this._name = this.getClass().getSimpleName();
        this.id = Id.generate(this._name);
        this.position = new Position(position); // copy
        this.annotations = annotations;
    }

    protected Node(Position position){
        this(position, new Annotation[0]);
    }

    public final String _name() {
        return this._name;
    }

    public final Id id() {
        return this.id;
    }

    public final Position position(){
        return this.position;
    }

    public final Annotation[] annotations() {
        return this.annotations;
    }

}
