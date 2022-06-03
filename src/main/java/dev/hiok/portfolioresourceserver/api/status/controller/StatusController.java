package dev.hiok.portfolioresourceserver.api.status.controller;

import java.time.OffsetDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.hiok.portfolioresourceserver.api.status.model.StatusResponse;
import dev.hiok.portfolioresourceserver.api.status.openapi.StatusControllerOpenApi;

@RestController
@RequestMapping("/api/status")
public class StatusController implements StatusControllerOpenApi {

  @Override
  @GetMapping
  public StatusResponse status() {
    return StatusResponse.builder()
      .statusCode(200)
      .message("online")
      .timestamp(OffsetDateTime.now())
      .build();
  }
  
}
