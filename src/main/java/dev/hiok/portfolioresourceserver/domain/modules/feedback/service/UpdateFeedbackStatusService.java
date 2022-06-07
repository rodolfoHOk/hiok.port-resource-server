package dev.hiok.portfolioresourceserver.domain.modules.feedback.service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.repository.FeedbackRepository;

@Service
public class UpdateFeedbackStatusService {
  
  @Autowired
  private FeedbackRepository feedbackRepository;

  @Autowired
  private Clock clock;
  
  @Transactional
  public Feedback updateStatus(UUID id, FeedbackStatus status) {
    Feedback foundFeedback = feedbackRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Feedback not found with informed id: " + id)
    );

    foundFeedback.setStatus(status);
    foundFeedback.setModifiedAt(OffsetDateTime.now(clock));

    return foundFeedback;
  }
}
