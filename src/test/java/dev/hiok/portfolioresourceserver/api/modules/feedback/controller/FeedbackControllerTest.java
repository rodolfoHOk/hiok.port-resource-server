package dev.hiok.portfolioresourceserver.api.modules.feedback.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import dev.hiok.portfolioresourceserver.api.exceptionHandler.ApiExceptionHandler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.assembler.FeedbackRequestDisassembler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.assembler.FeedbackResponseAssembler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.FeedbackRequest;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackType;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.service.FeedbackRegistrationService;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.service.SearchFeedbacksService;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.service.UpdateFeedbackStatusService;
import dev.hiok.portfolioresourceserver.utils.JsonConversionUnit;

@ExtendWith(MockitoExtension.class)
public class FeedbackControllerTest {

  @Mock
  private FeedbackRegistrationService feedbackRegistrationService; 

  @Mock
  private SearchFeedbacksService searchFeedbacksService;

  @Mock
  private UpdateFeedbackStatusService updateFeedbackStatusService;
  
  @Mock
  private FeedbackRequestDisassembler feedbackRequestDisassembler;
  
  @Mock
  private FeedbackResponseAssembler feedbackResponseAssembler;
  
  @InjectMocks
  private FeedbackController feedbackController;
  
  private MockMvc mockMvc;
  
  private static final String BASE_URL = "/api/v1/feedbacks";

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(feedbackController)
      .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
      .setControllerAdvice(new ApiExceptionHandler())
      .build();
  }

  @Test
  void shouldReturnCreatedAndCreatedWhenPostCreateIsCalled() throws Exception {
    FeedbackRequest feedbackRequest = new FeedbackRequest();
    feedbackRequest.setType(FeedbackType.BUG.toString());
    feedbackRequest.setComment("Test 01 - BUG");
    Feedback savedFeedback = new Feedback();
    savedFeedback.setId(UUID.randomUUID());
    savedFeedback.setType(FeedbackType.BUG);
    savedFeedback.setComment("Test 01 - BUG");
    savedFeedback.setHasScreenshot(false);
    savedFeedback.setStatus(FeedbackStatus.PENDING);
    
    when(feedbackRegistrationService.create(
      feedbackRequestDisassembler.toEntityModel(feedbackRequest)))
        .thenReturn(savedFeedback);

    mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonConversionUnit.asJsonString(feedbackRequest)))
      .andExpect(status().isCreated());
  }
}
