package dev.opuslang.opus.api.plugin.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.MODULE)
public @interface Plugin {
    String version();
    String name();
    String description() default "";
    String[] modulePath() default { "" };
    Plugin.InternalDependencyConfiguration[] internalDependencyConfigurations() default {};

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.MODULE)
    @interface InternalDependencyConfiguration {
        String id();
        String versionRange();
    }
}
