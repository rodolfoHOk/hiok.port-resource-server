package dev.hiok.portfolioresourceserver.domain.modules.feedback.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hiok.portfolioresourceserver.domain.exception.EntityInUseException;
import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.repository.FeedbackRepository;

@Service
public class FeedbackRegistrationService {
  
  @Autowired
  private FeedbackRepository feedbackRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Transactional
  public Feedback create(Feedback feedBack) {
    feedBack.setStatus(FeedbackStatus.PENDING);
    feedBack.setCreatedAt(OffsetDateTime.now());
    
    return feedbackRepository.save(feedBack);
  }

  public Feedback searchById(UUID id) {
    return feedbackRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Feedback not found with informed id: " + id));
  }

  @Transactional
  public Feedback update(UUID id, Feedback feedBack) {
    Feedback existFeedBack = searchById(id);
    
    modelMapper.map(feedBack, existFeedBack);
    existFeedBack.setModifiedAt(OffsetDateTime.now());
    
    return feedbackRepository.save(existFeedBack);
  }

  @Transactional
  public void delete(UUID id) {
    if (!feedbackRepository.existsById(id)) {
      throw new EntityNotFoundException("Feedback not found with informed id: " + id);
    }
    
    try {
      feedbackRepository.deleteById(id);
    } catch(DataIntegrityViolationException e) {
      throw new EntityInUseException("Feedback is in use and cannot be deleted");
    }
  }

}
