package dev.hiok.portifolioresourceserver.domain.feedback.service;

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
import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedBack;
import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedBackStatus;
import dev.hiok.portifolioresourceserver.domain.feedback.repository.FeedBackRepository;

@Service
public class FeedbackService {
  
  @Autowired
  private FeedBackRepository repository;

  @Autowired
  private ModelMapper modelMapper;

  @Transactional
  public FeedBack create(FeedBack feedBack) {
    return repository.save(feedBack);
  }

  public Page<FeedBack> list(Pageable pageable) {
    Pageable pagingSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
      Sort.Direction.DESC, "createdAt");
    return repository.findAll(pagingSort);
  }

  public Page<FeedBack> listByStatus(FeedBackStatus status, Pageable pageable) {
    Pageable pagingSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
      Sort.Direction.DESC, "createdAt");
    return repository.findByStatus(status, pagingSort);
  }

  public FeedBack getById(UUID id) {
    return repository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Feedback not found with informed id: " + id));
  }

  @Transactional
  public FeedBack update(UUID id, FeedBack feedBack) {
    FeedBack existFeedBack = getById(id);
    modelMapper.map(feedBack, existFeedBack);
    return repository.save(existFeedBack);
  }

  @Transactional
  public void delete(UUID id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException("Feedback not found with informed id: " + id);
    }
    try {
      repository.deleteById(id);
    } catch(DataIntegrityViolationException e) {
      throw new EntityInUseException("Feedback is in use and cannot be deleted");
    }
  }

}
