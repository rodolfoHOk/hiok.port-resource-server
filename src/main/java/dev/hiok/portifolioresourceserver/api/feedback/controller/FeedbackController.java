package dev.hiok.portifolioresourceserver.api.feedback.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.hiok.portifolioresourceserver.api.feedback.assembler.FeedbackRequestDisassembler;
import dev.hiok.portifolioresourceserver.api.feedback.assembler.FeedbackResponseAssembler;
import dev.hiok.portifolioresourceserver.api.feedback.model.request.FeedbackRequest;
import dev.hiok.portifolioresourceserver.api.feedback.model.response.FeedbackResponse;
import dev.hiok.portifolioresourceserver.domain.feedback.model.Feedback;
import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedbackStatus;
import dev.hiok.portifolioresourceserver.domain.feedback.service.FeedbackRegistrationService;

@RestController
@RequestMapping("/v1/feedbacks")
public class FeedbackController {

  @Autowired
  private FeedbackRegistrationService feedbackRegistrationService;

  @Autowired
  private FeedbackResponseAssembler feedbackResponseAssembler;

  @Autowired
  private FeedbackRequestDisassembler feedbackRequestDisassembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FeedbackResponse create(@Valid @RequestBody FeedbackRequest feedbackRequest) {
    Feedback createdFeedback = 
      feedbackRegistrationService.create(feedbackRequestDisassembler.toEntityModel(feedbackRequest));
    
    return feedbackResponseAssembler.toRepresentationModel(createdFeedback);
  }

  @GetMapping
  public Page<FeedbackResponse> search(
    @RequestParam("status") FeedbackStatus status,
    @PageableDefault(size = 9) Pageable pageable) {
    Page<Feedback> foundFeedbacks = feedbackRegistrationService.search(status, pageable);

    List<FeedbackResponse> feedbacksResponse = 
      feedbackResponseAssembler.toCollectionRepresentationModel(foundFeedbacks.getContent());

    Page<FeedbackResponse> paginatedFeedbacksResponse = 
      new PageImpl<>(feedbacksResponse, pageable, foundFeedbacks.getTotalElements());

    return paginatedFeedbacksResponse;
  }

  @GetMapping("/{id}")
  public FeedbackResponse searchById(@PathVariable UUID id) {
    Feedback foundFeedback = feedbackRegistrationService.searchById(id);

    return feedbackResponseAssembler.toRepresentationModel(foundFeedback);
  }

  @PutMapping("/{id}")
  public FeedbackResponse update(@PathVariable UUID id, @Valid @RequestBody FeedbackRequest feedbackRequest) {
    Feedback feedbackEntity = feedbackRequestDisassembler.toEntityModel(feedbackRequest);

    Feedback updatedFeedback = feedbackRegistrationService.update(id, feedbackEntity);

    return feedbackResponseAssembler.toRepresentationModel(updatedFeedback);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    feedbackRegistrationService.delete(id);
  }

}
