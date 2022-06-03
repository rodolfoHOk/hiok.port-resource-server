package dev.hiok.portfolioresourceserver.api.modules.feedback.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.FeedbackRequest;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;

@Component
public class FeedbackRequestDisassembler {
  
  @Autowired
  private ModelMapper modelMapper;

  public Feedback toEntityModel(FeedbackRequest feedbackRequest) {
    return modelMapper.map(feedbackRequest, Feedback.class);
  }

  public void copyToEntityModel(FeedbackRequest feedbackRequest, Feedback feedback) {
    modelMapper.map(feedbackRequest, feedback);
  }

}
