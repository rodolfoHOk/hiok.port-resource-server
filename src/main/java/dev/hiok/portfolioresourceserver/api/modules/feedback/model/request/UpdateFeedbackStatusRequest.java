package dev.hiok.portfolioresourceserver.api.modules.feedback.model.request;

import dev.hiok.portfolioresourceserver.core.validation.ValueOfEnum;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFeedbackStatusRequest {
  
  @ApiModelProperty(value = "New feedback status", example = "RESOLVED", required = true)
  @ValueOfEnum(enumClass = FeedbackStatus.class)
  private String status;

}
