package az.projectdailyreport.projectdailyreport.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CrocusoftEmailValidator.class)
public @interface CrocusoftEmail {
    String message() default "E-posta adresi geçerli değil";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

