package dev.hiok.portfolioresourceserver.api.modules.feedback.model.request;

import javax.validation.constraints.NotBlank;

import dev.hiok.portfolioresourceserver.core.validation.ValueOfEnum;
import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFeedbackStatusRequest {
  
  @ApiModelProperty(value = "New status of feedback", example = "RESOLVED", required = true)
  @NotBlank
  @ValueOfEnum(enumClass = FeedbackStatus.class)
  private FeedbackStatus status;

}
