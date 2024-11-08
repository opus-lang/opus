package dev.opuslang.opus.core.plugins.magnum.api.pipeline;

import java.util.HashMap;
import java.util.Map;

public class PassContext {

    private final Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value){
        if(this.data.containsKey(key)) throw new IllegalArgumentException("Key already exists.");
        this.data.put(key, value);
    }

    public Object rawGet(String key){
        if(!this.data.containsKey(key)) throw new IllegalArgumentException("Invalid key.");
        return this.data.get(key);
    }

    public <T> T get(String key, Class<T> type){
        Object value = this.rawGet(key);
        if(!type.isInstance(value)) throw new IllegalArgumentException("Invalid type.");
        return type.cast(value);
    }

}
