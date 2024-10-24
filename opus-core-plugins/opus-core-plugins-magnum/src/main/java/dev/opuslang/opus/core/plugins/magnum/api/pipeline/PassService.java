package dev.opuslang.opus.core.plugins.magnum.api.pipeline;

public interface PassService<T> {

    T execute(PassContext context, String[] args);

}
