package dev.hiok.portfolioresourceserver.domain.exception;

public class EntityInUseException extends BusinessException {
  
  static final long serialVersionUID = 1L;

  public EntityInUseException(String message) {
    super(message);
  }
  
}
