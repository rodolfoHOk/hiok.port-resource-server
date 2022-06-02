package dev.hiok.portfolioresourceserver.api.feedback.model.request;

import javax.validation.constraints.NotBlank;

import dev.hiok.portfolioresourceserver.core.validation.ValueOfEnum;
import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFeedbackStatusRequest {
  
  @NotBlank
  @ValueOfEnum(enumClass = FeedbackStatus.class)
  private FeedbackStatus status;

}
