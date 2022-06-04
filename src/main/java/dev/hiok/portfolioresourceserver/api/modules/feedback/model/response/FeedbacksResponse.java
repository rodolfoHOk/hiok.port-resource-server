package dev.hiok.portfolioresourceserver.api.modules.feedback.model.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbacksResponse {
  
  private List<FeedbackResponse> content;

  @ApiModelProperty(example = "20", value = "Total Elements")
  private Long totalElements;

  @ApiModelProperty(example = "3", value = "Total Pages")
  private Long totalPages;

  @ApiModelProperty(example = "9", value = "Elements per Page")
  private Long size;

  @ApiModelProperty(example = "0", value = "Page number (starts with 0)")
  private Long number;

}
