package dev.hiok.portfolioresourceserver.api.modules.feedback.openapi.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.multipart.MultipartFile;

import dev.hiok.portfolioresourceserver.api.exceptionHandler.ProblemDetails;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.FeedbackScreenshotRequest;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbackScreenshotResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Feedbacks")
public interface FeedbackScreenshotControllerOpenApi {
  
  @ApiOperation("Search screenshot")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(schema = @Schema(implementation = FeedbackScreenshotResponse.class), 
      mediaType = MediaType.APPLICATION_JSON_VALUE)),
    @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)),
    @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
    @ApiResponse(responseCode = "400", description = "Invalid ID",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE)),
    @ApiResponse(responseCode = "404", description = "Not Found",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  ResponseEntity<?> search(UUID id, 
    @ApiParam(name = "Accept", value = "Accepted media type in header of requisition",
      example = MediaType.IMAGE_JPEG_VALUE, required = true)
    String acceptHeader) throws HttpMediaTypeNotAcceptableException;

  @ApiOperation("Add or replace screenshot")
  @ApiResponses({
    @ApiResponse(responseCode = "404", description = "Not Found",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  FeedbackScreenshotResponse replace(UUID id, FeedbackScreenshotRequest screenshotRequest, 
    MultipartFile file) throws IOException;

  @ApiOperation("Remove screenshot")
  @ApiResponses({
    @ApiResponse(responseCode = "400", description = "Invalid ID",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE)),
    @ApiResponse(responseCode = "404", description = "Not Found",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE)),
    @ApiResponse(responseCode = "403", description = "Forbidden"),
  })
  void remove(UUID id);

}
