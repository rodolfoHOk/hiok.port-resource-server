package dev.hiok.portfolioresourceserver.domain.feedback.service;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hiok.portfolioresourceserver.domain.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackScreenshot;
import dev.hiok.portfolioresourceserver.domain.feedback.repository.FeedbackRepository;
import dev.hiok.portfolioresourceserver.domain.feedback.repository.FeedbackScreenshotRepository;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;
import dev.hiok.portfolioresourceserver.domain.service.StorageService.NewFile;

@Service
public class FeedbackScreenshotCatalogService {
  
  @Autowired
  private FeedbackScreenshotRepository feedbackScreenshotRepository;

  @Autowired
  private FeedbackRepository feedbackRepository;

  @Autowired
  private StorageService storageService;

  @Transactional
  public FeedbackScreenshot save(FeedbackScreenshot feedbackScreenshot, InputStream fileData) {
    UUID id = feedbackScreenshot.getId();
    
    Feedback existingFeedback = feedbackRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Feedback not found with informed id: " + id));

    Optional<FeedbackScreenshot> existingScreenshot = 
      feedbackScreenshotRepository.findById(id);

    String existingFilename = null;

    if (existingScreenshot.isPresent()) {
      existingFilename = existingScreenshot.get().getFilename();
    }
    
    String newFilename = storageService.generateFileName(feedbackScreenshot.getFilename());
    feedbackScreenshot.setFilename(newFilename);
    FeedbackScreenshot savedScreenshot = feedbackScreenshotRepository.save(feedbackScreenshot);
    feedbackScreenshotRepository.flush();
    
    StorageService.NewFile newFile = NewFile.builder()
      .filename(savedScreenshot.getFilename())
      .contentType(savedScreenshot.getContentType())
      .inputStream(fileData)
      .build();

    storageService.replace(existingFilename, newFile);
    
    existingFeedback.setHasScreenshot(true);

    return savedScreenshot;
  }

  @Transactional
  public void remove(UUID id) {
    Feedback existingFeedback = feedbackRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Feedback not found with informed id: " + id));

    FeedbackScreenshot existingScreenshot = this.searchById(id);

    feedbackScreenshotRepository.delete(existingScreenshot);
    feedbackScreenshotRepository.flush();

    storageService.remove(existingScreenshot.getFilename());

    existingFeedback.setHasScreenshot(false);
  }

  public FeedbackScreenshot searchById(UUID id) {
    return feedbackScreenshotRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Feedback not found with informed id: " + id));
  }
  
}
