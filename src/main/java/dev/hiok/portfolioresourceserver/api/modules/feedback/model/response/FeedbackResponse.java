package dev.hiok.portfolioresourceserver.api.modules.feedback.model.response;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackResponse {
  
  @ApiModelProperty(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
  private UUID id;

  @ApiModelProperty(example = "BUG")
  private FeedbackType type;

  @ApiModelProperty(example = "The feedback listing is bugged")
  private String comment;

  @ApiModelProperty(example = "true")
  private Boolean hasScreenshot;

  @ApiModelProperty(example = "RESOLVED")
  private FeedbackStatus status;

  @ApiModelProperty(example = "2022-06-03T13:30:52.502356626-03:00")
  private OffsetDateTime createdAt;

  @ApiModelProperty(example = "2022-06-03T13:30:52.502356626-03:00")
  private OffsetDateTime modifiedAt;
  
}
