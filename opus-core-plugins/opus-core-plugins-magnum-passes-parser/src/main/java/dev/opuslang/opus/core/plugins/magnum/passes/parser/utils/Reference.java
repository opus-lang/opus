package dev.opuslang.opus.core.plugins.magnum.passes.parser.utils;

public class Reference<T> {

    private T value;

    public Reference(T value){
        this.value = value;
    }

    public T set(T newValue){
        T oldValue = value;
        value = newValue;
        return oldValue;
    }

    public T get(){
        return this.value;
    }

}
