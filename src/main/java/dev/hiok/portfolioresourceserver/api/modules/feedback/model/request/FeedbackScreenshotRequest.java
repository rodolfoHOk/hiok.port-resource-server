package dev.hiok.portfolioresourceserver.api.modules.feedback.model.request;

import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import dev.hiok.portfolioresourceserver.core.validation.MultipartFileContentType;
import dev.hiok.portfolioresourceserver.core.validation.MultipartFileSize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackScreenshotRequest {
  
  @ApiModelProperty(hidden = true)
  @NotNull
  @MultipartFileSize(max = "1048576")
  @MultipartFileContentType(allowed = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
  private MultipartFile file;

}
