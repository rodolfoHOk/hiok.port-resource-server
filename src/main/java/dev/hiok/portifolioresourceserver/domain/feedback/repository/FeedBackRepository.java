package dev.hiok.portifolioresourceserver.domain.feedback.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedBack;
import dev.hiok.portifolioresourceserver.domain.feedback.model.FeedBackStatus;

public interface FeedBackRepository extends JpaRepository<FeedBack, UUID> {
  
  Page<FeedBack> findByStatus(FeedBackStatus status, Pageable pageable);

}
