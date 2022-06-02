package dev.hiok.portfolioresourceserver.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

  ACCESS_DENIED("/access-denied", "Access denied"),
  BUSINESS_ERROR("/business-error", "Business error"),
  ENTITY_IN_USE("/entity-in-use", "Entity in use"),
  INCOMPREHENSIBLE_MESSAGE("/incomprehensible-message", "Incomprehensible message"),
  INVALID_DATA("/invalid-data", "Invalid data"),
  INVALID_PARAMETER("/invalid-parameter", "Invalid parameter"),
  RESOURCE_NOT_FOUND ("/resource-not-found", "Resource not found"),
  SYSTEM_ERROR("/system-error", "System error"),
  UNSUPPORTED_MEDIA_TYPE("/unsupported-media-type", "Unsupported media type");

  private String type;
  private String title;

  private ProblemType(String type, String title) {
    this.type = type;
    this.title = title;
  }
  
}
