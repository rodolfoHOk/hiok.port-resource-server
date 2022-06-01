package dev.hiok.portifolioresourceserver.api.feedback.model.response;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedbackStatus;
import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedbackType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackResponse {
  
  private UUID id;
  private FeedbackType type;
  private String comment;
  private String screenshotUrl;
  private FeedbackStatus status;
  private OffsetDateTime createdAt;
  private OffsetDateTime modifiedAt;
  
}
