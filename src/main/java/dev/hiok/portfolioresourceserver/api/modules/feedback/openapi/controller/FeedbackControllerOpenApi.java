package dev.hiok.portfolioresourceserver.api.modules.feedback.openapi.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;

import dev.hiok.portfolioresourceserver.api.exceptionHandler.ProblemDetails;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.FeedbackRequest;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.UpdateFeedbackStatusRequest;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbackResponse;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Feedbacks")
public interface FeedbackControllerOpenApi {

  @ApiOperation("Create")
  FeedbackResponse create(FeedbackRequest feedbackRequest);

  @ApiOperation("Search")
  Page<FeedbackResponse> search(
  @ApiParam(name = "status", value="Feedback status", example = "RESOLVED", required = false)  
  FeedbackStatus status, Pageable pageable);

  @ApiOperation("Search by ID")
  @ApiResponses({
    @ApiResponse(responseCode = "400", description = "Invalid ID",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE)),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "Forbidden"),
    @ApiResponse(responseCode = "404", description = "Not Found",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  FeedbackResponse searchById(UUID id);

  @ApiOperation("Update")
  @ApiResponses({
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "Forbidden"),
    @ApiResponse(responseCode = "404", description = "Not Found",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  FeedbackResponse update(UUID id, FeedbackRequest feedbackRequest);

  @ApiOperation("Delete")
  @ApiResponses({
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "Forbidden"),
    @ApiResponse(responseCode = "404", description = "Not Found",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  void delete(UUID id);

  @ApiOperation("Update status")
  @ApiResponses({
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
    @ApiResponse(responseCode = "403", description = "Forbidden"),
    @ApiResponse(responseCode = "404", description = "Not Found",
      content = @Content(schema = @Schema(implementation = ProblemDetails.class),
      mediaType = MediaType.APPLICATION_JSON_VALUE))
  })
  FeedbackResponse updateStatus(UUID id, UpdateFeedbackStatusRequest updateFeedbackStatusRequest);

}
