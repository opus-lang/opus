package dev.opuslang.opus.symphonia.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class Symphonia {
    private Symphonia(){}

    public static final class DI{
        private DI(){}

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.PACKAGE)
        public @interface Package {
            String outputClass();
            Provider[] additionalProviders() default {};
        }

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.PACKAGE)
        public @interface Provider {
            Class<?> type();
            String name();
            boolean required() default false;
        }

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.TYPE)
        public @interface Component {
            String name() default "";
        }

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.FIELD)
        public @interface Inject {
        }
    }

    public static final class Visitor{
        private Visitor(){}

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.PACKAGE)
        public @interface Package {
            String outputClass();
            String outputInterface();
        }

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.TYPE)
        public @interface Visitable {
            int order() default 0;
        }
    }

}
