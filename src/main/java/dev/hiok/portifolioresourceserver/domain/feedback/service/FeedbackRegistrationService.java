package dev.hiok.portifolioresourceserver.domain.feedback.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hiok.portifolioresourceserver.domain.exception.EntityInUseException;
import dev.hiok.portifolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portifolioresourceserver.domain.feedback.model.Feedback;
import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedbackStatus;
import dev.hiok.portifolioresourceserver.domain.feedback.repository.FeedbackRepository;

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

  public Page<Feedback> search(FeedbackStatus status, Pageable pageable) {
    Pageable pagingSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
      Sort.Direction.DESC, "createdAt");

    if (status != null) {
      return feedbackRepository.findByStatus(status, pagingSort);
    }

    return feedbackRepository.findAll(pagingSort);
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
