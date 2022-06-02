package dev.hiok.portfolioresourceserver.api.feedback.model.response;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackResponse {
  
  private UUID id;
  private FeedbackType type;
  private String comment;
  private Boolean hasScreenshot;
  private FeedbackStatus status;
  private OffsetDateTime createdAt;
  private OffsetDateTime modifiedAt;
  
}
