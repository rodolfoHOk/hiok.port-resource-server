package dev.hiok.portfolioresourceserver.domain.modules.feedback.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackScreenshot;

public interface FeedbackScreenshotRepository extends JpaRepository<FeedbackScreenshot, UUID> {
  
}
