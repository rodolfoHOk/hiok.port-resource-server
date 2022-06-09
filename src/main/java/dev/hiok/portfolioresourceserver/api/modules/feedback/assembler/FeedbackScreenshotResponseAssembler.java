package dev.hiok.portfolioresourceserver.api.modules.feedback.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbackScreenshotResponse;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackScreenshot;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeedbackScreenshotResponseAssembler {
  
  private final ModelMapper modelMapper;

  public FeedbackScreenshotResponse toRepresentationModel(FeedbackScreenshot feedbackScreenshot) {
    return modelMapper.map(feedbackScreenshot, FeedbackScreenshotResponse.class);
  }
  
}
