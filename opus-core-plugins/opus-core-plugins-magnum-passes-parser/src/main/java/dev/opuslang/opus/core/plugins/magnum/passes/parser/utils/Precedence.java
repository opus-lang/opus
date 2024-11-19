package dev.opuslang.opus.core.plugins.magnum.passes.parser.utils;

import java.util.EnumMap;

public final class Precedence<T extends Enum<T>> {

    private int value;
    private final int step;
    private final EnumMap<T, Integer> precedenceByName;

    public Precedence(Class<T> clazz, int min, int step) {
        this.value = min;
        this.step = step;
        this.precedenceByName = new EnumMap<>(clazz);
    }

    public int getOrInit(T key) {
        return this.precedenceByName.computeIfAbsent(key, k -> {
            int temp = this.value;
            this.value += this.step;
            return temp;
        });
    }

}