package dev.hiok.portifolioresourceserver.api.feedback.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.hiok.portifolioresourceserver.api.feedback.model.response.FeedbackResponse;
import dev.hiok.portifolioresourceserver.domain.feedback.model.Feedback;

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
