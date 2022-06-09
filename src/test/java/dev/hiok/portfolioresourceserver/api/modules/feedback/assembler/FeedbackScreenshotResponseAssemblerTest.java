package dev.hiok.portfolioresourceserver.api.modules.feedback.assembler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;

import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbackScreenshotResponse;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackScreenshot;

public class FeedbackScreenshotResponseAssemblerTest {
  
  private static FeedbackScreenshotResponseAssembler feedbackScreenshotResponseAssembler;

  @BeforeAll
  static void setup() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    feedbackScreenshotResponseAssembler = new FeedbackScreenshotResponseAssembler(modelMapper);
  }

  @Test
  void shouldReturnAFeedbackScreenshotResponseWhenToRepresentationModelIsCalledWithAFeedbackScreenshot() {
    UUID id = UUID.randomUUID();
    FeedbackScreenshot feedbackScreenshot = new FeedbackScreenshot();
    feedbackScreenshot.setId(id);
    feedbackScreenshot.setFilename("test.png");
    feedbackScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    feedbackScreenshot.setSize(102400L);
    FeedbackScreenshotResponse expectedFeedbackScreenshotResponse = new FeedbackScreenshotResponse();
    expectedFeedbackScreenshotResponse.setId(id);
    expectedFeedbackScreenshotResponse.setFilename("test.png");
    expectedFeedbackScreenshotResponse.setContentType(MediaType.IMAGE_PNG_VALUE);
    expectedFeedbackScreenshotResponse.setSize(102400L);

    FeedbackScreenshotResponse convertedFeedbackScreenshotResponse = 
      feedbackScreenshotResponseAssembler.toRepresentationModel(feedbackScreenshot);
    assertThat(convertedFeedbackScreenshotResponse, 
      is(samePropertyValuesAs(expectedFeedbackScreenshotResponse)));
  }
}
