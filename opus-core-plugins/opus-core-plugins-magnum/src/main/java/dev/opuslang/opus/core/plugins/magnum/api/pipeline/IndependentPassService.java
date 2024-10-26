package dev.opuslang.opus.core.plugins.magnum.api.pipeline;

import java.io.File;

public non-sealed abstract class IndependentPassService<T> implements PassService<T> {

    public abstract T execute(File file, PassContext context, String[] args);

    @Override
    public final boolean isIndependent() {
        return true;
    }

}
