package dev.hiok.portfolioresourceserver.domain.modules.feedback.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import dev.hiok.portfolioresourceserver.domain.exception.EntityInUseException;
import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackType;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.repository.FeedbackRepository;

@ExtendWith(MockitoExtension.class)
public class FeedbackRegistrationServiceTest {
  
  @Mock
  private FeedbackRepository feedbackRepository;

  @Mock
  private Clock clock;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private FeedbackRegistrationService feedbackRegistrationService;

  private void initClock() {
    Clock fixedClock = Clock.fixed(Instant.parse("2022-01-01T00:00:00Z"), ZoneId.of("UTC"));
    doReturn(fixedClock.instant()).when(clock).instant();
    doReturn(fixedClock.getZone()).when(clock).getZone();
  }

  @Test
  void shouldReturnCreatedFeedbackWhenSaveCalled() {
    initClock();

    // given
    Feedback feedbackToSave = new Feedback();
    feedbackToSave.setType(FeedbackType.BUG);
    feedbackToSave.setComment("Teste feedback - Bug");
    Feedback expectedCreatedFeedback = new Feedback();
    expectedCreatedFeedback.setType(FeedbackType.BUG);
    expectedCreatedFeedback.setComment("Teste feedback - Bug");
    expectedCreatedFeedback.setStatus(FeedbackStatus.PENDING);
    expectedCreatedFeedback.setHasScreenshot(false);
    expectedCreatedFeedback.setCreatedAt(OffsetDateTime.now(clock));

    // when
    when(feedbackRepository.save(expectedCreatedFeedback)).thenReturn(expectedCreatedFeedback);

    // then
    Feedback createdFeedback = feedbackRegistrationService.create(feedbackToSave);
    assertThat(createdFeedback.getComment(), is(equalTo(expectedCreatedFeedback.getComment())));
    assertThat(createdFeedback.getType(), is(equalTo(expectedCreatedFeedback.getType())));
    assertThat(createdFeedback.getStatus(), is(equalTo(expectedCreatedFeedback.getStatus())));
    assertThat(createdFeedback.getHasScreenshot(), is(equalTo(expectedCreatedFeedback.getHasScreenshot())));
  }


  @Test
  void shouldReturnAFeedbackWhenSearchByIdCalledWithAValidId() {
    initClock();
    UUID id = UUID.randomUUID();

    Feedback expectedFoundFeedback = new Feedback();
    expectedFoundFeedback.setId(id);
    expectedFoundFeedback.setType(FeedbackType.BUG);
    expectedFoundFeedback.setComment("Teste feedback - Bug");
    expectedFoundFeedback.setStatus(FeedbackStatus.PENDING);
    expectedFoundFeedback.setHasScreenshot(false);
    expectedFoundFeedback.setCreatedAt(OffsetDateTime.now(clock));
    expectedFoundFeedback.setModifiedAt(null);

    when(feedbackRepository.findById(id)).thenReturn(Optional.of(expectedFoundFeedback));

    Feedback foundFeedback = feedbackRegistrationService.searchById(id);

    assertThat(foundFeedback, is(equalTo(expectedFoundFeedback)));
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenSearchByIdCalledWithAInvalidId() {
    UUID invalidId = UUID.randomUUID();

    when(feedbackRepository.findById(invalidId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, 
      () -> feedbackRegistrationService.searchById(invalidId));
  }

  @Test
  void shouldUpdateAFeedbackWhenUpdateCalledWithAValidId() {
    initClock();
    UUID id = UUID.randomUUID();
    Feedback feedbackToUpdate = new Feedback();
    feedbackToUpdate.setType(FeedbackType.BUG);
    feedbackToUpdate.setComment("Teste feedback - Bug - Update");
    Feedback expectedUpdatedFeedback = new Feedback();
    expectedUpdatedFeedback.setId(id);
    expectedUpdatedFeedback.setType(FeedbackType.BUG);
    expectedUpdatedFeedback.setComment("Teste feedback - Bug - Update");
    expectedUpdatedFeedback.setStatus(FeedbackStatus.PENDING);
    expectedUpdatedFeedback.setHasScreenshot(false);
    expectedUpdatedFeedback.setCreatedAt(OffsetDateTime.now(clock));
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

    when(feedbackRepository.findById(id)).thenReturn(Optional.of(expectedUpdatedFeedback));
    when(feedbackRepository.save(expectedUpdatedFeedback)).thenReturn(expectedUpdatedFeedback);

    Feedback updatedFeedback = feedbackRegistrationService.update(id, feedbackToUpdate);
    assertThat(updatedFeedback, is(equalTo(expectedUpdatedFeedback)));
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenUpdateCalledWithAInvalidId() {
    UUID invalidId = UUID.randomUUID();
    Feedback feedbackToUpdate = new Feedback();
    feedbackToUpdate.setType(FeedbackType.BUG);
    feedbackToUpdate.setComment("Teste feedback - Bug - Update");

    when(feedbackRepository.findById(invalidId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, 
      () -> feedbackRegistrationService.update(invalidId, feedbackToUpdate));
  }

  @Test
  void shouldDeleteAFeedbackWhenDeleteCalledWithAValidId() {
    UUID id = UUID.randomUUID();

    when(feedbackRepository.existsById(id)).thenReturn(true);
    doNothing().when(feedbackRepository).deleteById(id);

    assertDoesNotThrow(() -> feedbackRegistrationService.delete(id));
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenDeleteCalledWithAInvalidId() {
    UUID invalidId = UUID.randomUUID();

    when(feedbackRepository.existsById(invalidId)).thenReturn(false);

    assertThrows(EntityNotFoundException.class, 
      () -> feedbackRegistrationService.delete(invalidId));
  }

  @Test
  void shouldThrowEntityInUseExceptionWhenDeleteCalledAndFeedbackHasAScreenshotAssociated() {
    UUID id = UUID.randomUUID();

    when(feedbackRepository.existsById(id)).thenReturn(true);
    doThrow(DataIntegrityViolationException.class).when(feedbackRepository).deleteById(id);

    assertThrows(EntityInUseException.class, 
      () -> feedbackRegistrationService.delete(id));
  }
}
