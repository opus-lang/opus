package dev.opuslang.opus.core.plugins.magnum.api.pipeline;

import java.io.File;
import java.util.List;
import java.util.Map;

public non-sealed abstract class SynchronizedPassService<T> implements PassService<T>{

    public abstract Map<File, T> execute(List<File> files, Map<File, PassContext> contexts, String[] args);

    @Override
    public final boolean isIndependent() {
        return false;
    }
}
