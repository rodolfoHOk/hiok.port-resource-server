package dev.hiok.portifolioresourceserver.domain.feedback.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hiok.portifolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portifolioresourceserver.domain.feedback.model.Feedback;
import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedbackStatus;
import dev.hiok.portifolioresourceserver.domain.feedback.repository.FeedbackRepository;

@Service
public class UpdateFeedbackStatusService {
  
  @Autowired
  private FeedbackRepository feedbackRepository;
  
  @Transactional
  public Feedback updateStatus(UUID id, FeedbackStatus status) {
    Feedback foundFeedback = feedbackRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Feedback not found with informed id: " + id)
    );

    foundFeedback.setStatus(status);

    return foundFeedback;
  }
}
