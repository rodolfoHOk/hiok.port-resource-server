package dev.hiok.portfolioresourceserver.core.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileContentTypeValidator implements ConstraintValidator<MultipartFileContentType, MultipartFile> {

  private List<String> allowed;
  
  @Override
  public void initialize(MultipartFileContentType constraintAnnotation) {
    this.allowed = Arrays.asList(constraintAnnotation.allowed());
  }

  @Override
  public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
    return value == null || this.allowed.contains(value.getContentType());
  }
  
}
