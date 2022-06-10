package dev.hiok.portfolioresourceserver.domain.modules.feedback.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackType;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.repository.FeedbackRepository;

@ExtendWith(MockitoExtension.class)
public class SearchFeedbacksServiceTest {
  
  @Mock
  private FeedbackRepository feedbackRepository;

  @InjectMocks
  private SearchFeedbacksService searchFeedbacksService;

  @Test
  void shouldReturnAPagedListOfFeedbacksWhenSearchCalledWithStatus() {
    Pageable pageable = PageRequest.of(0, 9);
    Pageable pagingSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
      Sort.Direction.DESC, "createdAt");
    UUID id = UUID.randomUUID();
    Feedback foundedFeedback = createFoundedFeedback(id, FeedbackStatus.PENDING);
    Page<Feedback> expectedPagedFeedbacks = new PageImpl<>(List.of(foundedFeedback));
    
    when(feedbackRepository.findByStatus(FeedbackStatus.PENDING, pagingSort))
      .thenReturn(expectedPagedFeedbacks);

    Page<Feedback> pagedFeedbacks = searchFeedbacksService.search(FeedbackStatus.PENDING, pageable);
    assertThat(pagedFeedbacks, is(equalTo(expectedPagedFeedbacks)));
  }

  @Test
  void shouldReturnAPagedListOfFeedbacksWhenSearchCalledWithoutStatus() {
    Pageable pageable = PageRequest.of(0, 9);
    Pageable pagingSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
      Sort.Direction.DESC, "createdAt");
    UUID id1 = UUID.randomUUID();
    Feedback foundedFeedback1 = createFoundedFeedback(id1, FeedbackStatus.UNDER_ANALYSIS);
    UUID id2 = UUID.randomUUID();
    Feedback foundedFeedback2 = createFoundedFeedback(id2, FeedbackStatus.RESOLVED);
    Page<Feedback> expectedPagedFeedbacks = new PageImpl<>(List.of(foundedFeedback1, foundedFeedback2));
    
    when(feedbackRepository.findAll(pagingSort)).thenReturn(expectedPagedFeedbacks);

    Page<Feedback> pagedFeedbacks = searchFeedbacksService.search(null, pageable);
    assertThat(pagedFeedbacks, is(equalTo(expectedPagedFeedbacks)));
  }

  private Feedback createFoundedFeedback(UUID id, FeedbackStatus status) {
    Feedback foundedFeedback = new Feedback();
    foundedFeedback.setId(id);
    foundedFeedback.setType(FeedbackType.IDEA);
    foundedFeedback.setComment("Teste 01 - IDEA");
    foundedFeedback.setStatus(status);
    foundedFeedback.setHasScreenshot(false);
    foundedFeedback.setCreatedAt(OffsetDateTime.now());
    foundedFeedback.setModifiedAt(null);
    return foundedFeedback;
  }
}
