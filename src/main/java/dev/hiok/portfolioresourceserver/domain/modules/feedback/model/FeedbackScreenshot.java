package dev.hiok.portfolioresourceserver.domain.modules.feedback.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class FeedbackScreenshot {
  
  @Id
  @Column(name = "feedback_id")
  private UUID id;

  @Column(nullable = false)
  private String filename;

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private Long size;
  
}
