package dev.hiok.portfolioresourceserver.api.status.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import dev.hiok.portfolioresourceserver.api.exceptionHandler.ApiExceptionHandler;

@ExtendWith(MockitoExtension.class)
public class StatusControllerTest {

  @InjectMocks
  private StatusController statusController;

  private MockMvc mockMvc;

  private static final String BASE_URL = "/api/status";

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(statusController)
      .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
      .setControllerAdvice(new ApiExceptionHandler())
      .build();
  }

  @Test
  void shouldReturnOkAndAPIStatusWhenGetStatusIsCalled() throws Exception {
    mockMvc.perform(
      get(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.statusCode", is(equalTo(200))))
      .andExpect(jsonPath("$.message", is(equalTo("online"))))
    ;
  }
}
