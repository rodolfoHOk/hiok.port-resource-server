package dev.hiok.portfolioresourceserver.domain.modules.feedback.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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

import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackType;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.repository.FeedbackRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateFeedbackStatusServiceTest {
  
  @Mock
  private FeedbackRepository feedbackRepository;

  @Mock
  private Clock clock;

  @InjectMocks
  private UpdateFeedbackStatusService updateFeedbackStatusService;

  private void initClock() {
    Clock fixedClock = Clock.fixed(Instant.parse("2022-01-01T00:00:00Z"), ZoneId.of("UTC"));
    doReturn(fixedClock.instant()).when(clock).instant();
    doReturn(fixedClock.getZone()).when(clock).getZone();
  }

  @Test
  void shouldUpdateFeedbackStatusWhenUpdateStatusCalledWithAValidId() {
    initClock();
    UUID id = UUID.randomUUID();
    Feedback expectedFoundFeedback = new Feedback();
    expectedFoundFeedback.setId(id);
    expectedFoundFeedback.setType(FeedbackType.BUG);
    expectedFoundFeedback.setComment("Teste feedback - Bug - Update");
    expectedFoundFeedback.setStatus(FeedbackStatus.RESOLVED);
    expectedFoundFeedback.setHasScreenshot(false);
    expectedFoundFeedback.setCreatedAt(OffsetDateTime.now(clock));
    expectedFoundFeedback.setModifiedAt(null);
    Feedback expectedUpdatedFeedback = new Feedback();
    expectedUpdatedFeedback.setId(id);
    expectedUpdatedFeedback.setType(FeedbackType.BUG);
    expectedUpdatedFeedback.setComment("Teste feedback - Bug - Update");
    expectedUpdatedFeedback.setStatus(FeedbackStatus.RESOLVED);
    expectedUpdatedFeedback.setHasScreenshot(false);
    expectedUpdatedFeedback.setCreatedAt(OffsetDateTime.now(clock));
    expectedUpdatedFeedback.setModifiedAt(OffsetDateTime.now(clock));

    when(feedbackRepository.findById(id)).thenReturn(Optional.of(expectedFoundFeedback));
    
    Feedback updatedFeedback = updateFeedbackStatusService.updateStatus(id, FeedbackStatus.RESOLVED);

    assertThat(updatedFeedback, is(equalTo(expectedUpdatedFeedback)));
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenUpdateStatusCalledWithAInvalidId() {
    UUID invalidId = UUID.randomUUID();

    when(feedbackRepository.findById(invalidId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, 
      () -> updateFeedbackStatusService.updateStatus(invalidId, any()));
  }
}
