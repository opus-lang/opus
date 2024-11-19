package dev.opuslang.opus.core.plugins.magnum.passes.parser.utils;

import java.util.UUID;
import java.util.function.Supplier;

public final class Utils {
    private Utils() {
    }

    public static String generateUniqueIdentifier() {
        return String.format("%s_%s_%s", UUID.randomUUID(), Thread.currentThread().getName(), Thread.currentThread().threadId());
    }

    public static <T> T evaluate(Supplier<T> supplier){
        return supplier.get();
    }

}

