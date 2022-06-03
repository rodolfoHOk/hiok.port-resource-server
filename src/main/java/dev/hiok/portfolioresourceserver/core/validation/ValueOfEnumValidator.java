package dev.hiok.portfolioresourceserver.core.validation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {
  
  private List<String> acceptedValues = null;

  @Override
  public void initialize(ValueOfEnum constraintAnnotation) {
    this.acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
      .map(Enum::name)
      .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return this.acceptedValues.contains(value.toUpperCase());
  }
}
