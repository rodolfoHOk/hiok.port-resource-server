package dev.hiok.portfolioresourceserver.domain.feedback.model;

import java.time.OffsetDateTime;
import java.util.UUID;

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

  private FeedbackType type;

  private String comment;

  private Boolean hasScreenshot;

  private FeedbackStatus status;

  private OffsetDateTime createdAt;
  
  private OffsetDateTime modifiedAt;

}
