package dev.hiok.portfolioresourceserver.api.modules.feedback.model.request;

import javax.validation.constraints.NotBlank;

import dev.hiok.portfolioresourceserver.core.validation.ValueOfEnum;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
  
  @ApiModelProperty(value = "Feedback type", example = "BUG", required = true)
  @ValueOfEnum(enumClass = FeedbackType.class)
  private String type;

  @ApiModelProperty(value = "Comment about feedback", example = "The feedback listing is bugged", required = true)
  @NotBlank
  private String comment;

}
