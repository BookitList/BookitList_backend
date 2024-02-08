package cotato.bookitlist.book.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsbnValidator.class)
public @interface IsValidIsbn {

    String message() default "잘못된 형식의 isbn13입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

