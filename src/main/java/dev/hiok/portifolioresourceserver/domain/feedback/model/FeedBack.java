package dev.hiok.portifolioresourceserver.domain.feedback.model;

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
public class FeedBack {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  private FeedBackType type;
  private String comment;
  private String screenshotUrl;
  private FeedBackStatus status;
  private OffsetDateTime createdAt;
  private OffsetDateTime modifiedAt;

}
