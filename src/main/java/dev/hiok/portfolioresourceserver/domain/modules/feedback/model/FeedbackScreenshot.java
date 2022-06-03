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

  private String filename;

  private String contentType;

  private Long size;
  
}
