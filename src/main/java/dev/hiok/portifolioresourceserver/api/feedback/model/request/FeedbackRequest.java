package dev.hiok.portifolioresourceserver.api.feedback.model.request;

import javax.validation.constraints.NotBlank;

import dev.hiok.portifolioresourceserver.core.validation.ValueOfEnum;
import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedbackType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
  
  @NotBlank
  @ValueOfEnum(enumClass = FeedbackType.class)
  private FeedbackType type;

  @NotBlank
  private String comment;

}
