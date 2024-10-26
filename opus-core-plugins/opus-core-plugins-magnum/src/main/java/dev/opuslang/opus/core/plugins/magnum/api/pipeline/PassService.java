package dev.opuslang.opus.core.plugins.magnum.api.pipeline;

public sealed interface PassService<T> permits IndependentPassService, SynchronizedPassService {

    boolean isIndependent();

}
