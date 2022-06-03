package dev.hiok.portfolioresourceserver.api.feedback.model.request;

import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import dev.hiok.portfolioresourceserver.core.validation.MultipartFileContentType;
import dev.hiok.portfolioresourceserver.core.validation.MultipartFileSize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackScreenshotRequest {
  
  @NotNull
  @MultipartFileSize(max = "1048576")
  @MultipartFileContentType(allowed = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
  private MultipartFile screenshot;

}