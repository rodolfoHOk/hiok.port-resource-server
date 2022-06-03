package dev.hiok.portfolioresourceserver.api.modules.feedback.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.hiok.portfolioresourceserver.api.modules.feedback.assembler.FeedbackRequestDisassembler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.assembler.FeedbackResponseAssembler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.FeedbackRequest;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.UpdateFeedbackStatusRequest;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbackResponse;
import dev.hiok.portfolioresourceserver.api.modules.feedback.openapi.controller.FeedbackControllerOpenApi;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.service.FeedbackRegistrationService;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.service.SearchFeedbacksService;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.service.UpdateFeedbackStatusService;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController implements FeedbackControllerOpenApi {

  @Autowired
  private FeedbackRegistrationService feedbackRegistrationService;

  @Autowired
  private SearchFeedbacksService searchFeedbacksService;

  @Autowired
  private UpdateFeedbackStatusService updateFeedbackStatusService;

  @Autowired
  private FeedbackResponseAssembler feedbackResponseAssembler;

  @Autowired
  private FeedbackRequestDisassembler feedbackRequestDisassembler;

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FeedbackResponse create(@Valid @RequestBody FeedbackRequest feedbackRequest) {
    Feedback createdFeedback = 
      feedbackRegistrationService.create(feedbackRequestDisassembler.toEntityModel(feedbackRequest));
    
    return feedbackResponseAssembler.toRepresentationModel(createdFeedback);
  }

  @Override
  @GetMapping
  public Page<FeedbackResponse> search(
    @RequestParam(name = "status", required = false) FeedbackStatus status,
    @PageableDefault(size = 9) Pageable pageable) {
    Page<Feedback> foundFeedbacks = searchFeedbacksService.search(status, pageable);

    List<FeedbackResponse> feedbacksResponse = 
      feedbackResponseAssembler.toCollectionRepresentationModel(foundFeedbacks.getContent());

    Page<FeedbackResponse> paginatedFeedbacksResponse = 
      new PageImpl<>(feedbacksResponse, pageable, foundFeedbacks.getTotalElements());

    return paginatedFeedbacksResponse;
  }

  @Override
  @GetMapping("/{id}")
  public FeedbackResponse searchById(@PathVariable UUID id) {
    Feedback foundFeedback = feedbackRegistrationService.searchById(id);

    return feedbackResponseAssembler.toRepresentationModel(foundFeedback);
  }

  @Override
  @PutMapping("/{id}")
  public FeedbackResponse update(
    @PathVariable UUID id, 
    @Valid @RequestBody FeedbackRequest feedbackRequest) {
    Feedback feedbackEntity = feedbackRequestDisassembler.toEntityModel(feedbackRequest);

    Feedback updatedFeedback = feedbackRegistrationService.update(id, feedbackEntity);

    return feedbackResponseAssembler.toRepresentationModel(updatedFeedback);
  }

  @Override
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    feedbackRegistrationService.delete(id);
  }

  @Override
  @PatchMapping("/{id}")
  public FeedbackResponse updateStatus(
    @PathVariable UUID id, 
    @Valid @RequestBody UpdateFeedbackStatusRequest updateFeedbackStatusRequest) {
    Feedback updatedFeedback = 
      updateFeedbackStatusService.updateStatus(id, updateFeedbackStatusRequest.getStatus());

    return feedbackResponseAssembler.toRepresentationModel(updatedFeedback);
  }

}
