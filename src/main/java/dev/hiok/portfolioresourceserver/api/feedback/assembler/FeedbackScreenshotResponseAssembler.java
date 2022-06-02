package dev.hiok.portfolioresourceserver.api.feedback.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.hiok.portfolioresourceserver.api.feedback.model.response.FeedbackScreenshotResponse;
import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackScreenshot;

@Component
public class FeedbackScreenshotResponseAssembler {
  
  @Autowired
  private ModelMapper modelMapper;

  public FeedbackScreenshotResponse toRepresentationModel(FeedbackScreenshot feedbackScreenshot) {
    return modelMapper.map(feedbackScreenshot, FeedbackScreenshotResponse.class);
  }
  
}
