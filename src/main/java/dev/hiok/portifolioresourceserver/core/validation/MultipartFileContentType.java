package dev.hiok.portifolioresourceserver.core.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

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
@Constraint(validatedBy = { MultipartFileContentTypeValidator.class })
public @interface MultipartFileContentType {
  
  String message() default "Invalid file size";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

  String[] allowed();
}
