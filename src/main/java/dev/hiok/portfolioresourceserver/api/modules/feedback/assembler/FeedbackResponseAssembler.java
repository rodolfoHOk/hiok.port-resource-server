package dev.hiok.portfolioresourceserver.api.modules.feedback.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbackResponse;
import dev.hiok.portfolioresourceserver.domain.feedback.model.Feedback;

@Component
public class FeedbackResponseAssembler {

  @Autowired
  private ModelMapper modelMapper;

  public FeedbackResponse toRepresentationModel(Feedback feedBack) {
    return modelMapper.map(feedBack, FeedbackResponse.class);
  }

  public List<FeedbackResponse> toCollectionRepresentationModel(List<Feedback> feedbacks) {
    return feedbacks.stream()
      .map(feedback -> toRepresentationModel(feedback))
      .collect(Collectors.toList());
  }

}
