package dev.hiok.portfolioresourceserver.api.status.model;

import java.time.OffsetDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatusResponse {
  
  @ApiModelProperty(example = "200")
  private Integer statusCode;

  @ApiModelProperty(example = "online")
  private String message;

  @ApiModelProperty(example = "2022-06-03T13:30:52.502356626-03:00")
  private OffsetDateTime timestamp;

}
