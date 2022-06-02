package dev.hiok.portfolioresourceserver.core.validation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
  
  private List<String> acceptedValues;

  @Override
  public void initialize(ValueOfEnum constraintAnnotation) {
    this.acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
      .map(Enum::name)
      .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    return value == null || this.acceptedValues.contains(value.toString());
  }
}
