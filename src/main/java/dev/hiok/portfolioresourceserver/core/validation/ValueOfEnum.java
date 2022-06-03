package dev.hiok.portfolioresourceserver.core.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;

@Documented
@Target({ 
  ElementType.METHOD, 
  ElementType.FIELD, 
  ElementType.ANNOTATION_TYPE, 
  ElementType.CONSTRUCTOR, 
  ElementType.PARAMETER,
  ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValueOfEnumValidator.class })
@NotNull
@ReportAsSingleViolation
public @interface ValueOfEnum {
  
  Class<? extends Enum<?>> enumClass();

  String message() default "Invalid value";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

}
