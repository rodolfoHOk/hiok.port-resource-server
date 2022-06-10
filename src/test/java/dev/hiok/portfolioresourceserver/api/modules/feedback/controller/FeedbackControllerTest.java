package dev.hiok.portfolioresourceserver.api.modules.feedback.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import dev.hiok.portfolioresourceserver.api.exceptionHandler.ApiExceptionHandler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.assembler.FeedbackRequestDisassembler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.assembler.FeedbackResponseAssembler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.assembler.FeedbacksResponseAssembler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.FeedbackRequest;
import dev.hiok.portfolioresourceserver.api.modules.feedback.model.request.UpdateFeedbackStatusRequest;
import dev.hiok.portfolioresourceserver.domain.exception.EntityInUseException;
import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
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

  @Mock
  private FeedbacksResponseAssembler feedbacksResponseAssembler;
  
  @InjectMocks
  private FeedbackController feedbackController;
  
  private MockMvc mockMvc;
  
  private static final String BASE_URL = "/api/v1/feedbacks";

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(feedbackController)
      .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
      .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
      .setControllerAdvice(new ApiExceptionHandler())
      .build();
  }

  @Test
  void shouldReturnCreatedWhenPostCreateIsCalled() throws Exception {
    FeedbackRequest feedbackRequest = new FeedbackRequest();
    feedbackRequest.setType(FeedbackType.BUG.toString());
    feedbackRequest.setComment("Test 01 - BUG");
    Feedback savedFeedback = new Feedback();
    
    when(feedbackRegistrationService.create(
      feedbackRequestDisassembler.toEntityModel(feedbackRequest)))
        .thenReturn(savedFeedback);

    mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonConversionUnit.asJsonString(feedbackRequest)))
      .andExpect(status().isCreated());
  }

  @Test
  void shouldReturnBaqRequestWhenPostCreatedIsCalledWithInvalidRequest() throws Exception {
    FeedbackRequest feedbackRequest = new FeedbackRequest();
    feedbackRequest.setType("invalid type");
    feedbackRequest.setComment("Test 02 - Invalid type");

    mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonConversionUnit.asJsonString(feedbackRequest)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnOkWhenSearchIsCalled() throws Exception {
    Feedback foundedFeedback = new Feedback();
    List<Feedback> feedbacks = List.of(foundedFeedback);
    Page<Feedback> paginatedFeedback = new PageImpl<>(feedbacks);
    Pageable pageable = Pageable.ofSize(9);

    when(searchFeedbacksService.search(null, pageable)).thenReturn(paginatedFeedback);

    mockMvc.perform(get(BASE_URL + "?page=0&size=9").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnOkWhenSearchByIdIsCalled() throws Exception {
    UUID id = UUID.randomUUID();
    Feedback foundedFeedback = new Feedback();

    when(feedbackRegistrationService.searchById(id)).thenReturn(foundedFeedback);

    mockMvc.perform(get(BASE_URL + "/" + id).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnNotFoundWhenSearchByIdIsCalled() throws Exception {
    UUID invalidId = UUID.randomUUID();

    when(feedbackRegistrationService.searchById(invalidId))
      .thenThrow(new EntityNotFoundException("test"));

    mockMvc.perform(get(BASE_URL + "/" + invalidId).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnOkWhenUpdateIsCalled() throws Exception {
    UUID id = UUID.randomUUID();
    FeedbackRequest feedbackRequest = new FeedbackRequest();
    feedbackRequest.setType(FeedbackType.BUG.toString());
    feedbackRequest.setComment("Test 06 - BUG");
    Feedback updatedFeedback = new Feedback();

    when(feedbackRegistrationService
      .update(id, feedbackRequestDisassembler.toEntityModel(feedbackRequest)))
      .thenReturn(updatedFeedback);

    mockMvc.perform(put(BASE_URL + "/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonConversionUnit.asJsonString(feedbackRequest)))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnNotFoundWhenUpdateIsCalledWithInvalidId() throws Exception {
    UUID invalidId = UUID.randomUUID();
    FeedbackRequest feedbackRequest = new FeedbackRequest();
    feedbackRequest.setType(FeedbackType.BUG.toString());
    feedbackRequest.setComment("Test 06 - BUG");

    when(feedbackRegistrationService
      .update(invalidId, feedbackRequestDisassembler.toEntityModel(feedbackRequest)))
      .thenThrow(new EntityNotFoundException("test"));

      mockMvc.perform(put(BASE_URL + "/" + invalidId)
      .contentType(MediaType.APPLICATION_JSON)
      .content(JsonConversionUnit.asJsonString(feedbackRequest)))
    .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestWhenUpdateIsCalledWithInvalidRequest() throws Exception {
    UUID id = UUID.randomUUID();
    FeedbackRequest feedbackRequest = new FeedbackRequest();
    feedbackRequest.setType("FeedbackType.BUG.toString()");
    feedbackRequest.setComment("");

    mockMvc.perform(put(BASE_URL + "/" + id)
      .contentType(MediaType.APPLICATION_JSON)
      .content(JsonConversionUnit.asJsonString(feedbackRequest)))
    .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNoContentWhenDeleteIsCalled() throws Exception {
    UUID id = UUID.randomUUID();

    doNothing().when(feedbackRegistrationService).delete(id);

    mockMvc.perform(delete(BASE_URL + "/" + id).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundWhenDeleteIsCalledWithInvalidId() throws Exception {
    UUID invalidId = UUID.randomUUID();

    doThrow(new EntityNotFoundException("test"))
      .when(feedbackRegistrationService).delete(invalidId);

      mockMvc.perform(delete(BASE_URL + "/" + invalidId).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnConflictWhenDeleteIsCalledWithEntityInUse() throws Exception {
    UUID id = UUID.randomUUID();

    doThrow(new EntityInUseException("test"))
      .when(feedbackRegistrationService).delete(id);

      mockMvc.perform(delete(BASE_URL + "/" + id).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isConflict());
  }

  @Test
  void shouldReturnOkWhenUpdateStatusIsCalled() throws Exception {
    UUID id = UUID.randomUUID();
    UpdateFeedbackStatusRequest updateFeedbackStatusRequest = new UpdateFeedbackStatusRequest();
    updateFeedbackStatusRequest.setStatus(FeedbackStatus.RESOLVED.toString());
    Feedback updatedFeedback = new Feedback();

    when(updateFeedbackStatusService
      .updateStatus(id, FeedbackStatus.valueOf(updateFeedbackStatusRequest.getStatus())))
      .thenReturn(updatedFeedback);

    mockMvc.perform(patch(BASE_URL + "/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonConversionUnit.asJsonString(updateFeedbackStatusRequest)))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnNotFoundWhenUpdateStatusIsCalledWithInvalidId() throws Exception {
    UUID invalidId = UUID.randomUUID();
    UpdateFeedbackStatusRequest updateFeedbackStatusRequest = new UpdateFeedbackStatusRequest();
    updateFeedbackStatusRequest.setStatus(FeedbackStatus.RESOLVED.toString());
    
    doThrow(new EntityNotFoundException("test"))
      .when(updateFeedbackStatusService)
      .updateStatus(invalidId, FeedbackStatus.valueOf(updateFeedbackStatusRequest.getStatus()));

    mockMvc.perform(patch(BASE_URL + "/" + invalidId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonConversionUnit.asJsonString(updateFeedbackStatusRequest)))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestWhenUpdateStatusIsCalledWithInvalidRequest() throws Exception {
    UUID id = UUID.randomUUID();
    UpdateFeedbackStatusRequest updateFeedbackStatusRequest = new UpdateFeedbackStatusRequest();
    updateFeedbackStatusRequest.setStatus("Invalid status");
    
    mockMvc.perform(patch(BASE_URL + "/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonConversionUnit.asJsonString(updateFeedbackStatusRequest)))
      .andExpect(status().isBadRequest());
  }

}
