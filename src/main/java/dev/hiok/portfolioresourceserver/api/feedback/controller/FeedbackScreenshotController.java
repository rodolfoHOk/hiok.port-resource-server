package dev.hiok.portfolioresourceserver.api.feedback.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.hiok.portfolioresourceserver.api.feedback.assembler.FeedbackScreenshotResponseAssembler;
import dev.hiok.portfolioresourceserver.api.feedback.model.request.FeedbackScreenshotRequest;
import dev.hiok.portfolioresourceserver.api.feedback.model.response.FeedbackScreenshotResponse;
import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portfolioresourceserver.domain.feedback.model.FeedbackScreenshot;
import dev.hiok.portfolioresourceserver.domain.feedback.service.FeedbackScreenshotCatalogService;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;

@RestController
@RequestMapping("/v1/feedbacks/{id}/screenshot")
public class FeedbackScreenshotController {

  @Autowired
  private FeedbackScreenshotCatalogService feedbackScreenshotCatalogService;

  @Autowired
  private FeedbackScreenshotResponseAssembler feedbackScreenshotResponseAssembler;

  @Autowired
  private StorageService storageService;

  @GetMapping(produces = MediaType.ALL_VALUE)
  public ResponseEntity<?> search(
    @PathVariable UUID id, 
    @RequestHeader(name = "Accept") String acceptHeader) 
    throws HttpMediaTypeNotAcceptableException {
    
    if (acceptHeader.equals(MediaType.APPLICATION_JSON_VALUE)) {
      FeedbackScreenshot foundScreenshot = feedbackScreenshotCatalogService.searchById(id);
      
      return ResponseEntity.ok(
        feedbackScreenshotResponseAssembler.toRepresentationModel(foundScreenshot));
    }

    try {
      FeedbackScreenshot screenshot = feedbackScreenshotCatalogService.searchById(id);

      MediaType screenshotMediaType = MediaType.parseMediaType(screenshot.getContentType());
      List<MediaType> acceptMediaTypes = MediaType.parseMediaTypes(acceptHeader);

      this.verifyMediaTypeCompatibility(screenshotMediaType, acceptMediaTypes);

      StorageService.RecoveredFile recoveredFile = storageService.recover(screenshot.getFilename());

      if (recoveredFile.hasURL()) {
        return ResponseEntity.status(HttpStatus.FOUND)
          .header(HttpHeaders.LOCATION, recoveredFile.getUrl())
          .build();
      } else {
        return ResponseEntity.ok()
          .contentType(screenshotMediaType)
          .body(new InputStreamResource(recoveredFile.getInputStream()));
      }

    } catch (EntityNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public FeedbackScreenshotResponse replace(
    @PathVariable UUID id,
    @Valid FeedbackScreenshotRequest screenshotRequest,
    @RequestPart(required = true) MultipartFile file) throws IOException {

    FeedbackScreenshot screenshot = new FeedbackScreenshot();
    screenshot.setId(id);
    screenshot.setFilename(file.getOriginalFilename());
    screenshot.setContentType(file.getContentType());
    screenshot.setSize(file.getSize());

    FeedbackScreenshot savedScreenshot = 
      feedbackScreenshotCatalogService.save(screenshot, file.getInputStream());

    return feedbackScreenshotResponseAssembler.toRepresentationModel(savedScreenshot);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remove(@PathVariable UUID id) {
    feedbackScreenshotCatalogService.remove(id);    
  }

  private void verifyMediaTypeCompatibility(
    MediaType screenshotMediaType, List<MediaType> acceptMediaTypes) 
    throws HttpMediaTypeNotAcceptableException {
    
    boolean isCompatible = acceptMediaTypes.stream()
      .anyMatch(acceptMediaType -> acceptMediaType.isCompatibleWith(screenshotMediaType));

    if (!isCompatible) throw new HttpMediaTypeNotAcceptableException(acceptMediaTypes);
  }
}
