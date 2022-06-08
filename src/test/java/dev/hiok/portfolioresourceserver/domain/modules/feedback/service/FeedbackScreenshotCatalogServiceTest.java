package dev.hiok.portfolioresourceserver.domain.modules.feedback.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;

import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portfolioresourceserver.domain.exception.ValidationException;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackScreenshot;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackType;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.repository.FeedbackRepository;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.repository.FeedbackScreenshotRepository;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;
import dev.hiok.portfolioresourceserver.domain.service.StorageService.NewFile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FeedbackScreenshotCatalogServiceTest {

  @Mock
  private FeedbackRepository feedbackRepository;

  @Mock
  private FeedbackScreenshotRepository feedbackScreenshotRepository;

  @Mock
  private StorageService storageService;

  @Mock
  private Clock clock;

  @InjectMocks
  private FeedbackScreenshotCatalogService feedbackScreenshotCatalogService;

  private void initClock() {
    Clock fixedClock = Clock.fixed(Instant.parse("2022-01-01T00:00:00Z"), ZoneId.of("UTC"));
    doReturn(fixedClock.instant()).when(clock).instant();
    doReturn(fixedClock.getZone()).when(clock).getZone();
  }

  @Test
  void shouldSaveAFeedbackScreenshotWhenSaveCalledWithAValidFeedback() {
    initClock();
    UUID id = UUID.randomUUID();
    Feedback expectedExistingFeedback = new Feedback();
    expectedExistingFeedback.setId(id);
    expectedExistingFeedback.setType(FeedbackType.BUG);
    expectedExistingFeedback.setComment("Teste feedback - Bug - Update");
    expectedExistingFeedback.setStatus(FeedbackStatus.RESOLVED);
    expectedExistingFeedback.setHasScreenshot(false);
    expectedExistingFeedback.setCreatedAt(OffsetDateTime.now(clock));
    expectedExistingFeedback.setModifiedAt(null);
    FeedbackScreenshot screenshot = new FeedbackScreenshot();
    screenshot.setId(id);
    screenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    screenshot.setFilename("screenshot.png");
    screenshot.setSize(102400L);
    UUID fileUuid = UUID.randomUUID();
    FeedbackScreenshot expectedSavedScreenshot = new FeedbackScreenshot();
    expectedSavedScreenshot.setId(id);
    expectedSavedScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    expectedSavedScreenshot.setFilename(fileUuid.toString() + "_" + screenshot.getFilename());
    expectedSavedScreenshot.setSize(102400L);
    InputStream fileData = new ByteArrayInputStream("test data".getBytes());

    NewFile expectedNewFile = NewFile.builder()
      .filename(expectedSavedScreenshot.getFilename())
      .contentType(expectedSavedScreenshot.getContentType())
      .inputStream(fileData)
      .build();

    when(feedbackRepository.findById(id)).thenReturn(Optional.of(expectedExistingFeedback));
    when(feedbackScreenshotRepository.findById(id)).thenReturn(Optional.empty());
    when(storageService.generateFileName(screenshot.getFilename()))
      .thenReturn(fileUuid.toString() + "_" + screenshot.getFilename());
    when(feedbackScreenshotRepository.save(expectedSavedScreenshot)).thenReturn(expectedSavedScreenshot);
    doNothing().when(storageService).replace(null, expectedNewFile);

    FeedbackScreenshot savedFeedbackScreenshot = 
      feedbackScreenshotCatalogService.save(screenshot, fileData);
    assertThat(savedFeedbackScreenshot, is(equalTo(expectedSavedScreenshot)));
    assertThat(expectedExistingFeedback.getHasScreenshot(), is(true));
  }

  @Test
  void shouldSaveAFeedbackScreenshotWhenSaveCalledWithAValidFeedbackAndExistingScreenshot() {
    initClock();
    UUID id = UUID.randomUUID();
    Feedback expectedExistingFeedback = new Feedback();
    expectedExistingFeedback.setId(id);
    expectedExistingFeedback.setType(FeedbackType.BUG);
    expectedExistingFeedback.setComment("Teste feedback - Bug - Update");
    expectedExistingFeedback.setStatus(FeedbackStatus.RESOLVED);
    expectedExistingFeedback.setHasScreenshot(true);
    expectedExistingFeedback.setCreatedAt(OffsetDateTime.now(clock));
    expectedExistingFeedback.setModifiedAt(null);
    FeedbackScreenshot screenshot = new FeedbackScreenshot();
    screenshot.setId(id);
    screenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    screenshot.setFilename("screenshot.png");
    screenshot.setSize(102400L);
    UUID existingUuid = UUID.randomUUID();
    FeedbackScreenshot existingFeedbackScreenshot = new FeedbackScreenshot();
    existingFeedbackScreenshot.setId(id);
    existingFeedbackScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    existingFeedbackScreenshot.setFilename(existingUuid.toString() + "_" + screenshot.getFilename());
    existingFeedbackScreenshot.setSize(102400L);
    UUID fileUuid = UUID.randomUUID();
    FeedbackScreenshot expectedSavedScreenshot = new FeedbackScreenshot();
    expectedSavedScreenshot.setId(id);
    expectedSavedScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    expectedSavedScreenshot.setFilename(fileUuid.toString() + "_" + screenshot.getFilename());
    expectedSavedScreenshot.setSize(102400L);
    InputStream fileData = new ByteArrayInputStream("test data".getBytes());

    NewFile expectedNewFile = NewFile.builder()
      .filename(expectedSavedScreenshot.getFilename())
      .contentType(expectedSavedScreenshot.getContentType())
      .inputStream(fileData)
      .build();

    when(feedbackRepository.findById(id)).thenReturn(Optional.of(expectedExistingFeedback));
    when(feedbackScreenshotRepository.findById(id)).thenReturn(Optional.of(existingFeedbackScreenshot));
    when(storageService.generateFileName(screenshot.getFilename()))
      .thenReturn(fileUuid.toString() + "_" + screenshot.getFilename());
    when(feedbackScreenshotRepository.save(expectedSavedScreenshot)).thenReturn(expectedSavedScreenshot);
    doNothing().when(storageService).replace(existingFeedbackScreenshot.getFilename(), expectedNewFile);

    FeedbackScreenshot savedFeedbackScreenshot = 
      feedbackScreenshotCatalogService.save(screenshot, fileData);
    assertThat(savedFeedbackScreenshot, is(equalTo(expectedSavedScreenshot)));
    assertThat(expectedExistingFeedback.getHasScreenshot(), is(true));
  }

  @Test
  void shouldThrowValidationExceptionWhenSaveCalledWithInvalidFeedback() {
    UUID invalidId = UUID.randomUUID();
    FeedbackScreenshot feedbackScreenshot = new FeedbackScreenshot();
    feedbackScreenshot.setId(invalidId);
    InputStream fileData = new ByteArrayInputStream("test data".getBytes());

    when(feedbackRepository.findById(invalidId)).thenReturn(Optional.empty());

    assertThrows(ValidationException.class, 
      () -> feedbackScreenshotCatalogService.save(feedbackScreenshot, fileData));
  }

  @Test
  void shouldDeleteAFeedbackScreenshotWhenDeleteCalledWithAValidFeedbackAndValidScreenshot() {
    initClock();
    UUID id = UUID.randomUUID();
    Feedback existingFeedback = new Feedback();
    existingFeedback.setId(id);
    existingFeedback.setType(FeedbackType.BUG);
    existingFeedback.setComment("Teste feedback - Bug - Update");
    existingFeedback.setStatus(FeedbackStatus.RESOLVED);
    existingFeedback.setHasScreenshot(true);
    existingFeedback.setCreatedAt(OffsetDateTime.now(clock));
    existingFeedback.setModifiedAt(null);
    UUID existingUuid = UUID.randomUUID();
    FeedbackScreenshot existingFeedbackScreenshot = new FeedbackScreenshot();
    existingFeedbackScreenshot.setId(id);
    existingFeedbackScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    existingFeedbackScreenshot.setFilename(existingUuid.toString() + "_" + "screenshot.png");
    existingFeedbackScreenshot.setSize(102400L);

    when(feedbackRepository.findById(id)).thenReturn(Optional.of(existingFeedback));
    when(feedbackScreenshotRepository.findById(id)).thenReturn(Optional.of(existingFeedbackScreenshot));
    doNothing().when(feedbackScreenshotRepository).delete(existingFeedbackScreenshot);
    doNothing().when(storageService).remove(existingFeedbackScreenshot.getFilename());

    assertDoesNotThrow(() -> feedbackScreenshotCatalogService.remove(id));
    assertThat(existingFeedback.getHasScreenshot(), is(false));
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenDeleteCalledWithAValidFeedbackAndInvalidScreenshot() {
    initClock();
    UUID id = UUID.randomUUID();
    Feedback existingFeedback = new Feedback();
    existingFeedback.setId(id);
    existingFeedback.setType(FeedbackType.BUG);
    existingFeedback.setComment("Teste feedback - Bug - Update");
    existingFeedback.setStatus(FeedbackStatus.RESOLVED);
    existingFeedback.setHasScreenshot(true);
    existingFeedback.setCreatedAt(OffsetDateTime.now(clock));
    existingFeedback.setModifiedAt(null);

    when(feedbackRepository.findById(id)).thenReturn(Optional.of(existingFeedback));
    when(feedbackScreenshotRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, 
      () -> feedbackScreenshotCatalogService.remove(id));    
  }

  @Test
  void shouldThrowValidationExceptionWhenDeleteCalledWithAInvalidFeedback() {
    UUID invalUuid = UUID.randomUUID();

    when(feedbackRepository.findById(invalUuid)).thenReturn(Optional.empty());

    assertThrows(ValidationException.class, 
      () -> feedbackScreenshotCatalogService.remove(invalUuid));    
  }

  @Test
  void shouldReturnAFeedbackScreenshotWhenSearchByIdCalledWithAValidId() {
    UUID id = UUID.randomUUID();
    UUID existingUuid = UUID.randomUUID();
    FeedbackScreenshot existingFeedbackScreenshot = new FeedbackScreenshot();
    existingFeedbackScreenshot.setId(id);
    existingFeedbackScreenshot.setContentType(MediaType.IMAGE_PNG_VALUE);
    existingFeedbackScreenshot.setFilename(existingUuid.toString() + "_" + "screenshot.png");
    existingFeedbackScreenshot.setSize(102400L);

    when(feedbackScreenshotRepository.findById(id)).thenReturn(Optional.of(existingFeedbackScreenshot));

    FeedbackScreenshot foundedFeedbackScreenshot = feedbackScreenshotCatalogService.searchById(id);
    assertThat(foundedFeedbackScreenshot, is(equalTo(existingFeedbackScreenshot)));
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenSearchByIdCalledWithAInvalidId() {
    UUID invalidId = UUID.randomUUID();

    when(feedbackScreenshotRepository.findById(invalidId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, 
      () -> feedbackScreenshotCatalogService.searchById(invalidId));    
  }
}
