package dev.opuslang.opus.core.plugins.magnum.api.pipeline.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PassConfiguration {

    String name() default "";
    String id();
    String[] dependencies() default {};

}
