package dev.hiok.portfolioresourceserver.api.feedback.model.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackScreenshotResponse {
  
  private UUID id;
  private String filename;
  private String contentType;
  private Long size;
  
}
