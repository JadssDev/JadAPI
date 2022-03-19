package dev.jadss.jadapi.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This is an extremely Beta Method.
 */
@Retention(value = RetentionPolicy.SOURCE)
public @interface Beta {

    String description() default "none";
}
