package dev.hiok.portifolioresourceserver.api.feedback.model.request;

import javax.validation.constraints.NotBlank;

import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedbackType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
  
  @NotBlank
  private FeedbackType type;

  @NotBlank
  private String comment;

}
