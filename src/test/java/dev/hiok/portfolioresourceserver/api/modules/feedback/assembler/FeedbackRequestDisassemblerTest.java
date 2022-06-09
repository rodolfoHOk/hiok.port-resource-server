package dev.hiok.portfolioresourceserver.api.modules.feedback.assembler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.FeedbackRequest;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackType;

public class FeedbackRequestDisassemblerTest {

  private static FeedbackRequestDisassembler feedbackRequestDisassembler;

  @BeforeAll
  static void setup() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    feedbackRequestDisassembler = new FeedbackRequestDisassembler(modelMapper);
  }

  @Test
  void shouldReturnAFeedbackWhenToEntityModelIsCalledWithFeedbackRequest() {
    FeedbackRequest feedbackRequest = new FeedbackRequest();
    feedbackRequest.setType(FeedbackType.BUG.toString());
    feedbackRequest.setComment("Test 01 - BUG");
    Feedback expectedFeedback = new Feedback();
    expectedFeedback.setType(FeedbackType.BUG);
    expectedFeedback.setComment("Test 01 - BUG");

    Feedback convertFeedback = feedbackRequestDisassembler.toEntityModel(feedbackRequest);
    
    assertThat(convertFeedback.getType(), is(equalTo(expectedFeedback.getType())));
    assertThat(convertFeedback.getComment(), is(equalTo(expectedFeedback.getComment())));    
  }

  @Test
  void shouldCopyPropertiesFromFeedbackRequestToFeedbackWhenCopyToEntityModelIsCalled() {
    Feedback originalFeedback = new Feedback();
    originalFeedback.setType(FeedbackType.BUG);
    originalFeedback.setComment("Test 02 - BUG");
    FeedbackRequest feedbackRequest = new FeedbackRequest();
    feedbackRequest.setType(FeedbackType.IDEA.toString());
    feedbackRequest.setComment("Test 02 - IDEA");
    Feedback expectedFeedback = new Feedback();
    expectedFeedback.setType(FeedbackType.IDEA);
    expectedFeedback.setComment("Test 02 - IDEA");

    feedbackRequestDisassembler.copyToEntityModel(feedbackRequest, originalFeedback);

    assertThat(originalFeedback.getType(), is(equalTo(expectedFeedback.getType())));
    assertThat(originalFeedback.getComment(), is(equalTo(expectedFeedback.getComment())));    
  }
  
}
