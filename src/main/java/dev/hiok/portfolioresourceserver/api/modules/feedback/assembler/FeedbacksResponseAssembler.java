package dev.hiok.portfolioresourceserver.api.modules.feedback.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbacksResponse;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;

@Component
public class FeedbacksResponseAssembler {
  
  @Autowired
  private ModelMapper modelMapper;

  public FeedbacksResponse toRepresentationModel(Page<Feedback> paginatedFeedbacks) {
    return modelMapper.map(paginatedFeedbacks, FeedbacksResponse.class);
  }
}
