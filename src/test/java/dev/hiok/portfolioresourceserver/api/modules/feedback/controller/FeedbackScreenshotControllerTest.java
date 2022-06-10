package dev.hiok.portfolioresourceserver.api.modules.feedback.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import dev.hiok.portfolioresourceserver.api.exceptionHandler.ApiExceptionHandler;
import dev.hiok.portfolioresourceserver.api.modules.feedback.assembler.FeedbackScreenshotResponseAssembler;
import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portfolioresourceserver.domain.exception.ValidationException;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackScreenshot;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.service.FeedbackScreenshotCatalogService;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;

@ExtendWith(MockitoExtension.class)
public class FeedbackScreenshotControllerTest {
  
  @Mock
  private FeedbackScreenshotCatalogService feedbackScreenshotCatalogService;
  
  @Mock
  private FeedbackScreenshotResponseAssembler feedbackScreenshotResponseAssembler;

  @Mock
  private StorageService storageService;

  @InjectMocks
  private FeedbackScreenshotController feedbackScreenshotController;

  private MockMvc mockMvc;
  
  private static final String BASE_URL = "/api/v1/feedbacks/";

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(feedbackScreenshotController)
      .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
      .setControllerAdvice(new ApiExceptionHandler())
      .build();
  }

  @Test
  void shouldReturnOkWhenSearchIsCalledWithAValidId() throws Exception {
    UUID feedbackId = UUID.randomUUID();
    UUID screenshotNameUUID = UUID.randomUUID();
    FeedbackScreenshot feedbackScreenshot = new FeedbackScreenshot();
    feedbackScreenshot.setId(feedbackId);
    feedbackScreenshot.setFilename(screenshotNameUUID.toString() + "_screenshot.png");
    feedbackScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    feedbackScreenshot.setSize(102400L);

    when(feedbackScreenshotCatalogService.searchById(feedbackId)).thenReturn(feedbackScreenshot);

    mockMvc.perform(get(BASE_URL + feedbackId + "/screenshot")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Accept", "application/json"))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnBadRequestWhenSearchIsCalledWithoutAAcceptHeader() throws Exception {
    UUID feedbackId = UUID.randomUUID();
    
    mockMvc.perform(get(BASE_URL + feedbackId + "/screenshot")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNotFoundWhenSearchIsCalledWithAInvalidId() throws Exception {
    UUID feedbackInvalidId = UUID.randomUUID();
    
    when(feedbackScreenshotCatalogService.searchById(feedbackInvalidId))
      .thenThrow(new EntityNotFoundException("test"));

    mockMvc.perform(get(BASE_URL + feedbackInvalidId + "/screenshot")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Accept", "application/json"))
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnFoundWhenSearchIsCalledWithAValidMediaType() throws Exception {
    UUID feedbackId = UUID.randomUUID();
    UUID screenshotNameUUID = UUID.randomUUID();
    FeedbackScreenshot feedbackScreenshot = new FeedbackScreenshot();
    feedbackScreenshot.setId(feedbackId);
    feedbackScreenshot.setFilename(screenshotNameUUID.toString() + "_screenshot.png");
    feedbackScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    feedbackScreenshot.setSize(102400L);
    StorageService.RecoveredFile recoveredFile = StorageService.RecoveredFile.builder()
      .url("https://test.com/portfolio/images/" + feedbackScreenshot.getFilename())  
      .build();

    when(feedbackScreenshotCatalogService.searchById(feedbackId)).thenReturn(feedbackScreenshot);
    when(storageService.recover(feedbackScreenshot.getFilename())).thenReturn(recoveredFile);

    mockMvc.perform(get(BASE_URL + feedbackId + "/screenshot")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Accept", "image/png"))
      .andExpect(status().isFound());
  }

  @Test
  void shouldReturnOkWhenSearchIsCalledWithAValidMediaTypeAndHasInputStream() throws Exception {
    UUID feedbackId = UUID.randomUUID();
    UUID screenshotNameUUID = UUID.randomUUID();
    FeedbackScreenshot feedbackScreenshot = new FeedbackScreenshot();
    feedbackScreenshot.setId(feedbackId);
    feedbackScreenshot.setFilename(screenshotNameUUID.toString() + "_screenshot.png");
    feedbackScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    feedbackScreenshot.setSize(102400L);
    StorageService.RecoveredFile recoveredFile = StorageService.RecoveredFile.builder()
      .inputStream(new ByteArrayInputStream("test data".getBytes()))
      .build();

    when(feedbackScreenshotCatalogService.searchById(feedbackId)).thenReturn(feedbackScreenshot);
    when(storageService.recover(feedbackScreenshot.getFilename())).thenReturn(recoveredFile);

    mockMvc.perform(get(BASE_URL + feedbackId + "/screenshot")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Accept", "image/png"))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnOkWhenReplaceIsCalled() throws Exception {
    UUID feedbackId = UUID.randomUUID();
    MockMultipartFile file = new MockMultipartFile(
      "file",
      "screenshot.png",
      MediaType.IMAGE_PNG_VALUE,
      "test data".getBytes());
    FeedbackScreenshot feedbackScreenshot = new FeedbackScreenshot();
    feedbackScreenshot.setId(feedbackId);
    feedbackScreenshot.setContentType(file.getContentType());
    feedbackScreenshot.setFilename(file.getOriginalFilename());
    feedbackScreenshot.setSize(file.getSize());
    FeedbackScreenshot savedScreenshot = new FeedbackScreenshot();
    savedScreenshot.setId(feedbackScreenshot.getId());
    savedScreenshot.setContentType(feedbackScreenshot.getContentType());
    savedScreenshot.setSize(feedbackScreenshot.getSize());
    savedScreenshot.setFilename(UUID.randomUUID().toString() + "_" + feedbackScreenshot.getFilename());
    
    when(feedbackScreenshotCatalogService.save(eq(feedbackScreenshot), any()))
      .thenReturn(savedScreenshot);

    MockMultipartHttpServletRequestBuilder multipart = 
      (MockMultipartHttpServletRequestBuilder) multipart(BASE_URL + feedbackId + "/screenshot")
        .with(request -> {
      request.setMethod(HttpMethod.PUT.toString());
      return request;
    });
    mockMvc.perform(multipart.file(file))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnBadRequestWhenReplaceIsCalledWithAInvalidFeedbackId() throws Exception {
    UUID invalidFeedbackId = UUID.randomUUID();
    MockMultipartFile file = new MockMultipartFile(
      "file",
      "screenshot.png",
      MediaType.IMAGE_PNG_VALUE,
      "test data".getBytes());
    FeedbackScreenshot feedbackScreenshot = new FeedbackScreenshot();
    feedbackScreenshot.setId(invalidFeedbackId);
    feedbackScreenshot.setContentType(file.getContentType());
    feedbackScreenshot.setFilename(file.getOriginalFilename());
    feedbackScreenshot.setSize(file.getSize());

    when(feedbackScreenshotCatalogService.save(eq(feedbackScreenshot), any()))
      .thenThrow(new ValidationException("test"));

    MockMultipartHttpServletRequestBuilder multipart = 
      (MockMultipartHttpServletRequestBuilder) multipart(BASE_URL + invalidFeedbackId + "/screenshot")
        .with(request -> {
      request.setMethod(HttpMethod.PUT.toString());
      return request;
    });
    mockMvc.perform(multipart.file(file))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenReplaceIsCalledWithAInvalidRequest() throws Exception {
    UUID feedbackId = UUID.randomUUID();
    MockMultipartFile file = new MockMultipartFile(
      "invalid property name",
      "screenshot.png",
      MediaType.IMAGE_PNG_VALUE,
      "test data".getBytes());

    MockMultipartHttpServletRequestBuilder multipart = 
      (MockMultipartHttpServletRequestBuilder) multipart(BASE_URL + feedbackId + "/screenshot")
        .with(request -> {
      request.setMethod(HttpMethod.PUT.toString());
      return request;
    });
    mockMvc.perform(multipart.file(file))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNoContentWhenRemoveIsCalled() throws Exception {
    UUID feedbackId = UUID.randomUUID();

    doNothing().when(feedbackScreenshotCatalogService).remove(feedbackId);

    mockMvc.perform(delete(BASE_URL + feedbackId + "/screenshot")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnBadRequestWhenRemoveIsCalledWithInvalidFeedbackId() throws Exception {
    UUID invalidFeedbackId = UUID.randomUUID();

    doThrow(new ValidationException("test"))
      .when(feedbackScreenshotCatalogService).remove(invalidFeedbackId);

    mockMvc.perform(delete(BASE_URL + invalidFeedbackId + "/screenshot")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNotFoundWhenRemoveIsCalledWithAValidFeedbackIdButNotHasScreenshot() throws Exception {
    UUID invalidFeedbackId = UUID.randomUUID();

    doThrow(new EntityNotFoundException("test"))
      .when(feedbackScreenshotCatalogService).remove(invalidFeedbackId);

    mockMvc.perform(delete(BASE_URL + invalidFeedbackId + "/screenshot")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(status().isNotFound());
  }
}
