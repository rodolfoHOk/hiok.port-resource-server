package dev.hiok.portfolioresourceserver.api.modules.feedback.model.response;

import java.util.UUID;

import org.springframework.http.MediaType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackScreenshotResponse {
  
  @ApiModelProperty(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
  private UUID id;

  @ApiModelProperty(example = "bug-screenshot.png")
  private String filename;

  @ApiModelProperty(example = MediaType.IMAGE_PNG_VALUE)
  private String contentType;

  @ApiModelProperty(example = "512000")
  private Long size;
  
}
