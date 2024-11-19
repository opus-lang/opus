package dev.opuslang.opus.symphonia.utils;

import javax.lang.model.type.MirroredTypeException;
import java.util.function.Supplier;

public final class SymphoniaUtils {
    private SymphoniaUtils(){}

    public static MirroredTypeException getClassSafely(Supplier<Class<?>> classSupplier) {
        try {
            classSupplier.get();
            assert false; // must never happen
        } catch (MirroredTypeException e) {
            return e;
        }
        assert false; // must never happen
        return new MirroredTypeException(null);
    }

}
