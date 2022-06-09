package dev.hiok.portfolioresourceserver.api.modules.feedback.assembler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbackResponse;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class FeedbackResponseAssemblerTest {

  private static FeedbackResponseAssembler feedbackResponseAssembler;

  @BeforeAll
  static void setup() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    feedbackResponseAssembler = new FeedbackResponseAssembler(modelMapper);
  }
  
  @Test
  void shouldReturnAFeedbackResponseWhenToRepresentationModelIsCalledWithAFeedback() {
    UUID id = UUID.randomUUID();
    Feedback feedback = createFeedback(id);
    FeedbackResponse expectedFeedbackResponse = this.createExpectedFeedbackResponse(id, feedback);

    FeedbackResponse convertedFeedbackResponse = 
      feedbackResponseAssembler.toRepresentationModel(feedback);
    assertThat(convertedFeedbackResponse, is(samePropertyValuesAs(expectedFeedbackResponse)));
  }

  @Test
  void shouldReturnAFeedbackResponseListWhenToCollectionRepresentationModelIsCalledWithAFeedbackList() {
    UUID id = UUID.randomUUID();
    Feedback feedback = createFeedback(id);
    FeedbackResponse expectedFeedbackResponse = this.createExpectedFeedbackResponse(id, feedback);
    List<Feedback> feedbacks = List.of(feedback);
    List<FeedbackResponse> expectedFeedbackResponses = List.of(expectedFeedbackResponse);

    List<FeedbackResponse> convertedFeedbackResponses
      = feedbackResponseAssembler.toCollectionRepresentationModel(feedbacks);
    assertThat(convertedFeedbackResponses.size(), is(equalTo(expectedFeedbackResponses.size())));
    assertThat(convertedFeedbackResponses.get(0), is(samePropertyValuesAs(expectedFeedbackResponses.get(0))));
  }

  private Feedback createFeedback(UUID id) {
    Feedback feedback = new Feedback();
    feedback.setId(id);
    feedback.setType(FeedbackType.BUG);
    feedback.setComment("Test 01 - BUG");
    feedback.setHasScreenshot(false);
    feedback.setStatus(FeedbackStatus.PENDING);
    feedback.setCreatedAt(OffsetDateTime.now());
    feedback.setModifiedAt(OffsetDateTime.now());
    return feedback;
  }

  private FeedbackResponse createExpectedFeedbackResponse(UUID id, Feedback feedback) {
    FeedbackResponse expectedFeedbackResponse = new FeedbackResponse();
    expectedFeedbackResponse.setId(id);
    expectedFeedbackResponse.setType(FeedbackType.BUG);
    expectedFeedbackResponse.setComment("Test 01 - BUG");
    expectedFeedbackResponse.setHasScreenshot(false);
    expectedFeedbackResponse.setStatus(FeedbackStatus.PENDING);
    expectedFeedbackResponse.setCreatedAt(feedback.getCreatedAt());
    expectedFeedbackResponse.setModifiedAt(feedback.getModifiedAt());
    return expectedFeedbackResponse;
  }
}