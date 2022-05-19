package dev.jadss.jadapi.annotations;

public @interface ForChange {

    boolean isMajor() default false;

    String expectedVersionForChange() default "none";

    String reason() default "none";
}
