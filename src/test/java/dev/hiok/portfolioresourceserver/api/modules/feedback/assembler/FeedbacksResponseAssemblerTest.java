package dev.hiok.portfolioresourceserver.api.modules.feedback.assembler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.hiok.portfolioresourceserver.api.modules.feedback.model.response.FeedbacksResponse;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;

public class FeedbacksResponseAssemblerTest {

  private static FeedbacksResponseAssembler feedbacksResponseAssembler;

  @BeforeAll
  static void setup() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    feedbacksResponseAssembler = new FeedbacksResponseAssembler(modelMapper);
  }

  @Test
  void shouldReturnAFeedbacksResponseWhenToRepresentationModelIsCalledWithAPaginatedFeedbacks() {
    Pageable pageable = Pageable.ofSize(9);
    Page<Feedback> paginatedFeedbacks = Page.empty(pageable);
    FeedbacksResponse expectedFeedbacksResponse = new FeedbacksResponse();
    expectedFeedbacksResponse.setNumber(0L);
    expectedFeedbacksResponse.setSize(9L);
    expectedFeedbacksResponse.setTotalElements(0L);
    expectedFeedbacksResponse.setTotalPages(0L);

    FeedbacksResponse convertedFeedbacksResponse = 
      feedbacksResponseAssembler.toRepresentationModel(paginatedFeedbacks);

    assertThat(convertedFeedbacksResponse.getSize(), is(equalTo(expectedFeedbacksResponse.getSize())));
    assertThat(convertedFeedbacksResponse.getNumber(), is(equalTo(expectedFeedbacksResponse.getNumber())));
    assertThat(convertedFeedbacksResponse.getTotalElements(), 
      is(equalTo(expectedFeedbacksResponse.getTotalElements())));
    assertThat(convertedFeedbacksResponse.getTotalPages(), 
      is(equalTo(expectedFeedbacksResponse.getTotalPages())));
  }
  
}
