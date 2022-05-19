package dev.jadss.jadapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ForRemoval {
    
    boolean willBeRemoved() default true;
    
    String expectedVersionForRemoval() default "none";

    String reason() default "none";
}
