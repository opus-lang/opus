package dev.opuslang.opus.core.plugins.magnum.passes.parser.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<L, R> {

    private Either() {}

    // Factory methods
    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    public abstract <T> T map(Function<? super L, ? extends T> lFunc, Function<? super R, ? extends T> rFunc);

    public abstract <T> Either<T, R> mapLeft(Function<? super L, ? extends T> lFunc);

    public abstract <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc);

    public abstract void apply(Consumer<? super L> lFunc, Consumer<? super R> rFunc);

    public abstract boolean isLeft();
    public abstract boolean isRight();
    public abstract L getLeft();
    public abstract R getRight();

    private static final class Left<L, R> extends Either<L, R> {
        private final L value;

        private Left(L value) {
            this.value = value;
        }

        @Override
        public <T> T map(Function<? super L, ? extends T> lFunc, Function<? super R, ? extends T> rFunc) {
            return lFunc.apply(value);
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> lFunc) {
            return Either.left(lFunc.apply(value));
        }

        @Override
        public <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc) {
            return Either.left(value); // No transformation on Right; retains Left value
        }

        @Override
        public void apply(Consumer<? super L> lFunc, Consumer<? super R> rFunc) {
            lFunc.accept(value);
        }

        @Override
        public L getLeft() {
            return value;
        }

        @Override
        public R getRight() {
            throw new UnsupportedOperationException("No right value present in Left.");
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }
    }

    private static final class Right<L, R> extends Either<L, R> {
        private final R value;

        private Right(R value) {
            this.value = value;
        }

        @Override
        public <T> T map(Function<? super L, ? extends T> lFunc, Function<? super R, ? extends T> rFunc) {
            return rFunc.apply(value);
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> lFunc) {
            return Either.right(value); // No transformation on Left; retains Right value
        }

        @Override
        public <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc) {
            return Either.right(rFunc.apply(value));
        }

        @Override
        public void apply(Consumer<? super L> lFunc, Consumer<? super R> rFunc) {
            rFunc.accept(value);
        }

        @Override
        public L getLeft() {
            throw new UnsupportedOperationException("No left value present in Right.");
        }

        @Override
        public R getRight() {
            return value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }
    }
}
