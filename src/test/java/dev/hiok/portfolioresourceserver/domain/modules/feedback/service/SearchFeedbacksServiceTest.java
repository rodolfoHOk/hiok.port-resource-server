package dev.hiok.portfolioresourceserver.domain.modules.feedback.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.Feedback;
import dev.hiok.portfolioresourceserver.domain.modules.feedback.model.FeedbackStatus;
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
    Page<Feedback> expectedPagedFeedbacks = mock(Page.class);
    
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
    Page<Feedback> expectedPagedFeedbacks = mock(Page.class);
    
    when(feedbackRepository.findAll(pagingSort)).thenReturn(expectedPagedFeedbacks);

    Page<Feedback> pagedFeedbacks = searchFeedbacksService.search(null, pageable);
    assertThat(pagedFeedbacks, is(equalTo(expectedPagedFeedbacks)));
  }
}
