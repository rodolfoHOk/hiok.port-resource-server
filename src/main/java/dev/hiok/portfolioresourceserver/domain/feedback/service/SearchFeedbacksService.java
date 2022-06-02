package dev.hiok.portfolioresourceserver.domain.feedback.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import dev.hiok.portfolioresourceserver.domain.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.feedback.repository.FeedbackRepository;

public class SearchFeedbacksService {
  
  @Autowired
  private FeedbackRepository feedbackRepository;
  
  public Page<Feedback> search(FeedbackStatus status, Pageable pageable) {
    Pageable pagingSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
      Sort.Direction.DESC, "createdAt");

    if (status != null) {
      return feedbackRepository.findByStatus(status, pagingSort);
    }

    return feedbackRepository.findAll(pagingSort);
  }
  
}
