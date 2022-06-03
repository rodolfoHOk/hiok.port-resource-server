package dev.hiok.portfolioresourceserver.domain.modules.feedback.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "feedback_feedbacks")
@Data
public class Feedback {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false)
  private FeedbackType type;

  @Column(nullable = false)
  private String comment;

  @Column(nullable = false)
  private Boolean hasScreenshot;

  @Column(nullable = false)
  private FeedbackStatus status;

  @Column(nullable = false)
  private OffsetDateTime createdAt;
  
  private OffsetDateTime modifiedAt;

}
