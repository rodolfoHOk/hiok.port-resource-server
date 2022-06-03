package dev.hiok.portfolioresourceserver.domain.modules.feedback.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
  
  Page<Feedback> findByStatus(FeedbackStatus status, Pageable pageable);

}
